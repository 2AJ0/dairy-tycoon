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
 * Technology Node in the corporate Tech Tree.
 */
data class TechTreeNode(
    val id: String,
    val name: String,
    val rpCost: Int,
    val parentId: String?,
    val requiredTechIds: List<String> = if (parentId != null) listOf(parentId) else emptyList(),
    val gridX: Float,
    val gridY: Float,
    val daysToComplete: Int = 1,
    val iconEmoji: String = "🔬",
    val description: String = "",
    val category: ResearchCategory = ResearchCategory.PRODUCTS
)

object TechCatalog {
    val RAW_MILK = TechTreeNode(
        id = "tech_raw_milk",
        name = "Raw Milk",
        rpCost = 0,
        parentId = null,
        gridX = 0f,
        gridY = 0f,
        daysToComplete = 0,
        iconEmoji = "🐄",
        description = "Unprocessed bovine lactation."
    )

    // BRANCH A: Heat Processing (Y = -150f)
    val PASTEURIZED_MILK = TechTreeNode(
        id = "tech_pasteurization", // Keep legacy ID
        name = "Pasteurized Whole Milk & Shelf Stable Milk",
        rpCost = 5,
        parentId = "tech_raw_milk",
        gridX = 200f,
        gridY = -150f,
        daysToComplete = 1,
        iconEmoji = "🥛",
        description = "Heat treatment for safety."
    )
    val COFFEE_MILK = TechTreeNode(
        id = "tech_coffee_milk",
        name = "Coffee Milk",
        rpCost = 8,
        parentId = "tech_pasteurization",
        gridX = 400f,
        gridY = -150f,
        daysToComplete = 1,
        iconEmoji = "☕"
    )
    val CONDENSED_MILK = TechTreeNode(
        id = "tech_condensed_milk",
        name = "Condensed Milk & Milk Powder",
        rpCost = 15,
        parentId = "tech_coffee_milk",
        gridX = 600f,
        gridY = -150f,
        daysToComplete = 2,
        iconEmoji = "🥫"
    )

    // BRANCH B: Cultures & Fermentation (Y = 0)
    val YOGURT = TechTreeNode(
        id = "tech_yogurt",
        name = "Yogurt",
        rpCost = 10,
        parentId = "tech_raw_milk",
        gridX = 200f,
        gridY = 0f,
        daysToComplete = 1,
        iconEmoji = "🥣"
    )
    val KEFIR = TechTreeNode(
        id = "tech_kefir",
        name = "Kefir",
        rpCost = 15,
        parentId = "tech_yogurt",
        gridX = 400f,
        gridY = 0f,
        daysToComplete = 1,
        iconEmoji = "🍶"
    )
    val CURDS_WHEY = TechTreeNode(
        id = "tech_curds_whey",
        name = "Curds & Whey",
        rpCost = 20,
        parentId = "tech_kefir",
        gridX = 600f,
        gridY = 0f,
        daysToComplete = 2,
        iconEmoji = "🧀"
    )

    // Branch B1: Whey Line
    val RICOTTA = TechTreeNode(
        id = "tech_ricotta",
        name = "Ricotta",
        rpCost = 15,
        parentId = "tech_curds_whey",
        gridX = 800f,
        gridY = -50f,
        daysToComplete = 1,
        iconEmoji = "🧀"
    )
    val BRUNOST = TechTreeNode(
        id = "tech_brunost",
        name = "Brunost",
        rpCost = 20,
        parentId = "tech_ricotta",
        gridX = 1000f,
        gridY = -50f,
        daysToComplete = 2,
        iconEmoji = "🧀"
    )
    val WHEY_PROTEIN = TechTreeNode(
        id = "tech_whey_protein",
        name = "Whey Protein Powder",
        rpCost = 30,
        parentId = "tech_brunost",
        gridX = 1200f,
        gridY = -50f,
        daysToComplete = 3,
        iconEmoji = "💪"
    )

