import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

# Update line 379
kt = kt.replace(
    "val rawMaintenance = currentState.buildings.filter { it.isConstructed && it.isOperational }",
    "val rawMaintenance = currentState.buildings.filter { it.isConstructed && it.isOperational && !it.isSpunOff }"
)

# Update line 382
kt = kt.replace(
    "var feedCost = currentState.buildings.filter { it.isConstructed && it.type == BuildingType.PASTURE && it.isOperational }",
    "var feedCost = currentState.buildings.filter { it.isConstructed && it.type == BuildingType.PASTURE && it.isOperational && !it.isSpunOff }"
)

# Update line 502
kt = kt.replace(
    "effectiveBuildings.filter { it.isConstructed && it.isOperational && it.sabotagedDaysRemaining <= 0 }.forEach { building ->",
    "effectiveBuildings.filter { it.isConstructed && it.isOperational && it.sabotagedDaysRemaining <= 0 && !it.isSpunOff }.forEach { building ->"
)

# Update line 556
kt = kt.replace(
    "effectiveBuildings.filter { it.isConstructed && it.isOperational && it.sabotagedDaysRemaining <= 0 && it.activeRecipe != null }.forEach { building ->",
    "effectiveBuildings.filter { it.isConstructed && it.isOperational && it.sabotagedDaysRemaining <= 0 && it.activeRecipe != null && !it.isSpunOff }.forEach { building ->"
)

with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(kt)
