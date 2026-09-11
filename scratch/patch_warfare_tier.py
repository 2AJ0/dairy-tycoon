import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/CorporateWarfareScreen.kt')

# Sort items
old_items = """            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(gameState.rivalCompanies, key = { it.id }) { rival ->"""

new_items = """            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val sortedRivals = gameState.rivalCompanies.sortedByDescending { it.tier.ordinal }
                items(sortedRivals, key = { it.id }) { rival ->"""

kt = kt.replace(old_items, new_items)

# Add Tier Badge
old_card_header = """                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(rival.logoEmoji, fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(rival.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Text("Net Worth: $${String.format("%,.0f", rival.netWorth)}", style = MaterialTheme.typography.bodySmall, color = com.example.ui.theme.BullishGreen)
                                }
                            }"""

new_card_header = """                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(rival.logoEmoji, fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(rival.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Text("Net Worth: $${String.format("%,.0f", rival.netWorth)}", style = MaterialTheme.typography.bodySmall, color = com.example.ui.theme.BullishGreen)
                                }
                                Surface(
                                    color = when (rival.tier) {
                                        com.example.model.RivalTier.GLOBAL -> MaterialTheme.colorScheme.errorContainer
                                        com.example.model.RivalTier.SPECIALIZED -> MaterialTheme.colorScheme.secondaryContainer
                                        com.example.model.RivalTier.LOCAL -> MaterialTheme.colorScheme.surfaceVariant
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        rival.tier.name,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = when (rival.tier) {
                                            com.example.model.RivalTier.GLOBAL -> MaterialTheme.colorScheme.onErrorContainer
                                            com.example.model.RivalTier.SPECIALIZED -> MaterialTheme.colorScheme.onSecondaryContainer
                                            com.example.model.RivalTier.LOCAL -> MaterialTheme.colorScheme.onSurfaceVariant
                                        }
                                    )
                                }
                            }"""

kt = kt.replace(old_card_header, new_card_header)

with open('app/src/main/java/com/example/ui/screens/CorporateWarfareScreen.kt', 'w') as f:
    f.write(kt)
