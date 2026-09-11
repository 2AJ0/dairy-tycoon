import os
import glob

# Common instruction header for all UI files
ui_header = """/**
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
"""

# Common instruction header for ViewModel
vm_header = """/**
 * ==========================================
 * AI / IDE AGENT INSTRUCTIONS:
 * ==========================================
 * This is the GameViewModel, the central state machine for Dairy Tycoon.
 * 
 * CORE RULES FOR FIXING ERRORS:
 * 1. IMMUTABILITY: `GameState` is immutable. All state mutations MUST happen via `_gameState.update { it.copy(...) }`.
 * 2. END DAY LOOP: The `endDay()` function is a massive, strictly ordered pipeline (Spoilage -> B2B -> Market -> Construction -> Story -> Save). 
 *    - DO NOT alter the sequence of operations.
 *    - DO NOT change the mathematical formulas for profit, capacity, or subsidiary dividends.
 * 3. SAFE FIXES: If there are compilation errors (e.g., unresolved references, type mismatches), fix the types or imports. Do not re-architect the loop.
 */
"""

# Common instruction header for Models
model_header = """/**
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
"""

def add_header(filepath, header):
    if not os.path.exists(filepath):
        return
    with open(filepath, 'r') as f:
        content = f.read()
    
    if "AI / IDE AGENT INSTRUCTIONS" in content:
        return # Already commented
        
    # Insert after package declaration if present
    if content.startswith("package "):
        parts = content.split("\n", 1)
        new_content = parts[0] + "\n\n" + header + parts[1]
    else:
        new_content = header + "\n" + content
        
    with open(filepath, 'w') as f:
        f.write(new_content)

# Apply to ViewModel
add_header('app/src/main/java/com/example/viewmodel/GameViewModel.kt', vm_header)

# Apply to Models
for f in glob.glob('app/src/main/java/com/example/model/*.kt'):
    add_header(f, model_header)

# Apply to UI Screens and Components
for f in glob.glob('app/src/main/java/com/example/ui/screens/*.kt'):
    add_header(f, ui_header)

for f in glob.glob('app/src/main/java/com/example/ui/components/*.kt'):
    add_header(f, ui_header)

print("Headers added successfully to all files.")
