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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AnomalyCatalog
import com.example.model.GameState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedLabScreen(
    gameState: GameState,
    onUnlockAnomaly: (String) -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("☢️", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Advanced Lab", fontWeight = FontWeight.Black, color = Color(0xFF00FFCC))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) { Icon(Icons.Default.Menu, "Menu", tint = Color(0xFF00FFCC)) }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF111111)
                )
            )
        },
        modifier = modifier.background(Color(0xFF0A0A0A))
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0A0A0A))
                .padding(innerPadding)
        ) {
            // Header stats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "EXPERIMENTAL BLUEPRINTS",
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    fontSize = 12.sp
                )
                Surface(
                    color = Color(0xFF00FFCC).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00FFCC))
                ) {
                    Text(
                        "${gameState.researchPoints} RP Available",
                        color = Color(0xFF00FFCC),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
            
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(AnomalyCatalog.BLUEPRINTS) { bp ->
                    val isUnlocked = gameState.unlockedAnomalies.contains(bp.id)
                    val canAfford = gameState.researchPoints >= bp.researchCostRp
                    val prereqsMet = bp.prerequisites.isEmpty() || bp.prerequisites.all { it in gameState.unlockedAnomalies }
                    
                    val cardColor = if (isUnlocked) Color(0xFF00FFCC).copy(alpha = 0.15f) else Color(0xFF1A1A1A)
                    val borderColor = if (isUnlocked) Color(0xFF00FFCC) else Color.DarkGray
                    
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(if (isUnlocked) 2.dp else 1.dp, borderColor, RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = cardColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(bp.title, fontWeight = FontWeight.Black, fontSize = 18.sp, color = if (isUnlocked) Color.White else Color.LightGray)
                                if (isUnlocked) {
                                    Text("UNLOCKED", color = Color(0xFF00FFCC), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(bp.description, color = Color.Gray, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Equip Cost: $${String.format("%,.0f", bp.installCostCash)}", color = Color(0xFF00FFCC), fontSize = 12.sp)
                                
                                if (!isUnlocked) {
                                    Button(
                                        onClick = { onUnlockAnomaly(bp.id) },
                                        enabled = canAfford && prereqsMet,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF00FFCC),
                                            contentColor = Color.Black,
                                            disabledContainerColor = Color.DarkGray
                                        )
                                    ) {
                                        Text(if (!prereqsMet) "LOCKED" else "Unlock (${bp.researchCostRp} RP)", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
