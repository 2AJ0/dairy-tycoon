import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/B2BContract.kt')

# Add threatLevel to RivalCompany
old_rival = """    val offenseRating: Int = 10,
    val targetSector: ProductCategory? = null,
    val hostilityToPlayer: Int = 0,
    val b2bLockoutDaysRemaining: Int = 0
) {"""

new_rival = """    val offenseRating: Int = 10,
    val targetSector: ProductCategory? = null,
    val hostilityToPlayer: Int = 0,
    val b2bLockoutDaysRemaining: Int = 0,
    val threatLevel: Int = 10
) {"""

kt = kt.replace(old_rival, new_rival)

# Add AttackType and PendingCyberAttack
old_enum = """enum class ContractType {"""

new_enum = """enum class AttackType {
    DDOS_FACILITY,
    LOGISTICS_HIJACK,
    FINANCIAL_PHISHING
}

data class PendingCyberAttack(
    val id: String = java.util.UUID.randomUUID().toString(),
    val attackerId: String, // Rival ID or "PLAYER"
    val targetId: String, // Rival ID or "PLAYER"
    val attackType: AttackType,
    val executionDay: Int
)

enum class ContractType {"""

kt = kt.replace(old_enum, new_enum)

with open('app/src/main/java/com/example/model/B2BContract.kt', 'w') as f:
    f.write(kt)
