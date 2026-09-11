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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Achievement
import com.example.model.AchievementCategory
import com.example.model.GameState
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.DairyEmeraldDark
import com.example.ui.theme.DairyEmeraldPrimary
import com.example.ui.theme.DairyGold
import com.example.ui.theme.DairyGoldDark

enum class AchievementTab(val title: String, val iconEmoji: String) {
    MILESTONES("Milestones Map", "🗺️"),
    TROPHY_ROOM("Trophy Room", "🏆")
}

@Composable
fun AchievementsScreen(
    gameState: GameState,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    onMenuClick: (() -> Unit)? = null
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val unlockedCount = gameState.achievements.count { it.isUnlocked }
    val totalCount = gameState.achievements.size
    val progressPercent = if (totalCount > 0) (unlockedCount.toFloat() / totalCount) else 0f

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("achievements_screen")
    ) {
        // Top Header Banner
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
                                    .testTag("achievements_back_button")
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
                                    .testTag("achievements_menu_button")
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
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    Brush.linearGradient(listOf(DairyGold, DairyGoldDark))
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🏆", fontSize = 22.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Hall of Fame & Records",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Corporate Milestones & Absurd Accolades",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Score pill
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = DairyGold.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, DairyGold.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("✨", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$unlockedCount / $totalCount",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = DairyGoldDark
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Progress Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progressPercent)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(DairyGold, DairyEmeraldPrimary)
                                )
                            )
                    )
                }
            }
        }

        // Tab Row
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        ) {
            AchievementTab.values().forEachIndexed { index, tab ->
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
                    modifier = Modifier.testTag("achievement_tab_$index")
                )
            }
        }

        // Tab Content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (AchievementTab.values()[selectedTab]) {
                AchievementTab.MILESTONES -> {
                    MilestonesTimelineTab(
                        milestones = gameState.achievements.filter { it.category == AchievementCategory.MILESTONE },
                        currentDay = gameState.day
                    )
                }
                AchievementTab.TROPHY_ROOM -> {
                    TrophyRoomGridTab(
                        trophies = gameState.achievements.filter { it.category != AchievementCategory.MILESTONE }
                    )
                }
            }
        }
    }
}

/**
 * Milestones Tab (The Map): Displayed as a connected vertical timeline path.
 */
@Composable
private fun MilestonesTimelineTab(
    milestones: List<Achievement>,
    currentDay: Int,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        itemsIndexed(milestones, key = { _, item -> item.id }) { index, milestone ->
            val isLast = index == milestones.lastIndex
            TimelineMilestoneNode(
                milestone = milestone,
                isLast = isLast,
                nodeNumber = index + 1
            )
        }
    }
}

