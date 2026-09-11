import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

# 1. Update CorporateWarfareScreen signature
kt = read_file('app/src/main/java/com/example/ui/screens/CorporateWarfareScreen.kt')
kt = kt.replace(
    "onQueueAttack: (String, AttackType, Double, Int) -> Unit,",
    "onQueueAttack: (String, AttackType, Double, Int) -> Unit,\n    onBuyShares: (String, Int) -> Unit,"
)
kt = kt.replace(
    "onClick = { /* Need to call viewmodel buyRivalShares */ }",
    "onClick = { onBuyShares(rival.id, buyAmount.toInt()) }"
)
with open('app/src/main/java/com/example/ui/screens/CorporateWarfareScreen.kt', 'w') as f:
    f.write(kt)

# 2. Update MainGameScreen invocation
kt2 = read_file('app/src/main/java/com/example/ui/screens/MainGameScreen.kt')
kt2 = kt2.replace(
    "onQueueAttack = { rivalId, type, cost, apCost -> viewModel.queuePlayerCyberAttack(rivalId, type, cost, apCost) },",
    "onQueueAttack = { rivalId, type, cost, apCost -> viewModel.queuePlayerCyberAttack(rivalId, type, cost, apCost) },\n                            onBuyShares = { rivalId, amount -> viewModel.buyRivalShares(rivalId, amount) },"
)
with open('app/src/main/java/com/example/ui/screens/MainGameScreen.kt', 'w') as f:
    f.write(kt2)
