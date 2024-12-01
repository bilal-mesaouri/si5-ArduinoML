sensor "button" onPin 9
actuator "led" pin 12
actuator "buzzer" pin 13


state "firstpush" means led becomes low  and buzzer becomes high
state "secondpush" means led becomes high and buzzer becomes low
state "off" means led becomes low and buzzer becomes low

initial "off"


from "off" to "firstpush" when button becomes high
from "firstpush" to "secondpush" when button becomes high
from "secondpush" to "off" when button becomes high

export "MultistateAlarm"