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

enum class ExecutiveRole {
    CFO,
    COO,
    CISO,
    CMO
}

data class Executive(
    val id: String,
    val name: String,
    val role: ExecutiveRole,
    val dailySalary: Double,
    val hiringCost: Double,
    val description: String,
    val isHired: Boolean = false,
    val iconEmoji: String
)

object ExecutiveCatalog {
    val INITIAL_EXECUTIVES = listOf(
        Executive(
            id = "exec_cfo",
            name = "Victoria Sterling",
            role = ExecutiveRole.CFO,
            dailySalary = 450.0,
            hiringCost = 15000.0,
            description = "Increases the compounding interest rate of invested bank assets to 2.5% daily.",
            iconEmoji = "📊"
        ),
        Executive(
            id = "exec_coo",
            name = "Marcus Vance",
            role = ExecutiveRole.COO,
            dailySalary = 600.0,
            hiringCost = 25000.0,
            description = "Applies a global 1.5x multiplier to the output of all processing facilities.",
            iconEmoji = "⚙️"
        ),
        Executive(
            id = "exec_ciso",
            name = "Elena Rostova",
            role = ExecutiveRole.CISO,
            dailySalary = 500.0,
            hiringCost = 20000.0,
            description = "Automatically blocks Corporate Sabotage and Digital Fraud attempts.",
            iconEmoji = "🛡️"
        ),
        Executive(
            id = "exec_cmo",
            name = "Barnaby 'The Legend'",
            role = ExecutiveRole.CMO,
            dailySalary = 800.0,
            hiringCost = 40000.0,
            description = "Legendary Mentor. Applies a +20% bonus to the payout of all generated B2B Contract Offers.",
            iconEmoji = "🎩"
        )
    )
}
