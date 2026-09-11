import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/StatsDashboardScreen.kt')

old_line = 'StatRow(label = "Subsidiaries Controlled (51%+)", value = "${gameState.subsidiaryCompanyIds.size} / 4 Megacorps", valueColor = if (gameState.subsidiaryCompanyIds.size == 4) DairyGoldDark else TechCyan)'
new_line = 'val subSize = gameState.subsidiaryCompanyIds?.size ?: 0\n                    StatRow(label = "Subsidiaries Controlled (51%+)", value = "${subSize} / 4 Megacorps", valueColor = if (subSize == 4) DairyGoldDark else TechCyan)'

if old_line in kt:
    kt = kt.replace(old_line, new_line)
else:
    print("Could not find line!")

with open('app/src/main/java/com/example/ui/screens/StatsDashboardScreen.kt', 'w') as f:
    f.write(kt)
