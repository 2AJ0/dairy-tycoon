import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/MainGameScreen.kt')

awe_overlay = """
            if (gameState.showAweDialogue) {
                com.example.ui.components.MentorDialogOverlay(
                    onDismiss = { viewModel.advanceAweTutorial() }
                )
            }
"""

kt = kt.replace(
    "if (gameState.gamePhase == com.example.model.GamePhase.COMPLETED) {",
    awe_overlay + "\n            if (gameState.gamePhase == com.example.model.GamePhase.COMPLETED) {"
)

with open('app/src/main/java/com/example/ui/screens/MainGameScreen.kt', 'w') as f:
    f.write(kt)
