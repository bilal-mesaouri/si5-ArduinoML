package io.github.mosser.arduinoml.kernel.structural;

import io.github.mosser.arduinoml.kernel.generator.Visitor;

public class BusActuator extends BusBrick implements Actuator {

    String message = "SET MESSAGE";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
