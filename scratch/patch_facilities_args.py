import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/FacilitiesTab.kt')

old_sig = """@Composable
fun FacilitiesTab(
    gameState: GameState,
    onBuyBuilding: (String) -> Unit,
    onUpgradeBuilding: (String) -> Unit,
    onToggleBuilding: (String) -> Unit,
    onSelectRecipe: (String, Int) -> Unit,
    onBuyLandExpansion: () -> Unit,
    onUpdateAllocation: (String, Int) -> Unit
) {"""

new_sig = """@Composable
fun FacilitiesTab(
    gameState: GameState,
    onBuyBuilding: (String) -> Unit,
    onUpgradeBuilding: (String) -> Unit,
    onToggleBuilding: (String) -> Unit,
    onSelectRecipe: (String, Int) -> Unit,
    onBuyLandExpansion: () -> Unit,
    onUpdateAllocation: (String, Int) -> Unit,
    onSpinOff: (String, String) -> Unit = { _, _ -> }
) {"""

kt = kt.replace(old_sig, new_sig)

old_btn = """                    // Recipe Switcher for Facilities with multiple recipes
                    if (isConstructed && building.availableRecipes.size > 1) {"""

new_btn = """                    if (isConstructed && building.type != com.example.model.BuildingType.PASTURE && building.type != com.example.model.BuildingType.RD_LAB && building.type != com.example.model.BuildingType.COLD_STORAGE && !building.isSpunOff) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { onSpinOff(building.id, "${building.name} Corp") },
                            modifier = Modifier.fillMaxWidth().com.example.ui.components.bounceClick(),
                            colors = ButtonDefaults.buttonColors(containerColor = com.example.ui.theme.DairyGoldDark)
                        ) {
                            Text("Spin Off into Subsidiary", color = androidx.compose.ui.graphics.Color.Black, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                        }
                    }

                    // Recipe Switcher for Facilities with multiple recipes
                    if (isConstructed && building.availableRecipes.size > 1) {"""

kt = kt.replace(old_btn, new_btn)

with open('app/src/main/java/com/example/ui/screens/FacilitiesTab.kt', 'w') as f:
    f.write(kt)
