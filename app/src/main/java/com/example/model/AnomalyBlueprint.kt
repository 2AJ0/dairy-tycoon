package com.example.model

/**
 * ==========================================
 * AI / IDE AGENT INSTRUCTIONS:
 * ==========================================
 * This file defines the core Data Models and static Catalogs (TechTree, Rivals, Products).
 * 
 * CORE RULES FOR FIXING ERRORS:
 * 1. DATA INTEGRITY: These classes represent the persistent save state and configuration. 
 * 2. NO LOGIC HERE: Keep these as pure Kotlin `data class` or `object` singletons. Business logic belongs in `GameViewModel`.
 * 3. PRESERVE IDs: String IDs (e.g., "tech_pasteurization", "rival_global_whey") are used heavily in cross-referencing. Do not rename them.
 */

enum class AnomalyEffect {
    RADIATIVE_COOLING,
    ACOUSTIC_LEVITATION,
    NANOMATERIAL_DESALINATION
}

data class ExperimentalBlueprint(
    val id: String,
    val title: String,
    val description: String,
    val researchCostRp: Int,
    val installCostCash: Double,
    val effectType: AnomalyEffect,
    val prerequisites: List<String> = emptyList()
)

object AnomalyCatalog {
    val BLUEPRINTS = listOf(
        ExperimentalBlueprint(
            id = "anomaly_rad_cool",
            title = "Radiative Cooling",
            description = "Force daily maintenance to 0 by siphoning heat into the quantum void.",
            researchCostRp = 500,
            installCostCash = 500_000.0,
            effectType = AnomalyEffect.RADIATIVE_COOLING
        ),
        ExperimentalBlueprint(
            id = "anomaly_aco_levi",
            title = "Acoustic Levitation",
            description = "Suspend materials in a vacuum grid, multiplying facility capacity by 5x.",
            researchCostRp = 1000,
            installCostCash = 1_500_000.0,
            effectType = AnomalyEffect.ACOUSTIC_LEVITATION,
            prerequisites = listOf("anomaly_rad_cool")
        ),
        ExperimentalBlueprint(
            id = "anomaly_nano_desal",
            title = "Nanomaterial Synthesis",
            description = "Facility synthesizes its own raw materials. Bypass standard input queue costs entirely.",
            researchCostRp = 2500,
            installCostCash = 5_000_000.0,
            effectType = AnomalyEffect.NANOMATERIAL_DESALINATION,
            prerequisites = listOf("anomaly_aco_levi")
        )
    )
}
