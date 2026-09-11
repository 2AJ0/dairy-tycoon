import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/ProductAndInventory.kt')

new_product = """    val BIO_SYNTH_DAIRY = Product(
        id = "bio_synth_dairy",
        name = "Bio-Synthesized Dairy",
        category = ProductCategory.PROCESSED,
        description = "Lab-grown dairy alternative.",
        basePrice = 25.0,
        shelfLifeDays = 9999,
        tier = 5,
        emoji = "🧫"
    )

    val RAW_MILK"""

kt = kt.replace('    val RAW_MILK', new_product)

with open('app/src/main/java/com/example/model/ProductAndInventory.kt', 'w') as f:
    f.write(kt)
