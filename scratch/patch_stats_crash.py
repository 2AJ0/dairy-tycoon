import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/StatsDashboardScreen.kt')

old_line = 'StatRow(label = "Total Inventory Spoiled (FIFO Loss)", value = "${String.format("%,.0f", stats.totalSpoiledUnits)} Units", valueColor = if (stats.totalSpoiledUnits > 0) BearishRed else BullishGreen)'
new_line = 'StatRow(label = "Total Inventory Spoiled (FIFO Loss)", value = "${String.format("%,d", stats.totalSpoiledUnits)} Units", valueColor = if (stats.totalSpoiledUnits > 0) BearishRed else BullishGreen)'

if old_line in kt:
    kt = kt.replace(old_line, new_line)
else:
    print("Could not find the target line to replace!")

with open('app/src/main/java/com/example/ui/screens/StatsDashboardScreen.kt', 'w') as f:
    f.write(kt)
