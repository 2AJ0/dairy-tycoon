import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/Building.kt')

# Add ADVANCED_LAB to BuildingType
kt = kt.replace(
    "enum class BuildingType {\n    PASTURE,          // Produces raw milk\n    PASTEURIZER,",
    "enum class BuildingType {\n    PASTURE,          // Produces raw milk\n    PASTEURIZER,\n    ADVANCED_LAB,"
)

# Add to DefaultBuildings
new_lab = """            Building(
                id = "adv_lab_1",
                type = BuildingType.ADVANCED_LAB,
                name = "Advanced Research Lab",
                description = "Classified underground bunker generating experimental tech paradigms. Unlocks the Experimental Research Branch.",
                level = 1,
                isConstructed = false,
                baseCost = 150000.0,
                baseDailyMaintenance = 1500.0,
                dailyResearchPoints = 50,
                iconEmoji = "☢️",
                daysToComplete = 5,
                landRequired = 1,
                requiredTechId = "tech_singularity"
            )
        )"""

kt = kt.replace(
    """                requiredTechId = "tech_basic_cold_storage"
            )
        )""",
    """                requiredTechId = "tech_basic_cold_storage"
            ),
""" + new_lab
)

with open('app/src/main/java/com/example/model/Building.kt', 'w') as f:
    f.write(kt)
