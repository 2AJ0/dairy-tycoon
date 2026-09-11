package com.example.ui.screens

/**
 * ==========================================
 * AI / IDE AGENT INSTRUCTIONS:
 * ==========================================
 * This is a Jetpack Compose UI file for the Dairy Tycoon game.
 * 
 * CORE RULES FOR FIXING ERRORS:
 * 1. UNIDIRECTIONAL DATA FLOW: The UI strictly observes `GameState`. Do not introduce local state for global data.
 * 2. PRESERVE LOGIC: If you encounter unresolved references (e.g., missing imports or renamed variables), fix the import or typo. DO NOT rewrite the composable's structural layout or business logic.
 * 3. LAZYCOLUMNS: We heavily use `.filter` lists mapped to `items()`. Preserve the separation of visible/unlocked items and "Upcoming Milestones".
 * 4. MODIFIERS: Preserve `.testTag`, `.weight(1f)`, and `.clipToBounds()` layout constraints.
 */

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameState
import com.example.model.SkillType
import com.example.model.TechCatalog
import com.example.model.TechTreeNode
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.DairyEmeraldDark
import com.example.ui.theme.DairyEmeraldPrimary
import com.example.ui.theme.DairyGold
import com.example.ui.theme.DairyGoldDark
import com.example.ui.theme.TechCyan

import androidx.compose.material3.ScrollableTabRow
import com.example.model.ResearchCategory

enum class ResearchTab(val title: String, val iconEmoji: String, val category: ResearchCategory) {
    PRODUCTS("Products", "🥛", ResearchCategory.PRODUCTS),
    PERSONAL("Personal", "👔", ResearchCategory.PERSONAL),
    INDUSTRY("Industry", "🏭", ResearchCategory.INDUSTRY),
    COMPANY("Company", "🏢", ResearchCategory.COMPANY),
    EXPERIMENTAL("Experimental", "☢️", ResearchCategory.EXPERIMENTAL)
}

