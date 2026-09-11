import sys
def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

new_func = """
    fun resolveMissedDelivery(payPenalty: Boolean) {
        val currentState = _gameState.value
        val contractId = currentState.missedDeliveryEvent ?: return
        val contract = currentState.activeContracts.find { it.id == contractId } ?: return
        
        _gameState.update { state ->
            if (payPenalty) {
                state.copy(
                    cash = state.cash - contract.cashPenaltyPerMiss,
                    missedDeliveryEvent = null
                )
            } else {
                val updatedContract = contract.copy(
                    strikes = contract.strikes + 1,
                    payoutAmount = (contract.payoutAmount - contract.rewardReductionPerStrike).coerceAtLeast(0.0)
                )
                // If it hits 3 strikes, we could cancel it, but for now just take the strike.
                state.copy(
                    reputation = (state.reputation - 5).coerceAtLeast(0),
                    activeContracts = state.activeContracts.map { if (it.id == contractId) updatedContract else it },
                    missedDeliveryEvent = null
                )
            }
        }
    }
"""

# add it before the final closing brace
if new_func not in kt:
    kt = kt.rstrip()[:-1] + new_func + "\n}\n"
    with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
        f.write(kt)
    print("Added!")
else:
    print("Already added")
