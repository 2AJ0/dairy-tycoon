import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/ResearchHubScreen.kt')

kt = kt.replace(
    "modifier = Modifier\n                    .fillMaxSize()\n                    .background(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.7f))",
    "modifier = Modifier\n                    .fillMaxSize()\n                    .padding(top = 130.dp)\n                    .background(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.8f))"
)

kt = kt.replace(
    "modifier = Modifier.align(Alignment.TopCenter).padding(top = 180.dp)",
    "modifier = Modifier.align(Alignment.TopCenter).padding(top = 24.dp)"
)

with open('app/src/main/java/com/example/ui/screens/ResearchHubScreen.kt', 'w') as f:
    f.write(kt)
