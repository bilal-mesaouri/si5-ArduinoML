# About the project :
This project uses Groovy for the internal DSL and ANTLR for the external DSL.

Implemented Features:
- All 4 basic scenarios

Optional Extensions:
- LCD display
- Remote communication

generated .ino code is in the root directory of each method (embeeded/external)

# How to launch :
run : " mvn clean install " inside the kernel
- ### embeeded (groovy):
  -  mvn clean compile assembly:single
  - java -jar "target/dsl-groovy-1.0-jar-with-dependencies.jar" "scripts/YOUR_SCENARIO_HERE.groovy"
- ### external (antlr):
  - mvn exec:java -Dexec.args="src/main/resources/YOUR_SCENARIO_HERE.arduinoml"