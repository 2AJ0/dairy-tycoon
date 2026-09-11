import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/MainGameScreen.kt')

dilemma = """            if (gameState.cyberAttackWarningEvent != null) {
                val event = gameState.cyberAttackWarningEvent
                val attacker = com.example.model.RivalCatalog.getRivalById(event.attackerId)
                androidx.compose.material3.AlertDialog(
                    onDismissRequest = { /* Must choose */ },
                    title = {
                        Text("🚨 Early Warning: Cyber Attack Detected", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                    },
                    text = {
                        Column {
                            Text("Your Scam Detection Algorithm has intercepted encrypted traffic from ${attacker.name}.")
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Projected Attack Type: ${event.attackType}", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Would you like to authorize an emergency expenditure of $15,000 to temporarily boost our Cyber Defense by +25?", style = MaterialTheme.typography.bodySmall)
                        }
                    },
                    confirmButton = {
                        val canAfford = gameState.cash >= 15000.0
                        androidx.compose.material3.Button(
                            onClick = { viewModel.resolveCyberAttackWarning(true) },
                            enabled = canAfford,
                            modifier = Modifier.bounceClick(),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = if (canAfford) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Text("Boost Defense (-$15,000)")
                        }
                    },
                    dismissButton = {
                        androidx.compose.material3.OutlinedButton(
                            onClick = { viewModel.resolveCyberAttackWarning(false) },
                            modifier = Modifier.bounceClick()
                        ) {
                            Text("Ignore (Rely on Base Defense)", color = MaterialTheme.colorScheme.error)
                        }
                    }
                )
            }
"""

old_target = """            if (gameState.missedDeliveryEvent != null) {"""
new_target = dilemma + "\n" + old_target

kt = kt.replace(old_target, new_target)

with open('app/src/main/java/com/example/ui/screens/MainGameScreen.kt', 'w') as f:
    f.write(kt)
