import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/GameState.kt')

# Fix SpunOffSubsidiary
old_spun_off = """data class SpunOffSubsidiary(
    val id: String = java.util.UUID.randomUUID().toString(),
    val customName: String,
    val parentFacilityId: String,
    val dividendSlider: Float = 0.5f,
    val internalCapital: Double = 0.0,
    val qualityMultiplier: Float = 1.0f
)"""

new_spun_off = """data class SpunOffSubsidiary(
    val id: String = java.util.UUID.randomUUID().toString(),
    val customName: String = "",
    val parentFacilityId: String = "",
    val dividendSlider: Float = 0.5f,
    val internalCapital: Double = 0.0,
    val qualityMultiplier: Float = 1.0f
)"""

kt = kt.replace(old_spun_off, new_spun_off)

# Fix CrisisEvent
old_crisis = """data class CrisisEvent(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val durationDays: Int,
    val modifierType: CrisisModifierType
)"""

new_crisis = """data class CrisisEvent(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String = "",
    val description: String = "",
    val durationDays: Int = 0,
    val modifierType: CrisisModifierType = CrisisModifierType.MARKET_CRASH
)"""

kt = kt.replace(old_crisis, new_crisis)

with open('app/src/main/java/com/example/model/GameState.kt', 'w') as f:
    f.write(kt)
