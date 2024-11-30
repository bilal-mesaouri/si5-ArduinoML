package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.generator.Visitor;


public class TimeCondition extends Condition {

    private int delayInMS;


    public int getDelay() {
        return delayInMS;
    }

    public void setDelay(int newDelay) {
        this.delayInMS = newDelay;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
