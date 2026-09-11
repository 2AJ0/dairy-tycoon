import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/FacilitiesTab.kt')

old_sig = """    onBuyLandExpansion: () -> Unit,
    onUpdateAllocation: (String, Int) -> Unit,
    onSpinOff: (String, String) -> Unit = { _, _ -> },
    onBuyFacilityPerk: (String, String) -> Unit = { _, _ -> }
) {"""

new_sig = """    onBuyLandExpansion: () -> Unit,
    onUpdateAllocation: (String, Int) -> Unit,
    onSpinOff: (String, String) -> Unit = { _, _ -> },
    onBuyFacilityPerk: (String, String) -> Unit = { _, _ -> },
    onInstallAnomaly: (String, String) -> Unit = { _, _ -> }
) {
    var selectedAnomalyFacility by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<com.example.model.Building?>(null) }
    
    if (selectedAnomalyFacility != null) {
        com.example.ui.components.AnomalySelectionModal(
            facility = selectedAnomalyFacility!!,
            gameState = gameState,
            onDismiss = { selectedAnomalyFacility = null },
            onInstall = { bpId ->
                onInstallAnomaly(selectedAnomalyFacility!!.id, bpId)
                selectedAnomalyFacility = null
            }
        )
    }
"""

kt = kt.replace(old_sig, new_sig)

old_buttons = """                            Button(
                                onClick = { onUpgradeBuilding(building.id) },
                                modifier = Modifier.weight(1f).com.example.ui.components.bounceClick(),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Upgrade ($${String.format("%,.0f", upgradeCost)})", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                            }"""

new_buttons = """                            if (building.level < building.maxLevel) {
                                Button(
                                    onClick = { onUpgradeBuilding(building.id) },
                                    modifier = Modifier.weight(1f).com.example.ui.components.bounceClick(),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Text("Upgrade ($${String.format("%,.0f", upgradeCost)})", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                                }
                            } else {
                                Button(
                                    onClick = { selectedAnomalyFacility = building },
                                    modifier = Modifier.weight(1f).com.example.ui.components.bounceClick(),
                                    colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color(0xFF00FFCC))
                                ) {
                                    val hasAnomaly = building.activeAnomalyId != null
                                    Text(if (hasAnomaly) "Anomaly Active" else "Anomaly Slot", color = androidx.compose.ui.graphics.Color.Black, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                                }
                            }"""

kt = kt.replace(old_buttons, new_buttons)

with open('app/src/main/java/com/example/ui/screens/FacilitiesTab.kt', 'w') as f:
    f.write(kt)
