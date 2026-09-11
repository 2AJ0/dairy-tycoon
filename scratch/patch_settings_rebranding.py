import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/SettingsScreen.kt')

# Add imports
imports = [
    "import androidx.compose.material3.OutlinedTextField",
    "import androidx.compose.material3.Button",
    "import androidx.compose.material3.CardDefaults",
    "import androidx.compose.material3.Card",
    "import androidx.compose.ui.Alignment"
]
for imp in imports:
    if imp not in kt:
        kt = kt.replace("import androidx.compose.material3.Text", "import androidx.compose.material3.Text\n" + imp)

# Add section
old_code = """        // Persistence & Storage Controls"""
new_code = """        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            "Empire Rebranding",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                var currentName by remember(gameState.saveName) { mutableStateOf(gameState.saveName) }
                
                OutlinedTextField(
                    value = currentName,
                    onValueChange = { 
                        if (it.length <= 20) {
                            currentName = it 
                        }
                    },
                    label = { Text("Empire Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Button(
                    onClick = { onRenameEmpire?.invoke(currentName) },
                    modifier = Modifier.align(Alignment.End),
                    enabled = currentName.isNotBlank() && currentName != gameState.saveName
                ) {
                    Text("Save Name")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        // Persistence & Storage Controls"""

kt = kt.replace(old_code, new_code)

with open('app/src/main/java/com/example/ui/screens/SettingsScreen.kt', 'w') as f:
    f.write(kt)
