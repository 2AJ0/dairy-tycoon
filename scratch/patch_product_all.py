import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/ProductAndInventory.kt')

old_all = """    val ALL_PRODUCTS = listOf(
        COW_FEED,
        GLASS_BOTTLES,
        RAW_MILK,"""

new_all = """    val ALL_PRODUCTS = listOf(
        COW_FEED,
        GLASS_BOTTLES,
        BIO_SYNTH_DAIRY,
        RAW_MILK,"""

kt = kt.replace(old_all, new_all)

with open('app/src/main/java/com/example/model/ProductAndInventory.kt', 'w') as f:
    f.write(kt)
