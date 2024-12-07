// Wiring code generated from an ArduinoML model
// Application name: dualCheckAlarmLed

long debounce = 200;
boolean buttonBounceGuard = false;
long buttonLastDebounceTime = 0;

enum STATE {on, off};
STATE currentState = off;

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
			if( ((digitalRead(9) == HIGH && buttonBounceGuard)&&(digitalRead(10) == HIGH && buttonBounceGuard))){
				currentState = off;
				Serial.println("LED is OFF");
				buttonLastDebounceTime = millis();
			}
		break;
		case off:
			digitalWrite(12,LOW);
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if( ((digitalRead(9) == LOW && buttonBounceGuard)||(digitalRead(10) == LOW && buttonBounceGuard))){
				currentState = on;
				buttonLastDebounceTime = millis();
			}
		break;
	}
}
