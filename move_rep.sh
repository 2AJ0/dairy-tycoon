sed -i '' '1190,1192d' app/src/main/java/com/example/viewmodel/GameViewModel.kt

sed -i '' '/\/\/ Step 3c: B2B Contract Auto-Fulfillment/i\
                \/\/ Update Reputation before Contracts & Sales\
                val (baseUpdatedRep, repBreakdown) = calculateDailyReputation(currentState, processedItemIdsThisDay)\
                val repGainedFromNews = if (isNewEventTriggered) (activeNews?.reputationDelta ?: 0) else 0\
                val updatedReputation = (baseUpdatedRep + repGainedFromNews).coerceIn(0, 100)\
                val finalRepBreakdown = repBreakdown.copy(penalties = repBreakdown.penalties - repGainedFromNews.coerceAtMost(0))\
' app/src/main/java/com/example/viewmodel/GameViewModel.kt
