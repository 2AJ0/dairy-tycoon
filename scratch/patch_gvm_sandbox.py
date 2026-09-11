import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

sandbox_func = """
    fun continueInSandbox() {
        _gameState.update { state ->
            state.copy(
                gamePhase = com.example.model.GamePhase.SANDBOX,
                isGameOver = false,
                gameOverReason = null,
                rivalCompanies = emptyList(), // Remove AI rivals
                pendingCyberAttacks = emptyList(),
                cyberAttackWarningEvent = null
            )
        }
    }
"""

if "fun continueInSandbox" not in kt:
    kt = kt.rstrip()[:-1] + sandbox_func + "\n}\n"

with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(kt)
