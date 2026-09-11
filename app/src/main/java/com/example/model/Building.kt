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
 * Type of building in the farm/corporate manufacturing complex.
 */

enum class PerkEffect {
    THROUGHPUT_MULTIPLIER,
    QUALITY_MULTIPLIER,
    SPOILAGE_CONVERSION
}

data class FacilityPerk(
    val id: String,
    val title: String,
    val cost: Double,
    val requiredFacilityLevel: Int,
    val mutuallyExclusiveWith: List<String>,
    val effectType: PerkEffect,
    val effectValue: Float,
    val branch: String
)

object FacilityPerkCatalog {
    val PERKS = listOf(
        FacilityPerk("perk_vol_1", "Basic Automation", 5000.0, 1, listOf("perk_qual_1", "perk_eco_1"), PerkEffect.THROUGHPUT_MULTIPLIER, 1.25f, "VOLUME"),
        FacilityPerk("perk_vol_2", "Robotic Assembly", 15000.0, 2, emptyList(), PerkEffect.THROUGHPUT_MULTIPLIER, 1.50f, "VOLUME"),
        FacilityPerk("perk_qual_1", "Artisanal Care", 5000.0, 1, listOf("perk_vol_1", "perk_eco_1"), PerkEffect.QUALITY_MULTIPLIER, 1.25f, "QUALITY"),
        FacilityPerk("perk_qual_2", "Mastercrafted", 15000.0, 2, emptyList(), PerkEffect.QUALITY_MULTIPLIER, 1.50f, "QUALITY"),
        FacilityPerk("perk_eco_1", "Waste Recycler", 5000.0, 1, listOf("perk_vol_1", "perk_qual_1"), PerkEffect.SPOILAGE_CONVERSION, 0.50f, "ECO"),
        FacilityPerk("perk_eco_2", "Zero-Waste Loop", 15000.0, 2, emptyList(), PerkEffect.SPOILAGE_CONVERSION, 1.0f, "ECO")
    )
    
    fun getById(id: String) = PERKS.first { it.id == id }
}

enum class BuildingType {
    PASTURE,          // Produces raw milk
    PASTEURIZER,
    ADVANCED_LAB,      // Converts Raw Milk -> Pasteurized Milk
    CREAMERY,         // Converts Raw Milk -> Cream or Butter
    CHEESE_CAVE,      // Converts Raw Milk -> Fresh Cheese or Aged Cheddar
    RD_LAB,           // Generates passive Research Points (RP)
    PROCESSING_PLANT, // Multi-purpose industrial plant
    PROTEIN_EXTRACTOR,
    FEED_MILL,
    COLD_STORAGE
}

/**
 * Defines recipe and conversion ratio for processing facilities.
 */
data class ProcessingRecipe(
    val inputItemId: String,
    val inputQuantity: Int,
    val outputItemId: String,
    val outputQuantity: Int,
    val qualityMultiplier: Double = 1.15,
    val extraOperatingCostPerUnit: Double = 0.0,
    val byProducts: Map<String, Int> = emptyMap()
) {
    val name: String get() = "${ProductCatalog.getById(outputItemId).name} Recipe"
}

/**
 * Building representation on the player's estate.
 */
data class Building(
    val id: String,
    val type: BuildingType,
    val name: String,
    val description: String,
    val level: Int = 1,
    val isConstructed: Boolean = false,
    val baseCost: Double,
    val baseDailyMaintenance: Double,
    val dailyRawProductionQty: Int = 0, // for Pastures
    val maxProcessingCapacity: Int = 0, // units per day
    val dailyResearchPoints: Int = 0,   // for R&D
    val activeRecipe: ProcessingRecipe? = null,
    val availableRecipes: List<ProcessingRecipe> = emptyList(),
    val isOperational: Boolean = true,
    val iconEmoji: String = "🏠",
    val requiredTechId: String? = null,
    val maxLevel: Int = 5,
    val daysToComplete: Int = 1,
    val landRequired: Int = 1,
    val upgradeCostMultiplier: Double = 1.5,
    val sabotagedDaysRemaining: Int = 0,
    val allocationPercentage: Int = 100,
    val capacityTier: Int = 1,
    val isSpunOff: Boolean = false,
    val activeAnomalyId: String? = null,
    val unlockedPerks: List<String> = emptyList()
) {
    val currentCost: Double get() = if (!isConstructed) baseCost else baseCost * Math.pow(1.65, level.toDouble())
    val upgradeCost: Double get() = baseCost * Math.pow(1.65, level.toDouble())
    val capacityUpgradeCost: Double get() = baseCost * Math.pow(2.5, capacityTier.toDouble())
    val baseMaintenance: Double get() = baseDailyMaintenance
    val currentMaintenance: Double get() = if (isConstructed && isOperational) baseDailyMaintenance * (1.0 + (level - 1) * 0.45) else 0.0
    val currentDailyRawProduction: Int get() = if (isConstructed && isOperational) dailyRawProductionQty * level else 0
    
    val currentProcessingCapacity: Int get() = if (isConstructed && isOperational) {
        when(capacityTier) {
            1 -> maxProcessingCapacity
            2 -> maxProcessingCapacity * 5
            3 -> maxProcessingCapacity * 20
            4 -> maxProcessingCapacity * 50
            else -> maxProcessingCapacity * 100
        }
    } else 0
    
    val currentResearchPoints: Int get() = if (isConstructed && isOperational) dailyResearchPoints * level else 0
    val upgradeDaysToComplete: Int get() = (level).coerceAtLeast(1)
}

