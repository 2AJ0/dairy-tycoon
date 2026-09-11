import sys
import re

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

# Add generateRandomEmpireName
name_generator = """
    private fun generateRandomEmpireName(): String {
        val prefixes = listOf("Golden", "Sunny", "Automated", "Merku", "Pananchery", "Quantum")
        val suffixes = listOf("Meadows", "Dairies", "Acres", "Pastures", "Holdings")
        return "${prefixes.random()} ${suffixes.random()}"
    }
"""

if "generateRandomEmpireName" not in kt:
    kt = kt.replace("class GameViewModel(application: Application) : AndroidViewModel(application) {", "class GameViewModel(application: Application) : AndroidViewModel(application) {" + name_generator)

# Replace GameState() with GameState(saveName = generateRandomEmpireName())
kt = kt.replace("val newState = GameState()", "val newState = GameState(saveName = generateRandomEmpireName())")

# Add updateEmpireName function
update_func = """
    fun updateEmpireName(newName: String) {
        val trimmed = newName.take(20).trim()
        if (trimmed.isNotEmpty()) {
            _gameState.update { it.copy(saveName = trimmed) }
            saveGameManager.saveGame(_gameState.value)
            _snackBarMessage.value = "Empire renamed to $trimmed"
        }
    }
"""

if "fun updateEmpireName" not in kt:
    # Insert it before fun startNewGame
    kt = kt.replace("fun startNewGame()", update_func + "\n    fun startNewGame()")

with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(kt)
