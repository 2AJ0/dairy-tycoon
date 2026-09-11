import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/MainGameScreen.kt')

old_call = """                                onStudyResearch = { viewModel.studyResearch() },
                                evaluateAction = { viewModel.evaluateAction(it) },
                                onBackClick = { currentDrawerDestination = DrawerDestination.DASHBOARD }
                            )"""

new_call = """                                onStudyResearch = { viewModel.studyResearch() },
                                evaluateAction = { viewModel.evaluateAction(it) },
                                onBackClick = { currentDrawerDestination = DrawerDestination.DASHBOARD },
                                onDismissAweHighlight = { viewModel.dismissAweHighlight() }
                            )"""

kt = kt.replace(old_call, new_call)

with open('app/src/main/java/com/example/ui/screens/MainGameScreen.kt', 'w') as f:
    f.write(kt)
