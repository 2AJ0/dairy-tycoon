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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Badge
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import com.example.ui.components.bounceClick
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.CrossroadsDialog
import com.example.ui.components.DailyDairyChronicleDialog
import com.example.ui.components.DailyReportDialog
import com.example.ui.components.EndgameDialog
import com.example.ui.components.GameOverDialog
import com.example.ui.components.TopStatsHeader
import com.example.ui.theme.BearishRed
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.DairyEmeraldDark
import com.example.ui.theme.DairyEmeraldLight
import com.example.ui.theme.DairyEmeraldPrimary
import com.example.ui.theme.DairyGold
import com.example.ui.theme.DairyGoldDark
import com.example.ui.theme.TechCyan
import com.example.ui.screens.B2BContractsScreen
import com.example.ui.screens.CorporateWarfareScreen
import com.example.ui.screens.VictoryScreen
import com.example.viewmodel.GameViewModel
import com.example.model.RivalCatalog
import kotlinx.coroutines.launch

enum class NavigationTab(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val testTag: String) {
    OVERVIEW("Overview", Icons.Default.Dashboard, "tab_overview"),
    INVENTORY("Inventory", Icons.Default.Inventory2, "tab_inventory"),
    MARKET("Market", Icons.Default.ShowChart, "tab_market"),
    FACILITIES("Facilities", Icons.Default.Apartment, "tab_facilities"),
    BANK("The Bank", Icons.Default.AccountBalance, "tab_bank")
}

