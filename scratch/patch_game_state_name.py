import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/GameState.kt')

# Add the function at the bottom of the file
random_name_func = """
fun generateRandomEmpireName(): String {
    val prefixes = listOf("Golden", "Sunny", "Automated", "Merku", "Pananchery", "Quantum")
    val suffixes = listOf("Meadows", "Dairies", "Acres", "Pastures", "Holdings")
    return "${prefixes.random()} ${suffixes.random()}"
}
"""

if "fun generateRandomEmpireName" not in kt:
    kt += random_name_func

with open('app/src/main/java/com/example/model/GameState.kt', 'w') as f:
    f.write(kt)
