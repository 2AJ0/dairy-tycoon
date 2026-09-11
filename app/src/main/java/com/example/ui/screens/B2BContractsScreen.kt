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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ContractOffer
import com.example.model.ContractType
import com.example.model.GameState
import com.example.model.ProductCatalog
import com.example.model.RivalCatalog
import com.example.model.RivalCompany
import com.example.ui.components.pulseWarning
import com.example.ui.theme.BearishRed
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.DairyEmeraldDark
import com.example.ui.theme.DairyEmeraldLight
import com.example.ui.theme.DairyEmeraldPrimary
import com.example.ui.theme.DairyGold
import com.example.ui.theme.DairyGoldDark
import com.example.ui.theme.DairySlateCardDark
import com.example.ui.theme.TechCyan

enum class B2BTab(val title: String, val emoji: String) {
    INBOUND_DEALS("Inbound Deals", "📥"),
    ACTIVE_CONTRACTS("Active Contracts", "🤝"),
    RIVAL_INTELLIGENCE("Rival AI", "🏢")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun B2BContractsScreen(
    gameState: GameState,
    onAcceptContract: (String) -> Unit,
    onDeclineContract: (String) -> Unit,
    onBargainContract: (String) -> Unit,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
    evaluateAction: (com.example.model.GameAction) -> com.example.model.GuidanceState,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Corporate Relations",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("🤝", fontSize = 16.sp)
                    }
                    Text(
                        text = "B2B RFP Deals & Rival Intelligence",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.testTag("btn_back_b2b")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Operations"
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = onMenuClick,
                    modifier = Modifier.testTag("btn_menu_b2b")
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Open Drawer"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        // Executive Overview Dashboard Bar
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Active Contracts Counter
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "ACTIVE DEALS",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = DairyGoldDark,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "${gameState.activeContracts.size} Ongoing",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Inbound RFP Offers
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "INBOUND RFPs",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = TechCyan,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "${gameState.pendingContractOffers.size} Pending",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Total Contract Revenue
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "B2B EARNINGS",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = BullishGreen,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "$${String.format("%,.0f", gameState.totalContractRevenueEarned)}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = BullishGreen
                    )
                }
            }
        }

        // Tab Navigation
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        ) {
            B2BTab.values().forEachIndexed { index, tab ->
                val count = when (tab) {
                    B2BTab.INBOUND_DEALS -> gameState.pendingContractOffers.size
                    B2BTab.ACTIVE_CONTRACTS -> gameState.activeContracts.size
                    B2BTab.RIVAL_INTELLIGENCE -> gameState.rivalCompanies.size
                }

                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    modifier = Modifier.testTag("tab_b2b_${tab.name.lowercase()}"),
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(tab.emoji)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = tab.title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp
                            )
                            if (count > 0 && tab != B2BTab.RIVAL_INTELLIGENCE) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Surface(
                                    shape = CircleShape,
                                    color = if (selectedTab == index) DairyEmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "$count",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 10.sp,
                                            color = if (selectedTab == index) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }

        // Tab Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when (selectedTab) {
                0 -> InboundOffersTab(
                    gameState = gameState,
                    onAccept = onAcceptContract,
                    onDecline = onDeclineContract,
                    onBargain = onBargainContract,
                    evaluateAction = evaluateAction
                )
                1 -> ActiveContractsTab(
                    gameState = gameState,
                    onSwitchToDeals = { selectedTab = 0 }
                )
                2 -> RivalIntelligenceTab(
                    gameState = gameState
                )
            }
        }
    }
}

/**
 * Tab 1: Inbound Deals & Negotiation Engine
 */
