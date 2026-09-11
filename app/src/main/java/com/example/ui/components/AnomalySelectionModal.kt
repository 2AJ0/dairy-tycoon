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

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AnomalyCatalog
import com.example.model.Building
import com.example.model.GameState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnomalySelectionModal(
    facility: Building,
    gameState: GameState,
    onDismiss: () -> Unit,
    onInstall: (String) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF111111),
        scrimColor = Color.Black.copy(alpha = 0.6f)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp).padding(bottom = 32.dp)) {
            Text("Install Anomaly in ${facility.name}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Anomalies permanently alter facility logic. Only one can be installed per max-level factory.", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(16.dp))
            
            if (gameState.unlockedAnomalies.isEmpty()) {
                Text("No Experimental Blueprints unlocked yet. Visit the Advanced Lab.", color = Color(0xFF00FFCC), modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    val unlockedBps = AnomalyCatalog.BLUEPRINTS.filter { it.id in gameState.unlockedAnomalies }
                    items(unlockedBps) { bp ->
                        val isEquipped = facility.activeAnomalyId == bp.id
                        val canAfford = gameState.cash >= bp.installCostCash
                        
                        Card(
                            modifier = Modifier.fillMaxWidth().border(1.dp, if (isEquipped) Color(0xFF00FFCC) else Color.DarkGray, RoundedCornerShape(8.dp)),
                            colors = CardDefaults.cardColors(containerColor = if (isEquipped) Color(0xFF00FFCC).copy(alpha = 0.1f) else Color(0xFF1A1A1A))
                        ) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(bp.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text(bp.description, color = Color.LightGray, fontSize = 12.sp, lineHeight = 16.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Install Cost: $${String.format("%,.0f", bp.installCostCash)}", color = Color(0xFF00FFCC), fontSize = 12.sp)
                                }
                                
                                Spacer(modifier = Modifier.width(16.dp))
                                
                                if (isEquipped) {
                                    Text("EQUIPPED", color = Color(0xFF00FFCC), fontWeight = FontWeight.Black, fontSize = 12.sp)
                                } else {
                                    Button(
                                        onClick = { onInstall(bp.id) },
                                        enabled = canAfford,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF00FFCC),
                                            contentColor = Color.Black,
                                            disabledContainerColor = Color.DarkGray
                                        )
                                    ) {
                                        Text("Install", fontWeight = FontWeight.Bold)
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
