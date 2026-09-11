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

enum class ResearchCategory {
    PRODUCTS, PERSONAL, INDUSTRY, COMPANY, EXPERIMENTAL
}

enum class NodeStatus {
    LOCKED, AVAILABLE, RESEARCHING, COMPLETED
}

sealed class ResearchEffect {
    object UnlockArtisanCheese : ResearchEffect()
    object UnlockWheyProtein : ResearchEffect()
    object UnlockLabGrownDairy : ResearchEffect()
    
    data class IncreaseTaskSpeed(val multiplier: Double) : ResearchEffect()
    data class IncreaseNegotiationMargin(val margin: Double) : ResearchEffect()
    data class IncreaseReputationRecovery(val recovery: Int) : ResearchEffect()
    
    object UnlockColdStorage : ResearchEffect()
    data class IncreaseMaxFacilities(val amount: Int) : ResearchEffect()
    object UnlockDedicatedLabs : ResearchEffect()
    object UnlockConstructionCrews : ResearchEffect()
    
    data class IncreaseCyberDefense(val amount: Int) : ResearchEffect()
    data class IncreaseCyberOffense(val amount: Int) : ResearchEffect()
    object UnlockAdvancedB2B : ResearchEffect()
    object UnlockScamDetection : ResearchEffect()
    
    object ZeroColdStorageMaintenance : ResearchEffect()
    object DoubleLiquidThroughput : ResearchEffect()
    object DoubleGlobalSellPrice : ResearchEffect()
    
    object None : ResearchEffect()
}

data class ResearchNode(
    val id: String,
    val title: String,
    val description: String,
    val category: ResearchCategory,
    val researchCost: Int, // Liquid Cash
    val researchTimeDays: Int,
    val prerequisites: List<String>,
    val effect: ResearchEffect = ResearchEffect.None
)

object ResearchCatalog {
    val ALL_NODES = listOf(
        // PRODUCTS
        ResearchNode("res_artisan_cheese", "Artisan Cheese", "Unlock premium artisan cheese production.", ResearchCategory.PRODUCTS, 2500, 3, emptyList(), ResearchEffect.UnlockArtisanCheese),
        ResearchNode("res_whey_protein", "Whey Protein Isolate", "Extract high-margin fitness supplements.", ResearchCategory.PRODUCTS, 4000, 5, listOf("res_artisan_cheese"), ResearchEffect.UnlockWheyProtein),
        ResearchNode("res_lab_grown", "Bio-Synthesized Dairy", "Endgame product requiring zero raw milk.", ResearchCategory.PRODUCTS, 15000, 10, listOf("res_whey_protein"), ResearchEffect.UnlockLabGrownDairy),
        
        // PERSONAL
        ResearchNode("res_hustler_1", "Hustler Mentality", "Increases task speed by 15%.", ResearchCategory.PERSONAL, 1000, 2, emptyList(), ResearchEffect.IncreaseTaskSpeed(1.15)),
        ResearchNode("res_negotiator_1", "Silver Tongue", "Increases B2B margins by 10%.", ResearchCategory.PERSONAL, 2000, 3, emptyList(), ResearchEffect.IncreaseNegotiationMargin(0.1)),
        
        // INDUSTRY
        ResearchNode("res_cold_storage", "Basic Cold Storage", "Unlock the Cold Storage Warehouse.", ResearchCategory.INDUSTRY, 3000, 3, emptyList(), ResearchEffect.UnlockColdStorage),
        ResearchNode("res_construction_crew", "Dedicated Construction Crews", "Hire a dedicated crew to build facilities asynchronously.", ResearchCategory.INDUSTRY, 5000, 5, emptyList(), ResearchEffect.UnlockConstructionCrews),
        ResearchNode("res_expansion_1", "Land Expansion Permits", "Increases maximum land limit.", ResearchCategory.INDUSTRY, 8000, 4, emptyList(), ResearchEffect.IncreaseMaxFacilities(2)),
        
        // COMPANY
        ResearchNode("res_b2b", "B2B Contracts", "Unlock business-to-business wholesale contracts.", ResearchCategory.COMPANY, 1500, 2, emptyList(), ResearchEffect.UnlockAdvancedB2B),
        ResearchNode("res_dedicated_labs", "Dedicated R&D Labs", "Unlock advanced corporate research labs.", ResearchCategory.COMPANY, 10000, 7, listOf("res_b2b"), ResearchEffect.UnlockDedicatedLabs),
        ResearchNode("res_cyber_defense", "Cybersecurity Protocols", "Defend against corporate espionage.", ResearchCategory.COMPANY, 12000, 5, listOf("res_dedicated_labs"), ResearchEffect.IncreaseCyberDefense(20)),
        ResearchNode("res_corp_offense", "Zero-Day Exploits", "Equip your hackers with powerful new tools (+15 Offense).", ResearchCategory.COMPANY, 10000, 7, listOf("res_cyber_defense"), ResearchEffect.IncreaseCyberOffense(15)),
        ResearchNode("tech_scam_detection", "Scam Detection Algorithm", "Detect incoming corporate cyber attacks one day early.", ResearchCategory.COMPANY, 8500, 5, listOf("res_cyber_defense"), ResearchEffect.UnlockScamDetection),
        ResearchNode("tech_singularity", "AGI Market Singularity", "Achieve absolute automation and transcend the market.", ResearchCategory.COMPANY, 500000, 15, listOf("tech_scam_detection"), ResearchEffect.None)
    )
    
    fun getById(id: String): ResearchNode = ALL_NODES.first { it.id == id }
}
