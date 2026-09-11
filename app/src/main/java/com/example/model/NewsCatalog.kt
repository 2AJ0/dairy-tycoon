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

/**
 * Curated catalog of dynamic commodity news and economic bulletins.
 */
object NewsCatalog {

    val ALL_NEWS_EVENTS: List<NewsEvent> = listOf(
        NewsEvent(
            title = "National Artisan Cheese Gala Sweeps Media!",
            description = "Culinary judges at the Grand Epicurean Expo awarded top honors to aged farm cheddar. Delis and bistros are bidding aggressively on quality wheels.",
            targetProductId = ProductCatalog.AGED_CHEDDAR.id,
            multiplier = 1.55,
            durationDays = 3,
            reputationDelta = 2,
            iconEmoji = "🧀"
        ),
        NewsEvent(
            title = "Boutique Bakery Craze Grips Metropolis",
            description = "A viral pastry trend for laminated golden croissants has created an acute commercial shortage of churned butter.",
            targetProductId = ProductCatalog.BUTTER.id,
            multiplier = 1.45,
            durationDays = 2,
            reputationDelta = 1,
            iconEmoji = "🧈"
        ),
        NewsEvent(
            title = "Health Influencers Promote Fresh Whole Milk",
            description = "Fitness icons and sports dietitians highlight the recovery power of whole dairy. Consumer retail milk orders have surged across supermarkets.",
            targetProductId = ProductCatalog.PASTEURIZED_MILK.id,
            multiplier = 1.40,
            durationDays = 3,
            reputationDelta = 2,
            iconEmoji = "🥛"
        ),
        NewsEvent(
            title = "Wood-Fired Pizzeria Craze Booms",
            description = "Neapolitan restaurants and rustic eateries order heavy consignments of fresh curd and farmhouse cheeses.",
            targetProductId = ProductCatalog.FRESH_CHEESE.id,
            multiplier = 1.40,
            durationDays = 2,
            reputationDelta = 1,
            iconEmoji = "🧀"
        ),
        NewsEvent(
            title = "Third-Wave Coffee Wave Demands Heavy Cream",
            description = "Specialty cafes debut signature cold foam and espresso concoctions. Commercial bids for rich fresh cream hit yearly highs.",
            targetProductId = ProductCatalog.CREAM.id,
            multiplier = 1.50,
            durationDays = 3,
            reputationDelta = 1,
            iconEmoji = "☕"
        ),
        NewsEvent(
            title = "National Dairy Association Week Proclaimed!",
            description = "The Department of Agriculture launches an expansive nationwide campaign promoting all domestic dairy commodities.",
            targetProductId = null, // Global boost across all products
            multiplier = 1.25,
            durationDays = 2,
            reputationDelta = 3,
            iconEmoji = "🎉"
        ),
        NewsEvent(
            title = "Federal Farm Modernization Grant Distributed",
            description = "State Treasury releases emergency technology and utility relief funds to registered dairy operators.",
            targetProductId = null,
            multiplier = 1.0,
            cashBonus = 250.0,
            durationDays = 1,
            reputationDelta = 1,
            iconEmoji = "🏛️"
        ),
        NewsEvent(
            title = "Freight Corridor Delay Delays Logistics",
            description = "Tanker driver strikes and rail bottlenecks cause temporary regional backlogs. Raw milk spot bids soften slightly.",
            targetProductId = ProductCatalog.RAW_MILK.id,
            multiplier = 0.75,
            durationDays = 2,
            reputationDelta = 0,
            iconEmoji = "🚛"
        ),
        NewsEvent(
            title = "Synthetic Alternative Beverage PR Blitz",
            description = "Venture-backed oat milk marketers flood media channels with ads. Fluid milk bids experience a minor slump.",
            targetProductId = ProductCatalog.RAW_MILK.id,
            multiplier = 0.82,
            durationDays = 2,
            reputationDelta = 0,
            iconEmoji = "📉"
        )
    )

    fun getRandomEvent(): NewsEvent {
        val template = ALL_NEWS_EVENTS.random()
        return template.copy(
            id = java.util.UUID.randomUUID().toString(),
            remainingDays = template.durationDays
        )
    }
}
