import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/components/TopStatsHeader.kt')

old_chip = """@Composable
fun StatusChip(
    icon: String,
    label: String,
    backgroundColor: Color,
    textColor: Color,
    tooltipText: String,
    modifier: Modifier = Modifier,
    testTag: String = "",
    onClick: (() -> Unit)? = null
) {"""

new_chip = """@Composable
fun StatusChip(
    icon: String,
    label: String = "",
    value: Double? = null,
    prefix: String = "",
    backgroundColor: Color,
    textColor: Color,
    tooltipText: String,
    modifier: Modifier = Modifier,
    testTag: String = "",
    onClick: (() -> Unit)? = null
) {"""

kt = kt.replace(old_chip, new_chip)

old_text = """                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    maxLines = 1
                )"""

new_text = """                if (value != null) {
                    AnimatedCounter(
                        targetValue = value,
                        prefix = prefix,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = textColor
                    )
                } else {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        maxLines = 1
                    )
                }"""

kt = kt.replace(old_text, new_text)

# Also replace the cash chip usage in TopStatsHeader to use `value`
old_cash_usage = """                StatusChip(
                    icon = "💵",
                    label = "$${String.format("%,.0f", gameState.cash)}",
                    backgroundColor = BullishGreen.copy(alpha = 0.18f),
                    textColor = BullishGreen,
                    tooltipText = "Liquid Cash: Used for purchasing resources, building facilities, and market trades.",
                    modifier = Modifier.weight(1f),
                    testTag = "cash_badge"
                )"""

new_cash_usage = """                StatusChip(
                    icon = "💵",
                    value = gameState.cash,
                    prefix = "$",
                    backgroundColor = BullishGreen.copy(alpha = 0.18f),
                    textColor = BullishGreen,
                    tooltipText = "Liquid Cash: Used for purchasing resources, building facilities, and market trades.",
                    modifier = Modifier.weight(1f),
                    testTag = "cash_badge"
                )"""

kt = kt.replace(old_cash_usage, new_cash_usage)

old_imports = "import androidx.compose.ui.text.font.FontWeight"
new_imports = "import androidx.compose.ui.text.font.FontWeight\nimport com.example.ui.components.AnimatedCounter"

if "import com.example.ui.components.AnimatedCounter" not in kt:
    kt = kt.replace(old_imports, new_imports)

with open('app/src/main/java/com/example/ui/components/TopStatsHeader.kt', 'w') as f:
    f.write(kt)
