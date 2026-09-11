import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

old_fail = """                        } else {
                            if (missedEventId == null) {
                                missedEventId = contract.id
                            }
                            val nextDays = contract.daysRemaining - 1
                            val newFailedDays = contract.failedDays + 1
                            notes.add("⚠️ Contract Missed: Shortfall on ${dailyQuota}x ${targetProduct.name} for ${rival.name}! Pending executive decision tomorrow.")
                            updatedActiveContracts.add(contract.copy(daysRemaining = nextDays, failedDays = newFailedDays))
                        }"""

new_fail = """                        } else {
                            if (missedEventId == null) {
                                missedEventId = contract.id
                                val nextDays = contract.daysRemaining - 1
                                val newFailedDays = contract.failedDays + 1
                                notes.add("⚠️ Contract Missed: Shortfall on ${dailyQuota}x ${targetProduct.name} for ${rival.name}! Pending executive decision tomorrow.")
                                updatedActiveContracts.add(contract.copy(daysRemaining = nextDays, failedDays = newFailedDays))
                            } else {
                                currentCash -= contract.penaltyAmount
                                val nextDays = contract.daysRemaining - 1
                                notes.add("⚠️ Daily Default: Shortfall on ${dailyQuota}x ${targetProduct.name} for ${rival.name}! Fined -$${String.format("%.2f", contract.penaltyAmount)}.")
                                if (nextDays <= 0) {
                                    completedContractsDelta++
                                    notes.add("❌ Contract Expired: Terms with ${rival.name} failed overall.")
                                } else {
                                    updatedActiveContracts.add(contract.copy(daysRemaining = nextDays, failedDays = contract.failedDays + 1))
                                }
                            }
                        }"""

kt = kt.replace(old_fail, new_fail)

with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(kt)
