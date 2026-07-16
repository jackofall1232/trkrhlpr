package com.lastwagon.core.data

import com.lastwagon.core.model.ApplicabilityFlag
import com.lastwagon.core.model.VerificationStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Pure-JVM coverage for the schema-v2 content-provenance encoding and the 1 -> 2 migration
 *  metadata. Instrumented ALTER-TABLE validation (MigrationTestHelper) remains a device gate. */
class InspectionContentSchemaTest {

    @Test fun applicabilityFlagsRoundTrip() {
        val flags = setOf(ApplicabilityFlag.COMBO, ApplicabilityFlag.AIR)
        assertEquals(flags, decodeApplicabilityFlags(encodeApplicabilityFlags(flags)))
    }

    @Test fun emptyFlagSetEncodesToEmptyStringAndBack() {
        assertEquals("", encodeApplicabilityFlags(emptySet()))
        assertEquals(emptySet<ApplicabilityFlag>(), decodeApplicabilityFlags(""))
    }

    @Test fun encodingIsStableAndSorted() {
        val a = encodeApplicabilityFlags(setOf(ApplicabilityFlag.AIR, ApplicabilityFlag.COMBO))
        val b = encodeApplicabilityFlags(setOf(ApplicabilityFlag.COMBO, ApplicabilityFlag.AIR))
        assertEquals(a, b)
        assertEquals("AIR,COMBO", a)
    }

    @Test fun decodeIgnoresUnknownTokensAndWhitespace() {
        // Forward-compatible: an unknown/renamed flag degrades to "ignore", never a crash.
        assertEquals(
            setOf(ApplicabilityFlag.IF_EQUIPPED),
            decodeApplicabilityFlags(" IF_EQUIPPED , FUTURE_FLAG ,"),
        )
    }

    @Test fun verificationStatusParsesKnownValues() {
        assertEquals(VerificationStatus.VERIFIED, parseVerificationStatus("VERIFIED"))
        assertEquals(VerificationStatus.PARTIAL, parseVerificationStatus("PARTIAL"))
        assertEquals(VerificationStatus.UNVERIFIED, parseVerificationStatus("UNVERIFIED"))
    }

    @Test fun verificationStatusDefaultsToUnverifiedForNullOrGarbage() {
        assertEquals(VerificationStatus.UNVERIFIED, parseVerificationStatus(null))
        assertEquals(VerificationStatus.UNVERIFIED, parseVerificationStatus("not-a-status"))
    }

    @Test fun sampleContentDecodesToValidFlags() {
        val byId = SampleContent.inspectionItems.associateBy { it.id }
        assertEquals(emptySet<ApplicabilityFlag>(),
            decodeApplicabilityFlags(byId.getValue("sample-seat-belt").applicabilityFlags))
        assertEquals(setOf(ApplicabilityFlag.IF_EQUIPPED),
            decodeApplicabilityFlags(byId.getValue("sample-lights").applicabilityFlags))
        assertEquals(setOf(ApplicabilityFlag.COMBO, ApplicabilityFlag.AIR),
            decodeApplicabilityFlags(byId.getValue("sample-coupling").applicabilityFlags))
        // Every sample item carries a (placeholder) citation and a parseable status.
        SampleContent.inspectionItems.forEach {
            assertTrue(it.sourceCitation.isNotBlank())
            assertEquals(VerificationStatus.UNVERIFIED, parseVerificationStatus(it.verificationStatus))
        }
    }

    @Test fun migration1To2HasCorrectVersionBounds() {
        assertEquals(1, MIGRATION_1_2.startVersion)
        assertEquals(2, MIGRATION_1_2.endVersion)
    }
}
