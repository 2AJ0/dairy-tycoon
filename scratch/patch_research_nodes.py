import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/ResearchNode.kt')

# Add EXPERIMENTAL to ResearchCategory
kt = kt.replace(
    "enum class ResearchCategory {\n    PRODUCTS, PERSONAL, INDUSTRY, COMPANY\n}",
    "enum class ResearchCategory {\n    PRODUCTS, PERSONAL, INDUSTRY, COMPANY, EXPERIMENTAL\n}"
)

# Add the new ResearchEffects (if needed, or just None)
kt = kt.replace(
    "object UnlockScamDetection : ResearchEffect()\n    \n    object None : ResearchEffect()",
    "object UnlockScamDetection : ResearchEffect()\n    \n    object ZeroColdStorageMaintenance : ResearchEffect()\n    object DoubleLiquidThroughput : ResearchEffect()\n    object DoubleGlobalSellPrice : ResearchEffect()\n    \n    object None : ResearchEffect()"
)

# Add the new nodes to ALL_NODES
new_nodes = """        ResearchNode("tech_singularity", "AGI Market Singularity", "Achieve absolute automation and transcend the market.", ResearchCategory.COMPANY, 500000, 15, listOf("tech_scam_detection"), ResearchEffect.None),
        
        // EXPERIMENTAL
        ResearchNode("tech_exp_1", "Radiative Cooling", "Eliminates all maintenance costs for Cold Storage.", ResearchCategory.EXPERIMENTAL, 250000, 10, emptyList(), ResearchEffect.ZeroColdStorageMaintenance),
        ResearchNode("tech_exp_2", "Acoustic Levitation", "Doubles maximum processing throughput for liquid facilities.", ResearchCategory.EXPERIMENTAL, 500000, 15, listOf("tech_exp_1"), ResearchEffect.DoubleLiquidThroughput),
        ResearchNode("tech_exp_3", "Transparent Wood Packaging", "Doubles all global market sell prices by revolutionizing packaging.", ResearchCategory.EXPERIMENTAL, 1000000, 25, listOf("tech_exp_2"), ResearchEffect.DoubleGlobalSellPrice)
    )"""

kt = kt.replace(
    """        ResearchNode("tech_singularity", "AGI Market Singularity", "Achieve absolute automation and transcend the market.", ResearchCategory.COMPANY, 500000, 15, listOf("tech_scam_detection"), ResearchEffect.None)\n    )""",
    new_nodes
)

with open('app/src/main/java/com/example/model/ResearchNode.kt', 'w') as f:
    f.write(kt)
