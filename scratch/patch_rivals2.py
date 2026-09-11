import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/B2BContract.kt')

old_catalog = """    val ALL_RIVALS: List<RivalCompany> = listOf(
        MOOCORP,
        GLOBAL_WHEY,
        LACTO_DYNASTY,
        ALPINE_BOVINE
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

    val ALL_RIVALS: List<RivalCompany> = listOf(
        MOOCORP,
        GLOBAL_WHEY,
        LACTO_DYNASTY,
        ALPINE_BOVINE,
        NEON_COW,
        LACTOSE_LOGISTICS
    )"""

if old_catalog in kt:
    kt = kt.replace(old_catalog, new_rivals)
    with open('app/src/main/java/com/example/model/B2BContract.kt', 'w') as f:
        f.write(kt)
    print("Patched successfully")
else:
    print("Could not find old catalog")
