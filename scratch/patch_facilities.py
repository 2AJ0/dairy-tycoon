import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/FacilitiesTab.kt')

old_imports = "import androidx.compose.ui.text.font.FontWeight"
new_imports = "import androidx.compose.ui.text.font.FontWeight\nimport com.example.ui.components.pulseWarning"
if "import com.example.ui.components.pulseWarning" not in kt:
    kt = kt.replace(old_imports, new_imports)

old_land = """                            onClick = onBuyLandExpansion,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .testTag("need_land_${building.id}"),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, BearishRed)"""

new_land = """                            onClick = onBuyLandExpansion,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .testTag("need_land_${building.id}")
                                .pulseWarning(),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, BearishRed)"""

kt = kt.replace(old_land, new_land)

with open('app/src/main/java/com/example/ui/screens/FacilitiesTab.kt', 'w') as f:
    f.write(kt)
