Transition condition set
true
Transition condition set
false
Transition condition set
false
Transition condition set
false
Transition condition set
true
Transition condition set
false
####### creating the transss
####### creating the signal condition
####### creating the signal condition
####### creating the signal condition
###### transss
####### creating the transss
####### creating the signal condition
###### transss
// Wiring code generated from an ArduinoML model
// Application name: Switch!

long debounce = 200;

enum STATE {on, off};
STATE currentState = off;

boolean buttonBounceGuard = false;
long buttonLastDebounceTime = 0;

void setup(){
  pinMode(9, INPUT);  // button [Sensor]
  pinMode(12, OUTPUT); // led [Actuator]
}

void loop() {
	switch(currentState){
		case on:
			digitalWrite(12,HIGH);
			if( (((digitalRead(9) == HIGH && buttonBounceGuard)&&(digitalRead(9) == HIGH && buttonBounceGuard))||(digitalRead(9) == LOW && buttonBounceGuard))){
				currentState = off;
			}
		break;
		case off:
			digitalWrite(12,LOW);
			if( (digitalRead(9) == HIGH && buttonBounceGuard)){
				currentState = on;
			}
		break;
	}
}
