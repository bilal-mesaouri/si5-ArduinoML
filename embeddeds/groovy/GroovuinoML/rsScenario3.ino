// Wiring code generated from an ArduinoML model
// Application name: SimpleToggle

long debounce = 200;
boolean buttonBounceGuard = false;
long buttonLastDebounceTime = 0;

enum STATE {off, on};
STATE currentState = off;

void setup(){
  pinMode(9, INPUT);  // button [Sensor]
  pinMode(12, OUTPUT); // led [Actuator]
}

void loop() {
	switch(currentState){
		case off:
			digitalWrite(12,LOW);
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if( (digitalRead(9) == HIGH && buttonBounceGuard)){
				currentState = on;
				buttonLastDebounceTime = millis();
			}
		break;
		case on:
			digitalWrite(12,HIGH);
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if( (digitalRead(9) == HIGH && buttonBounceGuard)){
				currentState = off;
				buttonLastDebounceTime = millis();
			}
		break;
	}
}
