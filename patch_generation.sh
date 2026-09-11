sed -i '' '/val newOffer = ContractOffer(/i\
                    val type = if (Random.nextBoolean()) com.example.model.ContractType.DAILY_QUOTA else com.example.model.ContractType.BULK_DEADLINE\
                    val targetTotal = if (type == com.example.model.ContractType.BULK_DEADLINE) baseQuota * duration else 0\
' app/src/main/java/com/example/viewmodel/GameViewModel.kt

sed -i '' 's/requiredQuantity = baseQuota,/contractType = type,\n                        targetTotalQuantity = targetTotal,\n                        requiredQuantity = baseQuota,/g' app/src/main/java/com/example/viewmodel/GameViewModel.kt
