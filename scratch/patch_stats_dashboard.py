import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/StatsDashboardScreen.kt')

# Hide Corporate Warfare if FARMING phase
old_megacorp = """            // Megacorp & Stock Market
            item {
                StatsSectionCard(
                    title = "Corporate Warfare & Stocks","""

new_megacorp = """            // Megacorp & Stock Market
            if (gameState.gamePhase != com.example.model.GamePhase.FARMING) {
                item {
                    StatsSectionCard(
                        title = "Corporate Warfare & Stocks","""

kt = kt.replace(old_megacorp, new_megacorp)

# Close the if block
old_megacorp_end = """                    StatRow(label = "Subsidiaries Controlled (51%+)", value = "${gameState.subsidiaryCompanyIds.size} / 4 Megacorps", valueColor = if (gameState.subsidiaryCompanyIds.size == 4) DairyGoldDark else TechCyan)
                }
            }

            item {"""

new_megacorp_end = """                    StatRow(label = "Subsidiaries Controlled (51%+)", value = "${gameState.subsidiaryCompanyIds.size} / 4 Megacorps", valueColor = if (gameState.subsidiaryCompanyIds.size == 4) DairyGoldDark else TechCyan)
                }
            }
            }

            item {"""

kt = kt.replace(old_megacorp_end, new_megacorp_end)

# Add Daily Averages Section
averages_section = """            // Analytical Averages
            item {
                StatsSectionCard(
                    title = "Operational Analytics",
                    icon = Icons.Default.TrendingUp,
                    iconTint = MaterialTheme.colorScheme.primary
                ) {
                    val safeDays = stats.daysPlayed.coerceAtLeast(1).toDouble()
                    val avgRevenue = stats.totalRevenueEarned / safeDays
                    val avgProduction = stats.totalRawMilkProduced / safeDays
                    
                    val winRate = if (gameState.hostileTakeovers + gameState.attacksThwarted > 0) {
                        (gameState.attacksThwarted.toDouble() / (gameState.hostileTakeovers + gameState.attacksThwarted)) * 100.0
                    } else {
                        0.0
                    }

                    StatRow(label = "Average Daily Revenue", value = "$${String.format("%,.2f", avgRevenue)} / day", valueColor = BullishGreen)
                    StatRow(label = "Average Milk Production", value = "${String.format("%,.1f", avgProduction)} units / day", valueColor = MaterialTheme.colorScheme.onSurface)
                    StatRow(label = "Global Market Share", value = "${String.format("%.1f", gameState.playerMarketShare)}%", valueColor = DairyGoldDark)
                    if (gameState.gamePhase != com.example.model.GamePhase.FARMING) {
                        StatRow(label = "Cyber Defense Win Rate", value = "${String.format("%.1f", winRate)}%", valueColor = TechCyan)
                    }
                }
            }
"""

kt = kt.replace("            // Megacorp & Stock Market", averages_section + "\n            // Megacorp & Stock Market")

# Null safety checks for stats
# It's already val stats = gameState.lifetimeStats which is non-null. 

with open('app/src/main/java/com/example/ui/screens/StatsDashboardScreen.kt', 'w') as f:
    f.write(kt)
