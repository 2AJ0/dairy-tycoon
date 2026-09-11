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

enum class AchievementCategory(val displayName: String, val iconEmoji: String) {
    MILESTONE("Milestones", "🗺️"),
    TYCOON("Tycoon", "💼"),
    ABSURD("Absurd", "🤪")
}

data class Achievement(
    val id: String,
    val title: String,
    val hint: String,
    val isUnlocked: Boolean = false,
    val isHidden: Boolean = false,
    val category: AchievementCategory,
    val trophyEmoji: String,
    val unlockedDay: Int? = null
)

object AchievementCatalog {
    val INITIAL_ACHIEVEMENTS = listOf(
        // --- MILESTONES (Connected Timeline / Roadmap) ---
        Achievement(
            id = "milestone_first_drops",
            title = "First Milk Yield",
            hint = "Harvest your first fresh milk batch from pasture.",
            isUnlocked = true, // Starter
            isHidden = false,
            category = AchievementCategory.MILESTONE,
            trophyEmoji = "🥛",
            unlockedDay = 1
        ),
        Achievement(
            id = "milestone_first_trade",
            title = "Open Market Debut",
            hint = "Sell your first commodity batch on the wholesale exchange.",
            isUnlocked = false,
            isHidden = false,
            category = AchievementCategory.MILESTONE,
            trophyEmoji = "📈"
        ),
        Achievement(
            id = "milestone_rd_debut",
            title = "Dairy Science Debut",
            hint = "Unlock your first technology in the R&D Tech Tree.",
            isUnlocked = false,
            isHidden = false,
            category = AchievementCategory.MILESTONE,
            trophyEmoji = "🔬"
        ),
        Achievement(
            id = "milestone_industrial_age",
            title = "Industrial Revolution",
            hint = "Construct your first manufacturing plant (Cheese Vat, Butter Churn, etc.).",
            isUnlocked = false,
            isHidden = false,
            category = AchievementCategory.MILESTONE,
            trophyEmoji = "🏭"
        ),
        Achievement(
            id = "milestone_cold_chain",
            title = "Cold Chain Logistics",
            hint = "Research Cold Chain Logistics to extend all product shelf lives.",
            isUnlocked = false,
            isHidden = false,
            category = AchievementCategory.MILESTONE,
            trophyEmoji = "❄️"
        ),
        Achievement(
            id = "milestone_artisan_mastery",
            title = "Master of Artisans",
            hint = "Unlock and produce Gourmet Artisan Cheeses.",
            isUnlocked = false,
            isHidden = false,
            category = AchievementCategory.MILESTONE,
            trophyEmoji = "🧀"
        ),
        Achievement(
            id = "milestone_reputation_titan",
            title = "Corporate Titan",
            hint = "Reach 50+ enterprise market reputation.",
            isUnlocked = false,
            isHidden = false,
            category = AchievementCategory.MILESTONE,
            trophyEmoji = "⭐"
        ),
        Achievement(
            id = "milestone_century_club",
            title = "100-Day Dynasty",
            hint = "Survive and manage your empire for 25 operating days.",
            isUnlocked = false,
            isHidden = false,
            category = AchievementCategory.MILESTONE,
            trophyEmoji = "👑"
        ),

        // --- TYCOON (Trophy Room Grid) ---
        Achievement(
            id = "tycoon_ten_grand",
            title = "Ten Grand Capitalist",
            hint = "Accumulate $10,000 in liquid cash reserves.",
            isUnlocked = false,
            isHidden = false,
            category = AchievementCategory.TYCOON,
            trophyEmoji = "💵"
        ),
        Achievement(
            id = "tycoon_debt_free_baron",
            title = "Debt-Free Baron",
            hint = "Reach $20,000+ Net Worth with exactly $0 in bank loans.",
            isUnlocked = false,
            isHidden = false,
            category = AchievementCategory.TYCOON,
            trophyEmoji = "🏦"
        ),
        Achievement(
            id = "tycoon_executive_mindset",
            title = "Executive Suite",
            hint = "Upgrade any Player Executive Skill to Level 3 or higher.",
            isUnlocked = false,
            isHidden = false,
            category = AchievementCategory.TYCOON,
            trophyEmoji = "👔"
        ),
        Achievement(
            id = "tycoon_conglomerate",
            title = "Dairy Conglomerate",
            hint = "Construct 4 or more distinct facilities simultaneously.",
            isUnlocked = false,
            isHidden = false,
            category = AchievementCategory.TYCOON,
            trophyEmoji = "🏢"
        ),
        Achievement(
            id = "tycoon_hostile_takeover",
            title = "Corporate Raider",
            hint = "Acquire a 51%+ controlling voting stake in a rival dairy corporation.",
            isUnlocked = false,
            isHidden = false,
            category = AchievementCategory.TYCOON,
            trophyEmoji = "🏢"
        ),
        Achievement(
            id = "tycoon_diamond_cowbell",
            title = "The Diamond Cowbell",
            hint = "Maximize shareholder value and achieve total global dairy monopoly (Endgame Choice B).",
            isUnlocked = false,
            isHidden = true, // Hidden secret achievement!
            category = AchievementCategory.TYCOON,
            trophyEmoji = "🔔"
        ),
        Achievement(
            id = "tycoon_mega_fortune",
            title = "Dairy Centimillionaire",
            hint = "Reach $50,000 total enterprise net worth.",
            isUnlocked = false,
            isHidden = false,
            category = AchievementCategory.TYCOON,
            trophyEmoji = "💎"
        ),

        // --- ABSURD (Trophy Room Grid) ---
        Achievement(
            id = "absurd_golden_pitchfork",
            title = "The Golden Pitchfork",
            hint = "Subsidize dairy for the people and feed humanity over corporate profit (Endgame Choice A).",
            isUnlocked = false,
            isHidden = true, // Hidden secret achievement!
            category = AchievementCategory.ABSURD,
            trophyEmoji = "🔱"
        ),
        Achievement(
            id = "absurd_sour_fortune",
            title = "Sour Fortune",
            hint = "Hold 10 or more units of spoiled milk in your warehouse.",
            isUnlocked = false,
            isHidden = false,
            category = AchievementCategory.ABSURD,
            trophyEmoji = "🤢"
        ),
        Achievement(
            id = "absurd_subprime_farmer",
            title = "Subprime Mogul",
            hint = "Leverage your farm with over $4,000 in bank debt.",
            isUnlocked = false,
            isHidden = true, // Hidden secret achievement!
            category = AchievementCategory.ABSURD,
            trophyEmoji = "📉"
        ),
        Achievement(
            id = "absurd_stakhanovite",
            title = "Stakhanovite Workaholic",
            hint = "Grind manual labor overtime shifts on the farm.",
            isUnlocked = false,
            isHidden = false,
            category = AchievementCategory.ABSURD,
            trophyEmoji = "🚜"
        ),
        Achievement(
            id = "absurd_cheese_hoarder",
            title = "Cheese Dragon",
            hint = "Hoard 50+ units of cheeses and yogurts without selling them.",
            isUnlocked = false,
            isHidden = true, // Hidden secret achievement!
            category = AchievementCategory.ABSURD,
            trophyEmoji = "🧀"
        ),
        Achievement(
            id = "absurd_foreclosure_tightrope",
            title = "Repo Tightrope Walker",
            hint = "Reach critical bank foreclosure countdown (within 2 days of seizure).",
            isUnlocked = false,
            isHidden = true, // Hidden secret achievement!
            category = AchievementCategory.ABSURD,
            trophyEmoji = "🪓"
        )
    )
}
