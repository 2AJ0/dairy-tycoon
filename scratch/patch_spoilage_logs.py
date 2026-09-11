import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

old_spoilage = """                    } else {
                        evaluatedInventory.add(batch)
                    }
                }
                finalInventory.clear()
                finalInventory.addAll(evaluatedInventory)"""

new_spoilage = """                    } else {
                        evaluatedInventory.add(batch)
                    }
                }
                
                android.util.Log.d("TycoonDebug", "Milk spoiled today: $spoiledCount")
                val finalMilkCount = evaluatedInventory.filter { it.itemId == com.example.model.ProductCatalog.RAW_MILK.id && !it.isSpoiled }.sumOf { it.quantity }
                android.util.Log.d("TycoonDebug", "Final Inventory count for Milk: $finalMilkCount")
                
                finalInventory.clear()
                finalInventory.addAll(evaluatedInventory)"""

kt = kt.replace(old_spoilage, new_spoilage)

# Also fix the list assignment issue since I didn't replace it correctly (it used finalInventory before)
old_inventory_assign = """                    inventory = finalInventory,
                    lifetimeSpoilage = currentState.lifetimeSpoilage + spoiledCount,"""

new_inventory_assign = """                    inventory = finalInventory.toList(),
                    lifetimeSpoilage = currentState.lifetimeSpoilage + spoiledCount,"""

kt = kt.replace(old_inventory_assign, new_inventory_assign)

with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(kt)
