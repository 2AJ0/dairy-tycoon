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

import java.util.UUID

/**
 * Competitor dairy corporation in the regional market.
 */
data class RivalCompany(
    val id: String,
    val name: String,
    val tickerSymbol: String,
    val logoEmoji: String,
    val tagline: String,
    val marketPower: Float, // Scale ~0.5f (small boutique) to 3.0f (mega conglomerate)
    val stockPrice: Double,
    val stockChangePercent: Double = 0.0,
    val specialtyProduct: String = ProductCatalog.RAW_MILK.id,
    val headquarters: String = "Geneva, Switzerland",
    val corporateTone: String = "Aggressive & Expansionist",
    val totalShares: Int = 100, // 100 circulating voting shares (51 = majority takeover)
    val dailyDividendPerShare: Double = 2.50,
    val subsidiaryPerkTitle: String,
    val subsidiaryPerkDescription: String,
    val isSmeared: Boolean = false,
    val smearDaysRemaining: Int = 0,
    val netWorth: Double = 500000.0,
    val defenseRating: Int = 10,
    val offenseRating: Int = 10,
    val targetSector: ProductCategory? = null,
    val hostilityToPlayer: Int = 0,
    val b2bLockoutDaysRemaining: Int = 0,
    val threatLevel: Int = 10,
    val marketShare: Float = 15.0f,
    val tier: RivalTier = RivalTier.LOCAL
) {
    val marketPowerFormatted: String
        get() = String.format("%.1fx", marketPower)
}

/**
 * Narrative Endgame choice after acquiring 51%+ of every rival corporation.
 */
enum class EndgameChoice(
    val title: String,
    val subtitle: String,
    val quote: String,
    val iconEmoji: String,
    val trophyName: String,
    val priceMultiplier: Double
) {
    SUBSIDIZE_FOR_THE_PEOPLE(
        title = "Subsidize for the People",
        subtitle = "Agrarian Benevolence & Global Food Security",
        quote = "“You remembered the soil, boy. You used the monopoly not to bleed the world dry, but to make sure no child ever goes to bed hungry.”",
        iconEmoji = "🌾",
        trophyName = "The Golden Pitchfork",
        priceMultiplier = 0.20 // 80% price slash for consumers
    ),
    MAXIMIZE_SHAREHOLDER_VALUE(
        title = "Maximize Shareholder Value",
        subtitle = "Unbridled Hyper-Capitalist Monopoly",
        quote = "“So this is what you wanted all along. Just another ruthless boardroom predator. May your gold keep you warm at night, Tycoon.”",
        iconEmoji = "💎",
        trophyName = "The Diamond Cowbell",
        priceMultiplier = 3.00 // 3.0x price multiplier
    )
}

/**
 * B2B Bulk supply contract offer or active agreement.
 */
enum class RivalTier {
    LOCAL, SPECIALIZED, GLOBAL
}

enum class AttackType {
    DDOS_FACILITY,
    LOGISTICS_HIJACK,
    FINANCIAL_PHISHING,
    HOSTILE_BUYOUT
}

data class PendingCyberAttack(
    val id: String = java.util.UUID.randomUUID().toString(),
    val attackerId: String, // Rival ID or "PLAYER"
    val targetId: String, // Rival ID or "PLAYER"
    val attackType: AttackType,
    val executionDay: Int
)

enum class ContractType {
    DAILY_QUOTA,
    BULK_DEADLINE
}

