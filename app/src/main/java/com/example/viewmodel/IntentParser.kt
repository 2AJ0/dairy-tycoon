package com.example.viewmodel

object IntentParser {
    
    enum class IntentType {
        GREETING,
        TOPIC_INQUIRY,
        GIBBERISH,
        FEATURE_QUERY,
        UNKNOWN
    }
    
    data class ParsedIntent(val type: IntentType, val matchedTopic: String? = null)

    private val greetingRegex = Regex("(?i)\\b(hi|hello|hey|greetings|howdy|sup|morning|evening)\\b")
    
    fun parseMessage(message: String, availableFeatures: List<String>): ParsedIntent {
        if (greetingRegex.containsMatchIn(message)) {
            return ParsedIntent(IntentType.GREETING)
        }
        
        // Simple exact or partial match for features
        for (feature in availableFeatures) {
            val featureWords = feature.lowercase().split(" ", "?", "!", ".", ",").filter { it.length > 3 }
            val messageLower = message.lowercase()
            
            // If the message contains the exact feature string or most of its key words
            if (messageLower.contains(feature.lowercase().replace("?", ""))) {
                return ParsedIntent(IntentType.FEATURE_QUERY, feature)
            }
            
            if (featureWords.isNotEmpty() && featureWords.any { messageLower.contains(it) }) {
                return ParsedIntent(IntentType.FEATURE_QUERY, feature)
            }
        }
        
        return ParsedIntent(IntentType.UNKNOWN)
    }

    fun getResponseForFeature(mentorId: String, feature: String): String {
        return when (mentorId) {
            "mentor_barnaby" -> {
                when {
                    feature.contains("Pasteurization") -> "Pasteurization extends shelf life. Highly recommended if you can't sell your raw milk fast enough before it spoils."
                    feature.contains("cows") -> "Cows are the heart of this farm! Keep them fed, make sure you don't overwork them, and they'll produce quality milk."
                    feature.contains("land") -> "Buy more land when you run out of space for pastures or processing facilities. But watch out, price goes up each time!"
                    else -> "That's a good question, but you might want to ask someone else about it."
                }
            }
            "mentor_chad" -> {
                when {
                    feature.contains("EBITDA") -> "Earnings Before Interest, Taxes, Depreciation, and Amortization. Basically, it's our core operating profitability! Keep it green."
                    feature.contains("maintenance") -> "Facilities cost money every single day just to exist. Upgrade them efficiently so you get more output per dollar of maintenance."
                    feature.contains("operating costs") -> "Feed for cows and maintenance for buildings. These are fixed overheads. You MUST out-earn these daily or we go bankrupt."
                    else -> "Hmm, let me circle back to you on that one."
                }
            }
            "mentor_sterling" -> {
                when {
                    feature.contains("stock dividends") -> "When we buy out rival companies, we earn a slice of their daily profits as a dividend. It's passive income at its finest."
                    feature.contains("B2B contracts") -> "Corporate B2B contracts lock in a high premium price for your goods, but if you fail to deliver the quota, the penalty is severe."
                    feature.contains("spot pricing") -> "The wholesale market price fluctuates daily. Don't dump all your inventory when prices are low unless it's about to spoil!"
                    else -> "That's above my pay grade. Try another mentor."
                }
            }
            else -> "I have no information on that topic."
        }
    }
}
