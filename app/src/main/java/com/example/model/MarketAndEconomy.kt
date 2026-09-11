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

/**
 * Live market pricing ticker state for an individual commodity.
 */
data class MarketItemState(
    val itemId: String,
    val currentPrice: Double,
    val basePrice: Double,
    val yesterdayUnitsSold: Int = 0,
    val priceChangePercent: Double = 0.0,
    val priceHistory: List<Double> = emptyList()
) {
    val isAboveBase: Boolean get() = currentPrice >= basePrice
    val diffFromBase: Double get() = currentPrice - basePrice
    val diffPercentFromBase: Double
        get() = if (basePrice > 0) ((currentPrice - basePrice) / basePrice) * 100.0 else 0.0
    val isBullish: Boolean get() = isAboveBase
}

/**
 * Bank debt, credit rating, and foreclosure timer.
 */
data class BankState(
    val totalDebt: Double = 0.0,
    val daysInDebt: Int = 0,
    val dailyInterestRate: Double = 0.015, // 1.5% daily compounding interest
    val maxCreditLimit: Double = 10000.0,
    val foreclosureWarningThreshold: Int = 7,
    val investedFunds: Double = 0.0
) {
    val isInDebt: Boolean get() = totalDebt > 0.0
    val isForeclosureImminent: Boolean get() = daysInDebt >= foreclosureWarningThreshold
    val daysUntilLiquidation: Int get() = (foreclosureWarningThreshold - daysInDebt).coerceAtLeast(0)
    val dailyInterestCharge: Double get() = totalDebt * dailyInterestRate
}

/**
 * News event structure containing headline, story, target commodity, price multiplier, and duration.
 */
data class NewsEvent(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val targetProductId: String? = null, // null indicates a global market or non-product news bulletin
    val multiplier: Double = 1.0, // e.g. 1.50 for +50% price spike, 0.75 for 25% slump
    val durationDays: Int = 1,
    val remainingDays: Int = durationDays,
    val cashBonus: Double = 0.0,
    val reputationDelta: Int = 0,
    val iconEmoji: String = "📰"
) {
    val isProductSpike: Boolean get() = multiplier > 1.0
    val isProductSlump: Boolean get() = multiplier < 1.0
    val percentageFormatted: String
        get() {
            val pct = ((multiplier - 1.0) * 100.0).toInt()
            return if (pct > 0) "+$pct%" else "$pct%"
        }
}

/**
 * Backward compatibility alias if needed.
 */
typealias DailyNewspaperEvent = NewsEvent

/**
 * Player skill state tracking levels for the distinct RP skill sinks.
 */
data class PlayerSkills(
    val hustlerLevel: Int = 0,
    val efficiencyExpertLevel: Int = 0,
    val silverTongueLevel: Int = 0,
    val staminaLevel: Int = 0,
    val bovineGeneticsLevel: Int = 0,
    val coldChainLogisticsLevel: Int = 0,
    val multiTaskingLevel: Int = 0
) {
    val totalLevel: Int get() = hustlerLevel + efficiencyExpertLevel + silverTongueLevel + staminaLevel + bovineGeneticsLevel + coldChainLogisticsLevel + multiTaskingLevel

    // Concurrent Personal Actions (Base 1, +1 per level, max 5)
    val personalActionCapacity: Int get() = (1 + multiTaskingLevel).coerceAtMost(5)

    // Base 5 actions/day + 1 action per level of Stamina
    val maxDailyActions: Int get() = 5 + staminaLevel

    // 1. Hustler: +$18 flat cash bonus per level on manual "Work" overtime
    val hustlerCashBonus: Double get() = hustlerLevel * 18.0

    // 2. Efficiency Expert: 7% discount on daily facility maintenance per level (up to 49%)
    val maintenanceDiscountPercent: Double get() = (efficiencyExpertLevel * 0.07).coerceAtMost(0.49)

    // 3. Silver Tongue: +35% bonus to reputation price boost factor per level
    val silverTongueRepBonusMultiplier: Double get() = 1.0 + (silverTongueLevel * 0.35)

    // 4. Bovine Genetics: +20% raw milk quality and +2 pasture yield
    val milkQualityBonus: Double get() = bovineGeneticsLevel * 0.20
    val pastureYieldBonus: Int get() = bovineGeneticsLevel * 2

    // 5. Cold-Chain Logistics: +2 shelf life days
    val shelfLifeBonus: Int get() = coldChainLogisticsLevel * 2

    fun getLevel(skillType: SkillType): Int = when (skillType) {
        SkillType.HUSTLER -> hustlerLevel
        SkillType.EFFICIENCY_EXPERT -> efficiencyExpertLevel
        SkillType.SILVER_TONGUE -> silverTongueLevel
        SkillType.STAMINA -> staminaLevel
        SkillType.BOVINE_GENETICS -> bovineGeneticsLevel
        SkillType.COLD_CHAIN_LOGISTICS -> coldChainLogisticsLevel
        SkillType.MULTI_TASKING -> multiTaskingLevel
    }
}

