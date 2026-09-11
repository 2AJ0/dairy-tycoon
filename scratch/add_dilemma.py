import sys
def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/MainGameScreen.kt')

dilemma = """
            if (gameState.missedDeliveryEvent != null) {
                val contractId = gameState.missedDeliveryEvent
                val contract = gameState.activeContracts.find { it.id == contractId }
                if (contract != null) {
                    androidx.compose.material3.AlertDialog(
                        onDismissRequest = { /* Must choose */ },
                        title = {
                            Text("⚠️ Morning Briefing: Delivery Missed", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                        },
                        text = {
                            Column {
                                val rival = com.example.model.RivalCatalog.getRivalById(contract.rivalId)
                                Text("Your farm failed to meet yesterday's delivery quota for ${rival.name}!")
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("A representative is demanding an immediate resolution. You can either pay the breach penalty in cash, or refuse and take a permanent strike on the contract.", style = MaterialTheme.typography.bodySmall)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("Strikes: ${contract.strikes} / 3", fontWeight = FontWeight.Bold)
                            }
                        },
                        confirmButton = {
                            val canAfford = gameState.cash >= contract.cashPenaltyPerMiss
                            Button(
                                onClick = { viewModel.resolveMissedDelivery(true) },
                                enabled = canAfford,
                                colors = ButtonDefaults.buttonColors(containerColor = if (canAfford) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Text("Pay Penalty (-$${contract.cashPenaltyPerMiss})")
                            }
                        },
                        dismissButton = {
                            androidx.compose.material3.OutlinedButton(
                                onClick = { viewModel.resolveMissedDelivery(false) }
                            ) {
                                Text("Accept Strike", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    )
                }
            }
"""

old_target = """            if (gameState.isGameOver) {"""
new_target = dilemma + "\n" + old_target

kt = kt.replace(old_target, new_target)

with open('app/src/main/java/com/example/ui/screens/MainGameScreen.kt', 'w') as f:
    f.write(kt)
