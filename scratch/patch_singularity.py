import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/ResearchNode.kt')

old_tech = """        ResearchNode("tech_scam_detection", "Scam Detection Algorithm", "Detect incoming corporate cyber attacks one day early.", ResearchCategory.COMPANY, 8500, 5, listOf("res_cyber_defense"), ResearchEffect.UnlockScamDetection)
    )"""

new_tech = """        ResearchNode("tech_scam_detection", "Scam Detection Algorithm", "Detect incoming corporate cyber attacks one day early.", ResearchCategory.COMPANY, 8500, 5, listOf("res_cyber_defense"), ResearchEffect.UnlockScamDetection),
        ResearchNode("tech_singularity", "AGI Market Singularity", "Achieve absolute automation and transcend the market.", ResearchCategory.COMPANY, 500000, 15, listOf("tech_scam_detection"), ResearchEffect.None)
    )"""

kt = kt.replace(old_tech, new_tech)

with open('app/src/main/java/com/example/model/ResearchNode.kt', 'w') as f:
    f.write(kt)
