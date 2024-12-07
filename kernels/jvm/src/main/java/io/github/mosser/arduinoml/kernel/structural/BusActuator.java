package io.github.mosser.arduinoml.kernel.structural;

import io.github.mosser.arduinoml.kernel.generator.Visitor;

public class BusActuator extends BusBrick implements Actuator {

    String actuatorMessage = "SET MESSAGE";


    public String getActuatorMessage() {
        return actuatorMessage;
    }

    public void setActuatorMessage(String actuatorMessage) {
        this.actuatorMessage = actuatorMessage;
    }
    
    @Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
