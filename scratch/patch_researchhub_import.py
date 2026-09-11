import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/ResearchHubScreen.kt')

if "import androidx.compose.foundation.layout.offset" not in kt:
    kt = kt.replace("import androidx.compose.foundation.layout.padding", "import androidx.compose.foundation.layout.padding\nimport androidx.compose.foundation.layout.offset")

with open('app/src/main/java/com/example/ui/screens/ResearchHubScreen.kt', 'w') as f:
    f.write(kt)
