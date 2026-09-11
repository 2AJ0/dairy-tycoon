import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/MainGameScreen.kt')

old_call = """                                onBuyLandExpansion = { viewModel.buyLandExpansion() },
                                onUpdateAllocation = { id, pct -> viewModel.updateBuildingAllocation(id, pct) },
                                onSpinOff = { id, customName -> viewModel.spinOffFacility(id, customName) }
                            )"""

new_call = """                                onBuyLandExpansion = { viewModel.buyLandExpansion() },
                                onUpdateAllocation = { id, pct -> viewModel.updateBuildingAllocation(id, pct) },
                                onSpinOff = { id, customName -> viewModel.spinOffFacility(id, customName) },
                                onBuyFacilityPerk = { facilityId, perkId -> viewModel.buyFacilityPerk(facilityId, perkId) }
                            )"""

kt = kt.replace(old_call, new_call)

with open('app/src/main/java/com/example/ui/screens/MainGameScreen.kt', 'w') as f:
    f.write(kt)
