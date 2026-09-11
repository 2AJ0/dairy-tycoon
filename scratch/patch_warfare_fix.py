import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/CorporateWarfareScreen.kt')

old_state = """    var selectedTarget by remember { mutableStateOf<RivalCompany?>(null) }"""
new_state = """    var selectedTarget by remember { mutableStateOf<RivalCompany?>(null) }
    var sortAscending by remember { mutableStateOf(false) }"""

kt = kt.replace(old_state, new_state)

with open('app/src/main/java/com/example/ui/screens/CorporateWarfareScreen.kt', 'w') as f:
    f.write(kt)
