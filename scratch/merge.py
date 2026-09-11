import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

head_kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

# Our new 3c logic:
new_3c = """
                    if (contract.contractType == ContractType.BULK_DEADLINE) {
                        val remainingToFulfill = contract.targetTotalQuantity - contract.fulfilledQuantity
                        val eligibleBatches = workingInventory.filter { it.itemId == targetProdId && !it.isSpoiled && it.quantity > 0 }
                        val totalAvailable = eligibleBatches.sumOf { it.quantity }
                        
                        val takeAmount = minOf(remainingToFulfill, totalAvailable)
                        
                        if (takeAmount > 0) {
                            var toDeduct = takeAmount
                            val orderedBatches = if (currentState.inventoryMethod == InventoryMethod.LIFO) eligibleBatches.reversed() else eligibleBatches
                            for (batch in orderedBatches) {
                                if (toDeduct <= 0) break
                                val bIndex = workingInventory.indexOf(batch)
                                if (bIndex == -1) continue
                                val take = minOf(toDeduct, batch.quantity)
                                toDeduct -= take
                                if (batch.quantity <= take) {
                                    workingInventory.removeAt(bIndex)
                                } else {
                                    workingInventory[bIndex] = batch.copy(quantity = batch.quantity - take)
                                }
                            }
                            
                            val newFulfilled = contract.fulfilledQuantity + takeAmount
                            if (newFulfilled >= contract.targetTotalQuantity) {
                                currentCash += contract.payoutAmount
                                contractsRevenueToday += contract.payoutAmount
                                completedContractsDelta++
                                notes.add("🎉 Bulk Complete: Delivered ${contract.targetTotalQuantity}x ${targetProduct.name} to ${rival.name} (+$${String.format("%.2f", contract.payoutAmount)}).")
                            } else {
                                updatedActiveContracts.add(contract.copy(fulfilledQuantity = newFulfilled, daysRemaining = contract.daysRemaining - 1))
                            }
                        } else {
                            val nextDays = contract.daysRemaining - 1
                            if (nextDays <= 0) {
                                currentCash -= contract.penaltyAmount
                                completedContractsDelta++
                                notes.add("❌ Bulk Failed: Failed to deliver ${contract.targetTotalQuantity}x ${targetProduct.name} to ${rival.name} in time! Fined -$${String.format("%.2f", contract.penaltyAmount)}.")
                            } else {
                                updatedActiveContracts.add(contract.copy(daysRemaining = nextDays))
                            }
                        }
                    } else {
                        // DAILY QUOTA
                        val dailyQuota = contract.dailyRequiredQuantity
                        val eligibleBatches = workingInventory.filter { it.itemId == targetProdId && !it.isSpoiled && it.quantity > 0 }
                        val totalAvailable = eligibleBatches.sumOf { it.quantity }
                        
                        if (totalAvailable >= dailyQuota) {
                            var remainingToDeduct = dailyQuota
                            val orderedBatches = if (currentState.inventoryMethod == InventoryMethod.LIFO) eligibleBatches.reversed() else eligibleBatches
                            for (batch in orderedBatches) {
                                if (remainingToDeduct <= 0) break
                                val bIndex = workingInventory.indexOf(batch)
                                if (bIndex == -1) continue
                                val take = minOf(remainingToDeduct, batch.quantity)
                                remainingToDeduct -= take
                                if (batch.quantity <= take) {
                                    workingInventory.removeAt(bIndex)
                                } else {
                                    workingInventory[bIndex] = batch.copy(quantity = batch.quantity - take)
                                }
                            }
                            
                            val newSuccessDays = contract.daysSuccessfullyFulfilled + 1
                            val nextDays = contract.daysRemaining - 1
                            
                            if (nextDays <= 0) {
                                currentCash += contract.payoutAmount
                                contractsRevenueToday += contract.payoutAmount
                                completedContractsDelta++
                                notes.add("🎉 Daily Complete: Fulfilled all terms with ${rival.name} (+$${String.format("%.2f", contract.payoutAmount)}).")
                            } else {
                                updatedActiveContracts.add(contract.copy(daysRemaining = nextDays, daysSuccessfullyFulfilled = newSuccessDays))
                                notes.add("🤝 Daily Delivery: Supplied ${dailyQuota}x ${targetProduct.name} to ${rival.name}.")
                            }
                        } else {
                            currentCash -= contract.penaltyAmount
                            val nextDays = contract.daysRemaining - 1
                            notes.add("⚠️ Daily Default: Shortfall on ${dailyQuota}x ${targetProduct.name} for ${rival.name}! Fined -$${String.format("%.2f", contract.penaltyAmount)}.")
                            
                            if (nextDays <= 0) {
                                completedContractsDelta++
                                notes.add("❌ Contract Expired: Terms with ${rival.name} failed overall.")
                            } else {
                                updatedActiveContracts.add(contract.copy(daysRemaining = nextDays))
                            }
                        }
                    }
                }
"""

reconstructed = read_file('scratch/reconstructed.kt')
idx = reconstructed.find('// 3d. Auto-Sell Execution')

if idx == -1:
    idx = reconstructed.find('var autoSellRevenueToday = 0.0')

tail_rest = reconstructed[idx:]

full_kt = head_kt.rstrip() + "\n" + new_3c + "\n                " + tail_rest.strip()

with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(full_kt)

print("Merged!")
