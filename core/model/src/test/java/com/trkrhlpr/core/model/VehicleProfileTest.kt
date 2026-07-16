package com.trkrhlpr.core.model

import org.junit.Assert.*
import org.junit.Test

class VehicleProfileTest {
    private val valid = VehicleProfile(
        CommercialVehicleType.TRACTOR_TRAILER,
        heightMeters = 4.1148, widthMeters = 2.59, lengthMeters = 21.0,
        grossWeightTonnes = 36.3, axleLoadTonnes = 9.1, axleCount = 5,
        hazmat = false, avoidTolls = false, avoidFerries = true,
        avoidUnpavedRoads = true, confirmedAtEpochMillis = 1L,
    )

    @Test fun validConfirmedProfilePasses() {
        assertTrue(VehicleProfileValidator.validate(valid).isEmpty())
    }

    @Test fun rejectsImplausibleAndUnconfirmedProfile() {
        val errors = VehicleProfileValidator.validate(valid.copy(
            heightMeters = 0.0, axleLoadTonnes = 40.0, axleCount = 1,
            confirmedAtEpochMillis = 0,
        ))
        assertTrue(errors.any { it.field == VehicleProfileField.HEIGHT })
        assertTrue(errors.any { it.field == VehicleProfileField.AXLE_LOAD })
        assertTrue(errors.any { it.field == VehicleProfileField.AXLE_COUNT })
        assertTrue(errors.any { it.field == VehicleProfileField.CONFIRMATION })
    }

    @Test fun usCustomaryConversionsRoundTrip() {
        val feet = 13.5
        val pounds = 80_000.0
        assertEquals(feet, VehicleUnitConversions.metersToFeet(
            VehicleUnitConversions.feetToMeters(feet)), 0.000001)
        assertEquals(pounds, VehicleUnitConversions.metricTonnesToPounds(
            VehicleUnitConversions.poundsToMetricTonnes(pounds)), 0.001)
    }
}
