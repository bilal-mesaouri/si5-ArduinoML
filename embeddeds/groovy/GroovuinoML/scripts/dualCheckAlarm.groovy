pinSensor "buttonA" onPin 9
pinSensor "buttonB" onPin 10
actuator "led" pin 12


state "on" means "led" becomes "high"
state "off" means "led" becomes "low"

initial "off"

from "on" to "off" when "buttonA" becomes "high" and "buttonB" becomes "high"
from off to on when buttonA becomes low or buttonB becomes low

export "Switch!"