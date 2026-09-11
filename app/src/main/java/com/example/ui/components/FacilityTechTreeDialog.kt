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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.Building
import com.example.model.FacilityPerk
import com.example.model.FacilityPerkCatalog
import com.example.ui.theme.DairyGoldDark
import com.example.ui.theme.TechCyan

@Composable
fun FacilityTechTreeDialog(
    building: Building,
    onDismiss: () -> Unit,
    onBuyPerk: (String) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("${building.name} Specialization", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("Level ${building.level} Facility", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
                
                HorizontalDivider()
                
                // Branches
                Row(
                    modifier = Modifier.fillMaxWidth().weight(1f).padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // VOLUME BRANCH
                    PerkBranchColumn(
                        branchName = "Automation",
                        icon = Icons.Default.PrecisionManufacturing,
                        branchColor = TechCyan,
                        perks = FacilityPerkCatalog.PERKS.filter { it.branch == "VOLUME" },
                        building = building,
                        onBuyPerk = onBuyPerk,
                        modifier = Modifier.weight(1f)
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // QUALITY BRANCH
                    PerkBranchColumn(
                        branchName = "Boutique",
                        icon = Icons.Default.Star,
                        branchColor = DairyGoldDark,
                        perks = FacilityPerkCatalog.PERKS.filter { it.branch == "QUALITY" },
                        building = building,
                        onBuyPerk = onBuyPerk,
                        modifier = Modifier.weight(1f)
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // ECO BRANCH
                    PerkBranchColumn(
                        branchName = "Eco-Processing",
                        icon = Icons.Default.Recycling,
                        branchColor = Color(0xFF4CAF50),
                        perks = FacilityPerkCatalog.PERKS.filter { it.branch == "ECO" },
                        building = building,
                        onBuyPerk = onBuyPerk,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun PerkBranchColumn(
    branchName: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    branchColor: Color,
    perks: List<FacilityPerk>,
    building: Building,
    onBuyPerk: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = null, tint = branchColor, modifier = Modifier.size(32.dp))
        Text(branchName, fontWeight = FontWeight.Bold, color = branchColor, modifier = Modifier.padding(vertical = 8.dp))
        
        perks.forEach { perk ->
            val isUnlocked = building.unlockedPerks.contains(perk.id)
            val isLockedOut = perk.mutuallyExclusiveWith.any { it in building.unlockedPerks }
            val levelTooLow = building.level < perk.requiredFacilityLevel
            
            val cardColor = when {
                isUnlocked -> branchColor.copy(alpha = 0.2f)
                isLockedOut -> Color.DarkGray.copy(alpha = 0.3f)
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
            val borderColor = if (isUnlocked) branchColor else Color.Transparent
            
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).border(2.dp, borderColor, RoundedCornerShape(8.dp)),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(perk.title, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, fontSize = 14.sp)
                    Text("Tier ${perk.requiredFacilityLevel}", fontSize = 10.sp, color = Color.Gray)
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        when (perk.effectType) {
                            com.example.model.PerkEffect.THROUGHPUT_MULTIPLIER -> "+${((perk.effectValue - 1f) * 100).toInt()}% Capacity"
                            com.example.model.PerkEffect.QUALITY_MULTIPLIER -> "+${((perk.effectValue - 1f) * 100).toInt()}% Quality"
                            com.example.model.PerkEffect.SPOILAGE_CONVERSION -> "Converts Spoilage"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    if (isUnlocked) {
                        Text("UNLOCKED", color = branchColor, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    } else if (isLockedOut) {
                        Text("LOCKED OUT", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    } else if (levelTooLow) {
                        Text("Req. Lvl ${perk.requiredFacilityLevel}", color = Color.Gray, fontSize = 12.sp)
                    } else {
                        Button(
                            onClick = { onBuyPerk(perk.id) },
                            modifier = Modifier.fillMaxWidth().height(36.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("$${String.format("%,.0f", perk.cost)}", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
