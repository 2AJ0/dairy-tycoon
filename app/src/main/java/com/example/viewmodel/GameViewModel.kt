package com.example.viewmodel

/**
 * ==========================================
 * AI / IDE AGENT INSTRUCTIONS:
 * ==========================================
 * This is the GameViewModel, the central state machine for Dairy Tycoon.
 * 
 * CORE RULES FOR FIXING ERRORS:
 * 1. IMMUTABILITY: `GameState` is immutable. All state mutations MUST happen via `_gameState.update { it.copy(...) }`.
 * 2. END DAY LOOP: The `endDay()` function is a massive, strictly ordered pipeline (Spoilage -> B2B -> Market -> Construction -> Story -> Save). 
 *    - DO NOT alter the sequence of operations.
 *    - DO NOT change the mathematical formulas for profit, capacity, or subsidiary dividends.
 * 3. SAFE FIXES: If there are compilation errors (e.g., unresolved references, type mismatches), fix the types or imports. Do not re-architect the loop.
 */

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import java.util.UUID
import com.example.data.SaveGameManager
import com.example.data.SaveSummary
import com.example.data.SettingsRepository
import com.example.model.Achievement
import com.example.model.AchievementCatalog
import com.example.model.AchievementCategory
import com.example.model.ActiveProject
import com.example.model.BankState
import com.example.model.Building
import com.example.model.BuildingType
import com.example.model.AttackType
import com.example.model.ContractOffer
import com.example.model.ContractType
import com.example.model.DailyReport
import com.example.model.EndgameChoice
import com.example.model.Executive
import com.example.model.ExecutiveRole
import com.example.model.GameState
import com.example.model.generateRandomEmpireName
import com.example.model.InventoryBatch
import com.example.model.InventoryMethod
import com.example.model.LifetimeStats
import com.example.model.MarketItemState
import com.example.model.ChatMessage
import com.example.model.MessageStatus
import com.example.model.Mentor
import com.example.model.NewsCatalog
import com.example.model.NewsEvent
import com.example.model.PendingCyberAttack
import com.example.model.PlayerSkills
import com.example.model.ProductCatalog
import com.example.model.ProcessingRecipe
import com.example.model.ProjectType
import com.example.model.RivalCatalog
import com.example.model.RivalCompany
import com.example.model.RivalTier
import com.example.model.SkillType
import com.example.model.TechCatalog
import com.example.model.ResearchCategory
import com.example.model.NetWorthPhase
import com.example.model.GameAction
import com.example.model.GuidanceState
import com.example.model.DefaultBuildings
import com.example.ui.screens.DrawerDestination
import com.example.model.ReputationBreakdown
import com.example.model.NodeStatus
import com.example.model.ColdStoragePriority
import com.example.model.GamePhase
import com.example.model.ResearchCatalog
import com.example.model.MentorCatalog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel(application: Application) : AndroidViewModel(application) {



    private val saveGameManager = SaveGameManager(application.applicationContext)
    private val settingsRepository = SettingsRepository(application.applicationContext)

    val soundEffectsEnabled: StateFlow<Boolean> = settingsRepository.soundEnabledFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )

    val newsAlertsEnabled: StateFlow<Boolean> = settingsRepository.notificationsEnabledFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )

    fun toggleSoundEffects(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setSoundEnabled(enabled) }
    }

    fun toggleNewsAlerts(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setNotificationsEnabled(enabled) }
    }

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _hasSavedGame = MutableStateFlow(saveGameManager.hasSavedGame())
    val hasSavedGame: StateFlow<Boolean> = _hasSavedGame.asStateFlow()

    private val _saveSummary = MutableStateFlow<SaveSummary?>(saveGameManager.getSaveSummary())
    val saveSummary: StateFlow<SaveSummary?> = _saveSummary.asStateFlow()

    private val _showDailyReportDialog = MutableStateFlow(false)
    val showDailyReportDialog: StateFlow<Boolean> = _showDailyReportDialog.asStateFlow()

    private val _showNewsChronicleDialog = MutableStateFlow(false)
    val showNewsChronicleDialog: StateFlow<Boolean> = _showNewsChronicleDialog.asStateFlow()

    private val _showEndgameDialog = MutableStateFlow(false)
    val showEndgameDialog: StateFlow<Boolean> = _showEndgameDialog.asStateFlow()
    
    private val _showMilestoneScreen = MutableStateFlow(false)
    val showMilestoneScreen: StateFlow<Boolean> = _showMilestoneScreen.asStateFlow()

    private val _showCrossroadsDialog = MutableStateFlow(false)
    val showCrossroadsDialog: StateFlow<Boolean> = _showCrossroadsDialog.asStateFlow()

    private val _snackBarMessage = MutableStateFlow<String?>(null)
    val snackBarMessage: StateFlow<String?> = _snackBarMessage.asStateFlow()

    // Research Hub Cached State Flows
    val techNodesProducts = MutableStateFlow(com.example.model.TechCatalog.ALL_TECHS.filter { it.category == com.example.model.ResearchCategory.PRODUCTS }).asStateFlow()
    val techNodesIndustry = MutableStateFlow(com.example.model.TechCatalog.ALL_TECHS.filter { it.category == com.example.model.ResearchCategory.INDUSTRY }).asStateFlow()
    val techNodesCompany = MutableStateFlow(com.example.model.TechCatalog.ALL_TECHS.filter { it.category == com.example.model.ResearchCategory.COMPANY }).asStateFlow()
    val techNodesPersonal = MutableStateFlow(com.example.model.TechCatalog.ALL_TECHS.filter { it.category == com.example.model.ResearchCategory.PERSONAL }).asStateFlow()

    fun clearSnackBar() {
        _snackBarMessage.value = null
    }

    fun dismissDailyReport() {
        _showDailyReportDialog.value = false
    }

    fun dismissNewsChronicle() {
        _showNewsChronicleDialog.value = false
    }

    fun openNewsChronicle() {
        _showNewsChronicleDialog.value = true
    }

    fun dismissEndgameDialog() {
        _showEndgameDialog.value = false
    }

    fun openEndgameDialog() {
        _showEndgameDialog.value = true
    }

    fun dismissCrossroadsDialog() {
        _showCrossroadsDialog.value = false
    }

    fun chooseCrossroadsOption(incorporate: Boolean) {
        _showCrossroadsDialog.value = false
        if (incorporate) {
            _gameState.update { it.copy(netWorthPhase = NetWorthPhase.CORPORATE) }
            _snackBarMessage.value = "Phase Shift: Welcome to Corporate Life."
        } else {
            _gameState.update { it.copy(isGameOver = true, gameOverReason = "You chose the peaceful farm life. Barnaby is proud.") }
        }
    }

    fun evaluateAction(action: GameAction): GuidanceState {
        val state = _gameState.value
        if (state.netWorthPhase == NetWorthPhase.CORPORATE) return GuidanceState.HIDDEN

        return when (state.netWorthPhase) {
            NetWorthPhase.STARTUP -> {
                when (action) {
                    is GameAction.Construct -> {
                        val b = DefaultBuildings.getInitialBuildings().find { it.id == action.buildingId }
                        if (b != null && state.cash >= b.baseCost * 2) GuidanceState.GOOD else GuidanceState.BAD
                    }
                    is GameAction.Upgrade -> GuidanceState.GOOD
                    is GameAction.Research -> GuidanceState.GOOD
                    is GameAction.Contract -> GuidanceState.GOOD
                    is GameAction.Sell -> GuidanceState.GOOD
                }
            }
            NetWorthPhase.TENSION -> {
                when (action) {
                    is GameAction.Construct -> {
                        val b = DefaultBuildings.getInitialBuildings().find { it.id == action.buildingId }
                        if (b?.type == BuildingType.RD_LAB || b?.type == BuildingType.PROCESSING_PLANT) GuidanceState.BAD
                        else GuidanceState.GOOD
                    }
                    is GameAction.Upgrade -> GuidanceState.BAD
                    is GameAction.Research -> GuidanceState.BAD
                    is GameAction.Contract -> GuidanceState.BAD
                    is GameAction.Sell -> GuidanceState.GOOD
                }
            }
            NetWorthPhase.CORPORATE -> GuidanceState.HIDDEN
        }
    }

    /**
     * Checks achievement conditions across Milestones, Tycoon, and Absurd categories.
     */
    fun checkAchievements(state: GameState): GameState {
        val currentAchievements = state.achievements
        var newlyUnlockedName: String? = null
        var newlyUnlockedEmoji: String = "🏆"

        val updatedAchievements = currentAchievements.map { ach ->
            if (ach.isUnlocked) {
                ach
            } else {
                val shouldUnlock = when (ach.id) {
                    // Milestones
                    "milestone_first_drops" -> state.inventory.any { it.itemId == ProductCatalog.RAW_MILK.id }
                    "milestone_first_trade" -> state.todaySoldUnits.isNotEmpty() || state.totalDaysPlayed > 1
                    "milestone_rd_debut" -> state.unlockedTechIds.isNotEmpty()
                    "milestone_industrial_age" -> state.buildings.any { it.isConstructed && it.type != BuildingType.PASTURE && it.type != BuildingType.RD_LAB }
                    "milestone_cold_chain" -> state.playerSkills.getLevel(SkillType.COLD_CHAIN_LOGISTICS) > 0
                    "milestone_artisan_mastery" -> state.unlockedTechIds.contains("tech_cheese_aging") || state.inventory.any { it.itemId in listOf(ProductCatalog.AGED_CHEDDAR.id, ProductCatalog.FRESH_CHEESE.id, ProductCatalog.BUTTER.id) }
                    "milestone_reputation_titan" -> state.reputation >= 50
                    "milestone_century_club" -> state.day >= 25

                    // Tycoon
                    "tycoon_ten_grand" -> state.cash >= 10000.0
                    "tycoon_debt_free_baron" -> state.netWorth >= 20000.0 && state.bank.totalDebt <= 0.0
                    "tycoon_executive_mindset" -> state.playerSkills.hustlerLevel >= 3 || state.playerSkills.efficiencyExpertLevel >= 3 || state.playerSkills.silverTongueLevel >= 3
                    "tycoon_conglomerate" -> state.buildings.count { it.isConstructed } >= 4
                    "tycoon_hostile_takeover" -> state.subsidiaryCompanyIds.isNotEmpty()
                    "tycoon_diamond_cowbell" -> state.endgameChoice == EndgameChoice.MAXIMIZE_SHAREHOLDER_VALUE
                    "tycoon_mega_fortune" -> state.netWorth >= 50000.0

                    // Absurd
                    "absurd_sour_fortune" -> state.inventory.filter { it.isSpoiled || it.itemId == ProductCatalog.SPOILED_MILK.id }.sumOf { it.quantity } >= 10
                    "absurd_subprime_farmer" -> state.bank.totalDebt >= 4000.0
                    "absurd_stakhanovite" -> state.manualLaborCount >= 5
                    "absurd_cheese_hoarder" -> state.inventory.filter { it.itemId in listOf(ProductCatalog.AGED_CHEDDAR.id, ProductCatalog.FRESH_CHEESE.id, ProductCatalog.BUTTER.id, ProductCatalog.CREAM.id) }.sumOf { it.quantity } >= 40
                    "absurd_foreclosure_tightrope" -> state.bank.daysInDebt >= 5
                    "absurd_golden_pitchfork" -> state.endgameChoice == EndgameChoice.SUBSIDIZE_FOR_THE_PEOPLE
                    else -> false
                }

                if (shouldUnlock) {
                    if (newlyUnlockedName == null) {
                        newlyUnlockedName = ach.title
                        newlyUnlockedEmoji = ach.trophyEmoji
                    }
                    ach.copy(isUnlocked = true, unlockedDay = state.day)
                } else {
                    ach
                }
            }
        }

        if (newlyUnlockedName != null) {
            _snackBarMessage.value = "🏆 Achievement Unlocked: $newlyUnlockedEmoji $newlyUnlockedName!"
        }

        return state.copy(achievements = updatedAchievements)
    }

    /**
     * Master End-Day simulation loop.
     * 1. Daily News Events & Maintenance Deductions (applying Efficiency Expert skill)
     * 2. Production Engine & Facility Processing (Consuming oldest inputs in FIFO order)
     * 3. Inventory Aging & Spoilage (Preexisting batches age; fresh batches remain Age 0)
     * 4. Debt & Foreclosure Auto-liquidation
     * 5. Dynamic Market Spot Pricing (applying active NewsEvent multiplier & Silver Tongue)
     * 6. State Consolidation, Daily Report, and Morning Paper Popup
     */
    private fun calculateDailyUsage(state: GameState, itemId: String): Int {
        var needed = 0
        if (itemId == "cow_feed") {
            // Pastures consume 1 feed per pasture level
            needed += state.buildings.filter { it.type == BuildingType.PASTURE && it.isConstructed && it.isOperational }.sumOf { it.level }
        } else if (itemId == "glass_bottles") {
            // Pasteurizers consume 1 bottle per max processing capacity unit
            needed += state.buildings.filter { it.isConstructed && it.isOperational && it.activeRecipe?.outputItemId == "pasteurized_milk" }.sumOf { it.currentProcessingCapacity }
        }
        return needed
    }

    private fun calculateDailyReputation(state: GameState, processedItems: Set<String>): Pair<Int, ReputationBreakdown> {
        val livestockScore = (state.buildings.filter { it.type == BuildingType.PASTURE && it.isConstructed }.sumOf { it.level } * 2).coerceAtMost(25)
        val scaleScore = (state.buildings.count { it.isConstructed } * 2).coerceAtMost(25)
        
        val qualityScore = if (state.inventory.any { !it.isSpoiled }) {
            (state.inventory.filter { !it.isSpoiled }.map { it.quality }.average() * 10).toInt().coerceAtMost(30)
        } else 5

        val baseRep = livestockScore + scaleScore + qualityScore
        val breakdown = ReputationBreakdown(
            livestockScore = livestockScore,
            scaleScore = scaleScore,
            qualityScore = qualityScore,
            penalties = state.reputationBreakdown.penalties,
            totalReputation = baseRep - state.reputationBreakdown.penalties
        )
        return Pair(breakdown.totalReputation.coerceIn(0, 100), breakdown)
    }

    fun endDay() {
        android.util.Log.d("TycoonDebug", "Starting End Day...")
        if (_gameState.value.isGameOver) return
        
        if (_gameState.value.netWorth >= 100_000.0 && _gameState.value.netWorthPhase != NetWorthPhase.CORPORATE) {
            _showCrossroadsDialog.value = true
            return
        }

        /**
         * Executes the main game loop simulating the passage of one day.
         */
        viewModelScope.launch {
            var triggeredNews: NewsEvent? = null

            _gameState.update { currentState ->
                val currentDay = currentState.day
                val notes = mutableListOf<String>()
                val newlyCompletedTechIds = mutableListOf<String>()

                var b2bUnlocked = currentState.isB2BUnlocked
                if (!b2bUnlocked) {
                    val barnabyAffinity = currentState.mentors.find { it.id == "mentor_barnaby" }?.hiddenAffinity ?: 0
                    if (currentDay >= 25 || currentState.reputation >= 20 || barnabyAffinity >= 10) {
                        b2bUnlocked = true
                    }
                }

                // 0. Auto-Buy Procurement Phase (Consumables)
                var currentLiquidCash = currentState.cash
                val workingInventory = currentState.inventory.toMutableList()
                
                currentState.autoBuySubscriptions.forEach { (itemId, isActive) ->
                    if (isActive) {
                        val needed = calculateDailyUsage(currentState, itemId)
                        if (needed > 0) {
                            val spotPrice = currentState.marketPrices[itemId]?.currentPrice ?: com.example.model.ProductCatalog.getById(itemId).basePrice
                            val affordableUnits = (currentLiquidCash / spotPrice).toInt()
                            
                            val unitsToBuy = minOf(affordableUnits, needed)
                            if (unitsToBuy > 0) {
                                currentLiquidCash -= (unitsToBuy * spotPrice)
                                workingInventory.add(
                                    com.example.model.InventoryBatch(
                                        itemId = itemId,
                                        itemName = com.example.model.ProductCatalog.getById(itemId).name,
                                        quantity = unitsToBuy,
                                        quality = 1.0,
                                        
                                        maxShelfLife = com.example.model.ProductCatalog.getById(itemId).shelfLifeDays,
                                        dayProduced = currentDay
                                    )
                                )
                                if (unitsToBuy < needed) {
                                    notes.add("⚠️ Low Cash: Auto-Buy partially fulfilled $unitsToBuy/$needed units of ${com.example.model.ProductCatalog.getById(itemId).name}.")
                                } else {
                                    notes.add("🛒 Auto-Buy procured $unitsToBuy units of ${com.example.model.ProductCatalog.getById(itemId).name}.")
                                }
                            } else {
                                notes.add("⚠️ Insufficient funds to Auto-Buy $needed units of ${com.example.model.ProductCatalog.getById(itemId).name}.")
                            }
                        }
                    }
                }

                // 1. Daily News Event Progression or Triggering
                var activeNews = currentState.activeNewsEvent
                var isNewEventTriggered = false

                if (activeNews != null) {
                    val remaining = activeNews.remainingDays - 1
                    if (remaining > 0) {
                        activeNews = activeNews.copy(remainingDays = remaining)
                    } else {
                        notes.add("Market buzz from '${activeNews.title}' has concluded.")
                        activeNews = null
                    }
                }

                // Random 30% chance for morning news event if none currently active
                if (activeNews == null && Random.nextDouble() < 0.35) {
                    val freshEvent = NewsCatalog.getRandomEvent()
                    activeNews = freshEvent
                    isNewEventTriggered = true
                    triggeredNews = freshEvent
                    notes.add("📰 Morning Chronicle: ${freshEvent.title}")
                }

                // Mentor Buffs & Proactive Advice
                val barnabyBuff = currentState.mentors.find { it.id == "mentor_barnaby" }?.isBuffUnlocked == true
                var chadBuff = currentState.mentors.find { it.id == "mentor_chad" }?.isBuffUnlocked == true
                val sterlingBuff = currentState.mentors.find { it.id == "mentor_sterling" }?.isBuffUnlocked == true
                
                var updatedMentors = currentState.mentors.map { mentor ->
                    val probability = mentor.hiddenAffinity * 0.003
                    if (mentor.hiddenAffinity > 0 && Random.nextDouble() < probability) {
                        notes.add("✉️ New unprompted advice from ${mentor.name} in your Mentor Network!")
                        val msg = ChatMessage(text = "Just checking in! Remember to keep your daily operations smooth.", isFromPlayer = false, timestampDay = currentDay)
                        mentor.copy(chatHistory = mentor.chatHistory + msg, hasGreetedToday = false)
                    } else {
                        mentor.copy(hasGreetedToday = false)
                    }
                }

                if (currentState.netWorthPhase == NetWorthPhase.TENSION && Random.nextDouble() < 0.15) {
                    updatedMentors = updatedMentors.map { mentor ->
                        if (mentor.id == "mentor_sterling") {
                            notes.add("✉️ Private message from ${mentor.name}!")
                            val msg = ChatMessage(text = "Barnaby's anti-profit warnings will stall your growth. Ignore him and expand your operations.", isFromPlayer = false, timestampDay = currentDay)
                            mentor.copy(chatHistory = mentor.chatHistory + msg)
                        } else mentor
                    }
                }

                // 2. Financial Deductions (Maintenance with Efficiency Expert discount, Feed, Debt Interest)
                val rawMaintenance = currentState.buildings.filter { it.isConstructed && it.isOperational && !it.isSpunOff }
                    .sumOf { 
                        val hasRadiativeCooling = it.activeAnomalyId != null && com.example.model.AnomalyCatalog.BLUEPRINTS.find { bp -> bp.id == it.activeAnomalyId }?.effectType == com.example.model.AnomalyEffect.RADIATIVE_COOLING
                        if (hasRadiativeCooling) 0.0 else it.currentMaintenance 
                    }
                var maintenanceCost = rawMaintenance * (1.0 - currentState.playerSkills.maintenanceDiscountPercent)
                var feedCost = currentState.buildings.filter { it.isConstructed && it.type == BuildingType.PASTURE && it.isOperational && !it.isSpunOff }
                    .sumOf { it.level * 12.0 }
                
                if (chadBuff) {
                    maintenanceCost *= 0.85
                    feedCost *= 0.85
                }
                val interestCharge = currentState.bank.dailyInterestCharge
                val eventCashBonus = if (isNewEventTriggered) (activeNews?.cashBonus ?: 0.0) else 0.0
                
                val executiveSalary = currentState.totalExecutiveSalary
                if (executiveSalary > 0) {
                    notes.add("👔 Executive Payroll: -$${String.format("%.2f", executiveSalary)}")
                }

                val hasCFO = currentState.activeExecutives.any { it.role == ExecutiveRole.CFO }
                val hasCOO = currentState.activeExecutives.any { it.role == ExecutiveRole.COO }
                val hasCISO = currentState.activeExecutives.any { it.role == ExecutiveRole.CISO }
                val hasCMO = currentState.activeExecutives.any { it.role == ExecutiveRole.CMO }
                val investmentRate = if (hasCFO) 0.025 else 0.005
                val investmentEarnings = currentState.bank.investedFunds * investmentRate
                val newInvestedFunds = currentState.bank.investedFunds + investmentEarnings
                if (investmentEarnings > 0) {
                    notes.add("📈 Investment Returns: +$${String.format("%.2f", investmentEarnings)} at ${investmentRate * 100}%")
                }

                var currentCash = currentLiquidCash - (maintenanceCost + feedCost + interestCharge + executiveSalary) + eventCashBonus

                // Corporate Sabotage / Digital Fraud attempt
                if (Random.nextDouble() < 0.05) {
                    if (hasCISO) {
                        notes.add("🛡️ CISO Blocked an attempted Digital Fraud hack on corporate accounts!")
                    } else {
                        val stolen = (currentCash * 0.10).coerceAtLeast(100.0).coerceAtMost(2500.0)
                        if (currentCash >= stolen) {
                            currentCash -= stolen
                            stolenCashToday += stolen
                            notes.add("⚠️ Corporate Sabotage! Rival hackers stole $${String.format("%.2f", stolen)} from liquid reserves.")
                        }
                    }
                }

                // 3. Time-Gated Projects Progression Phase
                val remainingActiveProjects = mutableListOf<ActiveProject>()
                var effectiveBuildings = currentState.buildings
                var effectiveUnlockedTechs = currentState.unlockedTechIds
                var newlyBuiltFacilitiesCount = 0

                val tempRemaining = mutableListOf<ActiveProject>()

                currentState.activeProjects.forEach { project ->
                    val speedMultiplier = if (project.isDedicated) 2 else 1
                    val daysLeft = project.daysRemaining - speedMultiplier
                    if (daysLeft <= 0) {
                        when (project.type) {
                            ProjectType.FACILITY_CONSTRUCTION -> {
                                var builtBuildingType: com.example.model.BuildingType? = null
                                effectiveBuildings = effectiveBuildings.map { b ->
                                    if (b.id == project.targetId) {
                                        builtBuildingType = b.type
                                        b.copy(isConstructed = true, level = 1, isOperational = true)
                                    } else b
                                }
                                newlyBuiltFacilitiesCount += 1
                                notes.add("🔨 Construction Finished: ${project.targetName} is now fully operational!")
                                
                                if (builtBuildingType == com.example.model.BuildingType.ADVANCED_LAB && !currentState.hasSeenAweTutorial) {
                                    _gameState.update { it.copy(showAweDialogue = true, hasSeenAweTutorial = true) }
                                }
                            }
                            ProjectType.FACILITY_UPGRADE -> {
                                effectiveBuildings = effectiveBuildings.map { b ->
                                    if (b.id == project.targetId) {
                                        b.copy(level = project.targetLevel)
                                    } else b
                                }
                                notes.add("⭐ Upgrade Finished: ${project.targetName} is ready!")
                            }
                            ProjectType.TECH_RESEARCH -> {
                                effectiveUnlockedTechs = effectiveUnlockedTechs + project.targetId
                                newlyCompletedTechIds.add(project.targetId)
                                
                                notes.add("🔬 Research Finished: ${project.targetName} R&D milestone reached!")
                            }
                        }
                    } else {
                        tempRemaining.add(project.copy(daysRemaining = daysLeft))
                    }
                }

                // Re-evaluate available labs and crews based on updated tech (in case a tech unlocked them this turn)
                val newOwnedLabs = effectiveUnlockedTechs.count { it.startsWith("tech_dedicated_lab_") }
                val newOwnedCrews = effectiveUnlockedTechs.count { it.startsWith("tech_construction_crew_") }

                val currentLabsUsed = tempRemaining.count { it.isDedicated && it.type == ProjectType.TECH_RESEARCH }
                val currentCrewsUsed = tempRemaining.count { it.isDedicated && (it.type == ProjectType.FACILITY_CONSTRUCTION || it.type == ProjectType.FACILITY_UPGRADE) }

                var freeLabs = (newOwnedLabs - currentLabsUsed).coerceAtLeast(0)
                var freeCrews = (newOwnedCrews - currentCrewsUsed).coerceAtLeast(0)

                for (i in tempRemaining.indices) {
                    val p = tempRemaining[i]
                    if (!p.isDedicated) {
                        if (p.type == ProjectType.TECH_RESEARCH && freeLabs > 0) {
                            tempRemaining[i] = p.copy(isDedicated = true)
                            freeLabs--
                        } else if ((p.type == ProjectType.FACILITY_CONSTRUCTION || p.type == ProjectType.FACILITY_UPGRADE) && freeCrews > 0) {
                            tempRemaining[i] = p.copy(isDedicated = true)
                            freeCrews--
                        }
                    }
                }
                
                remainingActiveProjects.addAll(tempRemaining)

                // 3b. Production Engine (processFacilities Phase)
                var rawProducedUnits = 0
                var processedUnitsProduced = 0
                val processedItemIdsThisDay = mutableSetOf<String>()
                var operatingCostFromProcessing = 0.0
                var researchPointsGained = 0

                val hasColdChain = currentState.playerSkills.getLevel(SkillType.COLD_CHAIN_LOGISTICS) > 0
                val hasHerdGenetics = currentState.playerSkills.getLevel(SkillType.BOVINE_GENETICS) > 0

                // Step 3a: Direct Raw Producers (Pastures) & R&D Labs
                val hasAlpineSubsidiary = currentState.subsidiaryCompanyIds.contains("rival_alpine_bovine")
                effectiveBuildings.filter { it.isConstructed && it.isOperational && it.sabotagedDaysRemaining <= 0 && !it.isSpunOff }.forEach { building ->
                    if (building.type == BuildingType.PASTURE) {
                        val extraYield = if (hasHerdGenetics) 2 * building.level else 0
                        var yield = building.currentDailyRawProduction + extraYield
                        if (hasAlpineSubsidiary) {
                            yield = ((yield * 1.30) + 0.5).toInt().coerceAtLeast(yield + 1)
                        }
                        if (barnabyBuff) yield = (yield * 1.10).toInt()
                        
                        val feedRequired = building.level
                        val availableFeedBatches = workingInventory.filter { it.itemId == "cow_feed" }
                        val totalFeedAvailable = availableFeedBatches.sumOf { it.quantity }
                        val feedUsed = minOf(feedRequired, totalFeedAvailable)
                        
                        var remainingToDeduct = feedUsed
                        for (batch in availableFeedBatches) {
                            if (remainingToDeduct <= 0) break
                            val bIndex = workingInventory.indexOf(batch)
                            if (bIndex == -1) continue
                            val take = minOf(batch.quantity, remainingToDeduct)
                            remainingToDeduct -= take
                            if (batch.quantity <= take) {
                                workingInventory.removeAt(bIndex)
                            } else {
                                workingInventory[bIndex] = batch.copy(quantity = batch.quantity - take)
                            }
                        }
                        
                        if (feedRequired > 0) {
                            val originalYield = yield
                            val feedRatio = feedUsed.toDouble() / feedRequired
                            yield = (yield * feedRatio).toInt()
                            if (feedUsed < feedRequired) {
                                notes.add("⚠️ Starvation: ${building.name} produced ${yield}/${originalYield} milk due to lack of feed (${feedUsed}/${feedRequired}).")
                                android.util.Log.d("TycoonDebug", "Yield reduced for ${building.name} due to missing feed. feedUsed: $feedUsed, feedRequired: $feedRequired")
                            }
                        }
                        
                        android.util.Log.d("TycoonDebug", "PRE-ADD TRACE: Attempting to deposit +$yield units of Milk to inventory from ${building.name}.")
                        if (yield > 0) {
                            rawProducedUnits += yield
                            processedItemIdsThisDay.add(ProductCatalog.RAW_MILK.id)
                            android.util.Log.d("TycoonDebug", "Milk produced today: $yield")
                            val baseQuality = (1.0 + (currentState.reputation / 100.0) * 0.7) * (if (hasHerdGenetics) 1.20 else 1.0)
                            val milkBatch = InventoryBatch(
                                itemId = ProductCatalog.RAW_MILK.id,
                                itemName = ProductCatalog.RAW_MILK.name,
                                quantity = yield,
                                quality = (baseQuality * 10.0).toInt() / 10.0,
                                
                                maxShelfLife = ProductCatalog.RAW_MILK.shelfLifeDays,
                                dayProduced = currentDay + 1
                            )
                            workingInventory.add(milkBatch)
                        } else {
                            android.util.Log.d("TycoonDebug", "PRE-ADD TRACE: Yield was 0, skipping inventory deposit.")
                        }
                    } else if (building.type == BuildingType.RD_LAB) {
                        researchPointsGained += building.currentResearchPoints
                    }
                }

                // Step 3b: Processing Facilities (FIFO Queue Consumption from oldest unspoiled batches)
                val hasWheySubsidiary = currentState.subsidiaryCompanyIds.contains("rival_global_whey")
                effectiveBuildings.filter { it.isConstructed && it.isOperational && it.sabotagedDaysRemaining <= 0 && it.activeRecipe != null && !it.isSpunOff }.forEach { building ->
                    val recipe = building.activeRecipe ?: return@forEach
                    
                    var throughputMult = 1.0f
                    val hasAcousticLevitation = building.activeAnomalyId != null && com.example.model.AnomalyCatalog.BLUEPRINTS.find { bp -> bp.id == building.activeAnomalyId }?.effectType == com.example.model.AnomalyEffect.ACOUSTIC_LEVITATION
                    if (hasAcousticLevitation) {
                        throughputMult *= 5.0f
                    }
                    var qualityMult = 1.0f
                    building.unlockedPerks.forEach { perkId ->
                        val perk = com.example.model.FacilityPerkCatalog.PERKS.find { it.id == perkId }
                        if (perk != null) {
                            if (perk.effectType == com.example.model.PerkEffect.THROUGHPUT_MULTIPLIER) throughputMult += (perk.effectValue - 1.0f)
                            if (perk.effectType == com.example.model.PerkEffect.QUALITY_MULTIPLIER) qualityMult += (perk.effectValue - 1.0f)
                        }
                    }
                    
                    var remainingCapacity = (building.currentProcessingCapacity * throughputMult).toInt()
                    if (hasWheySubsidiary) {
                        remainingCapacity = ((remainingCapacity * 1.5) + 0.5).toInt().coerceAtLeast(remainingCapacity + 1)
                    }

                    while (remainingCapacity > 0) {
                        val eligibleBatches = workingInventory.filter {
                            it.itemId == recipe.inputItemId && !it.isSpoiled && it.quantity > 0
                        }

                        val totalAvailableInput = eligibleBatches.sumOf { it.quantity }
                        val requestedInputUnits = (totalAvailableInput * (building.allocationPercentage / 100.0)).toInt()
                        val possibleBatches = requestedInputUnits / recipe.inputQuantity
                        if (possibleBatches <= 0) {
                            break
                        }

                        var batchesToProcess = remainingCapacity.coerceAtMost(possibleBatches)
                        if (batchesToProcess <= 0) break
                        
                        if (recipe.outputItemId == "pasteurized_milk") {
                            val availableBottles = workingInventory.filter { it.itemId == "glass_bottles" }
                            val totalBottles = availableBottles.sumOf { it.quantity }
                            
                            val bottlesNeeded = batchesToProcess * recipe.outputQuantity
                            
                            if (totalBottles < bottlesNeeded) {
                                batchesToProcess = totalBottles / recipe.outputQuantity
                            }
                            
                            val bottlesToConsume = batchesToProcess * recipe.outputQuantity
                            var remainingBottles = bottlesToConsume
                            val orderedBottles = if (currentState.inventoryMethod == InventoryMethod.LIFO) availableBottles.reversed() else availableBottles
                            for (b in orderedBottles) {
                                if (remainingBottles <= 0) break
                                val bIndex = workingInventory.indexOf(b)
                                if (bIndex == -1) continue
                                val take = remainingBottles.coerceAtMost(b.quantity)
                                remainingBottles -= take
                                if (b.quantity <= take) {
                                    workingInventory.removeAt(bIndex)
                                } else {
                                    workingInventory[bIndex] = b.copy(quantity = b.quantity - take)
                                }
                            }
                        }

                        if (batchesToProcess <= 0) break

                        val totalInputNeeded = batchesToProcess * recipe.inputQuantity
                        var totalOutputProduced = batchesToProcess * recipe.outputQuantity
                        if (hasCOO) {
                            totalOutputProduced = (totalOutputProduced * 1.5).toInt()
                        }

                        var remainingInputToConsume = totalInputNeeded
                        var weightedQualitySum = 0.0

                        val orderedBatches = if (currentState.inventoryMethod == InventoryMethod.LIFO) eligibleBatches.reversed() else eligibleBatches
                        val hasNanoSynthesis = building.activeAnomalyId != null && com.example.model.AnomalyCatalog.BLUEPRINTS.find { bp -> bp.id == building.activeAnomalyId }?.effectType == com.example.model.AnomalyEffect.NANOMATERIAL_DESALINATION
                        if (hasNanoSynthesis) {
                            // Synthesize inputs instantly from nothing, keep the highest possible quality average (1.0)
                            weightedQualitySum = remainingInputToConsume * 1.0
                            remainingInputToConsume = 0
                        } else {
                            for (batch in orderedBatches) {
                                if (remainingInputToConsume <= 0) break
                                val batchIndex = workingInventory.indexOf(batch)
                                if (batchIndex == -1) continue
    
                                val unitsToTake = remainingInputToConsume.coerceAtMost(batch.quantity)
                                weightedQualitySum += (unitsToTake * batch.quality)
                                remainingInputToConsume -= unitsToTake
    
                                if (batch.quantity <= unitsToTake) {
                                    workingInventory.removeAt(batchIndex)
                                } else {
                                    workingInventory[batchIndex] = batch.copy(quantity = batch.quantity - unitsToTake)
                                }
                            }
                        }

                        val avgInputQuality = if (totalInputNeeded > 0) weightedQualitySum / totalInputNeeded else 1.0
                        val outputProduct = ProductCatalog.getById(recipe.outputItemId)
                        val outputQuality = ((avgInputQuality * recipe.qualityMultiplier * qualityMult) * 10.0).toInt() / 10.0
                        val shelfLife = outputProduct.shelfLifeDays + (if (hasColdChain) 2 else 0)

                        // Push new output batch with Age: 0
                        processedItemIdsThisDay.add(outputProduct.id)
                        workingInventory.add(
                            InventoryBatch(
                                itemId = outputProduct.id,
                                itemName = outputProduct.name,
                                quantity = totalOutputProduced,
                                quality = outputQuality,
                                
                                maxShelfLife = shelfLife,
                                dayProduced = currentDay + 1
                            )
                        )

                        // Push By-Products
                        recipe.byProducts.forEach { (byProdId, qtyPerBatch) ->
                            val byProductQty = batchesToProcess * qtyPerBatch
                            if (byProductQty > 0) processedItemIdsThisDay.add(byProdId)
                            if (byProductQty > 0) {
                                val byProd = ProductCatalog.getById(byProdId)
                                workingInventory.add(
                                    InventoryBatch(
                                        itemId = byProd.id,
                                        itemName = byProd.name,
                                        quantity = byProductQty,
                                        quality = outputQuality,
                                        
                                        maxShelfLife = byProd.shelfLifeDays,
                                        dayProduced = currentDay + 1
                                    )
                                )
                            }
                        }

                        val extraOpCost = totalInputNeeded * recipe.extraOperatingCostPerUnit
                        operatingCostFromProcessing += extraOpCost
                        currentCash -= extraOpCost
                        processedUnitsProduced += totalOutputProduced
                        remainingCapacity -= batchesToProcess
                    }
                }

                // Update Reputation before Contracts & Sales
                val (baseUpdatedRep, repBreakdown) = calculateDailyReputation(currentState, processedItemIdsThisDay)
                
                val repGainedFromNews = if (isNewEventTriggered) (activeNews?.reputationDelta ?: 0) else 0
                val updatedReputation = (baseUpdatedRep + repGainedFromNews).coerceIn(0, 100)
                val finalRepBreakdown = repBreakdown.copy(penalties = repBreakdown.penalties - repGainedFromNews.coerceAtMost(0))

                // Step 3c: B2B Contract Auto-Fulfillment (FIFO Quota Deduction before Spoilage & Aging)
                val updatedActiveContracts = mutableListOf<ContractOffer>()
                var completedContractsDelta = 0
                var missedEventId: String? = currentState.missedDeliveryEvent
                var contractsRevenueToday = 0.0
                var contractPenaltiesToday = 0.0
                var stolenCashToday = 0.0

                currentState.activeContracts.forEach { contract ->
                    val targetProdId = contract.targetProduct
                    val targetProduct = ProductCatalog.getById(targetProdId)
                    val rival = currentState.rivalCompanies.find { it.id == contract.rivalId } ?: RivalCatalog.getRivalById(contract.rivalId)

                    if (contract.contractType == ContractType.BULK_DEADLINE) {
                        val remainingToFulfill = contract.targetTotalQuantity - contract.fulfilledQuantity
                        val eligibleBatches = workingInventory.filter { it.itemId == targetProdId && !it.isSpoiled && it.quantity > 0 }
                        val totalAvailable = eligibleBatches.sumOf { it.quantity }
                        
                        val takeAmount = minOf(remainingToFulfill, totalAvailable)
                        
                        if (takeAmount > 0) {
                            var toDeduct = takeAmount
                            val orderedBatches = if (currentState.inventoryMethod == InventoryMethod.LIFO) eligibleBatches.reversed() else eligibleBatches
                            for (batch in orderedBatches) {
                                if (toDeduct <= 0) break
                                val bIndex = workingInventory.indexOf(batch)
                                if (bIndex == -1) continue
                                val take = minOf(toDeduct, batch.quantity)
                                toDeduct -= take
                                if (batch.quantity <= take) {
                                    workingInventory.removeAt(bIndex)
                                } else {
                                    workingInventory[bIndex] = batch.copy(quantity = batch.quantity - take)
                                }
                            }
                            
                            val newFulfilled = contract.fulfilledQuantity + takeAmount
                            if (newFulfilled >= contract.targetTotalQuantity) {
                                currentCash += contract.payoutAmount
                                contractsRevenueToday += contract.payoutAmount
                                completedContractsDelta++
                                notes.add("🎉 Bulk Complete: Delivered ${contract.targetTotalQuantity}x ${targetProduct.name} to ${rival.name} (+$${String.format("%.2f", contract.payoutAmount)}).")
                            } else {
                                updatedActiveContracts.add(contract.copy(fulfilledQuantity = newFulfilled, daysRemaining = contract.daysRemaining - 1))
                            }
                        } else {
                            val nextDays = contract.daysRemaining - 1
                            if (nextDays <= 0) {
                                currentCash -= contract.penaltyAmount
                                contractPenaltiesToday += contract.penaltyAmount
                                completedContractsDelta++
                                notes.add("❌ Bulk Failed: Failed to deliver ${contract.targetTotalQuantity}x ${targetProduct.name} to ${rival.name} in time! Fined -$${String.format("%.2f", contract.penaltyAmount)}.")
                            } else {
                                updatedActiveContracts.add(contract.copy(daysRemaining = nextDays))
                            }
                        }
                    } else {
                        // DAILY QUOTA
                        val dailyQuota = contract.requiredQuantity
                        val eligibleBatches = workingInventory.filter { it.itemId == targetProdId && !it.isSpoiled && it.quantity > 0 }
                        val totalAvailable = eligibleBatches.sumOf { it.quantity }
                        
                        if (totalAvailable >= dailyQuota) {
                            var remainingToDeduct = dailyQuota
                            val orderedBatches = if (currentState.inventoryMethod == InventoryMethod.LIFO) eligibleBatches.reversed() else eligibleBatches
                            for (batch in orderedBatches) {
                                if (remainingToDeduct <= 0) break
                                val bIndex = workingInventory.indexOf(batch)
                                if (bIndex == -1) continue
                                val take = minOf(remainingToDeduct, batch.quantity)
                                remainingToDeduct -= take
                                if (batch.quantity <= take) {
                                    workingInventory.removeAt(bIndex)
                                } else {
                                    workingInventory[bIndex] = batch.copy(quantity = batch.quantity - take)
                                }
                            }
                            
                            val newSuccessDays = contract.fulfilledDays + 1
                            val nextDays = contract.daysRemaining - 1
                            
                            if (nextDays <= 0) {
                                currentCash += contract.payoutAmount
                                contractsRevenueToday += contract.payoutAmount
                                completedContractsDelta++
                                notes.add("🎉 Daily Complete: Fulfilled all terms with ${rival.name} (+$${String.format("%.2f", contract.payoutAmount)}).")
                            } else {
                                updatedActiveContracts.add(contract.copy(daysRemaining = nextDays, fulfilledDays = newSuccessDays))
                                notes.add("🤝 Daily Delivery: Supplied ${dailyQuota}x ${targetProduct.name} to ${rival.name}.")
                            }
                        } else {
                            if (missedEventId == null) {
                                missedEventId = contract.id
                                val nextDays = contract.daysRemaining - 1
                                val newFailedDays = contract.failedDays + 1
                                notes.add("⚠️ Contract Missed: Shortfall on ${dailyQuota}x ${targetProduct.name} for ${rival.name}! Pending executive decision tomorrow.")
                                updatedActiveContracts.add(contract.copy(daysRemaining = nextDays, failedDays = newFailedDays))
                            } else {
                                currentCash -= contract.penaltyAmount
                                contractPenaltiesToday += contract.penaltyAmount
                                val nextDays = contract.daysRemaining - 1
                                notes.add("⚠️ Daily Default: Shortfall on ${dailyQuota}x ${targetProduct.name} for ${rival.name}! Fined -$${String.format("%.2f", contract.penaltyAmount)}.")
                                if (nextDays <= 0) {
                                    completedContractsDelta++
                                    notes.add("❌ Contract Expired: Terms with ${rival.name} failed overall.")
                                } else {
                                    updatedActiveContracts.add(contract.copy(daysRemaining = nextDays, failedDays = contract.failedDays + 1))
                                }
                            }
                        }
                    }
                }

                // 3d. Auto-Sell Execution
                var autoSellRevenueToday = 0.0
                val autoSoldUnits = mutableMapOf<String, Int>()
                
                currentState.autoSellSubscriptions.forEach { (itemId, isActive) ->
                    if (isActive) {
                        val eligibleBatches = workingInventory.filter { it.itemId == itemId && !it.isSpoiled && it.quantity > 0 }
                        var totalSold = 0
                        var itemRevenue = 0.0
                        
                        val marketState = currentState.marketPrices[itemId]
                        val baseMarketPrice = marketState?.currentPrice ?: ProductCatalog.getById(itemId).basePrice
                        
                        val repFactor = currentState.reputation * 0.003 * currentState.playerSkills.silverTongueRepBonusMultiplier
                        val repMultiplier = 1.0 + repFactor
                        val mooCorpBonus = if (currentState.subsidiaryCompanyIds.contains("rival_moocorp")) 1.2 else 1.0
                        
                        val isGourmet = itemId in listOf(ProductCatalog.AGED_CHEDDAR.id, ProductCatalog.FRESH_CHEESE.id, ProductCatalog.BUTTER.id, ProductCatalog.CREAM.id)
                        val lactoBonus = if (isGourmet && currentState.subsidiaryCompanyIds.contains("rival_lacto_dynasty")) 1.35 else 1.0
                        
                        val endgameMultiplier = currentState.endgamePriceMultiplier
                        
                        val orderedBatches = if (currentState.inventoryMethod == InventoryMethod.LIFO) eligibleBatches.reversed() else eligibleBatches
                        for (batch in orderedBatches) {
                            val bIndex = workingInventory.indexOf(batch)
                            if (bIndex == -1) continue
                            
                            val unitPrice = baseMarketPrice * (0.8 + batch.quality * 0.2) * repMultiplier * mooCorpBonus * lactoBonus * endgameMultiplier
                            val batchRev = batch.quantity * unitPrice
                            
                            totalSold += batch.quantity
                            itemRevenue += batchRev
                            workingInventory.removeAt(bIndex)
                        }
                        
                        if (totalSold > 0) {
                            autoSellRevenueToday += itemRevenue
                            currentCash += itemRevenue
                            autoSoldUnits[itemId] = totalSold
                            
                            notes.add("🛒 Auto-Sell: Sold $totalSold units of ${ProductCatalog.getById(itemId).name} for +$${String.format("%.2f", itemRevenue)}.")
                        }
                    }
                }

                // 4. Inventory Aging & Spoilage Evaluation
                var spoiledCount = 0
                val finalInventory = mutableListOf<InventoryBatch>()
                var remainingCapacity = currentState.globalColdStorageCapacity
                val perishables = mutableListOf<InventoryBatch>()
                val nonPerishables = mutableListOf<InventoryBatch>()
                
                workingInventory.forEach { batch ->
                    if (batch.dayProduced > currentDay) {
                        finalInventory.add(batch)
                    } else if (batch.itemId == ProductCatalog.SPOILED_MILK.id) {
                        val nextDaysSpoiled = batch.daysUntilSpoiled - 1.0f
                        if (nextDaysSpoiled > 0.0f) {
                            finalInventory.add(batch.copy(daysUntilSpoiled = nextDaysSpoiled))
                        } else {
                            notes.add("Disposed of ${batch.quantity} units of rotting spoiled milk.")
                        }
                    } else {
                        if (batch.maxShelfLife >= 999) {
                            nonPerishables.add(batch.copy(daysUntilSpoiled = batch.daysUntilSpoiled - 1.0f))
                        } else {
                            perishables.add(batch)
                        }
                    }
                }
                
                finalInventory.addAll(nonPerishables)
                
                when (currentState.coldStoragePriority) {
                    ColdStoragePriority.OLDEST_FIRST -> perishables.sortByDescending { it.daysOld }
                    ColdStoragePriority.MOST_EXPENSIVE -> perishables.sortByDescending { ProductCatalog.getById(it.itemId).basePrice }
                    ColdStoragePriority.SPOILING_FIRST -> perishables.sortBy { it.daysUntilSpoiled }
                    ColdStoragePriority.HIGH_VALUE_FIRST -> perishables.sortByDescending { ProductCatalog.getById(it.itemId).basePrice }
                    ColdStoragePriority.MANUAL -> {}
                }
                
                if (currentState.coldStoragePriority == ColdStoragePriority.MANUAL) {
                    val itemAllocationsRemaining = currentState.manualColdStorageAllocations.toMutableMap()
                    
                    for (batch in perishables) {
                        val allowedCapacity = itemAllocationsRemaining.getOrDefault(batch.itemId, 0)
                        if (allowedCapacity > 0) {
                            if (batch.quantity <= allowedCapacity) {
                                itemAllocationsRemaining[batch.itemId] = allowedCapacity - batch.quantity
                                finalInventory.add(batch.copy(daysUntilSpoiled = batch.daysUntilSpoiled - 0.5f, isInColdStorage = true))
                            } else {
                                val protectedQty = allowedCapacity
                                val exposedQty = batch.quantity - protectedQty
                                itemAllocationsRemaining[batch.itemId] = 0
                                finalInventory.add(batch.copy(quantity = protectedQty, daysUntilSpoiled = batch.daysUntilSpoiled - 0.5f, isInColdStorage = true))
                                finalInventory.add(batch.copy(batchId = UUID.randomUUID().toString(), quantity = exposedQty, daysUntilSpoiled = batch.daysUntilSpoiled - 1.0f, isInColdStorage = false))
                            }
                        } else {
                            finalInventory.add(batch.copy(daysUntilSpoiled = batch.daysUntilSpoiled - 1.0f, isInColdStorage = false))
                        }
                    }
                } else {
                    for (batch in perishables) {
                        if (remainingCapacity > 0) {
                            if (batch.quantity <= remainingCapacity) {
                                remainingCapacity -= batch.quantity
                                finalInventory.add(batch.copy(daysUntilSpoiled = batch.daysUntilSpoiled - 0.5f, isInColdStorage = true))
                            } else {
                                val protectedQty = remainingCapacity
                                val exposedQty = batch.quantity - protectedQty
                                remainingCapacity = 0
                                finalInventory.add(batch.copy(quantity = protectedQty, daysUntilSpoiled = batch.daysUntilSpoiled - 0.5f, isInColdStorage = true))
                                finalInventory.add(batch.copy(batchId = UUID.randomUUID().toString(), quantity = exposedQty, daysUntilSpoiled = batch.daysUntilSpoiled - 1.0f, isInColdStorage = false))
                            }
                        } else {
                            finalInventory.add(batch.copy(daysUntilSpoiled = batch.daysUntilSpoiled - 1.0f, isInColdStorage = false))
                        }
                    }
                }
                
                val evaluatedInventory = mutableListOf<InventoryBatch>()
                for (batch in finalInventory) {
                    if (batch.isSpoiled && batch.itemId != ProductCatalog.SPOILED_MILK.id) {
                        spoiledCount += batch.quantity
                        evaluatedInventory.add(
                            InventoryBatch(
                                itemId = ProductCatalog.SPOILED_MILK.id,
                                itemName = ProductCatalog.SPOILED_MILK.name,
                                quantity = batch.quantity,
                                quality = 0.2,
                                maxShelfLife = ProductCatalog.SPOILED_MILK.shelfLifeDays,
                                dayProduced = currentDay + 1,
                                daysUntilSpoiled = ProductCatalog.SPOILED_MILK.shelfLifeDays.toFloat()
                            )
                        )
                        notes.add("⚠️ Spoilage Alert: ${batch.quantity}x ${batch.itemName} turned sour!")
                    } else {
                        evaluatedInventory.add(batch)
                    }
                }
                
                android.util.Log.d("TycoonDebug", "Milk spoiled today: $spoiledCount")
                val finalMilkCount = evaluatedInventory.filter { it.itemId == com.example.model.ProductCatalog.RAW_MILK.id && !it.isSpoiled }.sumOf { it.quantity }
                android.util.Log.d("TycoonDebug", "Final Inventory count for Milk: $finalMilkCount")
                
                finalInventory.clear()
                finalInventory.addAll(evaluatedInventory)

                // 5. Debt & Foreclosure Check
                var daysInDebt = currentState.bank.daysInDebt
                var bankDebt = currentState.bank.totalDebt
                var foreclosureSalesCount = 0
                var automatedSalesRevenue = 0.0
                
                if (currentCash < 0.0) {
                    val deficit = -currentCash
                    bankDebt += deficit
                    currentCash = 0.0
                }
                
                if (bankDebt > 0.0) {
                    daysInDebt++
                    if (daysInDebt >= 7 && finalInventory.isNotEmpty()) {
                        notes.add("🚨 BANK FORECLOSURE: The bank seized inventory to cover outstanding debt!")
                        val iterator = finalInventory.iterator()
                        while (iterator.hasNext() && bankDebt > 0.0) {
                            val batch = iterator.next()
                            val marketPrice = currentState.marketPrices[batch.itemId]?.currentPrice ?: 1.0
                            val distressPrice = marketPrice * 0.75
                            val batchTotalDistressVal = batch.quantity * distressPrice
                            
                            if (batchTotalDistressVal <= bankDebt) {
                                bankDebt -= batchTotalDistressVal
                                automatedSalesRevenue += batchTotalDistressVal
                                foreclosureSalesCount += batch.quantity
                                iterator.remove()
                            } else {
                                val unitsToLiquidate = minOf(Math.ceil(bankDebt / distressPrice).toInt(), batch.quantity)
                                val recovered = unitsToLiquidate * distressPrice
                                bankDebt = maxOf(bankDebt - recovered, 0.0)
                                automatedSalesRevenue += recovered
                                foreclosureSalesCount += unitsToLiquidate
                                
                                val remainingUnits = batch.quantity - unitsToLiquidate
                                if (remainingUnits <= 0) {
                                    iterator.remove()
                                } else {
                                    val idx = finalInventory.indexOf(batch)
                                    if (idx != -1) {
                                        finalInventory[idx] = batch.copy(quantity = remainingUnits)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    daysInDebt = 0
                }


                // 5.5 Crisis Event Logic
                val updatedCrises = currentState.activeCrises.map { it.copy(durationDays = it.durationDays - 1) }.filter { it.durationDays > 0 }.toMutableList()
                var newCrisisFired: com.example.model.CrisisEvent? = null
                
                if (updatedCrises.isEmpty() && kotlin.random.Random.nextDouble() < 0.05) {
                    val isCrash = kotlin.random.Random.nextBoolean()
                    val newCrisis = com.example.model.CrisisEvent(
                        title = if (isCrash) "GLOBAL RECESSION" else "SUPPLY CHAIN DISRUPTION",
                        description = if (isCrash) "Consumer spending drops massively. All market sell prices are reduced by 30% for 5 days." else "Raw materials are scarce. Procurement costs spike by 50% for 3 days.",
                        durationDays = if (isCrash) 5 else 3,
                        modifierType = if (isCrash) com.example.model.CrisisModifierType.MARKET_CRASH else com.example.model.CrisisModifierType.SUPPLY_SHORTAGE
                    )
                    updatedCrises.add(newCrisis)
                    newCrisisFired = newCrisis
                    notes.add("🚨 CRISIS ALERT: ${newCrisis.title}")
                }

                // 6. Dynamic Market Pricing Update
                val newMarketPrices = currentState.marketPrices.mapValues { (productId, marketState) ->
                    val product = ProductCatalog.getById(productId)
                    val base = product.basePrice
                    val soldYesterday = (currentState.todaySoldUnits[productId] ?: 0) + (autoSoldUnits[productId] ?: 0)
                    
                    val rng = kotlin.random.Random.nextDouble(0.85, 1.25)
                    val repBonus = 1.0 + currentState.reputation * 0.003 * currentState.playerSkills.silverTongueRepBonusMultiplier
                    val supplyPenalty = (1.0 - soldYesterday * 0.01).coerceIn(0.6, 1.0)
                    
                    val eventMult = if (activeNews != null && (activeNews.targetProductId == null || activeNews.targetProductId == productId)) activeNews.multiplier else 1.0
                    
                    var crisisMult = 1.0
                    updatedCrises.forEach { crisis ->
                        if (crisis.modifierType == com.example.model.CrisisModifierType.MARKET_CRASH) crisisMult *= 0.7
                        if (crisis.modifierType == com.example.model.CrisisModifierType.SUPPLY_SHORTAGE && product.category == com.example.model.ProductCategory.RAW) crisisMult *= 1.5
                    }
                    

                    
                    var calculatedPrice = base * rng * repBonus * supplyPenalty * eventMult * crisisMult
                    calculatedPrice = (calculatedPrice * 100.0).toInt() / 100.0
                    calculatedPrice = maxOf(calculatedPrice, 0.1)
                    
                    val oldPrice = marketState.currentPrice
                    val changePercent = if (oldPrice > 0.0) ((calculatedPrice - oldPrice) / oldPrice) * 100.0 else 0.0
                    
                    val history = (marketState.priceHistory + calculatedPrice).takeLast(10)
                    
                    marketState.copy(
                        currentPrice = calculatedPrice,
                        yesterdayUnitsSold = soldYesterday,
                        priceChangePercent = (changePercent * 10.0).toInt() / 10.0,
                        priceHistory = history
                    )
                }

                // 7. Rival AI & Corporate Simulation
                val updatedRivals = currentState.rivalCompanies.map { rival ->
                    val isStillSmeared = rival.smearDaysRemaining > 1
                    val smearDaysLeft = if (rival.smearDaysRemaining > 0) rival.smearDaysRemaining - 1 else 0
                    
                    val stockChangePct = if (isStillSmeared) Random.nextDouble(-9.0, -3.0) else Random.nextDouble(-3.5, 4.5)
                    val newStock = maxOf((rival.stockPrice * (1.0 + stockChangePct / 100.0) * 100.0).toInt() / 100.0, 10.0)
                    
                    val powerDelta = (stockChangePct * 0.01).toFloat()
                    val newPower = (rival.marketPower + powerDelta).coerceIn(0.5f, 4.0f)
                    
                    var newHostility = rival.hostilityToPlayer
                    if (rival.targetSector != null) {
                        val matchingProducts = ProductCatalog.ALL_PRODUCTS.filter { it.category == rival.targetSector }
                        val soldInSector = matchingProducts.sumOf { currentState.todaySoldUnits[it.id] ?: 0 }
                        if (soldInSector > 50) newHostility += 2
                    }
                    
                    val lockout = if (rival.b2bLockoutDaysRemaining > 0) rival.b2bLockoutDaysRemaining - 1 else 0
                    
                    if (newHostility >= 10 && currentState.netWorthPhase == NetWorthPhase.CORPORATE && Random.nextDouble() < 0.2) {
                        val aiOffense = rival.offenseRating + Random.nextInt(1, 20)
                        val playerDef = currentState.playerDefenseRating + Random.nextInt(1, 20)
                        
                        if (aiOffense > playerDef) {
                            notes.add("🚨 CYBER BREACH: ${rival.name} successfully hacked our network!")
                            val vulnerableBuildings = effectiveBuildings.filter { it.isConstructed && it.isOperational }
                            val targetedBuilding = vulnerableBuildings.randomOrNull()
                            
                            if (targetedBuilding != null) {
                                effectiveBuildings = effectiveBuildings.map { b ->
                                    if (b.id == targetedBuilding.id) b.copy(sabotagedDaysRemaining = 1) else b
                                }
                                val toDelete = finalInventory.filter { 
                                    val rec = targetedBuilding.activeRecipe
                                    if (rec != null) {
                                        it.itemId == rec.inputItemId || it.itemId == rec.outputItemId
                                    } else {
                                        it.itemId == ProductCatalog.RAW_MILK.id
                                    }
                                }
                                finalInventory.removeAll(toDelete)
                                notes.add("💥 SABOTAGE: Inventory at ${targetedBuilding.name} was destroyed, and operations halted for 1 day!")
                            }
                            newHostility -= 10
                        } else {
                            notes.add("🛡️ CYBER DEFENSE: Successfully blocked a digital attack from ${rival.name}.")
                            newHostility -= 5
                        }
                        newHostility = maxOf(newHostility, 0)
                    }
                    
                    rival.copy(
                        stockPrice = newStock,
                        marketPower = (newPower * 10.0f).toInt() / 10.0f,
                        hostilityToPlayer = newHostility,
                        b2bLockoutDaysRemaining = lockout,
                        smearDaysRemaining = smearDaysLeft,
                        isSmeared = isStillSmeared,
                        stockChangePercent = (stockChangePct * 10.0).toInt() / 10.0
                    )
                }

                // 8. Dividends & Offers
                var totalDividendsToday = 0.0
                currentState.rivalSharesOwned.forEach { (rivalId, shares) ->
                    val rival = updatedRivals.find { it.id == rivalId } ?: RivalCatalog.getRivalById(rivalId)
                    totalDividendsToday += shares * rival.dailyDividendPerShare
                }
                
                if (sterlingBuff) {
                    totalDividendsToday *= 1.15
                }
                
                if (totalDividendsToday > 0.0) {
                    currentCash += totalDividendsToday
                    notes.add("📈 Stock Dividends: Earned +$${String.format("%.2f", totalDividendsToday)} from corporate shareholdings.")
                }
                
                val agedPendingOffers = currentState.pendingContractOffers
                    .map { it.copy(expiresInDays = it.expiresInDays - 1) }
                    .filter { it.expiresInDays > 0 }
                    .toMutableList()
                
                val nonSubsidiaryRivals = updatedRivals.filter { !currentState.subsidiaryCompanyIds.contains(it.id) }
                if (b2bUnlocked && nonSubsidiaryRivals.isNotEmpty() && agedPendingOffers.size < 4 && Random.nextDouble() < 0.7) {
                    val randomRival = nonSubsidiaryRivals.random()
                    val candidateProducts = listOf(ProductCatalog.RAW_MILK, ProductCatalog.PASTEURIZED_MILK, ProductCatalog.CREAM, ProductCatalog.BUTTER, ProductCatalog.AGED_CHEDDAR)
                    val chosenProduct = candidateProducts.random()
                    
                    var baseQuota = when (chosenProduct.tier) {
                        1 -> Random.nextInt(10, 25)
                        2 -> Random.nextInt(5, 14)
                        else -> Random.nextInt(3, 8)
                    }
                    
                    val spotPrice = newMarketPrices[chosenProduct.id]?.currentPrice ?: chosenProduct.basePrice
                    val cmoBonus = if (hasCMO) 1.2 else 1.0
                    val sterlingMultiplier = if (sterlingBuff) 1.15 else 1.0
                    
                    val isJunk = currentState.reputation < 10
                    val premiumMultiplier: Double
                    val duration: Int
                    val penaltyMultiplier: Double
                    
                    if (isJunk) {
                        premiumMultiplier = Random.nextDouble(0.7, 0.9)
                        duration = 1
                        penaltyMultiplier = 1.5
                    } else {
                        val repScale = currentState.reputation / 100.0
                        premiumMultiplier = Random.nextDouble(1.1, 1.3) + repScale * 0.5
                        duration = Random.nextInt(2, 4) + currentState.reputation / 30
                        penaltyMultiplier = 0.65
                        baseQuota = (baseQuota * (1.0 + repScale * 1.5)).toInt()
                    }
                    
                    val dailyPayout = ((spotPrice * baseQuota * premiumMultiplier * cmoBonus * sterlingMultiplier) * 100.0).toInt() / 100.0
                    val penalty = ((dailyPayout * penaltyMultiplier + 50.0) * 100.0).toInt() / 100.0
                    
                    val newOffer = ContractOffer(
                        rivalId = randomRival.id,
                        targetProduct = chosenProduct.id,
                        requiredQuantity = baseQuota,
                        payoutAmount = dailyPayout,
                        durationDays = duration,
                        daysRemaining = duration,
                        penaltyAmount = penalty,
                        expiresInDays = Random.nextInt(2, 4),
                        isJunk = isJunk
                    )
                    agedPendingOffers.add(newOffer)
                    val junkTag = if (isJunk) " [HIGH RISK]" else ""
                    notes.add("💼 Inbound RFP: ${randomRival.name} submitted a bulk supply contract for ${baseQuota}x ${chosenProduct.name}/day.$junkTag")
                }

                // 9. Endgame Trigger Check
                val isEndgameReady = currentState.subsidiaryCompanyIds.size >= RivalCatalog.ALL_RIVALS.size && !currentState.isEndgameCompleted && !currentState.isEndgameTriggered
                if (isEndgameReady) {
                    _showEndgameDialog.value = true
                }

                // 10. State Consolidation
                val estimatedTotalNetWorth = currentCash + finalInventory.sumOf { batch ->
                    val price = newMarketPrices[batch.itemId]?.currentPrice ?: 1.0
                    batch.quantity * price
                } + effectiveBuildings.filter { it.isConstructed }.sumOf { it.baseCost * it.level * 0.75 } + currentState.totalPortfolioValue - bankDebt
                
                val isBankrupt = daysInDebt >= 14 && estimatedTotalNetWorth < 0.0
                val nextDay = currentDay + 1
                
                val report = DailyReport(
                    day = currentDay,
                    rawProducedUnits = rawProducedUnits,
                    processedUnitsProduced = processedUnitsProduced,
                    spoiledUnitsCount = spoiledCount,
                    maintenancePaid = maintenanceCost + operatingCostFromProcessing,
                    feedCostsPaid = feedCost,
                    interestPaid = interestCharge,
                    researchPointsGained = researchPointsGained,
                    automatedSalesRevenue = automatedSalesRevenue + autoSellRevenueToday,
                    foreclosureSalesCount = foreclosureSalesCount,
                    dividendsEarned = totalDividendsToday,
                    event = activeNews,
                    summaryNotes = notes
                )
                
                val updatedReputation2 = (currentState.reputation + repGainedFromNews).coerceIn(0, 100)
                
                val newLogEntry = "Day $nextDay begun. Total Inventory: ${finalInventory.sumOf { it.quantity }} units. Cash: $${String.format("%.2f", currentCash)}"
                val newLogs = listOf(newLogEntry) + currentState.dailyLogs.take(19)
                
                val newPhase = if (currentState.netWorthPhase == NetWorthPhase.STARTUP && estimatedTotalNetWorth >= 50000.0) NetWorthPhase.TENSION else currentState.netWorthPhase
                
                val newPeak = maxOf(currentState.peakNetWorth, estimatedTotalNetWorth)
                val newRevenue = currentState.lifetimeRevenue + automatedSalesRevenue + contractsRevenueToday + autoSellRevenueToday
                
                var finalMentors = updatedMentors
                var isNewBoardroom = currentState.unlockedFeatures.isBoardroomNew
                var isNewStockMarket = currentState.unlockedFeatures.isStockMarketNew
                
                val shouldUnlockBoardroom = currentState.unlockedFeatures.isBoardroomUnlocked || estimatedTotalNetWorth >= 50000.0
                if (!currentState.unlockedFeatures.isBoardroomUnlocked && shouldUnlockBoardroom) {
                    isNewBoardroom = true
                    finalMentors = finalMentors.map { mentor ->
                        if (mentor.id == "mentor_sterling") {
                            notes.add("✉️ Private message from ${mentor.name}!")
                            val msg1 = ChatMessage(text = "Your net worth is swelling. It's time to build a Boardroom and hire real Executives.", isFromPlayer = false, timestampDay = currentDay)
                            val msg2 = ChatMessage(text = "Executives charge a daily salary, but provide powerful, permanent passive buffs.", isFromPlayer = false, timestampDay = currentDay)
                            val msg3 = ChatMessage(text = "For instance, a COO will boost your processing speed, while a CFO improves bank interest rates. Choose wisely based on your cash flow.", isFromPlayer = false, timestampDay = currentDay)
                            val newUnlocks = if (!mentor.unlockedFeatures.contains("Explain the Boardroom")) mentor.unlockedFeatures + "Explain the Boardroom" else mentor.unlockedFeatures
                            mentor.copy(unlockedFeatures = newUnlocks, chatHistory = mentor.chatHistory + msg1 + msg2 + msg3)
                        } else mentor
                    }
                }
                
                val shouldUnlockStockMarket = currentState.unlockedFeatures.isStockMarketUnlocked || newPhase == NetWorthPhase.CORPORATE
                if (!currentState.unlockedFeatures.isStockMarketUnlocked && shouldUnlockStockMarket) {
                    isNewStockMarket = true
                    finalMentors = finalMentors.map { mentor ->
                        if (mentor.id == "mentor_sterling") {
                            notes.add("✉️ Private message from ${mentor.name}!")
                            val msg1 = ChatMessage(text = "Welcome to the Corporate phase. The Stock Market is now open.", isFromPlayer = false, timestampDay = currentDay)
                            val msg2 = ChatMessage(text = "You can now purchase shares of your rival companies. Accumulate 51% to trigger a hostile takeover.", isFromPlayer = false, timestampDay = currentDay)
                            val msg3 = ChatMessage(text = "Subjugated subsidiaries will no longer compete against you and will provide massive empire-wide buffs. It's time to crush the competition.", isFromPlayer = false, timestampDay = currentDay)
                            val newUnlocks = if (!mentor.unlockedFeatures.contains("Explain the Stock Market")) mentor.unlockedFeatures + "Explain the Stock Market" else mentor.unlockedFeatures
                            mentor.copy(unlockedFeatures = newUnlocks, chatHistory = mentor.chatHistory + msg1 + msg2 + msg3)
                        } else mentor
                    }
                }
                
                val dailyRevenue = automatedSalesRevenue + autoSellRevenueToday + contractsRevenueToday + totalDividendsToday + eventCashBonus + investmentEarnings
                val dailyExpenses = maintenanceCost + operatingCostFromProcessing + feedCost + interestCharge + executiveSalary + contractPenaltiesToday + stolenCashToday
                val newFinancialRecord = com.example.model.DailyFinancialRecord(
                    day = currentDay,
                    totalRevenue = dailyRevenue,
                    totalExpenses = dailyExpenses
                )
                val newFinancialHistory = (currentState.financialHistory + newFinancialRecord).takeLast(14)
                
                val updatedStats = currentState.stats.copy(
                    totalDaysPlayed = currentState.stats.totalDaysPlayed + 1,
                    totalMilkProduced = currentState.stats.totalMilkProduced + rawProducedUnits,
                    totalProductsProcessed = currentState.stats.totalProductsProcessed + processedUnitsProduced,
                    totalSpoiledUnits = currentState.stats.totalSpoiledUnits + spoiledCount,
                    totalResearchPointsEarned = currentState.stats.totalResearchPointsEarned + researchPointsGained,
                    totalDividendsEarned = currentState.stats.totalDividendsEarned + totalDividendsToday,
                    totalCashEarned = currentState.stats.totalCashEarned + automatedSalesRevenue + contractsRevenueToday + autoSellRevenueToday + totalDividendsToday,
                    totalContractsFulfilled = currentState.stats.totalContractsFulfilled + completedContractsDelta,
                    facilitiesBuilt = currentState.stats.facilitiesBuilt + newlyBuiltFacilitiesCount
                )
                
                val finalState = currentState.copy(
                    day = nextDay,
                    dailyActionsRemaining = currentState.maxDailyActions,
                    cash = currentCash,
                    reputation = updatedReputation2,
                    researchPoints = currentState.researchPoints + researchPointsGained,
                    unlockedTechIds = effectiveUnlockedTechs,
                    activeProjects = remainingActiveProjects,
                    stats = updatedStats,
                    activeNewsEvent = activeNews,
                    activeCrises = updatedCrises,
                    newCrisisFired = currentState.newCrisisFired ?: newCrisisFired,
                    financialHistory = newFinancialHistory,
                    inventory = evaluatedInventory.toList(),
                    lifetimeSpoilage = currentState.lifetimeSpoilage + spoiledCount,
                    buildings = effectiveBuildings,
                    marketPrices = newMarketPrices,
                    bank = currentState.bank.copy(
                        totalDebt = (bankDebt * 100.0).toInt() / 100.0,
                        daysInDebt = daysInDebt,
                        investedFunds = (newInvestedFunds * 100.0).toInt() / 100.0
                    ),
                    todaySoldUnits = emptyMap(),
                    dailyLogs = newLogs,
                    latestReport = report,
                    isGameOver = isBankrupt,
                    gameOverReason = if (isBankrupt) "The Agricultural Credit Union foreclosed on all land and assets. Debt exceeded total enterprise value." else null,
                    rivalCompanies = updatedRivals,
                    pendingContractOffers = agedPendingOffers,
                    activeContracts = updatedActiveContracts,
                    completedContractsCount = currentState.completedContractsCount + completedContractsDelta,
                    totalContractRevenueEarned = currentState.totalContractRevenueEarned + contractsRevenueToday,
                    isEndgameTriggered = currentState.isEndgameTriggered || isEndgameReady,
                    mentors = finalMentors,
                    netWorthPhase = newPhase,
                    peakNetWorth = newPeak,
                    lifetimeRevenue = newRevenue,
                    isB2BUnlocked = b2bUnlocked,
                    unlockedFeatures = currentState.unlockedFeatures.copy(
                        isBoardroomUnlocked = shouldUnlockBoardroom,
                        isStockMarketUnlocked = shouldUnlockStockMarket,
                        isBoardroomNew = isNewBoardroom,
                        isStockMarketNew = isNewStockMarket
                    ),
                    missedDeliveryEvent = missedEventId
                )
                
                var newState = checkAchievements(finalState)
                for (id in newlyCompletedTechIds) {
                    newState = applyResearchEffect(newState, id)
                }
                
                val standardNodes = com.example.model.ResearchCatalog.ALL_NODES.filter { it.category != com.example.model.ResearchCategory.EXPERIMENTAL }
                val isStandardComplete = standardNodes.all { newState.researchNodeStatuses[it.id] == com.example.model.NodeStatus.COMPLETED }
                newState = newState.copy(isStandardTreeComplete = isStandardComplete)
                
                newState = processCorporateWarfare(newState, currentDay, notes)
                newState = processSubsidiaries(newState, notes)
                newState = evaluateStoryEvents(newState)
                
                newState
            }

            val finalState = _gameState.value
            if (finalState.netWorth >= 100_000.0 && !finalState.hasFired100kEvent) {
                _gameState.update { it.copy(hasFired100kEvent = true) }
                saveGame()
                _showMilestoneScreen.value = true
                return@launch
            }

            checkWinState()
            saveGame()
            
            if (triggeredNews != null) {
                _showNewsChronicleDialog.value = true
            } else {
                _showDailyReportDialog.value = true
            }
        }
    }
fun depositFunds(amount: Double) {
    _gameState.update { currentState ->
        if (currentState.cash < amount) {
            currentState
        } else {
            currentState.copy(
                cash = currentState.cash - amount,
                bank = currentState.bank.copy(
                    investedFunds = currentState.bank.investedFunds + amount
                )
            )
        }
    }
}

fun withdrawFunds(amount: Double) {
    _gameState.update { currentState ->
        val withdrawAmount = amount.coerceAtMost(currentState.bank.investedFunds)
        if (withdrawAmount <= 0.0) {
            currentState
        } else {
            currentState.copy(
                cash = currentState.cash + withdrawAmount,
                bank = currentState.bank.copy(
                    investedFunds = currentState.bank.investedFunds - withdrawAmount
                )
            )
        }
    }
}

fun processPlayerMessage(mentorId: String, messageText: String) {
    viewModelScope.launch {
        val currentState = _gameState.value
        val currentDay = currentState.day
        val mentorCheck = currentState.mentors.find { it.id == mentorId } ?: return@launch
        
        if (mentorCheck.isAbandoned) return@launch
        
        val playerMsgId = UUID.randomUUID().toString()
        var playerMsg = ChatMessage(
            id = playerMsgId,
            text = messageText,
            isFromPlayer = true,
            timestampDay = currentDay,
            status = MessageStatus.SENT
        )
        
        _gameState.update { current ->
            current.copy(
                mentors = current.mentors.map {
                    if (it.id == mentorId) {
                        it.copy(chatHistory = it.chatHistory + playerMsg)
                    } else it
                }
            )
        }
        
        delay(500L)
        
        playerMsg = playerMsg.copy(status = MessageStatus.DELIVERED)
        
        _gameState.update { current ->
            current.copy(
                mentors = current.mentors.map {
                    if (it.id == mentorId) {
                        it.copy(
                            chatHistory = it.chatHistory.map { msg ->
                                if (msg.id == playerMsgId) playerMsg else msg
                            }
                        )
                    } else it
                }
            )
        }
        
        val updatedState = _gameState.value
        val mentor = updatedState.mentors.find { it.id == mentorId } ?: return@launch
        val parsed = IntentParser.parseMessage(messageText, mentor.unlockedFeatures)
        
        var responseText = "I'm not sure how to respond to that."
        var affinityGain = 0
        var newHasGreeted = mentor.hasGreetedToday
        val newRecentlyAsked = mentor.recentlyAskedTopics.toMutableList()
        
        when (parsed.type) {
            IntentParser.IntentType.GREETING -> {
                if (!mentor.hasGreetedToday) {
                    responseText = "Hello there! Good to see you."
                    affinityGain = 1
                    newHasGreeted = true
                } else {
                    responseText = "We already said our hellos! What do you need?"
                }
            }
            IntentParser.IntentType.TOPIC_INQUIRY, IntentParser.IntentType.FEATURE_QUERY -> {
                val topic = parsed.matchedTopic ?: ""
                responseText = IntentParser.getResponseForFeature(mentorId, topic)
                if (!mentor.recentlyAskedTopics.contains(topic)) {
                    affinityGain = 1
                    newRecentlyAsked.add(topic)
                    if (newRecentlyAsked.size > 5) {
                        newRecentlyAsked.removeAt(0)
                    }
                }
            }
            IntentParser.IntentType.GIBBERISH, IntentParser.IntentType.UNKNOWN -> {
                responseText = "Could you rephrase that? I'm busy with other matters."
                affinityGain = -1
            }
        }
        
        val newAffinity = mentor.hiddenAffinity + affinityGain
        
        if (newAffinity <= -5) {
            _gameState.update { current ->
                current.copy(
                    mentors = current.mentors.map {
                        if (it.id == mentorId) {
                            it.copy(hiddenAffinity = newAffinity, isAbandoned = true)
                        } else it
                    }
                )
            }
            return@launch
        }
        
        val baseDelay = 1000L
        val lengthDelay = responseText.length * 20L
        val affinityPenalty = if (newAffinity < 0) kotlin.math.abs(newAffinity) * 1000L else 0L
        val typingDelay = baseDelay + lengthDelay + affinityPenalty
        
        playerMsg = playerMsg.copy(status = MessageStatus.READ)
        
        _gameState.update { current ->
            current.copy(
                mentors = current.mentors.map {
                    if (it.id == mentorId) {
                        it.copy(
                            chatHistory = it.chatHistory.map { msg ->
                                if (msg.id == playerMsgId) playerMsg else msg
                            },
                            isTyping = true
                        )
                    } else it
                }
            )
        }
        
        delay(typingDelay)
        
        _gameState.update { current ->
            val m = current.mentors.find { it.id == mentorId } ?: return@update current
            
            val newBuffUnlocked = m.isBuffUnlocked || newAffinity >= 50
            if (newBuffUnlocked && !m.isBuffUnlocked) {
                _snackBarMessage.value = "\ud83c\udf89 ${m.name} respects you enough to unlock their passive buff!"
            }
            
            val mentorMsg = ChatMessage(
                text = responseText,
                isFromPlayer = false,
                timestampDay = current.day
            )
            
            current.copy(
                mentors = current.mentors.map {
                    if (it.id == mentorId) {
                        it.copy(
                            hiddenAffinity = newAffinity,
                            isBuffUnlocked = newBuffUnlocked,
                            chatHistory = it.chatHistory + mentorMsg,
                            hasGreetedToday = newHasGreeted,
                            recentlyAskedTopics = newRecentlyAsked.toList(),
                            isTyping = false
                        )
                    } else it
                }
            )
        }
    }
}

fun sendApologyGift(mentorId: String) {
    _gameState.update { currentState ->
        val mentor = currentState.mentors.find { it.id == mentorId }
        if (mentor == null || !mentor.isAbandoned) {
            return@update currentState
        }
        
        val giftCost = 500.0
        if (currentState.cash < giftCost) {
            _snackBarMessage.value = "You can't afford a $500 apology gift."
            return@update currentState
        }
        
        _snackBarMessage.value = "Sent a premium apology gift to ${mentor.name}."
        
        val msg1 = ChatMessage(
            text = "\ud83c\udf81 You sent a premium Apology Basket.",
            isFromPlayer = true,
            timestampDay = currentState.day,
            status = MessageStatus.READ
        )
        val msg2 = ChatMessage(
            text = "I received your gift. Let's start fresh. But don't waste my time again.",
            isFromPlayer = false,
            timestampDay = currentState.day
        )
        
        currentState.copy(
            cash = currentState.cash - giftCost,
            mentors = currentState.mentors.map {
                if (it.id == mentorId) {
                    it.copy(
                        hiddenAffinity = 0,
                        isAbandoned = false,
                        chatHistory = it.chatHistory + msg1 + msg2
                    )
                } else {
                    it
                }
            }
        )
    }
}

fun hireExecutive(executiveId: String) {
    _gameState.update { currentState ->
        val executive = currentState.executives.find { it.id == executiveId }
        if (executive == null || executive.isHired) {
            return@update currentState
        }
        
        if (currentState.cash < executive.hiringCost) {
            _snackBarMessage.value = "Insufficient funds to hire ${executive.name}."
            return@update currentState
        }
        
        val costFormatted = String.format("%,.2f", executive.hiringCost)
        _snackBarMessage.value = "Hired ${executive.name} (${executive.role}) for $$costFormatted!"
        
        val newLog = "\ud83e\udd1d Hired ${executive.role}: ${executive.name} joined the executive board."
        val newLogs = listOf(newLog) + currentState.dailyLogs.take(19)
        
        currentState.copy(
            cash = currentState.cash - executive.hiringCost,
            dailyLogs = newLogs,
            executives = currentState.executives.map {
                if (it.id == executiveId) {
                    it.copy(isHired = true)
                } else {
                    it
                }
            }
        )
    }
}
fun acceptContract(offerId: String) {
    _gameState.update { current ->
        val offer = current.pendingContractOffers.find { it.id == offerId }
        if (offer == null) {
            _snackBarMessage.value = "Contract offer is no longer active."
            return@update current
        }

        val rival = current.rivalCompanies.find { it.id == offer.rivalId }
            ?: RivalCatalog.getRivalById(offer.rivalId)
        val product = ProductCatalog.getById(offer.targetProduct)
        
        val executedContract = offer.copy(isAccepted = true)
        
        val updatedPending = current.pendingContractOffers.filter { it.id != offerId }
        val updatedActive = current.activeContracts + executedContract

        val formattedPayout = String.format("%.2f", offer.payoutAmount)
        _snackBarMessage.value = "🤝 Contract Executed with ${rival.name}! Quota: ${offer.requiredQuantity}x ${product.name}/day."

        val newLog = "🤝 Executed B2B Contract with ${rival.name}: ${offer.requiredQuantity}x ${product.name} @ $$formattedPayout/day for ${offer.durationDays} days."
        val updatedLogs = listOf(newLog) + current.dailyLogs.take(19)

        checkAchievements(current.copy(
            pendingContractOffers = updatedPending,
            activeContracts = updatedActive,
            dailyLogs = updatedLogs
        ))
    }
}

fun declineContract(offerId: String) {
    _gameState.update { current ->
        val offer = current.pendingContractOffers.find { it.id == offerId }
        val rival = offer?.let { o -> current.rivalCompanies.find { it.id == o.rivalId } }
        
        val updatedPending = current.pendingContractOffers.filter { it.id != offerId }
        val rivalName = rival?.name ?: "rival"
        _snackBarMessage.value = "Contract offer from $rivalName declined."

        current.copy(
            pendingContractOffers = updatedPending
        )
    }
}

fun bargainContract(offerId: String) {
    _gameState.update { current ->
        val offer = current.pendingContractOffers.find { it.id == offerId }
        if (offer == null) {
            _snackBarMessage.value = "Contract offer is no longer active."
            return@update current
        }

        val rival = current.rivalCompanies.find { it.id == offer.rivalId }
            ?: RivalCatalog.getRivalById(offer.rivalId)

        val silverTongueLevel = current.playerSkills.silverTongueLevel
        val successChance = (50 + silverTongueLevel * 15).coerceAtMost(95)
        val roll = kotlin.random.Random.nextInt(1, 101)

        if (roll <= successChance) {
            val newPayout = (offer.payoutAmount * 1.15 * 100.0).toInt() / 100.0
            val updatedOffer = offer.copy(
                payoutAmount = newPayout,
                bargainCount = offer.bargainCount + 1
            )
            
            val updatedPending = current.pendingContractOffers.map {
                if (it.id == offerId) updatedOffer else it
            }
            
            val formattedPayout = String.format("%.2f", newPayout)
            _snackBarMessage.value = "🎲 Bargain Successful! Silver Tongue charmed ${rival.name}. Payout raised +15% to $$formattedPayout/day!"

            val newLog = "🎲 Hardball Negotiation: Charmed ${rival.name}, boosting contract daily payout to $$formattedPayout."
            val updatedLogs = listOf(newLog) + current.dailyLogs.take(19)

            current.copy(
                pendingContractOffers = updatedPending,
                dailyLogs = updatedLogs
            )
        } else {
            val updatedPending = current.pendingContractOffers.filter { it.id != offerId }
            _snackBarMessage.value = "🎲 Bargain Failed! ${rival.name} was insulted by your aggressive terms and revoked the offer!"

            val newLog = "⚠️ Deal Collapsed: ${rival.name} was insulted during negotiation and revoked their RFP offer."
            val updatedLogs = listOf(newLog) + current.dailyLogs.take(19)

            current.copy(
                pendingContractOffers = updatedPending,
                dailyLogs = updatedLogs
            )
        }
    }
}

fun upgradePlayerSkill(skillType: SkillType) {
    _gameState.update { current ->
        val currentLevel = current.playerSkills.getLevel(skillType)
        if (currentLevel >= skillType.maxLevel) {
            _snackBarMessage.value = "${skillType.title} is already at max level!"
            return@update current
        }

        val cost = skillType.costForLevel(currentLevel)
        if (current.researchPoints < cost) {
            _snackBarMessage.value = "Insufficient RP. Requires $cost RP (Have ${current.researchPoints} RP)."
            return@update current
        }

        val newLevel = currentLevel + 1
        val newSkills = when (skillType) {
            SkillType.HUSTLER -> current.playerSkills.copy(hustlerLevel = newLevel)
            SkillType.EFFICIENCY_EXPERT -> current.playerSkills.copy(efficiencyExpertLevel = newLevel)
            SkillType.SILVER_TONGUE -> current.playerSkills.copy(silverTongueLevel = newLevel)
            SkillType.STAMINA -> current.playerSkills.copy(staminaLevel = newLevel)
            SkillType.BOVINE_GENETICS -> current.playerSkills.copy(bovineGeneticsLevel = newLevel)
            SkillType.COLD_CHAIN_LOGISTICS -> current.playerSkills.copy(coldChainLogisticsLevel = newLevel)
            SkillType.MULTI_TASKING -> current.playerSkills.copy(multiTaskingLevel = newLevel)
        }

        val extraAction = if (skillType == SkillType.STAMINA) 1 else 0
        _snackBarMessage.value = "Upgraded ${skillType.title} to Level $newLevel! ✨"

        checkAchievements(current.copy(
            researchPoints = current.researchPoints - cost,
            playerSkills = newSkills,
            dailyActionsRemaining = current.dailyActionsRemaining + extraAction
        ))
    }
}

private fun tryAssignProject(current: GameState, baseProject: ActiveProject): ActiveProject? {
    val isResearch = baseProject.type == ProjectType.TECH_RESEARCH
    val dedicatedCapacity = if (isResearch) current.ownedLabs else current.ownedConstructionCrews
    
    val currentDedicatedCount = current.activeProjects.count { 
        it.isDedicated && if (isResearch) it.type == ProjectType.TECH_RESEARCH else (it.type == ProjectType.FACILITY_CONSTRUCTION || it.type == ProjectType.FACILITY_UPGRADE)
    }

    if (currentDedicatedCount < dedicatedCapacity) {
        return baseProject.copy(isDedicated = true)
    }

    val personalCapacity = current.playerSkills.personalActionCapacity
    val currentPersonalCount = current.activeProjects.count { !it.isDedicated }

    if (currentPersonalCount < personalCapacity) {
        return baseProject.copy(isDedicated = false)
    }

    return null
}

fun unlockTechnology(techId: String) {
    android.util.Log.d("ResearchDebug", "Attempting to unlock node: $techId")
    _gameState.update { current ->
        if (current.unlockedTechIds.contains(techId)) {
            _snackBarMessage.value = "Technology is already researched."
            return@update current
        }

        if (current.activeProjects.any { it.type == ProjectType.TECH_RESEARCH && it.targetId == techId }) {
            _snackBarMessage.value = "R&D research is already underway for this technology!"
            return@update current
        }

        val node = TechCatalog.ALL_TECHS.firstOrNull { it.id == techId }
        if (node == null) {
            _snackBarMessage.value = "Unknown technology."
            return@update current
        }

        val parentId = node.parentId
        val prereqsMet = parentId == null || current.unlockedTechIds.contains(parentId) || TechCatalog.ALL_TECHS.firstOrNull { it.id == parentId }?.rpCost == 0
        android.util.Log.d("ResearchDebug", "Prerequisites met: $prereqsMet")

        if (!prereqsMet) {
            val missingTitle = TechCatalog.ALL_TECHS.firstOrNull { it.id == parentId }?.name ?: "Prerequisite"
            _snackBarMessage.value = "Requires prerequisite research: $missingTitle first!"
            return@update current
        }

        val hasRP = current.researchPoints >= node.rpCost
        android.util.Log.d("ResearchDebug", "Sufficient RP: $hasRP (Cost: ${node.rpCost}, Current: ${current.researchPoints})")

        if (!hasRP) {
            _snackBarMessage.value = "Insufficient RP. Requires ${node.rpCost} (Have ${current.researchPoints})."
            return@update current
        }

        if (node.daysToComplete <= 0) {
            _snackBarMessage.value = "Unlocked ${node.name} instantly!"
            android.util.Log.d("ResearchDebug", "Unlock successful (instant), new state emitted.")
            return@update checkAchievements(current.copy(
                researchPoints = current.researchPoints - node.rpCost,
                unlockedTechIds = current.unlockedTechIds + techId
            ))
        }

        val newProject = ActiveProject(
            type = ProjectType.TECH_RESEARCH,
            targetId = techId,
            targetName = node.name,
            iconEmoji = node.iconEmoji,
            totalDays = node.daysToComplete,
            daysRemaining = node.daysToComplete
        )

        val assignedProject = tryAssignProject(current, newProject)
        if (assignedProject == null) {
            _snackBarMessage.value = "All Slots & Labs Busy! Upgrade Multi-Tasking or build Labs."
            return@update current
        }

        _snackBarMessage.value = "Initiated R&D on ${node.name}! (${node.daysToComplete} days to finish)."
        android.util.Log.d("ResearchDebug", "Unlock successful (assigned project), new state emitted.")

        checkAchievements(current.copy(
            researchPoints = current.researchPoints - node.rpCost,
            activeProjects = current.activeProjects + assignedProject
        ))
    }
}

fun toggleAutoBuy(itemId: String, isActive: Boolean) {
    _gameState.update { current ->
        val updatedMap = current.autoBuySubscriptions.toMutableMap()
        if (isActive) {
            updatedMap[itemId] = true
            _snackBarMessage.value = "Enabled Auto-Procurement for ${ProductCatalog.getById(itemId).name}."
        } else {
            updatedMap.remove(itemId)
            _snackBarMessage.value = "Disabled Auto-Procurement for ${ProductCatalog.getById(itemId).name}."
        }
        current.copy(autoBuySubscriptions = updatedMap)
    }
}

fun toggleAutoSell(itemId: String, isActive: Boolean) {
    _gameState.update { current ->
        val updatedMap = current.autoSellSubscriptions.toMutableMap()
        if (isActive) {
            updatedMap[itemId] = true
            _snackBarMessage.value = "Enabled Auto-Sell for ${ProductCatalog.getById(itemId).name}."
        } else {
            updatedMap.remove(itemId)
            _snackBarMessage.value = "Disabled Auto-Sell for ${ProductCatalog.getById(itemId).name}."
        }
        current.copy(autoSellSubscriptions = updatedMap)
    }
}

fun workManualLabor() {
    _gameState.update { current ->
        if (current.dailyActionsRemaining <= 0) {
            _snackBarMessage.value = "⚡ Out of Daily Actions! Click 'End Day' to rest."
            return@update current
        }

        val hustlerBonus = current.playerSkills.hustlerCashBonus
        val earnings = 35.0 + current.reputation * 0.3 + hustlerBonus
        
        val hustlerText = if (hustlerBonus > 0.0) {
            " (+$${String.format("%.0f", hustlerBonus)} Hustler)"
        } else {
            ""
        }

        val formattedEarnings = String.format("%.2f", earnings)
        _snackBarMessage.value = "Worked overtime! +$$formattedEarnings$hustlerText (1 Action used)"

        val newStats = current.stats.copy(
            totalCashEarned = current.stats.totalCashEarned + earnings
        )

        checkAchievements(current.copy(
            cash = current.cash + earnings,
            dailyActionsRemaining = current.dailyActionsRemaining - 1,
            manualLaborCount = current.manualLaborCount + 1,
            stats = newStats
        ))
    }
}

fun studyManualResearch() {
    _gameState.update { current ->
        if (current.dailyActionsRemaining <= 0) {
            _snackBarMessage.value = "⚡ Out of Daily Actions! Click 'End Day' to rest."
            return@update current
        }

        val rpGained = 4
        _snackBarMessage.value = "Studied dairy biochemistry! +$rpGained Research Points (1 Action used)"

        val newStats = current.stats.copy(
            totalResearchPointsEarned = current.stats.totalResearchPointsEarned + rpGained
        )

        current.copy(
            researchPoints = current.researchPoints + rpGained,
            dailyActionsRemaining = current.dailyActionsRemaining - 1,
            stats = newStats
        )
    }
}

fun sellProduct(productType: String, amount: Int) {
    if (amount <= 0) return
    _gameState.update { current ->
        val isSpoiledMilk = productType == ProductCatalog.SPOILED_MILK.id
        val sellableBatches = current.inventory.filter { 
            it.itemId == productType && (isSpoiledMilk || !it.isSpoiled) 
        }
        val totalAvailable = sellableBatches.sumOf { it.quantity }

        if (totalAvailable <= 0) {
            _snackBarMessage.value = "No unspoiled stock available to sell."
            return@update current
        }

        var remainingToSell = amount.coerceAtMost(totalAvailable)
        var totalRevenue = 0.0

        val basePrice = current.marketPrices[productType]?.currentPrice ?: ProductCatalog.getById(productType).basePrice
        val repMultiplier = 1.0 + (current.reputation * 0.003 * current.playerSkills.silverTongueRepBonusMultiplier)
        val mooCorpMultiplier = if (current.subsidiaryCompanyIds.contains("rival_moocorp")) 1.2 else 1.0
        val isCheeseOrButter = listOf(ProductCatalog.AGED_CHEDDAR.id, ProductCatalog.FRESH_CHEESE.id, ProductCatalog.BUTTER.id, ProductCatalog.CREAM.id).contains(productType)
        val lactoMultiplier = if (isCheeseOrButter && current.subsidiaryCompanyIds.contains("rival_lacto_dynasty")) 1.35 else 1.0
        val endgameMultiplier = current.endgamePriceMultiplier

        val updatedInventory = current.inventory.toMutableList()
        val batchesToProcess = if (current.inventoryMethod == InventoryMethod.LIFO) sellableBatches.reversed() else sellableBatches

        for (batch in batchesToProcess) {
            if (remainingToSell <= 0) break
            val index = updatedInventory.indexOf(batch)
            if (index == -1) continue

            val quantityToTake = remainingToSell.coerceAtMost(batch.quantity)
            val finalPricePerUnit = basePrice * (0.8 + batch.quality * 0.2) * repMultiplier * mooCorpMultiplier * lactoMultiplier * endgameMultiplier
            totalRevenue += quantityToTake * finalPricePerUnit
            remainingToSell -= quantityToTake

            if (batch.quantity <= quantityToTake) {
                updatedInventory.removeAt(index)
            } else {
                updatedInventory[index] = batch.copy(quantity = batch.quantity - quantityToTake)
            }
        }

        val actualSold = amount.coerceAtMost(totalAvailable) - remainingToSell
        val todaySoldUnits = current.todaySoldUnits.toMutableMap()
        todaySoldUnits[productType] = (todaySoldUnits[productType] ?: 0) + actualSold

        val productName = ProductCatalog.getById(productType).name
        val formattedRevenue = String.format("%.2f", totalRevenue)
        _snackBarMessage.value = "Sold $actualSold units of $productName for +$$formattedRevenue"

        checkAchievements(current.copy(
            cash = current.cash + totalRevenue,
            inventory = updatedInventory,
            todaySoldUnits = todaySoldUnits
        ))
    }
}

fun sellInventoryBatch(batchId: String, quantityToSell: Int) {
    _gameState.update { current ->
        val batchIndex = current.inventory.indexOfFirst { it.batchId == batchId }
        if (batchIndex == -1) return@update current

        val batch = current.inventory[batchIndex]
        val isSpoiledMilk = batch.itemId == ProductCatalog.SPOILED_MILK.id
        if (batch.isSpoiled && !isSpoiledMilk) {
            _snackBarMessage.value = "Cannot sell spoiled batch as regular product! Please dump spoiled goods."
            return@update current
        }

        val actualSellAmount = quantityToSell.coerceIn(1, batch.quantity)
        val basePrice = current.marketPrices[batch.itemId]?.currentPrice ?: ProductCatalog.getById(batch.itemId).basePrice
        val repMultiplier = 1.0 + (current.reputation * 0.003 * current.playerSkills.silverTongueRepBonusMultiplier)
        val mooCorpMultiplier = if (current.subsidiaryCompanyIds.contains("rival_moocorp")) 1.2 else 1.0
        val isCheeseOrButter = listOf(ProductCatalog.AGED_CHEDDAR.id, ProductCatalog.FRESH_CHEESE.id, ProductCatalog.BUTTER.id, ProductCatalog.CREAM.id).contains(batch.itemId)
        val lactoMultiplier = if (isCheeseOrButter && current.subsidiaryCompanyIds.contains("rival_lacto_dynasty")) 1.35 else 1.0
        val endgameMultiplier = current.endgamePriceMultiplier

        val finalPricePerUnit = basePrice * (0.8 + batch.quality * 0.2) * repMultiplier * mooCorpMultiplier * lactoMultiplier * endgameMultiplier
        val totalRevenue = actualSellAmount * finalPricePerUnit

        val updatedInventory = current.inventory.toMutableList()
        if (batch.quantity <= actualSellAmount) {
            updatedInventory.removeAt(batchIndex)
        } else {
            updatedInventory[batchIndex] = batch.copy(quantity = batch.quantity - actualSellAmount)
        }

        val todaySoldUnits = current.todaySoldUnits.toMutableMap()
        todaySoldUnits[batch.itemId] = (todaySoldUnits[batch.itemId] ?: 0) + actualSellAmount

        val productName = ProductCatalog.getById(batch.itemId).name
        val formattedRevenue = String.format("%.2f", totalRevenue)
        _snackBarMessage.value = "Sold $actualSellAmount units of $productName for +$$formattedRevenue"

        checkAchievements(current.copy(
            cash = current.cash + totalRevenue,
            inventory = updatedInventory,
            todaySoldUnits = todaySoldUnits
        ))
    }
}

fun sellAllOfProduct(productId: String) {
    _gameState.update { current ->
        val isSpoiledMilk = productId == ProductCatalog.SPOILED_MILK.id
        val sellableBatches = current.inventory.filter { 
            it.itemId == productId && (isSpoiledMilk || !it.isSpoiled) 
        }
        val totalQuantity = sellableBatches.sumOf { it.quantity }

        if (totalQuantity == 0) {
            _snackBarMessage.value = "No stock to sell for this product."
            return@update current
        }

        val basePrice = current.marketPrices[productId]?.currentPrice ?: ProductCatalog.getById(productId).basePrice
        val repMultiplier = 1.0 + (current.reputation * 0.003 * current.playerSkills.silverTongueRepBonusMultiplier)
        val mooCorpMultiplier = if (current.subsidiaryCompanyIds.contains("rival_moocorp")) 1.2 else 1.0
        val isCheeseOrButter = listOf(ProductCatalog.AGED_CHEDDAR.id, ProductCatalog.FRESH_CHEESE.id, ProductCatalog.BUTTER.id, ProductCatalog.CREAM.id).contains(productId)
        val lactoMultiplier = if (isCheeseOrButter && current.subsidiaryCompanyIds.contains("rival_lacto_dynasty")) 1.35 else 1.0
        val endgameMultiplier = current.endgamePriceMultiplier

        var totalRevenue = 0.0
        for (batch in sellableBatches) {
            val finalPricePerUnit = basePrice * (0.8 + batch.quality * 0.2) * repMultiplier * mooCorpMultiplier * lactoMultiplier * endgameMultiplier
            totalRevenue += batch.quantity * finalPricePerUnit
        }

        val updatedInventory = current.inventory.filter { 
            it.itemId != productId || (it.isSpoiled && !isSpoiledMilk)
        }

        val todaySoldUnits = current.todaySoldUnits.toMutableMap()
        todaySoldUnits[productId] = (todaySoldUnits[productId] ?: 0) + totalQuantity

        val productName = ProductCatalog.getById(productId).name
        val formattedRevenue = String.format("%.2f", totalRevenue)
        _snackBarMessage.value = "Liquidated $totalQuantity units of $productName for +$$formattedRevenue"

        checkAchievements(current.copy(
            cash = current.cash + totalRevenue,
            inventory = updatedInventory,
            todaySoldUnits = todaySoldUnits
        ))
    }
}
fun buyBuilding(buildingId: String) {
    _gameState.update { state ->
        val building = state.buildings.find { it.id == buildingId }
        if (building == null) {
            _snackBarMessage.value = "Facility blueprint not found."
            return@update state
        }
        if (building.isConstructed) {
            _snackBarMessage.value = "${building.name} is already constructed. Upgrade it instead!"
            return@update state
        }
        if (state.isBuildingUnderConstruction(buildingId)) {
            _snackBarMessage.value = "${building.name} is already under construction!"
            return@update state
        }
        if (building.requiredTechId != null && !state.unlockedTechIds.contains(building.requiredTechId)) {
            val reqName = TechCatalog.ALL_TECHS.find { it.id == building.requiredTechId }?.name ?: "Prerequisite"
            _snackBarMessage.value = "Locked! Requires R&D Technology: '$reqName'."
            return@update state
        }
        if (state.usedLand + building.landRequired > state.totalLandCapacity) {
            _snackBarMessage.value = "Insufficient Real Estate! Expand Land in Facilities tab (${state.usedLand}/${state.totalLandCapacity} plots used)."
            return@update state
        }
        if (state.cash < building.baseCost) {
            _snackBarMessage.value = "Cannot afford ${building.name}. Requires $${String.format("%.2f", building.baseCost)} (Have $${String.format("%.2f", state.cash)})."
            return@update state
        }

        val activeProject = ActiveProject(
            type = ProjectType.FACILITY_CONSTRUCTION,
            targetId = buildingId,
            targetName = building.name,
            iconEmoji = building.iconEmoji,
            daysRemaining = building.daysToComplete,
            totalDays = building.daysToComplete,
            targetLevel = 0,
            rushCostPerDay = 120.0
        )

        val assignedProject = tryAssignProject(state, activeProject)
        if (assignedProject == null) {
            _snackBarMessage.value = "All Slots & Crews Busy! Upgrade Multi-Tasking or assign more Construction Crews."
            return@update state
        }

        _snackBarMessage.value = "Started construction of ${building.name}! (${building.daysToComplete} days to finish)."
        
        checkAchievements(
            state.copy(
                cash = state.cash - building.baseCost,
                activeProjects = state.activeProjects + assignedProject,
                reputation = (state.reputation + 2).coerceAtMost(100)
            )
        )
    }
}

fun upgradeBuilding(buildingId: String) {
    _gameState.update { state ->
        val building = state.buildings.find { it.id == buildingId }
        if (building == null || !building.isConstructed) {
            _snackBarMessage.value = "Facility not built yet."
            return@update state
        }
        if (building.level >= building.maxLevel) {
            _snackBarMessage.value = "${building.name} is already at max level!"
            return@update state
        }
        if (state.isBuildingUnderUpgrade(buildingId)) {
            _snackBarMessage.value = "${building.name} upgrade is already in progress!"
            return@update state
        }
        if (state.cash < building.upgradeCost) {
            _snackBarMessage.value = "Insufficient cash. Upgrading requires $${String.format("%.2f", building.upgradeCost)}."
            return@update state
        }

        val nextLevel = building.level + 1
        val activeProject = ActiveProject(
            type = ProjectType.FACILITY_UPGRADE,
            targetId = buildingId,
            targetName = "${building.name} (Lv.$nextLevel)",
            iconEmoji = building.iconEmoji,
            daysRemaining = building.upgradeDaysToComplete,
            totalDays = building.upgradeDaysToComplete,
            targetLevel = nextLevel,
            rushCostPerDay = 150.0
        )

        val assignedProject = tryAssignProject(state, activeProject)
        if (assignedProject == null) {
            _snackBarMessage.value = "All Slots & Crews Busy! Upgrade Multi-Tasking or assign more Construction Crews."
            return@update state
        }

        _snackBarMessage.value = "Commissioned upgrade for ${building.name} to Level $nextLevel! (${building.upgradeDaysToComplete} days)."

        checkAchievements(
            state.copy(
                cash = state.cash - building.upgradeCost,
                activeProjects = state.activeProjects + assignedProject,
                reputation = (state.reputation + 1).coerceAtMost(100)
            )
        )
    }
}

fun buyLandExpansion() {
    _gameState.update { state ->
        val cost = state.nextLandCost
        if (state.cash < cost) {
            _snackBarMessage.value = "Cannot afford land plot. Requires $${String.format("%.2f", cost)} (Have $${String.format("%.2f", state.cash)})."
            return@update state
        }
        
        val newPlots = state.purchasedLandPlots + 1
        val newCapacity = state.maxLandCapacity + newPlots
        _snackBarMessage.value = "Acquired real estate plot! Estate expanded to $newCapacity plots \ud83c\udfe1."
        
        checkAchievements(
            state.copy(
                cash = state.cash - cost,
                purchasedLandPlots = newPlots,
                reputation = (state.reputation + 4).coerceAtMost(100),
                stats = state.stats.copy(
                    totalLandPlotsBought = state.stats.totalLandPlotsBought + 1
                )
            )
        )
    }
}

fun rushProject(projectId: String) {
    _gameState.update { state ->
        val project = state.activeProjects.find { it.id == projectId }
        if (project == null) {
            _snackBarMessage.value = "Project not found or already finished."
            return@update state
        }

        val rushCost = project.totalRushCost
        if (state.cash < rushCost) {
            _snackBarMessage.value = "Insufficient cash to rush! Requires $${String.format("%.2f", rushCost)} (Have $${String.format("%.2f", state.cash)})."
            return@update state
        }

        var facilitiesBuiltAdded = 0
        var updatedBuildings = state.buildings
        var updatedTechs = state.unlockedTechIds

        when (project.type) {
            ProjectType.FACILITY_CONSTRUCTION -> {
                updatedBuildings = state.buildings.map { b ->
                    if (b.id == project.targetId) b.copy(isConstructed = true) else b
                }
                facilitiesBuiltAdded = 1
            }
            ProjectType.FACILITY_UPGRADE -> {
                updatedBuildings = state.buildings.map { b ->
                    if (b.id == project.targetId) b.copy(level = project.targetLevel) else b
                }
            }
            ProjectType.TECH_RESEARCH -> {
                updatedTechs = state.unlockedTechIds + project.targetId
            }
        }

        _snackBarMessage.value = "⚡ RUSHED! Completed ${project.targetName} immediately for $${String.format("%.2f", rushCost)}!"

        checkAchievements(
            state.copy(
                cash = state.cash - rushCost,
                buildings = updatedBuildings,
                unlockedTechIds = updatedTechs,
                activeProjects = state.activeProjects.filterNot { it.id == projectId },
                reputation = (state.reputation + 2).coerceAtMost(100),
                stats = state.stats.copy(
                    totalRushCount = state.stats.totalRushCount + 1,
                    facilitiesBuilt = state.stats.facilitiesBuilt + facilitiesBuiltAdded
                )
            )
        )
    }
}

fun toggleBuildingOperational(buildingId: String) {
    _gameState.update { state ->
        state.copy(
            buildings = state.buildings.map { b ->
                if (b.id == buildingId && b.isConstructed) {
                    val newOperational = !b.isOperational
                    val status = if (newOperational) "Activated" else "Pausd / Idle"
                    _snackBarMessage.value = "$status ${b.name}."
                    b.copy(isOperational = newOperational)
                } else {
                    b
                }
            }
        )
    }
}

fun setFacilityRecipe(buildingId: String, recipe: ProcessingRecipe) {
    _gameState.update { state ->
        state.copy(
            buildings = state.buildings.map { b ->
                if (b.id == buildingId && b.isConstructed) {
                    _snackBarMessage.value = "Set ${b.name} recipe to: ${recipe.name}"
                    b.copy(activeRecipe = recipe)
                } else {
                    b
                }
            }
        )
    }
}

fun borrowLoan(amount: Double) {
    if (amount <= 0.0) return
    _gameState.update { state ->
        val availableCredit = state.bank.maxCreditLimit - state.bank.totalDebt
        if (amount > availableCredit) {
            _snackBarMessage.value = "Loan exceeds max available credit limit ($${String.format("%.2f", availableCredit)} max)."
            return@update state
        }

        val newDebt = state.bank.totalDebt + amount
        _snackBarMessage.value = "Approved loan of $${String.format("%.2f", amount)}. Daily interest: ${state.bank.dailyInterestRate * 100}%."

        checkAchievements(
            state.copy(
                cash = state.cash + amount,
                bank = state.bank.copy(
                    totalDebt = newDebt,
                    daysInDebt = if (state.bank.daysInDebt == 0) 1 else state.bank.daysInDebt
                )
            )
        )
    }
}

fun repayLoan(amount: Double) {
    if (amount <= 0.0) return
    _gameState.update { state ->
        if (!state.bank.isInDebt) {
            _snackBarMessage.value = "You currently have no outstanding loan debt."
            return@update state
        }

        val repayAmount = minOf(amount, state.bank.totalDebt).coerceAtMost(state.cash)
        if (repayAmount <= 0.0) {
            _snackBarMessage.value = "Insufficient cash to repay loan."
            return@update state
        }

        val newDebt = state.bank.totalDebt - repayAmount
        _snackBarMessage.value = "Repaid $${String.format("%.2f", repayAmount)} to the bank."

        checkAchievements(
            state.copy(
                cash = state.cash - repayAmount,
                bank = state.bank.copy(
                    totalDebt = newDebt,
                    daysInDebt = if (newDebt <= 0.0) 0 else state.bank.daysInDebt
                )
            )
        )
    }
}

fun constructOrUpgradeBuilding(buildingId: String) {
    val state = _gameState.value
    val building = state.buildings.find { it.id == buildingId } ?: return

    if (!building.isConstructed) {
        buyBuilding(buildingId)
    } else {
        upgradeBuilding(buildingId)
    }
}

fun setBuildingRecipe(buildingId: String, recipeIndex: Int) {
    val state = _gameState.value
    val building = state.buildings.find { it.id == buildingId } ?: return

    if (recipeIndex in building.availableRecipes.indices) {
        setFacilityRecipe(buildingId, building.availableRecipes[recipeIndex])
    }
}

fun updateFacilityAllocation(buildingId: String, percentage: Int) {
    _gameState.update { state ->
        state.copy(
            buildings = state.buildings.map { b ->
                if (b.id == buildingId) {
                    b.copy(allocationPercentage = percentage.coerceIn(0, 100))
                } else {
                    b
                }
            }
        )
    }
}

fun setInventoryMethod(method: InventoryMethod) {
    _gameState.update { state ->
        state.copy(inventoryMethod = method)
    }
    _snackBarMessage.value = "Inventory processing method set to ${method.name}"
}

fun upgradeFactoryCapacity(buildingId: String) {
    _gameState.update { state ->
        val building = state.buildings.find { it.id == buildingId } ?: return@update state

        val requiredTech = when (building.capacityTier) {
            1 -> "tech_industrial_throughput_1"
            2 -> "tech_industrial_throughput_2"
            3 -> "tech_industrial_throughput_3"
            else -> null
        }

        if (requiredTech != null && !state.unlockedTechIds.contains(requiredTech)) {
            _snackBarMessage.value = "Required technology not researched yet!"
            return@update state
        }

        if (building.capacityTier >= 4) {
            _snackBarMessage.value = "Maximum capacity tier reached."
            return@update state
        }

        val upgradeCost = building.capacityUpgradeCost
        if (state.cash < upgradeCost) {
            _snackBarMessage.value = "Insufficient funds for capacity upgrade. Need $${String.format("%,.0f", upgradeCost)}"
            return@update state
        }

        _snackBarMessage.value = "✅ ${building.name} throughput capacity upgraded to Tier ${building.capacityTier + 1}!"

        state.copy(
            cash = state.cash - upgradeCost,
            buildings = state.buildings.map { b ->
                if (b.id == buildingId) {
                    b.copy(capacityTier = building.capacityTier + 1)
                } else {
                    b
                }
            }
        )
    }
}
fun takeBankLoan(amount: Double) {
    borrowLoan(amount)
}

fun repayBankLoan(amount: Double) {
    repayLoan(amount)
}

fun buyShares(rivalId: String, quantity: Int) {
    if (quantity <= 0) return
    _gameState.update { current ->
        val rivalCompany = current.rivalCompanies.find { it.id == rivalId }
            ?: RivalCatalog.getRivalById(rivalId)
        
        val sharesOwned = current.getSharesOwned(rivalId)
        val availableShares = (rivalCompany.totalShares - sharesOwned).coerceAtLeast(0)
        
        if (availableShares <= 0) {
            _snackBarMessage.value = "You already own 100% of all circulating shares in ${rivalCompany.name}!"
            return@update current
        }
        
        val sharesToBuy = quantity.coerceAtMost(availableShares)
        val cost = sharesToBuy * rivalCompany.stockPrice
        
        if (current.cash < cost) {
            val costStr = String.format("%.2f", cost)
            val cashStr = String.format("%.2f", current.cash)
            _snackBarMessage.value = "Insufficient liquid cash. Buying $sharesToBuy shares requires $$costStr (Have $$cashStr)."
            return@update current
        }
        
        val newSharesOwned = sharesOwned + sharesToBuy
        val newRivalSharesOwned = current.rivalSharesOwned + (rivalId to newSharesOwned)
        
        val alreadySubsidiary = current.subsidiaryCompanyIds.contains(rivalId)
        val becomesSubsidiary = newSharesOwned >= 51
        
        val newSubsidiaries = if (becomesSubsidiary && !alreadySubsidiary) {
            current.subsidiaryCompanyIds + rivalId
        } else {
            current.subsidiaryCompanyIds
        }
        
        val newPendingOffers = if (becomesSubsidiary && !alreadySubsidiary) {
            current.pendingContractOffers.filter { it.rivalId != rivalId }
        } else {
            current.pendingContractOffers
        }
        
        val logsToAdd = if (becomesSubsidiary && !alreadySubsidiary) {
            listOf("🚨 HOSTILE TAKEOVER COMPLETE: Acquired $newSharesOwned% controlling stake in ${rivalCompany.name}! Subsidiary Perk Unlocked: ${rivalCompany.subsidiaryPerkTitle}")
        } else {
            val priceStr = String.format("%.2f", rivalCompany.stockPrice)
            listOf("📈 Stock Exchange: Acquired $sharesToBuy shares of ${rivalCompany.name} (${rivalCompany.tickerSymbol}) at $$priceStr/share.")
        }
        
        val isAllConquered = newSubsidiaries.size >= RivalCatalog.ALL_RIVALS.size
        val triggerEndgame = isAllConquered && !current.isEndgameCompleted && !current.isEndgameTriggered
        
        if (triggerEndgame) {
            _showEndgameDialog.value = true
        }
        
        if (becomesSubsidiary && !alreadySubsidiary) {
            _snackBarMessage.value = "🏢 HOSTILE TAKEOVER! ${rivalCompany.name} is now your subsidiary! Perk: ${rivalCompany.subsidiaryPerkTitle}"
        } else {
            val costStr = String.format("%.2f", cost)
            _snackBarMessage.value = "📈 Purchased $sharesToBuy shares of ${rivalCompany.tickerSymbol} for $$costStr ($newSharesOwned% owned)"
        }
        
        val newCash = current.cash - cost
        val newLogs = (logsToAdd + current.dailyLogs).take(25)
        val newEndgameTriggered = current.isEndgameTriggered || triggerEndgame
        
        val nextState = current.copy(
            cash = newCash,
            dailyLogs = newLogs,
            pendingContractOffers = newPendingOffers,
            rivalSharesOwned = newRivalSharesOwned,
            subsidiaryCompanyIds = newSubsidiaries,
            isEndgameTriggered = newEndgameTriggered
        )
        checkAchievements(nextState)
    }
}

fun sellShares(rivalId: String, quantity: Int) {
    if (quantity <= 0) return
    _gameState.update { current ->
        val rivalCompany = current.rivalCompanies.find { it.id == rivalId }
            ?: RivalCatalog.getRivalById(rivalId)
            
        val sharesOwned = current.getSharesOwned(rivalId)
        if (sharesOwned <= 0) {
            _snackBarMessage.value = "You do not own any equity shares in ${rivalCompany.name}."
            return@update current
        }
        
        val sharesToSell = quantity.coerceAtMost(sharesOwned)
        val revenue = sharesToSell * rivalCompany.stockPrice
        val newSharesOwned = sharesOwned - sharesToSell
        
        val newRivalSharesOwned = current.rivalSharesOwned + (rivalId to newSharesOwned)
        
        val wasSubsidiary = current.subsidiaryCompanyIds.contains(rivalId)
        val stillSubsidiary = newSharesOwned >= 51
        
        val newSubsidiaries = if (wasSubsidiary && !stillSubsidiary) {
            current.subsidiaryCompanyIds - rivalId
        } else {
            current.subsidiaryCompanyIds
        }
        
        val logsToAdd = if (wasSubsidiary && !stillSubsidiary) {
            listOf("📉 Divestment: Sold majority control in ${rivalCompany.name}. Lost subsidiary privileges.")
        } else {
            val revenueStr = String.format("%.2f", revenue)
            listOf("📉 Stock Exchange: Liquidated $sharesToSell shares of ${rivalCompany.name} (${rivalCompany.tickerSymbol}) for +$$revenueStr.")
        }
        
        val revenueStr = String.format("%.2f", revenue)
        _snackBarMessage.value = "📉 Liquidated $sharesToSell shares of ${rivalCompany.tickerSymbol} for +$$revenueStr ($newSharesOwned% remaining)"
        
        val newCash = current.cash + revenue
        val newLogs = (logsToAdd + current.dailyLogs).take(25)
        
        val nextState = current.copy(
            cash = newCash,
            dailyLogs = newLogs,
            rivalSharesOwned = newRivalSharesOwned,
            subsidiaryCompanyIds = newSubsidiaries
        )
        checkAchievements(nextState)
    }
}

fun launchCorporateSabotage(rivalId: String) {
    _gameState.update { current ->
        val rivalCompany = current.rivalCompanies.find { it.id == rivalId } ?: return@update current
        
        val rpCost = 100
        val cashCost = 5000.0
        
        if (current.researchPoints < rpCost || current.cash < cashCost) {
            val costStr = String.format("%.2f", cashCost)
            _snackBarMessage.value = "Insufficient resources! Requires $rpCost RP & $$costStr cash."
            return@update current
        }
        
        val playerRoll = current.playerOffenseRating + Random.nextInt(1, 20)
        val rivalRoll = rivalCompany.defenseRating + Random.nextInt(1, 20)
        
        val repLoss: Int
        val modifiedRival: RivalCompany
        
        if (playerRoll > rivalRoll) {
            val damage = rivalCompany.netWorth * 0.15
            val newNetWorth = (rivalCompany.netWorth - damage).coerceAtLeast(0.0)
            repLoss = 0
            modifiedRival = rivalCompany.copy(
                netWorth = newNetWorth,
                hostilityToPlayer = rivalCompany.hostilityToPlayer + 10
            )
            _snackBarMessage.value = "✅ CYBER OFFENSE SUCCESS! ${rivalCompany.name} suffered massive financial damage and B2B lockout."
        } else {
            repLoss = 15
            modifiedRival = rivalCompany.copy(
                hostilityToPlayer = rivalCompany.hostilityToPlayer + 25
            )
            _snackBarMessage.value = "❌ SABOTAGE FAILED! Traced back to your IP. Reputation tanked."
        }
        
        val newRivals = current.rivalCompanies.map { if (it.id == rivalId) modifiedRival else it }
        val newCash = current.cash - cashCost
        val newRp = current.researchPoints - rpCost
        val newReputation = (current.reputation - repLoss).coerceAtLeast(0)
        
        val nextState = current.copy(
            cash = newCash,
            researchPoints = newRp,
            reputation = newReputation,
            rivalCompanies = newRivals
        )
        checkAchievements(nextState)
    }
}

fun launchSmearCampaign(rivalId: String) {
    _gameState.update { current ->
        val rivalCompany = current.rivalCompanies.find { it.id == rivalId } ?: return@update current
        
        if (current.subsidiaryCompanyIds.contains(rivalId)) {
            _snackBarMessage.value = "Cannot execute smear campaigns on your own subsidiary!"
            return@update current
        }
        
        val rpCost = 2
        val cashCost = 250.0
        
        if (current.researchPoints < rpCost || current.cash < cashCost) {
            val costStr = String.format("%.2f", cashCost)
            _snackBarMessage.value = "Smear campaign requires $rpCost RP and $$costStr cash."
            return@update current
        }
        
        val silverTongueLevel = current.playerSkills.silverTongueLevel
        val successChance = (50 + silverTongueLevel * 15).coerceAtMost(95)
        val roll = Random.nextInt(1, 101)
        val success = roll <= successChance
        
        val newCash = current.cash - cashCost
        val newRp = current.researchPoints - rpCost
        
        if (success) {
            val newStockPrice = (rivalCompany.stockPrice * 0.6 * 100.0).toInt() / 100.0
            val modifiedRival = rivalCompany.copy(
                stockPrice = newStockPrice.coerceAtLeast(10.0),
                stockChangePercent = -40.0
            )
            val newRivals = current.rivalCompanies.map { if (it.id == rivalId) modifiedRival else it }
            
            val priceStr = String.format("%.2f", newStockPrice)
            val logMsg = "📉 Media Scandal: Leaked damaging intelligence on ${rivalCompany.name}, crashing stock by -40% to $$priceStr."
            val newLogs = (listOf(logMsg) + current.dailyLogs).take(25)
            
            _snackBarMessage.value = "🎯 Smear Success ($successChance% roll)! ${rivalCompany.tickerSymbol} shares plummeted -40% to $$priceStr!"
            
            val nextState = current.copy(
                cash = newCash,
                researchPoints = newRp,
                dailyLogs = newLogs,
                rivalCompanies = newRivals
            )
            checkAchievements(nextState)
        } else {
            val newReputation = (current.reputation - 5).coerceAtLeast(0)
            val logMsg = "⚠️ PR Disaster: Defamation lawsuit from ${rivalCompany.name} exposed smear attempt! Lost -5 Reputation."
            val newLogs = (listOf(logMsg) + current.dailyLogs).take(25)
            
            _snackBarMessage.value = "⚠️ Smear Botched ($successChance% chance)! ${rivalCompany.name} countersued (-5 Rep)!"
            
            val nextState = current.copy(
                cash = newCash,
                researchPoints = newRp,
                reputation = newReputation,
                dailyLogs = newLogs
            )
            checkAchievements(nextState)
        }
    }
}

fun checkWinState(): Boolean {
    val current = _gameState.value
    if (RivalCatalog.ALL_RIVALS.isEmpty()) return false
    
    val allRivalsConquered = RivalCatalog.ALL_RIVALS.all { rival ->
        current.getSharesOwned(rival.id) >= 51 || current.subsidiaryCompanyIds.contains(rival.id)
    }
    
    if (!allRivalsConquered) return false
    if (current.isEndgameCompleted || current.isEndgameTriggered) return true
    
    _gameState.update {
        it.copy(isEndgameTriggered = true)
    }
    _showEndgameDialog.value = true
    return true
}

fun saveGame(): Boolean {
    val success = saveGameManager.saveGame(_gameState.value)
    if (success) {
        _hasSavedGame.value = true
        _saveSummary.value = saveGameManager.getSaveSummary()
        _snackBarMessage.value = "💾 Game saved successfully!"
    } else {
        _snackBarMessage.value = "⚠️ Failed to save game state."
    }
    return success
}

fun loadGame(saveId: String? = null): Boolean {
    val loadedState = saveGameManager.loadGame(saveId)
    if (loadedState != null) {
        _gameState.value = loadedState
        _showDailyReportDialog.value = false
        _showNewsChronicleDialog.value = false
        _showEndgameDialog.value = loadedState.isEndgameTriggered && !loadedState.isEndgameCompleted
        _hasSavedGame.value = true
        _saveSummary.value = saveGameManager.getSaveSummary(loadedState.saveId)
        
        val cashStr = String.format("%.2f", loadedState.cash)
        _snackBarMessage.value = "📂 Game loaded: Day ${loadedState.day} ($$cashStr)"
        checkWinState()
        return true
    } else {
        _snackBarMessage.value = "⚠️ No valid save file found."
        return false
    }
}


    fun updateEmpireName(newName: String) {
        val trimmed = newName.take(20).trim()
        if (trimmed.isNotEmpty()) {
            _gameState.update { it.copy(saveName = trimmed) }
            saveGameManager.saveGame(_gameState.value)
            _snackBarMessage.value = "Empire renamed to $trimmed"
        }
    }

    fun startNewGame() {
    val newState = GameState(saveName = generateRandomEmpireName())
    _gameState.value = newState
    _showDailyReportDialog.value = false
    _showNewsChronicleDialog.value = false
    _showEndgameDialog.value = false
    
    saveGameManager.saveGame(newState)
    _hasSavedGame.value = true
    _saveSummary.value = saveGameManager.getSaveSummary(newState.saveId)
    _snackBarMessage.value = "🌱 New Dairy Empire started!"
}

fun deleteSave(saveId: String) {
    saveGameManager.clearSaveGame(saveId)
    if (_gameState.value.saveId == saveId) {
        startNewGame()
    }
}

fun chooseEndgameOption(choice: EndgameChoice) {
    _gameState.update { current ->
        _showEndgameDialog.value = false
        val newLogs: List<String>
        val nextState: GameState
        
        when (choice) {
            EndgameChoice.SUBSIDIZE_FOR_THE_PEOPLE -> {
                val logMsg = "🌾 THE GOLDEN PITCHFORK: You chose agrarian benevolence. All consumer prices slashed by 80% to feed the world."
                _snackBarMessage.value = "🌾 The Golden Pitchfork: Dairy is now subsidized for all humanity!"
                newLogs = (listOf(logMsg) + current.dailyLogs).take(25)
                nextState = current.copy(
                    reputation = 100,
                    dailyLogs = newLogs,
                    isEndgameCompleted = true,
                    endgameChoice = choice,
                    endgamePriceMultiplier = choice.priceMultiplier
                )
            }
            EndgameChoice.MAXIMIZE_SHAREHOLDER_VALUE -> {
                val logMsg = "💎 THE DIAMOND COWBELL: You chose unbridled monopoly capitalism! All product prices increased by +300%."
                _snackBarMessage.value = "💎 The Diamond Cowbell: Total corporate monopoly achieved (+300% prices)!"
                newLogs = (listOf(logMsg) + current.dailyLogs).take(25)
                nextState = current.copy(
                    dailyLogs = newLogs,
                    isEndgameCompleted = true,
                    endgameChoice = choice,
                    endgamePriceMultiplier = choice.priceMultiplier
                )
            }
        }
        checkAchievements(nextState)
    }
    saveGame()
}

fun markFeatureAsSeen(destination: DrawerDestination) {
    _gameState.update { current ->
        when (destination) {
            DrawerDestination.EXECUTIVE_BOARDROOM -> {
                if (current.unlockedFeatures.isBoardroomNew) {
                    current.copy(unlockedFeatures = current.unlockedFeatures.copy(isBoardroomNew = false))
                } else current
            }
            DrawerDestination.STOCK_MARKET -> {
                if (current.unlockedFeatures.isStockMarketNew) {
                    current.copy(unlockedFeatures = current.unlockedFeatures.copy(isStockMarketNew = false))
                } else current
            }
            else -> current
        }
    }
}

fun discardSpoiledGoods() {
    _gameState.update { current ->
        val spoiledGoods = current.inventory.filter { it.isSpoiled || it.itemId == ProductCatalog.SPOILED_MILK.id }
        val totalSpoiled = spoiledGoods.sumOf { it.quantity }
        
        if (totalSpoiled == 0) {
            _snackBarMessage.value = "No spoiled goods found in the warehouse."
            return@update current
        }
        
        _snackBarMessage.value = "Dumped $totalSpoiled units of spoiled goods to clear warehouse space."
        
        val newInventory = current.inventory.filterNot { it.isSpoiled || it.itemId == ProductCatalog.SPOILED_MILK.id }
        current.copy(inventory = newInventory)
    }
    saveGame()
}

fun restartGame() {
    startNewGame()
}

fun goCorporate() {
    val current = _gameState.value
    val backupId = UUID.randomUUID().toString()
    val backupState = current.copy(
        saveId = backupId,
        saveName = "Farm_Backup",
        gamePhase = GamePhase.SANDBOX
    )
    saveGameManager.saveGame(backupState)
    
    _gameState.update {
        it.copy(
            saveName = "Corp_${it.saveName}",
            gamePhase = GamePhase.CORPORATE
        )
    }
    _showMilestoneScreen.value = false
    saveGame()
}

fun startSandbox() {
    _gameState.update { current ->
        current.copy(gamePhase = GamePhase.SANDBOX)
    }
    _showMilestoneScreen.value = false
    saveGame()
}

fun retireSave() {
    _gameState.update { current ->
        current.copy(gamePhase = GamePhase.COMPLETED)
    }
    _showMilestoneScreen.value = false
    saveGame()
}

fun updateColdStoragePriority(priority: ColdStoragePriority) {
    _gameState.update { current ->
        current.copy(coldStoragePriority = priority)
    }
    saveGame()
}

fun updateManualColdStorageAllocation(itemId: String, allocation: Int) {
    _gameState.update { current ->
        val newAllocations = current.manualColdStorageAllocations.toMutableMap()
        if (allocation <= 0) {
            newAllocations.remove(itemId)
        } else {
            newAllocations[itemId] = allocation
        }
        current.copy(manualColdStorageAllocations = newAllocations)
    }
    saveGame()
}

private fun applyResearchEffect(state: GameState, techId: String): GameState {
    val node = ResearchCatalog.ALL_NODES.find { it.id == techId } ?: return state
    
    val newStatuses = state.researchNodeStatuses.toMutableMap()
    newStatuses[techId] = NodeStatus.COMPLETED
    
    return state.copy(researchNodeStatuses = newStatuses)
}

    fun resolveMissedDelivery(payPenalty: Boolean) {
        val currentState = _gameState.value
        val contractId = currentState.missedDeliveryEvent ?: return
        val contract = currentState.activeContracts.find { it.id == contractId } ?: return
        
        _gameState.update { state ->
            if (payPenalty) {
                state.copy(
                    cash = state.cash - contract.cashPenaltyPerMiss,
                    missedDeliveryEvent = null
                )
            } else {
                val updatedContract = contract.copy(
                    strikes = contract.strikes + 1,
                    payoutAmount = (contract.payoutAmount - contract.rewardReductionPerStrike).coerceAtLeast(0.0)
                )
                // If it hits 3 strikes, we could cancel it, but for now just take the strike.
                state.copy(
                    reputation = (state.reputation - 5).coerceAtLeast(0),
                    activeContracts = state.activeContracts.map { if (it.id == contractId) updatedContract else it },
                    missedDeliveryEvent = null
                )
            }
        }
    }


    private fun processCorporateWarfare(state: com.example.model.GameState, currentDay: Int, logs: MutableList<String>): com.example.model.GameState {
        if (state.gamePhase != com.example.model.GamePhase.CORPORATE) return state
        
        var newState = state
        var cash = state.cash
        var reputation = state.reputation
        val activeAttacks = state.pendingCyberAttacks.filter { it.executionDay <= currentDay }
        val futureAttacks = state.pendingCyberAttacks.filter { it.executionDay > currentDay }.toMutableList()
        val updatedBuildings = state.buildings.toMutableList()
        var updatedRivals = state.rivalCompanies
        var newAttacksThwarted = state.attacksThwarted
        var newFraudLosses = state.fraudLosses
        var newDdosDays = state.ddosDowntimeDays
        var newHostileTakeovers = state.hostileTakeovers
        
        for (attack in activeAttacks) {
            if (attack.attackerId == "PLAYER") {
                val target = updatedRivals.find { it.id == attack.targetId } ?: continue
                val successChance = state.offenseRating.toFloat() / (state.offenseRating + target.defenseRating).coerceAtLeast(1)
                val isSuccess = kotlin.random.Random.nextFloat() <= successChance
                
                if (isSuccess) {
                    logs.add("🚀 Cyber Attack SUCCESS against ${target.name}! [${attack.attackType}]")
                    when (attack.attackType) {
                        AttackType.FINANCIAL_PHISHING -> {
                            val stolen = target.netWorth * 0.05
                            cash += stolen
                            val newTarget = target.copy(netWorth = target.netWorth - stolen)
                            updatedRivals = updatedRivals.map { if (it.id == newTarget.id) newTarget else it }
                        }
                        AttackType.LOGISTICS_HIJACK -> {
                            cash += 25000.0
                        }
                        AttackType.DDOS_FACILITY -> {
                            // Abstract drop in defense
                            val newTarget = target.copy(defenseRating = (target.defenseRating - 5).coerceAtLeast(1))
                            updatedRivals = updatedRivals.map { if (it.id == newTarget.id) newTarget else it }
                        }
                        AttackType.HOSTILE_BUYOUT -> {
                            logs.add("💼 HOSTILE BUYOUT SUCCESS: Forced a board vote in ${target.name}.")
                        }
                        else -> {}
                    }
                } else {
                    logs.add("❌ Cyber Attack FAILED against ${target.name}. They traced it back to us (-5 Reputation).")
                    reputation = (reputation - 5).coerceAtLeast(0)
                }
            } else {
                val attacker = updatedRivals.find { it.id == attack.attackerId } ?: continue
                val successChance = attacker.offenseRating.toFloat() / (attacker.offenseRating + state.defenseRating).coerceAtLeast(1)
                val isSuccess = kotlin.random.Random.nextFloat() <= successChance
                
                if (isSuccess) {
                    logs.add("⚠️ SECURITY BREACH! ${attacker.name} successfully executed a ${attack.attackType} attack against us!")
                    when (attack.attackType) {
                        com.example.model.AttackType.FINANCIAL_PHISHING -> {
                            val stolen = cash * (kotlin.random.Random.nextInt(1, 6) / 100.0)
                            cash -= stolen
                            newFraudLosses += stolen
                        }
                        com.example.model.AttackType.LOGISTICS_HIJACK -> {
                            cash -= 15000.0
                            newFraudLosses += 15000.0
                        }
                        com.example.model.AttackType.DDOS_FACILITY -> {
                            val factoryIdx = updatedBuildings.indexOfFirst { it.isOperational && it.type != com.example.model.BuildingType.PASTURE }
                            if (factoryIdx != -1) {
                                updatedBuildings[factoryIdx] = updatedBuildings[factoryIdx].copy(sabotagedDaysRemaining = 3)
                                newDdosDays += 3
                            }
                        }
                        com.example.model.AttackType.HOSTILE_BUYOUT -> {
                            if (newState.playerMarketShare > 15.0f) {
                                newState = newState.copy(playerMarketShare = newState.playerMarketShare - 5.0f)
                                updatedRivals = updatedRivals.map { if (it.id == attacker.id) it.copy(marketShare = it.marketShare + 5.0f) else it }
                                logs.add("🚨 HOSTILE BUYOUT: ${attacker.name} manipulated the market and stole 5% of your Market Share!")
                            } else {
                                cash -= 25000.0
                                logs.add("🚨 FAILED BUYOUT: ${attacker.name} attempted a buyout, costing you $25k in legal fees to defend!")
                            }
                        }
                        else -> {}
                    }
                } else {
                    logs.add("🛡️ Cyber Defense SUCCESS! We blocked an attack from ${attacker.name}.")
                    newAttacksThwarted++
                }
            }
        }
        
        // AI queues new attacks
        for (rival in updatedRivals) {
            if (rival.threatLevel > 0 && kotlin.random.Random.nextInt(100) < rival.threatLevel) {
                val type = AttackType.values().random()
                futureAttacks.add(PendingCyberAttack(
                    attackerId = rival.id,
                    targetId = "PLAYER",
                    attackType = type,
                    executionDay = currentDay + kotlin.random.Random.nextInt(2, 5)
                ))
            }
        }
        
        // Early Warning System
        val hasScamDetection = state.researchNodeStatuses["tech_scam_detection"] == NodeStatus.COMPLETED
        var warningEvent: PendingCyberAttack? = null
        if (hasScamDetection) {
            warningEvent = futureAttacks.find { it.targetId == "PLAYER" && it.executionDay == currentDay + 1 }
        }
        

        // --- HOSTILE TAKEOVER CHECK ---
        val rivalsToEliminate = mutableListOf<String>()
        var additionalPlayerMarketShare = 0f
        var liquidationBonus = 0.0
        
        for (rival in updatedRivals) {
            val ownedShares = newState.getSharesOwned(rival.id)
            if (ownedShares > 50) {
                rivalsToEliminate.add(rival.id)
                additionalPlayerMarketShare += rival.marketShare
                liquidationBonus += rival.netWorth * 0.20 // 20% bonus
                newHostileTakeovers++
                logs.add("🚨 HOSTILE TAKEOVER COMPLETE: Acquired controlling stake in ${rival.name}! Operations liquidated.")
            }
        }
        
        if (rivalsToEliminate.isNotEmpty()) {
            updatedRivals = updatedRivals.filter { it.id !in rivalsToEliminate }
            cash += liquidationBonus
            newState = newState.copy(playerMarketShare = newState.playerMarketShare + additionalPlayerMarketShare)
        }
        
        // --- VICTORY CONDITIONS ---
        if (newState.playerMarketShare >= 100.0f && newState.gamePhase != com.example.model.GamePhase.COMPLETED) {
            newState = newState.copy(
                gamePhase = com.example.model.GamePhase.COMPLETED,
                gameOverReason = "MONOPOLIST"
            )
        }
        
        if (newState.researchNodeStatuses["tech_singularity"] == NodeStatus.COMPLETED && newState.gamePhase != com.example.model.GamePhase.COMPLETED) {
            newState = newState.copy(
                gamePhase = com.example.model.GamePhase.COMPLETED,
                gameOverReason = "TECH_SINGULARITY"
            )
        }

        return newState.copy(
            cash = cash,
            reputation = reputation,
            pendingCyberAttacks = futureAttacks,
            buildings = updatedBuildings,
            cyberAttackWarningEvent = warningEvent,
            rivalCompanies = updatedRivals,
            attacksThwarted = newAttacksThwarted,
            fraudLosses = newFraudLosses,
            ddosDowntimeDays = newDdosDays,
            hostileTakeovers = newHostileTakeovers
        )
    }


    fun queuePlayerCyberAttack(rivalId: String, type: AttackType, cost: Double, apCost: Int) {
        val currentState = _gameState.value
        if (currentState.cash < cost || currentState.dailyActionsRemaining < apCost) return
        
        _gameState.update { state ->
            val newAttacks = state.pendingCyberAttacks.toMutableList()
            newAttacks.add(PendingCyberAttack(
                attackerId = "PLAYER",
                targetId = rivalId,
                attackType = type,
                executionDay = state.day + 2
            ))
            state.copy(
                cash = state.cash - cost,
                dailyActionsRemaining = state.dailyActionsRemaining - apCost,
                pendingCyberAttacks = newAttacks
            )
        }
        _snackBarMessage.value = "Cyber attack deployed against target!"
    }
    
    fun resolveCyberAttackWarning(boostDefense: Boolean) {
        val currentState = _gameState.value
        val warningEvent = currentState.cyberAttackWarningEvent ?: return
        
        _gameState.update { state ->
            if (boostDefense) {
                if (state.cash >= 15000.0) {
                    state.copy(
                        cash = state.cash - 15000.0,
                        defenseRating = state.defenseRating + 25, // Temporary huge boost
                        cyberAttackWarningEvent = null
                    )
                } else state
            } else {
                state.copy(cyberAttackWarningEvent = null)
            }
        }
    }


    fun continueInSandbox() {
        _gameState.update { state ->
            state.copy(
                gamePhase = com.example.model.GamePhase.SANDBOX,
                isGameOver = false,
                gameOverReason = null,
                rivalCompanies = emptyList(), // Remove AI rivals
                pendingCyberAttacks = emptyList(),
                cyberAttackWarningEvent = null
            )
        }
    }


    private fun processSubsidiaries(state: com.example.model.GameState, logs: MutableList<String>): com.example.model.GameState {
        if (state.spunOffSubsidiaries.isEmpty()) return state
        
        var currentCash = state.cash
        val updatedSubsidiaries = mutableListOf<com.example.model.SpunOffSubsidiary>()
        var totalDividend = 0.0
        
        for (sub in state.spunOffSubsidiaries) {
            val facility = state.buildings.find { it.id == sub.parentFacilityId }
            if (facility == null) {
                updatedSubsidiaries.add(sub)
                continue
            }
            
            val recipe = facility.activeRecipe
            if (recipe == null) {
                updatedSubsidiaries.add(sub)
                continue
            }
            
            // Calculate theoretical daily yield and revenue
            val outputVolume = facility.currentProcessingCapacity * recipe.outputQuantity
            val outputProduct = com.example.model.ProductCatalog.getById(recipe.outputItemId)
            val marketPrice = state.marketPrices[outputProduct.id]?.basePrice ?: outputProduct.basePrice
            val repBonus = 1.0 + (state.reputation / 100.0)
            val grossRevenue = outputVolume * marketPrice * sub.qualityMultiplier * repBonus
            
            // Calculate theoretical costs
            val inputProduct = com.example.model.ProductCatalog.getById(recipe.inputItemId)
            val inputMarketPrice = state.marketPrices[inputProduct.id]?.basePrice ?: inputProduct.basePrice
            val inputCosts = (facility.currentProcessingCapacity * recipe.inputQuantity) * inputMarketPrice
            
            val netProfit = (grossRevenue - inputCosts - facility.currentMaintenance).coerceAtLeast(0.0)
            
            val dividend = netProfit * sub.dividendSlider
            val retained = netProfit - dividend
            
            currentCash += dividend
            totalDividend += dividend
            
            var newCapital = sub.internalCapital + retained
            var newQuality = sub.qualityMultiplier
            var upgrades = 0
            
            while (newCapital >= 50000.0) {
                newCapital -= 50000.0
                newQuality += 0.1f
                upgrades++
            }
            
            if (upgrades > 0) {
                logs.add("📈 ${sub.customName} auto-reinvested capital! Quality Multiplier is now ${String.format("%.1f", newQuality)}x")
            }
            if (dividend > 0) {
                logs.add("🏢 ${sub.customName} distributed $${String.format("%,.0f", dividend)} in dividends today.")
            }
            
            updatedSubsidiaries.add(sub.copy(
                internalCapital = newCapital,
                qualityMultiplier = newQuality
            ))
        }
        
        return state.copy(
            cash = currentCash,
            spunOffSubsidiaries = updatedSubsidiaries
        )
    }
    
    fun spinOffFacility(facilityId: String, customName: String) {
        _gameState.update { state ->
            val facility = state.buildings.find { it.id == facilityId } ?: return@update state
            if (facility.isSpunOff) return@update state
            
            val updatedBuildings = state.buildings.map {
                if (it.id == facilityId) it.copy(isSpunOff = true) else it
            }
            
            val newSub = com.example.model.SpunOffSubsidiary(
                customName = customName,
                parentFacilityId = facilityId
            )
            
            _snackBarMessage.value = "Spun off $customName as an autonomous subsidiary!"
            
            state.copy(
                buildings = updatedBuildings,
                spunOffSubsidiaries = state.spunOffSubsidiaries + newSub
            )
        }
    }
    
    fun updateSubsidiaryDividend(subsidiaryId: String, newSliderValue: Float) {
        _gameState.update { state ->
            val updatedSubs = state.spunOffSubsidiaries.map {
                if (it.id == subsidiaryId) it.copy(dividendSlider = newSliderValue) else it
            }
            state.copy(spunOffSubsidiaries = updatedSubs)
        }
    }


    fun resolveNewCrisisAlert() {
        _gameState.update { it.copy(newCrisisFired = null) }
    }


    fun buyFacilityPerk(facilityId: String, perkId: String) {
        _gameState.update { state ->
            val facility = state.buildings.find { it.id == facilityId } ?: return@update state
            val perk = com.example.model.FacilityPerkCatalog.PERKS.find { it.id == perkId } ?: return@update state
            
            if (state.cash < perk.cost) {
                _snackBarMessage.value = "Insufficient funds for ${perk.title}."
                return@update state
            }
            if (facility.level < perk.requiredFacilityLevel) {
                _snackBarMessage.value = "Facility level too low."
                return@update state
            }
            if (perk.mutuallyExclusiveWith.any { it in facility.unlockedPerks }) {
                _snackBarMessage.value = "Mutually exclusive perk already unlocked."
                return@update state
            }
            if (perkId in facility.unlockedPerks) return@update state
            
            val updatedBuildings = state.buildings.map {
                if (it.id == facilityId) it.copy(unlockedPerks = it.unlockedPerks + perkId) else it
            }
            
            _snackBarMessage.value = "Unlocked ${perk.title} for ${facility.name}!"
            state.copy(
                cash = state.cash - perk.cost,
                buildings = updatedBuildings
            )
        }
    }


    fun advanceAweTutorial() {
        _gameState.update { it.copy(showAweDialogue = false, showAweHighlight = true) }
    }
    
    fun dismissAweHighlight() {
        _gameState.update { it.copy(showAweHighlight = false) }
    }


    fun unlockAnomalyBlueprint(blueprintId: String) {
        _gameState.update { state ->
            val blueprint = com.example.model.AnomalyCatalog.BLUEPRINTS.find { it.id == blueprintId } ?: return@update state
            if (state.researchPoints < blueprint.researchCostRp) {
                _snackBarMessage.value = "Insufficient Research Points."
                return@update state
            }
            if (blueprint.prerequisites.any { it !in state.unlockedAnomalies }) {
                _snackBarMessage.value = "Prerequisites not met."
                return@update state
            }
            if (blueprintId in state.unlockedAnomalies) return@update state
            
            _snackBarMessage.value = "Unlocked Experimental Blueprint: ${blueprint.title}!"
            state.copy(
                researchPoints = state.researchPoints - blueprint.researchCostRp,
                unlockedAnomalies = state.unlockedAnomalies + blueprintId
            )
        }
    }
    
    fun installAnomaly(facilityId: String, blueprintId: String) {
        _gameState.update { state ->
            val facility = state.buildings.find { it.id == facilityId } ?: return@update state
            val blueprint = com.example.model.AnomalyCatalog.BLUEPRINTS.find { it.id == blueprintId } ?: return@update state
            
            if (facility.level < facility.maxLevel) {
                _snackBarMessage.value = "Facility must be Max Level."
                return@update state
            }
            if (state.cash < blueprint.installCostCash) {
                _snackBarMessage.value = "Insufficient Cash to install Anomaly."
                return@update state
            }
            if (blueprintId !in state.unlockedAnomalies) return@update state
            
            val updatedBuildings = state.buildings.map {
                if (it.id == facilityId) it.copy(activeAnomalyId = blueprintId) else it
            }
            
            _snackBarMessage.value = "Installed ${blueprint.title} in ${facility.name}!"
            state.copy(
                cash = state.cash - blueprint.installCostCash,
                buildings = updatedBuildings
            )
        }
    }


    private fun evaluateStoryEvents(state: com.example.model.GameState): com.example.model.GameState {
        var newState = state
        val newPending = state.pendingStoryEvents.toMutableList()
        var addedAny = false
        
        // Helper to check and add
        fun triggerEvent(eventId: String) {
            if (!newState.readStoryEvents.contains(eventId) && !newPending.any { it.id == eventId }) {
                com.example.model.StoryCatalog.ALL_EVENTS.find { it.id == eventId }?.let {
                    newPending.add(it)
                    addedAny = true
                }
            }
        }
        
        // 1. Farming Beat (Always triggers early, say day >= 1)
        if (state.day >= 1) triggerEvent("event_farming_1")
        
        // 2. Corporate Beat (Net Worth > 100k)
        if (state.netWorth >= 100000.0) triggerEvent("event_corporate_1")
        
        // 3. Experimental Beat (Advanced Lab built)
        val hasAdvLab = state.buildings.any { it.type == com.example.model.BuildingType.ADVANCED_LAB && it.isConstructed }
        if (hasAdvLab) triggerEvent("event_exp_1")
        
        if (addedAny) {
            newState = newState.copy(pendingStoryEvents = newPending)
        }
        return newState
    }
    
    fun markStoryEventAsRead(eventId: String) {
        _gameState.update { state ->
            state.copy(
                pendingStoryEvents = state.pendingStoryEvents.filter { it.id != eventId },
                readStoryEvents = state.readStoryEvents + eventId
            )
        }
    }

}
