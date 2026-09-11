import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/GameState.kt')

old_save_name = """    val saveName: String = "Farm_${java.util.UUID.randomUUID().toString().substring(0, 5)}","""

new_save_name = """    val saveName: String = generateRandomEmpireName(),"""

kt = kt.replace(old_save_name, new_save_name)

companion_obj = """
    companion object {
        fun generateRandomEmpireName(): String {
            val prefixes = listOf("Golden", "Sunny", "Automated", "Merku", "Pananchery", "Quantum")
            val suffixes = listOf("Meadows", "Dairies", "Acres", "Pastures", "Holdings")
            return "${prefixes.random()} ${suffixes.random()}"
        }
    }
}
"""

if "generateRandomEmpireName" not in kt:
    kt = kt.rsplit("}", 1)
    kt = kt[0] + companion_obj

with open('app/src/main/java/com/example/model/GameState.kt', 'w') as f:
    f.write(kt)
