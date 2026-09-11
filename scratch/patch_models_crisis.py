import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

# 1. Update B2BContract.kt (RivalCompany, AttackType, RivalTier)
kt1 = read_file('app/src/main/java/com/example/model/B2BContract.kt')

new_enum_tier = """enum class RivalTier {
    LOCAL, SPECIALIZED, GLOBAL
}

enum class AttackType {"""
kt1 = kt1.replace("enum class AttackType {", new_enum_tier)

kt1 = kt1.replace(
    "FINANCIAL_PHISHING\n}",
    "FINANCIAL_PHISHING,\n    HOSTILE_BUYOUT\n}"
)

old_rival_sig = """    val threatLevel: Int = 10,
    val marketShare: Float = 15.0f
) {"""
new_rival_sig = """    val threatLevel: Int = 10,
    val marketShare: Float = 15.0f,
    val tier: RivalTier = RivalTier.LOCAL
) {"""
kt1 = kt1.replace(old_rival_sig, new_rival_sig)

# Update RivalCatalog
kt1 = kt1.replace("targetSector = ProductCategory.PROCESSED,", "targetSector = ProductCategory.PROCESSED,\n        tier = RivalTier.GLOBAL,")
kt1 = kt1.replace("targetSector = ProductCategory.RAW,", "targetSector = ProductCategory.RAW,\n        tier = RivalTier.GLOBAL,")
kt1 = kt1.replace("targetSector = ProductCategory.ARTISANAL,", "targetSector = ProductCategory.ARTISANAL,\n        tier = RivalTier.SPECIALIZED,")
kt1 = kt1.replace("netWorth = 250000.0\n    )", "netWorth = 250000.0,\n        tier = RivalTier.LOCAL\n    )")

with open('app/src/main/java/com/example/model/B2BContract.kt', 'w') as f:
    f.write(kt1)

# 2. Update GameState.kt (CrisisEvent, activeCrises, newCrisisFired)
kt2 = read_file('app/src/main/java/com/example/model/GameState.kt')

new_crisis = """
enum class CrisisModifierType {
    MARKET_CRASH, // All sell prices -30%
    SUPPLY_SHORTAGE // Raw material costs +50%
}

data class CrisisEvent(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val durationDays: Int,
    val modifierType: CrisisModifierType
)
"""

if "enum class CrisisModifierType" not in kt2:
    kt2 = kt2.replace("import com.example.model.GamePhase", "import com.example.model.GamePhase\n" + new_crisis)

old_gs = """    val spunOffSubsidiaries: List<SpunOffSubsidiary> = emptyList()
) {"""
new_gs = """    val spunOffSubsidiaries: List<SpunOffSubsidiary> = emptyList(),
    val activeCrises: List<CrisisEvent> = emptyList(),
    val newCrisisFired: CrisisEvent? = null
) {"""
kt2 = kt2.replace(old_gs, new_gs)

with open('app/src/main/java/com/example/model/GameState.kt', 'w') as f:
    f.write(kt2)

