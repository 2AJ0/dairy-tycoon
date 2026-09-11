import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/model/ResearchNode.kt')

old_cyber = """    data class IncreaseCyberDefense(val amount: Int) : ResearchEffect()
    data class IncreaseCyberOffense(val amount: Int) : ResearchEffect()
    object UnlockAdvancedB2B : ResearchEffect()"""

new_cyber = """    data class IncreaseCyberDefense(val amount: Int) : ResearchEffect()
    data class IncreaseCyberOffense(val amount: Int) : ResearchEffect()
    object UnlockAdvancedB2B : ResearchEffect()
    object UnlockScamDetection : ResearchEffect()"""

kt = kt.replace(old_cyber, new_cyber)

old_nodes = """        ResearchNode("res_corp_offense", "Zero-Day Exploits", "Equip your hackers with powerful new tools (+15 Offense).", ResearchCategory.COMPANY, 10000, 7, listOf("res_corp_defense"), ResearchEffect.IncreaseCyberOffense(15))
    )
}"""

new_nodes = """        ResearchNode("res_corp_offense", "Zero-Day Exploits", "Equip your hackers with powerful new tools (+15 Offense).", ResearchCategory.COMPANY, 10000, 7, listOf("res_corp_defense"), ResearchEffect.IncreaseCyberOffense(15)),
        ResearchNode("tech_scam_detection", "Scam Detection Algorithm", "Detect incoming corporate cyber attacks one day early.", ResearchCategory.COMPANY, 8500, 5, listOf("res_corp_defense"), ResearchEffect.UnlockScamDetection)
    )
}"""

if 'ResearchNode("res_corp_offense"' in kt:
    kt = kt.replace(old_nodes, new_nodes)
else:
    # Append if not exactly matching
    pass

with open('app/src/main/java/com/example/model/ResearchNode.kt', 'w') as f:
    f.write(kt)