    // Branch B2: Curd Line
    val QUARK = TechTreeNode(
        id = "tech_quark",
        name = "Quark",
        rpCost = 15,
        parentId = "tech_curds_whey",
        gridX = 800f,
        gridY = 50f,
        daysToComplete = 1,
        iconEmoji = "🥄"
    )
    val COTTAGE_CHEESE = TechTreeNode(
        id = "tech_cottage_cheese",
        name = "Cottage Cheese",
        rpCost = 20,
        parentId = "tech_quark",
        gridX = 1000f,
        gridY = 50f,
        daysToComplete = 1,
        iconEmoji = "🥗"
    )
    val MOZZARELLA = TechTreeNode(
        id = "tech_mozzarella",
        name = "Mozzarella",
        rpCost = 25,
        parentId = "tech_cottage_cheese",
        gridX = 1200f,
        gridY = 50f,
        daysToComplete = 2,
        iconEmoji = "🍕"
    )
    val HALLOUMI = TechTreeNode(
        id = "tech_halloumi",
        name = "Halloumi",
        rpCost = 25,
        parentId = "tech_mozzarella",
        gridX = 1400f,
        gridY = 50f,
        daysToComplete = 2,
        iconEmoji = "🧀"
    )
    val SALAD_CHEESE = TechTreeNode(
        id = "tech_salad_cheese",
        name = "Salad Cheese",
        rpCost = 30,
        parentId = "tech_halloumi",
        gridX = 1600f,
        gridY = 50f,
        daysToComplete = 2,
        iconEmoji = "🥗"
    )
    val AGED_HARD_CHEESE = TechTreeNode(
        id = "tech_cheese_aging", // Keep legacy ID
        name = "Aged Hard Cheese",
        rpCost = 35,
        parentId = "tech_salad_cheese",
        gridX = 1800f,
        gridY = 50f,
        daysToComplete = 3,
        iconEmoji = "🧀"
    )
    val BLUE_CHEESE = TechTreeNode(
        id = "tech_blue_cheese",
        name = "Blue Cheese",
        rpCost = 40,
        parentId = "tech_cheese_aging",
        gridX = 2000f,
        gridY = 50f,
        daysToComplete = 3,
        iconEmoji = "🦠"
    )
    val BRIE_CAMEMBERT = TechTreeNode(
        id = "tech_brie_camembert",
        name = "Brie & Camembert",
        rpCost = 45,
        parentId = "tech_blue_cheese",
        gridX = 2200f,
        gridY = 50f,
        daysToComplete = 3,
        iconEmoji = "🇫🇷"
    )

    // BRANCH C: Centrifugal Separation (Y = 150f)
    val SKIMMED_MILK_CREAM = TechTreeNode(
        id = "tech_fermentation", // Keeping legacy ID just in case
        name = "Skimmed Milk & Cream",
        rpCost = 15,
        parentId = "tech_raw_milk",
        gridX = 200f,
        gridY = 150f,
        daysToComplete = 2,
        iconEmoji = "🥛"
    )
    
    // Branch C1: Cream Line
    val SOUR_CREAM = TechTreeNode(
        id = "tech_sour_cream",
        name = "Sour Cream & Mascarpone",
        rpCost = 20,
        parentId = "tech_fermentation",
        gridX = 400f,
        gridY = 150f,
        daysToComplete = 1,
        iconEmoji = "🥣"
    )
    val WHIPPED_CREAM = TechTreeNode(
        id = "tech_whipped_cream",
        name = "Whipped Cream",
        rpCost = 25,
        parentId = "tech_sour_cream",
        gridX = 600f,
        gridY = 150f,
        daysToComplete = 1,
        iconEmoji = "🍦"
    )
    val BUTTER_BUTTERMILK = TechTreeNode(
        id = "tech_butter_buttermilk",
        name = "Butter & Buttermilk",
        rpCost = 30,
        parentId = "tech_whipped_cream",
        gridX = 800f,
        gridY = 150f,
        daysToComplete = 2,
        iconEmoji = "🧈"
    )
    val GHEE = TechTreeNode(
        id = "tech_ghee",
        name = "Ghee",
        rpCost = 35,
        parentId = "tech_butter_buttermilk",
        gridX = 1000f,
        gridY = 150f,
        daysToComplete = 2,
        iconEmoji = "🥫"
    )

