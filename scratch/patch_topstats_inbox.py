import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/components/TopStatsHeader.kt')

# Add Inbox icon import
kt = kt.replace("import androidx.compose.material.icons.filled.Work", "import androidx.compose.material.icons.filled.Work\nimport androidx.compose.material.icons.filled.Mail\nimport androidx.compose.material.icons.filled.Email")

# Add onInboxClick parameter
old_sig = """    onOpenSkillTree: () -> Unit,
    onReputationClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {}
) {"""

new_sig = """    onOpenSkillTree: () -> Unit,
    onReputationClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {},
    onInboxClick: () -> Unit = {}
) {"""
kt = kt.replace(old_sig, new_sig)

# Add Inbox icon next to Hamburger menu
old_row1 = """                    IconButton(
                        onClick = onMenuClick,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("menu_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Main Menu",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))"""

new_row1 = """                    IconButton(
                        onClick = onMenuClick,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("menu_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Main Menu",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(modifier = Modifier.size(36.dp), contentAlignment = Alignment.Center) {
                        IconButton(onClick = onInboxClick) {
                            Icon(Icons.Default.Email, contentDescription = "Inbox", tint = MaterialTheme.colorScheme.onSurface)
                        }
                        if (gameState.pendingStoryEvents.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(BearishRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = gameState.pendingStoryEvents.size.toString(),
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(6.dp))"""

kt = kt.replace(old_row1, new_row1)

with open('app/src/main/java/com/example/ui/components/TopStatsHeader.kt', 'w') as f:
    f.write(kt)
