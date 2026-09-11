package com.example.ui.components

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

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameState
import com.example.model.StoryEvent
import com.example.model.StoryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxOverlay(
    gameState: GameState,
    onDismiss: () -> Unit,
    onMarkAsRead: (String) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        scrimColor = Color.Black.copy(alpha = 0.5f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Priority Inbox", fontSize = 24.sp, fontWeight = FontWeight.Black)
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close Inbox")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (gameState.pendingStoryEvents.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("No new messages.", color = Color.Gray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(gameState.pendingStoryEvents) { event ->
                        StoryMessageCard(event = event, onMarkAsRead = { onMarkAsRead(event.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun StoryMessageCard(event: StoryEvent, onMarkAsRead: () -> Unit) {
    val bgColor: Color
    val textColor: Color
    val borderColor: Color
    val fontFamily: FontFamily
    
    when (event.phaseTheme) {
        StoryTheme.FARMING -> {
            bgColor = Color(0xFFFDF5E6) // Old Lace / Paper
            textColor = Color(0xFF5C4033) // Dark Brown
            borderColor = Color(0xFFD2B48C)
            fontFamily = FontFamily.Serif
        }
        StoryTheme.CORPORATE -> {
            bgColor = Color(0xFF0F0F0F) // Dark
            textColor = Color(0xFF00FF00) // Terminal Green
            borderColor = Color(0xFF00FF00)
            fontFamily = FontFamily.Monospace
        }
        StoryTheme.EXPERIMENTAL -> {
            bgColor = Color(0xFF050510) // Very Dark Blue
            textColor = Color(0xFF00FFFF) // Cyan
            borderColor = Color(0xFF00FFFF)
            fontFamily = FontFamily.Monospace
        }
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, borderColor, RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "FROM: ${event.sender}",
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamily
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = event.title,
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                fontFamily = fontFamily
            )
            HorizontalDivider(color = borderColor.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = event.body,
                color = textColor,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontFamily = fontFamily
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(
                    onClick = onMarkAsRead,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = borderColor.copy(alpha = 0.2f),
                        contentColor = textColor
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
                ) {
                    Text("Archive Message", fontFamily = fontFamily, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
