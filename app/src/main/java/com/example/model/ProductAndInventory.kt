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

import java.util.UUID

/**
 * Defines a product type in Dairy Tycoon.
 */
data class Product(
    val id: String,
    val name: String,
    val category: ProductCategory,
    val description: String,
    val basePrice: Double,
    val shelfLifeDays: Int,
    val tier: Int = 1,
    val emoji: String = "🥛"
)

enum class ProductCategory {
    RAW,
    PROCESSED,
    ARTISANAL,
    BYPRODUCT,
    CONSUMABLE
}

object ProductCatalog {
    val COW_FEED = Product(
        id = "cow_feed",
        name = "Premium Cow Feed",
        category = ProductCategory.CONSUMABLE,
        description = "High-nutrition feed required for pastures.",
        basePrice = 1.20,
        shelfLifeDays = 30,
        tier = 1,
        emoji = "🌾"
    )

    val GLASS_BOTTLES = Product(
        id = "glass_bottles",
        name = "Glass Bottles",
        category = ProductCategory.CONSUMABLE,
        description = "Packaging for pasteurized milk.",
        basePrice = 0.50,
        shelfLifeDays = 9999,
        tier = 1,
        emoji = "🍼"
    )

    val BIO_SYNTH_DAIRY = Product(
        id = "bio_synth_dairy",
        name = "Bio-Synthesized Dairy",
        category = ProductCategory.PROCESSED,
        description = "Lab-grown dairy alternative.",
        basePrice = 25.0,
        shelfLifeDays = 9999,
        tier = 5,
        emoji = "🧫"
    )

    val RAW_MILK = Product(
        id = "raw_milk",
        name = "Raw Milk",
        category = ProductCategory.RAW,
        description = "Fresh unpasteurized cow's milk. Highly perishable but versatile.",
        basePrice = 2.50,
        shelfLifeDays = 3,
        tier = 1,
        emoji = "🥛"
    )

    val PASTEURIZED_MILK = Product(
        id = "pasteurized_milk",
        name = "Pasteurized Milk",
        category = ProductCategory.PROCESSED,
        description = "Heat-treated milk for longer shelf life and safe consumer sale.",
        basePrice = 4.80,
        shelfLifeDays = 8,
        tier = 1,
        emoji = "🧃"
    )

    val CREAM = Product(
        id = "cream",
        name = "Heavy Cream",
        category = ProductCategory.PROCESSED,
        description = "Rich high-fat cream skimmed from top-tier milk.",
        basePrice = 7.50,
        shelfLifeDays = 6,
        tier = 2,
        emoji = "🍶"
    )

    val BUTTER = Product(
        id = "butter",
        name = "Artisan Butter",
        category = ProductCategory.ARTISANAL,
        description = "Churned rich butter.",
        basePrice = 12.00,
        shelfLifeDays = 14,
        tier = 2,
        emoji = "🧈"
    )

    val FRESH_CHEESE = Product(
        id = "fresh_cheese",
        name = "Fresh Cheese",
        category = ProductCategory.ARTISANAL,
        description = "Soft unaged cheese.",
        basePrice = 15.00,
        shelfLifeDays = 10,
        tier = 2,
        emoji = "🧀"
    )

    val AGED_CHEDDAR = Product(
        id = "aged_cheddar",
        name = "Aged Cheddar",
        category = ProductCategory.ARTISANAL,
        description = "Sharp aged cheddar block.",
        basePrice = 45.00,
        shelfLifeDays = 90,
        tier = 3,
        emoji = "🧀"
    )

    val SPOILED_MILK = Product(
        id = "spoiled_milk",
        name = "Spoiled Biomatter",
        category = ProductCategory.BYPRODUCT,
        description = "Rancid waste. Worthless unless converted to fertilizer.",
        basePrice = 0.05,
        shelfLifeDays = 5,
        tier = 1,
        emoji = "☣️"
    )

    val WHEY = Product(
        id = "whey",
        name = "Raw Whey",
        category = ProductCategory.BYPRODUCT,
        description = "Liquid by-product of cheese production.",
        basePrice = 1.00,
        shelfLifeDays = 4,
        tier = 1,
        emoji = "🪣"
    )

    val PROTEIN_ISOLATE = Product(
        id = "protein_isolate",
        name = "Whey Protein Isolate",
        category = ProductCategory.PROCESSED,
        description = "High-value fitness supplement powder extracted from raw whey.",
        basePrice = 35.00,
        shelfLifeDays = 365,
        tier = 3,
        emoji = "💪"
    )

    val SLUDGE = Product(
        id = "sludge",
        name = "Bio-Sludge",
        category = ProductCategory.BYPRODUCT,
        description = "Thick by-product from intense pasteurization and separation.",
        basePrice = 0.20,
        shelfLifeDays = 7,
        tier = 1,
        emoji = "🛢️"
    )

    val ANIMAL_FEED = Product(
        id = "animal_feed",
        name = "Processed Animal Feed",
        category = ProductCategory.PROCESSED,
        description = "Nutritious feed pellets derived from bio-sludge and waste.",
        basePrice = 5.00,
        shelfLifeDays = 180,
        tier = 2,
        emoji = "🌾"
    )

    val ALL_PRODUCTS = listOf(
        COW_FEED,
        GLASS_BOTTLES,
        BIO_SYNTH_DAIRY,
        RAW_MILK,
        PASTEURIZED_MILK,
        CREAM,
        BUTTER,
        FRESH_CHEESE,
        AGED_CHEDDAR,
        SPOILED_MILK,
        WHEY,
        PROTEIN_ISOLATE,
        SLUDGE,
        ANIMAL_FEED
    )

    fun getById(id: String): Product {
        return ALL_PRODUCTS.find { it.id == id } ?: RAW_MILK
    }
}

/**
 * Represents a single batch of goods inside the First-In, First-Out (FIFO) queue.
 */
data class InventoryBatch(
    val batchId: String = UUID.randomUUID().toString(),
    val itemId: String,
    val itemName: String,
    val quantity: Int,
    val quality: Double, // 1.0 (Standard) to 3.0 (Grand Champion)
    val maxShelfLife: Int,
    val dayProduced: Int = 1,
    val daysUntilSpoiled: Float = maxShelfLife.toFloat(),
    val isProtectedToday: Boolean = false,
    val isInColdStorage: Boolean = false
) {
    val isSpoiled: Boolean get() = daysUntilSpoiled <= 0f || itemId == ProductCatalog.SPOILED_MILK.id
    val daysRemaining: Int get() = daysUntilSpoiled.toInt().coerceAtLeast(0)
    val daysOld: Int get() = (maxShelfLife - daysRemaining).coerceAtLeast(0)
    val freshnessFraction: Float
        get() = if (isSpoiled) 0f else if (maxShelfLife <= 0) 0f else (daysUntilSpoiled / maxShelfLife.toFloat()).coerceIn(0f, 1f)
}
