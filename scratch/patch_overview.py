import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/OverviewTab.kt')

old_net = """                        Text(
                            "$${String.format("%,.0f", gameState.netWorth)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = BullishGreen
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Cash: $${String.format("%,.0f", gameState.cash)}",
                            style = MaterialTheme.typography.labelSmall
                        )"""

new_net = """                        com.example.ui.components.AnimatedCounter(
                            targetValue = gameState.netWorth,
                            prefix = "$",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = BullishGreen
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Cash: ", style = MaterialTheme.typography.labelSmall)
                            com.example.ui.components.AnimatedCounter(
                                targetValue = gameState.cash,
                                prefix = "$",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }"""

kt = kt.replace(old_net, new_net)

with open('app/src/main/java/com/example/ui/screens/OverviewTab.kt', 'w') as f:
    f.write(kt)
