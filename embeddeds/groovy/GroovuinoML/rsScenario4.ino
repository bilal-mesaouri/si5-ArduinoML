// Wiring code generated from an ArduinoML model
// Application name: MultistateAlarm

long debounce = 200;
boolean buttonBounceGuard = false;
long buttonLastDebounceTime = 0;

enum STATE {firstpush, secondpush, off};
STATE currentState = off;

void setup(){
  pinMode(9, INPUT);  // button [Sensor]
  pinMode(12, OUTPUT); // led [Actuator]
  pinMode(13, OUTPUT); // buzzer [Actuator]
}

void loop() {
	switch(currentState){
		case firstpush:
			digitalWrite(12,LOW);
			digitalWrite(13,HIGH);
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if( (digitalRead(9) == HIGH && buttonBounceGuard)){
				currentState = secondpush;
				buttonLastDebounceTime = millis();
			}
		break;
		case secondpush:
			digitalWrite(12,HIGH);
			digitalWrite(13,LOW);
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if( (digitalRead(9) == HIGH && buttonBounceGuard)){
				currentState = off;
				buttonLastDebounceTime = millis();
			}
		break;
		case off:
			digitalWrite(12,LOW);
			digitalWrite(13,LOW);
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if( (digitalRead(9) == HIGH && buttonBounceGuard)){
				currentState = firstpush;
				buttonLastDebounceTime = millis();
			}
		break;
	}
}
