import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/FacilitiesTab.kt')

# Add missing parameters or states
old_sig = """    onBuyLandExpansion: () -> Unit,
    onUpdateAllocation: (String, Int) -> Unit,
    onSpinOff: (String, String) -> Unit = { _, _ -> }
) {"""

new_sig = """    onBuyLandExpansion: () -> Unit,
    onUpdateAllocation: (String, Int) -> Unit,
    onSpinOff: (String, String) -> Unit = { _, _ -> },
    onBuyFacilityPerk: (String, String) -> Unit = { _, _ -> }
) {
    var selectedTechTreeFacility by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<com.example.model.Building?>(null) }
    
    if (selectedTechTreeFacility != null) {
        val currentBuilding = gameState.buildings.find { it.id == selectedTechTreeFacility!!.id }
        if (currentBuilding != null) {
            com.example.ui.components.FacilityTechTreeDialog(
                building = currentBuilding,
                onDismiss = { selectedTechTreeFacility = null },
                onBuyPerk = { perkId -> onBuyFacilityPerk(currentBuilding.id, perkId) }
            )
        } else {
            selectedTechTreeFacility = null
        }
    }
"""

kt = kt.replace(old_sig, new_sig)

# Add Specialization button
old_btn = """                    if (isConstructed && building.type != com.example.model.BuildingType.PASTURE && building.type != com.example.model.BuildingType.RD_LAB && building.type != com.example.model.BuildingType.COLD_STORAGE && !building.isSpunOff) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { onSpinOff(building.id, "${building.name} Corp") },
                            modifier = Modifier.fillMaxWidth().com.example.ui.components.bounceClick(),
                            colors = ButtonDefaults.buttonColors(containerColor = com.example.ui.theme.DairyGoldDark)
                        ) {
                            Text("Spin Off into Subsidiary", color = androidx.compose.ui.graphics.Color.Black, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                        }
                    }"""

new_btn = """                    if (isConstructed && building.type != com.example.model.BuildingType.PASTURE && building.type != com.example.model.BuildingType.RD_LAB && building.type != com.example.model.BuildingType.COLD_STORAGE && !building.isSpunOff) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { selectedTechTreeFacility = building },
                                modifier = Modifier.weight(1f).com.example.ui.components.bounceClick(),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                            ) {
                                Text("Specializations", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                            }
                            Button(
                                onClick = { onSpinOff(building.id, "${building.name} Corp") },
                                modifier = Modifier.weight(1f).com.example.ui.components.bounceClick(),
                                colors = ButtonDefaults.buttonColors(containerColor = com.example.ui.theme.DairyGoldDark)
                            ) {
                                Text("Spin Off", color = androidx.compose.ui.graphics.Color.Black, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                            }
                        }
                    }"""

kt = kt.replace(old_btn, new_btn)

with open('app/src/main/java/com/example/ui/screens/FacilitiesTab.kt', 'w') as f:
    f.write(kt)
