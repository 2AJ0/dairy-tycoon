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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameState
import com.example.model.MarketItemState
import com.example.model.GameAction
import com.example.model.GuidanceState
import com.example.model.BuildingType
import com.example.model.ProductCategory
import com.example.ui.components.GuidanceBadge
import com.example.model.Product
import com.example.model.ProductCatalog
import com.example.ui.theme.*

@Composable
fun MarketTab(
    gameState: GameState,
    onSellProduct: (String, Int) -> Unit,
    onSellAllOfProduct: (String) -> Unit,
    onToggleAutoBuy: (String, Boolean) -> Unit,
    onToggleAutoSell: (String, Boolean) -> Unit,
    evaluateAction: (com.example.model.GameAction) -> com.example.model.GuidanceState,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<ProductCategory?>(null) }

    val (visibleProducts, lockedProducts) = remember(gameState.unlockedTechIds, gameState.inventory, selectedCategory) {
        val visible = mutableListOf<Product>()
        val locked = mutableListOf<Product>()
        
        for (product in ProductCatalog.ALL_PRODUCTS) {
            val hasInventoryStock = gameState.inventory.any { it.itemId == product.id && it.quantity > 0 }
            if (hasInventoryStock) {
                visible.add(product)
                continue
            }

            val requiredTechId = when (product.id) {
                ProductCatalog.RAW_MILK.id -> null
                ProductCatalog.COW_FEED.id -> null
                ProductCatalog.GLASS_BOTTLES.id -> "tech_pasteurization"
                ProductCatalog.PASTEURIZED_MILK.id -> "tech_pasteurization"
                ProductCatalog.CREAM.id -> "tech_fermentation"
                ProductCatalog.BUTTER.id -> "tech_fermentation"
                ProductCatalog.FRESH_CHEESE.id -> "tech_cheese_aging"
                ProductCatalog.AGED_CHEDDAR.id -> "tech_cheese_aging"
                ProductCatalog.SPOILED_MILK.id -> "NEVER_BY_TECH"
                ProductCatalog.WHEY.id -> "tech_cheese_aging"
                ProductCatalog.PROTEIN_ISOLATE.id -> "tech_industrial_throughput_2"
                ProductCatalog.SLUDGE.id -> "tech_pasteurization"
                ProductCatalog.ANIMAL_FEED.id -> "tech_industrial_throughput_1"
                else -> null
            }
            
            if (requiredTechId == "NEVER_BY_TECH") {
                continue // don't show sludge/spoiled milk in locked
            }
            
            val isUnlocked = requiredTechId == null || gameState.unlockedTechIds.contains(requiredTechId)
            if (isUnlocked) {
                visible.add(product)
            } else {
                locked.add(product)
            }
        }
        
        if (selectedCategory != null) {
            Pair(visible.filter { it.category == selectedCategory }, locked.filter { it.category == selectedCategory })
        } else {
            Pair(visible, locked)
        }
    }
            
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("market_tab"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("market_header_card"),
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
                                    imageVector = Icons.Default.ShowChart,
                                    contentDescription = "Exchange",
                                    tint = BullishGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "Dairy Commodity Exchange",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                "Live wholesale spot pricing & FIFO liquidation desk",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text("📈", fontSize = 26.sp)
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
                                Text(
                                    "Reputation Multiplier",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    "+${String.format("%.1f", gameState.reputation * 0.3)}% Price Bonus",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = DairyGold
                                )
                            }
                        }
                                                    
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    "Supply Rule",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    "High sales reduce next-day bids",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
                                                                                                            
        gameState.activeNewsEvent?.let { news ->
            item {
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("active_market_news_banner"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = (if (news.isProductSpike) BullishGreen else if (news.isProductSlump) BearishRed else DairyGold).copy(alpha = 0.08f)
                    ),
                    border = BorderStroke(
                        1.dp,
                        (if (news.isProductSpike) BullishGreen else if (news.isProductSlump) BearishRed else DairyGold).copy(alpha = 0.5f)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(news.iconEmoji, fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "DAILY CHRONICLE BULLETIN",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Black,
                                    color = if (news.isProductSpike) BullishGreen else if (news.isProductSlump) BearishRed else DairyGoldDark
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = "${news.remainingDays}d remaining",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                                                    
                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = news.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = news.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val target = news.targetProductId?.let { ProductCatalog.getById(it) }
                            Text(
                                text = if (target != null) "Affected: ${target.name}" else "Scope: Global Market",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text = "${news.percentageFormatted} Price Effect",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (news.isProductSpike) BullishGreen else if (news.isProductSlump) BearishRed else DairyGoldDark
                            )
                        }
                    }
                }
            }
        }
                                                                                
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Wholesale Market Board (${visibleProducts.size} Unlocked)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Prices update daily on 'End Day'",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                                
                androidx.compose.foundation.lazy.LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    item {
                        androidx.compose.material3.FilterChip(
                            selected = selectedCategory == null,
                            onClick = { selectedCategory = null },
                            label = { Text("All") }
                        )
                    }
                    val categories = com.example.model.ProductCategory.values()
                    items(categories.size) { index ->
                        val cat = categories[index]
                        androidx.compose.material3.FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat.name.lowercase().replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }
            }
        }
                                                        
        items(visibleProducts, key = { it.id }) { product ->
            val marketState = gameState.marketPrices[product.id] ?: MarketItemState(
                itemId = product.id,
                currentPrice = product.basePrice,
                basePrice = product.basePrice
            )

            val currentPrice = marketState.currentPrice
            val basePrice = product.basePrice
            val isAboveBase = currentPrice >= basePrice
            val diffPercentFromBase = if (basePrice > 0) ((currentPrice - basePrice) / basePrice) * 100.0 else 0.0

            val isSpoiledMilkProduct = product.id == ProductCatalog.SPOILED_MILK.id
            val matchingBatches = gameState.inventory.filter { it.itemId == product.id }
            val totalInStock = matchingBatches.sumOf { it.quantity }
            val eligibleBatches = matchingBatches.filter { isSpoiledMilkProduct || !it.isSpoiled }
            val sellableUnits = eligibleBatches.sumOf { it.quantity }
            val spoiledUnitsInProduct = totalInStock - sellableUnits
            val unitsSoldToday = gameState.todaySoldUnits[product.id] ?: 0

            val repMultiplier = 1.0 + (gameState.reputation / 100.0) * gameState.playerSkills.silverTongueRepBonusMultiplier
            MarketProductCard(
                product = product,
                marketState = marketState,
                repMultiplier = repMultiplier,
                currentPrice = currentPrice,
                basePrice = basePrice,
                isAboveBase = isAboveBase,
                diffPercentFromBase = diffPercentFromBase,
                totalInStock = totalInStock,
                sellableUnits = sellableUnits,
                spoiledUnitsInProduct = spoiledUnitsInProduct,
                unitsSoldToday = unitsSoldToday,
                isAutoBuyEnabled = gameState.autoBuySubscriptions[product.id] == true,
                isAutoSellEnabled = gameState.autoSellSubscriptions[product.id] == true,
                dailyUsageNeeded = if (product.id == "cow_feed") {
                    gameState.buildings.filter { it.type == BuildingType.PASTURE && it.isConstructed && it.isOperational }.sumOf { it.level }
                } else if (product.id == "glass_bottles") {
                    gameState.buildings.filter { it.isConstructed && it.isOperational && it.activeRecipe?.outputItemId == "pasteurized_milk" }.sumOf { it.currentProcessingCapacity }
                } else 0,
                onToggleAutoBuy = { isChecked -> onToggleAutoBuy(product.id, isChecked) },
                onToggleAutoSell = { isChecked -> onToggleAutoSell(product.id, isChecked) },
                onSellProduct = onSellProduct,
                onSellAll = { onSellAllOfProduct(product.id) },
                evaluateAction = evaluateAction
            )
        }
        
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
            