@Composable
fun ResearchHubScreen(
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
    onMenuClick: (() -> Unit)? = null
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("research_hub_screen")
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (onBackClick != null) {
                            IconButton(
                                onClick = onBackClick,
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("skill_map_back_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back to Dashboard"
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                        } else if (onMenuClick != null) {
                            IconButton(
                                onClick = onMenuClick,
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("skill_map_menu_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Open Menu"
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                        }

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    Brush.linearGradient(listOf(TechCyan, DairyEmeraldDark))
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🧬", fontSize = 22.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Skills & Technology Map",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Science Blueprints & Executive Talents",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // RP pill
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = TechCyan.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, TechCyan.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🔬", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${gameState.researchPoints} RP",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = TechCyan,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                
                // Capacity Breakdown
                val personalCapacity = gameState.playerSkills.personalActionCapacity
                val currentPersonalCount = gameState.activeProjects.count { !it.isDedicated }
                val dedicatedCapacity = gameState.ownedLabs
                val currentDedicatedCount = gameState.activeProjects.count { it.isDedicated && it.type == com.example.model.ProjectType.TECH_RESEARCH }
                
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Personal Pool: $currentPersonalCount / $personalCapacity", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text("Labs: $currentDedicatedCount Active, ${(dedicatedCapacity - currentDedicatedCount).coerceAtLeast(0)} Idle", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Study Action bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Need more Research Points?",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    FilledTonalButton(
                        onClick = onStudyResearch,
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = TechCyan.copy(alpha = 0.20f),
                            contentColor = TechCyan
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("skill_map_study_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = "Study",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Study Research (+4 RP)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Tabs
        ScrollableTabRow(
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
                    modifier = Modifier.testTag("research_tab_$index")
                )
            }
        }

        // Tab Content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            val activeTab = ResearchTab.values()[selectedTab]
            when (activeTab.category) {
                ResearchCategory.PERSONAL -> {
                    ExecutiveSkillsList(
                        gameState = gameState,
                        onUpgradeSkill = onUpgradeSkill
                    )
                }
                else -> {
                    val nodes = when (activeTab.category) {
                        ResearchCategory.PRODUCTS -> techNodesProducts
                        ResearchCategory.INDUSTRY -> techNodesIndustry
                        ResearchCategory.COMPANY -> techNodesCompany
                        else -> emptyList()
                    }
                    RdTechsList(
                        gameState = gameState,
                        onUnlockTech = onUnlockTech,
                        evaluateAction = evaluateAction,
                        nodes = nodes
                    )
                }
            }
        }
    }
}

@Composable
private fun RdTechsList(
    gameState: GameState,
    onUnlockTech: (String) -> Unit,
    evaluateAction: (com.example.model.GameAction) -> com.example.model.GuidanceState,
    nodes: List<TechTreeNode>,
    modifier: Modifier = Modifier
) {
    val unlockedIds = gameState.unlockedTechIds.toSet()
    val isLabsBusy = remember(gameState.activeProjects, gameState.playerSkills.multiTaskingLevel, gameState.unlockedTechIds) {
        val currentPersonal = gameState.activeProjects.count { !it.isDedicated }
        val capacity = gameState.playerSkills.personalActionCapacity
        val currentDedicated = gameState.activeProjects.count { it.isDedicated && it.type == com.example.model.ProjectType.TECH_RESEARCH }
        val totalDedicated = gameState.ownedLabs
        currentPersonal >= capacity && currentDedicated >= totalDedicated
    }

    val density = androidx.compose.ui.platform.LocalDensity.current
    val nodeWidthDp = 180.dp
    val nodeHeightDp = 90.dp
    val tierPaddingDp = 64.dp
    val verticalPaddingDp = 32.dp

    val nodeWidthPx = with(density) { nodeWidthDp.toPx() }
    val nodeHeightPx = with(density) { nodeHeightDp.toPx() }
    val tierPaddingPx = with(density) { tierPaddingDp.toPx() }
    val verticalPaddingPx = with(density) { verticalPaddingDp.toPx() }

    val nodePositions = remember(nodes) {
        val tierMap = mutableMapOf<String, Int>()
        fun getTier(nodeId: String): Int {
            if (tierMap.containsKey(nodeId)) return tierMap[nodeId]!!
            val node = nodes.find { it.id == nodeId } ?: return 0
            if (node.parentId == null) {
                tierMap[nodeId] = 0
                return 0
            }
            val t = getTier(node.parentId) + 1
            tierMap[nodeId] = t
            return t
        }

        val nodesByTier = nodes.groupBy { getTier(it.id) }
        val positions = mutableMapOf<String, Offset>()

        for ((tier, tierNodes) in nodesByTier) {
            val sortedNodes = tierNodes.sortedBy { it.gridY }
            sortedNodes.forEachIndexed { index, node ->
                val x = tier * (nodeWidthPx + tierPaddingPx) + 50f
                val y = index * (nodeHeightPx + verticalPaddingPx) + 50f
                positions[node.id] = Offset(x, y)
            }
        }
        positions
    }

    val maxXPx = nodePositions.values.maxOfOrNull { it.x } ?: 0f
    val maxYPx = nodePositions.values.maxOfOrNull { it.y } ?: 0f
    val contentWidthDp = with(density) { (maxXPx + nodeWidthPx + 100f).toDp() }
    val contentHeightDp = with(density) { (maxYPx + nodeHeightPx + 100f).toDp() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clipToBounds()
            .horizontalScroll(rememberScrollState())
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(32.dp)
    ) {
        Box(
            modifier = Modifier.size(contentWidthDp, contentHeightDp)
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                for (node in nodes) {
                    val isResearched = unlockedIds.contains(node.id) || node.rpCost == 0
                    if (node.parentId != null) {
                        val parentPos = nodePositions[node.parentId]
                        val nodePos = nodePositions[node.id]
                        if (parentPos != null && nodePos != null) {
                            val startX = parentPos.x + nodeWidthPx / 2
                            val startY = parentPos.y + nodeHeightPx / 2
                            val endX = nodePos.x + nodeWidthPx / 2
                            val endY = nodePos.y + nodeHeightPx / 2

                            val isBranchResearched = isResearched
                            drawLine(
                                color = if (isBranchResearched) Color(0xFF00C853) else Color.Gray.copy(alpha = 0.5f),
                                start = Offset(startX, startY),
                                end = Offset(endX, endY),
                                strokeWidth = if (isBranchResearched) 6f else 3f,
                                pathEffect = if (!isBranchResearched) PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f) else null
                            )
                        }
                    }
                }
            }

            nodes.forEach { node ->
                val isResearched = unlockedIds.contains(node.id) || node.rpCost == 0
                val isLocked = node.parentId != null && !unlockedIds.contains(node.parentId)
                val pos = nodePositions[node.id] ?: Offset.Zero

                val xDp = with(density) { pos.x.toDp() }
                val yDp = with(density) { pos.y.toDp() }

                Box(
                    modifier = Modifier.offset(x = xDp, y = yDp)
                ) {
                    TechNodeCard(
                        node = node,
                        isResearched = isResearched,
                        isLocked = isLocked,
                        canAfford = gameState.researchPoints >= node.rpCost,
                        isLabsBusy = isLabsBusy,
                        onResearchClick = { onUnlockTech(node.id) },
                        evaluateAction = evaluateAction,
                        modifier = Modifier.width(nodeWidthDp).height(nodeHeightDp)
                    )
                }
            }
        }
    }
}

