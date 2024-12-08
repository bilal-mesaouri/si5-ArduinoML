// Configuration série
serial 9600

// Composants
actuator "led" pin 11
pinSensor "button" pin 9

// États
state "on" means led becomes high
state "off" means led becomes low

// État initial
initial off

// Transitions avec remote
from "off" to "on" when "remote" receives 'a'
from "on" to "off" when "remote" receives 'z'

// Transitions avec bouton physique
from "off" to "on" when button becomes high
from "on" to "off" when button becomes high

export "RemoteCommunication"