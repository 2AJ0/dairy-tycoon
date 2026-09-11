import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/ResearchHubScreen.kt')

old_mod = """    Card(
        modifier = modifier
            .width(150.dp)
            .height(80.dp)
            .clickable(enabled = !isResearched && canAfford && !isLabsBusy, onClick = onResearchClick),"""

new_mod = """    Card(
        modifier = modifier
            .clickable(enabled = !isResearched && canAfford && !isLabsBusy, onClick = onResearchClick),"""

kt = kt.replace(old_mod, new_mod)

# Update RdTechsList modifier to pass both width and height
kt = kt.replace("modifier = Modifier.width(nodeWidthDp)", "modifier = Modifier.width(nodeWidthDp).height(nodeHeightDp)")

with open('app/src/main/java/com/example/ui/screens/ResearchHubScreen.kt', 'w') as f:
    f.write(kt)
