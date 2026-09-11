import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/MainGameScreen.kt')

old_call = """                        SettingsScreen(
                            gameState = gameState,
                            soundEffectsEnabled = soundEffectsEnabled,
                            newsAlertsEnabled = newsAlertsEnabled,
                            onToggleSound = { viewModel.toggleSoundEffects(it) },
                            onToggleNews = { viewModel.toggleNewsAlerts(it) },
                            onSaveGame = { viewModel.saveGame() },
                            onExitToMainMenu = onNavigateToMainMenu,
                            onRestartGame = {"""

new_call = """                        SettingsScreen(
                            gameState = gameState,
                            soundEffectsEnabled = soundEffectsEnabled,
                            newsAlertsEnabled = newsAlertsEnabled,
                            onToggleSound = { viewModel.toggleSoundEffects(it) },
                            onToggleNews = { viewModel.toggleNewsAlerts(it) },
                            onSaveGame = { viewModel.saveGame() },
                            onRenameEmpire = { viewModel.updateEmpireName(it) },
                            onExitToMainMenu = onNavigateToMainMenu,
                            onRestartGame = {"""

kt = kt.replace(old_call, new_call)

with open('app/src/main/java/com/example/ui/screens/MainGameScreen.kt', 'w') as f:
    f.write(kt)
