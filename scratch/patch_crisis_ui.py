import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

# 1. Update MainGameScreen.kt
kt = read_file('app/src/main/java/com/example/ui/screens/MainGameScreen.kt')

alert_dialog = """
            if (gameState.newCrisisFired != null) {
                androidx.compose.material3.AlertDialog(
                    onDismissRequest = { viewModel.resolveNewCrisisAlert() },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⚠️", fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("BREAKING NEWS", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    text = {
                        Column {
                            Text(gameState.newCrisisFired.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(gameState.newCrisisFired.description)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Duration: ${gameState.newCrisisFired.durationDays} Days", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    confirmButton = {
                        androidx.compose.material3.Button(onClick = { viewModel.resolveNewCrisisAlert() }, colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                            Text("Acknowledge")
                        }
                    }
                )
            }
"""

# Insert alert_dialog before "if (gameState.gamePhase == com.example.model.GamePhase.COMPLETED) {"
kt = kt.replace("if (gameState.gamePhase == com.example.model.GamePhase.COMPLETED) {", alert_dialog + "\n            if (gameState.gamePhase == com.example.model.GamePhase.COMPLETED) {")

with open('app/src/main/java/com/example/ui/screens/MainGameScreen.kt', 'w') as f:
    f.write(kt)
