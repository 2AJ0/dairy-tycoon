import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/MainGameScreen.kt')

kt = kt.replace("var showInbox by remember { mutableStateOf(false) }", "var showInboxModal by remember { mutableStateOf(false) }")
kt = kt.replace("if (showInbox) {", "if (showInboxModal) {")

with open('app/src/main/java/com/example/ui/screens/MainGameScreen.kt', 'w') as f:
    f.write(kt)
