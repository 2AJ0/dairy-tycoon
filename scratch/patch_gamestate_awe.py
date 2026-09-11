import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/GameState.kt')

kt = kt.replace(
    "val isStandardTreeComplete: Boolean = false",
    "val isStandardTreeComplete: Boolean = false,\n    val showAweDialogue: Boolean = false,\n    val showAweHighlight: Boolean = false,\n    val hasSeenAweTutorial: Boolean = false"
)

with open('app/src/main/java/com/example/model/GameState.kt', 'w') as f:
    f.write(kt)
