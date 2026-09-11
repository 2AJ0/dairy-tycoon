package com.example.model

/**
 * ==========================================
 * AI / IDE AGENT INSTRUCTIONS:
 * ==========================================
 * This file defines the core Data Models and static Catalogs (TechTree, Rivals, Products).
 * 
 * CORE RULES FOR FIXING ERRORS:
 * 1. DATA INTEGRITY: These classes represent the persistent save state and configuration. 
 * 2. NO LOGIC HERE: Keep these as pure Kotlin `data class` or `object` singletons. Business logic belongs in `GameViewModel`.
 * 3. PRESERVE IDs: String IDs (e.g., "tech_pasteurization", "rival_global_whey") are used heavily in cross-referencing. Do not rename them.
 */

enum class MessageStatus { SENT, DELIVERED, READ }

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isFromPlayer: Boolean,
    val timestampDay: Int,
    val status: MessageStatus = MessageStatus.SENT
)

data class Mentor(
    val id: String,
    val name: String,
    val specialization: String,
    val iconEmoji: String,
    val hiddenAffinity: Int = 0,
    val unlockedFeatures: List<String> = emptyList(),
    val isBuffUnlocked: Boolean = false,
    val chatHistory: List<ChatMessage> = emptyList(),
    val hasGreetedToday: Boolean = false,
    val recentlyAskedTopics: List<String> = emptyList(),
    val isAbandoned: Boolean = false,
    val isTyping: Boolean = false
)

object MentorCatalog {
    val ALL_MENTORS = listOf(
        Mentor(
            id = "mentor_barnaby",
            name = "Barnaby",
            specialization = "Agricultural Operations & Cows",
            iconEmoji = "👨‍🌾",
            unlockedFeatures = listOf("Explain Pasteurization", "Tell me about cows", "How to use land"),
            chatHistory = listOf(
                ChatMessage(text = "Howdy, boss! Let me know if you need any advice on running the farm.", isFromPlayer = false, timestampDay = 1)
            )
        ),
        Mentor(
            id = "mentor_chad",
            name = "Chad",
            specialization = "Venture Capital & Business Efficiency",
            iconEmoji = "🧑‍💼",
            unlockedFeatures = listOf("What is EBITDA?", "Reduce maintenance", "Explain operating costs"),
            chatHistory = listOf(
                ChatMessage(text = "Ready to synergize our paradigms? Ping me for corporate strategy.", isFromPlayer = false, timestampDay = 1)
            )
        ),
        Mentor(
            id = "mentor_sterling",
            name = "Sterling",
            specialization = "Wall Street & B2B Contracts",
            iconEmoji = "🎩",
            unlockedFeatures = listOf("Explain stock dividends", "How do B2B contracts work?", "Market spot pricing"),
            chatHistory = listOf(
                ChatMessage(text = "Time is money. I'm here to ensure we extract maximum shareholder value.", isFromPlayer = false, timestampDay = 1)
            )
        )
    )
}
