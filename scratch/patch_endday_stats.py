import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

# Spoilage tracking
old_spoil = """                    val spoiled = inventory.filter { it.daysUntilSpoiled <= 0f }.sumOf { it.quantity }
                    val goodInv = inventory.filter { it.daysUntilSpoiled > 0f }"""
new_spoil = """                    val spoiled = inventory.filter { it.daysUntilSpoiled <= 0f }.sumOf { it.quantity }
                    val goodInv = inventory.filter { it.daysUntilSpoiled > 0f }
                    var currentSpoilage = currentState.lifetimeSpoilage + spoiled"""
kt = kt.replace(old_spoil, new_spoil)

old_inv = """                    inventory = finalInventory,"""
new_inv = """                    inventory = finalInventory,
                    lifetimeSpoilage = currentSpoilage,"""
kt = kt.replace(old_inv, new_inv)

# We need to track peakNetWorth and lifetimeRevenue
# At the end of endDay, where state is consolidated:
old_consol = """                val estimatedTotalNetWorth = currentCash + finalInventory.sumOf { batch ->
                    val market = newMarketPrices[batch.itemId]
                    batch.quantity * (market?.basePrice ?: 0.0)
                } + effectiveBuildings.sumOf { it.level * 2000.0 }
                
                val isBankrupt = bankDebt > estimatedTotalNetWorth && daysInDebt > 15
                
                val newPhase = when {
                    estimatedTotalNetWorth < 50000 -> com.example.model.NetWorthPhase.STARTUP
                    estimatedTotalNetWorth < 250000 -> com.example.model.NetWorthPhase.TENSION
                    else -> com.example.model.NetWorthPhase.CORPORATE
                }"""

new_consol = """                val estimatedTotalNetWorth = currentCash + finalInventory.sumOf { batch ->
                    val market = newMarketPrices[batch.itemId]
                    batch.quantity * (market?.basePrice ?: 0.0)
                } + effectiveBuildings.sumOf { it.level * 2000.0 }
                
                val isBankrupt = bankDebt > estimatedTotalNetWorth && daysInDebt > 15
                
                val newPhase = when {
                    estimatedTotalNetWorth < 50000 -> com.example.model.NetWorthPhase.STARTUP
                    estimatedTotalNetWorth < 250000 -> com.example.model.NetWorthPhase.TENSION
                    else -> com.example.model.NetWorthPhase.CORPORATE
                }
                
                val newPeak = maxOf(currentState.peakNetWorth, estimatedTotalNetWorth)
                val newRevenue = currentState.lifetimeRevenue + salesRevenue + contractsRevenueToday"""

kt = kt.replace(old_consol, new_consol)

old_state = """                    netWorthPhase = newPhase,"""
new_state = """                    netWorthPhase = newPhase,
                    peakNetWorth = newPeak,
                    lifetimeRevenue = newRevenue,"""
kt = kt.replace(old_state, new_state)

with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(kt)
