package com.example.ui.screens

/**
 * ==========================================
 * AI / IDE AGENT INSTRUCTIONS:
 * ==========================================
 * This is a Jetpack Compose UI file for the Dairy Tycoon game.
 * 
 * CORE RULES FOR FIXING ERRORS:
 * 1. UNIDIRECTIONAL DATA FLOW: The UI strictly observes `GameState`. Do not introduce local state for global data.
 * 2. PRESERVE LOGIC: If you encounter unresolved references (e.g., missing imports or renamed variables), fix the import or typo. DO NOT rewrite the composable's structural layout or business logic.
 * 3. LAZYCOLUMNS: We heavily use `.filter` lists mapped to `items()`. Preserve the separation of visible/unlocked items and "Upcoming Milestones".
 * 4. MODIFIERS: Preserve `.testTag`, `.weight(1f)`, and `.clipToBounds()` layout constraints.
 */

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.GameViewModel
import com.example.data.SaveGameManager
import com.example.data.SaveSummary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MainMenuScreen(
    viewModel: GameViewModel,
    onStartGame: () -> Unit
) {
    val context = LocalContext.current
    val saveManager = remember { SaveGameManager(context) }
    var saveList by remember { mutableStateOf(saveManager.getAllSaveSummaries().sortedByDescending { it.timestamp }) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Dairy Tycoon", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.startNewGame()
                onStartGame()
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("NEW EMPIRE")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Save Slots", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        if (saveList.isEmpty()) {
            Text("No saved games found.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().weight(1f, fill = false)
            ) {
                items(saveList, key = { it.saveId }) { save ->
                    SaveSlotCard(
                        save = save,
                        onLoad = {
                            if (viewModel.loadGame(save.saveId)) {
                                onStartGame()
                            }
                        },
                        onDelete = {
                            viewModel.deleteSave(save.saveId)
                            saveList = saveManager.getAllSaveSummaries().sortedByDescending { it.timestamp }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SaveSlotCard(save: SaveSummary, onLoad: () -> Unit, onDelete: () -> Unit) {
    val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    val dateStr = sdf.format(Date(save.timestamp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(save.saveName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Day ${save.day} | ${save.gamePhase.name}", style = MaterialTheme.typography.bodySmall)
                Text("$${String.format("%.2f", save.cash)} | NW: $${String.format("%.2f", save.netWorth)}", style = MaterialTheme.typography.bodySmall)
                Text("Last saved: $dateStr", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row {
                IconButton(onClick = onLoad) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Load Save")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Save", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
