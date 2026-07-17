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

    // Fictional truck-stop directory content (Track C), carried as a document in the
    // versioned content-pipeline schema parsed by TruckStopContent — the same path the
    // real dataset will take once its source verification clears
    // (docs/truck-stop-data-sources.md): the verified dataset replaces this JSON, nothing
    // else changes. Names and coordinates are invented; records exercise the three-state
    // amenity model (true / false / absent = unknown).
    val truckStopsJson = """
        {
          "schema_version": 1,
          "dataset": {
            "citation": "$SAMPLE_CITATION",
            "vintage": "Sample data — fictional locations for interface demonstration.",
            "verification": "UNVERIFIED",
            "sample": true
          },
          "stops": [
            {"id": "sample-stop-01", "name": "Sample Junction Travel Plaza", "state": "OH",
             "highway": "I-75 Exit 100", "lat": 40.10, "lon": -84.20,
             "parking_spaces": 60, "diesel": true, "showers": true, "food": true, "repair": false},
            {"id": "sample-stop-02", "name": "Sample Ridge Fuel & Parking", "state": "OH",
             "highway": "I-70 Exit 52", "lat": 39.95, "lon": -83.80,
             "parking_spaces": 25, "diesel": true, "food": true},
            {"id": "sample-stop-03", "name": "Sample Prairie Rest Area", "state": "IA",
             "highway": "I-80 Mile 200", "lat": 41.60, "lon": -93.60,
             "parking_spaces": 18},
            {"id": "sample-stop-04", "name": "Sample Summit Truck Stop", "state": "CO",
             "highway": "I-70 Exit 250", "lat": 39.60, "lon": -106.00,
             "parking_spaces": 40, "diesel": true, "showers": true, "repair": true},
            {"id": "sample-stop-05", "name": "Sample Delta Travel Center", "state": "TX",
             "highway": "I-10 Exit 800", "lat": 29.80, "lon": -95.40,
             "parking_spaces": 110, "diesel": true, "showers": true, "food": true, "repair": true},
            {"id": "sample-stop-06", "name": "Sample Basin Rest Stop", "state": "TX",
             "highway": "I-20 Mile 150", "lat": 32.30, "lon": -101.50}
          ]
        }
    """.trimIndent()

    private fun kindFor(id: String) = when (id) {
        "class-a" -> "CLASS_A"; "class-b" -> "CLASS_B"; "general" -> "GENERAL"
        "air-brakes" -> "AIR_BRAKES"; "combination" -> "COMBINATION"
        "hazmat" -> "HAZMAT"; "tanker" -> "TANKER"; else -> "PASSENGER"
    }
}
