import sys

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

kt = read_file('app/src/main/java/com/example/ui/screens/FacilitiesTab.kt')

# In FacilitiesTab, we need a way to pass onSpinOff.
# Or we can just build the SubsidiariesScreen as requested and add the button there?
# The prompt says: "Create a SubsidiariesScreen displaying a list of active child companies."
# It does not explicitly mention how to spin them off in the UI, but says "allow players to spin off".
# Let's add the button to FacilitiesTab.kt