/**
 * Skill definitions and scaling RP cost formula.
 */
enum class SkillType(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val iconEmoji: String,
    val maxLevel: Int = 5
) {
    STAMINA(
        id = "stamina",
        title = "Executive Stamina",
        subtitle = "Daily Action Limit",
        description = "Time management and executive vigor! Permanently increases maximum daily actions by +1 per level.",
        iconEmoji = "⚡"
    ),
    HUSTLER(
        id = "hustler",
        title = "Hustler",
        subtitle = "Manual Labor Yield",
        description = "Increases flat Cash earned when clicking the manual 'Work' overtime button (+ $18.00 per level).",
        iconEmoji = "💼"
    ),
    EFFICIENCY_EXPERT(
        id = "efficiency_expert",
        title = "Efficiency Expert",
        subtitle = "Operating Cost Reduction",
        description = "Streamlines facility maintenance and machinery lubrication, reducing daily upkeep fees by 7% per level.",
        iconEmoji = "⚙️"
    ),
    MULTI_TASKING(
        id = "multi_tasking",
        title = "Multi-Tasking",
        subtitle = "Personal Action Slots",
        description = "Boosts personal project bandwidth, increasing concurrent personal task capacity by 1 per level (max 5).",
        iconEmoji = "🤹",
        maxLevel = 4
    ),
    SILVER_TONGUE(
        id = "silver_tongue",
        title = "Silver Tongue",
        subtitle = "Market Reputation Premium",
        description = "Corporate negotiating prowess! Boosts the price premium gained from your Brand Reputation when selling in the Market.",
        iconEmoji = "🎙️"
    ),
    BOVINE_GENETICS(
        id = "bovine_genetics",
        title = "Bovine Genetics & Meadow Flora",
        subtitle = "Herd Yield & Quality Boost",
        description = "Pedigree lineage selection and nutrient-dense clover seeded pasture plots. Increases baseline raw milk quality by +20% and pasture yield by +2 units daily.",
        iconEmoji = "🧬",
        maxLevel = 1
    ),
    COLD_CHAIN_LOGISTICS(
        id = "cold_chain_logistics",
        title = "Cold-Chain Logistics Fleet",
        subtitle = "Global Shelf-Life Extension",
        description = "Thermo-insulated holding tanks and active refrigerated storage distribution. +2 extra shelf life days for all manufactured dairy products across the enterprise.",
        iconEmoji = "❄️",
        maxLevel = 1
    );

    fun costForLevel(currentLevel: Int): Int {
        if (this == BOVINE_GENETICS) return 12
        if (this == COLD_CHAIN_LOGISTICS) return 20
        return 4 + (currentLevel * 4) // Lv0->1: 4 RP, Lv1->2: 8 RP, Lv2->3: 12 RP, Lv3->4: 16 RP, Lv4->5: 20 RP
    }
}

/**
 * Complete summary of End Day activities.
 */
data class DailyReport(
    val day: Int,
    val rawProducedUnits: Int = 0,
    val processedUnitsProduced: Int = 0,
    val spoiledUnitsCount: Int = 0,
    val maintenancePaid: Double = 0.0,
    val feedCostsPaid: Double = 0.0,
    val interestPaid: Double = 0.0,
    val researchPointsGained: Int = 0,
    val automatedSalesRevenue: Double = 0.0,
    val foreclosureSalesCount: Int = 0,
    val dividendsEarned: Double = 0.0,
    val event: NewsEvent? = null,
    val summaryNotes: List<String> = emptyList()
)
