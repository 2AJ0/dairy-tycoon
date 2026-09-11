import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

# 1. Add Log.d("TycoonDebug", "Starting End Day...")
old_endday_start = """    fun endDay() {
        if (_gameState.value.isGameOver) return"""

new_endday_start = """    fun endDay() {
        android.util.Log.d("TycoonDebug", "Starting End Day...")
        if (_gameState.value.isGameOver) return"""

kt = kt.replace(old_endday_start, new_endday_start)


# 2. Add Log.d("TycoonDebug", "Milk produced today: [amount]")
old_milk_produced = """                        if (yield > 0) {
                            rawProducedUnits += yield
                            processedItemIdsThisDay.add(ProductCatalog.RAW_MILK.id)"""

new_milk_produced = """                        if (yield > 0) {
                            rawProducedUnits += yield
                            processedItemIdsThisDay.add(ProductCatalog.RAW_MILK.id)
                            android.util.Log.d("TycoonDebug", "Milk produced today: $yield")"""

kt = kt.replace(old_milk_produced, new_milk_produced)


# 3. Add Log.d("TycoonDebug", "Milk spoiled today: [amount]")
# We need to find where spoiledCount is finalized.
old_spoiled_count = """                val evaluatedInventory = mutableListOf<InventoryBatch>()
                for (batch in finalInventory) {
                    if (batch.isSpoiled && batch.itemId != ProductCatalog.SPOILED_MILK.id) {"""

new_spoiled_count = """                val evaluatedInventory = mutableListOf<InventoryBatch>()
                for (batch in finalInventory) {
                    if (batch.isSpoiled && batch.itemId != ProductCatalog.SPOILED_MILK.id) {"""

# Actually let's just insert it after the loop where evaluatedInventory is populated.
old_after_spoilage = """                        evaluatedInventory.add(batch)
                    }
                }

                // 5. Update Bank & Daily Report"""

new_after_spoilage = """                        evaluatedInventory.add(batch)
                    }
                }
                
                android.util.Log.d("TycoonDebug", "Milk spoiled today: $spoiledCount")
                
                val finalMilkCount = evaluatedInventory.filter { it.itemId == com.example.model.ProductCatalog.RAW_MILK.id && !it.isSpoiled }.sumOf { it.quantity }
                android.util.Log.d("TycoonDebug", "Final Inventory count for Milk: $finalMilkCount")

                // 5. Update Bank & Daily Report"""

kt = kt.replace(old_after_spoilage, new_after_spoilage)


# 4. List Instance Fix (inventory = finalInventory.toList())
old_inventory_assign = """                    newCrisisFired = currentState.newCrisisFired ?: newCrisisFired,
                    inventory = evaluatedInventory,
                    lifetimeSpoilage = currentState.lifetimeSpoilage + spoiledCount,"""

new_inventory_assign = """                    newCrisisFired = currentState.newCrisisFired ?: newCrisisFired,
                    inventory = evaluatedInventory.toList(),
                    lifetimeSpoilage = currentState.lifetimeSpoilage + spoiledCount,"""

kt = kt.replace(old_inventory_assign, new_inventory_assign)


# Make sure evaluatedInventory is what's assigned
old_inventory_assign_2 = """                    newCrisisFired = currentState.newCrisisFired ?: newCrisisFired,
                    inventory = finalInventory,
                    lifetimeSpoilage = currentState.lifetimeSpoilage + spoiledCount,"""

new_inventory_assign_2 = """                    newCrisisFired = currentState.newCrisisFired ?: newCrisisFired,
                    inventory = evaluatedInventory.toList(),
                    lifetimeSpoilage = currentState.lifetimeSpoilage + spoiledCount,"""

kt = kt.replace(old_inventory_assign_2, new_inventory_assign_2)


with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(kt)
