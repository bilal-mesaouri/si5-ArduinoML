// Wiring code generated from an ArduinoML model
// Application name: SimpleAlarm

long debounce = 200;
boolean buttonBounceGuard = false;
long buttonLastDebounceTime = 0;

enum STATE {on, off};
STATE currentState = off;

void setup(){
  pinMode(9, INPUT);  // button [Sensor]
  pinMode(12, OUTPUT); // led [Actuator]
  pinMode(11, OUTPUT); // buzzer [Actuator]
}

void loop() {
	switch(currentState){
		case on:
			digitalWrite(12,HIGH);
			digitalWrite(11,HIGH);
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if( (digitalRead(9) == LOW && buttonBounceGuard)){
				currentState = off;
				buttonLastDebounceTime = millis();
			}
		break;
		case off:
			digitalWrite(12,LOW);
			digitalWrite(11,LOW);
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if( (digitalRead(9) == HIGH && buttonBounceGuard)){
				currentState = on;
				buttonLastDebounceTime = millis();
			}
		break;
	}
}