    // Products Extension
    val ICE_CREAM = TechTreeNode(
        id = "tech_ice_cream", name = "Ice Cream", rpCost = 35,
        parentId = "tech_whipped_cream", gridX = 800f, gridY = 250f, daysToComplete = 2, iconEmoji = "🍨"
    )
    val GELATO = TechTreeNode(
        id = "tech_gelato", name = "Gelato", rpCost = 45,
        parentId = "tech_ice_cream", gridX = 1000f, gridY = 250f, daysToComplete = 3, iconEmoji = "🍧"
    )
    val BIO_SYNTH_DAIRY = TechTreeNode(
        id = "tech_bio_synth_dairy", name = "Bio-Synthesized Dairy", rpCost = 100,
        parentId = "tech_gelato", gridX = 1200f, gridY = 250f, daysToComplete = 5, iconEmoji = "🧫"
    )

    // Industry Tree
    val MANUAL_LOGISTICS = TechTreeNode(
        id = "tech_manual_logistics", name = "Manual Logistics", rpCost = 0,
        parentId = null, gridX = 0f, gridY = 0f, daysToComplete = 0, iconEmoji = "📦", category = ResearchCategory.INDUSTRY
    )
    val AUTO_MILKING = TechTreeNode(
        id = "tech_auto_milking", name = "Automated Milking Milieu", rpCost = 20,
        parentId = "tech_manual_logistics", gridX = 200f, gridY = -100f, daysToComplete = 2, iconEmoji = "🤖", category = ResearchCategory.INDUSTRY
    )
    val DRONE_DELIVERY = TechTreeNode(
        id = "tech_drone_delivery", name = "Drone Delivery Fleets", rpCost = 50,
        parentId = "tech_auto_milking", gridX = 400f, gridY = -100f, daysToComplete = 4, iconEmoji = "🚁", category = ResearchCategory.INDUSTRY
    )
    val BASIC_COLD_STORAGE = TechTreeNode(
        id = "tech_basic_cold_storage", name = "Basic Cold Storage", rpCost = 15,
        parentId = "tech_manual_logistics", gridX = 200f, gridY = 100f, daysToComplete = 2, iconEmoji = "🧊", category = ResearchCategory.INDUSTRY
    )
    val AI_TEMP_CONTROL = TechTreeNode(
        id = "tech_ai_temp_control", name = "AI-Driven Temp Control", rpCost = 45,
        parentId = "tech_basic_cold_storage", gridX = 400f, gridY = 100f, daysToComplete = 4, iconEmoji = "🌡️", category = ResearchCategory.INDUSTRY
    )
    val CONSTRUCTION_CREW_1 = TechTreeNode(
        id = "tech_construction_crew_1", name = "Dedicated Construction Crew", rpCost = 100,
        parentId = "tech_drone_delivery", gridX = 600f, gridY = -100f, daysToComplete = 5, iconEmoji = "👷", category = ResearchCategory.INDUSTRY
    )
    
    val INDUSTRIAL_THROUGHPUT_1 = TechTreeNode(
        id = "tech_industrial_throughput_1", name = "High-Volume Processing", rpCost = 50,
        parentId = "tech_manual_logistics", gridX = 200f, gridY = 300f, daysToComplete = 3, iconEmoji = "⚙️", category = ResearchCategory.INDUSTRY
    )
    val INDUSTRIAL_THROUGHPUT_2 = TechTreeNode(
        id = "tech_industrial_throughput_2", name = "Mega-Factory Architecture", rpCost = 150,
        parentId = "tech_industrial_throughput_1", gridX = 400f, gridY = 300f, daysToComplete = 5, iconEmoji = "🏭", category = ResearchCategory.INDUSTRY
    )
    val INDUSTRIAL_THROUGHPUT_3 = TechTreeNode(
        id = "tech_industrial_throughput_3", name = "Gigafactory Automation", rpCost = 350,
        parentId = "tech_industrial_throughput_2", gridX = 600f, gridY = 300f, daysToComplete = 7, iconEmoji = "🦾", category = ResearchCategory.INDUSTRY
    )

