import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/FacilitiesTab.kt')

old_items_end = """                            Text(
                                text = if (!isConstructed) "Start Construction (${building.daysToComplete}d) • $${String.format("%,.0f", building.currentCost)}" else "Begin Upgrade (${building.daysToComplete}d) • $${String.format("%,.0f", building.currentCost)}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }"""

new_items_end = """                            Text(
                                text = if (!isConstructed) "Start Construction (${building.daysToComplete}d) • $${String.format("%,.0f", building.currentCost)}" else "Begin Upgrade (${building.daysToComplete}d) • $${String.format("%,.0f", building.currentCost)}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        if (lockedBuildings.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                    Text(
                        "Upcoming Milestones",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Research blueprints in the Tech Tree to unlock these facilities.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            items(lockedBuildings, key = { "locked_" + it.id }) { building ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(building.iconEmoji, fontSize = 24.sp, modifier = Modifier.alpha(0.5f))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = building.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                            Text(
                                text = "🔒 Locked Blueprint",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                        Icon(Icons.Default.Lock, contentDescription = "Locked", tint = Color.Gray)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }"""

kt = kt.replace(old_items_end, new_items_end)

if "import androidx.compose.ui.draw.alpha" not in kt:
    kt = kt.replace("import androidx.compose.ui.Alignment", "import androidx.compose.ui.Alignment\nimport androidx.compose.ui.draw.alpha")

with open('app/src/main/java/com/example/ui/screens/FacilitiesTab.kt', 'w') as f:
    f.write(kt)

