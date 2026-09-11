import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

old_copy = """                val finalState = currentState.copy(
                    day = nextDay,
                    cash = currentCash,
                    reputation = updatedReputation2,"""

new_copy = """                val finalState = currentState.copy(
                    day = nextDay,
                    dailyActionsRemaining = currentState.maxDailyActions,
                    cash = currentCash,
                    reputation = updatedReputation2,"""

kt = kt.replace(old_copy, new_copy)

with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(kt)

