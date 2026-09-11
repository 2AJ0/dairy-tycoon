import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

# Add imports for PendingCyberAttack and AttackType
if "import com.example.model.PendingCyberAttack" not in kt:
    kt = kt.replace("import com.example.model.GamePhase", "import com.example.model.GamePhase\nimport com.example.model.PendingCyberAttack\nimport com.example.model.AttackType\nimport com.example.model.NodeStatus")

warfare_func = """
    private fun processCorporateWarfare(state: com.example.model.GameState, currentDay: Int, logs: MutableList<String>): com.example.model.GameState {
        if (state.gamePhase != com.example.model.GamePhase.CORPORATE) return state
        
        var newState = state
        var cash = state.cash
        var reputation = state.reputation
        val activeAttacks = state.pendingCyberAttacks.filter { it.executionDay <= currentDay }
        val futureAttacks = state.pendingCyberAttacks.filter { it.executionDay > currentDay }.toMutableList()
        val updatedBuildings = state.buildings.toMutableList()
        var updatedRivals = state.rivalCompanies
        
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
                        AttackType.FINANCIAL_PHISHING -> {
                            val stolen = cash * (kotlin.random.Random.nextInt(1, 6) / 100.0)
                            cash -= stolen
                        }
                        AttackType.LOGISTICS_HIJACK -> {
                            cash -= 15000.0
                        }
                        AttackType.DDOS_FACILITY -> {
                            val factoryIdx = updatedBuildings.indexOfFirst { it.isOperational && it.type != com.example.model.BuildingType.PASTURE }
                            if (factoryIdx != -1) {
                                updatedBuildings[factoryIdx] = updatedBuildings[factoryIdx].copy(sabotagedDaysRemaining = 3)
                            }
                        }
                    }
                } else {
                    logs.add("🛡️ Cyber Defense SUCCESS! We blocked an attack from ${attacker.name}.")
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
        
        return newState.copy(
            cash = cash,
            reputation = reputation,
            pendingCyberAttacks = futureAttacks,
            buildings = updatedBuildings,
            cyberAttackWarningEvent = warningEvent,
            rivalCompanies = updatedRivals
        )
    }
"""

if "private fun processCorporateWarfare" not in kt:
    kt = kt.rstrip()[:-1] + warfare_func + "\n}\n"

# Now inject it into endDay
# Need to find the return statement of endDay

old_return = """                var newState = checkAchievements(finalState)
                for (id in newlyCompletedTechIds) {
                    newState = applyResearchEffect(newState, id)
                }
                
                newState
            }"""

new_return = """                var newState = checkAchievements(finalState)
                for (id in newlyCompletedTechIds) {
                    newState = applyResearchEffect(newState, id)
                }
                
                newState = processCorporateWarfare(newState, currentDay, newLogs)
                
                newState
            }"""

kt = kt.replace(old_return, new_return)

with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(kt)
