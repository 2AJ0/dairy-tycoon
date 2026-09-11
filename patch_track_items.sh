sed -i '' 's/var processedUnitsProduced = 0/var processedUnitsProduced = 0\n                val processedItemIdsThisDay = mutableSetOf<String>()/g' app/src/main/java/com/example/viewmodel/GameViewModel.kt

sed -i '' 's/rawProducedUnits += yield/rawProducedUnits += yield\n                            processedItemIdsThisDay.add(ProductCatalog.RAW_MILK.id)/g' app/src/main/java/com/example/viewmodel/GameViewModel.kt

sed -i '' 's/\/\/ Push new output batch with Age: 0/\/\/ Push new output batch with Age: 0\n                        processedItemIdsThisDay.add(outputProduct.id)/g' app/src/main/java/com/example/viewmodel/GameViewModel.kt

sed -i '' 's/val byProductQty = batchesToProcess \* qtyPerBatch/val byProductQty = batchesToProcess \* qtyPerBatch\n                            if (byProductQty > 0) processedItemIdsThisDay.add(byProdId)/g' app/src/main/java/com/example/viewmodel/GameViewModel.kt
