pinSensor "button" onPin 9
actuator "led" pin 12
lcdActuator "lcd" bus "bus1" message "Switch!"

state "on" means "led" becomes "high" and "lcd" displays "high"
state "off" means"led" becomes "low" and "lcd" displays "lolw"

initial "off"

from "on" to "off" when "button" becomes "high"
from off to on when button becomes low

export "Switch!"