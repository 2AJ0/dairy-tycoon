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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GamePhase
import com.example.model.GameState
import com.example.ui.components.bounceClick

@Composable
fun VictoryScreen(
    gameState: GameState,
    onReturnToMenu: () -> Unit,
    onContinueSandbox: () -> Unit
) {
    val isMonopoly = gameState.gameOverReason == "MONOPOLIST"
    val isSingularity = gameState.gameOverReason == "TECH_SINGULARITY"
    
    val primaryColor = if (isSingularity) com.example.ui.theme.TechCyan else com.example.ui.theme.DairyGoldDark
    val bgGradient = if (isSingularity) MaterialTheme.colorScheme.surface else Color(0xFF1E1E1E)
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))
                if (isSingularity) {
                    Surface(
                        color = primaryColor.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Text(
                            "🤖 AI AGENTS UNLEASHED",
                            color = primaryColor,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                } else {
                    Text("👑", fontSize = 72.sp)
                }
                
                Text(
                    text = if (isSingularity) "TECH SINGULARITY ACHIEVED" else "GLOBAL MONOPOLIST",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Black,
                    color = primaryColor,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "The dairy industry bends to your will.",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
            
            // Financial Metrics
            item {
                MetricCard("Financial Dominance", primaryColor) {
                    MetricRow("Peak Net Worth", "$${String.format("%,.0f", gameState.peakNetWorth)}")
                    MetricRow("Lifetime Revenue", "$${String.format("%,.0f", gameState.lifetimeRevenue)}")
                    MetricRow("Hostile Takeovers", "${gameState.hostileTakeovers}")
                }
            }
            
            // Cyber Metrics
            item {
                MetricCard("Cyber Warfare", primaryColor) {
                    MetricRow("Attacks Thwarted", "${gameState.attacksThwarted}")
                    MetricRow("Fraud Losses", "$${String.format("%,.0f", gameState.fraudLosses)}")
                    MetricRow("DDoS Downtime", "${gameState.ddosDowntimeDays} Days")
                }
            }
            
            // Logistics Metrics
            item {
                MetricCard("Logistics & Time", primaryColor) {
                    MetricRow("Days to Victory", "${gameState.totalDaysPlayed} Days")
                    MetricRow("Lifetime Spoilage", "${gameState.lifetimeSpoilage} Units")
                }
            }
            
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
        
        // Bottom Action Bar
        Surface(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
            color = Color.Transparent
        ) {
            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                Button(
                    onClick = onContinueSandbox,
                    modifier = Modifier.fillMaxWidth().height(56.dp).bounceClick(),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Continue in Sandbox",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSingularity) Color.Black else Color.White
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onReturnToMenu,
                    modifier = Modifier.fillMaxWidth().height(56.dp).bounceClick(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Return to Main Menu", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun MetricCard(title: String, accentColor: Color, content: @Composable ColumnScope.() -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFF2C2C2C)),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = accentColor)
            Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.Gray.copy(alpha = 0.3f))
            content()
        }
    }
}

@Composable
fun MetricRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = Color.LightGray)
        Text(value, style = MaterialTheme.typography.bodyMedium.copy(fontFeatureSettings = "tnum"), fontWeight = FontWeight.Bold, color = Color.White)
    }
}
