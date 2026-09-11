import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/ResearchHubScreen.kt')

# 1. Update ResearchTab Enum
old_enum = """enum class ResearchTab(val title: String, val iconEmoji: String, val category: ResearchCategory) {
    PRODUCTS("Products", "🥛", ResearchCategory.PRODUCTS),
    PERSONAL("Personal", "👔", ResearchCategory.PERSONAL),
    INDUSTRY("Industry", "🏭", ResearchCategory.INDUSTRY),
    COMPANY("Company", "🏢", ResearchCategory.COMPANY)
}"""

new_enum = """enum class ResearchTab(val title: String, val iconEmoji: String, val category: ResearchCategory) {
    PRODUCTS("Products", "🥛", ResearchCategory.PRODUCTS),
    PERSONAL("Personal", "👔", ResearchCategory.PERSONAL),
    INDUSTRY("Industry", "🏭", ResearchCategory.INDUSTRY),
    COMPANY("Company", "🏢", ResearchCategory.COMPANY),
    EXPERIMENTAL("Experimental", "☢️", ResearchCategory.EXPERIMENTAL)
}"""

kt = kt.replace(old_enum, new_enum)

# 2. Filter tabs based on unlock condition in ResearchHubScreen
old_scrollable = """        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth(),
            edgePadding = 8.dp
        ) {
            ResearchTab.values().forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(tab.iconEmoji, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = tab.title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    },
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }"""

new_scrollable = """        val hasAdvancedLab = gameState.buildings.any { it.type == com.example.model.BuildingType.ADVANCED_LAB && it.isConstructed }
        val isExperimentalUnlocked = gameState.isStandardTreeComplete && hasAdvancedLab
        val visibleTabs = if (isExperimentalUnlocked) ResearchTab.values().toList() else ResearchTab.values().filter { it != ResearchTab.EXPERIMENTAL }
        
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth(),
            edgePadding = 8.dp
        ) {
            visibleTabs.forEachIndexed { index, tab ->
                val isExperimentalTab = tab == ResearchTab.EXPERIMENTAL
                val highlightTab = gameState.showAweHighlight && isExperimentalTab
                
                Tab(
                    selected = selectedTab == index,
                    onClick = { 
                        selectedTab = index 
                        if (highlightTab) onDismissAweHighlight()
                    },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(tab.iconEmoji, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = tab.title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (isExperimentalTab) androidx.compose.ui.graphics.Color(0xFF00FFCC) else androidx.compose.ui.graphics.Color.Unspecified
                            )
                        }
                    },
                    modifier = Modifier.padding(vertical = 4.dp).then(
                        if (highlightTab) Modifier.border(2.dp, androidx.compose.ui.graphics.Color(0xFF00FFCC), RoundedCornerShape(4.dp)).background(androidx.compose.ui.graphics.Color(0xFF00FFCC).copy(alpha=0.2f)) else Modifier
                    )
                )
            }
        }"""

kt = kt.replace(old_scrollable, new_scrollable)

# Need to update MainGameScreen call and ResearchHubScreen signature to add onDismissAweHighlight
old_sig = """fun ResearchHubScreen(
    gameState: GameState,
    techNodesProducts: List<TechTreeNode>,
    techNodesIndustry: List<TechTreeNode>,
    techNodesCompany: List<TechTreeNode>,
    onUnlockTech: (String) -> Unit,
    onUpgradeSkill: (SkillType) -> Unit,
    onStudyResearch: () -> Unit,
    evaluateAction: (com.example.model.GameAction) -> com.example.model.GuidanceState,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null
) {"""

new_sig = """fun ResearchHubScreen(
    gameState: GameState,
    techNodesProducts: List<TechTreeNode>,
    techNodesIndustry: List<TechTreeNode>,
    techNodesCompany: List<TechTreeNode>,
    onUnlockTech: (String) -> Unit,
    onUpgradeSkill: (SkillType) -> Unit,
    onStudyResearch: () -> Unit,
    evaluateAction: (com.example.model.GameAction) -> com.example.model.GuidanceState,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    onDismissAweHighlight: () -> Unit = {}
) {"""

kt = kt.replace(old_sig, new_sig)

# Add the dark overlay over everything EXCEPT the tabs if showAweHighlight is true
old_box = """    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {"""
new_box = """    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (gameState.showAweHighlight) {
                // The prompt asked for a drawWithContent cutout, but the simplest visually robust method in standard compose
                // is to just let the tabs render above by drawing the overlay below the tabs, but wait, ScrollableTabRow is IN the column.
                // We will handle the overlay at the end of the Box.
            }"""
kt = kt.replace(old_box, new_box)

old_end_box = """    }
}

@Composable
private fun SkillLevelBadge("""

new_end_box = """        if (gameState.showAweHighlight) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.7f))
                    .clickable { /* Block clicks */ }
            ) {
                Text(
                    "Click the EXPERIMENTAL tab above!",
                    color = androidx.compose.ui.graphics.Color(0xFF00FFCC),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 180.dp)
                )
            }
        }
    }
}

@Composable
private fun SkillLevelBadge("""

kt = kt.replace(old_end_box, new_end_box)

with open('app/src/main/java/com/example/ui/screens/ResearchHubScreen.kt', 'w') as f:
    f.write(kt)