@Composable
fun MarketProductCard(
    product: Product,
    marketState: MarketItemState,
    repMultiplier: Double,
    currentPrice: Double,
    basePrice: Double,
    isAboveBase: Boolean,
    diffPercentFromBase: Double,
    totalInStock: Int,
    sellableUnits: Int,
    spoiledUnitsInProduct: Int,
    unitsSoldToday: Int,
    isAutoBuyEnabled: Boolean,
    isAutoSellEnabled: Boolean,
    dailyUsageNeeded: Int,
    onToggleAutoBuy: (Boolean) -> Unit,
    onToggleAutoSell: (Boolean) -> Unit,
    onSellProduct: (productType: String, amount: Int) -> Unit,
    onSellAll: () -> Unit,
    evaluateAction: (GameAction) -> GuidanceState
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("market_card_${product.id}"),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            1.dp,
            if (sellableUnits > 0) DairyEmeraldPrimary.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = "Standard Base: $${String.format("%.2f", basePrice)} • Shelf life: ${product.shelfLifeDays}d",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                                    
                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (repMultiplier > 1.0) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Reputation Premium",
                                tint = BullishGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = "$${String.format("%.2f", currentPrice)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = if (isAboveBase) BullishGreen else BearishRed,
                            modifier = Modifier.testTag("price_${product.id}")
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (isAboveBase) BullishGreen.copy(alpha = 0.16f) else BearishRed.copy(alpha = 0.16f),
                        modifier = Modifier.testTag("trend_${product.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isAboveBase) "▲" else "▼",
                                color = if (isAboveBase) BullishGreen else BearishRed,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${if (isAboveBase) "+" else ""}${String.format("%.1f", diffPercentFromBase)}% vs Base",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = if (isAboveBase) BullishGreen else BearishRed
                            )
                        }
                    }
                }
            }
                                                                        
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Warehouse Stock: ",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$sellableUnits sellable units",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (sellableUnits > 0) BullishGreen else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (spoiledUnitsInProduct > 0) {
                        Text(
                            text = " ($spoiledUnitsInProduct spoiled)",
                            style = MaterialTheme.typography.labelSmall,
                            color = BearishRed,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                                    
                if (unitsSoldToday > 0) {
                    Text(
                        text = "Sold today: $unitsSoldToday",
                        style = MaterialTheme.typography.labelSmall,
                        color = DairyGold,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
                            
            Spacer(modifier = Modifier.height(10.dp))

            if (product.category == com.example.model.ProductCategory.CONSUMABLE) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Auto-Procurement",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (isAutoBuyEnabled) {
                            val projectedCost = dailyUsageNeeded * currentPrice
                            Text(
                                text = "Auto: -$${String.format("%.2f", projectedCost)}/day (${dailyUsageNeeded} units)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    androidx.compose.material3.Switch(
                        checked = isAutoBuyEnabled,
                        onCheckedChange = { isChecked -> onToggleAutoBuy(isChecked) }
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { onSellProduct(product.id, 1) },
                        enabled = sellableUnits >= 1,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .weight(1f)
                            .testTag("sell_1_${product.id}")
                    ) {
                        Text("Sell 1", style = MaterialTheme.typography.labelSmall)
                    }
                    
                    OutlinedButton(
                        onClick = { onSellProduct(product.id, 5) },
                        enabled = sellableUnits >= 5,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .weight(1f)
                            .testTag("sell_5_${product.id}")
                    ) {
                        Text("Sell 5", style = MaterialTheme.typography.labelSmall)
                    }
                    
                    if (sellableUnits >= 10) {
                        OutlinedButton(
                            onClick = { onSellProduct(product.id, 10) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .height(34.dp)
                                .weight(1f)
                                .testTag("sell_10_${product.id}")
                        ) {
                            Text("Sell 10", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                                            
                    Button(
                        onClick = onSellAll,
                        enabled = sellableUnits > 0,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DairyEmeraldPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier
                            .height(34.dp)
                            .weight(1.3f)
                            .testTag("sell_all_${product.id}")
                    ) {
                        val guidance = evaluateAction(com.example.model.GameAction.Sell(product.id))
                        GuidanceBadge(state = guidance)
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            Icons.Default.Sell,
                            contentDescription = "Sell All",
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (sellableUnits > 0) "Sell All ($sellableUnits)" else "No Stock",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
                                                                        
            if (product.category == com.example.model.ProductCategory.BYPRODUCT) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Auto-Sell Operations",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Automatically liquidates all stock at end of day.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    androidx.compose.material3.Switch(
                        checked = isAutoSellEnabled,
                        onCheckedChange = { isChecked -> onToggleAutoSell(isChecked) }
                    )
                }
            }
        }
    }
}