    // Company Tree
    val CORPORATE_INC = TechTreeNode(
        id = "tech_corporate_inc", name = "Corporate Incorporation", rpCost = 0,
        parentId = null, gridX = 0f, gridY = 0f, daysToComplete = 0, iconEmoji = "🏢", category = ResearchCategory.COMPANY
    )
    val LOBBYING = TechTreeNode(
        id = "tech_lobbying", name = "Lobbying", rpCost = 40,
        parentId = "tech_corporate_inc", gridX = 200f, gridY = -100f, daysToComplete = 3, iconEmoji = "🗣️", category = ResearchCategory.COMPANY
    )
    val SHELL_CORP = TechTreeNode(
        id = "tech_shell_corp", name = "Shell Corporations", rpCost = 80,
        parentId = "tech_lobbying", gridX = 400f, gridY = -100f, daysToComplete = 5, iconEmoji = "🐚", category = ResearchCategory.COMPANY
    )
    val ALGO_TRADING = TechTreeNode(
        id = "tech_algo_trading", name = "Algorithmic Trading", rpCost = 60,
        parentId = "tech_corporate_inc", gridX = 200f, gridY = 100f, daysToComplete = 4, iconEmoji = "📈", category = ResearchCategory.COMPANY
    )
    val HOSTILE_AI = TechTreeNode(
        id = "tech_hostile_ai", name = "Hostile Takeover AI", rpCost = 120,
        parentId = "tech_algo_trading", gridX = 400f, gridY = 100f, daysToComplete = 7, iconEmoji = "😈", category = ResearchCategory.COMPANY
    )
    val DEDICATED_LAB_1 = TechTreeNode(
        id = "tech_dedicated_lab_1", name = "Dedicated R&D Lab", rpCost = 150,
        parentId = "tech_hostile_ai", gridX = 600f, gridY = 100f, daysToComplete = 6, iconEmoji = "🔬", category = ResearchCategory.COMPANY
    )
    val CYBERSECURITY_1 = TechTreeNode(
        id = "tech_cybersecurity_1", name = "Basic Cybersecurity", rpCost = 100,
        parentId = "tech_corporate_inc", gridX = 200f, gridY = 200f, daysToComplete = 4, iconEmoji = "🛡️", category = ResearchCategory.COMPANY
    )
    val CYBERSECURITY_2 = TechTreeNode(
        id = "tech_cybersecurity_2", name = "Advanced Encryption", rpCost = 250,
        parentId = "tech_cybersecurity_1", gridX = 400f, gridY = 200f, daysToComplete = 8, iconEmoji = "🔐", category = ResearchCategory.COMPANY
    )

    val ALL_TECHS: List<TechTreeNode> = listOf(
        // Products
        RAW_MILK, PASTEURIZED_MILK, COFFEE_MILK, CONDENSED_MILK,
        YOGURT, KEFIR, CURDS_WHEY, RICOTTA, BRUNOST, WHEY_PROTEIN,
        QUARK, COTTAGE_CHEESE, MOZZARELLA, HALLOUMI, SALAD_CHEESE,
        AGED_HARD_CHEESE, BLUE_CHEESE, BRIE_CAMEMBERT,
        SKIMMED_MILK_CREAM, SOUR_CREAM, WHIPPED_CREAM, BUTTER_BUTTERMILK, GHEE,
        ICE_CREAM, GELATO, BIO_SYNTH_DAIRY,
        
        // Industry
        MANUAL_LOGISTICS, AUTO_MILKING, DRONE_DELIVERY, BASIC_COLD_STORAGE, AI_TEMP_CONTROL, CONSTRUCTION_CREW_1,
        INDUSTRIAL_THROUGHPUT_1, INDUSTRIAL_THROUGHPUT_2, INDUSTRIAL_THROUGHPUT_3,
        
        // Company
        CORPORATE_INC, LOBBYING, SHELL_CORP, ALGO_TRADING, HOSTILE_AI, DEDICATED_LAB_1, CYBERSECURITY_1, CYBERSECURITY_2
    )

    fun getById(id: String): TechTreeNode {
        return ALL_TECHS.firstOrNull { it.id == id }
            ?: throw NoSuchElementException("Technology $id not found in TechCatalog")
    }
}
