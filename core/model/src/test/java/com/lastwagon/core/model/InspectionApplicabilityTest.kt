package com.lastwagon.core.model

import org.junit.Assert.assertEquals
import org.junit.Test

class InspectionApplicabilityTest {
    private val none = InspectionConfig(isCombination = false, hasAirBrakes = false)
    private val comboAir = InspectionConfig(isCombination = true, hasAirBrakes = true)

    private fun eval(flags: Set<ApplicabilityFlag>, config: InspectionConfig) =
        InspectionApplicability.evaluate(flags, config)

    @Test fun noFlagsAlwaysApplies() {
        assertEquals(ItemApplicability.APPLIES, eval(emptySet(), none))
        assertEquals(ItemApplicability.APPLIES, eval(emptySet(), comboAir))
    }

    @Test fun comboExcludedWhenNotCombination() {
        assertEquals(ItemApplicability.NOT_APPLICABLE, eval(setOf(ApplicabilityFlag.COMBO), none))
        assertEquals(ItemApplicability.APPLIES, eval(setOf(ApplicabilityFlag.COMBO), comboAir))
    }

    @Test fun airExcludedWhenNoAirBrakes() {
        assertEquals(ItemApplicability.NOT_APPLICABLE, eval(setOf(ApplicabilityFlag.AIR), none))
        assertEquals(ItemApplicability.APPLIES,
            eval(setOf(ApplicabilityFlag.AIR), InspectionConfig(isCombination = false, hasAirBrakes = true)))
    }

    @Test fun allHardFlagsMustBeSatisfied() {
        val bothFlags = setOf(ApplicabilityFlag.COMBO, ApplicabilityFlag.AIR)
        assertEquals(ItemApplicability.NOT_APPLICABLE,
            eval(bothFlags, InspectionConfig(isCombination = true, hasAirBrakes = false)))
        assertEquals(ItemApplicability.APPLIES, eval(bothFlags, comboAir))
    }

    @Test fun ifEquippedIsSoftAndNeverExcludes() {
        // IF_EQUIPPED alone: shown-but-optional regardless of config, never NOT_APPLICABLE.
        assertEquals(ItemApplicability.IF_EQUIPPED, eval(setOf(ApplicabilityFlag.IF_EQUIPPED), none))
        assertEquals(ItemApplicability.IF_EQUIPPED, eval(setOf(ApplicabilityFlag.IF_EQUIPPED), comboAir))
    }

    @Test fun ifEquippedCombinedWithUnsatisfiedHardFlagIsStillExcluded() {
        // A hard flag still governs inclusion; IF_EQUIPPED only softens an otherwise-included item.
        assertEquals(ItemApplicability.NOT_APPLICABLE,
            eval(setOf(ApplicabilityFlag.COMBO, ApplicabilityFlag.IF_EQUIPPED), none))
        assertEquals(ItemApplicability.IF_EQUIPPED,
            eval(setOf(ApplicabilityFlag.COMBO, ApplicabilityFlag.IF_EQUIPPED), comboAir))
    }
}
