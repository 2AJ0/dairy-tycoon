import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/FacilitiesTab.kt')

# Add lockedBuildings declaration right after visibleBuildings
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

# Change Divider to HorizontalDivider
kt = kt.replace("Divider()", "HorizontalDivider()")

# Change `building` to `it` to comply with user's specific request
old_items = """            items(lockedBuildings, key = { "locked_" + it.id }) { building ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(building.iconEmoji, fontSize = 24.sp, modifier = Modifier.alpha(0.5f))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = building.name,"""

new_items = """            items(lockedBuildings, key = { "locked_" + it.id }) { 
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(it.iconEmoji, fontSize = 24.sp, modifier = Modifier.alpha(0.5f))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = it.name,"""

kt = kt.replace(old_items, new_items)

with open('app/src/main/java/com/example/ui/screens/FacilitiesTab.kt', 'w') as f:
    f.write(kt)
