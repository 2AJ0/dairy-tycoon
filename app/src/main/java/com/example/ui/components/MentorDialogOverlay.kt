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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun MentorDialogOverlay(
    onDismiss: () -> Unit
) {
    val dialogueLines = listOf(
        "Greetings, CEO! I am Dr. A.W.E., Director of the newly constructed Advanced Research Lab.",
        "The standard tech tree? Child's play. We are about to bend reality, but it requires MASSIVE liquid cash and RP.",
        "Click the new EXPERIMENTAL tab in the Research Hub to behold the future of dairy automation. Don't be intimidated by the price tags!"
    )
    
    var currentLineIndex by remember { mutableStateOf(0) }
    var displayedText by remember { mutableStateOf("") }
    
    LaunchedEffect(currentLineIndex) {
        displayedText = ""
        val line = dialogueLines[currentLineIndex]
        for (i in line.indices) {
            delay(15) // Typewriter speed
            displayedText += line[i]
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable {
                if (displayedText.length < dialogueLines[currentLineIndex].length) {
                    // Skip animation
                    displayedText = dialogueLines[currentLineIndex]
                } else if (currentLineIndex < dialogueLines.size - 1) {
                    currentLineIndex++
                } else {
                    onDismiss()
                }
            },
        contentAlignment = Alignment.BottomStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // Mentor Portrait
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF222222))
                    .border(2.dp, Color(0xFF00FFCC), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("👩‍🔬", fontSize = 60.sp)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Dialogue Box
            Surface(
                modifier = Modifier.weight(1f).heightIn(min = 120.dp),
                shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp),
                color = Color(0xFF111111),
                border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF00FFCC))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Dr. A.W.E.", color = Color(0xFF00FFCC), fontWeight = FontWeight.Black, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(displayedText, color = Color.White, fontSize = 16.sp, lineHeight = 22.sp)
                    
                    Spacer(modifier = Modifier.weight(1f))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Text(if (displayedText.length < dialogueLines[currentLineIndex].length) "Skip ▸" else "Next ▸", color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
