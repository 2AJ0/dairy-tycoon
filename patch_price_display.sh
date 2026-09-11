sed -i '' '/Column(horizontalAlignment = Alignment.End) {/!b;n;n;n;n;n;n;n;c\
                    Row(verticalAlignment = Alignment.CenterVertically) {\
                        if (repMultiplier > 1.0) {\
                            Icon(\
                                imageVector = Icons.Default.Star,\
                                contentDescription = "Reputation Premium",\
                                tint = BullishGreen,\
                                modifier = Modifier.size(16.dp)\
                            )\
                            Spacer(modifier = Modifier.width(4.dp))\
                        }\
                        Text(\
                            text = "$${String.format("%.2f", currentPrice)}",\
                            style = MaterialTheme.typography.titleLarge,\
                            fontWeight = FontWeight.Black,\
                            color = if (isAboveBase) BullishGreen else BearishRed,\
                            modifier = Modifier.testTag("price_${product.id}")\
                        )\
                    }
' app/src/main/java/com/example/ui/screens/MarketTab.kt
