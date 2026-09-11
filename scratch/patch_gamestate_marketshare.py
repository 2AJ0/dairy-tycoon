import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/GameState.kt')

old_gs = """    val pendingCyberAttacks: List<PendingCyberAttack> = emptyList(),
    val cyberAttackWarningEvent: PendingCyberAttack? = null
) {"""

new_gs = """    val pendingCyberAttacks: List<PendingCyberAttack> = emptyList(),
    val cyberAttackWarningEvent: PendingCyberAttack? = null,
    val playerMarketShare: Float = 25.0f
) {"""

kt = kt.replace(old_gs, new_gs)

with open('app/src/main/java/com/example/model/GameState.kt', 'w') as f:
    f.write(kt)
