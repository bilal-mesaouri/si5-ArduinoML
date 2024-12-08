
// Wiring code generated from an ArduinoML model
// Application name: remoteControlApp

long debounce = 200;
boolean buttonBounceGuard = false;
long buttonLastDebounceTime = 0;

enum STATE {waitingForInput, ledOn, ledOff};
STATE currentState = waitingForInput;
// Serial communication setup

void setup(){
  Serial.begin(9600);
  pinMode(9, OUTPUT); // led [Actuator]
}

void loop() {
	switch(currentState){
		case waitingForInput:
			digitalWrite(9,LOW);
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if( (Serial.available() > 0 && Serial.read() == '1')){
				currentState = ledOn;
				Serial.println("LED is ON");
				buttonLastDebounceTime = millis();
			}
			else if( (Serial.available() > 0 && Serial.read() == '0')){
				currentState = ledOff;
				Serial.println("LED is OFF");
				buttonLastDebounceTime = millis();
			}
		break;
		case ledOn:
			digitalWrite(9,HIGH);
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if( (Serial.available() > 0 && Serial.read() == '0')){
				currentState = ledOff;
				buttonLastDebounceTime = millis();
			}
		break;
		case ledOff:
			digitalWrite(9,LOW);
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if( (Serial.available() > 0 && Serial.read() == '1')){
				currentState = ledOn;
				buttonLastDebounceTime = millis();
			}
		break;
	}
}