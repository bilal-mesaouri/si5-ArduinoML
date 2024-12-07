
// Wiring code generated from an ArduinoML model
// Application name: redButton

long debounce = 200;

enum STATE {on, off};
STATE currentState = off;

boolean buttonABounceGuard = false;
long buttonALastDebounceTime = 0;

boolean buttonBBounceGuard = false;
long buttonBLastDebounceTime = 0;

void setup(){
  pinMode(9, INPUT);  // buttonA [Sensor]
  pinMode(10, INPUT);  // buttonB [Sensor]
  pinMode(12, OUTPUT); // led [Actuator]
}

void loop() {
	switch(currentState){
		case on:
			digitalWrite(12,HIGH);
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if( ((digitalRead(9) == HIGH && buttonABounceGuard)&&(digitalRead(10) == HIGH && buttonBBounceGuard))){
				currentState = off;
				buttonLastDebounceTime = millis();
			}
		break;
		case off:
			digitalWrite(12,LOW);
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if( ((digitalRead(9) == LOW && buttonABounceGuard)||(digitalRead(10) == LOW && buttonBBounceGuard))){
				currentState = on;
				buttonLastDebounceTime = millis();
			}
		break;
	}
}
