import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/MarketTab.kt')

# Split visibleProducts and lockedProducts
old_visible_products = """    val visibleProducts = remember(gameState.unlockedTechIds, gameState.inventory, selectedCategory) {
        val baseFiltered = ProductCatalog.ALL_PRODUCTS.filter { product ->
            val hasInventoryStock = gameState.inventory.any { it.itemId == product.id && it.quantity > 0 }
            if (hasInventoryStock) return@filter true

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
            
            when (requiredTechId) {
                null -> true
                "NEVER_BY_TECH" -> false
                else -> gameState.unlockedTechIds.contains(requiredTechId)
            }
        }
                            
        if (selectedCategory != null) {
            baseFiltered.filter { it.category == selectedCategory }
        } else {
            baseFiltered
        }
    }"""

new_visible_products = """    val (visibleProducts, lockedProducts) = remember(gameState.unlockedTechIds, gameState.inventory, selectedCategory) {
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
    }"""

kt = kt.replace(old_visible_products, new_visible_products)

# Find end of LazyColumn in MarketTab
old_market_end = """                onLaunchAdCampaign = onLaunchAdCampaign,
                onHireB2bAgent = onHireB2bAgent,
                onStartMarketResearch = onStartMarketResearch
            )
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}"""

new_market_end = """                onLaunchAdCampaign = onLaunchAdCampaign,
                onHireB2bAgent = onHireB2bAgent,
                onStartMarketResearch = onStartMarketResearch
            )
        }
        
        if (lockedProducts.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                    Text(
                        "Upcoming Milestones",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Research blueprints to unlock these products.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            items(lockedProducts, key = { "locked_" + it.id }) { product ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(product.emoji, fontSize = 24.sp, modifier = Modifier.alpha(0.5f))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                            Text(
                                text = "🔒 Locked Product",
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
}"""

kt = kt.replace(old_market_end, new_market_end)

if "import androidx.compose.ui.draw.alpha" not in kt:
    kt = kt.replace("import androidx.compose.ui.Alignment", "import androidx.compose.ui.Alignment\nimport androidx.compose.ui.draw.alpha")

with open('app/src/main/java/com/example/ui/screens/MarketTab.kt', 'w') as f:
    f.write(kt)

