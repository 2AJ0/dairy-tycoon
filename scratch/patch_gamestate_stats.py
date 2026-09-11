import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/GameState.kt')

old_gs = """    val playerMarketShare: Float = 25.0f
) {"""

new_gs = """    val playerMarketShare: Float = 25.0f,
    val peakNetWorth: Double = 0.0,
    val hostileTakeovers: Int = 0,
    val maxContractValue: Double = 0.0,
    val lifetimeRevenue: Double = 0.0,
    val attacksThwarted: Int = 0,
    val fraudLosses: Double = 0.0,
    val ddosDowntimeDays: Int = 0,
    val lifetimeProduced: Map<String, Int> = emptyMap(),
    val lifetimeSpoilage: Int = 0
) {"""

kt = kt.replace(old_gs, new_gs)

with open('app/src/main/java/com/example/model/GameState.kt', 'w') as f:
    f.write(kt)
