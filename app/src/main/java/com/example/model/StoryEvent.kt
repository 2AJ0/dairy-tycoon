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

enum class StoryTheme {
    FARMING,
    CORPORATE,
    EXPERIMENTAL
}

data class StoryEvent(
    val id: String,
    val title: String,
    val body: String,
    val sender: String,
    val phaseTheme: StoryTheme
)

object StoryCatalog {
    val ALL_EVENTS = listOf(
        StoryEvent(
            id = "event_farming_1",
            title = "Welcome to the Farm!",
            body = "Hey there!\n\nI'm Barnaby. I wanted to formally welcome you to your new life out here. Things might seem a bit slow right now, just you and the cows, but it's honest work. I've sent over my trusty golden retriever, Merku. He's a good boy and he'll keep you company while you learn the ropes.\n\nTake it one day at a time.\n\nBest,\nBarnaby",
            sender = "Barnaby (Local Mentor)",
            phaseTheme = StoryTheme.FARMING
        ),
        StoryEvent(
            id = "event_corporate_1",
            title = "INTERCEPTED TRANSMISSION",
            body = "To all regional directors:\n\nWe have a situation. That 'quaint' little dairy operation just broke $100k in net valuation. I don't care if they started in a shed with a golden retriever—they are now encroaching on our market share.\n\nInitiate Phase 1 suppression. Flood their logistics networks and prep the legal team.\n\n- CEO, Global Whey Corp",
            sender = "UNKNOWN SENDER (Intercepted)",
            phaseTheme = StoryTheme.CORPORATE
        ),
        StoryEvent(
            id = "event_exp_1",
            title = "AUDIO TRANSCRIPT: LAB INITIALIZATION",
            body = "Log 001. Dr. A.W.E. speaking.\n\nThe Advanced Lab is officially online. The core reactor is stable, but the quantum thresholds are... erratic. The standard tech tree was child's play compared to what we're looking at now.\n\nCEO, we are about to bend reality. Prepare the capital reserves. We're going to need millions.\n\n[END LOG]",
            sender = "Dr. A.W.E.",
            phaseTheme = StoryTheme.EXPERIMENTAL
        )
    )
}
