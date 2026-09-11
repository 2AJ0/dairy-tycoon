import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/MainGameScreen.kt')

old_call = """                        CorporateWarfareScreen(
                            gameState = gameState,
                            onLaunchSabotage = { rivalId -> viewModel.launchCorporateSabotage(rivalId) },
                            onBackClick = { currentDrawerDestination = DrawerDestination.DASHBOARD },
                            onMenuClick = { coroutineScope.launch { drawerState.open() } },
                            modifier = Modifier.padding(innerPadding)
                        )"""

new_call = """                        CorporateWarfareScreen(
                            gameState = gameState,
                            onQueueAttack = { rivalId, type, cost, apCost -> viewModel.queuePlayerCyberAttack(rivalId, type, cost, apCost) },
                            onBackClick = { currentDrawerDestination = DrawerDestination.DASHBOARD },
                            onMenuClick = { coroutineScope.launch { drawerState.open() } },
                            modifier = Modifier.padding(innerPadding)
                        )"""

kt = kt.replace(old_call, new_call)

with open('app/src/main/java/com/example/ui/screens/MainGameScreen.kt', 'w') as f:
    f.write(kt)