object DefaultBuildings {
    fun getInitialBuildings(): List<Building> {
        return listOf(
            Building(
                id = "pasture_1",
                type = BuildingType.PASTURE,
                name = "Holsteins Pasture",
                description = "Lush green meadow with dairy cattle grazing happily. Generates fresh raw milk daily.",
                level = 1,
                isConstructed = true,
                baseCost = 500.0,
                baseDailyMaintenance = 20.0,
                dailyRawProductionQty = 15,
                iconEmoji = "🐄",
                daysToComplete = 1,
                landRequired = 1
            ),
            Building(
                id = "pasteurizer_1",
                type = BuildingType.PASTEURIZER,
                name = "Flash Pasteurizer Unit",
                description = "High-temperature short-time heat exchanger. Extends raw milk shelf life.",
                level = 1,
                isConstructed = false,
                baseCost = 650.0,
                baseDailyMaintenance = 25.0,
                maxProcessingCapacity = 15,
                availableRecipes = listOf(
                    ProcessingRecipe(
                        inputItemId = ProductCatalog.RAW_MILK.id,
                        inputQuantity = 1,
                        outputItemId = ProductCatalog.PASTEURIZED_MILK.id,
                        outputQuantity = 1,
                        qualityMultiplier = 1.10,
                        extraOperatingCostPerUnit = 0.30,
                        byProducts = mapOf(ProductCatalog.SLUDGE.id to 1)
                    )
                ),
                activeRecipe = ProcessingRecipe(
                    inputItemId = ProductCatalog.RAW_MILK.id,
                    inputQuantity = 1,
                    outputItemId = ProductCatalog.PASTEURIZED_MILK.id,
                    outputQuantity = 1,
                    qualityMultiplier = 1.10,
                    extraOperatingCostPerUnit = 0.30,
                    byProducts = mapOf(ProductCatalog.SLUDGE.id to 1)
                ),
                iconEmoji = "🥛",
                requiredTechId = "tech_pasteurization",
                daysToComplete = 2,
                landRequired = 1
            ),
            Building(
                id = "creamery_1",
                type = BuildingType.CREAMERY,
                name = "Heritage Creamery",
                description = "Centrifugal separators and batch churns to craft artisanal cream and golden butter.",
                level = 1,
                isConstructed = false,
                baseCost = 1200.0,
                baseDailyMaintenance = 40.0,
                maxProcessingCapacity = 12,
                availableRecipes = listOf(
                    ProcessingRecipe(
                        inputItemId = ProductCatalog.RAW_MILK.id,
                        inputQuantity = 2,
                        outputItemId = ProductCatalog.CREAM.id,
                        outputQuantity = 1,
                        qualityMultiplier = 1.25,
                        extraOperatingCostPerUnit = 0.80
                    ),
                    ProcessingRecipe(
                        inputItemId = ProductCatalog.RAW_MILK.id,
                        inputQuantity = 3,
                        outputItemId = ProductCatalog.BUTTER.id,
                        outputQuantity = 1,
                        qualityMultiplier = 1.35,
                        extraOperatingCostPerUnit = 1.20
                    )
                ),
                activeRecipe = ProcessingRecipe(
                    inputItemId = ProductCatalog.RAW_MILK.id,
                    inputQuantity = 3,
                    outputItemId = ProductCatalog.BUTTER.id,
                    outputQuantity = 1,
                    qualityMultiplier = 1.35,
                    extraOperatingCostPerUnit = 1.20
                ),
                iconEmoji = "🧈",
                requiredTechId = "tech_fermentation",
                daysToComplete = 3,
                landRequired = 1
            ),
            Building(
                id = "cheese_cave_1",
                type = BuildingType.CHEESE_CAVE,
                name = "Limestone Cheese Cave",
                description = "Humidity-controlled ripening cellars for molding fresh curds into premium aged wheels.",
                level = 1,
                isConstructed = false,
                baseCost = 2500.0,
                baseDailyMaintenance = 65.0,
                maxProcessingCapacity = 10,
                availableRecipes = listOf(
                    ProcessingRecipe(
                        inputItemId = ProductCatalog.RAW_MILK.id,
                        inputQuantity = 3,
                        outputItemId = ProductCatalog.FRESH_CHEESE.id,
                        outputQuantity = 1,
                        qualityMultiplier = 1.30,
                        extraOperatingCostPerUnit = 1.50,
                        byProducts = mapOf(ProductCatalog.WHEY.id to 2)
                    ),
                    ProcessingRecipe(
                        inputItemId = ProductCatalog.RAW_MILK.id,
                        inputQuantity = 5,
                        outputItemId = ProductCatalog.AGED_CHEDDAR.id,
                        outputQuantity = 1,
                        qualityMultiplier = 1.50,
                        extraOperatingCostPerUnit = 3.00,
                        byProducts = mapOf(ProductCatalog.WHEY.id to 4)
                    )
                ),
                activeRecipe = ProcessingRecipe(
                    inputItemId = ProductCatalog.RAW_MILK.id,
                    inputQuantity = 5,
                    outputItemId = ProductCatalog.AGED_CHEDDAR.id,
                    outputQuantity = 1,
                    qualityMultiplier = 1.50,
                    extraOperatingCostPerUnit = 3.00,
                    byProducts = mapOf(ProductCatalog.WHEY.id to 4)
                ),
                iconEmoji = "🧀",
                requiredTechId = "tech_cheese_aging",
                daysToComplete = 4,
                landRequired = 1
            ),
            Building(
                id = "protein_extractor_1",
                type = BuildingType.PROTEIN_EXTRACTOR,
                name = "Protein Isolate Extractor",
                description = "Advanced lab-grade centrifuges that refine raw whey into high-value protein isolate.",
                level = 1,
                isConstructed = false,
                baseCost = 3500.0,
                baseDailyMaintenance = 90.0,
                maxProcessingCapacity = 8,
                availableRecipes = listOf(
                    ProcessingRecipe(
                        inputItemId = ProductCatalog.WHEY.id,
                        inputQuantity = 4,
                        outputItemId = ProductCatalog.PROTEIN_ISOLATE.id,
                        outputQuantity = 1,
                        qualityMultiplier = 1.40,
                        extraOperatingCostPerUnit = 2.00
                    )
                ),
                activeRecipe = ProcessingRecipe(
                    inputItemId = ProductCatalog.WHEY.id,
                    inputQuantity = 4,
                    outputItemId = ProductCatalog.PROTEIN_ISOLATE.id,
                    outputQuantity = 1,
                    qualityMultiplier = 1.40,
                    extraOperatingCostPerUnit = 2.00
                ),
                iconEmoji = "💪",
                requiredTechId = "tech_industrial_throughput_2",
                daysToComplete = 5,
                landRequired = 1
            ),
            Building(
                id = "feed_mill_1",
                type = BuildingType.FEED_MILL,
                name = "Animal Feed Mill",
                description = "Converts pasteurization sludge and waste into nutritional feed pellets.",
                level = 1,
                isConstructed = false,
                baseCost = 2000.0,
                baseDailyMaintenance = 40.0,
                maxProcessingCapacity = 20,
                availableRecipes = listOf(
                    ProcessingRecipe(
                        inputItemId = ProductCatalog.SLUDGE.id,
                        inputQuantity = 2,
                        outputItemId = ProductCatalog.ANIMAL_FEED.id,
                        outputQuantity = 1,
                        qualityMultiplier = 1.10,
                        extraOperatingCostPerUnit = 0.50
                    )
                ),
                activeRecipe = ProcessingRecipe(
                    inputItemId = ProductCatalog.SLUDGE.id,
                    inputQuantity = 2,
                    outputItemId = ProductCatalog.ANIMAL_FEED.id,
                    outputQuantity = 1,
                    qualityMultiplier = 1.10,
                    extraOperatingCostPerUnit = 0.50
                ),
                iconEmoji = "🌾",
                requiredTechId = "tech_industrial_throughput_1",
                daysToComplete = 3,
                landRequired = 1
            ),
            Building(
                id = "rd_lab_1",
                type = BuildingType.RD_LAB,
                name = "Corporate R&D Lab",
                description = "Biochemistry lab staffed by nutritionists and food scientists to generate Research Points.",
                level = 1,
                isConstructed = false,
                baseCost = 1800.0,
                baseDailyMaintenance = 50.0,
                dailyResearchPoints = 8,
                iconEmoji = "🔬",
                daysToComplete = 2,
                landRequired = 1
            ),
            Building(
                id = "cold_storage_1",
                type = BuildingType.COLD_STORAGE,
                name = "Cold Storage Warehouse",
                description = "Extends the shelf life of perishable inventory by maintaining optimal climate conditions.",
                level = 1,
                isConstructed = false,
                baseCost = 1500.0,
                baseDailyMaintenance = 30.0,
                iconEmoji = "🧊",
                daysToComplete = 2,
                landRequired = 1,
                requiredTechId = "tech_basic_cold_storage"
            ),
            Building(
                id = "adv_lab_1",
                type = BuildingType.ADVANCED_LAB,
                name = "Advanced Research Lab",
                description = "Classified underground bunker generating experimental tech paradigms. Unlocks the Experimental Research Branch.",
                level = 1,
                isConstructed = false,
                baseCost = 150000.0,
                baseDailyMaintenance = 1500.0,
                dailyResearchPoints = 50,
                iconEmoji = "☢️",
                daysToComplete = 5,
                landRequired = 1,
                requiredTechId = "tech_singularity"
            )
        )
    }
}