enum class DrawerDestination(
    val title: String,
    val subtitle: String,
    val emoji: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val testTag: String,
    val isHidden: Boolean = false
) {
    DASHBOARD("Dashboard", "Operations & Facilities", "🐄", Icons.Default.Dashboard, "drawer_dashboard"),
    CORPORATE_RELATIONS("Corporate Relations", "Rival AI & B2B Contracts", "🤝", Icons.Default.Apartment, "drawer_corporate_relations"),
    CORPORATE_WARFARE("Corporate Warfare", "Sabotage & Defense", "⚔️", Icons.Default.Security, "drawer_corporate_warfare"),
    RESEARCH("Research Hub", "Tech & Talents", "🔬", Icons.Default.Science, "drawer_research"),
    ACHIEVEMENTS("Achievements", "Hall of Fame & Trophies", "🏆", Icons.Default.EmojiEvents, "drawer_achievements"),
    STOCK_MARKET("Stock Market", "Wall Street Dairy Exchange", "📈", Icons.Default.ShowChart, "drawer_stock_market"),
    SUBSIDIARIES("Subsidiaries", "Autonomous Sub-Corps", "🏢", Icons.Default.BusinessCenter, "drawer_subsidiaries"),
    ENTERPRISE_STATS("Enterprise Stats", "Lifetime Analytics & Records", "📊", Icons.Default.BarChart, "drawer_stats"),
    MENTOR_NETWORK("Mentor Network", "Advisors & Passive Buffs", "💡", Icons.Default.ChatBubble, "drawer_mentor_network"),
    MENTOR_CHAT("Mentor Chat", "Active Conversation", "💬", Icons.Default.ChatBubble, "drawer_mentor_chat", isHidden = true),
    EXECUTIVE_BOARDROOM("Boardroom", "C-Suite & Personnel", "👔", Icons.Default.People, "drawer_boardroom"),
    SETTINGS("Settings", "Executive Operations & Reset", "⚙️", Icons.Default.Settings, "drawer_settings")
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun MainGameScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier,
    onNavigateToMainMenu: () -> Unit = {}
) {
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()
    val showDailyReport by viewModel.showDailyReportDialog.collectAsStateWithLifecycle()
    val showNewsChronicle by viewModel.showNewsChronicleDialog.collectAsStateWithLifecycle()
    val showEndgameDialog by viewModel.showEndgameDialog.collectAsStateWithLifecycle()
    val showMilestoneScreen by viewModel.showMilestoneScreen.collectAsStateWithLifecycle()
    val showCrossroadsDialog by viewModel.showCrossroadsDialog.collectAsStateWithLifecycle()
    val snackBarMessage by viewModel.snackBarMessage.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) }
    var showReputationSheet by remember { mutableStateOf(false) }
    var showInboxModal by remember { mutableStateOf(false) }
    var currentDrawerDestination by remember { mutableStateOf(DrawerDestination.DASHBOARD) }

    var activeChatMentorId by remember { mutableStateOf<String?>(null) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(snackBarMessage) {
        snackBarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackBar()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(320.dp)
                    .fillMaxHeight()
                    .testTag("navigation_drawer"),
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerTonalElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // Drawer Header Banner
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, DairyEmeraldPrimary.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            Brush.linearGradient(
                                                listOf(DairyEmeraldPrimary, DairyEmeraldDark)
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("🥛", fontSize = 24.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "DAIRY TYCOON",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.2.sp,
                                        color = DairyEmeraldPrimary
                                    )
                                    Text(
                                        text = "Executive Operations Hub",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(10.dp))

                            // Stats Snapshot
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "DAY ${gameState.day}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = DairyGold
                                    )
                                    Text(
                                        text = "Cash: $${String.format("%,.0f", gameState.cash)}",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = BullishGreen
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "RP: ${gameState.researchPoints}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = TechCyan
                                    )
                                    Text(
                                        text = "NW: $${String.format("%,.0f", gameState.netWorth)}",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "ENTERPRISE MODULES",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                    )

                    // Navigation Items
                    DrawerDestination.values().filter { destination ->
                        if (destination == DrawerDestination.MENTOR_CHAT) return@filter false
                        if (destination == DrawerDestination.EXECUTIVE_BOARDROOM && !gameState.unlockedFeatures.isBoardroomUnlocked) return@filter false
                        if (destination == DrawerDestination.STOCK_MARKET && !gameState.unlockedFeatures.isStockMarketUnlocked) return@filter false
                        if (destination == DrawerDestination.ENTERPRISE_STATS || destination == DrawerDestination.SETTINGS) return@filter false
                        true
                    }.forEach { destination ->
                        val isSelected = currentDrawerDestination == destination
                        NavigationDrawerItem(
                            label = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val displayTitle = if (destination == DrawerDestination.RESEARCH && gameState.isStandardTreeComplete) "Advanced Lab" else destination.title
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
                                    }

                                    // Badges for specific items
                                    when (destination) {
                                        DrawerDestination.CORPORATE_RELATIONS -> {
                                            val pendingCount = gameState.pendingContractOffers.size
                                            val activeCount = gameState.activeContracts.size
                                            if (pendingCount > 0) {
                                                Surface(
                                                    shape = RoundedCornerShape(10.dp),
                                                    color = DairyEmeraldPrimary.copy(alpha = 0.2f),
                                                    border = BorderStroke(1.dp, DairyEmeraldPrimary.copy(alpha = 0.5f))
                                                ) {
                                                    Text(
                                                        text = "$pendingCount RFP",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        fontWeight = FontWeight.Bold,
                                                        color = DairyEmeraldLight,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                            } else if (activeCount > 0) {
                                                Surface(
                                                    shape = RoundedCornerShape(10.dp),
                                                    color = TechCyan.copy(alpha = 0.2f),
                                                    border = BorderStroke(1.dp, TechCyan.copy(alpha = 0.5f))
                                                ) {
                                                    Text(
                                                        text = "$activeCount Active",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        fontWeight = FontWeight.Bold,
                                                        color = TechCyan,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                        }
                                        DrawerDestination.ACHIEVEMENTS -> {
                                            val unlocked = gameState.achievements.count { it.isUnlocked }
                                            val total = gameState.achievements.size
                                            Surface(
                                                shape = RoundedCornerShape(10.dp),
                                                color = DairyGold.copy(alpha = 0.2f),
                                                border = BorderStroke(1.dp, DairyGold.copy(alpha = 0.5f))
                                            ) {
                                                Text(
                                                    text = "$unlocked/$total",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = DairyGoldDark,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        DrawerDestination.STOCK_MARKET -> {
                                            val subCount = gameState.subsidiaryCompanyIds.size
                                            Surface(
                                                shape = RoundedCornerShape(10.dp),
                                                color = if (subCount == 4) DairyGold.copy(alpha = 0.2f) else DairyEmeraldPrimary.copy(alpha = 0.2f),
                                                border = BorderStroke(1.dp, if (subCount == 4) DairyGold else DairyEmeraldPrimary.copy(alpha = 0.5f))
                                            ) {
                                                Text(
                                                    text = if (subCount == 4) "🏆 Monopoly" else "🏢 $subCount/4",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 10.sp,
                                                    color = if (subCount == 4) DairyGoldDark else DairyEmeraldLight,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        else -> {}
                                    }
                                }
                            },
                            icon = {
                                val isNewFeature = when (destination) {
                                    DrawerDestination.EXECUTIVE_BOARDROOM -> gameState.unlockedFeatures.isBoardroomNew
                                    DrawerDestination.STOCK_MARKET -> gameState.unlockedFeatures.isStockMarketNew
                                    else -> false
                                }
                                if (isNewFeature) {
                                    val infiniteTransition = rememberInfiniteTransition()
                                    val scale by infiniteTransition.animateFloat(
                                        initialValue = 1f,
                                        targetValue = 1.8f,
                                        animationSpec = infiniteRepeatable(
                                            animation = tween(1000, easing = LinearOutSlowInEasing),
                                            repeatMode = RepeatMode.Restart
                                        )
                                    )
                                    val alpha by infiniteTransition.animateFloat(
                                        initialValue = 1f,
                                        targetValue = 0f,
                                        animationSpec = infiniteRepeatable(
                                            animation = tween(1000, easing = LinearOutSlowInEasing),
                                            repeatMode = RepeatMode.Restart
                                        )
                                    )
                                    
                                    BadgedBox(
                                        badge = {
                                            Box(
                                                modifier = Modifier
                                                    .size(10.dp)
                                                    .clip(CircleShape)
                                                    .background(MaterialTheme.colorScheme.error)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .graphicsLayer(
                                                            scaleX = scale,
                                                            scaleY = scale,
                                                            alpha = alpha
                                                        )
                                                        .background(MaterialTheme.colorScheme.error, CircleShape)
                                                )
                                            }
                                        }
                                    ) {
                                        val displayEmoji = if (destination == DrawerDestination.RESEARCH && gameState.isStandardTreeComplete) "☢️" else destination.emoji
                                        Text(displayEmoji, fontSize = 20.sp)
                                    }
                                } else {
                                    Text(destination.emoji, fontSize = 20.sp)
                                }
                            },
                            selected = isSelected,
                            onClick = {
                                coroutineScope.launch { drawerState.close() }
                                currentDrawerDestination = destination
                                viewModel.markFeatureAsSeen(destination)
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = DairyEmeraldPrimary.copy(alpha = 0.15f),
                                selectedTextColor = DairyEmeraldPrimary,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .testTag(destination.testTag)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "UTILITIES",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                    )

                    // Stats, Settings, Exit
                    listOf(
                        DrawerDestination.ENTERPRISE_STATS,
                        DrawerDestination.SETTINGS
                    ).forEach { destination ->
                        val isSelected = currentDrawerDestination == destination
                        NavigationDrawerItem(
                            label = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val displayTitle = if (destination == DrawerDestination.RESEARCH && gameState.isStandardTreeComplete) "Advanced Lab" else destination.title
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
                                    }
                                }
                            },
                            icon = {
                                Text(destination.emoji, fontSize = 20.sp)
                            },
                            selected = isSelected,
                            onClick = {
                                coroutineScope.launch { drawerState.close() }
                                currentDrawerDestination = destination
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = DairyEmeraldPrimary.copy(alpha = 0.15f),
                                selectedTextColor = DairyEmeraldPrimary,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .testTag(destination.testTag)
                        )
                    }

                    // Exit Item
                    NavigationDrawerItem(
                        label = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Exit",
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "Close Application",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        icon = {
                            Text("🚪", fontSize = 20.sp)
                        },
                        selected = false,
                        onClick = {
                            (context as? android.app.Activity)?.finish()
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedTextColor = MaterialTheme.colorScheme.error
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .testTag("drawer_exit")
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Corporate Bottom Tag
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Bovine Capitalism v2.4",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Solvent & Producing 🐄",
                                style = MaterialTheme.typography.labelSmall,
                                color = DairyEmeraldPrimary,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            when (currentDrawerDestination) {
                DrawerDestination.DASHBOARD -> {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            TopStatsHeader(
                                gameState = gameState,
                                onWorkClick = { viewModel.workManualLabor() },
                                onStudyClick = { viewModel.studyManualResearch() },
                                onEndDayClick = { viewModel.endDay() },
                                onOpenTechTree = { currentDrawerDestination = DrawerDestination.RESEARCH },
                                onOpenSkillTree = { currentDrawerDestination = DrawerDestination.RESEARCH },
                                onReputationClick = { showReputationSheet = true },
                                onMenuClick = {
                                    coroutineScope.launch { drawerState.open() }
                                },
                                onInboxClick = { showInboxModal = true }
                            )
                        },
                        bottomBar = {
                            NavigationBar(
                                modifier = Modifier
                                    .navigationBarsPadding()
                                    .testTag("bottom_nav_bar"),
                                tonalElevation = 8.dp
                            ) {
                                NavigationTab.values().forEachIndexed { index, tab ->
                                    NavigationBarItem(
                                        selected = selectedTab == index,
                                        onClick = { selectedTab = index },
                                        icon = {
                                            Icon(
                                                imageVector = tab.icon,
                                                contentDescription = tab.title
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = tab.title,
                                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = DairyEmeraldPrimary,
                                            selectedTextColor = DairyEmeraldDark,
                                            indicatorColor = DairyEmeraldPrimary.copy(alpha = 0.15f)
                                        ),
                                        modifier = Modifier.testTag(tab.testTag)
                                    )
                                }
                            }
                        },
                        snackbarHost = {
                            SnackbarHost(hostState = snackbarHostState)
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            when (NavigationTab.values()[selectedTab]) {
                                NavigationTab.OVERVIEW -> {
                                    OverviewTab(
                                        gameState = gameState
                                    )
                                }
                                NavigationTab.INVENTORY -> {
                                    InventoryTab(
                                        gameState = gameState,
                                        onSellProduct = { prodType, amt -> viewModel.sellProduct(prodType, amt) },
                                        onSellBatch = { bId, qty -> viewModel.sellInventoryBatch(bId, qty) },
                                        onSellAllOfProduct = { pId -> viewModel.sellAllOfProduct(pId) },
                                        onDiscardSpoiled = { viewModel.discardSpoiledGoods() },
                                        onSetInventoryMethod = { method -> viewModel.setInventoryMethod(method) }
                                    )
                                }
                                NavigationTab.MARKET -> {
                                    MarketTab(
                                        gameState = gameState,
                                        onSellProduct = { prodType, amt -> viewModel.sellProduct(prodType, amt) },
                                        onSellAllOfProduct = { pId -> viewModel.sellAllOfProduct(pId) },
                                        onToggleAutoBuy = { pId, active -> viewModel.toggleAutoBuy(pId, active) },
                                        onToggleAutoSell = { pId, active -> viewModel.toggleAutoSell(pId, active) },
                                        evaluateAction = { action -> viewModel.evaluateAction(action) }
                                    )
                                }
                                NavigationTab.FACILITIES -> {
                                    FacilitiesTab(
                                        gameState = gameState,
                                        onConstructOrUpgrade = { bId -> viewModel.constructOrUpgradeBuilding(bId) },
                                        onUpgradeFactoryCapacity = { bId -> viewModel.upgradeFactoryCapacity(bId) },
                                        onToggleOperational = { bId -> viewModel.toggleBuildingOperational(bId) },
                                        onSelectRecipe = { bId, recipeIdx -> viewModel.setBuildingRecipe(bId, recipeIdx) },
                                        onUpdateAllocation = { bId, pct -> viewModel.updateFacilityAllocation(bId, pct) },
                                        onOpenTechTree = { currentDrawerDestination = DrawerDestination.RESEARCH },
                                        onBuyLandExpansion = { viewModel.buyLandExpansion() },
                                        onRushProject = { projectId -> viewModel.rushProject(projectId) },
                                        onUpdateColdStoragePriority = { priority -> viewModel.updateColdStoragePriority(priority) },
                                        onUpdateManualAllocation = { itemId, alloc -> viewModel.updateManualColdStorageAllocation(itemId, alloc) },
                                        onSpinOff = { bId, name -> viewModel.spinOffFacility(bId, name) },
                                        onBuyFacilityPerk = { bId, pId -> viewModel.buyFacilityPerk(bId, pId) },
                                        evaluateAction = { action -> viewModel.evaluateAction(action) }
                                    )
                                }
                                NavigationTab.BANK -> {
                                    BankTab(
                                        gameState = gameState,
                                        onTakeLoan = { amt -> viewModel.takeBankLoan(amt) },
                                        onRepayLoan = { amt -> viewModel.repayBankLoan(amt) },
                                        onDeposit = { amt -> viewModel.depositFunds(amt) },
                                        onWithdraw = { amt -> viewModel.withdrawFunds(amt) }
                                    )
                                }
                            }
                        }
                    }
                }
                DrawerDestination.CORPORATE_RELATIONS -> {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) { innerPadding ->
                        B2BContractsScreen(
                            gameState = gameState,
                            onAcceptContract = { offerId -> viewModel.acceptContract(offerId) },
                            onDeclineContract = { offerId -> viewModel.declineContract(offerId) },
                            onBargainContract = { offerId -> viewModel.bargainContract(offerId) },
                            onBackClick = { currentDrawerDestination = DrawerDestination.DASHBOARD },
                            onMenuClick = { coroutineScope.launch { drawerState.open() } },
                            evaluateAction = { action -> viewModel.evaluateAction(action) },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
                DrawerDestination.SUBSIDIARIES -> {
                    com.example.ui.screens.SubsidiariesScreen(
                        gameState = gameState,
                        onUpdateDividend = { subId, sliderVal -> viewModel.updateSubsidiaryDividend(subId, sliderVal) },
                        onBackClick = { currentDrawerDestination = DrawerDestination.DASHBOARD },
                        onMenuClick = { coroutineScope.launch { drawerState.open() } }
                    )
                }
                DrawerDestination.RESEARCH -> {
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
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
                DrawerDestination.ACHIEVEMENTS -> {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) { innerPadding ->
                        AchievementsScreen(
                            gameState = gameState,
                            onBackClick = { currentDrawerDestination = DrawerDestination.DASHBOARD },
                            onMenuClick = { coroutineScope.launch { drawerState.open() } },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
                DrawerDestination.CORPORATE_WARFARE -> {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) { innerPadding ->
                        CorporateWarfareScreen(
                            gameState = gameState,
                            onQueueAttack = { rivalId, type, cost, apCost -> viewModel.queuePlayerCyberAttack(rivalId, type, cost, apCost) },
                            onBuyShares = { rivalId, amount -> viewModel.buyShares(rivalId, amount) },
                            onBackClick = { currentDrawerDestination = DrawerDestination.DASHBOARD },
                            onMenuClick = { coroutineScope.launch { drawerState.open() } },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
                DrawerDestination.STOCK_MARKET -> {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) { innerPadding ->
                        StockMarketScreen(
                            gameState = gameState,
                            onBuyShares = { rivalId, qty -> viewModel.buyShares(rivalId, qty) },
                            onSellShares = { rivalId, qty -> viewModel.sellShares(rivalId, qty) },
                            onSmearCampaign = { rivalId -> viewModel.launchSmearCampaign(rivalId) },
                            onBackClick = { currentDrawerDestination = DrawerDestination.DASHBOARD },
                            onMenuClick = { coroutineScope.launch { drawerState.open() } },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
                DrawerDestination.ENTERPRISE_STATS -> {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) { innerPadding ->
                        StatsDashboardScreen(
                            gameState = gameState,
                            onBackClick = { currentDrawerDestination = DrawerDestination.DASHBOARD },
                            onMenuClick = { coroutineScope.launch { drawerState.open() } },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
                DrawerDestination.MENTOR_NETWORK -> {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        MentorListScreen(
                            gameState = gameState,
                            onMentorClick = { mentorId ->
                                activeChatMentorId = mentorId
                                currentDrawerDestination = DrawerDestination.MENTOR_CHAT
                            },
                            onMenuClick = { coroutineScope.launch { drawerState.open() } },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
                DrawerDestination.MENTOR_CHAT -> {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        val mentor = gameState.mentors.find { it.id == activeChatMentorId }
                        if (mentor != null) {
                            MentorChatScreen(
                                mentor = mentor,
                                onSendMessage = { text -> viewModel.processPlayerMessage(mentor.id, text) },
                                onSendApologyGift = { viewModel.sendApologyGift(mentor.id) },
                                onBackClick = { currentDrawerDestination = DrawerDestination.MENTOR_NETWORK },
                                modifier = Modifier.padding(innerPadding)
                            )
                        } else {
                            currentDrawerDestination = DrawerDestination.MENTOR_NETWORK
                        }
                    }
                }
                DrawerDestination.EXECUTIVE_BOARDROOM -> {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        BoardroomScreen(
                            gameState = gameState,
                            onHireExecutive = { id -> viewModel.hireExecutive(id) },
                            onBackClick = { currentDrawerDestination = DrawerDestination.DASHBOARD },
                            onMenuClick = { coroutineScope.launch { drawerState.open() } },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
                DrawerDestination.SETTINGS -> {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) { innerPadding ->
                        val soundEffectsEnabled by viewModel.soundEffectsEnabled.collectAsStateWithLifecycle()
                        val newsAlertsEnabled by viewModel.newsAlertsEnabled.collectAsStateWithLifecycle()
                        
                        SettingsScreen(
                            gameState = gameState,
                            soundEffectsEnabled = soundEffectsEnabled,
                            newsAlertsEnabled = newsAlertsEnabled,
                            onToggleSound = { viewModel.toggleSoundEffects(it) },
                            onToggleNews = { viewModel.toggleNewsAlerts(it) },
                            onSaveGame = { viewModel.saveGame() },
                            onRenameEmpire = { viewModel.updateEmpireName(it) },
                            onExitToMainMenu = onNavigateToMainMenu,
                            onRestartGame = {
                                viewModel.restartGame()
                                currentDrawerDestination = DrawerDestination.DASHBOARD
                            },
                            onBackClick = { currentDrawerDestination = DrawerDestination.DASHBOARD },
                            onMenuClick = { coroutineScope.launch { drawerState.open() } },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }

            // Daily Dairy Chronicle (The Morning Paper Alert)
            if (showNewsChronicle && gameState.activeNewsEvent != null) {
                DailyDairyChronicleDialog(
                    newsEvent = gameState.activeNewsEvent,
                    currentDay = gameState.day,
                    onDismiss = { viewModel.dismissNewsChronicle() }
                )
            }

            // Daily Report Dialog after End Day
            if (showDailyReport && !showNewsChronicle) {
                DailyReportDialog(
                    report = gameState.latestReport,
                    onDismiss = { viewModel.dismissDailyReport() }
                )
            }

            // Crossroads Dialog
            if (showCrossroadsDialog) {
                CrossroadsDialog(
                    onChooseOption = { incorporate -> viewModel.chooseCrossroadsOption(incorporate) }
                )
            }
            
            if (showMilestoneScreen) {
                com.example.ui.screens.MilestoneScreen(
                    onGoCorporate = { viewModel.goCorporate() },
                    onStartSandbox = { viewModel.startSandbox() },
                    onRetire = { 
                        viewModel.retireSave() 
                        onNavigateToMainMenu()
                    }
                )
            }

            // True Endgame Dialog (Barnaby's Moral Dilemma upon acquiring all 4 Megacorps)
            if (showEndgameDialog || (gameState.isEndgameTriggered && !gameState.isEndgameCompleted)) {
                EndgameDialog(
                    gameState = gameState,
                    onChooseOption = { choice -> viewModel.chooseEndgameOption(choice) },
                    onDismiss = { /* Modal interrupt: requires explicit decision */ }
                )
            }

            // Game Over Dialog

            if (showReputationSheet) {
                androidx.compose.material3.ModalBottomSheet(
                    onDismissRequest = { showReputationSheet = false },
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    Column(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
                        Text("Reputation Breakdown", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        val breakdown = gameState.reputationBreakdown
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Livestock (Pastures):")
                            Text("+${breakdown.livestockScore}", color = BullishGreen, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Scale (Facilities & Variety):")
                            Text("+${breakdown.scaleScore}", color = BullishGreen, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Quality (Highest Tier):")
                            Text("+${breakdown.qualityScore}", color = BullishGreen, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Penalties:")
                            Text("-${breakdown.penalties}", color = BearishRed, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Target Reputation:", fontWeight = FontWeight.Bold)
                            Text("${breakdown.totalReputation} / 100", fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Current Level: ${gameState.reputation} / 100 (Adjusts daily towards target)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }


            gameState.cyberAttackWarningEvent?.let { event ->
                val attacker = RivalCatalog.getRivalById(event.attackerId)
                androidx.compose.material3.AlertDialog(
                    onDismissRequest = { /* Must choose */ },
                    title = {
                        Text("🚨 Early Warning: Cyber Attack Detected", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                    },
                    text = {
                        Column {
                            Text("Your Scam Detection Algorithm has intercepted encrypted traffic from ${attacker.name}.")
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Projected Attack Type: ${event.attackType}", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Would you like to authorize an emergency expenditure of $15,000 to temporarily boost our Cyber Defense by +25?", style = MaterialTheme.typography.bodySmall)
                        }
                    },
                    confirmButton = {
                        val canAfford = gameState.cash >= 15000.0
                        androidx.compose.material3.Button(
                            onClick = { viewModel.resolveCyberAttackWarning(true) },
                            enabled = canAfford,
                            modifier = Modifier.bounceClick(),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = if (canAfford) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Text("Boost Defense (-$15,000)")
                        }
                    },
                    dismissButton = {
                        androidx.compose.material3.OutlinedButton(
                            onClick = { viewModel.resolveCyberAttackWarning(false) },
                            modifier = Modifier.bounceClick()
                        ) {
                            Text("Ignore (Rely on Base Defense)", color = MaterialTheme.colorScheme.error)
                        }
                    }
                )
            }

            if (gameState.missedDeliveryEvent != null) {
                val contractId = gameState.missedDeliveryEvent
                val contract = gameState.activeContracts.find { it.id == contractId }
                if (contract != null) {
                    androidx.compose.material3.AlertDialog(
                        onDismissRequest = { /* Must choose */ },
                        title = {
                            Text("⚠️ Morning Briefing: Delivery Missed", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                        },
                        text = {
                            Column {
                                val rival = com.example.model.RivalCatalog.getRivalById(contract.rivalId)
                                Text("Your farm failed to meet yesterday's delivery quota for ${rival.name}!")
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("A representative is demanding an immediate resolution. You can either pay the breach penalty in cash, or refuse and take a permanent strike on the contract.", style = MaterialTheme.typography.bodySmall)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("Strikes: ${contract.strikes} / 3", fontWeight = FontWeight.Bold)
                            }
                        },
                        confirmButton = {
                            val canAfford = gameState.cash >= contract.cashPenaltyPerMiss
                            androidx.compose.material3.Button(
                                onClick = { viewModel.resolveMissedDelivery(true) },
                                enabled = canAfford,
                                modifier = Modifier.bounceClick(),
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = if (canAfford) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Text("Pay Penalty (-$${contract.cashPenaltyPerMiss})")
                            }
                        },
                        dismissButton = {
                            androidx.compose.material3.OutlinedButton(
                                onClick = { viewModel.resolveMissedDelivery(false) }
                            ) {
                                Text("Accept Strike", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    )
                }
            }

            
            gameState.newCrisisFired?.let { crisis ->
                androidx.compose.material3.AlertDialog(
                    onDismissRequest = { viewModel.resolveNewCrisisAlert() },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⚠️", fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("BREAKING NEWS", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    text = {
                        Column {
                            Text(crisis.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(crisis.description)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Duration: ${crisis.durationDays} Days", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    confirmButton = {
                        androidx.compose.material3.Button(onClick = { viewModel.resolveNewCrisisAlert() }, colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                            Text("Acknowledge")
                        }
                    }
                )
            }

            
            if (showInboxModal) {
                com.example.ui.components.InboxOverlay(
                    gameState = gameState,
                    onDismiss = { showInboxModal = false },
                    onMarkAsRead = { viewModel.markStoryEventAsRead(it) }
                )
            }
            
            if (gameState.showAweDialogue) {
                com.example.ui.components.MentorDialogOverlay(
                    onDismiss = { viewModel.advanceAweTutorial() }
                )
            }

            if (gameState.gamePhase == com.example.model.GamePhase.COMPLETED) {
                VictoryScreen(
                    gameState = gameState,
                    onReturnToMenu = {
                        viewModel.restartGame()
                        currentDrawerDestination = DrawerDestination.DASHBOARD
                    },
                    onContinueSandbox = {
                        viewModel.continueInSandbox()
                    }
                )
            } else if (gameState.isGameOver) {
                GameOverDialog(
                    reason = gameState.gameOverReason,
                    onRestart = {
                        viewModel.restartGame()
                        currentDrawerDestination = DrawerDestination.DASHBOARD
                    }
                )
            }
        }
    }
}
