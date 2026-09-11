import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/ResearchHubScreen.kt')

if "import androidx.compose.foundation.border" not in kt:
    kt = kt.replace("import androidx.compose.foundation.layout.*", "import androidx.compose.foundation.layout.*\nimport androidx.compose.foundation.border")

if "import androidx.compose.foundation.shape.RoundedCornerShape" not in kt:
    kt = kt.replace("import androidx.compose.foundation.layout.*", "import androidx.compose.foundation.layout.*\nimport androidx.compose.foundation.shape.RoundedCornerShape")

if "import androidx.compose.ui.unit.dp" not in kt:
    kt = kt.replace("import androidx.compose.foundation.layout.*", "import androidx.compose.foundation.layout.*\nimport androidx.compose.ui.unit.dp")

with open('app/src/main/java/com/example/ui/screens/ResearchHubScreen.kt', 'w') as f:
    f.write(kt)
