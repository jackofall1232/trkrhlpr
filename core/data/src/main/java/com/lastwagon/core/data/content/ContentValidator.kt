package com.lastwagon.core.data.content

import com.lastwagon.core.model.VerificationStatus
import java.time.LocalDate

/**
 * Mechanical content gate. [structuralProblems] must always be empty for a well-formed asset;
 * [shipBlockers] enforces "no non-VERIFIED item ships". The build/test fails on either, so
 * unverified or malformed content cannot reach production.
 */
object ContentValidator {
    const val SUPPORTED_SCHEMA_VERSION = 1
    private val SLUG = Regex("^[a-z0-9]+(?:[.-][a-z0-9]+)*$")
    private val CONTENT_VERSION = Regex("^\\d{4}\\.\\d{2}\\.\\d+$")
    private val PLACEHOLDERS = listOf("TODO", "TBD", "FIXME", "XXX", "PLACEHOLDER", "SAMPLE", "WIP", "DRAFT", "[VERIFY]")
    private val VERIFIED_FLOOR = LocalDate.of(2020, 1, 1)
    /** A VERIFIED item must rest on at least one public-domain primary source, not a manual-only ref. */
    private val PRIMARY_CITATION = setOf(CitationType.CFR, CitationType.FMVSS, CitationType.FMCSA_GUIDE)

    /** Human-readable problems; empty means structurally valid. `today` is injected for testability. */
    fun structuralProblems(content: InspectionContent, today: LocalDate, checkDates: Boolean = true): List<String> {
        val problems = mutableListOf<String>()
        fun flag(cond: Boolean, msg: String) { if (cond) problems += msg }

        flag(content.schemaVersion != SUPPORTED_SCHEMA_VERSION,
            "schemaVersion ${content.schemaVersion} != supported $SUPPORTED_SCHEMA_VERSION")
        flag(!CONTENT_VERSION.matches(content.contentVersion),
            "contentVersion '${content.contentVersion}' is not YYYY.MM.N")
        problems += dateProblem("header verifiedOn", content.verifiedOn, today, checkDates)
        flag(content.expectedItemCount != content.items.size,
            "expectedItemCount ${content.expectedItemCount} != actual item count ${content.items.size}")
        problems += placeholderIn("contentVersion", content.contentVersion)
        val headerDate = runCatching { LocalDate.parse(content.verifiedOn) }.getOrNull()

        // Categories
        flag(content.categories.isEmpty(), "no categories")
        content.categories.groupBy { it.id }.filterValues { it.size > 1 }.keys
            .let { flag(it.isNotEmpty(), "duplicate category ids: $it") }
        content.categories.groupBy { it.sequence }.filterValues { it.size > 1 }.keys
            .let { flag(it.isNotEmpty(), "duplicate category sequences: $it") }
        content.categories.forEach { c ->
            flag(c.title.isBlank(), "category[${c.id}]: blank title")
            flag(c.sequence < 1, "category[${c.id}]: non-positive sequence")
            problems += placeholderIn("category[${c.id}].title", c.title)
        }
        val catIds = content.categories.map { it.id }.toSet()

        // Items
        content.items.groupBy { it.id }.filterValues { it.size > 1 }.keys
            .let { flag(it.isNotEmpty(), "duplicate item ids: $it") }
        content.items.forEach { item ->
            val at = "item[${item.id}]"
            flag(!SLUG.matches(item.id), "$at: id is not a valid slug")
            flag(item.categoryId !in catIds, "$at: categoryId '${item.categoryId}' has no category")
            flag(item.name.isBlank(), "$at: blank name")
            flag(item.inspectFor.isBlank(), "$at: blank inspectFor")
            flag(item.defects.none { it.isNotBlank() }, "$at: no non-blank defect")

            // Provenance
            flag(item.citations.none { it.ref.isNotBlank() }, "$at: no citation with a non-blank ref")
            item.citations.forEach { problems += citationProblems(at, it) }
            if (item.verificationStatus == VerificationStatus.VERIFIED) {
                flag(item.citations.none { it.type in PRIMARY_CITATION && it.ref.isNotBlank() },
                    "$at: VERIFIED requires a public-primary citation (CFR/FMVSS/FMCSA_GUIDE)")
                flag(item.verifiedOn.isNullOrBlank(), "$at: VERIFIED item is missing verifiedOn")
            }
            item.verifiedOn?.let { vo ->
                problems += dateProblem("$at verifiedOn", vo, today, checkDates)
                val d = runCatching { LocalDate.parse(vo) }.getOrNull()
                flag(d != null && d.isBefore(VERIFIED_FLOOR), "$at: verifiedOn '$vo' is implausibly old")
                flag(d != null && headerDate != null && d.isAfter(headerDate),
                    "$at: verifiedOn '$vo' is after the header verifiedOn '${content.verifiedOn}'")
            }
            (listOf(item.name, item.inspectFor) + item.defects + listOfNotNull(item.notes))
                .forEach { problems += placeholderIn(at, it) }
        }
        // Every declared category must be used.
        (catIds - content.items.map { it.categoryId }.toSet())
            .forEach { problems += "category '$it' has no items" }
        // Sequences must be distinct and positive (final 1..N contiguity is importCompletenessProblems).
        content.items.groupBy { it.sequence }.filterValues { it.size > 1 }.keys
            .let { flag(it.isNotEmpty(), "duplicate item sequences: $it") }
        flag(content.items.any { it.sequence < 1 }, "non-positive item sequence present")

        return problems.filter { it.isNotEmpty() }
    }

    /** Items that must not ship because they are not VERIFIED. */
    fun shipBlockers(content: InspectionContent): List<ContentItem> =
        content.items.filter { it.verificationStatus != VerificationStatus.VERIFIED }

    /** Extra gate applied only at final import: the corpus is complete and correctly numbered. */
    fun importCompletenessProblems(content: InspectionContent, expectedFullCount: Int): List<String> {
        val problems = mutableListOf<String>()
        if (content.items.size != expectedFullCount)
            problems += "corpus has ${content.items.size} items, expected $expectedFullCount"
        val seqs = content.items.map { it.sequence }.sorted()
        if (seqs != (1..expectedFullCount).toList())
            problems += "item sequences are not contiguous 1..$expectedFullCount"
        return problems
    }

    private fun citationProblems(at: String, c: Citation): List<String> = buildList {
        when (c.type) {
            CitationType.CFR -> if (!c.ref.contains("CFR") && !c.ref.contains("§"))
                add("$at: CFR citation ref '${c.ref}' does not look like a CFR reference")
            CitationType.FMVSS -> if (!c.ref.contains("571") && !c.ref.uppercase().contains("FMVSS"))
                add("$at: FMVSS citation ref '${c.ref}' does not reference 571")
            else -> {}
        }
        c.url?.let { u ->
            if (!u.startsWith("https://")) add("$at: citation url '$u' is not https")
            if (c.type == CitationType.CFR && !u.contains("ecfr.gov") && !u.contains("govinfo.gov"))
                add("$at: CFR citation url '$u' is not an eCFR/GovInfo link")
        }
    }

    private fun placeholderIn(where: String, text: String): String =
        PLACEHOLDERS.firstOrNull { text.uppercase().contains(it) }
            ?.let { "$where: contains placeholder marker '$it'" } ?: ""

    private fun dateProblem(label: String, value: String, today: LocalDate, checkFuture: Boolean): String = try {
        val d = LocalDate.parse(value)
        if (checkFuture && d.isAfter(today)) "$label '$value' is in the future" else ""
    } catch (e: Exception) {
        "$label '$value' is not an ISO date (yyyy-MM-dd)"
    }
}
