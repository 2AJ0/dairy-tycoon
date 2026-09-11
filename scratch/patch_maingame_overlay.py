import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/MainGameScreen.kt')

old_code = """            if (gameState.showAweDialogue) {"""

new_code = """            if (showInboxModal) {
                com.example.ui.components.InboxOverlay(
                    gameState = gameState,
                    onDismiss = { showInboxModal = false },
                    onMarkAsRead = { viewModel.markStoryEventAsRead(it) }
                )
            }
            
            if (gameState.showAweDialogue) {"""

if old_code in kt:
    kt = kt.replace(old_code, new_code)
    with open('app/src/main/java/com/example/ui/screens/MainGameScreen.kt', 'w') as f:
        f.write(kt)
    print("Patched MainGameScreen successfully")
else:
    print("Could not find the hook location")
