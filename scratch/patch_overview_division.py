import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/OverviewTab.kt')

old_progress = """val progress = (contract.fulfilledQuantity.toFloat() / contract.targetTotalQuantity.toFloat()).coerceIn(0f, 1f)"""
new_progress = """val progress = (contract.fulfilledQuantity.toFloat() / contract.targetTotalQuantity.toFloat().coerceAtLeast(1f)).coerceIn(0f, 1f)"""

kt = kt.replace(old_progress, new_progress)

with open('app/src/main/java/com/example/ui/screens/OverviewTab.kt', 'w') as f:
    f.write(kt)
