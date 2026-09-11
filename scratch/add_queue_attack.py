import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

new_func = """
    fun queuePlayerCyberAttack(rivalId: String, type: AttackType, cost: Double, apCost: Int) {
        val currentState = _gameState.value
        if (currentState.cash < cost || currentState.dailyActionsRemaining < apCost) return
        
        _gameState.update { state ->
            val newAttacks = state.pendingCyberAttacks.toMutableList()
            newAttacks.add(PendingCyberAttack(
                attackerId = "PLAYER",
                targetId = rivalId,
                attackType = type,
                executionDay = state.day + 2
            ))
            state.copy(
                cash = state.cash - cost,
                dailyActionsRemaining = state.dailyActionsRemaining - apCost,
                pendingCyberAttacks = newAttacks
            )
        }
        _snackBarMessage.value = "Cyber attack deployed against target!"
    }
    
    fun resolveCyberAttackWarning(boostDefense: Boolean) {
        val currentState = _gameState.value
        val warningEvent = currentState.cyberAttackWarningEvent ?: return
        
        _gameState.update { state ->
            if (boostDefense) {
                if (state.cash >= 15000.0) {
                    state.copy(
                        cash = state.cash - 15000.0,
                        defenseRating = state.defenseRating + 25, // Temporary huge boost
                        cyberAttackWarningEvent = null
                    )
                } else state
            } else {
                state.copy(cyberAttackWarningEvent = null)
            }
        }
    }
"""

if "fun queuePlayerCyberAttack" not in kt:
    kt = kt.rstrip()[:-1] + new_func + "\n}\n"
    with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
        f.write(kt)
