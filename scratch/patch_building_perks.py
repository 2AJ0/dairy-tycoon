import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/Building.kt')

# Add the enums and models
new_enums = """
enum class PerkEffect {
    THROUGHPUT_MULTIPLIER,
    QUALITY_MULTIPLIER,
    SPOILAGE_CONVERSION
}

data class FacilityPerk(
    val id: String,
    val title: String,
    val cost: Double,
    val requiredFacilityLevel: Int,
    val mutuallyExclusiveWith: List<String>,
    val effectType: PerkEffect,
    val effectValue: Float,
    val branch: String
)

object FacilityPerkCatalog {
    val PERKS = listOf(
        FacilityPerk("perk_vol_1", "Basic Automation", 5000.0, 1, listOf("perk_qual_1", "perk_eco_1"), PerkEffect.THROUGHPUT_MULTIPLIER, 1.25f, "VOLUME"),
        FacilityPerk("perk_vol_2", "Robotic Assembly", 15000.0, 2, emptyList(), PerkEffect.THROUGHPUT_MULTIPLIER, 1.50f, "VOLUME"),
        FacilityPerk("perk_qual_1", "Artisanal Care", 5000.0, 1, listOf("perk_vol_1", "perk_eco_1"), PerkEffect.QUALITY_MULTIPLIER, 1.25f, "QUALITY"),
        FacilityPerk("perk_qual_2", "Mastercrafted", 15000.0, 2, emptyList(), PerkEffect.QUALITY_MULTIPLIER, 1.50f, "QUALITY"),
        FacilityPerk("perk_eco_1", "Waste Recycler", 5000.0, 1, listOf("perk_vol_1", "perk_qual_1"), PerkEffect.SPOILAGE_CONVERSION, 0.50f, "ECO"),
        FacilityPerk("perk_eco_2", "Zero-Waste Loop", 15000.0, 2, emptyList(), PerkEffect.SPOILAGE_CONVERSION, 1.0f, "ECO")
    )
    
    fun getById(id: String) = PERKS.first { it.id == id }
}
"""

if "enum class PerkEffect" not in kt:
    kt = kt.replace("enum class BuildingType", new_enums + "\nenum class BuildingType")

old_b = """    val allocationPercentage: Int = 100,
    val isSpunOff: Boolean = false
) {"""

new_b = """    val allocationPercentage: Int = 100,
    val isSpunOff: Boolean = false,
    val unlockedPerks: List<String> = emptyList()
) {"""

kt = kt.replace(old_b, new_b)

with open('app/src/main/java/com/example/model/Building.kt', 'w') as f:
    f.write(kt)
