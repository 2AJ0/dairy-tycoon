import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

subsidiary_logic = """
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
"""

if "private fun processSubsidiaries" not in kt:
    kt = kt.rstrip()[:-1] + subsidiary_logic + "\n}\n"

old_war = """                newState = processCorporateWarfare(newState, currentDay, newLogs)
                
                newState
            }"""

new_war = """                newState = processCorporateWarfare(newState, currentDay, newLogs)
                newState = processSubsidiaries(newState, newLogs)
                
                newState
            }"""

kt = kt.replace(old_war, new_war)

with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(kt)
