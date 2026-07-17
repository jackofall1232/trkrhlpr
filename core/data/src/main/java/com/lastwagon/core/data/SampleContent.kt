package com.lastwagon.core.data

import com.lastwagon.core.model.ApplicabilityFlag

/** Fictional UI demonstration data only; never authoritative inspection or CDL content. The
 *  provenance fields below are placeholders that exercise the schema-v2 metadata; they are
 *  explicitly NOT citations to any regulation. */
internal object SampleContent {
    private const val SAMPLE_CITATION = "Sample only — not an authoritative citation."

    val inspectionCategories = listOf(
        InspectionCategoryEntity("sample-cab", "Cab & controls", 1),
        InspectionCategoryEntity("sample-front", "Front of vehicle", 2),
        InspectionCategoryEntity("sample-coupling", "Coupling area", 3),
    )
    val inspectionItems = listOf(
        // No flags: applies to every configuration.
        InspectionItemEntity("sample-seat-belt", "sample-cab", "Seat belt demonstration item",
            "Sample only: review component condition and secure operation.",
            "Sample only: visible damage, insecure mounting, or impaired operation.", 1, true,
            SAMPLE_CITATION, "UNVERIFIED", ""),
        // IF_EQUIPPED: soft flag — shown to everyone, skippable when not equipped.
        InspectionItemEntity("sample-lights", "sample-front", "Lighting demonstration item",
            "Sample only: review condition, cleanliness, and operation.",
            "Sample only: damage, obstruction, or failure to operate.", 2, true,
            SAMPLE_CITATION, "UNVERIFIED",
            encodeApplicabilityFlags(setOf(ApplicabilityFlag.IF_EQUIPPED))),
        // COMBO + AIR: hard flags — combination, air-brake-equipped vehicles only.
        InspectionItemEntity("sample-coupling", "sample-coupling", "Coupling demonstration item",
            "Sample only: review visible condition and secure engagement.",
            "Sample only: damage, looseness, or incomplete engagement.", 3, true,
            SAMPLE_CITATION, "UNVERIFIED",
            encodeApplicabilityFlags(setOf(ApplicabilityFlag.COMBO, ApplicabilityFlag.AIR))),
    )
    val testCategories = listOf(
        "class-a" to "Class A", "class-b" to "Class B", "general" to "General Knowledge",
        "air-brakes" to "Air Brakes", "combination" to "Combination Vehicles",
        "hazmat" to "Hazmat", "tanker" to "Tanker", "passenger" to "Passenger",
    ).mapIndexed { index, (id, title) -> TestCategoryEntity(id, title, kindFor(id), index) }

    // A small labeled-sample pool so one-per-day selection and streaks are demonstrable. Not
    // authoritative content — real daily questions require source and safety review (Track B).
    private val dailyPrompts = listOf(
        Triple("What is the safest response when conditions are unclear?",
            listOf("Continue without checking", "Stop in a safe place and reassess", "Rely on a guess"), 1),
        Triple("You notice a small air leak before a trip. What is the safe choice?",
            listOf("Drive and monitor it", "Do not drive until it is resolved", "Ignore it if brakes still work"), 1),
        Triple("When following another vehicle in good conditions, you should keep a following distance that is:",
            listOf("As short as traffic allows", "Enough to stop safely for your speed and load", "Fixed at one second"), 1),
        Triple("If you feel too tired to drive safely, the safe action is to:",
            listOf("Push through to the next stop", "Stop and rest before continuing", "Open a window and continue"), 1),
    )
    // Several labeled-sample practice questions per category so mock exams can randomize.
    private val practiceTemplates = listOf(
        Triple("Which option demonstrates a deliberate safety check?",
            listOf("Skip the check", "Pause and verify the condition", "Assume the prior driver checked it"), 1),
        Triple("Before moving the vehicle, what should you confirm?",
            listOf("Mirrors and gauges are set", "Nothing — just go", "Only that the radio works"), 0),
        Triple("If a warning light stays on, the safe action is to:",
            listOf("Ignore it", "Investigate before driving", "Cover it up"), 1),
        Triple("The safest habit for a repeated task is to:",
            listOf("Rush to save time", "Follow the same deliberate steps every time", "Skip steps you already know"), 1),
    )
    val questions = testCategories.flatMap { category ->
        practiceTemplates.mapIndexed { i, (prompt, answers, correct) ->
            val id = "sample-question-${category.id}-$i"
            QuestionEntity(id, category.id, "Sample interface question: $prompt",
                answers.joinToString("|"), "$id-answer-$correct",
                "Sample explanation only. Production questions require an approved official source.",
                "practice", true)
        }
    } + dailyPrompts.mapIndexed { index, (prompt, answers, correct) ->
        val id = "sample-daily-" + (index + 1)
        QuestionEntity(id, "daily", "Sample daily question: $prompt",
            answers.joinToString("|"), "$id-answer-$correct",
            "Sample explanation only. Future daily content requires source and safety review.",
            "daily", true)
    }

    private const val SAMPLE_VINTAGE = "Sample data — fictional locations for interface demonstration."

    // Fictional truck-stop directory rows (Track C phase 1). Names and coordinates are
    // invented; they exercise the three-state amenity model (true / false / NULL = unknown)
    // and the provenance columns. The real dataset import is gated on the source-verification
    // worklist in docs/truck-stop-data-sources.md.
    val truckStops = listOf(
        TruckStopEntity("sample-stop-01", "Sample Junction Travel Plaza", "OH", "I-75 Exit 100",
            40.10, -84.20, 60, true, true, true, false, true,
            SAMPLE_CITATION, "UNVERIFIED", SAMPLE_VINTAGE),
        TruckStopEntity("sample-stop-02", "Sample Ridge Fuel & Parking", "OH", "I-70 Exit 52",
            39.95, -83.80, 25, true, null, true, null, true,
            SAMPLE_CITATION, "UNVERIFIED", SAMPLE_VINTAGE),
        TruckStopEntity("sample-stop-03", "Sample Prairie Rest Area", "IA", "I-80 Mile 200",
            41.60, -93.60, 18, null, null, null, null, true,
            SAMPLE_CITATION, "UNVERIFIED", SAMPLE_VINTAGE),
        TruckStopEntity("sample-stop-04", "Sample Summit Truck Stop", "CO", "I-70 Exit 250",
            39.60, -106.00, 40, true, true, null, true, true,
            SAMPLE_CITATION, "UNVERIFIED", SAMPLE_VINTAGE),
        TruckStopEntity("sample-stop-05", "Sample Delta Travel Center", "TX", "I-10 Exit 800",
            29.80, -95.40, 110, true, true, true, true, true,
            SAMPLE_CITATION, "UNVERIFIED", SAMPLE_VINTAGE),
        TruckStopEntity("sample-stop-06", "Sample Basin Rest Stop", "TX", "I-20 Mile 150",
            32.30, -101.50, null, null, null, null, null, true,
            SAMPLE_CITATION, "UNVERIFIED", SAMPLE_VINTAGE),
    )

    private fun kindFor(id: String) = when (id) {
        "class-a" -> "CLASS_A"; "class-b" -> "CLASS_B"; "general" -> "GENERAL"
        "air-brakes" -> "AIR_BRAKES"; "combination" -> "COMBINATION"
        "hazmat" -> "HAZMAT"; "tanker" -> "TANKER"; else -> "PASSENGER"
    }
}
