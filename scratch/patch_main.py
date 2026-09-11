import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/MainActivity.kt')

old_imports = "import com.example.viewmodel.GameViewModel"
new_imports = "import com.example.viewmodel.GameViewModel\nimport androidx.lifecycle.compose.collectAsStateWithLifecycle\nimport com.example.model.GamePhase"

kt = kt.replace(old_imports, new_imports)

old_setContent = """        setContent {
            MyApplicationTheme {"""

new_setContent = """        setContent {
            val gameState by gameViewModel.gameState.collectAsStateWithLifecycle()
            val isCorporate = gameState.gamePhase == GamePhase.CORPORATE

            MyApplicationTheme(darkTheme = isCorporate) {"""

kt = kt.replace(old_setContent, new_setContent)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(kt)
