import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

# Hook crisis logic before pricing update
crisis_logic = """
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
                    newLogs.add("🚨 CRISIS ALERT: ${newCrisis.title}")
                }
"""

old_market = """                // 6. Dynamic Market Pricing Update
                val newMarketPrices = currentState.marketPrices.mapValues { (productId, marketState) ->
                    val product = ProductCatalog.getById(productId)
                    val base = product.basePrice
                    val soldYesterday = (currentState.todaySoldUnits[productId] ?: 0) + (autoSoldUnits[productId] ?: 0)
                    
                    val rng = Random.nextDouble(0.85, 1.25)
                    val repBonus = 1.0 + currentState.reputation * 0.003 * currentState.playerSkills.silverTongueRepBonusMultiplier
                    val supplyPenalty = (1.0 - soldYesterday * 0.01).coerceIn(0.6, 1.0)
                    
                    val eventMult = if (activeNews != null && (activeNews.targetProductId == null || activeNews.targetProductId == productId)) activeNews.multiplier else 1.0
                    
                    var calculatedPrice = base * rng * repBonus * supplyPenalty * eventMult"""

new_market = crisis_logic + """
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
                    
                    var calculatedPrice = base * rng * repBonus * supplyPenalty * eventMult * crisisMult"""

kt = kt.replace(old_market, new_market)

# Now inject state updates for activeCrises and newCrisisFired
old_state = """                    activeNewsEvent = activeNews,
                    inventory = finalInventory,"""

new_state = """                    activeNewsEvent = activeNews,
                    activeCrises = updatedCrises,
                    newCrisisFired = currentState.newCrisisFired ?: newCrisisFired,
                    inventory = finalInventory,"""

kt = kt.replace(old_state, new_state)

# Also update processCorporateWarfare
old_warfare = """            if (threatLevel > 40 && kotlin.random.Random.nextDouble() < 0.20 && activeAttacksForRival < 2) {
                // Determine attack type and execute day
                val attackTypes = AttackType.values()
                val selectedType = attackTypes[kotlin.random.Random.nextInt(attackTypes.size)]
                
                // Add to queue
                val executionDay = currentDay + kotlin.random.Random.nextInt(1, 4)"""

new_warfare = """            val attackChance = when (rival.tier) {
                com.example.model.RivalTier.LOCAL -> 0.05
                com.example.model.RivalTier.SPECIALIZED -> 0.35
                com.example.model.RivalTier.GLOBAL -> 0.25
            }
            if (threatLevel > 20 && kotlin.random.Random.nextDouble() < attackChance && activeAttacksForRival < 2) {
                // Determine attack type based on Tier
                val selectedType = when (rival.tier) {
                    com.example.model.RivalTier.LOCAL -> com.example.model.AttackType.LOGISTICS_HIJACK
                    com.example.model.RivalTier.SPECIALIZED -> if (kotlin.random.Random.nextBoolean()) com.example.model.AttackType.FINANCIAL_PHISHING else com.example.model.AttackType.LOGISTICS_HIJACK
                    com.example.model.RivalTier.GLOBAL -> if (kotlin.random.Random.nextDouble() < 0.3) com.example.model.AttackType.HOSTILE_BUYOUT else com.example.model.AttackType.DDOS_FACILITY
                }
                
                // Add to queue
                val executionDay = currentDay + kotlin.random.Random.nextInt(1, 4)"""

kt = kt.replace(old_warfare, new_warfare)

old_phishing = """                    when (attack.attackType) {
                        AttackType.FINANCIAL_PHISHING -> {
                            val stolen = cash * (kotlin.random.Random.nextInt(1, 6) / 100.0)
                            cash -= stolen
                            newFraudLosses += stolen
                        }
                        AttackType.LOGISTICS_HIJACK -> {
                            cash -= 15000.0
                            newFraudLosses += 15000.0
                        }
                        AttackType.DDOS_FACILITY -> {
                            val factoryIdx = updatedBuildings.indexOfFirst { it.isOperational && it.type != com.example.model.BuildingType.PASTURE }
                            if (factoryIdx != -1) {
                                updatedBuildings[factoryIdx] = updatedBuildings[factoryIdx].copy(sabotagedDaysRemaining = 3)
                                newDdosDays += 3
                            }
                        }
                    }"""

new_phishing = """                    when (attack.attackType) {
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
                    }"""

kt = kt.replace(old_phishing, new_phishing)

with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(kt)
