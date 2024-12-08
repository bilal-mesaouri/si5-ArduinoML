// Wiring code generated from an ArduinoML model
// Application name: RemoteCommunication

long debounce = 200;
boolean buttonBounceGuard = false;
long buttonLastDebounceTime = 0;

enum STATE {on, off};
STATE currentState = off;
// Serial communication setup

void setup(){
  Serial.begin(9600);
  pinMode(11, OUTPUT); // led [Actuator]
  pinMode(9, INPUT);  // button [Sensor]
}

void loop() {
	switch(currentState){
		case on:
			digitalWrite(11,HIGH);
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if( (Serial.available() > 0 && Serial.read() == 'z')){
				currentState = off;
				Serial.println("LED is OFF");
				buttonLastDebounceTime = millis();
			}
			else if( (digitalRead(9) == HIGH && buttonBounceGuard)){
				currentState = off;
				Serial.println("LED is OFF");
				buttonLastDebounceTime = millis();
			}
		break;
		case off:
			digitalWrite(11,LOW);
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if( (Serial.available() > 0 && Serial.read() == 'a')){
				currentState = on;
				Serial.println("LED is OFF");
				buttonLastDebounceTime = millis();
			}
			else if( (digitalRead(9) == HIGH && buttonBounceGuard)){
				currentState = on;
				Serial.println("LED is OFF");
				buttonLastDebounceTime = millis();
			}
		break;
	}
}
