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
 * Type of active construction or research project.
 */

data class DailyFinancialRecord(
    val day: Int,
    val totalRevenue: Double,
    val totalExpenses: Double
)

enum class ProjectType {
    FACILITY_CONSTRUCTION,
    FACILITY_UPGRADE,
    TECH_RESEARCH
}

enum class NetWorthPhase {
    STARTUP,
    TENSION,
    CORPORATE
}

enum class TopBarIconTheme(val emoji: String) {
    STARTUP("🥛"),
    INDUSTRIAL("🏭"),
    CORPORATE("🏢")
}

enum class GuidanceState {
    GOOD,
    BAD,
    HIDDEN
}

/**
 * Time-gated construction or R&D project with Rush mechanics.
 */
data class ActiveProject(
    val id: String = java.util.UUID.randomUUID().toString(),
    val type: ProjectType,
    val targetId: String,
    val targetName: String,
    val iconEmoji: String,
    val totalDays: Int,
    val daysRemaining: Int,
    val targetLevel: Int = 1,
    val rushCostPerDay: Double = 150.0,
    val isDedicated: Boolean = false
) {
    val totalRushCost: Double get() = (daysRemaining * rushCostPerDay).coerceAtLeast(50.0)
    val progress: Float get() = if (totalDays > 0) ((totalDays - daysRemaining).toFloat() / totalDays).coerceIn(0f, 1f) else 1f
}

/**
 * Enterprise lifetime statistics tracked over the entire game session.
 */
data class LifetimeStats(
    val totalDaysPlayed: Int = 1,
    val totalCashEarned: Double = 0.0,
    val totalMilkProduced: Long = 0,
    val totalProductsProcessed: Long = 0,
    val totalContractsFulfilled: Int = 0,
    val facilitiesBuilt: Int = 1,
    val totalResearchPointsEarned: Int = 0,
    val totalDividendsEarned: Double = 0.0,
    val totalSpoiledUnits: Long = 0,
    val totalRushCount: Int = 0,
    val totalLandPlotsBought: Int = 0,
    val totalManualWorkClicks: Int = 0,
    val totalManualResearchClicks: Int = 0,
    val totalRawMilkProduced: Double = 0.0,
    val totalProcessedGoodsProduced: Double = 0.0,
    val totalRevenueEarned: Double = 0.0,
    val totalMaintenancePaid: Double = 0.0,
    val totalInterestPaid: Double = 0.0,
    val totalContractsCompleted: Int = 0,
    val totalSmearCampaignsRun: Int = 0,
    val totalSharesPurchased: Int = 0,
    val totalSharesSold: Int = 0,
    val totalProjectsCompleted: Int = 0,
    val totalProjectsRushed: Int = 0,
    val highestNetWorthAchieved: Double = 0.0,
    val daysPlayed: Int = 1
)

data class UnlockedFeatures(
    val isBoardroomUnlocked: Boolean = false,
    val isStockMarketUnlocked: Boolean = false,
    val isSecurityUnlocked: Boolean = false,
    val isBoardroomNew: Boolean = false,
    val isStockMarketNew: Boolean = false,
    val isSecurityNew: Boolean = false
)

data class SpunOffSubsidiary(
    val id: String = java.util.UUID.randomUUID().toString(),
    val customName: String = "",
    val parentFacilityId: String = "",
    val dividendSlider: Float = 0.5f,
    val internalCapital: Double = 0.0,
    val qualityMultiplier: Float = 1.0f
)

enum class CrisisModifierType {
    MARKET_CRASH, // All sell prices -30%
    SUPPLY_SHORTAGE // Raw material costs +50%
}

data class CrisisEvent(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String = "",
    val description: String = "",
    val durationDays: Int = 0,
    val modifierType: CrisisModifierType = CrisisModifierType.MARKET_CRASH
)

enum class InventoryMethod { FIFO, LIFO }

/**
 * Root state of Dairy Tycoon simulation.
 */
