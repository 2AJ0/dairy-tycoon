import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/ResearchHubScreen.kt')

# Find the max width and height
old_box = """    Box(
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
        ) {"""

new_box = """    val maxXPx = nodePositions.values.maxOfOrNull { it.x } ?: 0f
    val maxYPx = nodePositions.values.maxOfOrNull { it.y } ?: 0f
    val contentWidthDp = with(density) { (maxXPx + nodeWidthPx + 100f).toDp() }
    val contentHeightDp = with(density) { (maxYPx + nodeHeightPx + 100f).toDp() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clipToBounds()
            .androidx.compose.foundation.horizontalScroll(androidx.compose.foundation.rememberScrollState())
            .androidx.compose.foundation.verticalScroll(androidx.compose.foundation.rememberScrollState())
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(32.dp)
    ) {
        Box(
            modifier = Modifier.size(contentWidthDp, contentHeightDp)
        ) {"""

kt = kt.replace(old_box, new_box)

old_padding = "modifier = Modifier.padding(start = xDp, top = yDp)"
new_padding = "modifier = Modifier.offset(x = xDp, y = yDp)"

kt = kt.replace(old_padding, new_padding)

with open('app/src/main/java/com/example/ui/screens/ResearchHubScreen.kt', 'w') as f:
    f.write(kt)
