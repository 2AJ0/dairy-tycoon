import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/MarketTab.kt')

if "import androidx.compose.material3.HorizontalDivider" not in kt:
    kt = kt.replace("import androidx.compose.material3.Icon", "import androidx.compose.material3.Icon\nimport androidx.compose.material3.HorizontalDivider")

with open('app/src/main/java/com/example/ui/screens/MarketTab.kt', 'w') as f:
    f.write(kt)