data class GameState(
    val inventoryMethod: InventoryMethod = InventoryMethod.FIFO,
    val day: Int = 1,
    val cash: Double = 1250.0,
    val reputation: Int = 15, // 0 to 100
    val reputationBreakdown: ReputationBreakdown = ReputationBreakdown(),
    val researchPoints: Int = 0,
    val unlockedTechIds: Set<String> = emptySet(),
    val playerSkills: PlayerSkills = PlayerSkills(),
    val dailyActionsRemaining: Int = 5,
    val maxLandCapacity: Int = 4,
    val purchasedLandPlots: Int = 0,
    val activeProjects: List<ActiveProject> = emptyList(),
    val stats: LifetimeStats = LifetimeStats(),
    val activeNewsEvent: NewsEvent? = null,
    val financialHistory: List<DailyFinancialRecord> = emptyList(),
    val inventory: List<InventoryBatch> = listOf(
        InventoryBatch(
            itemId = ProductCatalog.RAW_MILK.id,
            itemName = ProductCatalog.RAW_MILK.name,
            quantity = 25,
            quality = 1.2,
            maxShelfLife = ProductCatalog.RAW_MILK.shelfLifeDays,
            dayProduced = 1
        )
    ),
    val buildings: List<Building> = DefaultBuildings.getInitialBuildings(),
    val marketPrices: Map<String, MarketItemState> = defaultMarketStates(),
    val bank: BankState = BankState(),
    val todaySoldUnits: Map<String, Int> = emptyMap(),
    val dailyLogs: List<String> = listOf("Welcome to Dairy Tycoon! Your mentor Barnaby says: 'Mind the milk shelf life, boy!'"),
    val latestReport: DailyReport? = null,
    val isGameOver: Boolean = false,
    val gameOverReason: String? = null,
    val totalDaysPlayed: Int = 1,
    val manualLaborCount: Int = 0,
    val achievements: List<Achievement> = AchievementCatalog.INITIAL_ACHIEVEMENTS,
    val rivalCompanies: List<RivalCompany> = RivalCatalog.ALL_RIVALS,
    val pendingContractOffers: List<ContractOffer> = RivalCatalog.INITIAL_OFFERS,
    val activeContracts: List<ContractOffer> = emptyList(),
    val completedContractsCount: Int = 0,
    val totalContractRevenueEarned: Double = 0.0,
    val rivalSharesOwned: Map<String, Int> = emptyMap(), // rivalId -> share count (0 to 100)
    val subsidiaryCompanyIds: Set<String> = emptySet(), // Companies with 51%+ acquired shares
    val isEndgameTriggered: Boolean = false,
    val isEndgameCompleted: Boolean = false,
    val endgameChoice: EndgameChoice? = null,
    val endgamePriceMultiplier: Double = 1.0,
    val executives: List<Executive> = ExecutiveCatalog.INITIAL_EXECUTIVES,
    val mentors: List<Mentor> = MentorCatalog.ALL_MENTORS,
    val netWorthPhase: NetWorthPhase = NetWorthPhase.STARTUP,
    val isB2BUnlocked: Boolean = false,
    val unlockedFeatures: UnlockedFeatures = UnlockedFeatures(),
    val autoBuySubscriptions: Map<String, Boolean> = emptyMap(),
    val autoSellSubscriptions: Map<String, Boolean> = emptyMap(),
    val saveId: String = java.util.UUID.randomUUID().toString(),
    val saveName: String = generateRandomEmpireName(),
    val gamePhase: GamePhase = GamePhase.FARMING,
    val hasFired100kEvent: Boolean = false,
    val coldStoragePriority: ColdStoragePriority = ColdStoragePriority.OLDEST_FIRST,
    val manualColdStorageAllocations: Map<String, Int> = emptyMap(),
    val researchNodeStatuses: Map<String, NodeStatus> = ResearchCatalog.ALL_NODES.associate { it.id to NodeStatus.LOCKED },
    val missedDeliveryEvent: String? = null,
    val offenseRating: Int = 10,
    val defenseRating: Int = 10,
    val pendingCyberAttacks: List<PendingCyberAttack> = emptyList(),
    val cyberAttackWarningEvent: PendingCyberAttack? = null,
    val playerMarketShare: Float = 25.0f,
    val peakNetWorth: Double = 0.0,
    val hostileTakeovers: Int = 0,
    val maxContractValue: Double = 0.0,
    val lifetimeRevenue: Double = 0.0,
    val attacksThwarted: Int = 0,
    val fraudLosses: Double = 0.0,
    val ddosDowntimeDays: Int = 0,
    val lifetimeProduced: Map<String, Int> = emptyMap(),
    val lifetimeSpoilage: Int = 0,
    val spunOffSubsidiaries: List<SpunOffSubsidiary> = emptyList(),
    val activeCrises: List<CrisisEvent> = emptyList(),
    val newCrisisFired: CrisisEvent? = null,
    val isStandardTreeComplete: Boolean = false,
    val showAweDialogue: Boolean = false,
    val showAweHighlight: Boolean = false,
    val hasSeenAweTutorial: Boolean = false,
    val unlockedAnomalies: List<String> = emptyList(),
    val readStoryEvents: Set<String> = emptySet(),
    val pendingStoryEvents: List<StoryEvent> = emptyList()
) {
    val globalColdStorageCapacity: Int
        get() = buildings.filter { it.type == BuildingType.COLD_STORAGE && it.isConstructed && it.isOperational }.sumOf { it.level * 50 }

    val activeTopBarIcon: TopBarIconTheme get() = when (netWorthPhase) {
        NetWorthPhase.STARTUP -> TopBarIconTheme.STARTUP
        NetWorthPhase.TENSION -> TopBarIconTheme.INDUSTRIAL
        NetWorthPhase.CORPORATE -> TopBarIconTheme.CORPORATE
    }

    val ownedLabs: Int get() = unlockedTechIds.count { it.startsWith("tech_dedicated_lab_") }
    val ownedConstructionCrews: Int get() = unlockedTechIds.count { it.startsWith("tech_construction_crew_") }

    val activeExecutives: List<Executive> get() = executives.filter { it.isHired }
    val totalExecutiveSalary: Double get() = activeExecutives.sumOf { it.dailySalary }
    
    val totalInventoryQuantity: Int get() = inventory.sumOf { it.quantity }
    
    val totalInventoryValue: Double
        get() = inventory.sumOf { batch ->
            val marketState = marketPrices[batch.itemId]
            val currentPrice = marketState?.currentPrice ?: 1.0
            // Quality multiplies price (e.g. 1.2x quality = 1.2x value)
            batch.quantity * currentPrice * (0.8 + (batch.quality * 0.2))
        }

    val totalBuildingValue: Double
        get() = buildings.filter { it.isConstructed }.sumOf { it.baseCost * it.level * 0.75 }

    val totalPortfolioValue: Double
        get() = rivalSharesOwned.entries.sumOf { (rivalId, shares) ->
            val rival = rivalCompanies.find { it.id == rivalId } ?: RivalCatalog.getRivalById(rivalId)
            shares * rival.stockPrice
        }

    val totalDailyDividends: Double
        get() = rivalSharesOwned.entries.sumOf { (rivalId, shares) ->
            val rival = rivalCompanies.find { it.id == rivalId } ?: RivalCatalog.getRivalById(rivalId)
            shares * rival.dailyDividendPerShare
        }

    val netWorth: Double
        get() = cash + totalInventoryValue + totalBuildingValue + totalPortfolioValue - bank.totalDebt

    val maxDailyActions: Int get() = playerSkills.maxDailyActions

    val totalLandCapacity: Int get() = maxLandCapacity + purchasedLandPlots
    val availableLand: Int get() = (totalLandCapacity - usedLand).coerceAtLeast(0)
    val lifetimeStats: LifetimeStats get() = stats

    val usedLand: Int
        get() {
            val constructedLand = buildings.filter { it.isConstructed }.sumOf { it.landRequired }
            val underConstructionLand = activeProjects
                .filter { it.type == ProjectType.FACILITY_CONSTRUCTION }
                .sumOf { proj ->
                    buildings.find { it.id == proj.targetId }?.landRequired ?: 1
                }
            return constructedLand + underConstructionLand
        }

    val nextLandCost: Double
        get() = 5000.0 * Math.pow(3.0, purchasedLandPlots.toDouble())

    fun isBuildingUnderConstruction(buildingId: String): Boolean =
        activeProjects.any { it.type == ProjectType.FACILITY_CONSTRUCTION && it.targetId == buildingId }

    fun isBuildingUnderUpgrade(buildingId: String): Boolean =
        activeProjects.any { it.type == ProjectType.FACILITY_UPGRADE && it.targetId == buildingId }

    fun isTechUnderResearch(techId: String): Boolean =
        activeProjects.any { it.type == ProjectType.TECH_RESEARCH && it.targetId == techId }

    fun getProjectForBuilding(buildingId: String): ActiveProject? =
        activeProjects.find { (it.type == ProjectType.FACILITY_CONSTRUCTION || it.type == ProjectType.FACILITY_UPGRADE) && it.targetId == buildingId }

    fun getProjectForTech(techId: String): ActiveProject? =
        activeProjects.find { it.type == ProjectType.TECH_RESEARCH && it.targetId == techId }

    fun getSharesOwned(rivalId: String): Int = rivalSharesOwned[rivalId] ?: 0
    fun getOwnershipPercent(rivalId: String): Int {
        val owned = getSharesOwned(rivalId)
        val rival = rivalCompanies.find { it.id == rivalId } ?: RivalCatalog.getRivalById(rivalId)
        return if (rival.totalShares > 0) ((owned.toDouble() / rival.totalShares) * 100).toInt() else 0
    }
    fun isSubsidiary(rivalId: String): Boolean = subsidiaryCompanyIds.contains(rivalId)

    val totalDailyMaintenance: Double
        get() {
            val raw = buildings.filter { it.isConstructed && it.isOperational }.sumOf { it.currentMaintenance }
            return raw * (1.0 - playerSkills.maintenanceDiscountPercent)
        }

    val totalDailyMilkYield: Int
        get() = buildings.filter { it.type == BuildingType.PASTURE && it.isConstructed && it.isOperational }.sumOf { it.currentDailyRawProduction }

    val playerDefenseRating: Int
        get() {
            var rating = 10 // base
            if (unlockedTechIds.contains("tech_cybersecurity_1")) rating += 15
            if (unlockedTechIds.contains("tech_cybersecurity_2")) rating += 25
            if (activeExecutives.any { it.role == ExecutiveRole.CISO }) rating += 20
            return rating
        }
        
    val playerOffenseRating: Int
        get() {
            var rating = 5 // base
            if (unlockedTechIds.contains("tech_hostile_ai")) rating += 30
            if (activeExecutives.any { it.role == ExecutiveRole.CISO }) rating += 10
            return rating
        }
}

fun defaultMarketStates(): Map<String, MarketItemState> {
    return ProductCatalog.ALL_PRODUCTS.associate { product ->
        product.id to MarketItemState(
            itemId = product.id,
            currentPrice = product.basePrice,
            basePrice = product.basePrice,
            yesterdayUnitsSold = 0,
            priceChangePercent = 0.0,
            priceHistory = listOf(product.basePrice)
        )
    }
}

fun generateRandomEmpireName(): String {
    val prefixes = listOf("Golden", "Sunny", "Automated", "Merku", "Pananchery", "Quantum")
    val suffixes = listOf("Meadows", "Dairies", "Acres", "Pastures", "Holdings")
    return "${prefixes.random()} ${suffixes.random()}"
}
