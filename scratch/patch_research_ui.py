import sys
import re

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/ResearchHubScreen.kt')

# 1. Add clipToBounds to RdTechsList
kt = kt.replace("import androidx.compose.ui.draw.clip", "import androidx.compose.ui.draw.clip\nimport androidx.compose.ui.draw.clipToBounds")

old_box = """    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.5f, 2.0f)
                    offsetX += pan.x
                    offsetY += pan.y
                }
            }
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {"""

new_box = """    Box(
        modifier = modifier
            .fillMaxSize()
            .clipToBounds()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.5f, 2.0f)
                    offsetX += pan.x
                    offsetY += pan.y
                }
            }
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {"""

kt = kt.replace(old_box, new_box)

# 2. Render ALL nodes, regardless of isVisible
old_is_visible_lines = """            for (node in allNodes) {
                val isResearched = unlockedIds.contains(node.id) || node.rpCost == 0
                val isVisible = node.parentId == null || unlockedIds.contains(node.parentId)
                
                if (isVisible && node.parentId != null) {"""
new_is_visible_lines = """            for (node in allNodes) {
                val isResearched = unlockedIds.contains(node.id) || node.rpCost == 0
                
                if (node.parentId != null) {"""
kt = kt.replace(old_is_visible_lines, new_is_visible_lines)

old_render_nodes = """        // Render Nodes
        nodes.forEach { node ->
            val isResearched = unlockedIds.contains(node.id) || node.rpCost == 0
            val isVisible = node.parentId == null || unlockedIds.contains(node.parentId)

            if (isVisible) {
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            translationX = (node.gridX + centerOffsetX - 75f) * scale + offsetX
                            translationY = (node.gridY + centerOffsetY - 40f) * scale + offsetY
                            scaleX = scale
                            scaleY = scale
                        }
                ) {
                    TechNodeCard(
                        node = node,
                        isResearched = isResearched,
                        canAfford = gameState.researchPoints >= node.rpCost,
                        isLabsBusy = isLabsBusy,
                        onResearchClick = { onUnlockTech(node.id) },
                        evaluateAction = evaluateAction
                    )
                }
            }
        }"""

new_render_nodes = """        // Render Nodes
        nodes.forEach { node ->
            val isResearched = unlockedIds.contains(node.id) || node.rpCost == 0
            val isLocked = node.parentId != null && !unlockedIds.contains(node.parentId)

            Box(
                modifier = Modifier
                    .graphicsLayer {
                        translationX = (node.gridX + centerOffsetX - 75f) * scale + offsetX
                        translationY = (node.gridY + centerOffsetY - 40f) * scale + offsetY
                        scaleX = scale
                        scaleY = scale
                    }
            ) {
                TechNodeCard(
                    node = node,
                    isResearched = isResearched,
                    isLocked = isLocked,
                    canAfford = gameState.researchPoints >= node.rpCost,
                    isLabsBusy = isLabsBusy,
                    onResearchClick = { onUnlockTech(node.id) },
                    evaluateAction = evaluateAction
                )
            }
        }"""

kt = kt.replace(old_render_nodes, new_render_nodes)

# 3. Update TechNodeCard signature and logic
old_techcard_sig = """fun TechNodeCard(
    node: TechTreeNode,
    isResearched: Boolean,
    canAfford: Boolean,
    isLabsBusy: Boolean,
    onResearchClick: () -> Unit,
    evaluateAction: (com.example.model.GameAction) -> com.example.model.GuidanceState
) {"""

new_techcard_sig = """fun TechNodeCard(
    node: TechTreeNode,
    isResearched: Boolean,
    isLocked: Boolean,
    canAfford: Boolean,
    isLabsBusy: Boolean,
    onResearchClick: () -> Unit,
    evaluateAction: (com.example.model.GameAction) -> com.example.model.GuidanceState
) {"""
kt = kt.replace(old_techcard_sig, new_techcard_sig)


# TechNodeCard Colors and Locked State
old_techcard_body = """    OutlinedCard(
        modifier = Modifier
            .width(150.dp)
            .height(80.dp)
            .clickable(enabled = !isResearched && !isLabsBusy && canAfford) { onResearchClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isResearched) MaterialTheme.colorScheme.primaryContainer else if (!isResearched && !isLabsBusy && canAfford) Color.DarkGray.copy(alpha = 0.85f) else Color.DarkGray.copy(alpha = 0.4f)
        ),
        border = BorderStroke(
            if (isResearched) 2.dp else 1.dp,
            if (isResearched) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isResearched) {
                Text(
                    text = "${node.iconEmoji} ${node.name}",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 2,
                    lineHeight = 14.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Researched",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Text(
                    text = node.name,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color = Color.LightGray.copy(alpha = 0.6f),
                    maxLines = 2,
                    lineHeight = 14.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val action = com.example.model.GameAction.Research(node.id)
                    val guidance = evaluateAction(action)
                    com.example.ui.components.GuidanceBadge(state = guidance)
                    Spacer(modifier = Modifier.width(4.dp))
                    if (isLabsBusy) {
                        Text(
                            text = "Slots Busy",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    } else if (!canAfford) {
                        Text(
                            text = "${node.rpCost} RP",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    } else {
                        Text(
                            text = "${node.rpCost} RP",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = TechCyan
                        )
                    }
                }
            }
        }
    }"""

new_techcard_body = """    val bgColor = when {
        isResearched -> MaterialTheme.colorScheme.primaryContainer
        isLocked -> Color.DarkGray.copy(alpha = 0.3f)
        canAfford && !isLabsBusy -> Color.DarkGray.copy(alpha = 0.85f)
        else -> Color.DarkGray.copy(alpha = 0.4f)
    }
    
    val borderColor = when {
        isResearched -> MaterialTheme.colorScheme.primary
        isLocked -> Color.Gray.copy(alpha = 0.3f)
        else -> Color.Gray.copy(alpha = 0.5f)
    }

    OutlinedCard(
        modifier = Modifier
            .width(150.dp)
            .height(80.dp)
            .clickable(enabled = !isResearched && !isLocked && !isLabsBusy && canAfford) { onResearchClick() },
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(if (isResearched) 2.dp else 1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isResearched) {
                Text(
                    text = "${node.iconEmoji} ${node.name}",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 2,
                    lineHeight = 14.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Researched",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            } else if (isLocked) {
                Text(
                    text = node.name,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray.copy(alpha = 0.5f),
                    maxLines = 2,
                    lineHeight = 14.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = Color.Gray.copy(alpha = 0.5f),
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Text(
                    text = node.name,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color = Color.LightGray.copy(alpha = 0.6f),
                    maxLines = 2,
                    lineHeight = 14.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val action = com.example.model.GameAction.Research(node.id)
                    val guidance = evaluateAction(action)
                    com.example.ui.components.GuidanceBadge(state = guidance)
                    Spacer(modifier = Modifier.width(4.dp))
                    if (isLabsBusy) {
                        Text(
                            text = "Slots Busy",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    } else if (!canAfford) {
                        Text(
                            text = "${node.rpCost} RP",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    } else {
                        Text(
                            text = "${node.rpCost} RP",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = TechCyan
                        )
                    }
                }
            }
        }
    }"""

kt = kt.replace(old_techcard_body, new_techcard_body)

with open('app/src/main/java/com/example/ui/screens/ResearchHubScreen.kt', 'w') as f:
    f.write(kt)

