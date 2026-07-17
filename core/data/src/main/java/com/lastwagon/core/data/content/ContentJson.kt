package com.lastwagon.core.data.content

import com.lastwagon.core.model.ApplicabilityFlag
import com.lastwagon.core.model.VerificationStatus
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/**
 * Strict DOM parser for the inspection content asset. Fails loudly with a field path — unknown
 * fields, missing fields, JSON nulls in required strings, wrong types (a number/bool where a
 * string is expected, a quoted int), and unknown enum values all throw [ContentFormatException].
 * Structural/semantic checks (counts, ids, ship gate) live in [ContentValidator].
 */
class ContentFormatException(message: String) : IllegalArgumentException(message)

object ContentJson {
    private val json = Json { isLenient = false }

    fun parse(text: String): InspectionContent {
        val root = try {
            json.parseToJsonElement(text)
        } catch (e: Exception) {
            throw ContentFormatException("Content asset is not valid JSON: ${e.message}")
        }.obj("<root>")
        root.requireKeys("<root>", ROOT_KEYS)
        return InspectionContent(
            schemaVersion = root.int("schemaVersion", "<root>"),
            contentVersion = root.str("contentVersion", "<root>"),
            verifiedOn = root.str("verifiedOn", "<root>"),
            expectedItemCount = root.int("expectedItemCount", "<root>"),
            categories = root.arr("categories", "<root>").mapIndexed { i, e -> e.toCategory("categories[$i]") },
            items = root.arr("items", "<root>").mapIndexed { i, e -> e.toItem("items[$i]") },
        )
    }

    private fun JsonElement.toCategory(ctx0: String): ContentCategory {
        val o = obj(ctx0)
        o.requireKeys(ctx0, CATEGORY_KEYS)
        val id = o.str("id", ctx0)
        val ctx = "category[$id]"
        return ContentCategory(id, o.str("title", ctx), o.int("sequence", ctx))
    }

    private fun JsonElement.toItem(ctx0: String): ContentItem {
        val o = obj(ctx0)
        o.requireKeys(ctx0, ITEM_KEYS)
        val id = o.str("id", ctx0)
        val ctx = "item[$id]"
        return ContentItem(
            id = id,
            categoryId = o.str("categoryId", ctx),
            sequence = o.int("sequence", ctx),
            name = o.str("name", ctx),
            inspectFor = o.str("inspectFor", ctx),
            defects = o.arr("defects", ctx).mapIndexed { i, e -> e.asString("$ctx.defects[$i]") },
            applicability = o.arr("applicability", ctx).mapIndexed { i, e ->
                val v = e.asString("$ctx.applicability[$i]")
                ApplicabilityFlag.entries.firstOrNull { it.name == v }
                    ?: throw ContentFormatException("$ctx: unknown applicability flag '$v'")
            }.toSet(),
            citations = o.arr("citations", ctx).mapIndexed { i, e -> e.toCitation("$ctx.citations[$i]") },
            verificationStatus = o.str("verificationStatus", ctx).let { v ->
                VerificationStatus.entries.firstOrNull { it.name == v }
                    ?: throw ContentFormatException("$ctx: unknown verificationStatus '$v'")
            },
            verifiedOn = o.strOrNull("verifiedOn", ctx),
            notes = o.strOrNull("notes", ctx),
        )
    }

    private fun JsonElement.toCitation(ctx: String): Citation {
        val o = obj(ctx)
        o.requireKeys(ctx, CITATION_KEYS)
        val type = o.str("type", ctx).let { v ->
            CitationType.entries.firstOrNull { it.name == v }
                ?: throw ContentFormatException("$ctx: unknown citation type '$v'")
        }
        return Citation(type, o.str("ref", ctx), o.strOrNull("url", ctx))
    }

    // --- strict helpers ---
    private fun JsonElement.obj(ctx: String): JsonObject =
        this as? JsonObject ?: throw ContentFormatException("$ctx: expected a JSON object")

    private fun JsonElement.asString(ctx: String): String {
        val p = this as? JsonPrimitive ?: throw ContentFormatException("$ctx: expected a string")
        if (p is JsonNull || !p.isString) throw ContentFormatException("$ctx: expected a string")
        return p.content
    }

    private fun JsonObject.requireKeys(ctx: String, allowed: Set<String>) {
        val unknown = keys - allowed
        if (unknown.isNotEmpty()) throw ContentFormatException("$ctx: unknown field(s) $unknown")
    }
    private fun JsonObject.str(key: String, ctx: String): String =
        (this[key] ?: throw ContentFormatException("$ctx: missing '$key'")).asString("$ctx.$key")
    private fun JsonObject.strOrNull(key: String, ctx: String): String? {
        val el = this[key] ?: return null
        return if (el is JsonNull) null else el.asString("$ctx.$key")
    }
    private fun JsonObject.int(key: String, ctx: String): Int {
        val el = this[key] ?: throw ContentFormatException("$ctx: missing '$key'")
        val p = el as? JsonPrimitive ?: throw ContentFormatException("$ctx: '$key' must be an integer")
        if (p is JsonNull || p.isString) throw ContentFormatException("$ctx: '$key' must be an unquoted integer")
        return p.content.toIntOrNull() ?: throw ContentFormatException("$ctx: '$key' is not an integer")
    }
    private fun JsonObject.arr(key: String, ctx: String): JsonArray {
        val el = this[key] ?: throw ContentFormatException("$ctx: missing '$key'")
        return el as? JsonArray ?: throw ContentFormatException("$ctx: '$key' must be an array")
    }

    private val ROOT_KEYS = setOf("schemaVersion", "contentVersion", "verifiedOn", "expectedItemCount", "categories", "items")
    private val CATEGORY_KEYS = setOf("id", "title", "sequence")
    private val ITEM_KEYS = setOf("id", "categoryId", "sequence", "name", "inspectFor", "defects",
        "applicability", "citations", "verificationStatus", "verifiedOn", "notes")
    private val CITATION_KEYS = setOf("type", "ref", "url")
}
