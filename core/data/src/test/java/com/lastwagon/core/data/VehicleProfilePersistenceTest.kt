package com.lastwagon.core.data

import com.lastwagon.core.model.CommercialVehicleType
import org.junit.Assert.*
import org.junit.Test

class VehicleProfilePersistenceTest {
    @Test fun currentSchemaDecodesCompleteProfile() {
        val profile = decodeStoredVehicleProfile(
            1, "TRACTOR_TRAILER", "4.1", "2.5", "21.0", "36.0", "9.0",
            5, true, false, true, true, 123L,
        )

        assertNotNull(profile)
        assertEquals(CommercialVehicleType.TRACTOR_TRAILER, profile?.vehicleType)
        assertTrue(profile?.hazmat == true)
        assertEquals(123L, profile?.confirmedAtEpochMillis)
    }

    @Test fun rejectsUnknownSchemaCorruptionAndInvalidValues() {
        assertNull(decodeStoredVehicleProfile(
            2, "TRACTOR_TRAILER", "4.1", "2.5", "21", "36", "9",
            5, false, false, false, false, 1L,
        ))
        assertNull(decodeStoredVehicleProfile(
            1, "UNKNOWN", "4.1", "2.5", "21", "36", "9",
            5, false, false, false, false, 1L,
        ))
        assertNull(decodeStoredVehicleProfile(
            1, "TRACTOR_TRAILER", "not-a-number", "2.5", "21", "36", "9",
            5, false, false, false, false, 1L,
        ))
        assertNull(decodeStoredVehicleProfile(
            1, "TRACTOR_TRAILER", "9", "2.5", "21", "36", "9",
            5, false, false, false, false, 1L,
        ))
    }
}
