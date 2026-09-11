package com.example.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.model.GameState
import com.example.model.GamePhase
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class SaveGameManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson: Gson = GsonBuilder().serializeSpecialFloatingPointValues().create()

    companion object {
        private const val TAG = "SaveGameManager"
        private const val PREFS_NAME = "dairy_tycoon_multi_saves"
        private const val KEY_ACTIVE_SAVE = "active_save_id"
    }

    private fun getSaveIds(): Set<String> {
        return prefs.getStringSet("save_ids", emptySet()) ?: emptySet()
    }

    fun getAllSaveSummaries(): List<SaveSummary> {
        return getSaveIds().mapNotNull { getSaveSummary(it) }
    }

    fun hasSavedGame(): Boolean {
        return getSaveIds().isNotEmpty()
    }

    fun saveGame(gameState: GameState): Boolean {
        return try {
            val saveId = gameState.saveId
            val json = gson.toJson(gameState)
            
            val ids = getSaveIds().toMutableSet()
            ids.add(saveId)
            
            prefs.edit()
                .putStringSet("save_ids", ids)
                .putString("active_save_id", saveId)
                .putString("save_data_$saveId", json)
                .putLong("save_time_$saveId", System.currentTimeMillis())
                .apply()
                
            Log.d(TAG, "Saved game $saveId")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save game", e)
            false
        }
    }

    fun loadGame(saveId: String? = null): GameState? {
        val targetId = saveId ?: prefs.getString(KEY_ACTIVE_SAVE, null) ?: return null
        val json = prefs.getString("save_data_$targetId", null) ?: return null
        return try {
            gson.fromJson(json, GameState::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load game $targetId", e)
            null
        }
    }

    fun getSaveSummary(saveId: String? = null): SaveSummary? {
        val targetId = saveId ?: prefs.getString(KEY_ACTIVE_SAVE, null) ?: return null
        val json = prefs.getString("save_data_$targetId", null) ?: return null
        
        return try {
            val state = gson.fromJson(json, GameState::class.java)
            SaveSummary(
                saveId = state.saveId,
                saveName = state.saveName,
                day = state.day,
                cash = state.cash,
                netWorth = state.netWorth,
                timestamp = prefs.getLong("save_time_$targetId", System.currentTimeMillis()),
                gamePhase = state.gamePhase
            )
        } catch (e: Exception) {
            null
        }
    }

    fun clearSaveGame(saveId: String) {
        val ids = getSaveIds().toMutableSet()
        ids.remove(saveId)
        prefs.edit()
            .putStringSet("save_ids", ids)
            .remove("save_data_$saveId")
            .remove("save_time_$saveId")
            .apply()
            
        if (prefs.getString(KEY_ACTIVE_SAVE, null) == saveId) {
            prefs.edit().remove(KEY_ACTIVE_SAVE).apply()
        }
    }
}

data class SaveSummary(
    val saveId: String,
    val saveName: String,
    val day: Int,
    val cash: Double,
    val netWorth: Double,
    val timestamp: Long,
    val gamePhase: GamePhase
)
