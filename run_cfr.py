import subprocess
with open('decompiled.java', 'w') as f:
    subprocess.run(['/Applications/Android Studio.app/Contents/jbr/Contents/Home/bin/java', '-jar', 'cfr.jar', './app/build/intermediates/built_in_kotlinc/debug/compileDebugKotlin/classes/com/example/viewmodel/GameViewModel.class'], stdout=f)
