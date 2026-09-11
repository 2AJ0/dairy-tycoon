import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/GameState.kt')

# Add the SpunOffSubsidiary data class at the top (after imports)
new_class = """
data class SpunOffSubsidiary(
    val id: String = java.util.UUID.randomUUID().toString(),
    val customName: String,
    val parentFacilityId: String,
    val dividendSlider: Float = 0.5f,
    val internalCapital: Double = 0.0,
    val qualityMultiplier: Float = 1.0f
)
"""
if "data class SpunOffSubsidiary" not in kt:
    kt = kt.replace("import com.example.model.GamePhase", "import com.example.model.GamePhase\n" + new_class)

old_gs = """    val lifetimeProduced: Map<String, Int> = emptyMap(),
    val lifetimeSpoilage: Int = 0
) {"""

new_gs = """    val lifetimeProduced: Map<String, Int> = emptyMap(),
    val lifetimeSpoilage: Int = 0,
    val spunOffSubsidiaries: List<SpunOffSubsidiary> = emptyList()
) {"""

kt = kt.replace(old_gs, new_gs)

with open('app/src/main/java/com/example/model/GameState.kt', 'w') as f:
    f.write(kt)
