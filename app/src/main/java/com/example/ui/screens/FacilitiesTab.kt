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
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import com.example.ui.components.pulseWarning
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Building
import com.example.model.BuildingType
import com.example.model.GameState
import com.example.model.ProductCatalog
import com.example.model.ProjectType
import com.example.model.TechCatalog
import com.example.model.GameAction
import com.example.model.GuidanceState
import com.example.ui.components.GuidanceBadge
import com.example.ui.components.bounceClick
import com.example.ui.components.FacilityTechTreeDialog
import com.example.ui.theme.BearishRed
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.DairyEmeraldDark
import com.example.ui.theme.DairyEmeraldPrimary
import com.example.ui.theme.DairyGold
import com.example.ui.theme.DairyGoldDark
import com.example.ui.theme.TechCyan

@Composable
fun FacilitiesTab(
    gameState: GameState,
    onConstructOrUpgrade: (buildingId: String) -> Unit,
    onUpgradeFactoryCapacity: (buildingId: String) -> Unit,
    onToggleOperational: (buildingId: String) -> Unit,
    onSelectRecipe: (buildingId: String, recipeIndex: Int) -> Unit,
    onUpdateAllocation: (String, Int) -> Unit,
    onOpenTechTree: () -> Unit,
    onBuyLandExpansion: () -> Unit,
    onRushProject: (String) -> Unit,
    onUpdateColdStoragePriority: (com.example.model.ColdStoragePriority) -> Unit,
    onUpdateManualAllocation: (String, Int) -> Unit,
    onSpinOff: (String, String) -> Unit,
    onBuyFacilityPerk: (String, String) -> Unit,
    evaluateAction: (GameAction) -> GuidanceState,
    modifier: Modifier = Modifier
) {
    var specializingBuilding by remember { mutableStateOf<Building?>(null) }

    val isCrewsBusy = remember(gameState.activeProjects, gameState.playerSkills.multiTaskingLevel, gameState.unlockedTechIds) {
        val currentPersonal = gameState.activeProjects.count { !it.isDedicated }
        val capacity = gameState.playerSkills.personalActionCapacity
        val currentDedicated = gameState.activeProjects.count { it.isDedicated && (it.type == ProjectType.FACILITY_CONSTRUCTION || it.type == ProjectType.FACILITY_UPGRADE) }
        val totalDedicated = gameState.ownedConstructionCrews
        currentPersonal >= capacity && currentDedicated >= totalDedicated
    }

    // Strictly filter out locked blueprints: only show if constructed, no tech required, tech unlocked, or under active project
    val visibleBuildings = remember(gameState.buildings, gameState.unlockedTechIds, gameState.activeProjects) {
        gameState.buildings.filter { building ->
            building.isConstructed ||
            building.requiredTechId == null ||
            gameState.unlockedTechIds.contains(building.requiredTechId) ||
            gameState.activeProjects.any { it.targetId == building.id }
        }
    }

    val lockedBuildings = remember(gameState.buildings, visibleBuildings) {
        gameState.buildings.filter { !visibleBuildings.contains(it) }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("facilities_tab"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Facilities Header Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Industrial & Farm Tech Complex",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Process raw milk into high-margin artisanal goods",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text("🏭", fontSize = 28.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Total Daily Maintenance", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    "-$${String.format("%.2f", gameState.totalDailyMaintenance)}/day",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = BearishRed
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Research Progress", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    "${gameState.researchPoints} RP Available",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TechCyan
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    
                    val personalCapacity = gameState.playerSkills.personalActionCapacity
                    val currentPersonalCount = gameState.activeProjects.count { !it.isDedicated }
                    val dedicatedCapacity = gameState.ownedConstructionCrews
                    val currentDedicatedCount = gameState.activeProjects.count { it.isDedicated && (it.type == ProjectType.FACILITY_CONSTRUCTION || it.type == ProjectType.FACILITY_UPGRADE) }
                    
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Personal Pool: $currentPersonalCount / $personalCapacity", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Text("Construction Crews: $currentDedicatedCount Active, ${(dedicatedCapacity - currentDedicatedCount).coerceAtLeast(0)} Idle", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // R&D Shortcut button inside banner
                    FilledTonalButton(
                        onClick = onOpenTechTree,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("facilities_open_tech_tree_button"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Science,
                            contentDescription = "Tech Tree",
                            tint = TechCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "Open R&D Tech Tree (${gameState.researchPoints} RP Available)",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = TechCyan
                        )
                    }
                }
            }
        }

        // Real Estate & Land Capacity Card
        item {
            val landUsed = gameState.usedLand
            val landTotal = gameState.totalLandCapacity
            val landAvailable = gameState.availableLand
            val landCost = gameState.nextLandCost
            val canAffordLand = gameState.cash >= landCost

            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("land_expansion_card"),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, DairyGold.copy(alpha = 0.5f)),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = DairyGold.copy(alpha = 0.04f)
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Landscape,
                                contentDescription = "Estate Land",
                                tint = DairyGoldDark,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Dairy Estate Land",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "$landUsed / $landTotal Plots Occupied ($landAvailable Free)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (landAvailable == 0) BearishRed else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = if (landAvailable == 0) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }

                        Button(
                            onClick = onBuyLandExpansion,
                            enabled = canAffordLand,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DairyGoldDark
                            ),
                            modifier = Modifier.testTag("buy_land_expansion_button")
                        ) {
                            Text(
                                "+1 Plot • $${String.format("%,.0f", landCost)}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { if (landTotal > 0) (landUsed.toFloat() / landTotal.toFloat()).coerceIn(0f, 1f) else 0f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (landAvailable == 0) BearishRed else DairyGold,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }

        // Active Time-Gated Projects Queue
        if (gameState.activeProjects.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("active_projects_card"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = TechCyan.copy(alpha = 0.08f)
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.HourglassTop,
                                contentDescription = "Active Projects",
                                tint = TechCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Active Construction & Research (${gameState.activeProjects.size})",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = TechCyan
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        gameState.activeProjects.forEach { project ->
                            val canAffordRush = gameState.cash >= project.totalRushCost

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, TechCyan.copy(alpha = 0.3f))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "${project.iconEmoji} ${project.targetName}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "⏳ ${project.daysRemaining} days remaining (${(project.progress * 100).toInt()}% done)",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Button(
                                        onClick = { onRushProject(project.id) },
                                        enabled = canAffordRush,
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = DairyGoldDark
                                        ),
                                        modifier = Modifier.testTag("rush_project_${project.id}")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ElectricBolt,
                                            contentDescription = "Rush",
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            "Rush • $${String.format("%,.0f", project.totalRushCost)}",
                                            style = MaterialTheme.typography.labelSmall,
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

        // Section Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Facilities & Operations (${visibleBuildings.size})",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Research blueprints in Tech Tree",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(visibleBuildings, key = { it.id }) { building ->
            val isConstructed = building.isConstructed
            val isTechLocked = building.requiredTechId != null && !gameState.unlockedTechIds.contains(building.requiredTechId)
            val requiredTech = if (building.requiredTechId != null) {
                TechCatalog.ALL_TECHS.firstOrNull { it.id == building.requiredTechId }
            } else null
            val canAfford = gameState.cash >= building.currentCost
            val hasEnoughLand = isConstructed || gameState.availableLand >= building.landRequired

            // Active Project Check
            val activeProject = gameState.activeProjects.firstOrNull {
                it.targetId == building.id && (it.type == ProjectType.FACILITY_CONSTRUCTION || it.type == ProjectType.FACILITY_UPGRADE)
            }
            val isUnderConstruction = activeProject?.type == ProjectType.FACILITY_CONSTRUCTION
            val isUnderUpgrade = activeProject?.type == ProjectType.FACILITY_UPGRADE

            val cardBorderColor = when {
                activeProject != null -> DairyGold.copy(alpha = 0.8f)
                isTechLocked -> Color.Gray.copy(alpha = 0.35f)
                isConstructed -> DairyEmeraldPrimary.copy(alpha = 0.4f)
                else -> TechCyan.copy(alpha = 0.4f)
            }

            val cardBackgroundColor = when {
                activeProject != null -> DairyGold.copy(alpha = 0.05f)
                isTechLocked -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                isConstructed -> MaterialTheme.colorScheme.surface
                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            }

            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("facility_card_${building.id}"),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.2.dp, if (building.sabotagedDaysRemaining > 0) BearishRed else cardBorderColor),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = if (building.sabotagedDaysRemaining > 0) BearishRed.copy(alpha = 0.1f) else cardBackgroundColor
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    if (building.sabotagedDaysRemaining > 0) {
                        Surface(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            color = BearishRed,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                "🚨 BREACHED: INVENTORY DESTROYED",
                                color = androidx.compose.ui.graphics.Color.White,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                    
                    // Header: Icon, Name, Status Badge & Power Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(building.iconEmoji, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = building.name,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isTechLocked) Color.Gray else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = when {
                                        isUnderConstruction -> "🔨 Construction in Progress (${activeProject?.daysRemaining}d left)"
                                        isUnderUpgrade -> "⭐ Upgrade in Progress (${activeProject?.daysRemaining}d left)"
                                        isTechLocked -> "🔒 Locked Blueprint • Requires R&D"
                                        !isConstructed -> "✨ Available Blueprint • Requires ${building.landRequired} Plot • ${building.daysToComplete}d"
                                        else -> "Level ${building.level} • Maint: $${String.format("%.2f", building.currentMaintenance)}/day"
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = when {
                                        activeProject != null -> DairyGoldDark
                                        isTechLocked -> Color.Gray
                                        !isConstructed -> TechCyan
                                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                                    },
                                    fontWeight = if (!isConstructed || activeProject != null) FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        }

                        if (isConstructed && activeProject == null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    if (building.isOperational) "ON" else "OFF",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (building.isOperational) BullishGreen else BearishRed
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Switch(
                                    checked = building.isOperational,
                                    onCheckedChange = { onToggleOperational(building.id) },
                                    enabled = building.sabotagedDaysRemaining == 0,
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = DairyEmeraldPrimary
                                    )
                                )
                            }
                        } else if (activeProject != null) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = DairyGold.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "⏳ ${activeProject.daysRemaining}d",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = DairyGoldDark,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                )
                            }
                        } else if (isTechLocked) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color.Gray.copy(alpha = 0.2f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Locked",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        "Locked",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = building.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Yield / Capacity Stats
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            when (building.type) {
                                BuildingType.PASTURE -> {
                                    Text("Daily Raw Milk Yield:", style = MaterialTheme.typography.labelSmall)
                                    Text("+${building.currentDailyRawProduction} units / day", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                }
                                BuildingType.RD_LAB -> {
                                    Text("Daily Research Output:", style = MaterialTheme.typography.labelSmall)
                                    Text("+${building.currentResearchPoints} RP / day", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                }
                                BuildingType.COLD_STORAGE -> {
                                    Text("Cold Storage Capacity:", style = MaterialTheme.typography.labelSmall)
                                    Text("${building.level * 50} units", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = com.example.ui.theme.TechCyan)
                                }
                                else -> {
                                    Text("FIFO Processing Capacity:", style = MaterialTheme.typography.labelSmall)
                                    Text("${if (isConstructed) building.currentProcessingCapacity else building.maxProcessingCapacity} units / day", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                    
                    if (isConstructed && building.type != BuildingType.PASTURE && building.type != BuildingType.RD_LAB && building.type != BuildingType.COLD_STORAGE && building.activeRecipe != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        val recipe = building.activeRecipe
                        val totalStock = gameState.inventory.filter { it.itemId == recipe.inputItemId && !it.isSpoiled }.sumOf { it.quantity }
                        val requested = (totalStock * (building.allocationPercentage / 100.0)).toInt()
                        
                        val hasWheySubsidiary = gameState.subsidiaryCompanyIds.contains("rival_global_whey")
                        val buffedCapacity = if (hasWheySubsidiary) {
                            ((building.currentProcessingCapacity * 1.5) + 0.5).toInt().coerceAtLeast(building.currentProcessingCapacity + 1)
                        } else {
                            building.currentProcessingCapacity
                        }
                        
                        val maxInputCapacity = buffedCapacity * recipe.inputQuantity
                        val actualDraw = minOf(requested, maxInputCapacity)
                        
                        val outputProduct = com.example.model.ProductCatalog.getById(recipe.outputItemId)
                        val byProductText = if (recipe.byProducts.isNotEmpty()) {
                            val byProds = recipe.byProducts.keys.joinToString(" & ") { com.example.model.ProductCatalog.getById(it).name }
                            " & $byProds"
                        } else ""
                        
                        Text(
                            "Produces: ${outputProduct.name}$byProductText",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp), 
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Allocation: ${building.allocationPercentage}%", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            Text("Est. Draw: $actualDraw Units/Day", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = DairyEmeraldPrimary)
                        }
                        
                        if (actualDraw < requested || (totalStock == 0 && building.allocationPercentage > 0)) {
                            Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                Text("⚠️", fontSize = 14.sp, modifier = Modifier.pulseWarning())
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (totalStock == 0) "Supply Bottleneck: No Raw Material" else "Capacity Bottleneck: Upgrades Required", style = MaterialTheme.typography.labelSmall, color = BearishRed, fontWeight = FontWeight.Bold)
                            }
                        }
                        
                        Slider(
                            value = building.allocationPercentage / 100f,
                            onValueChange = { onUpdateAllocation(building.id, (it * 100).toInt()) },
                            steps = 19,
                            valueRange = 0f..1f,
                            enabled = building.sabotagedDaysRemaining == 0,
                            modifier = Modifier.height(36.dp)
                        )
                    }

                    if (isConstructed && building.type != BuildingType.PASTURE && building.type != BuildingType.RD_LAB && building.type != BuildingType.COLD_STORAGE && !building.isSpunOff) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { specializingBuilding = building },
                                modifier = Modifier.weight(1f).bounceClick(),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                            ) {
                                Text("Specializations", fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = { onSpinOff(building.id, "${building.name} Corp") },
                                modifier = Modifier.weight(1f).bounceClick(),
                                colors = ButtonDefaults.buttonColors(containerColor = DairyGoldDark)
                            ) {
                                Text("Spin Off", color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Recipe Switcher for Facilities with multiple recipes
                    if (isConstructed && building.availableRecipes.size > 1) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "Select Active Recipe (FIFO Target):",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            building.availableRecipes.forEachIndexed { idx, recipe ->
                                val outputProduct = ProductCatalog.getById(recipe.outputItemId)
                                val isSelected = building.activeRecipe?.outputItemId == recipe.outputItemId

                                FilterChip(
                                    selected = isSelected,
                                    onClick = { onSelectRecipe(building.id, idx) },
                                    enabled = building.sabotagedDaysRemaining == 0,
                                    label = {
                                        Text("${outputProduct.emoji} ${outputProduct.name} (${recipe.inputQuantity}x🥛)")
                                    },
                                    leadingIcon = if (isSelected) {
                                        { Icon(Icons.Default.Check, contentDescription = "Active", modifier = Modifier.size(14.dp)) }
                                    } else null,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = DairyEmeraldPrimary.copy(alpha = 0.2f),
                                        selectedLabelColor = DairyEmeraldDark
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isConstructed && building.type != BuildingType.PASTURE && building.type != BuildingType.RD_LAB && building.type != BuildingType.COLD_STORAGE) {
                        val reqTechId = when (building.capacityTier) {
                            1 -> "tech_industrial_throughput_1"
                            2 -> "tech_industrial_throughput_2"
                            3 -> "tech_industrial_throughput_3"
                            else -> null
                        }
                        
                        val isTechUnlocked = reqTechId == null || gameState.unlockedTechIds.contains(reqTechId)
                        val canAffordCapacity = gameState.cash >= building.capacityUpgradeCost
                        
                        if (building.capacityTier < 4) {
                            if (!isTechUnlocked) {
                                OutlinedButton(
                                    onClick = onOpenTechTree,
                                    modifier = Modifier.fillMaxWidth().height(40.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, TechCyan)
                                ) {
                                    Icon(Icons.Default.Science, contentDescription = "Locked", tint = TechCyan, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Research Industry Tier ${building.capacityTier} to Unlock", style = MaterialTheme.typography.labelSmall, color = TechCyan)
                                }
                            } else {
                                Button(
                                    onClick = { onUpgradeFactoryCapacity(building.id) },
                                    enabled = canAffordCapacity && building.sabotagedDaysRemaining == 0,
                                    modifier = Modifier.fillMaxWidth().height(40.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = TechCyan)
                                ) {
                                    Icon(Icons.Default.Speed, contentDescription = "Throughput", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Upgrade Capacity (Tier ${building.capacityTier + 1}) • $${String.format("%,.0f", building.capacityUpgradeCost)}", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        } else {
                            OutlinedButton(
                                onClick = { },
                                enabled = false,
                                modifier = Modifier.fillMaxWidth().height(40.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, DairyEmeraldPrimary)
                            ) {
                                Text("Max Throughput Capacity Reached", style = MaterialTheme.typography.labelSmall, color = DairyEmeraldPrimary)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    if (isConstructed && building.type == BuildingType.COLD_STORAGE) {
                        ColdStorageControls(
                            priority = gameState.coldStoragePriority,
                            onUpdatePriority = onUpdateColdStoragePriority,
                            allocations = gameState.manualColdStorageAllocations,
                            onUpdateAllocation = onUpdateManualAllocation,
                            totalCapacity = gameState.globalColdStorageCapacity
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Construct / Upgrade / Rush OR Unlock in Tech Tree Button
                    if (activeProject != null) {
                        val canRush = gameState.cash >= activeProject.totalRushCost
                        Button(
                            onClick = { onRushProject(activeProject.id) },
                            enabled = canRush,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .testTag("rush_${building.id}"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DairyGoldDark
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ElectricBolt,
                                contentDescription = "Rush Now",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Instant Rush (${activeProject.daysRemaining}d left) • $${String.format("%,.0f", activeProject.totalRushCost)}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else if (isTechLocked) {
                        OutlinedButton(
                            onClick = onOpenTechTree,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .testTag("unlock_blueprint_${building.id}"),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, TechCyan)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Science,
                                contentDescription = "Unlock in R&D",
                                tint = TechCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Research in Tech Tree (${requiredTech?.name ?: "R&D"} • ${requiredTech?.rpCost ?: 0} RP)",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = TechCyan
                            )
                        }
                    } else if (!isConstructed && !hasEnoughLand) {
                        OutlinedButton(
                            onClick = onBuyLandExpansion,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .testTag("need_land_${building.id}")
                                .pulseWarning(),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, BearishRed)
                        ) {
                            Text(
                                text = "⚠️ Insufficient Land (${building.landRequired} Plot Required • Expand Estate)",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = BearishRed
                            )
                        }
                    } else if (isCrewsBusy) {
                        OutlinedButton(
                            onClick = { },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color.Gray)
                        ) {
                            Text(
                                text = "All Slots & Crews Busy",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                        }
                    } else {
                        Button(
                            onClick = { onConstructOrUpgrade(building.id) },
                            enabled = canAfford && building.sabotagedDaysRemaining == 0,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .testTag("upgrade_${building.id}"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (!isConstructed) DairyEmeraldPrimary else DairyGoldDark
                            )
                        ) {
                            Icon(
                                imageVector = if (!isConstructed) Icons.Default.Build else Icons.Default.Upgrade,
                                contentDescription = "Build/Upgrade",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            val action = if (!isConstructed) GameAction.Construct(building.id) else GameAction.Upgrade(building.id)
                            val guidance = evaluateAction(action)
                            GuidanceBadge(state = guidance)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (!isConstructed) "Start Construction (${building.daysToComplete}d) • $${String.format("%,.0f", building.currentCost)}" else "Begin Upgrade (${building.daysToComplete}d) • $${String.format("%,.0f", building.currentCost)}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        if (lockedBuildings.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                    Text(
                        "Upcoming Milestones",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Research blueprints in the Tech Tree to unlock these facilities.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            items(lockedBuildings, key = { "locked_" + it.id }) { 
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(it.iconEmoji, fontSize = 24.sp, modifier = Modifier.alpha(0.5f))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                            Text(
                                text = "🔒 Locked Blueprint",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                        Icon(Icons.Default.Lock, contentDescription = "Locked", tint = Color.Gray)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    specializingBuilding?.let { building ->
        FacilityTechTreeDialog(
            building = building,
            onDismiss = { specializingBuilding = null },
            onBuyPerk = { perkId ->
                onBuyFacilityPerk(building.id, perkId)
            }
        )
    }
}

@Composable
fun ColdStorageControls(
    priority: com.example.model.ColdStoragePriority,
    onUpdatePriority: (com.example.model.ColdStoragePriority) -> Unit,
    allocations: Map<String, Int>,
    onUpdateAllocation: (String, Int) -> Unit,
    totalCapacity: Int
) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        border = BorderStroke(1.dp, com.example.ui.theme.TechCyan.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Cold Storage Priority Mode", style = MaterialTheme.typography.titleSmall, color = com.example.ui.theme.TechCyan)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf(
                    com.example.model.ColdStoragePriority.OLDEST_FIRST to "Oldest",
                    com.example.model.ColdStoragePriority.MOST_EXPENSIVE to "Highest Value",
                    com.example.model.ColdStoragePriority.MANUAL to "Manual"
                ).forEach { (mode, label) ->
                    val isSelected = priority == mode
                    FilterChip(
                        selected = isSelected,
                        onClick = { onUpdatePriority(mode) },
                        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                        colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
                            selectedContainerColor = com.example.ui.theme.TechCyan.copy(alpha = 0.2f),
                            selectedLabelColor = com.example.ui.theme.TechCyan
                        )
                    )
                }
            }

            if (priority == com.example.model.ColdStoragePriority.MANUAL) {
                Spacer(modifier = Modifier.height(12.dp))
                Text("Manual Allocations (Max: $totalCapacity)", style = MaterialTheme.typography.labelSmall)
                val used = allocations.values.sum()
                Text("Used: $used / $totalCapacity", style = MaterialTheme.typography.labelSmall, color = if (used > totalCapacity) com.example.ui.theme.BearishRed else MaterialTheme.colorScheme.onSurfaceVariant)
                
                Spacer(modifier = Modifier.height(8.dp))
                com.example.model.ProductCatalog.ALL_PRODUCTS.filter { it.shelfLifeDays < 999 && it.id != com.example.model.ProductCatalog.SPOILED_MILK.id }.forEach { product ->
                    val currentAlloc = allocations[product.id] ?: 0
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Text(product.name, modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall)
                        androidx.compose.material3.Slider(
                            value = currentAlloc.toFloat(),
                            onValueChange = { 
                                val newAlloc = it.toInt()
                                val newUsed = used - currentAlloc + newAlloc
                                if (newUsed <= totalCapacity) {
                                    onUpdateAllocation(product.id, newAlloc)
                                }
                            },
                            valueRange = 0f..totalCapacity.toFloat(),
                            modifier = Modifier.weight(2f)
                        )
                        Text("$currentAlloc", modifier = Modifier.width(30.dp), style = MaterialTheme.typography.labelSmall, textAlign = androidx.compose.ui.text.style.TextAlign.End)
                    }
                }
            }
        }
    }
}
