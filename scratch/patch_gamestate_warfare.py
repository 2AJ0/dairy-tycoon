import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/GameState.kt')

old_gs = """    val researchNodeStatuses: Map<String, NodeStatus> = ResearchCatalog.ALL_NODES.associate { it.id to NodeStatus.LOCKED },
    val missedDeliveryEvent: String? = null
) {"""

new_gs = """    val researchNodeStatuses: Map<String, NodeStatus> = ResearchCatalog.ALL_NODES.associate { it.id to NodeStatus.LOCKED },
    val missedDeliveryEvent: String? = null,
    val offenseRating: Int = 10,
    val defenseRating: Int = 10,
    val pendingCyberAttacks: List<PendingCyberAttack> = emptyList(),
    val cyberAttackWarningEvent: PendingCyberAttack? = null
) {"""

kt = kt.replace(old_gs, new_gs)

with open('app/src/main/java/com/example/model/GameState.kt', 'w') as f:
    f.write(kt)
