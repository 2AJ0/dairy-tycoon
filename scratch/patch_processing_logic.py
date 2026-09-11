import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

# Step 3b modifications
old_step3b = """                // Step 3b: Processing Facilities (FIFO Queue Consumption from oldest unspoiled batches)
                val hasWheySubsidiary = currentState.subsidiaryCompanyIds.contains("rival_global_whey")
                effectiveBuildings.filter { it.isConstructed && it.isOperational && it.sabotagedDaysRemaining <= 0 && it.activeRecipe != null && !it.isSpunOff }.forEach { building ->
                    val recipe = building.activeRecipe ?: return@forEach
                    var remainingCapacity = building.currentProcessingCapacity
                    if (hasWheySubsidiary) {
                        remainingCapacity = ((remainingCapacity * 1.5) + 0.5).toInt().coerceAtLeast(remainingCapacity + 1)
                    }"""

new_step3b = """                // Step 3b: Processing Facilities (FIFO Queue Consumption from oldest unspoiled batches)
                val hasWheySubsidiary = currentState.subsidiaryCompanyIds.contains("rival_global_whey")
                effectiveBuildings.filter { it.isConstructed && it.isOperational && it.sabotagedDaysRemaining <= 0 && it.activeRecipe != null && !it.isSpunOff }.forEach { building ->
                    val recipe = building.activeRecipe ?: return@forEach
                    
                    var throughputMult = 1.0f
                    var qualityMult = 1.0f
                    building.unlockedPerks.forEach { perkId ->
                        val perk = com.example.model.FacilityPerkCatalog.PERKS.find { it.id == perkId }
                        if (perk != null) {
                            if (perk.effectType == com.example.model.PerkEffect.THROUGHPUT_MULTIPLIER) throughputMult += (perk.effectValue - 1.0f)
                            if (perk.effectType == com.example.model.PerkEffect.QUALITY_MULTIPLIER) qualityMult += (perk.effectValue - 1.0f)
                        }
                    }
                    
                    var remainingCapacity = (building.currentProcessingCapacity * throughputMult).toInt()
                    if (hasWheySubsidiary) {
                        remainingCapacity = ((remainingCapacity * 1.5) + 0.5).toInt().coerceAtLeast(remainingCapacity + 1)
                    }"""

kt = kt.replace(old_step3b, new_step3b)

old_qual = """                        val outputQuality = ((avgInputQuality * recipe.qualityMultiplier) * 10.0).toInt() / 10.0"""
new_qual = """                        val outputQuality = ((avgInputQuality * recipe.qualityMultiplier * qualityMult) * 10.0).toInt() / 10.0"""
kt = kt.replace(old_qual, new_qual)

# Step 4 Spoilage modifications
old_spoil = """                // 4. Inventory Aging & Spoilage Evaluation
                var spoiledCount = 0
                val finalInventory = mutableListOf<com.example.model.InventoryBatch>()"""

new_spoil = """                // 4. Inventory Aging & Spoilage Evaluation
                var spoiledCount = 0
                
                var fertilizerConversionCapacity = effectiveBuildings.filter { it.isConstructed && it.isOperational }.sumOf { b ->
                    var cap = 0
                    b.unlockedPerks.forEach { perkId ->
                        val perk = com.example.model.FacilityPerkCatalog.PERKS.find { it.id == perkId }
                        if (perk != null && perk.effectType == com.example.model.PerkEffect.SPOILAGE_CONVERSION) {
                            cap += (b.currentProcessingCapacity * perk.effectValue).toInt()
                        }
                    }
                    cap
                }
                
                val finalInventory = mutableListOf<com.example.model.InventoryBatch>()"""

kt = kt.replace(old_spoil, new_spoil)

old_eval = """                val evaluatedInventory = mutableListOf<com.example.model.InventoryBatch>()
                for (batch in finalInventory) {
                    if (batch.isSpoiled && batch.itemId != com.example.model.ProductCatalog.SPOILED_MILK.id) {
                        spoiledCount += batch.quantity
                        evaluatedInventory.add(
                            com.example.model.InventoryBatch(
                                itemId = com.example.model.ProductCatalog.SPOILED_MILK.id,
                                itemName = com.example.model.ProductCatalog.SPOILED_MILK.name,
                                quantity = batch.quantity,
                                quality = 0.2,
                                maxShelfLife = com.example.model.ProductCatalog.SPOILED_MILK.shelfLifeDays,
                                dayProduced = currentDay + 1,
                                daysUntilSpoiled = com.example.model.ProductCatalog.SPOILED_MILK.shelfLifeDays.toFloat()
                            )
                        )
                        notes.add("⚠️ Spoilage Alert: ${batch.quantity}x ${batch.itemName} turned sour!")
                    } else {
                        evaluatedInventory.add(batch)
                    }
                }"""

new_eval = """                val evaluatedInventory = mutableListOf<com.example.model.InventoryBatch>()
                for (batch in finalInventory) {
                    if (batch.isSpoiled && batch.itemId != com.example.model.ProductCatalog.SPOILED_MILK.id && batch.itemId != "fertilizer") {
                        spoiledCount += batch.quantity
                        
                        val convertedToFertilizer = batch.quantity.coerceAtMost(fertilizerConversionCapacity)
                        fertilizerConversionCapacity -= convertedToFertilizer
                        val turnedToSpoiledMilk = batch.quantity - convertedToFertilizer
                        
                        if (convertedToFertilizer > 0) {
                            evaluatedInventory.add(
                                com.example.model.InventoryBatch(
                                    itemId = "fertilizer",
                                    itemName = "Bio-Fertilizer",
                                    quantity = convertedToFertilizer,
                                    quality = 1.0,
                                    maxShelfLife = 365,
                                    dayProduced = currentDay + 1,
                                    daysUntilSpoiled = 365f
                                )
                            )
                            notes.add("♻️ Eco-Conversion: ${convertedToFertilizer}x spoiled ${batch.itemName} was processed into Bio-Fertilizer!")
                        }
                        
                        if (turnedToSpoiledMilk > 0) {
                            evaluatedInventory.add(
                                com.example.model.InventoryBatch(
                                    itemId = com.example.model.ProductCatalog.SPOILED_MILK.id,
                                    itemName = com.example.model.ProductCatalog.SPOILED_MILK.name,
                                    quantity = turnedToSpoiledMilk,
                                    quality = 0.2,
                                    maxShelfLife = com.example.model.ProductCatalog.SPOILED_MILK.shelfLifeDays,
                                    dayProduced = currentDay + 1,
                                    daysUntilSpoiled = com.example.model.ProductCatalog.SPOILED_MILK.shelfLifeDays.toFloat()
                                )
                            )
                            notes.add("⚠️ Spoilage Alert: ${turnedToSpoiledMilk}x ${batch.itemName} turned sour!")
                        }
                    } else {
                        evaluatedInventory.add(batch)
                    }
                }"""

kt = kt.replace(old_eval, new_eval)

with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(kt)
