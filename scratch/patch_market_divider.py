import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/MarketTab.kt')

kt = kt.replace("Divider()", "HorizontalDivider()")

with open('app/src/main/java/com/example/ui/screens/MarketTab.kt', 'w') as f:
    f.write(kt)
