import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/GameState.kt')

old_game_state_vars = """    val lifetimeSpoilage: Int = 0,
    
    val gamePhase: GamePhase = GamePhase.FARMING,
    val netWorthPhase: NetWorthPhase = NetWorthPhase.SEED,
    
    val isStandardTreeComplete: Boolean = false,
    
    // --- LATE GAME / CORPORATE ---"""

new_game_state_vars = """    val lifetimeSpoilage: Int = 0,
    
    val gamePhase: GamePhase = GamePhase.FARMING,
    val netWorthPhase: NetWorthPhase = NetWorthPhase.SEED,
    
    val isStandardTreeComplete: Boolean = false,
    val activeSubsidiaries: List<SpunOffSubsidiary> = emptyList(),
    val victoryStats: LifetimeStats? = null,
    val reputationScore: Int = 15,
    
    // --- LATE GAME / CORPORATE ---"""

kt = kt.replace(old_game_state_vars, new_game_state_vars)

with open('app/src/main/java/com/example/model/GameState.kt', 'w') as f:
    f.write(kt)
