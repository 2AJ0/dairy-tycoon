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
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameState
import com.example.model.InventoryBatch
import com.example.model.Product
import com.example.model.ProductCatalog
import com.example.model.ProductCategory
import com.example.ui.theme.BearishRed
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.DairyCream
import com.example.ui.theme.DairyEmeraldDark
import com.example.ui.theme.DairyEmeraldPrimary
import com.example.ui.theme.DairyGold
import com.example.ui.theme.QualityPurple

@Composable
fun InventoryTab(
    gameState: GameState,
    onSellProduct: (productType: String, amount: Int) -> Unit,
    onSellBatch: (batchId: String, qty: Int) -> Unit,
    onSellAllOfProduct: (productId: String) -> Unit,
    onDiscardSpoiled: () -> Unit,
    onSetInventoryMethod: (com.example.model.InventoryMethod) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalUnits = gameState.totalInventoryQuantity
    val totalEstValue = gameState.totalInventoryValue

    // Identify spoiled batches across the entire inventory
    val allSpoiledBatches = gameState.inventory.filter {
        it.isSpoiled || it.itemId == ProductCatalog.SPOILED_MILK.id
    }
    val totalSpoiledUnits = allSpoiledBatches.sumOf { it.quantity }

    // Group inventory by product type for Data Grouping requirement (only displaying items with held stock > 0)
    val inventoryByProduct = remember(gameState.inventory) {
        // Group existing batches by productId, preserving catalog order and filtering only held stock
        val nonZeroBatches = gameState.inventory.filter { it.quantity > 0 }
        val grouped = nonZeroBatches.groupBy { it.itemId }
        val catalogOrder = ProductCatalog.ALL_PRODUCTS.map { it.id }
        grouped.entries
            .filter { it.value.isNotEmpty() && it.value.sumOf { b -> b.quantity } > 0 }
            .sortedBy { entry ->
                val idx = catalogOrder.indexOf(entry.key)
                if (idx == -1) 999 else idx
            }
    }

    // Expansion state for each product group (default: expanded)
    val expandedStates = remember { mutableStateMapOf<String, Boolean>() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("inventory_tab"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. Warehouse Overview Header Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("inventory_header_card"),
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Inventory2,
                                    contentDescription = "Warehouse",
                                    tint = DairyEmeraldPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "Warehouse & Batches",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Surface(
                            color = DairyEmeraldPrimary.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "$totalUnits Units Total",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = DairyEmeraldPrimary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = gameState.inventoryMethod == com.example.model.InventoryMethod.FIFO,
                            onClick = { onSetInventoryMethod(com.example.model.InventoryMethod.FIFO) },
                            label = { Text("FIFO", fontWeight = FontWeight.Bold) },
                            leadingIcon = { if (gameState.inventoryMethod == com.example.model.InventoryMethod.FIFO) Icon(Icons.Default.Check, null) }
                        )
                        FilterChip(
                            selected = gameState.inventoryMethod == com.example.model.InventoryMethod.LIFO,
                            onClick = { onSetInventoryMethod(com.example.model.InventoryMethod.LIFO) },
                            label = { Text("LIFO", fontWeight = FontWeight.Bold) },
                            leadingIcon = { if (gameState.inventoryMethod == com.example.model.InventoryMethod.LIFO) Icon(Icons.Default.Check, null) }
                        )
                    }

                    Text(
                        text = if (gameState.inventoryMethod == com.example.model.InventoryMethod.FIFO)
                            "First-In, First-Out: Processes oldest stock first. Safest against spoilage."
                        else
                            "Last-In, First-Out: Processes freshest stock first. Best quality output, but older stock rots faster.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Estimated Inventory Value:",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "$${String.format("%,.2f", totalEstValue)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BullishGreen
                        )
                    }

                    // Spoilage Alert Banner & Action Button
                    if (totalSpoiledUnits > 0) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = BearishRed.copy(alpha = 0.14f)
                            ),
                            border = BorderStroke(1.dp, BearishRed.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = "Spoilage Alert",
                                        tint = BearishRed,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            "Ruined Inventory Alert",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = BearishRed
                                        )
                                        Text(
                                            "$totalSpoiledUnits spoiled units consuming warehouse capacity",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = BearishRed.copy(alpha = 0.9f)
                                        )
                                    }
                                }

                                Button(
                                    onClick = onDiscardSpoiled,
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = BearishRed,
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .height(36.dp)
                                        .testTag("dump_spoiled_goods_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteSweep,
                                        contentDescription = "Dump Spoiled Goods",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "Dump Spoiled Goods",
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

        // Empty Inventory State
        if (gameState.inventory.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📦", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "Warehouse is completely empty!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Tap 'Work' above or End Day to harvest pasture milk.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Product Inventory Groups (${inventoryByProduct.size})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Oldest batches at top (FIFO)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 2. Grouped Product Master Rows with Batch Breakdown
            items(inventoryByProduct, key = { it.key }) { (productId, batches) ->
                val product = ProductCatalog.getById(productId)
                val isExpanded = expandedStates[productId] ?: true
                val totalGroupUnits = batches.sumOf { it.quantity }
                val unspoiledBatches = batches.filter { !it.isSpoiled && it.itemId != ProductCatalog.SPOILED_MILK.id }
                val unspoiledUnits = unspoiledBatches.sumOf { it.quantity }
                val hasSpoiledInGroup = batches.any { it.isSpoiled || it.itemId == ProductCatalog.SPOILED_MILK.id }

                val marketState = gameState.marketPrices[productId]
                val currentMarketPrice = marketState?.currentPrice ?: product.basePrice

                val groupEstimatedValue = batches.sumOf { b ->
                    val unitVal = currentMarketPrice * (0.8 + (b.quality * 0.2))
                    b.quantity * unitVal
                }

                ProductGroupMasterCard(
                    product = product,
                    batches = batches,
                    totalGroupUnits = totalGroupUnits,
                    unspoiledUnits = unspoiledUnits,
                    hasSpoiledInGroup = hasSpoiledInGroup,
                    groupEstimatedValue = groupEstimatedValue,
                    currentMarketPrice = currentMarketPrice,
                    isExpanded = isExpanded,
                    onToggleExpand = { expandedStates[productId] = !isExpanded },
                    onSellFIFO = { amt -> onSellProduct(productId, amt) },
                    onSellBatch = onSellBatch
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

/**
 * Master row/card grouping all batches of a specific Product Type.
 */
@Composable
fun ProductGroupMasterCard(
    product: Product,
    batches: List<InventoryBatch>,
    totalGroupUnits: Int,
    unspoiledUnits: Int,
    hasSpoiledInGroup: Boolean,
    groupEstimatedValue: Double,
    currentMarketPrice: Double,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onSellFIFO: (amount: Int) -> Unit,
    onSellBatch: (batchId: String, qty: Int) -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("product_group_${product.id}"),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            1.dp,
            if (hasSpoiledInGroup) BearishRed.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Master Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(product.emoji, fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = product.category.name,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }

                        Text(
                            text = "${batches.size} batch${if (batches.size > 1) "es" else ""} • $totalGroupUnits units in stock ($unspoiledUnits unspoiled)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "$${String.format("%,.2f", groupEstimatedValue)}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = BullishGreen
                        )
                        Text(
                            text = "$${String.format("%.2f", currentMarketPrice)}/unit base",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Quick Group FIFO Sell Actions
            if (unspoiledUnits > 0) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (unspoiledUnits > 5) {
                        OutlinedButton(
                            onClick = { onSellFIFO(5) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .height(32.dp)
                                .weight(1f)
                                .testTag("sell_5_fifo_${product.id}")
                        ) {
                            Text("Sell 5 (FIFO)", style = MaterialTheme.typography.labelSmall)
                        }
                    }

                    if (unspoiledUnits > 20) {
                        OutlinedButton(
                            onClick = { onSellFIFO(10) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .height(32.dp)
                                .weight(1f)
                                .testTag("sell_10_fifo_${product.id}")
                        ) {
                            Text("Sell 10 (FIFO)", style = MaterialTheme.typography.labelSmall)
                        }
                    }

                    FilledTonalButton(
                        onClick = { onSellFIFO(unspoiledUnits) },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(32.dp)
                            .weight(1.2f)
                            .testTag("sell_all_fifo_${product.id}")
                    ) {
                        Icon(
                            Icons.Default.Sell,
                            contentDescription = "Sell All",
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Sell All FIFO ($unspoiledUnits)",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // 3. Batch Breakdown (Expandable FIFO Queue Items)
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    Text(
                        text = "Individual FIFO Queue Batches:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    batches.forEachIndexed { index, batch ->
                        BatchCardItem(
                            fifoPosition = index + 1,
                            batch = batch,
                            product = product,
                            currentMarketPrice = currentMarketPrice,
                            onSellBatch = onSellBatch
                        )
                    }
                }
            }
        }
    }
}

/**
 * Individual Batch Card showing Quantity, Quality Score, Age, and Spoilage Visuals.
 */
@Composable
fun BatchCardItem(
    fifoPosition: Int,
    batch: InventoryBatch,
    product: Product,
    currentMarketPrice: Double,
    onSellBatch: (batchId: String, qty: Int) -> Unit
) {
    val isSpoiled = batch.isSpoiled
    val isSpoiledMilkCategory = batch.itemId == ProductCatalog.SPOILED_MILK.id
    val effectiveUnitPrice = currentMarketPrice * (0.8 + (batch.quality * 0.2))
    val batchEstimatedValue = batch.quantity * effectiveUnitPrice

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("batch_item_${batch.batchId}"),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSpoiled) {
                BearishRed.copy(alpha = 0.12f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = BorderStroke(
            width = if (isSpoiled) 1.5.dp else 1.dp,
            color = if (isSpoiled) BearishRed else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
        )
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Top Batch Header with FIFO indicator, Quality, and Spoilage tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // FIFO Queue Priority Badge
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (fifoPosition == 1) DairyEmeraldPrimary.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = if (fifoPosition == 1) "#1 Oldest (FIFO Next)" else "#$fifoPosition in Queue",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = if (fifoPosition == 1) DairyEmeraldDark else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Quality Score
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = QualityPurple.copy(alpha = 0.15f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = "Quality",
                                tint = QualityPurple,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${String.format("%.1f", batch.quality)}★ Quality",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = QualityPurple
                            )
                        }
                    }

                    if (batch.isProtectedToday) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            androidx.compose.material.icons.Icons.Default.Star,
                            contentDescription = "Cold Storage Protected",
                            tint = com.example.ui.theme.TechCyan,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                // Spoilage Visual Badge
                if (isSpoiled) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = BearishRed
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = "Spoiled",
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "SPOILED",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Main Batch Info: Quantity and Age
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${batch.quantity} Units",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (isSpoiled && !isSpoiledMilkCategory) TextDecoration.LineThrough else TextDecoration.None,
                        color = if (isSpoiled) BearishRed else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Produced Day ${batch.dayProduced} • Age: ${batch.daysOld} / ${batch.maxShelfLife} Days",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSpoiled) BearishRed else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$${String.format("%.2f", batchEstimatedValue)}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isSpoiled) BearishRed else BullishGreen
                    )
                    Text(
                        text = "$${String.format("%.2f", effectiveUnitPrice)}/ea",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Freshness Progress Countdown Bar
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isSpoiled) "Expired (${batch.daysOld}d old > max ${batch.maxShelfLife}d)" else "Shelf Life: ${batch.daysRemaining} days remaining",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        color = if (isSpoiled) BearishRed else if (batch.daysRemaining <= 1) BearishRed else if (batch.daysRemaining <= 3) DairyGold else BullishGreen
                    )
                    Text(
                        text = "${(batch.freshnessFraction * 100).toInt()}% Fresh",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                LinearProgressIndicator(
                    progress = { batch.freshnessFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (isSpoiled) BearishRed else if (batch.freshnessFraction < 0.35f) BearishRed else if (batch.freshnessFraction < 0.65f) DairyGold else BullishGreen,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            // Batch Action Buttons (Disabled if spoiled unless it is specifically spoiled milk by-product)
            Spacer(modifier = Modifier.height(8.dp))
            if (isSpoiled && !isSpoiledMilkCategory) {
                Surface(
                    color = BearishRed.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "⚠️ Ruined batch cannot be sold as regular product. Use 'Dump Spoiled Goods' above to clean up.",
                        style = MaterialTheme.typography.labelSmall,
                        color = BearishRed,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (batch.quantity > 5) {
                        OutlinedButton(
                            onClick = { onSellBatch(batch.batchId, 5) },
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .height(28.dp)
                                .testTag("sell_5_batch_${batch.batchId}")
                        ) {
                            Text("Sell 5", style = MaterialTheme.typography.labelSmall, fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    FilledTonalButton(
                        onClick = { onSellBatch(batch.batchId, batch.quantity) },
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .height(28.dp)
                            .testTag("sell_batch_${batch.batchId}")
                    ) {
                        Text(
                            "Sell Batch (${batch.quantity})",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
