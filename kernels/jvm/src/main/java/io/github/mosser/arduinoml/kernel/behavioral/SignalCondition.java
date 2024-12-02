package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.SIGNAL;
import io.github.mosser.arduinoml.kernel.structural.PinSensor;

public class SignalCondition extends Condition{

    private PinSensor sensor;
    private SIGNAL value;

    public PinSensor getSensor() {
        return sensor;
    }

    public void setSensor(PinSensor sensor) {
        this.sensor = sensor;
    }

    public SIGNAL getValue() {
        return value;
    }

    public void setValue(SIGNAL value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return sensor.getName() + " == " + value.toString();
    }
}
