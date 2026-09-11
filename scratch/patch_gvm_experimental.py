import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

# 1. Standard Tree Evaluation in endDay()
old_research_apply = """                for (id in newlyCompletedTechIds) {
                    newState = applyResearchEffect(newState, id)
                }"""
new_research_apply = """                for (id in newlyCompletedTechIds) {
                    newState = applyResearchEffect(newState, id)
                }
                
                val standardNodes = com.example.model.ResearchCatalog.ALL_NODES.filter { it.category != com.example.model.ResearchCategory.EXPERIMENTAL }
                val isStandardComplete = standardNodes.all { newState.researchNodeStatuses[it.id] == com.example.model.NodeStatus.COMPLETED }
                newState = newState.copy(isStandardTreeComplete = isStandardComplete)"""

kt = kt.replace(old_research_apply, new_research_apply)

# 2. Radiative Cooling (Zero Cold Storage Maintenance)
old_maint = """val rawMaintenance = currentState.buildings.filter { it.isConstructed && it.isOperational && !it.isSpunOff }
                    .sumOf { it.currentMaintenance }"""
new_maint = """val hasRadiativeCooling = currentState.researchNodeStatuses["tech_exp_1"] == com.example.model.NodeStatus.COMPLETED
                val rawMaintenance = currentState.buildings.filter { it.isConstructed && it.isOperational && !it.isSpunOff }
                    .sumOf { if (hasRadiativeCooling && it.type == com.example.model.BuildingType.COLD_STORAGE) 0.0 else it.currentMaintenance }"""
kt = kt.replace(old_maint, new_maint)

# 3. Acoustic Levitation (Double Liquid Throughput)
old_throughput = """                    var throughputMult = 1.0f
                    var qualityMult = 1.0f"""
new_throughput = """                    val hasAcousticLevitation = currentState.researchNodeStatuses["tech_exp_2"] == com.example.model.NodeStatus.COMPLETED
                    var throughputMult = 1.0f
                    if (hasAcousticLevitation && (building.type == com.example.model.BuildingType.PASTEURIZER || building.type == com.example.model.BuildingType.CREAMERY)) {
                        throughputMult *= 2.0f
                    }
                    var qualityMult = 1.0f"""
kt = kt.replace(old_throughput, new_throughput)

# 4. Transparent Wood Packaging (Double Global Sell Price)
old_crisis = """                    var crisisMult = 1.0
                    updatedCrises.forEach { crisis ->
                        if (crisis.modifierType == com.example.model.CrisisModifierType.MARKET_CRASH) crisisMult *= 0.7
                        if (crisis.modifierType == com.example.model.CrisisModifierType.SUPPLY_SHORTAGE && product.category == com.example.model.ProductCategory.RAW) crisisMult *= 1.5
                    }"""
new_crisis = """                    var crisisMult = 1.0
                    updatedCrises.forEach { crisis ->
                        if (crisis.modifierType == com.example.model.CrisisModifierType.MARKET_CRASH) crisisMult *= 0.7
                        if (crisis.modifierType == com.example.model.CrisisModifierType.SUPPLY_SHORTAGE && product.category == com.example.model.ProductCategory.RAW) crisisMult *= 1.5
                    }
                    
                    val hasTransparentWood = currentState.researchNodeStatuses["tech_exp_3"] == com.example.model.NodeStatus.COMPLETED
                    if (hasTransparentWood) {
                        crisisMult *= 2.0
                    }"""
kt = kt.replace(old_crisis, new_crisis)

with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(kt)
