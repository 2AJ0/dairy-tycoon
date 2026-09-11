import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

# 1. Update construction finish in endDay
old_const = """                                effectiveBuildings = effectiveBuildings.map { b ->
                                    if (b.id == project.targetId) {
                                        b.copy(isConstructed = true, level = 1, isOperational = true)
                                    } else b
                                }
                                newlyBuiltFacilitiesCount += 1
                                notes.add("🔨 Construction Finished: ${project.targetName} is now fully operational!")"""

new_const = """                                var builtBuildingType: com.example.model.BuildingType? = null
                                effectiveBuildings = effectiveBuildings.map { b ->
                                    if (b.id == project.targetId) {
                                        builtBuildingType = b.type
                                        b.copy(isConstructed = true, level = 1, isOperational = true)
                                    } else b
                                }
                                newlyBuiltFacilitiesCount += 1
                                notes.add("🔨 Construction Finished: ${project.targetName} is now fully operational!")
                                
                                if (builtBuildingType == com.example.model.BuildingType.ADVANCED_LAB && !currentState.hasSeenAweTutorial) {
                                    _gameState.update { it.copy(showAweDialogue = true, hasSeenAweTutorial = true) }
                                }"""
kt = kt.replace(old_const, new_const)

# 2. Add tutorial navigation functions
awe_funcs = """
    fun advanceAweTutorial() {
        _gameState.update { it.copy(showAweDialogue = false, showAweHighlight = true) }
    }
    
    fun dismissAweHighlight() {
        _gameState.update { it.copy(showAweHighlight = false) }
    }
"""

if "fun advanceAweTutorial" not in kt:
    kt = kt.rstrip()[:-1] + awe_funcs + "\n}\n"

with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(kt)
