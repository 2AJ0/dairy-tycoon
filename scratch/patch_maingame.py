import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/MainGameScreen.kt')

old_imports = "import androidx.compose.ui.text.font.FontWeight"
new_imports = "import androidx.compose.ui.text.font.FontWeight\nimport com.example.ui.components.bounceClick"
if "import com.example.ui.components.bounceClick" not in kt:
    kt = kt.replace(old_imports, new_imports)

old_pay = """                            androidx.compose.material3.Button(
                                onClick = { viewModel.resolveMissedDelivery(true) },
                                enabled = canAfford,
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = if (canAfford) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                            ) {"""

new_pay = """                            androidx.compose.material3.Button(
                                onClick = { viewModel.resolveMissedDelivery(true) },
                                enabled = canAfford,
                                modifier = Modifier.bounceClick(),
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = if (canAfford) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                            ) {"""

kt = kt.replace(old_pay, new_pay)

old_strike = """                            androidx.compose.material3.OutlinedButton(
                                onClick = { viewModel.resolveMissedDelivery(false) }
                            ) {"""

new_strike = """                            androidx.compose.material3.OutlinedButton(
                                onClick = { viewModel.resolveMissedDelivery(false) },
                                modifier = Modifier.bounceClick()
                            ) {"""

kt = kt.replace(old_strike, new_strike)

with open('app/src/main/java/com/example/ui/screens/MainGameScreen.kt', 'w') as f:
    f.write(kt)
