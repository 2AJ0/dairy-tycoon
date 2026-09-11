import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/MainGameScreen.kt')

# Add SUBSIDIARIES to enum
old_enum = """    ENTERPRISE_STATS("Enterprise Stats", "Lifetime Analytics & Records", "📊", Icons.Default.BarChart, "drawer_stats"),"""

new_enum = """    SUBSIDIARIES("Subsidiaries", "Autonomous Sub-Corps", "🏢", Icons.Default.BusinessCenter, "drawer_subsidiaries"),
    ENTERPRISE_STATS("Enterprise Stats", "Lifetime Analytics & Records", "📊", Icons.Default.BarChart, "drawer_stats"),"""

kt = kt.replace(old_enum, new_enum)

# Add route handling
old_route = """                DrawerDestination.RESEARCH -> {"""

new_route = """                DrawerDestination.SUBSIDIARIES -> {
                    com.example.ui.screens.SubsidiariesScreen(
                        gameState = gameState,
                        onUpdateDividend = { subId, sliderVal -> viewModel.updateSubsidiaryDividend(subId, sliderVal) },
                        onBackClick = { currentDrawerDestination = DrawerDestination.DASHBOARD },
                        onMenuClick = { coroutineScope.launch { drawerState.open() } }
                    )
                }
                DrawerDestination.RESEARCH -> {"""

kt = kt.replace(old_route, new_route)

# Also patch FacilitiesTab invocation to pass onSpinOff
old_fac = """                            NavigationTab.FACILITIES -> FacilitiesTab(
                                gameState = gameState,
                                onBuyBuilding = { id -> viewModel.buyBuilding(id) },
                                onUpgradeBuilding = { id -> viewModel.upgradeBuilding(id) },
                                onToggleBuilding = { id -> viewModel.toggleBuilding(id) },
                                onSelectRecipe = { id, recipeIdx -> viewModel.selectRecipe(id, recipeIdx) },
                                onBuyLandExpansion = { viewModel.buyLandExpansion() },
                                onUpdateAllocation = { id, pct -> viewModel.updateBuildingAllocation(id, pct) }
                            )"""

new_fac = """                            NavigationTab.FACILITIES -> FacilitiesTab(
                                gameState = gameState,
                                onBuyBuilding = { id -> viewModel.buyBuilding(id) },
                                onUpgradeBuilding = { id -> viewModel.upgradeBuilding(id) },
                                onToggleBuilding = { id -> viewModel.toggleBuilding(id) },
                                onSelectRecipe = { id, recipeIdx -> viewModel.selectRecipe(id, recipeIdx) },
                                onBuyLandExpansion = { viewModel.buyLandExpansion() },
                                onUpdateAllocation = { id, pct -> viewModel.updateBuildingAllocation(id, pct) },
                                onSpinOff = { id, customName -> viewModel.spinOffFacility(id, customName) }
                            )"""

kt = kt.replace(old_fac, new_fac)

with open('app/src/main/java/com/example/ui/screens/MainGameScreen.kt', 'w') as f:
    f.write(kt)
