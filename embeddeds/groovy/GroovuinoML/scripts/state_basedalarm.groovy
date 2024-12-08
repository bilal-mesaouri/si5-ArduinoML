pinSensor "button" onPin 9
actuator "led" pin 12

state "off" means led becomes low
state "on" means led becomes high

initial "off"

from "off" to "on" when button becomes high
from "on" to "off" when button becomes high

export "SimpleToggle"
