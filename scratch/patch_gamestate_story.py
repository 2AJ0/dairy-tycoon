import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/GameState.kt')

old_gs = """    val unlockedAnomalies: List<String> = emptyList()
) {"""

new_gs = """    val unlockedAnomalies: List<String> = emptyList(),
    val readStoryEvents: Set<String> = emptySet(),
    val pendingStoryEvents: List<StoryEvent> = emptyList()
) {"""

kt = kt.replace(old_gs, new_gs)

with open('app/src/main/java/com/example/model/GameState.kt', 'w') as f:
    f.write(kt)