data class ContractOffer(
    val contractType: ContractType = ContractType.DAILY_QUOTA,
    val targetTotalQuantity: Int = 0,
    val fulfilledQuantity: Int = 0,
    val id: String = UUID.randomUUID().toString(),
    val rivalId: String,
    val targetProduct: String, // Product ID (e.g. raw_milk, pasteurized_milk, aged_cheddar, butter)
    val requiredQuantity: Int, // Daily quota in units
    val payoutAmount: Double, // Daily payout on fulfillment
    val durationDays: Int, // Total contract lifespan
    val isAccepted: Boolean = false,
    val daysRemaining: Int = durationDays,
    val penaltyAmount: Double = payoutAmount * 0.65 + 75.0, // Financial penalty if daily quota not met
    val bargainCount: Int = 0,
    val originalPayoutAmount: Double = payoutAmount,
    val fulfilledDays: Int = 0,
    val failedDays: Int = 0,
    val totalPaidOut: Double = 0.0,
    val expiresInDays: Int = 3,
    val isJunk: Boolean = false,
    val minReputationRequired: Int = 0,
    val strikes: Int = 0,
    val cashPenaltyPerMiss: Int = (payoutAmount * 1.5).toInt(),
    val rewardReductionPerStrike: Int = (payoutAmount * 0.2).toInt()
) {
    val isCompleted: Boolean get() = daysRemaining <= 0
    val progressPercent: Float
        get() = if (durationDays > 0) ((durationDays - daysRemaining).toFloat() / durationDays.toFloat()).coerceIn(0f, 1f) else 1f
    
    val totalPotentialValue: Double get() = payoutAmount * durationDays
}

/**
 * Initial catalog of competitor corporations and introductory contracts.
 */
object RivalCatalog {
    val MOOCORP = RivalCompany(
        id = "rival_moocorp",
        name = "MooCorp International",
        tickerSymbol = "\$MOO",
        logoEmoji = "🏢",
        tagline = "Globalized Bovine Monopolies",
        marketPower = 2.4f,
        stockPrice = 185.40,
        stockChangePercent = 2.3,
        specialtyProduct = ProductCatalog.PASTEURIZED_MILK.id,
        headquarters = "Chicago, USA",
        corporateTone = "Ruthless Institutional Buyer",
        totalShares = 100,
        dailyDividendPerShare = 3.50,
        subsidiaryPerkTitle = "Monopoly Pricing Power",
        subsidiaryPerkDescription = "+20% Global selling price on all dairy commodities across spot markets.",
        targetSector = ProductCategory.PROCESSED,
        tier = RivalTier.GLOBAL,
        defenseRating = 35,
        offenseRating = 40,
        netWorth = 850000.0
    )

    val GLOBAL_WHEY = RivalCompany(
        id = "rival_global_whey",
        name = "Global Whey Enterprises",
        tickerSymbol = "\$WHEY",
        logoEmoji = "🧪",
        tagline = "Industrial Grade Protein & Supplements",
        marketPower = 1.8f,
        stockPrice = 94.20,
        stockChangePercent = -0.8,
        specialtyProduct = ProductCatalog.WHEY.id,
        headquarters = "Frankfurt, Germany",
        corporateTone = "High-Volume Commodity Processor",
        totalShares = 100,
        dailyDividendPerShare = 2.20,
        subsidiaryPerkTitle = "Industrial Turbine Processing",
        subsidiaryPerkDescription = "+50% Facility manufacturing throughput capacity across all processing plants.",
        targetSector = ProductCategory.RAW,
        tier = RivalTier.GLOBAL,
        defenseRating = 20,
        offenseRating = 25,
        netWorth = 550000.0
    )

    val LACTO_DYNASTY = RivalCompany(
        id = "rival_lacto_dynasty",
        name = "LactoDynasty Holdings",
        tickerSymbol = "\$CHED",
        logoEmoji = "🧀",
        tagline = "Premium Artisanal Cheese Syndicate",
        marketPower = 1.2f,
        stockPrice = 142.80,
        stockChangePercent = 4.1,
        specialtyProduct = ProductCatalog.AGED_CHEDDAR.id,
        headquarters = "Lyon, France",
        corporateTone = "High-Margin Gourmet Purveyor",
        totalShares = 100,
        dailyDividendPerShare = 3.00,
        subsidiaryPerkTitle = "Artisanal Syndicate Prestige",
        subsidiaryPerkDescription = "+35% Premium selling price bonus on Gourmet Cheeses, Butter & Yogurt.",
        targetSector = ProductCategory.ARTISANAL,
        tier = RivalTier.SPECIALIZED,
        defenseRating = 25,
        offenseRating = 15,
        netWorth = 420000.0
    )

