import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

# 1. Maintenance Cleanup & Local Radiative Cooling
old_maint = """val hasRadiativeCooling = currentState.researchNodeStatuses["tech_exp_1"] == com.example.model.NodeStatus.COMPLETED
                val rawMaintenance = currentState.buildings.filter { it.isConstructed && it.isOperational && !it.isSpunOff }
                    .sumOf { if (hasRadiativeCooling && it.type == com.example.model.BuildingType.COLD_STORAGE) 0.0 else it.currentMaintenance }"""
new_maint = """val rawMaintenance = currentState.buildings.filter { it.isConstructed && it.isOperational && !it.isSpunOff }
                    .sumOf { 
                        val hasRadiativeCooling = it.activeAnomalyId != null && com.example.model.AnomalyCatalog.BLUEPRINTS.find { bp -> bp.id == it.activeAnomalyId }?.effectType == com.example.model.AnomalyEffect.RADIATIVE_COOLING
                        if (hasRadiativeCooling) 0.0 else it.currentMaintenance 
                    }"""
kt = kt.replace(old_maint, new_maint)

# 2. Throughput Cleanup & Local Acoustic Levitation
old_throughput = """                    val hasAcousticLevitation = currentState.researchNodeStatuses["tech_exp_2"] == com.example.model.NodeStatus.COMPLETED
                    var throughputMult = 1.0f
                    if (hasAcousticLevitation && (building.type == com.example.model.BuildingType.PASTEURIZER || building.type == com.example.model.BuildingType.CREAMERY)) {
                        throughputMult *= 2.0f
                    }"""
new_throughput = """                    var throughputMult = 1.0f
                    val hasAcousticLevitation = building.activeAnomalyId != null && com.example.model.AnomalyCatalog.BLUEPRINTS.find { bp -> bp.id == building.activeAnomalyId }?.effectType == com.example.model.AnomalyEffect.ACOUSTIC_LEVITATION
                    if (hasAcousticLevitation) {
                        throughputMult *= 5.0f
                    }"""
kt = kt.replace(old_throughput, new_throughput)

# 3. Market Crisis / Global Price Cleanup (Removing Transparent Wood)
old_crisis = """                    val hasTransparentWood = currentState.researchNodeStatuses["tech_exp_3"] == com.example.model.NodeStatus.COMPLETED
                    if (hasTransparentWood) {
                        crisisMult *= 2.0
                    }"""
kt = kt.replace(old_crisis, "")

# 4. Nanomaterial Desalination Logic (Bypass input consumption)
# Around Step 3b Input Consumption
old_input_consume = """                        for (batch in orderedBatches) {
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
                        }"""
new_input_consume = """                        val hasNanoSynthesis = building.activeAnomalyId != null && com.example.model.AnomalyCatalog.BLUEPRINTS.find { bp -> bp.id == building.activeAnomalyId }?.effectType == com.example.model.AnomalyEffect.NANOMATERIAL_DESALINATION
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
                        }"""
kt = kt.replace(old_input_consume, new_input_consume)

# 5. ViewModel Functions for Anomalies
anomaly_funcs = """
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
"""
if "fun unlockAnomalyBlueprint" not in kt:
    kt = kt.rstrip()[:-1] + anomaly_funcs + "\n}\n"

with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(kt)
