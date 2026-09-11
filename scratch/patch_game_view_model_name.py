import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

old_func = """    private fun generateRandomEmpireName(): String {
        val prefixes = listOf("Golden", "Sunny", "Automated", "Merku", "Pananchery", "Quantum")
        val suffixes = listOf("Meadows", "Dairies", "Acres", "Pastures", "Holdings")
        return "${prefixes.random()} ${suffixes.random()}"
    }"""

kt = kt.replace(old_func, "")

with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(kt)
