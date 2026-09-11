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
            daySent = currentDay,
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
            IntentParser.IntentType.TOPIC_INQUIRY -> {
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
            IntentParser.IntentType.GIBBERISH -> {
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
                id = null,
                text = responseText,
                isFromPlayer = false,
                daySent = current.day
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
            daySent = currentState.day,
            status = MessageStatus.READ
        )
        val msg2 = ChatMessage(
            text = "I received your gift. Let's start fresh. But don't waste my time again.",
            isFromPlayer = false,
            daySent = currentState.day
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
        
        val executedContract = offer.copy(isActive = true)
        
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
            SkillType.NEGOTIATION -> current.playerSkills.copy(negotiationLevel = newLevel)
            SkillType.MARKETING -> current.playerSkills.copy(marketingLevel = newLevel)
            SkillType.AUTOMATION -> current.playerSkills.copy(automationLevel = newLevel)
            SkillType.EFFICIENCY -> current.playerSkills.copy(efficiencyLevel = newLevel)
            SkillType.SILVER_TONGUE -> current.playerSkills.copy(silverTongueLevel = newLevel)
            SkillType.HUSTLER -> current.playerSkills.copy(hustlerLevel = newLevel)
            SkillType.STAMINA -> current.playerSkills.copy(staminaLevel = newLevel)
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
    _gameState.update { current ->
        if (current.researchNodeStatuses[techId] == NodeStatus.COMPLETED) {
            _snackBarMessage.value = "Technology is already researched."
            return@update current
        }

        if (current.isTechUnderResearch(techId)) {
            _snackBarMessage.value = "R&D research is already underway for this technology!"
            return@update current
        }

        val node = ResearchCatalog.ALL_NODES.find { it.id == techId }
        if (node == null) {
            _snackBarMessage.value = "Unknown technology."
            return@update current
        }

        val missingPrereqs = node.prerequisites.filter { current.researchNodeStatuses[it] != NodeStatus.COMPLETED }
        if (missingPrereqs.isNotEmpty()) {
            val missingTitles = missingPrereqs.mapNotNull { prereqId -> 
                ResearchCatalog.ALL_NODES.find { it.id == prereqId }?.title 
            }.joinToString(", ")
            _snackBarMessage.value = "Requires prerequisite research: $missingTitles first!"
            return@update current
        }

        if (current.cash < node.researchCost) {
            val formattedCash = String.format("%.2f", current.cash)
            _snackBarMessage.value = "Insufficient Cash. Requires $${node.researchCost} (Have $$formattedCash)."
            return@update current
        }

        val newProject = ActiveProject(
            type = ProjectType.TECH_RESEARCH,
            targetId = techId,
            title = node.title,
            icon = "🔬",
            totalDurationDays = node.researchTimeDays,
            remainingDays = node.researchTimeDays,
            progress = 0.0
        )

        val assignedProject = tryAssignProject(current, newProject)
        if (assignedProject == null) {
            _snackBarMessage.value = "All Slots & Labs Busy! Upgrade Multi-Tasking or build Labs."
            return@update current
        }

        _snackBarMessage.value = "Initiated R&D on ${node.title}! (${node.researchTimeDays} days to finish)."

        val updatedStatuses = current.researchNodeStatuses.toMutableMap()
        updatedStatuses[techId] = NodeStatus.RESEARCHING

        checkAchievements(current.copy(
            cash = current.cash - node.researchCost,
            activeProjects = current.activeProjects + assignedProject,
            researchNodeStatuses = updatedStatuses
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
            baseRushCost = 120.0
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
            baseRushCost = 150.0
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
            ProjectType.RESEARCH -> {
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
                    b.copy(productionAllocation = percentage.coerceIn(0, 100))
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
                stockTrend = -40.0
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

fun startNewGame() {
    val newState = GameState()
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
                    priceMultiplier = choice.priceMultiplier
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
                    priceMultiplier = choice.priceMultiplier
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
