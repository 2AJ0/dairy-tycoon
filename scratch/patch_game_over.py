import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/components/GameOverDialog.kt')

old_dialog = """    AlertDialog(
        onDismissRequest = {},
        modifier = Modifier.testTag("game_over_dialog"),
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("💥", fontSize = 40.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "BANKRUPTCY & FORECLOSURE",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = BearishRed,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Text(
                text = reason ?: "Your liabilities exceeded your total net worth and debt remained unpaid for too long.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            Button(
                onClick = onRestart,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("restart_game_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BearishRed
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Start New Farm Empire", fontWeight = FontWeight.Bold)
            }
        }
    )"""

new_dialog = """
    val isVictory = reason?.contains("VICTORY") == true
    val isMonopoly = reason?.contains("MONOPOLIST") == true
    
    if (isVictory) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = {},
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .androidx.compose.foundation.layout.fillMaxSize()
                    .androidx.compose.foundation.background(com.example.ui.theme.DairyGoldDark)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                        .padding(32.dp)
                ) {
                    Text(if (isMonopoly) "👑" else "🤖", fontSize = 72.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (isMonopoly) "GLOBAL MONOPOLIST" else "TECH SINGULARITY",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = com.example.ui.theme.DairyGoldDark,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = reason ?: "You have won.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = onRestart,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = com.example.ui.theme.DairyEmeraldPrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Start New Sandbox", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    } else {
        AlertDialog(
            onDismissRequest = {},
            modifier = Modifier.testTag("game_over_dialog"),
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("💥", fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "BANKRUPTCY & FORECLOSURE",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = BearishRed,
                        textAlign = TextAlign.Center
                    )
                }
            },
            text = {
                Text(
                    text = reason ?: "Your liabilities exceeded your total net worth and debt remained unpaid for too long.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = onRestart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("restart_game_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BearishRed
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Start New Farm Empire", fontWeight = FontWeight.Bold)
                }
            }
        )
    }"""

kt = kt.replace(old_dialog, new_dialog)

with open('app/src/main/java/com/example/ui/components/GameOverDialog.kt', 'w') as f:
    f.write(kt)
