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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.material3.Slider
import androidx.compose.runtime.mutableFloatStateOf

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AttackType
import com.example.model.GamePhase
import com.example.model.GameState
import com.example.model.RivalCompany
import com.example.ui.components.bounceClick

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CorporateWarfareScreen(
    gameState: GameState,
    onQueueAttack: (String, AttackType, Double, Int) -> Unit,
    onBuyShares: (String, Int) -> Unit,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTarget by remember { mutableStateOf<RivalCompany?>(null) }
    var sortAscending by remember { mutableStateOf(false) }
    
    if (gameState.gamePhase != GamePhase.CORPORATE) {
        Column(
            modifier = modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            Text("War Room Locked", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Grow your empire to $100k Net Worth and enter the Corporate phase to unlock Sabotage.", textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onBackClick) { Text("Return to Dashboard") }
        }
        return
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Corporate War Room", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) { Icon(Icons.Default.Menu, "Menu") }
                },
                actions = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            
            // Header stats
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Our Offense", style = MaterialTheme.typography.labelMedium)
                        Text(gameState.offenseRating.toString(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Our Defense", style = MaterialTheme.typography.labelMedium)
                        Text(gameState.defenseRating.toString(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            Divider()
            
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Active Rivals",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { sortAscending = !sortAscending }) {
                    Icon(Icons.Default.Sort, contentDescription = "Sort by Market Power")
                }
            }
            
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val sortedRivals = if (sortAscending) gameState.rivalCompanies.sortedBy { it.marketPower } else gameState.rivalCompanies.sortedByDescending { it.marketPower }
                items(sortedRivals, key = { it.id }) { rival ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(rival.logoEmoji, fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(rival.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Text("Net Worth: $${String.format("%,.0f", rival.netWorth)}", style = MaterialTheme.typography.bodySmall, color = com.example.ui.theme.BullishGreen)
                                }
                                Surface(
                                    color = when (rival.tier) {
                                        com.example.model.RivalTier.GLOBAL -> MaterialTheme.colorScheme.errorContainer
                                        com.example.model.RivalTier.SPECIALIZED -> MaterialTheme.colorScheme.secondaryContainer
                                        com.example.model.RivalTier.LOCAL -> MaterialTheme.colorScheme.surfaceVariant
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        rival.tier.name,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = when (rival.tier) {
                                            com.example.model.RivalTier.GLOBAL -> MaterialTheme.colorScheme.onErrorContainer
                                            com.example.model.RivalTier.SPECIALIZED -> MaterialTheme.colorScheme.onSecondaryContainer
                                            com.example.model.RivalTier.LOCAL -> MaterialTheme.colorScheme.onSurfaceVariant
                                        }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Threat: ${rival.threatLevel}/100", style = MaterialTheme.typography.labelSmall, color = com.example.ui.theme.BearishRed)
                                Text("Market Share: ${rival.marketShare}%", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // Buy Shares section
                            val sharesOwned = gameState.getSharesOwned(rival.id)
                            val costPerShare = rival.netWorth / 100.0
                            var buyAmount by remember { mutableFloatStateOf(1f) }
                            val availableToBuy = (100 - sharesOwned).coerceAtLeast(0)
                            
                            Text("Shares Owned: $sharesOwned/100", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            if (availableToBuy > 0) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                    Slider(
                                        value = buyAmount,
                                        onValueChange = { buyAmount = it },
                                        valueRange = 1f..availableToBuy.toFloat(),
                                        steps = availableToBuy - 2,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("${buyAmount.toInt()} shares")
                                }
                                
                                Button(
                                    onClick = { onBuyShares(rival.id, buyAmount.toInt()) },
                                    modifier = Modifier.fillMaxWidth().bounceClick(),
                                    colors = ButtonDefaults.buttonColors(containerColor = com.example.ui.theme.DairyEmeraldPrimary)
                                ) {
                                    Text("Buy ${buyAmount.toInt()} Shares ($${String.format("%,.0f", buyAmount.toInt() * costPerShare)})")
                                }
                            } else {
                                Text("Maximum shares acquired! Hostile Takeover impending.", style = MaterialTheme.typography.bodySmall, color = com.example.ui.theme.DairyGoldDark)
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Button(
                                onClick = { selectedTarget = rival },
                                modifier = Modifier.fillMaxWidth().bounceClick(),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("Queue Sabotage")
                            }
                        }
                    }
                }
            }
        }
        
        // Sabotage Dialog
        if (selectedTarget != null) {
            val rival = selectedTarget!!
            AlertDialog(
                onDismissRequest = { selectedTarget = null },
                title = { Text("Sabotage ${rival.name}") },
                text = {
                    Column {
                        Text("Select Attack Vector", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Attack 1
                        OutlinedButton(
                            onClick = { 
                                onQueueAttack(rival.id, AttackType.DDOS_FACILITY, 10000.0, 2)
                                selectedTarget = null
                            },
                            modifier = Modifier.fillMaxWidth().bounceClick(),
                            enabled = gameState.cash >= 10000.0 && gameState.dailyActionsRemaining >= 2
                        ) {
                            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                                Text("DDoS Factories (Cost: $10k, 2 AP)", fontWeight = FontWeight.Bold)
                                Text("Temporarily drop their defense rating.", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Attack 2
                        OutlinedButton(
                            onClick = { 
                                onQueueAttack(rival.id, AttackType.FINANCIAL_PHISHING, 25000.0, 3)
                                selectedTarget = null
                            },
                            modifier = Modifier.fillMaxWidth().bounceClick(),
                            enabled = gameState.cash >= 25000.0 && gameState.dailyActionsRemaining >= 3
                        ) {
                            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                                Text("Phishing Campaign (Cost: $25k, 3 AP)", fontWeight = FontWeight.Bold)
                                Text("Siphon 5% of their net worth.", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Attack 3
                        OutlinedButton(
                            onClick = { 
                                onQueueAttack(rival.id, AttackType.LOGISTICS_HIJACK, 15000.0, 2)
                                selectedTarget = null
                            },
                            modifier = Modifier.fillMaxWidth().bounceClick(),
                            enabled = gameState.cash >= 15000.0 && gameState.dailyActionsRemaining >= 2
                        ) {
                            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                                Text("Logistics Hijack (Cost: $15k, 2 AP)", fontWeight = FontWeight.Bold)
                                Text("Steal $25k in liquid cash.", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { selectedTarget = null }) { Text("Cancel") }
                }
            )
        }
    }
}
