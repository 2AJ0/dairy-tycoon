import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/MainGameScreen.kt')

if "import com.example.ui.screens.VictoryScreen" not in kt:
    kt = kt.replace("import com.example.ui.screens.CorporateWarfareScreen", "import com.example.ui.screens.CorporateWarfareScreen\nimport com.example.ui.screens.VictoryScreen")

old_gameover = """            if (gameState.isGameOver) {
                GameOverDialog(
                    reason = gameState.gameOverReason,
                    onRestart = {
                        viewModel.restartGame()
                        currentDrawerDestination = DrawerDestination.DASHBOARD
                    }
                )
            }"""

new_gameover = """            if (gameState.gamePhase == com.example.model.GamePhase.COMPLETED) {
                VictoryScreen(
                    gameState = gameState,
                    onReturnToMenu = {
                        viewModel.restartGame()
                        currentDrawerDestination = DrawerDestination.DASHBOARD
                    },
                    onContinueSandbox = {
                        viewModel.continueInSandbox()
                    }
                )
            } else if (gameState.isGameOver) {
                GameOverDialog(
                    reason = gameState.gameOverReason,
                    onRestart = {
                        viewModel.restartGame()
                        currentDrawerDestination = DrawerDestination.DASHBOARD
                    }
                )
            }"""

kt = kt.replace(old_gameover, new_gameover)

with open('app/src/main/java/com/example/ui/screens/MainGameScreen.kt', 'w') as f:
    f.write(kt)
