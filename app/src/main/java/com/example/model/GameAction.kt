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

sealed class GameAction {
    data class Construct(val buildingId: String) : GameAction()
    data class Upgrade(val buildingId: String) : GameAction()
    data class Research(val techId: String) : GameAction()
    data class Contract(val contractId: String) : GameAction()
    data class Sell(val productId: String) : GameAction()
}
