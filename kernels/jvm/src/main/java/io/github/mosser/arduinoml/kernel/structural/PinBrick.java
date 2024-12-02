package io.github.mosser.arduinoml.kernel.structural;

public abstract class PinBrick extends Brick {
    private int pin;

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

}
