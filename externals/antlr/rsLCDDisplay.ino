
// Wiring code generated from an ArduinoML model
// Application name: redButton

long debounce = 200;

enum STATE {on, off};
STATE currentState = off;

boolean buttonBounceGuard = false;
long buttonLastDebounceTime = 0;
#include <Wire.h>
#include <LiquidCrystal.h>
LiquidCrystal lcd(8, 9, 10, 11, 12, 13, 14); // lcd 

void setup(){
  pinMode(9, INPUT);  // button [Sensor]
  pinMode(12, OUTPUT); // led [Actuator]
			lcd.begin(16, 2);// Initialize the LCD
			lcd.print("PressAButton");
}

void loop() {
	switch(currentState){
		case on:
			digitalWrite(12,HIGH);
			lcd.setCursor(0, 0);
			lcd.clear();
			lcd.print("HELLO");
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if( (digitalRead(9) == HIGH && buttonBounceGuard)){
				currentState = off;
				buttonLastDebounceTime = millis();
			}
		break;
		case off:
			digitalWrite(12,LOW);
			lcd.setCursor(0, 0);
			lcd.clear();
			lcd.print("GoodBye");
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if( (digitalRead(9) == HIGH && buttonBounceGuard)){
				currentState = on;
				buttonLastDebounceTime = millis();
			}
		break;
	}
}