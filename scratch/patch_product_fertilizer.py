import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/ProductAndInventory.kt')

# Find a good place to insert fertilizer, like at the end of the list
new_prod = """        Product("cheese", "Aged Cheese", ProductCategory.ARTISANAL, 100.0, 30.0f),
        Product("butter", "Craft Butter", ProductCategory.ARTISANAL, 75.0, 15.0f),
        Product("ice_cream", "Premium Ice Cream", ProductCategory.ARTISANAL, 150.0, 10.0f),
        Product("fertilizer", "Bio-Fertilizer", ProductCategory.PROCESSED, 25.0, 365.0f)
    )"""

kt = kt.replace(
    """        Product("cheese", "Aged Cheese", ProductCategory.ARTISANAL, 100.0, 30.0f),
        Product("butter", "Craft Butter", ProductCategory.ARTISANAL, 75.0, 15.0f),
        Product("ice_cream", "Premium Ice Cream", ProductCategory.ARTISANAL, 150.0, 10.0f)
    )""",
    new_prod
)

with open('app/src/main/java/com/example/model/ProductAndInventory.kt', 'w') as f:
    f.write(kt)
