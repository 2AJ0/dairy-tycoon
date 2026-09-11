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
                        currentCash += contract.payoutAmount
                        contractsRevenueToday += contract.payoutAmount
                        val nextDaysRemaining = contract.daysRemaining - 1
                        val newFulfilledDays = contract.fulfilledDays + 1
                        val newTotalPaidOut = contract.totalPaidOut + contract.payoutAmount
                        
                        notes.add("🤝 B2B Delivery: Supplied ${dailyQuota}x ${targetProduct.name} to ${rival.name} (+$${String.format("%.2f", contract.payoutAmount)}).")
                        
                        if (nextDaysRemaining <= 0) {
                            completedContractsDelta++
                            notes.add("🎉 Contract Complete: Fulfilled all terms with ${rival.name}!")
                        } else {
                            updatedActiveContracts.add(contract.copy(daysRemaining = nextDaysRemaining, fulfilledDays = newFulfilledDays, totalPaidOut = newTotalPaidOut))
                        }
                    } else {
                        currentCash -= contract.penaltyAmount
                        val nextDaysRemaining = contract.daysRemaining - 1
                        val newFailedDays = contract.failedDays + 1
                        
                        notes.add("⚠️ Contract Default: Shortfall on ${dailyQuota}x ${targetProduct.name} for ${rival.name}! Fined -$${String.format("%.2f", contract.penaltyAmount)}.")
                        
                        if (nextDaysRemaining <= 0) {
                            completedContractsDelta++
                            notes.add("❌ Contract Expired: Defaulted on contract term with ${rival.name}.")
                        } else {
                            updatedActiveContracts.add(contract.copy(daysRemaining = nextDaysRemaining, failedDays = newFailedDays))
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
                                finalInventory.add(batch.copy(id = UUID.randomUUID().toString(), quantity = exposedQty, daysUntilSpoiled = batch.daysUntilSpoiled - 1.0f, isInColdStorage = false))
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
                                finalInventory.add(batch.copy(id = UUID.randomUUID().toString(), quantity = exposedQty, daysUntilSpoiled = batch.daysUntilSpoiled - 1.0f, isInColdStorage = false))
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

                // 6. Dynamic Market Pricing Update
                val newMarketPrices = currentState.marketPrices.mapValues { (productId, marketState) ->
                    val product = ProductCatalog.getById(productId)
                    val base = product.basePrice
                    val soldYesterday = (currentState.todaySoldUnits[productId] ?: 0) + (autoSoldUnits[productId] ?: 0)
                    
                    val rng = Random.nextDouble(0.85, 1.25)
                    val repBonus = 1.0 + currentState.reputation * 0.003 * currentState.playerSkills.silverTongueRepBonusMultiplier
                    val supplyPenalty = (1.0 - soldYesterday * 0.01).coerceIn(0.6, 1.0)
                    
                    val eventMult = if (activeNews != null && (activeNews.targetProductId == null || activeNews.targetProductId == productId)) activeNews.multiplier else 1.0
                    
                    var calculatedPrice = base * rng * repBonus * supplyPenalty * eventMult
                    calculatedPrice = (calculatedPrice * 100.0).toInt() / 100.0
                    calculatedPrice = maxOf(calculatedPrice, 0.1)
                    
                    val oldPrice = marketState.currentPrice
                    val changePercent = if (oldPrice > 0.0) ((calculatedPrice - oldPrice) / oldPrice) * 100.0 else 0.0
                    
                    val history = (marketState.priceHistory + calculatedPrice).takeLast(10)
                    
                    marketState.copy(
                        currentPrice = calculatedPrice,
                        unitsSoldToday = soldYesterday,
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
                        recentStockChangePercent = (stockChangePct * 10.0).toInt() / 10.0
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
                    rawUnitsProduced = rawProducedUnits,
                    processedUnitsProduced = processedUnitsProduced,
                    spoiledUnits = spoiledCount,
                    maintenanceCost = maintenanceCost + operatingCostFromProcessing,
                    feedCost = feedCost,
                    interestCharge = interestCharge,
                    researchPointsGained = researchPointsGained,
                    salesRevenue = automatedSalesRevenue + autoSellRevenueToday,
                    foreclosureSalesCount = foreclosureSalesCount,
                    dividendsEarned = totalDividendsToday,
                    activeNews = activeNews,
                    notes = notes
                )
                
                val updatedReputation2 = (currentState.reputation + repGainedFromNews).coerceIn(0, 100)
                
                val newLogEntry = "Day $nextDay begun. Total Inventory: ${finalInventory.sumOf { it.quantity }} units. Cash: $${String.format("%.2f", currentCash)}"
                val newLogs = listOf(newLogEntry) + currentState.dailyLogs.take(19)
                
                val newPhase = if (currentState.netWorthPhase == NetWorthPhase.STARTUP && estimatedTotalNetWorth >= 50000.0) NetWorthPhase.TENSION else currentState.netWorthPhase
                
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
                    cash = currentCash,
                    reputation = updatedReputation2,
                    researchPoints = currentState.researchPoints + researchPointsGained,
                    unlockedTechIds = effectiveUnlockedTechs,
                    activeProjects = remainingActiveProjects,
                    stats = updatedStats,
                    activeNewsEvent = activeNews,
                    inventory = finalInventory,
                    buildings = effectiveBuildings,
                    marketPrices = newMarketPrices,
                    bank = currentState.bank.copy(
                        totalDebt = (bankDebt * 100.0).toInt() / 100.0,
                        daysInDebt = daysInDebt,
                        investedFunds = (newInvestedFunds * 100.0).toInt() / 100.0
                    ),
                    todaySoldUnits = emptyMap(),
                    dailyLogs = newLogs,
                    dailyReport = report,
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
                    isB2BUnlocked = b2bUnlocked,
                    unlockedFeatures = currentState.unlockedFeatures.copy(
                        isBoardroomUnlocked = shouldUnlockBoardroom,
                        isStockMarketUnlocked = shouldUnlockStockMarket,
                        isBoardroomNew = isNewBoardroom,
                        isStockMarketNew = isNewStockMarket
                    )
                )
                
                var newState = checkAchievements(finalState)
                for (id in newlyCompletedTechIds) {
                    newState = applyResearchEffect(newState, id)
                }
                
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
