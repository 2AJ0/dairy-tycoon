import sys
import re

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/ResearchHubScreen.kt')

# Find the start of RdTechsList
start_idx = kt.find("@Composable\nprivate fun RdTechsList(")
if start_idx == -1:
    start_idx = kt.find("private fun RdTechsList(")

# Find the next @Composable which is TechNodeCard
end_idx = kt.find("@Composable\nfun TechNodeCard", start_idx)

new_rd_techs_list = """@Composable
private fun RdTechsList(
    gameState: GameState,
    onUnlockTech: (String) -> Unit,
    evaluateAction: (com.example.model.GameAction) -> com.example.model.GuidanceState,
    nodes: List<TechTreeNode>,
    modifier: Modifier = Modifier
) {
    val unlockedIds = gameState.unlockedTechIds.toSet()
    val isLabsBusy = remember(gameState.activeProjects, gameState.playerSkills.multiTaskingLevel, gameState.unlockedTechIds) {
        val currentPersonal = gameState.activeProjects.count { !it.isDedicated }
        val capacity = gameState.playerSkills.personalActionCapacity
        val currentDedicated = gameState.activeProjects.count { it.isDedicated && it.type == com.example.model.ProjectType.TECH_RESEARCH }
        val totalDedicated = gameState.ownedLabs
        currentPersonal >= capacity && currentDedicated >= totalDedicated
    }

    val density = androidx.compose.ui.platform.LocalDensity.current
    val nodeWidthDp = 180.dp
    val nodeHeightDp = 90.dp
    val tierPaddingDp = 64.dp
    val verticalPaddingDp = 32.dp

    val nodeWidthPx = with(density) { nodeWidthDp.toPx() }
    val nodeHeightPx = with(density) { nodeHeightDp.toPx() }
    val tierPaddingPx = with(density) { tierPaddingDp.toPx() }
    val verticalPaddingPx = with(density) { verticalPaddingDp.toPx() }

    val nodePositions = remember(nodes) {
        val tierMap = mutableMapOf<String, Int>()
        fun getTier(nodeId: String): Int {
            if (tierMap.containsKey(nodeId)) return tierMap[nodeId]!!
            val node = nodes.find { it.id == nodeId } ?: return 0
            if (node.parentId == null) {
                tierMap[nodeId] = 0
                return 0
            }
            val t = getTier(node.parentId) + 1
            tierMap[nodeId] = t
            return t
        }

        val nodesByTier = nodes.groupBy { getTier(it.id) }
        val positions = mutableMapOf<String, Offset>()

        for ((tier, tierNodes) in nodesByTier) {
            val sortedNodes = tierNodes.sortedBy { it.gridY }
            sortedNodes.forEachIndexed { index, node ->
                val x = tier * (nodeWidthPx + tierPaddingPx) + 50f
                val y = index * (nodeHeightPx + verticalPaddingPx) + 50f
                positions[node.id] = Offset(x, y)
            }
        }
        positions
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clipToBounds()
            .androidx.compose.foundation.horizontalScroll(androidx.compose.foundation.rememberScrollState())
            .androidx.compose.foundation.verticalScroll(androidx.compose.foundation.rememberScrollState())
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(32.dp)
    ) {
        // Container box that sizes to the max elements
        Box(
            modifier = Modifier.padding(bottom = 64.dp, end = 64.dp)
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                for (node in nodes) {
                    val isResearched = unlockedIds.contains(node.id) || node.rpCost == 0
                    if (node.parentId != null) {
                        val parentPos = nodePositions[node.parentId]
                        val nodePos = nodePositions[node.id]
                        if (parentPos != null && nodePos != null) {
                            val startX = parentPos.x + nodeWidthPx / 2
                            val startY = parentPos.y + nodeHeightPx / 2
                            val endX = nodePos.x + nodeWidthPx / 2
                            val endY = nodePos.y + nodeHeightPx / 2

                            val isBranchResearched = isResearched
                            drawLine(
                                color = if (isBranchResearched) Color(0xFF00C853) else Color.Gray.copy(alpha = 0.5f),
                                start = Offset(startX, startY),
                                end = Offset(endX, endY),
                                strokeWidth = if (isBranchResearched) 6f else 3f,
                                pathEffect = if (!isBranchResearched) PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f) else null
                            )
                        }
                    }
                }
            }

            nodes.forEach { node ->
                val isResearched = unlockedIds.contains(node.id) || node.rpCost == 0
                val isLocked = node.parentId != null && !unlockedIds.contains(node.parentId)
                val pos = nodePositions[node.id] ?: Offset.Zero

                val xDp = with(density) { pos.x.toDp() }
                val yDp = with(density) { pos.y.toDp() }

                Box(
                    modifier = Modifier.padding(start = xDp, top = yDp)
                ) {
                    TechNodeCard(
                        node = node,
                        isResearched = isResearched,
                        isLocked = isLocked,
                        canAfford = gameState.researchPoints >= node.rpCost,
                        isLabsBusy = isLabsBusy,
                        onResearchClick = { onUnlockTech(node.id) },
                        evaluateAction = evaluateAction,
                        modifier = Modifier.width(nodeWidthDp)
                    )
                }
            }
        }
    }
}

"""

if start_idx != -1 and end_idx != -1:
    kt = kt[:start_idx] + new_rd_techs_list + kt[end_idx:]
    with open('app/src/main/java/com/example/ui/screens/ResearchHubScreen.kt', 'w') as f:
        f.write(kt)
    print("Replaced RdTechsList successfully")
else:
    print("Failed to find boundaries")
