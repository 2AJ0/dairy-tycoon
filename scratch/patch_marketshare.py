import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/B2BContract.kt')

# Add marketShare to RivalCompany
old_rival = """    val threatLevel: Int = 10
) {"""

new_rival = """    val threatLevel: Int = 10,
    val marketShare: Float = 15.0f
) {"""

kt = kt.replace(old_rival, new_rival)

with open('app/src/main/java/com/example/model/B2BContract.kt', 'w') as f:
    f.write(kt)
