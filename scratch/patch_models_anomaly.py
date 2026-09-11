import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt_b = read_file('app/src/main/java/com/example/model/Building.kt')

# Add maxLevel and activeAnomalyId
old_b = """    val unlockedPerks: List<String> = emptyList()
) {"""

new_b = """    val unlockedPerks: List<String> = emptyList(),
    val maxLevel: Int = 3,
    val activeAnomalyId: String? = null
) {"""

kt_b = kt_b.replace(old_b, new_b)

with open('app/src/main/java/com/example/model/Building.kt', 'w') as f:
    f.write(kt_b)

kt_gs = read_file('app/src/main/java/com/example/model/GameState.kt')

old_gs = """    val hasSeenAweTutorial: Boolean = false
) {"""
new_gs = """    val hasSeenAweTutorial: Boolean = false,
    val unlockedAnomalies: List<String> = emptyList()
) {"""
kt_gs = kt_gs.replace(old_gs, new_gs)

with open('app/src/main/java/com/example/model/GameState.kt', 'w') as f:
    f.write(kt_gs)

