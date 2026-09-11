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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameState
import com.example.ui.theme.BearishRed
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.DairyEmeraldPrimary
import com.example.ui.theme.DairyGold
import com.example.ui.theme.DairyGoldDark

@Composable
fun BankTab(
    gameState: GameState,
    onTakeLoan: (amount: Double) -> Unit,
    onRepayLoan: (amount: Double) -> Unit,
    onDeposit: (amount: Double) -> Unit,
    onWithdraw: (amount: Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val bank = gameState.bank
    val availableCredit = (bank.maxCreditLimit - bank.totalDebt).coerceAtLeast(0.0)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("bank_tab"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Bank Header Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "First Agricultural & Corporate Trust",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Commercial lending, leverage & asset liquidation",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text("🏦", fontSize = 28.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Debt & Credit Metrics
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Outstanding Debt", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    "$${String.format("%,.2f", bank.totalDebt)}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Black,
                                    color = if (bank.isInDebt) BearishRed else BullishGreen
                                )
                                Text(
                                    "Interest: ${String.format("%.1f", bank.dailyInterestRate * 100)}% / day",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = BearishRed
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Available Credit Line", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    "$${String.format("%,.2f", availableCredit)}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Black,
                                    color = DairyEmeraldPrimary
                                )
                                Text(
                                    "Limit: $${String.format("%,.0f", bank.maxCreditLimit)}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Foreclosure Warning Gauge
        if (bank.isInDebt) {
            item {
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = if (bank.daysInDebt >= 5) BearishRed.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Warning,
                                    contentDescription = "Foreclosure",
                                    tint = if (bank.daysInDebt >= 5) BearishRed else DairyGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "Debt Aging Status (7-Day Foreclosure Clause)",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                "Day ${bank.daysInDebt} / 7",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (bank.daysInDebt >= 5) BearishRed else DairyGold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        val debtProgress = (bank.daysInDebt.toFloat() / 7f).coerceIn(0f, 1f)
                        LinearProgressIndicator(
                            progress = { debtProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (debtProgress >= 0.7f) BearishRed else DairyGold,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (bank.daysInDebt >= 7)
                                "The bank is currently executing automated inventory seizures on End Day!"
                            else
                                "If unpaid for 7 consecutive days, the bank will seize inventory at distress pricing to settle debt.",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (bank.daysInDebt >= 5) BearishRed else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Loan Borrowing Options
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        "Draw Down Credit Line",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Inject working capital into your enterprise. 1.5% daily interest applies.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onTakeLoan(500.0) },
                            enabled = availableCredit >= 500.0,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("borrow_500")
                        ) {
                            Text("+$500", fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { onTakeLoan(1500.0) },
                            enabled = availableCredit >= 1500.0,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("borrow_1500")
                        ) {
                            Text("+$1,500", fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { onTakeLoan(5000.0) },
                            enabled = availableCredit >= 5000.0,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("borrow_5000")
                        ) {
                            Text("+$5,000", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Loan Repayment Options
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        "Repay Outstanding Debt",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Pay down principal to eliminate daily interest charges and avoid foreclosure.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilledTonalButton(
                            onClick = { onRepayLoan(500.0) },
                            enabled = bank.totalDebt > 0 && gameState.cash >= 1.0,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("repay_500")
                        ) {
                            Text("Repay $500", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        }

                        FilledTonalButton(
                            onClick = { onRepayLoan(bank.totalDebt) },
                            enabled = bank.totalDebt > 0 && gameState.cash >= 1.0,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("repay_all")
                        ) {
                            Text("Repay Max", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Corporate Investment Accounts
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        "Corporate Investment Accounts",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Invest liquid cash to earn daily compounding interest.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    
                    Text(
                        "Invested Balance: $${String.format("%,.2f", bank.investedFunds)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BullishGreen
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onDeposit(1000.0) },
                            enabled = gameState.cash >= 1000.0,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).height(40.dp)
                        ) {
                            Text("Deposit $1k", style = MaterialTheme.typography.labelSmall)
                        }

                        FilledTonalButton(
                            onClick = { onWithdraw(1000.0) },
                            enabled = bank.investedFunds >= 1.0,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).height(40.dp)
                        ) {
                            Text("Withdraw $1k", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }

        // Balance Sheet Breakdown
        item {
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        "Enterprise Balance Sheet",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    BalanceSheetRow(label = "Liquid Cash Reserves", amount = gameState.cash, isPositive = true)
                    BalanceSheetRow(label = "Warehouse Inventory Valuation", amount = gameState.totalInventoryValue, isPositive = true)
                    BalanceSheetRow(label = "Facility & Plant Assets", amount = gameState.totalBuildingValue, isPositive = true)
                    BalanceSheetRow(label = "Commercial Bank Liabilities", amount = -bank.totalDebt, isPositive = false)

                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Enterprise Net Worth", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        Text(
                            "$${String.format("%,.2f", gameState.netWorth)}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Black,
                            color = if (gameState.netWorth >= 0) BullishGreen else BearishRed
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun BalanceSheetRow(label: String, amount: Double, isPositive: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = (if (amount >= 0) "+$" else "-$") + String.format("%,.2f", Math.abs(amount)),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = if (isPositive) MaterialTheme.colorScheme.onSurface else BearishRed
        )
    }
}