@Composable
fun TechNodeCard(
    node: TechTreeNode,
    isResearched: Boolean,
    isLocked: Boolean,
    canAfford: Boolean,
    isLabsBusy: Boolean,
    onResearchClick: () -> Unit,
    evaluateAction: (com.example.model.GameAction) -> com.example.model.GuidanceState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable(enabled = !isResearched && canAfford && !isLabsBusy, onClick = onResearchClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isResearched) MaterialTheme.colorScheme.primaryContainer else if (!isResearched && !isLabsBusy && canAfford) Color.DarkGray.copy(alpha = 0.85f) else Color.DarkGray.copy(alpha = 0.4f)
        ),
        border = BorderStroke(
            if (isResearched) 2.dp else 1.dp,
            if (isResearched) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isResearched) {
                Text(
                    text = "${node.iconEmoji} ${node.name}",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 2,
                    lineHeight = 14.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Researched",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Text(
                    text = node.name,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color = Color.LightGray.copy(alpha = 0.6f),
                    maxLines = 2,
                    lineHeight = 14.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val action = com.example.model.GameAction.Research(node.id)
                    val guidance = evaluateAction(action)
                    com.example.ui.components.GuidanceBadge(state = guidance)
                    Spacer(modifier = Modifier.width(4.dp))
                    if (isLabsBusy) {
                        Text(
                            text = "Slots Busy",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            fontSize = 10.sp
                        )
                    } else {
                        Text(
                            text = "Required: ${node.rpCost} RP",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (canAfford) Color.White else MaterialTheme.colorScheme.error,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExecutiveSkillsList(
    gameState: GameState,
    onUpgradeSkill: (SkillType) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(SkillType.values()) { skill ->
            val currentLevel = gameState.playerSkills.getLevel(skill)
            val isMax = currentLevel >= skill.maxLevel
            val cost = if (!isMax) skill.costForLevel(currentLevel) else 0
            val canAfford = gameState.researchPoints >= cost

            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(
                    1.dp,
                    if (isMax) DairyGold.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outlineVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(skill.iconEmoji, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = skill.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Tier Level $currentLevel / ${skill.maxLevel}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DairyGoldDark
                                )
                            }
                        }

                        if (isMax) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = DairyGold.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "MAXED OUT",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DairyGoldDark,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 10.sp
                                )
                            }
                        } else {
                            Button(
                                onClick = { onUpgradeSkill(skill) },
                                enabled = canAfford,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = DairyEmeraldPrimary,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Upgrade ($cost RP)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = skill.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "CURRENT BONUS:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = when (skill) {
                                    SkillType.STAMINA -> "+${gameState.playerSkills.staminaLevel} daily actions capacity (${gameState.playerSkills.maxDailyActions} actions/day)"
                                    SkillType.HUSTLER -> "+$${String.format("%.0f", gameState.playerSkills.hustlerCashBonus)} extra cash per manual overtime shift"
                                    SkillType.EFFICIENCY_EXPERT -> "-${String.format("%.0f", gameState.playerSkills.maintenanceDiscountPercent * 100)}% daily facility maintenance cost reduction"
                                    SkillType.SILVER_TONGUE -> "+${String.format("%.0f", (gameState.playerSkills.silverTongueRepBonusMultiplier - 1.0) * 100)}% extra selling price gain from enterprise reputation"
                                    SkillType.BOVINE_GENETICS -> if (currentLevel > 0) "Active: +20% raw milk quality & +2 pasture yield" else "Inactive"
                                    SkillType.COLD_CHAIN_LOGISTICS -> if (currentLevel > 0) "Active: +2 days shelf life enterprise-wide" else "Inactive"
                                    SkillType.MULTI_TASKING -> "+${gameState.playerSkills.multiTaskingLevel} concurrent project slots"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = DairyEmeraldDark
                            )
                        }
                    }
                }
            }
        }
    }
}
