sed -i '' '/val dailyQuota = contract.requiredQuantity/,/notes.add("💥 Contract Expired: Terms with ${rival.name} failed overall.")/d' app/src/main/java/com/example/viewmodel/GameViewModel.kt
