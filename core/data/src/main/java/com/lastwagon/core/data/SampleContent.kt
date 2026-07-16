package com.lastwagon.core.data

/** Fictional UI demonstration data only; never authoritative inspection or CDL content. */
internal object SampleContent {
    val inspectionCategories = listOf(
        InspectionCategoryEntity("sample-cab", "Cab & controls", 1),
        InspectionCategoryEntity("sample-front", "Front of vehicle", 2),
        InspectionCategoryEntity("sample-coupling", "Coupling area", 3),
    )
    val inspectionItems = listOf(
        InspectionItemEntity("sample-seat-belt", "sample-cab", "Seat belt demonstration item",
            "Sample only: review component condition and secure operation.",
            "Sample only: visible damage, insecure mounting, or impaired operation.", 1, true),
        InspectionItemEntity("sample-lights", "sample-front", "Lighting demonstration item",
            "Sample only: review condition, cleanliness, and operation.",
            "Sample only: damage, obstruction, or failure to operate.", 2, true),
        InspectionItemEntity("sample-coupling", "sample-coupling", "Coupling demonstration item",
            "Sample only: review visible condition and secure engagement.",
            "Sample only: damage, looseness, or incomplete engagement.", 3, true),
    )
    val testCategories = listOf(
        "class-a" to "Class A", "class-b" to "Class B", "general" to "General Knowledge",
        "air-brakes" to "Air Brakes", "combination" to "Combination Vehicles",
        "hazmat" to "Hazmat", "tanker" to "Tanker", "passenger" to "Passenger",
    ).mapIndexed { index, (id, title) -> TestCategoryEntity(id, title, kindFor(id), index) }

    val questions = testCategories.map { category ->
        val id = "sample-question-" + category.id
        QuestionEntity(id, category.id,
            "Sample interface question: Which option demonstrates a deliberate safety check?",
            "Skip the check|Pause and verify the condition|Assume the prior driver checked it",
            "$id-answer-1",
            "Sample explanation only. Production questions require an approved official source.",
            "practice", true)
    } + QuestionEntity("sample-daily-1", "daily",
        "Sample daily question: What is the safest response when conditions are unclear?",
        "Continue without checking|Stop in a safe place and reassess|Rely on a guess",
        "sample-daily-1-answer-1",
        "Sample explanation only. Future daily content requires source and safety review.",
        "daily", true)

    private fun kindFor(id: String) = when (id) {
        "class-a" -> "CLASS_A"; "class-b" -> "CLASS_B"; "general" -> "GENERAL"
        "air-brakes" -> "AIR_BRAKES"; "combination" -> "COMBINATION"
        "hazmat" -> "HAZMAT"; "tanker" -> "TANKER"; else -> "PASSENGER"
    }
}
