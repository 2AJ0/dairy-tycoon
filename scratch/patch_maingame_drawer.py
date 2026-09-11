import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/MainGameScreen.kt')

old_label = """                                    Column {
                                        Text(
                                            text = destination.title,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                        Text(
                                            text = destination.subtitle,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }"""

new_label = """                                    val displayTitle = if (destination == DrawerDestination.RESEARCH && gameState.isStandardTreeComplete) "Advanced Lab" else destination.title
                                    val displaySubtitle = if (destination == DrawerDestination.RESEARCH && gameState.isStandardTreeComplete) "Anomalous Augmentations" else destination.subtitle
                                    
                                    Column {
                                        Text(
                                            text = displayTitle,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (destination == DrawerDestination.RESEARCH && gameState.isStandardTreeComplete) androidx.compose.ui.graphics.Color(0xFF00FFCC) else androidx.compose.ui.graphics.Color.Unspecified
                                        )
                                        Text(
                                            text = displaySubtitle,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }"""

kt = kt.replace(old_label, new_label)

old_emoji = """                                        Text(destination.emoji, fontSize = 20.sp)"""

new_emoji = """                                        val displayEmoji = if (destination == DrawerDestination.RESEARCH && gameState.isStandardTreeComplete) "☢️" else destination.emoji
                                        Text(displayEmoji, fontSize = 20.sp)"""

kt = kt.replace(old_emoji, new_emoji)

# Replace the routing inside the Scaffold for RESEARCH
old_route = """                DrawerDestination.RESEARCH -> {
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
                            onStudyResearch = { viewModel.studyManualResearch() },
                            onBackClick = { currentDrawerDestination = DrawerDestination.DASHBOARD },
                            onMenuClick = { coroutineScope.launch { drawerState.open() } },
                            evaluateAction = { action -> viewModel.evaluateAction(action) },
                            modifier = Modifier.padding(innerPadding),
                            onDismissAweHighlight = { viewModel.dismissAweHighlight() }
                        )
                    }
                }"""

new_route = """                DrawerDestination.RESEARCH -> {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) { innerPadding ->
                        if (gameState.isStandardTreeComplete) {
                            com.example.ui.screens.AdvancedLabScreen(
                                gameState = gameState,
                                onUnlockAnomaly = { bpId -> viewModel.unlockAnomalyBlueprint(bpId) },
                                onMenuClick = { coroutineScope.launch { drawerState.open() } },
                                modifier = Modifier.padding(innerPadding)
                            )
                        } else {
                            val techNodesProducts by viewModel.techNodesProducts.collectAsStateWithLifecycle()
                            val techNodesIndustry by viewModel.techNodesIndustry.collectAsStateWithLifecycle()
                            val techNodesCompany by viewModel.techNodesCompany.collectAsStateWithLifecycle()
                            ResearchHubScreen(
                                gameState = gameState,
                                techNodesProducts = techNodesProducts,
                                techNodesIndustry = techNodesIndustry,
                                techNodesCompany = techNodesCompany,
                                onUnlockTech = { techId -> viewModel.unlockTechnology(techId) },
                                onUpgradeSkill = { skillType -> viewModel.upgradePlayerSkill(skillType) },
                                onStudyResearch = { viewModel.studyManualResearch() },
                                onBackClick = { currentDrawerDestination = DrawerDestination.DASHBOARD },
                                onMenuClick = { coroutineScope.launch { drawerState.open() } },
                                evaluateAction = { action -> viewModel.evaluateAction(action) },
                                modifier = Modifier.padding(innerPadding),
                                onDismissAweHighlight = { viewModel.dismissAweHighlight() }
                            )
                        }
                    }
                }"""

kt = kt.replace(old_route, new_route)

# Now, need to pass installAnomaly to FacilitiesTab
old_fac_call = """                                onUpdateAllocation = { id, pct -> viewModel.updateBuildingAllocation(id, pct) },
                                onSpinOff = { id, customName -> viewModel.spinOffFacility(id, customName) },
                                onBuyFacilityPerk = { facilityId, perkId -> viewModel.buyFacilityPerk(facilityId, perkId) }"""

new_fac_call = """                                onUpdateAllocation = { id, pct -> viewModel.updateBuildingAllocation(id, pct) },
                                onSpinOff = { id, customName -> viewModel.spinOffFacility(id, customName) },
                                onBuyFacilityPerk = { facilityId, perkId -> viewModel.buyFacilityPerk(facilityId, perkId) },
                                onInstallAnomaly = { facilityId, bpId -> viewModel.installAnomaly(facilityId, bpId) }"""

kt = kt.replace(old_fac_call, new_fac_call)

with open('app/src/main/java/com/example/ui/screens/MainGameScreen.kt', 'w') as f:
    f.write(kt)
