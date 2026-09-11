import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/SettingsScreen.kt')

old_sig = """    onExitToMainMenu: (() -> Unit)? = null,
    onBackClick: (() -> Unit)? = null,
    onMenuClick: (() -> Unit)? = null
) {"""

new_sig = """    onExitToMainMenu: (() -> Unit)? = null,
    onBackClick: (() -> Unit)? = null,
    onMenuClick: (() -> Unit)? = null,
    onRenameEmpire: ((String) -> Unit)? = null
) {"""

kt = kt.replace(old_sig, new_sig)

with open('app/src/main/java/com/example/ui/screens/SettingsScreen.kt', 'w') as f:
    f.write(kt)
