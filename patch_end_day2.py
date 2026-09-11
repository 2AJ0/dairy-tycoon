import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/viewmodel/GameViewModel.kt')

old_copy = """                    unlockedFeatures = currentState.unlockedFeatures.copy(
                        isBoardroomUnlocked = shouldUnlockBoardroom,
                        isStockMarketUnlocked = shouldUnlockStockMarket,
                        isBoardroomNew = isNewBoardroom,
                        isStockMarketNew = isNewStockMarket
                    )
                )"""

new_copy = """                    unlockedFeatures = currentState.unlockedFeatures.copy(
                        isBoardroomUnlocked = shouldUnlockBoardroom,
                        isStockMarketUnlocked = shouldUnlockStockMarket,
                        isBoardroomNew = isNewBoardroom,
                        isStockMarketNew = isNewStockMarket
                    ),
                    missedDeliveryEvent = missedEventId
                )"""

kt = kt.replace(old_copy, new_copy)

with open('app/src/main/java/com/example/viewmodel/GameViewModel.kt', 'w') as f:
    f.write(kt)

