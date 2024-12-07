package io.github.mosser.arduinoml.kernel.structural;

import io.github.mosser.arduinoml.kernel.generator.Visitor;

public class SerialBrick extends BusBrick implements Actuator {
    private int baudeRate = 9600;
    private String message = "SET MESSAGE";

    public int getBaudeRate() {
        return baudeRate;
    }

    public void setBaudeRate(int baudeRate) {
        this.baudeRate = baudeRate;
    }

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