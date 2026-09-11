package com.example.ui.components

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
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import com.example.ui.components.AnimatedCounter
import com.example.ui.components.bounceClick
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameState
import com.example.ui.theme.BearishRed
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.DairyCreamSurface
import com.example.ui.theme.DairyEmeraldDark
import com.example.ui.theme.DairyEmeraldLight
import com.example.ui.theme.DairyEmeraldPrimary
import com.example.ui.theme.DairyGold
import com.example.ui.theme.DairyGoldDark
import com.example.ui.theme.TechCyan

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun TopStatsHeader(
    gameState: GameState,
    onWorkClick: () -> Unit,
    onStudyClick: () -> Unit,
    onEndDayClick: () -> Unit,
    onOpenTechTree: () -> Unit,
    onOpenSkillTree: () -> Unit,
    onReputationClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {},
    onInboxClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp,
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Row 1: Brand / Day Indicator, Hamburger Menu & Net Worth
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onMenuClick,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("menu_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Main Menu",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(modifier = Modifier.size(36.dp), contentAlignment = Alignment.Center) {
                        IconButton(onClick = onInboxClick) {
                            Icon(Icons.Default.Email, contentDescription = "Inbox", tint = MaterialTheme.colorScheme.onSurface)
                        }
                        if (gameState.pendingStoryEvents.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(BearishRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = gameState.pendingStoryEvents.size.toString(),
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(DairyEmeraldPrimary, DairyEmeraldDark)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.animation.AnimatedContent(
                            targetState = gameState.activeTopBarIcon,
                            transitionSpec = {
                                androidx.compose.animation.fadeIn() + androidx.compose.animation.scaleIn() togetherWith
                                androidx.compose.animation.fadeOut() + androidx.compose.animation.scaleOut()
                            },
                            label = "top_bar_icon_animation"
                        ) { iconTheme ->
                            Text(iconTheme.emoji, fontSize = 20.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "DAIRY TYCOON",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.5.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Day ${gameState.day}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Net Worth Chip
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.padding(2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = "Net Worth",
                            tint = DairyGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "NW: $${String.format("%,.0f", gameState.netWorth)}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Row 2: Metric Badges (Cash, Reputation, RP, Actions, Debt)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Cash Badge
                StatusChip(
                    icon = "💵",
                    label = "$${String.format("%,.0f", gameState.cash)}",
                    backgroundColor = if (gameState.cash < 0) BearishRed.copy(alpha = 0.15f) else BullishGreen.copy(alpha = 0.15f),
                    textColor = if (gameState.cash < 0) BearishRed else BullishGreen,
                    tooltipText = "Liquid Cash: Used for purchasing resources, building facilities, and market trades.",
                    modifier = Modifier.weight(1.1f),
                    testTag = "cash_badge"
                )

                // Actions Remaining Badge
                StatusChip(
                    icon = "⚡",
                    label = "${gameState.dailyActionsRemaining}/${gameState.maxDailyActions}",
                    backgroundColor = if (gameState.dailyActionsRemaining > 0) DairyGold.copy(alpha = 0.18f) else BearishRed.copy(alpha = 0.18f),
                    textColor = if (gameState.dailyActionsRemaining > 0) DairyGoldDark else BearishRed,
                    tooltipText = "Action Points: Required for manual labor, study, and construction. Regenerates daily.",
                    modifier = Modifier.weight(0.85f),
                    testTag = "actions_badge"
                )

                // Reputation Badge
                StatusChip(
                    icon = "⭐",
                    label = "${gameState.reputation}",
                    backgroundColor = DairyGold.copy(alpha = 0.15f),
                    textColor = DairyGoldDark,
                    tooltipText = "Reputation: Your public standing. High reputation aids in B2B negotiations; low reputation risks media backlash.",
                    modifier = Modifier.weight(0.7f),
                    testTag = "reputation_badge",
                    onClick = onReputationClick
                )

                // Research Points (RP) Badge (Clickable to open Tech Tree)
                StatusChip(
                    icon = "🧪",
                    label = "${gameState.researchPoints} RP",
                    backgroundColor = TechCyan.copy(alpha = 0.18f),
                    textColor = TechCyan,
                    tooltipText = "Research Points (RP): Spent in the Research Hub to unlock new technologies.",
                    modifier = Modifier.weight(0.95f),
                    testTag = "rp_badge",
                    onClick = onOpenTechTree
                )

                // Player Skills Badge (Clickable to open Skill Tree)
                StatusChip(
                    icon = "👤",
                    label = "Lv.${gameState.playerSkills.totalLevel}",
                    backgroundColor = DairyGold.copy(alpha = 0.18f),
                    textColor = DairyGoldDark,
                    tooltipText = "CEO Level: Your overall progression and corporate skill tier.",
                    modifier = Modifier.weight(0.85f),
                    testTag = "skills_badge",
                    onClick = onOpenSkillTree
                )

                // Debt Alert if any
                if (gameState.bank.isInDebt) {
                    StatusChip(
                        icon = "⚠️",
                        label = "$${String.format("%,.0f", gameState.bank.totalDebt)}",
                        backgroundColor = BearishRed.copy(alpha = 0.18f),
                        textColor = BearishRed,
                        tooltipText = "Outstanding Bank Debt",
                        modifier = Modifier.weight(0.9f),
                        testTag = "debt_badge"
                    )
                }
            }

            // Debt / Foreclosure Alert Banner
            AnimatedVisibility(
                visible = gameState.bank.isForeclosureImminent,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = BearishRed.copy(alpha = 0.12f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Foreclosure Warning",
                            tint = BearishRed,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "BANK REPO WARNING: ${gameState.bank.daysUntilLiquidation} days before asset seizure!",
                            style = MaterialTheme.typography.labelSmall,
                            color = BearishRed,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Row 3: Action Buttons Row ("Work", "Study", and master "End Day")
            val canAct = gameState.dailyActionsRemaining > 0
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Work Button
                FilledTonalButton(
                    onClick = onWorkClick,
                    enabled = canAct,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("work_button")
                        .bounceClick(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = "Work",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        if (canAct) "Work" else "Exhausted",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Study Button
                FilledTonalButton(
                    onClick = onStudyClick,
                    enabled = canAct,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("study_button")
                        .bounceClick(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "Study",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        if (canAct) "Study" else "Exhausted",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Master "End Day" Button
                Button(
                    onClick = onEndDayClick,
                    modifier = Modifier
                        .weight(1.3f)
                        .height(44.dp)
                        .testTag("end_day_button")
                        .bounceClick(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DairyEmeraldPrimary
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        "End Day",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Advance Day",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun StatusChip(
    icon: String,
    label: String = "",
    value: Double? = null,
    prefix: String = "",
    backgroundColor: Color,
    textColor: Color,
    tooltipText: String,
    modifier: Modifier = Modifier,
    testTag: String = "",
    onClick: (() -> Unit)? = null
) {
    val tooltipState = rememberTooltipState(isPersistent = false)

    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            PlainTooltip(
                containerColor = Color.Black.copy(alpha = 0.85f),
                contentColor = Color.White
            ) {
                Text(
                    text = tooltipText,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
            }
        },
        state = tooltipState
    ) {
        val clickableModifier = if (onClick != null) {
            modifier.clickable { onClick() }
        } else {
            modifier
        }

        Box(
            modifier = clickableModifier
                .clip(RoundedCornerShape(8.dp))
                .background(backgroundColor)
                .padding(horizontal = 6.dp, vertical = 6.dp)
                .testTag(testTag),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = icon, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(3.dp))
                if (value != null) {
                    AnimatedCounter(
                        targetValue = value,
                        prefix = prefix,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = textColor
                    )
                } else {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
