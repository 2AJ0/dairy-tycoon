import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/MainGameScreen.kt')

# Rename showInbox -> showInboxModal
kt = kt.replace("var showInbox by rememberSaveable { mutableStateOf(false) }", "var showInboxModal by rememberSaveable { mutableStateOf(false) }")
kt = kt.replace("if (showInbox) {", "if (showInboxModal) {")
kt = kt.replace("onDismiss = { showInbox = false },", "onDismiss = { showInboxModal = false },")
kt = kt.replace("onInboxClick = { showInbox = true }", "onInboxClick = { showInboxModal = true }")

with open('app/src/main/java/com/example/ui/screens/MainGameScreen.kt', 'w') as f:
    f.write(kt)

