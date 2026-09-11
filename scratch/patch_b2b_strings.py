import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/B2BContract.kt')

kt = kt.replace('tickerSymbol = "$NEON",', 'tickerSymbol = "\\$NEON",')
kt = kt.replace('tickerSymbol = "$LLOG",', 'tickerSymbol = "\\$LLOG",')

with open('app/src/main/java/com/example/model/B2BContract.kt', 'w') as f:
    f.write(kt)
