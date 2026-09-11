import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/Building.kt')

old_b = """    val sabotagedDaysRemaining: Int = 0,
    val allocationPercentage: Int = 100
) {"""

new_b = """    val sabotagedDaysRemaining: Int = 0,
    val allocationPercentage: Int = 100,
    val isSpunOff: Boolean = false
) {"""

kt = kt.replace(old_b, new_b)

with open('app/src/main/java/com/example/model/Building.kt', 'w') as f:
    f.write(kt)