    val ALPINE_BOVINE = RivalCompany(
        id = "rival_alpine_bovine",
        name = "Alpine Peak Creameries",
        tickerSymbol = "\$ALPN",
        logoEmoji = "🏔️",
        tagline = "Pasture-Raised Luxury Dairy Consortium",
        marketPower = 0.9f,
        stockPrice = 68.50,
        stockChangePercent = 1.5,
        specialtyProduct = ProductCatalog.BUTTER.id,
        headquarters = "Bern, Switzerland",
        corporateTone = "Traditional Heritage Co-operative",
        totalShares = 100,
        dailyDividendPerShare = 1.80,
        subsidiaryPerkTitle = "Alpine Heritage Genetics",
        subsidiaryPerkDescription = "+30% Raw Milk harvest yield across all active Pastures.",
        targetSector = ProductCategory.PROCESSED,
        defenseRating = 15,
        offenseRating = 10,
        netWorth = 250000.0,
        tier = RivalTier.LOCAL
    )

    val NEON_COW = RivalCompany(
        id = "rival_neon_cow",
        name = "Neon Cow Genetics",
        tickerSymbol = "\$NEON",
        logoEmoji = "🧬",
        tagline = "Engineering the perfect bovine.",
        marketPower = 2.0f,
        stockPrice = 145.50,
        stockChangePercent = +1.2,
        specialtyProduct = ProductCatalog.BIO_SYNTH_DAIRY.id,
        headquarters = "Neo-Tokyo, Japan",
        corporateTone = "Futuristic Biotech Firm",
        totalShares = 100,
        dailyDividendPerShare = 3.50,
        subsidiaryPerkTitle = "Genomic Sequencing",
        subsidiaryPerkDescription = "+20% Base quality to all raw milk produced.",
        targetSector = ProductCategory.PROCESSED,
        tier = RivalTier.SPECIALIZED,
        defenseRating = 60,
        offenseRating = 40,
        netWorth = 1250000.0
    )

    val LACTOSE_LOGISTICS = RivalCompany(
        id = "rival_lactose_logistics",
        name = "Lactose Logistics",
        tickerSymbol = "\$LLOG",
        logoEmoji = "🚚",
        tagline = "Moving milk at the speed of light.",
        marketPower = 1.2f,
        stockPrice = 45.20,
        stockChangePercent = -0.3,
        specialtyProduct = ProductCatalog.PASTEURIZED_MILK.id,
        headquarters = "Chicago, IL",
        corporateTone = "Gritty Supply Chain Empire",
        totalShares = 100,
        dailyDividendPerShare = 1.00,
        subsidiaryPerkTitle = "Supply Chain Dominance",
        subsidiaryPerkDescription = "Reduces logistics and maintenance costs by 15%.",
        targetSector = ProductCategory.RAW,
        tier = RivalTier.LOCAL,
        defenseRating = 25,
        offenseRating = 15,
        netWorth = 250000.0
    )

    val ALL_RIVALS: List<RivalCompany> = listOf(
        MOOCORP,
        GLOBAL_WHEY,
        LACTO_DYNASTY,
        ALPINE_BOVINE,
        NEON_COW,
        LACTOSE_LOGISTICS
    )

    fun getRivalById(id: String): RivalCompany {
        return ALL_RIVALS.find { it.id == id } ?: MOOCORP
    }

    val INITIAL_OFFERS: List<ContractOffer> = listOf(
        ContractOffer(
            id = "contract_intro_moocorp",
            rivalId = MOOCORP.id,
            targetProduct = ProductCatalog.RAW_MILK.id,
            requiredQuantity = 12,
            payoutAmount = 45.0, // $3.75/unit vs $2.50 base (+50% premium!)
            durationDays = 3,
            penaltyAmount = 60.0,
            expiresInDays = 3
        ),
        ContractOffer(
            id = "contract_intro_whey",
            rivalId = GLOBAL_WHEY.id,
            targetProduct = ProductCatalog.RAW_MILK.id,
            requiredQuantity = 20,
            payoutAmount = 70.0, // $3.50/unit (+40% premium!)
            durationDays = 4,
            penaltyAmount = 90.0,
            expiresInDays = 2
        )
    )
}
