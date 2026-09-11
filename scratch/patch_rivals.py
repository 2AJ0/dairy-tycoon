import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/B2BContract.kt')

# 1. Update Global Whey
old_whey = """        specialtyProduct = ProductCatalog.RAW_MILK.id,"""
new_whey = """        specialtyProduct = ProductCatalog.WHEY.id,"""
kt = kt.replace(old_whey, new_whey)

# 2. Add Neon Cow Genetics and Lactose Logistics
old_catalog = """    val ALL_RIVALS = listOf(
        DAIRY_KING,
        UDDER_DELIGHT,
        MOO_MOO_FARMS,
        MILKY_WAY,
        GLOBAL_WHEY,
        LACTO_DYNASTY
    )"""

new_rivals = """    val NEON_COW = RivalCompany(
        id = "rival_neon_cow",
        name = "Neon Cow Genetics",
        tickerSymbol = "$NEON",
        logoEmoji = "🧬",
        tagline = "Engineering the perfect bovine.",
        marketPower = 2.0f,
        stockPrice = 145.50,
        stockChangePercent = +1.2,
        specialtyProduct = ProductCatalog.BIO_SYNTH_DAIRY.id,
        headquarters = "Neo-Tokyo, Japan",
        corporateTone = "Futuristic Biotech Firm",
        totalShares = 100,
        dailyDividendPerShare = 3.50,
        subsidiaryPerkTitle = "Genomic Sequencing",
        subsidiaryPerkDescription = "+20% Base quality to all raw milk produced.",
        targetSector = ProductCategory.PROCESSED,
        tier = RivalTier.SPECIALIZED,
        defenseRating = 60,
        offenseRating = 40,
        netWorth = 1250000.0
    )

    val LACTOSE_LOGISTICS = RivalCompany(
        id = "rival_lactose_logistics",
        name = "Lactose Logistics",
        tickerSymbol = "$LLOG",
        logoEmoji = "🚚",
        tagline = "Moving milk at the speed of light.",
        marketPower = 1.2f,
        stockPrice = 45.20,
        stockChangePercent = -0.3,
        specialtyProduct = ProductCatalog.PASTEURIZED_MILK.id,
        headquarters = "Chicago, IL",
        corporateTone = "Gritty Supply Chain Empire",
        totalShares = 100,
        dailyDividendPerShare = 1.00,
        subsidiaryPerkTitle = "Supply Chain Dominance",
        subsidiaryPerkDescription = "Reduces logistics and maintenance costs by 15%.",
        targetSector = ProductCategory.RAW,
        tier = RivalTier.LOCAL,
        defenseRating = 25,
        offenseRating = 15,
        netWorth = 250000.0
    )

    val ALL_RIVALS = listOf(
        DAIRY_KING,
        UDDER_DELIGHT,
        MOO_MOO_FARMS,
        MILKY_WAY,
        GLOBAL_WHEY,
        LACTO_DYNASTY,
        NEON_COW,
        LACTOSE_LOGISTICS
    )"""

kt = kt.replace(old_catalog, new_rivals)

with open('app/src/main/java/com/example/model/B2BContract.kt', 'w') as f:
    f.write(kt)