@Composable
private fun TimelineMilestoneNode(
    milestone: Achievement,
    isLast: Boolean,
    nodeNumber: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag("milestone_node_${milestone.id}"),
        verticalAlignment = Alignment.Top
    ) {
        // Left Column: Indicator Node + Vertical Connecting Line
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(44.dp)
        ) {
            // Node circle
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        if (milestone.isUnlocked) {
                            Brush.radialGradient(listOf(DairyGold, DairyGoldDark))
                        } else {
                            Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    MaterialTheme.colorScheme.surfaceVariant
                                )
                            )
                        }
                    )
                    .then(
                        if (milestone.isUnlocked) {
                            Modifier.background(DairyGold.copy(alpha = 0.2f))
                        } else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (milestone.isUnlocked) {
                    Text(milestone.trophyEmoji, fontSize = 18.sp)
                } else {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked Milestone",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Connecting line to next node
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(68.dp)
                        .background(
                            if (milestone.isUnlocked) DairyGold.copy(alpha = 0.6f)
                            else MaterialTheme.colorScheme.outlineVariant
                        )
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Right Column: Milestone Card
        OutlinedCard(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (isLast) 0.dp else 12.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.outlinedCardColors(
                containerColor = if (milestone.isUnlocked) {
                    DairyGold.copy(alpha = 0.05f)
                } else {
                    MaterialTheme.colorScheme.surface
                }
            ),
            border = BorderStroke(
                1.dp,
                if (milestone.isUnlocked) DairyGold.copy(alpha = 0.5f)
                else MaterialTheme.colorScheme.outlineVariant
            )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (milestone.isUnlocked) DairyGold.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = "STEP $nodeNumber",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                fontSize = 9.sp,
                                color = if (milestone.isUnlocked) DairyGoldDark else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = milestone.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (milestone.isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (milestone.isUnlocked) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = BullishGreen.copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Completed",
                                    tint = BullishGreen,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (milestone.unlockedDay != null) "Day ${milestone.unlockedDay}" else "COMPLETED",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = BullishGreen,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = "IN PROGRESS",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 9.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (milestone.isUnlocked) milestone.hint else "Goal: ${milestone.hint}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (milestone.isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Trophy Room Tab (Tycoon & Absurd): Displayed as a grid of trophy display cases.
 */
@Composable
private fun TrophyRoomGridTab(
    trophies: List<Achievement>,
    modifier: Modifier = Modifier
) {
    var selectedCategoryFilter by remember { mutableStateOf<AchievementCategory?>(null) }

    val filteredTrophies = if (selectedCategoryFilter != null) {
        trophies.filter { it.category == selectedCategoryFilter }
    } else {
        trophies
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
    ) {
        // Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedCategoryFilter == null,
                onClick = { selectedCategoryFilter = null },
                label = { Text("All Trophies (${trophies.size})", fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DairyEmeraldPrimary,
                    selectedLabelColor = Color.White
                )
            )

            FilterChip(
                selected = selectedCategoryFilter == AchievementCategory.TYCOON,
                onClick = { selectedCategoryFilter = AchievementCategory.TYCOON },
                label = { Text("💼 Tycoon (${trophies.count { it.category == AchievementCategory.TYCOON }})", fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DairyGold,
                    selectedLabelColor = Color.Black
                )
            )

            FilterChip(
                selected = selectedCategoryFilter == AchievementCategory.ABSURD,
                onClick = { selectedCategoryFilter = AchievementCategory.ABSURD },
                label = { Text("🤪 Absurd (${trophies.count { it.category == AchievementCategory.ABSURD }})", fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DairyGold,
                    selectedLabelColor = Color.Black
                )
            )
        }

        // Grid of display cases
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredTrophies, key = { it.id }) { trophy ->
                TrophyDisplayCaseCard(trophy = trophy)
            }
        }
    }
}

/**
 * Individual Trophy Display Case Card
 */
@Composable
private fun TrophyDisplayCaseCard(
    trophy: Achievement,
    modifier: Modifier = Modifier
) {
    val isUnlocked = trophy.isUnlocked
    val isHiddenLocked = !isUnlocked && trophy.isHidden

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("trophy_card_${trophy.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = when {
                isUnlocked -> DairyGold.copy(alpha = 0.08f)
                isHiddenLocked -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
                else -> MaterialTheme.colorScheme.surface
            }
        ),
        border = BorderStroke(
            1.dp,
            when {
                isUnlocked -> DairyGold.copy(alpha = 0.6f)
                isHiddenLocked -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                else -> MaterialTheme.colorScheme.outlineVariant
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Category Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = trophy.category.displayName.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        fontSize = 8.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }

                if (isUnlocked) {
                    Text(
                        text = "UNLOCKED",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        fontSize = 8.sp,
                        color = BullishGreen
                    )
                } else if (isHiddenLocked) {
                    Icon(
                        imageVector = Icons.Default.VisibilityOff,
                        contentDescription = "Hidden",
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Trophy Pedestal Display
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isUnlocked -> Brush.radialGradient(listOf(DairyGold.copy(alpha = 0.35f), DairyGoldDark.copy(alpha = 0.15f)))
                            isHiddenLocked -> Brush.radialGradient(listOf(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), Color.Transparent))
                            else -> Brush.radialGradient(listOf(MaterialTheme.colorScheme.surfaceVariant, Color.Transparent))
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isUnlocked -> {
                        Text(
                            text = trophy.trophyEmoji,
                            fontSize = 32.sp
                        )
                    }
                    isHiddenLocked -> {
                        Text(
                            text = "❓",
                            fontSize = 26.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                    else -> {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = if (isHiddenLocked) "???" else trophy.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Hint / Description
            Text(
                text = when {
                    isUnlocked -> trophy.hint
                    isHiddenLocked -> "Secret Tycoon Mystery. Keep exploring the dairy world to uncover this award."
                    else -> trophy.hint
                },
                style = MaterialTheme.typography.bodySmall,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp,
                color = if (isUnlocked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.outline,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            if (isUnlocked && trophy.unlockedDay != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Achieved Day ${trophy.unlockedDay}",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    color = DairyGoldDark,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
