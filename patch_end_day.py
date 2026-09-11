import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

# Add missedEventId
old_init = """                // Step 3c: B2B Contract Auto-Fulfillment (FIFO Quota Deduction before Spoilage & Aging)
                val updatedActiveContracts = mutableListOf<ContractOffer>()
                var completedContractsDelta = 0"""

new_init = """                // Step 3c: B2B Contract Auto-Fulfillment (FIFO Quota Deduction before Spoilage & Aging)
                val updatedActiveContracts = mutableListOf<ContractOffer>()
                var completedContractsDelta = 0
                var missedEventId: String? = currentState.missedDeliveryEvent"""

kt = kt.replace(old_init, new_init)

old_daily_fail = """                        } else {
                            currentCash -= contract.penaltyAmount
                            val nextDays = contract.daysRemaining - 1
                            notes.add("⚠️ Daily Default: Shortfall on ${dailyQuota}x ${targetProduct.name} for ${rival.name}! Fined -$${String.format("%.2f", contract.penaltyAmount)}.")
                            
                            if (nextDays <= 0) {
                                completedContractsDelta++
                                notes.add("❌ Contract Expired: Terms with ${rival.name} failed overall.")
                            } else {
                                updatedActiveContracts.add(contract.copy(daysRemaining = nextDays))
                            }
                        }"""

new_daily_fail = """                        } else {
                            if (missedEventId == null) {
                                missedEventId = contract.id
                            }
                            val nextDays = contract.daysRemaining - 1
                            val newFailedDays = contract.failedDays + 1
                            notes.add("⚠️ Contract Missed: Shortfall on ${dailyQuota}x ${targetProduct.name} for ${rival.name}! Pending executive decision tomorrow.")
                            updatedActiveContracts.add(contract.copy(daysRemaining = nextDays, failedDays = newFailedDays))
                        }"""

kt = kt.replace(old_daily_fail, new_daily_fail)

# Also need to add missedDeliveryEvent = missedEventId to the final copy block.
# Let's find the final copy block.

with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(kt)

