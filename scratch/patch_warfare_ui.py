import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/CorporateWarfareScreen.kt')

# Add slider
slider_imports = "import androidx.compose.material3.Slider\nimport androidx.compose.runtime.mutableFloatStateOf\n"
if "import androidx.compose.material3.Slider" not in kt:
    kt = kt.replace("import androidx.compose.material3.*", "import androidx.compose.material3.*\n" + slider_imports)

old_card = """                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Threat: ${rival.threatLevel}/100", style = MaterialTheme.typography.labelSmall, color = com.example.ui.theme.BearishRed)
                                Text("Def: ${rival.defenseRating}", style = MaterialTheme.typography.labelSmall)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Button(
                                onClick = { selectedTarget = rival },
                                modifier = Modifier.fillMaxWidth().bounceClick(),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("Queue Sabotage")
                            }
                        }"""

new_card = """                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Threat: ${rival.threatLevel}/100", style = MaterialTheme.typography.labelSmall, color = com.example.ui.theme.BearishRed)
                                Text("Market Share: ${rival.marketShare}%", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // Buy Shares section
                            val sharesOwned = gameState.getSharesOwned(rival.id)
                            val costPerShare = rival.netWorth / 100.0
                            var buyAmount by remember { mutableFloatStateOf(1f) }
                            val availableToBuy = (100 - sharesOwned).coerceAtLeast(0)
                            
                            Text("Shares Owned: $sharesOwned/100", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            if (availableToBuy > 0) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                    Slider(
                                        value = buyAmount,
                                        onValueChange = { buyAmount = it },
                                        valueRange = 1f..availableToBuy.toFloat(),
                                        steps = availableToBuy - 2,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("${buyAmount.toInt()} shares")
                                }
                                
                                Button(
                                    onClick = { /* Need to call viewmodel buyRivalShares */ },
                                    modifier = Modifier.fillMaxWidth().bounceClick(),
                                    colors = ButtonDefaults.buttonColors(containerColor = com.example.ui.theme.DairyEmeraldPrimary)
                                ) {
                                    Text("Buy ${buyAmount.toInt()} Shares ($${String.format("%,.0f", buyAmount.toInt() * costPerShare)})")
                                }
                            } else {
                                Text("Maximum shares acquired! Hostile Takeover impending.", style = MaterialTheme.typography.bodySmall, color = com.example.ui.theme.DairyGoldDark)
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Button(
                                onClick = { selectedTarget = rival },
                                modifier = Modifier.fillMaxWidth().bounceClick(),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("Queue Sabotage")
                            }
                        }"""

kt = kt.replace(old_card, new_card)

with open('app/src/main/java/com/example/ui/screens/CorporateWarfareScreen.kt', 'w') as f:
    f.write(kt)
