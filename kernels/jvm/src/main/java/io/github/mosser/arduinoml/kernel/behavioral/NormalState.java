package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.generator.Visitable;
import io.github.mosser.arduinoml.kernel.generator.Visitor;
import java.util.ArrayList;
import java.util.List;

public class NormalState extends State implements Visitable {

    private String name;
    private List<Action> actions = new ArrayList<>();
    private List<Transition> transitions = new ArrayList<>();
    private List<SerialAction> serialActions = new ArrayList<>();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public void addAction(Action action) {
        this.actions.add(action);
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public void addTransition(Transition transition) {
        this.transitions.add(transition);
    }

    public List<SerialAction> getSerialActions() {
        return serialActions;
    }

    public void addSerialAction(SerialAction action) {
        this.serialActions.add(action);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}