import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

# In processCorporateWarfare, track cyber stats and takeovers
old_warfare = """        var reputation = state.reputation
        val activeAttacks = state.pendingCyberAttacks.filter { it.executionDay <= currentDay }
        val futureAttacks = state.pendingCyberAttacks.filter { it.executionDay > currentDay }.toMutableList()
        val updatedBuildings = state.buildings.toMutableList()
        var updatedRivals = state.rivalCompanies"""

new_warfare = """        var reputation = state.reputation
        val activeAttacks = state.pendingCyberAttacks.filter { it.executionDay <= currentDay }
        val futureAttacks = state.pendingCyberAttacks.filter { it.executionDay > currentDay }.toMutableList()
        val updatedBuildings = state.buildings.toMutableList()
        var updatedRivals = state.rivalCompanies
        var newAttacksThwarted = state.attacksThwarted
        var newFraudLosses = state.fraudLosses
        var newDdosDays = state.ddosDowntimeDays
        var newHostileTakeovers = state.hostileTakeovers"""

kt = kt.replace(old_warfare, new_warfare)

old_phishing = """                    when (attack.attackType) {
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
                    }"""

new_phishing = """                    when (attack.attackType) {
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

kt = kt.replace(old_phishing, new_phishing)

old_defense = """                } else {
                    logs.add("🛡️ Cyber Defense SUCCESS! We blocked an attack from ${attacker.name}.")
                }"""

new_defense = """                } else {
                    logs.add("🛡️ Cyber Defense SUCCESS! We blocked an attack from ${attacker.name}.")
                    newAttacksThwarted++
                }"""

kt = kt.replace(old_defense, new_defense)

old_takeover = """            if (ownedShares > 50) {
                rivalsToEliminate.add(rival.id)
                additionalPlayerMarketShare += rival.marketShare
                liquidationBonus += rival.netWorth * 0.20 // 20% bonus
                logs.add("🚨 HOSTILE TAKEOVER COMPLETE: Acquired controlling stake in ${rival.name}! Operations liquidated.")
            }"""

new_takeover = """            if (ownedShares > 50) {
                rivalsToEliminate.add(rival.id)
                additionalPlayerMarketShare += rival.marketShare
                liquidationBonus += rival.netWorth * 0.20 // 20% bonus
                newHostileTakeovers++
                logs.add("🚨 HOSTILE TAKEOVER COMPLETE: Acquired controlling stake in ${rival.name}! Operations liquidated.")
            }"""

kt = kt.replace(old_takeover, new_takeover)

old_victory = """        // --- VICTORY CONDITIONS ---
        if (newState.playerMarketShare >= 100.0f && newState.gameOverReason == null && !newState.isGameOver) {
            newState = newState.copy(
                isGameOver = true,
                gameOverReason = "MONOPOLIST VICTORY: You have conquered 100% of the market share. The dairy industry is yours."
            )
        }
        
        if (newState.researchNodeStatuses["tech_singularity"] == NodeStatus.COMPLETED && newState.gameOverReason == null && !newState.isGameOver) {
            newState = newState.copy(
                isGameOver = true,
                gameOverReason = "TECH SINGULARITY VICTORY: You have transcended the market with absolute automation."
            )
        }
"""

new_victory = """        // --- VICTORY CONDITIONS ---
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
"""

kt = kt.replace(old_victory, new_victory)

old_ret = """        return newState.copy(
            cash = cash,
            reputation = reputation,
            pendingCyberAttacks = futureAttacks,
            buildings = updatedBuildings,
            cyberAttackWarningEvent = warningEvent,
            rivalCompanies = updatedRivals
        )"""

new_ret = """        return newState.copy(
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
        )"""

kt = kt.replace(old_ret, new_ret)

with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(kt)
