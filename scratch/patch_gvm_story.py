import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

eval_func = """
    private fun evaluateStoryEvents(state: com.example.model.GameState): com.example.model.GameState {
        var newState = state
        val newPending = state.pendingStoryEvents.toMutableList()
        var addedAny = false
        
        // Helper to check and add
        fun triggerEvent(eventId: String) {
            if (!newState.readStoryEvents.contains(eventId) && !newPending.any { it.id == eventId }) {
                com.example.model.StoryCatalog.ALL_EVENTS.find { it.id == eventId }?.let {
                    newPending.add(it)
                    addedAny = true
                }
            }
        }
        
        // 1. Farming Beat (Always triggers early, say day >= 1)
        if (state.day >= 1) triggerEvent("event_farming_1")
        
        // 2. Corporate Beat (Net Worth > 100k)
        if (state.netWorth >= 100000.0) triggerEvent("event_corporate_1")
        
        // 3. Experimental Beat (Advanced Lab built)
        val hasAdvLab = state.buildings.any { it.type == com.example.model.BuildingType.ADVANCED_LAB && it.isConstructed }
        if (hasAdvLab) triggerEvent("event_exp_1")
        
        if (addedAny) {
            newState = newState.copy(pendingStoryEvents = newPending)
        }
        return newState
    }
    
    fun markStoryEventAsRead(eventId: String) {
        _gameState.update { state ->
            state.copy(
                pendingStoryEvents = state.pendingStoryEvents.filter { it.id != eventId },
                readStoryEvents = state.readStoryEvents + eventId
            )
        }
    }
"""

if "fun markStoryEventAsRead" not in kt:
    kt = kt.rstrip()[:-1] + eval_func + "\n}\n"

# Add evaluation call in endDay
old_warfare = """                newState = processCorporateWarfare(newState, currentDay, newLogs)
                newState = processSubsidiaries(newState, newLogs)"""

new_warfare = """                newState = processCorporateWarfare(newState, currentDay, newLogs)
                newState = processSubsidiaries(newState, newLogs)
                newState = evaluateStoryEvents(newState)"""

kt = kt.replace(old_warfare, new_warfare)

with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(kt)
