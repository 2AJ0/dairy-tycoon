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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.model.EndgameChoice
import com.example.model.GameState
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.DairyEmeraldDark
import com.example.ui.theme.DairyEmeraldPrimary
import com.example.ui.theme.DairyGold
import com.example.ui.theme.DairyGoldDark

@Composable
fun EndgameDialog(
    gameState: GameState,
    onChooseOption: (EndgameChoice) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedChoice by remember { mutableStateOf<EndgameChoice?>(null) }

    AlertDialog(
        onDismissRequest = { /* Modal interrupt: requires explicit decision */ },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        ),
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .testTag("endgame_dialog"),
        title = null,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dramatic Header
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(listOf(DairyGold.copy(alpha = 0.4f), DairyGoldDark.copy(alpha = 0.1f)))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👑", fontSize = 42.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = DairyGold.copy(alpha = 0.18f),
                    border = BorderStroke(1.dp, DairyGold)
                ) {
                    Text(
                        text = "THE FINAL ASCENSION • GLOBAL MONOPOLY",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp,
                        color = DairyGoldDark,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Barnaby (Old Herdsmaster)",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "A Final Audience in the Pasture",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Barnaby's Emotional Monologue Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("👴🌾", fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Barnaby's Soliloquy",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = DairyEmeraldPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "“Look at what you’ve built, son. MooCorp, Global Whey, LactoDynasty, Alpine Peak... every single one of them kneels before your balance sheet. You own the feed, the barns, the trucks, and every single drop of milk on this continent.\n\n" +
                                    "I remember when you had two scrawny heifers and a bucket full of dreams. Now you have boardroom lawyers, Wall Street ticker tape, and total dominion over the breakfast table.\n\n" +
                                    "The board of directors is waiting outside. The ink is dry on the master consolidation charter. The only question left is: What kind of ruler will you be?”",
                            style = MaterialTheme.typography.bodyMedium,
                            fontStyle = FontStyle.Italic,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "CHOOSE THE FATE OF THE GLOBAL DAIRY ORDER",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                // OPTION A: Subsidize for the People (Golden Pitchfork)
                val isOptionASelected = selectedChoice == EndgameChoice.SUBSIDIZE_FOR_THE_PEOPLE
                OutlinedCard(
                    onClick = { selectedChoice = EndgameChoice.SUBSIDIZE_FOR_THE_PEOPLE },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("endgame_choice_subsidize"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = if (isOptionASelected) DairyEmeraldDark.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(
                        if (isOptionASelected) 2.dp else 1.dp,
                        if (isOptionASelected) DairyEmeraldPrimary else MaterialTheme.colorScheme.outlineVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🌾", fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = EndgameChoice.SUBSIDIZE_FOR_THE_PEOPLE.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = DairyEmeraldPrimary
                                    )
                                    Text(
                                        text = "Trophy: ${EndgameChoice.SUBSIDIZE_FOR_THE_PEOPLE.trophyName}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = DairyGold
                                    )
                                }
                            }
                            if (isOptionASelected) {
                                Text("✓ SELECTED", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = DairyEmeraldPrimary)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Slash all consumer dairy prices by 80% to eradicate hunger. Convert your corporate empire into an agrarian commonwealth of boundless abundance.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DairyEmeraldDark.copy(alpha = 0.3f),
                            border = BorderStroke(1.dp, DairyEmeraldPrimary.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Spa, contentDescription = null, tint = DairyEmeraldPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Permanent -80% spot prices • +100 Max Reputation • Infinite Benevolent Sandbox",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color = DairyEmeraldPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // OPTION B: Maximize Shareholder Value (Diamond Cowbell)
                val isOptionBSelected = selectedChoice == EndgameChoice.MAXIMIZE_SHAREHOLDER_VALUE
                OutlinedCard(
                    onClick = { selectedChoice = EndgameChoice.MAXIMIZE_SHAREHOLDER_VALUE },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("endgame_choice_monopoly"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = if (isOptionBSelected) DairyGoldDark.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(
                        if (isOptionBSelected) 2.dp else 1.dp,
                        if (isOptionBSelected) DairyGold else MaterialTheme.colorScheme.outlineVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("💎", fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = EndgameChoice.MAXIMIZE_SHAREHOLDER_VALUE.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = DairyGold
                                    )
                                    Text(
                                        text = "Trophy: ${EndgameChoice.MAXIMIZE_SHAREHOLDER_VALUE.trophyName}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = DairyGold
                                    )
                                }
                            }
                            if (isOptionBSelected) {
                                Text("✓ SELECTED", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = DairyGold)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Unleash unbridled monopoly pricing power. Increase product spot prices by +300% and extract maximum EBITDA from every carton sold worldwide.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DairyGoldDark.copy(alpha = 0.3f),
                            border = BorderStroke(1.dp, DairyGold.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = DairyGold, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Permanent +300% spot prices • Infinite Hyper-Capitalist Monopoly Sandbox",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color = DairyGold,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Confirm Action Button
                Button(
                    onClick = {
                        val choice = selectedChoice ?: return@Button
                        onChooseOption(choice)
                    },
                    enabled = selectedChoice != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("endgame_confirm_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedChoice == EndgameChoice.SUBSIDIZE_FOR_THE_PEOPLE) DairyEmeraldPrimary else DairyGoldDark
                    )
                ) {
                    Text(
                        text = if (selectedChoice == null) "Select a Moral Path to Proceed" else "Seal the Charter & Continue Empire",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        },
        confirmButton = {}
    )
}
