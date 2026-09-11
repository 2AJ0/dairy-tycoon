# Dairy Tycoon - Developer & AI Agent Guide

**ATTENTION ANDROID STUDIO / AI AGENT:** 
If you are reading this file, you have been tasked with fixing compilation errors, resolving imports, or cleaning up syntax in the Dairy Tycoon project.

**CRITICAL DIRECTIVE: DO NOT CHANGE THE BUSINESS LOGIC, MATHEMATICAL FORMULAS, OR THE ARCHITECTURAL PARADIGM.** Your job is strictly to fix syntax errors, missing imports, or type mismatches.

## 1. Architectural Paradigm
- **Architecture:** Unidirectional Data Flow (UDF) using Jetpack Compose and a single `ViewModel`.
- **State Source of Truth:** `app/src/main/java/com/example/model/GameState.kt`. This is a massive immutable data class.
- **State Mutator:** `app/src/main/java/com/example/viewmodel/GameViewModel.kt`. It holds a `MutableStateFlow<GameState>`. 
- **Rule of Immutability:** State is NEVER mutated in place. All changes use `.copy()`.

## 2. The `endDay()` Pipeline (DANGER ZONE)
The `endDay()` function inside `GameViewModel.kt` is the most critical and fragile part of the application. It acts as a sequential state machine processing a full day of simulation:
1. Spoilage calculation
2. B2B Contract fulfillment
3. AI Rival Actions & Corporate Warfare
4. Market fluctuations & Sales
5. Facility Construction progress
6. Subsidiary processing
7. Narrative Event (Story) evaluation
8. Energy / Action Point Reset

**If there is an error in `endDay()`, fix the type mismatch or missing variable. DO NOT reorder these steps. DO NOT change the logic.**

## 3. UI Layer Constraints (Jetpack Compose)
Located in `app/src/main/java/com/example/ui/`.
- UI Screens observe `GameState` directly.
- **Lists & Modifiers:** We rely heavily on `.weight(1f)`, `.clipToBounds()`, and strict `Column`/`Box` layouts to keep elements constrained (e.g., in `ResearchHubScreen.kt`). 
- **Filtering:** Screens like `FacilitiesTab.kt` and `MarketTab.kt` explicitly split their rendering into two groups: "Visible/Unlocked" items at the top, and "Upcoming Milestones" (locked) at the bottom. Preserve this separation mapping.

## 4. Troubleshooting Missing References
If a reference like `Divider()` is deprecated or unresolved, replace it with `HorizontalDivider()`.
If an icon like `Icons.Default.Lock` is missing, ensure `androidx.compose.material.icons.filled.Lock` is imported.

Proceed with your fixes safely.
