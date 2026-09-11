import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/CorporateWarfareScreen.kt')

# Add imports for Icons if missing
if "import androidx.compose.material.icons.filled.Sort" not in kt:
    kt = kt.replace("import androidx.compose.material.icons.filled.Security", "import androidx.compose.material.icons.filled.Security\nimport androidx.compose.material.icons.filled.Sort")

# Add sort state
old_sig = """    var attackCostAp by remember { mutableIntStateOf(1) }"""
new_sig = """    var attackCostAp by remember { mutableIntStateOf(1) }
    var sortAscending by remember { mutableStateOf(false) }"""

kt = kt.replace(old_sig, new_sig)

# Add sort icon and modify LazyColumn
old_list_header = """            Text(
                "Active Rivals",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val sortedRivals = gameState.rivalCompanies.sortedByDescending { it.tier.ordinal }"""

new_list_header = """            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Active Rivals",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { sortAscending = !sortAscending }) {
                    Icon(Icons.Default.Sort, contentDescription = "Sort by Market Power")
                }
            }
            
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val sortedRivals = if (sortAscending) gameState.rivalCompanies.sortedBy { it.marketPower } else gameState.rivalCompanies.sortedByDescending { it.marketPower }"""

kt = kt.replace(old_list_header, new_list_header)

with open('app/src/main/java/com/example/ui/screens/CorporateWarfareScreen.kt', 'w') as f:
    f.write(kt)

