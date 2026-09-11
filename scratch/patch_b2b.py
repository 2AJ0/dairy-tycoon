import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/B2BContractsScreen.kt')

old_imports = "import androidx.compose.ui.text.style.TextOverflow"
new_imports = "import androidx.compose.ui.text.style.TextOverflow\nimport com.example.ui.components.pulseWarning"
if "import com.example.ui.components.pulseWarning" not in kt:
    kt = kt.replace(old_imports, new_imports)

old_badge = """                                // Day Progress Badge
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = DairyEmeraldPrimary.copy(alpha = 0.15f),
                                    border = BorderStroke(1.dp, DairyEmeraldPrimary.copy(alpha = 0.4f))
                                ) {"""

new_badge = """                                // Day Progress Badge
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (contract.daysRemaining <= 1) BearishRed.copy(alpha = 0.15f) else DairyEmeraldPrimary.copy(alpha = 0.15f),
                                    border = BorderStroke(1.dp, if (contract.daysRemaining <= 1) BearishRed.copy(alpha = 0.4f) else DairyEmeraldPrimary.copy(alpha = 0.4f)),
                                    modifier = if (contract.daysRemaining <= 1) Modifier.pulseWarning() else Modifier
                                ) {"""

kt = kt.replace(old_badge, new_badge)

with open('app/src/main/java/com/example/ui/screens/B2BContractsScreen.kt', 'w') as f:
    f.write(kt)
