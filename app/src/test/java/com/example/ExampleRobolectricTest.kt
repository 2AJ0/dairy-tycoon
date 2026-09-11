package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.GameState
import com.example.model.InventoryBatch
import com.example.model.ProductCatalog
import com.example.viewmodel.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Dairy Tycoon", appName)
  }

  @Test
  fun `strict FIFO selling consumes oldest batch first`() {
    val viewModel = GameViewModel()
    // Initial inventory has 1 batch of 25 Raw Milk (Day 1)
    assertEquals(25, viewModel.gameState.value.inventory.first().quantity)

    // Add a newer second batch of Raw Milk
    viewModel.workManualLabor() // Adds 2 units of Raw Milk as a newer batch

    val initialBatches = viewModel.gameState.value.inventory.filter { it.itemId == ProductCatalog.RAW_MILK.id }
    assertEquals(2, initialBatches.size)
    val oldestBatchId = initialBatches[0].batchId

    // Sell 10 units via FIFO
    val startingCash = viewModel.gameState.value.cash
    viewModel.sellProduct(ProductCatalog.RAW_MILK.id, 10)

    val updatedBatches = viewModel.gameState.value.inventory.filter { it.itemId == ProductCatalog.RAW_MILK.id }
    // Oldest batch should now have 15 units (25 - 10)
    assertEquals(15, updatedBatches.first { it.batchId == oldestBatchId }.quantity)
    // Total remaining should be 17 (15 + 2)
    assertEquals(17, updatedBatches.sumOf { it.quantity })
    assertTrue(viewModel.gameState.value.cash > startingCash)
  }

  @Test
  fun `dumping spoiled goods clears ruined inventory batches`() {
    val viewModel = GameViewModel()
    // Advance days to trigger aging and spoilage
    repeat(4) {
      viewModel.endDay()
    }

    val state = viewModel.gameState.value
    val hasSpoiled = state.inventory.any { it.isSpoiled || it.itemId == ProductCatalog.SPOILED_MILK.id }
    assertTrue(hasSpoiled)

    viewModel.discardSpoiledGoods()

    val afterDump = viewModel.gameState.value
    val spoiledRemaining = afterDump.inventory.any { it.isSpoiled || it.itemId == ProductCatalog.SPOILED_MILK.id }
    assertFalse(spoiledRemaining)
  }
}

