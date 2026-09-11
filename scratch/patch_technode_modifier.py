import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/ResearchHubScreen.kt')

old_sig = """fun TechNodeCard(
    node: TechTreeNode,
    isResearched: Boolean,
    isLocked: Boolean,
    canAfford: Boolean,
    isLabsBusy: Boolean,
    onResearchClick: () -> Unit,
    evaluateAction: (com.example.model.GameAction) -> com.example.model.GuidanceState
) {
    Card(
        modifier = Modifier"""

new_sig = """fun TechNodeCard(
    node: TechTreeNode,
    isResearched: Boolean,
    isLocked: Boolean,
    canAfford: Boolean,
    isLabsBusy: Boolean,
    onResearchClick: () -> Unit,
    evaluateAction: (com.example.model.GameAction) -> com.example.model.GuidanceState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier"""

kt = kt.replace(old_sig, new_sig)

with open('app/src/main/java/com/example/ui/screens/ResearchHubScreen.kt', 'w') as f:
    f.write(kt)
