package io.github.mosser.arduinoml.kernel.behavioral;

import java.util.Arrays;
import java.util.List;

import io.github.mosser.arduinoml.kernel.generator.Visitor;

public class CompositeCondition extends Condition {
    
    Operator operator;
    Condition first_condition;
    Condition second_condition;

    public CompositeCondition() {
    }
    public CompositeCondition(Operator operator, Condition first_condition, Condition second_condition) {
        this.operator = operator;
        this.first_condition = first_condition;
        this.second_condition = second_condition;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public Operator getOperator() {
        return operator;
    }

    public List<Condition> getConditions() {
        return Arrays.asList(first_condition, second_condition);
    }

    public Condition getFirstCondition() {
        return first_condition;
    }

    public Condition getSecondCondition() {
        return second_condition;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }
    public void setFirstCondition(Condition first_condition) {
        this.first_condition = first_condition;
    }
    public void setSecondCondition(Condition second_condition) {
        this.second_condition = second_condition;
    }




}
