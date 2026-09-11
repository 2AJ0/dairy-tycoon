import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/FacilitiesTab.kt')

# Split buildings
old_visible = """    val visibleBuildings = remember(gameState.buildings, gameState.unlockedTechIds, gameState.activeProjects) {
        gameState.buildings.filter { building ->
            building.isConstructed ||
            building.requiredTechId == null ||
            gameState.unlockedTechIds.contains(building.requiredTechId) ||
            gameState.activeProjects.any { it.targetId == building.id }
        }
    }"""

new_visible = """    val visibleBuildings = remember(gameState.buildings, gameState.unlockedTechIds, gameState.activeProjects) {
        gameState.buildings.filter { building ->
            building.isConstructed ||
            building.requiredTechId == null ||
            gameState.unlockedTechIds.contains(building.requiredTechId) ||
            gameState.activeProjects.any { it.targetId == building.id }
        }
    }
    
    val lockedBuildings = remember(gameState.buildings, visibleBuildings) {
        gameState.buildings.filter { !visibleBuildings.contains(it) }
    }"""

kt = kt.replace(old_visible, new_visible)

# Add milestones section at the end of LazyColumn
old_items = """        items(visibleBuildings, key = { it.id }) { building ->"""

new_items = """        items(visibleBuildings, key = { it.id }) { building ->"""
# Oh wait, we need to append after this `items` block finishes. Let's find where the `LazyColumn` ends.

with open('scratch/patch_facilities_milestones.py', 'w') as f:
    pass # we will manually do it
