package com.lastwagon.core.data.content

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.io.File
import java.time.LocalDate

/**
 * The build-time content gate. Parses the ACTUAL shipped asset through the real parser +
 * validator, and proves the gate rejects the failure modes (unverified, malformed, unknown
 * fields). If this test is red, unverified/broken content must not ship.
 */
class ContentPipelineTest {
    private val today = LocalDate.of(2026, 7, 17)

    private fun assetText(): String {
        // Unit tests run with the module dir as the working directory.
        val f = File("src/main/assets/${ContentLoader.DEFAULT_ASSET}")
        assertTrue("content asset not found at ${f.absolutePath}", f.exists())
        return f.readText()
    }

    @Test fun shippedAssetParsesIsStructurallyValidAndAllVerified() {
        val content = ContentJson.parse(assetText())
        val problems = ContentValidator.structuralProblems(content, today)
        assertEquals("structural problems: $problems", emptyList<String>(), problems)
        assertTrue("all shipped items must be VERIFIED; blockers=${ContentValidator.shipBlockers(content).map { it.id }}",
            ContentValidator.shipBlockers(content).isEmpty())
        assertEquals(content.expectedItemCount, content.items.size)
    }

    // --- negative cases: the gate must catch these ---

    private val validItem = """
        {"id":"c.item","categoryId":"c","sequence":1,"name":"Item","inspectFor":"check it",
         "defects":["d"],"applicability":[],"citations":[{"type":"CFR","ref":"49 CFR 1","url":null}],
         "verificationStatus":"VERIFIED","verifiedOn":"2026-07-17","notes":null}
    """.trimIndent()

    private fun doc(items: String, count: Int = 1, categories: String = DEFAULT_CATEGORY) = """
        {"schemaVersion":1,"contentVersion":"2026.07.0","verifiedOn":"2026-07-17","expectedItemCount":$count,
         "categories":[$categories],"items":[$items]}
    """.trimIndent()

    private val DEFAULT_CATEGORY = """{"id":"c","title":"C","sequence":1}"""

    private fun expectFormatError(json: String) = try {
        ContentJson.parse(json); fail("expected ContentFormatException"); Unit
    } catch (e: ContentFormatException) { Unit }

    @Test fun baselineItemIsValid() {
        val problems = ContentValidator.structuralProblems(ContentJson.parse(doc(validItem)), today)
        assertEquals(emptyList<String>(), problems)
    }

    @Test fun unknownFieldFailsToParse() =
        expectFormatError(doc(validItem.dropLast(1) + ""","bogus":true}"""))

    @Test fun unknownApplicabilityFlagFailsToParse() =
        expectFormatError(doc(validItem.replace("\"applicability\":[]", "\"applicability\":[\"NOPE\"]")))

    @Test fun unknownVerificationStatusFailsToParse() =
        expectFormatError(doc(validItem.replace("\"VERIFIED\"", "\"MAYBE\"")))

    @Test fun nonVerifiedItemIsAShipBlocker() {
        val content = ContentJson.parse(doc(validItem.replace("\"VERIFIED\"", "\"PARTIAL\"")))
        assertEquals(listOf("c.item"), ContentValidator.shipBlockers(content).map { it.id })
    }

    @Test fun missingCitationIsAStructuralProblem() {
        val content = ContentJson.parse(doc(validItem.replace(
            "\"citations\":[{\"type\":\"CFR\",\"ref\":\"49 CFR 1\",\"url\":null}]", "\"citations\":[]")))
        assertTrue(ContentValidator.structuralProblems(content, today).any { it.contains("citation") })
    }

    @Test fun duplicateIdIsAStructuralProblem() {
        val content = ContentJson.parse(doc("$validItem,$validItem", count = 2))
        assertTrue(ContentValidator.structuralProblems(content, today).any { it.contains("duplicate item ids") })
    }

    @Test fun countMismatchIsAStructuralProblem() {
        val content = ContentJson.parse(doc(validItem, count = 99))
        assertTrue(ContentValidator.structuralProblems(content, today).any { it.contains("expectedItemCount") })
    }

    @Test fun futureVerifiedOnIsAStructuralProblem() {
        val content = ContentJson.parse(doc(validItem.replace("\"verifiedOn\":\"2026-07-17\"", "\"verifiedOn\":\"2999-01-01\"")))
        assertTrue(ContentValidator.structuralProblems(content, today).any { it.contains("future") })
    }

    @Test fun placeholderMarkerIsAStructuralProblem() {
        val content = ContentJson.parse(doc(validItem.replace("\"name\":\"Item\"", "\"name\":\"TODO write this\"")))
        assertTrue(ContentValidator.structuralProblems(content, today).any { it.contains("placeholder") })
    }

    // --- gate holes found in review ---

    @Test fun jsonNullInRequiredStringFailsToParse() =
        expectFormatError(doc(validItem.replace("\"name\":\"Item\"", "\"name\":null")))

    @Test fun numberWhereStringExpectedFailsToParse() =
        expectFormatError(doc(validItem.replace("\"name\":\"Item\"", "\"name\":123")))

    @Test fun quotedIntSequenceFailsToParse() =
        expectFormatError(doc(validItem.replace("\"sequence\":1", "\"sequence\":\"1\"")))

    @Test fun verifiedWithOnlyAManualCitationIsAShipGateProblem() {
        val content = ContentJson.parse(doc(validItem.replace("\"type\":\"CFR\"", "\"type\":\"MANUAL\"")))
        assertTrue(ContentValidator.structuralProblems(content, today).any { it.contains("public-primary") })
    }

    @Test fun verifiedMissingVerifiedOnIsAStructuralProblem() {
        val content = ContentJson.parse(doc(validItem.replace("\"verifiedOn\":\"2026-07-17\"", "\"verifiedOn\":null")))
        assertTrue(ContentValidator.structuralProblems(content, today).any { it.contains("missing verifiedOn") })
    }

    @Test fun nonSlugIdIsAStructuralProblem() {
        val content = ContentJson.parse(doc(validItem.replace("\"id\":\"c.item\"", "\"id\":\"Bad Id\"")))
        assertTrue(ContentValidator.structuralProblems(content, today).any { it.contains("valid slug") })
    }

    @Test fun contentVersionFormatIsEnforced() {
        val content = ContentJson.parse(doc(validItem).replace("\"contentVersion\":\"2026.07.0\"", "\"contentVersion\":\"2026.7.0\""))
        assertTrue(ContentValidator.structuralProblems(content, today).any { it.contains("contentVersion") })
    }

    @Test fun categoryWithNoItemsIsAStructuralProblem() {
        val cats = """$DEFAULT_CATEGORY,{"id":"empty","title":"Empty","sequence":2}"""
        val content = ContentJson.parse(doc(validItem, categories = cats))
        assertTrue(ContentValidator.structuralProblems(content, today).any { it.contains("has no items") })
    }

    @Test fun importCompletenessChecksContiguousSequence() {
        val content = ContentJson.parse(doc(validItem))
        assertTrue(ContentValidator.importCompletenessProblems(content, 132).any { it.contains("132") })
    }
}
