import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/MainGameScreen.kt')

old_call = """                        AdvancedLabScreen(
                            gameState = gameState,
                            onUnlockAnomaly = { bpId -> viewModel.unlockAnomalyBlueprint(bpId) },
                            onBackClick = { currentDrawerDestination = DrawerDestination.DASHBOARD },
                            onMenuClick = { coroutineScope.launch { drawerState.open() } }
                        )"""

new_call = """                        AdvancedLabScreen(
                            gameState = gameState,
                            onUnlockAnomaly = { bpId -> viewModel.unlockAnomalyBlueprint(bpId) },
                            onMenuClick = { coroutineScope.launch { drawerState.open() } }
                        )"""

kt = kt.replace(old_call, new_call)

with open('app/src/main/java/com/example/ui/screens/MainGameScreen.kt', 'w') as f:
    f.write(kt)
