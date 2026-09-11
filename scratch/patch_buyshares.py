import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

old_buy = """fun buyShares(rivalId: String, quantity: Int) {
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
            _snackBarMessage.value = "Bought $sharesToBuy shares in ${rivalCompany.name}."
        }
        
        current.copy(
            cash = current.cash - cost,
            rivalSharesOwned = newRivalSharesOwned,
            subsidiaryCompanyIds = newSubsidiaries,
            pendingContractOffers = newPendingOffers,
            dailyLogs = current.dailyLogs + logsToAdd,
            isEndgameTriggered = current.isEndgameTriggered || triggerEndgame
        )
    }
}"""

new_buy = """fun buyRivalShares(rivalId: String, quantity: Int) {
    if (quantity <= 0) return
    _gameState.update { current ->
        val rivalCompany = current.rivalCompanies.find { it.id == rivalId } ?: return@update current
        
        val sharesOwned = current.getSharesOwned(rivalId)
        val availableShares = (rivalCompany.totalShares - sharesOwned).coerceAtLeast(0)
        
        if (availableShares <= 0) {
            _snackBarMessage.value = "You already own all available shares!"
            return@update current
        }
        
        val sharesToBuy = quantity.coerceAtMost(availableShares)
        val costPerShare = rivalCompany.netWorth / 100.0
        val totalCost = sharesToBuy * costPerShare
        
        if (current.cash < totalCost) {
            _snackBarMessage.value = "Insufficient liquid cash to execute trade."
            return@update current
        }
        
        val newSharesOwned = sharesOwned + sharesToBuy
        val newRivalSharesOwned = current.rivalSharesOwned + (rivalId to newSharesOwned)
        
        _snackBarMessage.value = "Bought $sharesToBuy shares in ${rivalCompany.name}."
        
        current.copy(
            cash = current.cash - totalCost,
            rivalSharesOwned = newRivalSharesOwned
        )
    }
}"""

kt = kt.replace(old_buy, new_buy)

with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(kt)
