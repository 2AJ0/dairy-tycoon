import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/FacilitiesTab.kt')

old_bottleneck = """                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp), 
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Allocation: ${building.allocationPercentage}%", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            Text("Est. Draw: $actualDraw Units/Day", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = DairyEmeraldPrimary)
                        }
                        
                        Slider("""

new_bottleneck = """                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp), 
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Allocation: ${building.allocationPercentage}%", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            Text("Est. Draw: $actualDraw Units/Day", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = DairyEmeraldPrimary)
                        }
                        
                        if (actualDraw < requested || (totalStock == 0 && building.allocationPercentage > 0)) {
                            Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                Text("⚠️", fontSize = 14.sp, modifier = Modifier.pulseWarning())
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (totalStock == 0) "Supply Bottleneck: No Raw Material" else "Capacity Bottleneck: Upgrades Required", style = MaterialTheme.typography.labelSmall, color = BearishRed, fontWeight = FontWeight.Bold)
                            }
                        }
                        
                        Slider("""

kt = kt.replace(old_bottleneck, new_bottleneck)

with open('app/src/main/java/com/example/ui/screens/FacilitiesTab.kt', 'w') as f:
    f.write(kt)
