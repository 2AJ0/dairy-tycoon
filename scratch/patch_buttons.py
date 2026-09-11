import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/components/TopStatsHeader.kt')

# Add import
old_imports = "import com.example.ui.components.AnimatedCounter"
new_imports = "import com.example.ui.components.AnimatedCounter\nimport com.example.ui.components.bounceClick"
if "import com.example.ui.components.bounceClick" not in kt:
    kt = kt.replace(old_imports, new_imports)

# Work Button
old_work = """                // Work Button
                FilledTonalButton(
                    onClick = onWorkClick,
                    enabled = canAct,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("work_button"),"""
new_work = """                // Work Button
                FilledTonalButton(
                    onClick = onWorkClick,
                    enabled = canAct,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("work_button")
                        .bounceClick(),"""
kt = kt.replace(old_work, new_work)

# Study Button
old_study = """                // Study Button
                FilledTonalButton(
                    onClick = onStudyClick,
                    enabled = canAct,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("study_button"),"""
new_study = """                // Study Button
                FilledTonalButton(
                    onClick = onStudyClick,
                    enabled = canAct,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("study_button")
                        .bounceClick(),"""
kt = kt.replace(old_study, new_study)

# End Day Button
old_end = """                // Master "End Day" Button
                Button(
                    onClick = onEndDayClick,
                    modifier = Modifier
                        .weight(1.3f)
                        .height(44.dp)
                        .testTag("end_day_button"),"""
new_end = """                // Master "End Day" Button
                Button(
                    onClick = onEndDayClick,
                    modifier = Modifier
                        .weight(1.3f)
                        .height(44.dp)
                        .testTag("end_day_button")
                        .bounceClick(),"""
kt = kt.replace(old_end, new_end)

with open('app/src/main/java/com/example/ui/components/TopStatsHeader.kt', 'w') as f:
    f.write(kt)
