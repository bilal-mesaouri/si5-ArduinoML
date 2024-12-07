// Wiring code generated from an ArduinoML model
// Application name: Switch!

long debounce = 200;

enum STATE {on, off};
STATE currentState = off;

boolean buttonBounceGuard = false;
long buttonLastDebounceTime = 0;
#include <Wire.h>
#include <LiquidCrystal.h>
LiquidCrystal lcd(2, 3, 4, 5, 6, 7, 8); // lcd 

void setup(){
  pinMode(9, INPUT);  // button [Sensor]
  pinMode(12, OUTPUT); // led [Actuator]
			lcd.begin(16, 2);// Initialize the LCD
			lcd.print("Switch!");
}

void loop() {
	switch(currentState){
		case on:
			digitalWrite(12,HIGH);
			lcd.setCursor(0, 0);
			lcd.clear();
			lcd.print("high");
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
			lcd.print("lolw");
			buttonBounceGuard = millis() - buttonLastDebounceTime > debounce;
			if( (digitalRead(9) == LOW && buttonBounceGuard)){
				currentState = on;
				buttonLastDebounceTime = millis();
			}
		break;
	}
}
