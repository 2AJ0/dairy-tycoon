sed -i '' '/if (gameState.isGameOver) {/i\
\
            if (showReputationSheet) {\
                androidx.compose.material3.ModalBottomSheet(\
                    onDismissRequest = { showReputationSheet = false },\
                    containerColor = MaterialTheme.colorScheme.surface\
                ) {\
                    Column(modifier = Modifier.padding(24.dp).fillMaxWidth()) {\
                        Text("Reputation Breakdown", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)\
                        Spacer(modifier = Modifier.height(16.dp))\
                        \
                        val breakdown = gameState.reputationBreakdown\
                        \
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {\
                            Text("Livestock (Pastures):")\
                            Text("+${breakdown.livestockScore}", color = BullishGreen, fontWeight = FontWeight.Bold)\
                        }\
                        Spacer(modifier = Modifier.height(8.dp))\
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {\
                            Text("Scale (Facilities & Variety):")\
                            Text("+${breakdown.scaleScore}", color = BullishGreen, fontWeight = FontWeight.Bold)\
                        }\
                        Spacer(modifier = Modifier.height(8.dp))\
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {\
                            Text("Quality (Highest Tier):")\
                            Text("+${breakdown.qualityScore}", color = BullishGreen, fontWeight = FontWeight.Bold)\
                        }\
                        Spacer(modifier = Modifier.height(8.dp))\
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {\
                            Text("Penalties:")\
                            Text("-${breakdown.penalties}", color = BearishRed, fontWeight = FontWeight.Bold)\
                        }\
                        Spacer(modifier = Modifier.height(16.dp))\
                        androidx.compose.material3.Divider()\
                        Spacer(modifier = Modifier.height(16.dp))\
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {\
                            Text("Target Reputation:", fontWeight = FontWeight.Bold)\
                            Text("${breakdown.totalReputation} / 100", fontWeight = FontWeight.Bold)\
                        }\
                        Spacer(modifier = Modifier.height(8.dp))\
                        Text("Current Level: ${gameState.reputation} / 100 (Adjusts daily towards target)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)\
                        Spacer(modifier = Modifier.height(24.dp))\
                    }\
                }\
            }\

' app/src/main/java/com/example/ui/screens/MainGameScreen.kt
