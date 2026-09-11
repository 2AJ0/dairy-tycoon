import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

buy_perk = """
    fun buyFacilityPerk(facilityId: String, perkId: String) {
        _gameState.update { state ->
            val facility = state.buildings.find { it.id == facilityId } ?: return@update state
            val perk = com.example.model.FacilityPerkCatalog.PERKS.find { it.id == perkId } ?: return@update state
            
            if (state.cash < perk.cost) {
                _snackBarMessage.value = "Insufficient funds for ${perk.title}."
                return@update state
            }
            if (facility.level < perk.requiredFacilityLevel) {
                _snackBarMessage.value = "Facility level too low."
                return@update state
            }
            if (perk.mutuallyExclusiveWith.any { it in facility.unlockedPerks }) {
                _snackBarMessage.value = "Mutually exclusive perk already unlocked."
                return@update state
            }
            if (perkId in facility.unlockedPerks) return@update state
            
            val updatedBuildings = state.buildings.map {
                if (it.id == facilityId) it.copy(unlockedPerks = it.unlockedPerks + perkId) else it
            }
            
            _snackBarMessage.value = "Unlocked ${perk.title} for ${facility.name}!"
            state.copy(
                cash = state.cash - perk.cost,
                buildings = updatedBuildings
            )
        }
    }
"""

if "fun buyFacilityPerk" not in kt:
    kt = kt.rstrip()[:-1] + buy_perk + "\n}\n"

with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(kt)
