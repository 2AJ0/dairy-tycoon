sed -i '' '/\/\/ Action Buttons: Bargain, Accept, Decline/i\
                        if (gameState.reputation < offer.minReputationRequired) {\
                            Text(\
                                text = "🔒 Requires ${offer.minReputationRequired} Reputation",\
                                style = MaterialTheme.typography.labelSmall,\
                                color = MaterialTheme.colorScheme.error,\
                                fontWeight = FontWeight.Bold,\
                                modifier = Modifier.padding(bottom = 8.dp)\
                            )\
                        }\
' app/src/main/java/com/example/ui/screens/B2BContractsScreen.kt
