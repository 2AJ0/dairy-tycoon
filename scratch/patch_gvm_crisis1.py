import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

alert_func = """
    fun resolveNewCrisisAlert() {
        _gameState.update { it.copy(newCrisisFired = null) }
    }
"""

if "fun resolveNewCrisisAlert" not in kt:
    kt = kt.rstrip()[:-1] + alert_func + "\n}\n"

with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(kt)
