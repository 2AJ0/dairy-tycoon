import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/ResearchNode.kt')

old_defense = """        ResearchNode("res_cyber_defense", "Cybersecurity Protocols", "Defend against corporate espionage.", ResearchCategory.COMPANY, 12000, 5, listOf("res_dedicated_labs"), ResearchEffect.IncreaseCyberDefense(20))
    )"""

new_defense = """        ResearchNode("res_cyber_defense", "Cybersecurity Protocols", "Defend against corporate espionage.", ResearchCategory.COMPANY, 12000, 5, listOf("res_dedicated_labs"), ResearchEffect.IncreaseCyberDefense(20)),
        ResearchNode("res_corp_offense", "Zero-Day Exploits", "Equip your hackers with powerful new tools (+15 Offense).", ResearchCategory.COMPANY, 10000, 7, listOf("res_cyber_defense"), ResearchEffect.IncreaseCyberOffense(15)),
        ResearchNode("tech_scam_detection", "Scam Detection Algorithm", "Detect incoming corporate cyber attacks one day early.", ResearchCategory.COMPANY, 8500, 5, listOf("res_cyber_defense"), ResearchEffect.UnlockScamDetection)
    )"""

kt = kt.replace(old_defense, new_defense)

with open('app/src/main/java/com/example/model/ResearchNode.kt', 'w') as f:
    f.write(kt)
