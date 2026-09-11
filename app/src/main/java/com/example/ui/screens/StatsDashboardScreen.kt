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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Factory
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameState
import com.example.ui.theme.BearishRed
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.DairyEmeraldDark
import com.example.ui.theme.DairyEmeraldLight
import com.example.ui.theme.DairyEmeraldPrimary
import com.example.ui.theme.DairyGold
import com.example.ui.theme.DairyGoldDark
import com.example.ui.theme.TechCyan

@Composable
fun StatsDashboardScreen(
    gameState: GameState,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    onMenuClick: (() -> Unit)? = null
) {
    val stats = gameState.lifetimeStats

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("lifetime_stats_screen")
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
                                    .testTag("stats_back_button")
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
                                    .testTag("stats_menu_button")
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
                                    Brush.linearGradient(listOf(DairyEmeraldPrimary, DairyEmeraldDark))
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📊", fontSize = 22.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Enterprise Statistics",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Lifetime Financial & Operational Records",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = DairyGold.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, DairyGold.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = "Day ${gameState.day}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = DairyGoldDark,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Peak Record Hero Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    border = BorderStroke(1.dp, DairyGold.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "ALL-TIME PEAK VALUATION",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    color = DairyGoldDark
                                )
                                Text(
                                    "$${String.format("%,.0f", stats.highestNetWorthAchieved.coerceAtLeast(gameState.netWorth))}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Black,
                                    color = BullishGreen
                                )
                            }
                            Text("🏆", fontSize = 36.sp)
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Operating Days", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${stats.daysPlayed.coerceAtLeast(gameState.day)} Days", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Current Net Worth", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("$${String.format("%,.0f", gameState.netWorth)}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = DairyEmeraldLight)
                            }
                        }
                    }
                }
            }

            // Financial Performance
            item {
                StatsSectionCard(
                    title = "Financial Capital Flow",
                    icon = Icons.Default.MonetizationOn,
                    iconTint = BullishGreen
                ) {
                    StatRow(label = "Total Gross Revenue Generated", value = "$${String.format("%,.2f", stats.totalRevenueEarned)}", valueColor = BullishGreen)
                    StatRow(label = "Total Facility Maintenance Paid", value = "-$${String.format("%,.2f", stats.totalMaintenancePaid)}", valueColor = BearishRed)
                    StatRow(label = "Total Bank Loan Interest Paid", value = "-$${String.format("%,.2f", stats.totalInterestPaid)}", valueColor = BearishRed)
                    StatRow(label = "Current Liquid Cash Reserves", value = "$${String.format("%,.2f", gameState.cash)}", valueColor = MaterialTheme.colorScheme.onSurface)
                }
            }

            // Dairy Production & Supply Chain
            item {
                StatsSectionCard(
                    title = "Supply Chain & Production",
                    icon = Icons.Default.Factory,
                    iconTint = DairyEmeraldPrimary
                ) {
                    StatRow(label = "Total Raw Milk Milked", value = "${String.format("%,.0f", stats.totalRawMilkProduced)} Units", valueColor = MaterialTheme.colorScheme.onSurface)
                    StatRow(label = "Total Artisanal Goods Manufactured", value = "${String.format("%,.0f", stats.totalProcessedGoodsProduced)} Units", valueColor = DairyEmeraldPrimary)
                    StatRow(label = "Total Inventory Spoiled (FIFO Loss)", value = "${String.format("%,d", stats.totalSpoiledUnits)} Units", valueColor = if (stats.totalSpoiledUnits > 0) BearishRed else BullishGreen)
                    StatRow(label = "Current Land Footprint", value = "${gameState.usedLand} / ${gameState.totalLandCapacity} Plots", valueColor = DairyGoldDark)
                }
            }

            // Executive Actions & Operations
            item {
                StatsSectionCard(
                    title = "Executive Productivity",
                    icon = Icons.Default.ElectricBolt,
                    iconTint = TechCyan
                ) {
                    StatRow(label = "Manual Overtime Labor Shifts", value = "${stats.totalManualWorkClicks} Shifts", valueColor = MaterialTheme.colorScheme.onSurface)
                    StatRow(label = "Manual Research Studies", value = "${stats.totalManualResearchClicks} Studies", valueColor = TechCyan)
                    StatRow(label = "Time-Gated Projects Completed", value = "${stats.totalProjectsCompleted} Projects", valueColor = MaterialTheme.colorScheme.onSurface)
                    StatRow(label = "Projects Expedited (Rushed ⚡)", value = "${stats.totalProjectsRushed} Rushed", valueColor = DairyGoldDark)
                    StatRow(label = "Daily Action Limit Capacity", value = "${gameState.playerSkills.maxDailyActions} Actions / Day", valueColor = DairyEmeraldLight)
                }
            }

            // Analytical Averages
            item {
                StatsSectionCard(
                    title = "Operational Analytics",
                    icon = Icons.Default.TrendingUp,
                    iconTint = MaterialTheme.colorScheme.primary
                ) {
                    val safeDays = stats.daysPlayed.coerceAtLeast(1).toDouble()
                    val avgRevenue = stats.totalRevenueEarned / safeDays
                    val avgProduction = stats.totalRawMilkProduced / safeDays
                    
                    val winRate = if (gameState.hostileTakeovers + gameState.attacksThwarted > 0) {
                        (gameState.attacksThwarted.toDouble() / (gameState.hostileTakeovers + gameState.attacksThwarted)) * 100.0
                    } else {
                        0.0
                    }

                    StatRow(label = "Average Daily Revenue", value = "$${String.format("%,.2f", avgRevenue)} / day", valueColor = BullishGreen)
                    StatRow(label = "Average Milk Production", value = "${String.format("%,.1f", avgProduction)} units / day", valueColor = MaterialTheme.colorScheme.onSurface)
                    StatRow(label = "Global Market Share", value = "${String.format("%.1f", gameState.playerMarketShare)}%", valueColor = DairyGoldDark)
                    if (gameState.gamePhase != com.example.model.GamePhase.FARMING) {
                        StatRow(label = "Cyber Defense Win Rate", value = "${String.format("%.1f", winRate)}%", valueColor = TechCyan)
                    }
                }
            }

            // Megacorp & Stock Market
            if (gameState.gamePhase != com.example.model.GamePhase.FARMING) {
                item {
                    StatsSectionCard(
                        title = "Corporate Warfare & Stocks",
                    icon = Icons.Default.ShowChart,
                    iconTint = DairyGoldDark
                ) {
                    StatRow(label = "B2B Supply Contracts Fulfilled", value = "${stats.totalContractsFulfilled} Contracts", valueColor = DairyEmeraldPrimary)
                    StatRow(label = "Corporate Smear Campaigns Launched", value = "${stats.totalSmearCampaignsRun} Operations", valueColor = BearishRed)
                    StatRow(label = "Rival Megacorp Shares Purchased", value = "${stats.totalSharesPurchased} Shares", valueColor = MaterialTheme.colorScheme.onSurface)
                    StatRow(label = "Shares Sold for Profit", value = "${stats.totalSharesSold} Shares", valueColor = MaterialTheme.colorScheme.onSurface)
                    val subSize = gameState.subsidiaryCompanyIds?.size ?: 0
                    StatRow(label = "Subsidiaries Controlled (51%+)", value = "${subSize} / 4 Megacorps", valueColor = if (subSize == 4) DairyGoldDark else TechCyan)
                }
            }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun StatsSectionCard(
    title: String,
    icon: ImageVector,
    iconTint: Color,
    content: @Composable () -> Unit
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(iconTint.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            Spacer(modifier = Modifier.height(8.dp))

            content()
        }
    }
}

@Composable
private fun StatRow(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }
}
