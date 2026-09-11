import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

takeover_logic = """
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
                logs.add("🚨 HOSTILE TAKEOVER COMPLETE: Acquired controlling stake in ${rival.name}! Operations liquidated.")
            }
        }
        
        if (rivalsToEliminate.isNotEmpty()) {
            updatedRivals = updatedRivals.filter { it.id not in rivalsToEliminate }
            cash += liquidationBonus
            newState = newState.copy(playerMarketShare = newState.playerMarketShare + additionalPlayerMarketShare)
        }
        
        // --- VICTORY CONDITIONS ---
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

# I need to insert this right before the end of processCorporateWarfare, or in endDay itself.
# Since processCorporateWarfare is already at the end of endDay, I can append it into processCorporateWarfare.

old_ret = """        return newState.copy(
            cash = cash,
            reputation = reputation,
            pendingCyberAttacks = futureAttacks,
            buildings = updatedBuildings,
            cyberAttackWarningEvent = warningEvent,
            rivalCompanies = updatedRivals
        )
    }"""

new_ret = takeover_logic + """
        return newState.copy(
            cash = cash,
            reputation = reputation,
            pendingCyberAttacks = futureAttacks,
            buildings = updatedBuildings,
            cyberAttackWarningEvent = warningEvent,
            rivalCompanies = updatedRivals
        )
    }"""

kt = kt.replace(old_ret, new_ret)

with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(kt)