@Composable
private fun InboundOffersTab(
    gameState: GameState,
    onAccept: (String) -> Unit,
    onDecline: (String) -> Unit,
    onBargain: (String) -> Unit,
    evaluateAction: (com.example.model.GameAction) -> com.example.model.GuidanceState
) {
    if (gameState.pendingContractOffers.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("📥", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No Pending RFP Offers",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Competitors submit bulk purchase requests as your daily operations proceed. Advance the day to receive new institutional tenders!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("inbound_offers_list"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Negotiation Info Banner
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DairyEmeraldDark.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, DairyEmeraldPrimary.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🎲", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            val silverTongueLvl = gameState.playerSkills.silverTongueLevel
                            val odds = (50 + (silverTongueLvl * 15)).coerceAtMost(95)
                            Text(
                                text = "Silver Tongue Negotiation (${odds}% Success Odds)",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = DairyEmeraldLight
                            )
                            Text(
                                text = "Bargaining raises daily payout by +15%, but failure causes the rival to cancel the deal. (Lv.$silverTongueLvl Silver Tongue)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            items(gameState.pendingContractOffers, key = { it.id }) { offer ->
                val rival = gameState.rivalCompanies.find { it.id == offer.rivalId } ?: RivalCatalog.getRivalById(offer.rivalId)
                val product = ProductCatalog.getById(offer.targetProduct)
                val availableInWarehouse = gameState.inventory.filter { it.itemId == offer.targetProduct && !it.isSpoiled }.sumOf { it.quantity }
                val spotPrice = gameState.marketPrices[offer.targetProduct]?.currentPrice ?: product.basePrice
                val payoutPerUnit = offer.payoutAmount / offer.requiredQuantity.toDouble()
                val premiumPct = if (spotPrice > 0) ((payoutPerUnit - spotPrice) / spotPrice) * 100.0 else 0.0

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("contract_offer_card_${offer.id}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    border = BorderStroke(
                        if (offer.isJunk) 2.dp else 1.dp,
                        if (offer.isJunk) MaterialTheme.colorScheme.error.copy(alpha = 0.8f) 
                        else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Header: Rival Info
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(rival.logoEmoji, fontSize = 20.sp)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = rival.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = rival.corporateTone,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            // Tags (High Risk + Expiry)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (offer.isJunk) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.errorContainer,
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                                    ) {
                                        Text(
                                            text = "HIGH RISK",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onErrorContainer,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = DairyGold.copy(alpha = 0.15f),
                                    border = BorderStroke(1.dp, DairyGold.copy(alpha = 0.4f))
                                ) {
                                    Text(
                                        text = "⏳ ${offer.expiresInDays}d left",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = DairyGoldDark,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                        Spacer(modifier = Modifier.height(12.dp))

                        // Deal Terms Grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Product Quota
                            Column {
                                Text(
                                    text = if (offer.contractType == ContractType.BULK_DEADLINE) "BULK TARGET" else "DAILY QUOTA",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 10.sp
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(product.emoji, fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${if (offer.contractType == ContractType.BULK_DEADLINE) offer.targetTotalQuantity else offer.requiredQuantity}x ${product.name}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Daily Payout
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = if (offer.contractType == ContractType.BULK_DEADLINE) "TOTAL PAYOUT" else "DAILY PAYOUT",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = if (offer.contractType == ContractType.BULK_DEADLINE) "+$${String.format("%.2f", offer.payoutAmount)}" else "+$${String.format("%.2f", offer.payoutAmount)}/day",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = BullishGreen
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Secondary Stats: Duration, Total Value, Premium
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "⏱️ Duration: ${offer.durationDays} Days",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "💰 Total: $${String.format("%.0f", offer.totalPotentialValue)}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = BullishGreen
                            )
                            Text(
                                text = "📈 +${String.format("%.0f", premiumPct)}% Premium",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = TechCyan
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Warehouse Readiness Check & Penalty Warning
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (availableInWarehouse >= offer.requiredQuantity) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = BullishGreen,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "In stock: ${availableInWarehouse}u (Ready)",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = BullishGreen,
                                        fontSize = 11.sp
                                    )
                                }
                            } else {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = DairyGoldDark,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "In stock: ${availableInWarehouse}u (${offer.requiredQuantity - availableInWarehouse} needed)",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = DairyGoldDark,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Text(
                                text = "Breach Fine: -$${String.format("%.0f", offer.penaltyAmount)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = BearishRed,
                                fontSize = 11.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        if (gameState.reputation < offer.minReputationRequired) {
                            Text(
                                text = "🔒 Requires ${offer.minReputationRequired} Reputation",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        // Action Buttons: Bargain, Accept, Decline
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Decline
                            OutlinedButton(
                                onClick = { onDecline(offer.id) },
                                modifier = Modifier
                                    .weight(0.7f)
                                    .testTag("btn_decline_contract_${offer.id}"),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Decline",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // Bargain Action
                            val silverTongueLvl = gameState.playerSkills.silverTongueLevel
                            val odds = (50 + (silverTongueLvl * 15)).coerceAtMost(95)
                            FilledTonalButton(
                                enabled = gameState.reputation >= offer.minReputationRequired,
                                onClick = { onBargain(offer.id) },
                                modifier = Modifier
                                    .weight(1.3f)
                                    .testTag("btn_bargain_contract_${offer.id}"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = TechCyan.copy(alpha = 0.2f),
                                    contentColor = TechCyan
                                ),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🎲", fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Bargain ($odds%)",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Accept Contract
                            Button(
                                enabled = gameState.reputation >= offer.minReputationRequired,
                                onClick = { onAccept(offer.id) },
                                modifier = Modifier
                                    .weight(1.2f)
                                    .testTag("btn_accept_contract_${offer.id}"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = DairyEmeraldPrimary,
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    val action = com.example.model.GameAction.Contract(offer.id)
                                    val guidance = evaluateAction(action)
                                    com.example.ui.components.GuidanceBadge(state = guidance)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("🤝", fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Accept",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Tab 2: Active Contracts & Daily Quota Monitoring
 */
@Composable
private fun ActiveContractsTab(
    gameState: GameState,
    onSwitchToDeals: () -> Unit
) {
    if (gameState.activeContracts.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🤝", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No Active B2B Contracts",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Sign supply deals with rival corporations to secure predictable, high-margin daily cash flow.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onSwitchToDeals,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DairyEmeraldPrimary)
                    ) {
                        Text("Browse Inbound RFP Deals")
                    }
                }
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("active_contracts_list"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("ℹ️", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Daily Quotas are automatically deducted from oldest warehouse batches at End of Day. Ensure sufficient stock to avoid breach penalties!",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            items(gameState.activeContracts, key = { it.id }) { contract ->
                val rival = gameState.rivalCompanies.find { it.id == contract.rivalId } ?: RivalCatalog.getRivalById(contract.rivalId)
                val product = ProductCatalog.getById(contract.targetProduct)
                val availableInWarehouse = gameState.inventory.filter { it.itemId == contract.targetProduct && !it.isSpoiled }.sumOf { it.quantity }
                val isQuotaMet = availableInWarehouse >= contract.requiredQuantity
                val daysPassed = contract.durationDays - contract.daysRemaining

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("active_contract_card_${contract.id}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    border = BorderStroke(
                        1.5.dp,
                        if (isQuotaMet) DairyEmeraldPrimary.copy(alpha = 0.6f) else BearishRed.copy(alpha = 0.6f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(rival.logoEmoji, fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = rival.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Supply Contract Agreement",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 10.sp
                                    )
                                }
                            }

                            // Tags
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (contract.isJunk) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.errorContainer,
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                                    ) {
                                        Text(
                                            text = "HIGH RISK",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onErrorContainer,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                }

                                // Day Progress Badge
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (contract.daysRemaining <= 1) BearishRed.copy(alpha = 0.15f) else DairyEmeraldPrimary.copy(alpha = 0.15f),
                                    border = BorderStroke(1.dp, if (contract.daysRemaining <= 1) BearishRed.copy(alpha = 0.4f) else DairyEmeraldPrimary.copy(alpha = 0.4f)),
                                    modifier = if (contract.daysRemaining <= 1) Modifier.pulseWarning() else Modifier
                                ) {
                                    Text(
                                        text = "Day $daysPassed / ${contract.durationDays}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = DairyEmeraldPrimary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Progress Bar
                        LinearProgressIndicator(
                            progress = { contract.progressPercent },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = DairyEmeraldPrimary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Daily Delivery Quota vs Warehouse Stock
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isQuotaMet) BullishGreen.copy(alpha = 0.10f) else BearishRed.copy(alpha = 0.10f),
                            border = BorderStroke(
                                1.dp,
                                if (isQuotaMet) BullishGreen.copy(alpha = 0.3f) else BearishRed.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "TONIGHT'S DELIVERY QUOTA",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp,
                                        color = if (isQuotaMet) BullishGreen else BearishRed
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(product.emoji, fontSize = 16.sp)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "${contract.requiredQuantity}x ${product.name}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = if (isQuotaMet) "STOCK READY" else "DEFICIT WARNING",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp,
                                        color = if (isQuotaMet) BullishGreen else BearishRed
                                    )
                                    Text(
                                        text = "${availableInWarehouse} / ${contract.requiredQuantity} units",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isQuotaMet) BullishGreen else BearishRed
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Financial & Delivery Breakdown
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "DAILY PAYOUT",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = "+$${String.format("%.2f", contract.payoutAmount)}/day",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = BullishGreen
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "TOTAL EARNED",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = "$${String.format("%.2f", contract.totalPaidOut)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = DairyGoldDark
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "BREACH PENALTY",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = "-$${String.format("%.2f", contract.penaltyAmount)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = BearishRed
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Fulfilled vs Failed days tracker chips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text("✅", fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${contract.fulfilledDays} Delivered",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text("⚠️", fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${contract.failedDays} Defaults",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (contract.failedDays > 0) BearishRed else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Tab 3: Rival AI Competitor Intelligence
 */
@Composable
private fun RivalIntelligenceTab(
    gameState: GameState
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("rival_intelligence_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = TechCyan.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, TechCyan.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📊", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Regional Corporate Ecosystem",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = TechCyan
                        )
                        Text(
                            text = "Track rival corporate valuations, market dominance multipliers, and strategic commodity specializations.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        items(gameState.rivalCompanies, key = { it.id }) { rival ->
            val product = ProductCatalog.getById(rival.specialtyProduct)
            val isBullish = rival.stockChangePercent >= 0

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("rival_card_${rival.id}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(rival.logoEmoji, fontSize = 24.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = rival.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = rival.headquarters,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Stock Price Chip
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isBullish) BullishGreen.copy(alpha = 0.15f) else BearishRed.copy(alpha = 0.15f),
                            border = BorderStroke(
                                1.dp,
                                if (isBullish) BullishGreen.copy(alpha = 0.4f) else BearishRed.copy(alpha = 0.4f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = "$${String.format("%.2f", rival.stockPrice)}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isBullish) BullishGreen else BearishRed
                                )
                                Text(
                                    text = "${if (isBullish) "+" else ""}${String.format("%.1f", rival.stockChangePercent)}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color = if (isBullish) BullishGreen else BearishRed
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "\"${rival.tagline}\"",
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(10.dp))

                    // Metrics: Market Power & Specialty
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "MARKET POWER",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = rival.marketPowerFormatted,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TechCyan
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                LinearProgressIndicator(
                                    progress = { (rival.marketPower / 3.5f).coerceIn(0f, 1f) },
                                    modifier = Modifier
                                        .width(70.dp)
                                        .height(5.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = TechCyan,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "SPECIALIZATION",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(product.emoji, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = product.name,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
