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

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GamePhase
import com.example.model.GameState
import com.example.ui.theme.DairyGoldDark
import com.example.ui.theme.DairyEmeraldPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubsidiariesScreen(
    gameState: GameState,
    onUpdateDividend: (String, Float) -> Unit,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (gameState.spunOffSubsidiaries.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.BusinessCenter, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            Text("No Subsidiaries Active", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(
                "Spin off operational processing facilities from the Facilities Tab to automate them.",
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onBackClick) { Text("Return to Dashboard") }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Subsidiaries Portfolio", fontWeight = FontWeight.Bold) },
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
        LazyColumn(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(gameState.spunOffSubsidiaries, key = { it.id }) { sub ->
                val progress by animateFloatAsState(targetValue = (sub.internalCapital / 50000.0).toFloat().coerceIn(0f, 1f))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🏢", fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(sub.customName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(
                                    "Quality Multiplier: ${String.format("%.1f", sub.qualityMultiplier)}x", 
                                    style = MaterialTheme.typography.labelSmall, 
                                    color = DairyGoldDark
                                )
                            }
                        }
                        
                        Divider(modifier = Modifier.padding(vertical = 12.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Retained Earnings: ${( (1f - sub.dividendSlider) * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            Text("Player Dividend: ${(sub.dividendSlider * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = DairyEmeraldPrimary)
                        }
                        
                        Slider(
                            value = sub.dividendSlider,
                            onValueChange = { onUpdateDividend(sub.id, it) },
                            valueRange = 0f..1f,
                            steps = 19,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            "Internal Capital: $${String.format("%,.0f", sub.internalCapital)} / $50,000",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth().height(8.dp).padding(top = 4.dp),
                            color = DairyGoldDark,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                        Text(
                            "Fills up to trigger automatic +0.1x Quality Upgrade",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            fontSize = 9.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
