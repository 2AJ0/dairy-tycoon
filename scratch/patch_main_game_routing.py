import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/MainGameScreen.kt')

old_routing = """                DrawerDestination.RESEARCH -> {
                    val techNodesProducts by viewModel.techNodesProducts.collectAsStateWithLifecycle()
                    val techNodesIndustry by viewModel.techNodesIndustry.collectAsStateWithLifecycle()
                    val techNodesCompany by viewModel.techNodesCompany.collectAsStateWithLifecycle()

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) { innerPadding ->
                        ResearchHubScreen(
                            gameState = gameState,
                            techNodesProducts = techNodesProducts,
                            techNodesIndustry = techNodesIndustry,
                            techNodesCompany = techNodesCompany,
                            onUnlockTech = { techId -> viewModel.unlockTechnology(techId) },
                            onUpgradeSkill = { skillType -> viewModel.upgradePlayerSkill(skillType) },
                            onBackClick = { currentDrawerDestination = DrawerDestination.DASHBOARD },
                            onMenuClick = { coroutineScope.launch { drawerState.open() } },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }"""

new_routing = """                DrawerDestination.RESEARCH -> {
                    if (gameState.isStandardTreeComplete) {
                        AdvancedLabScreen(
                            gameState = gameState,
                            onUnlockAnomaly = { bpId -> viewModel.unlockAnomalyBlueprint(bpId) },
                            onBackClick = { currentDrawerDestination = DrawerDestination.DASHBOARD },
                            onMenuClick = { coroutineScope.launch { drawerState.open() } }
                        )
                    } else {
                        val techNodesProducts by viewModel.techNodesProducts.collectAsStateWithLifecycle()
                        val techNodesIndustry by viewModel.techNodesIndustry.collectAsStateWithLifecycle()
                        val techNodesCompany by viewModel.techNodesCompany.collectAsStateWithLifecycle()

                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                        ) { innerPadding ->
                            ResearchHubScreen(
                                gameState = gameState,
                                techNodesProducts = techNodesProducts,
                                techNodesIndustry = techNodesIndustry,
                                techNodesCompany = techNodesCompany,
                                onUnlockTech = { techId -> viewModel.unlockTechnology(techId) },
                                onUpgradeSkill = { skillType -> viewModel.upgradePlayerSkill(skillType) },
                                onBackClick = { currentDrawerDestination = DrawerDestination.DASHBOARD },
                                onMenuClick = { coroutineScope.launch { drawerState.open() } },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }"""

kt = kt.replace(old_routing, new_routing)

with open('app/src/main/java/com/example/ui/screens/MainGameScreen.kt', 'w') as f:
    f.write(kt)
