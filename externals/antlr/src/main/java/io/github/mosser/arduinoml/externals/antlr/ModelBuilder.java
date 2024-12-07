package io.github.mosser.arduinoml.externals.antlr;

import io.github.mosser.arduinoml.externals.antlr.grammar.*;
import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.structural.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModelBuilder extends ArduinomlBaseListener {

    /********************
     ** Business Logic **
     ********************/

    private App theApp = null;
    private boolean built = false;

    public App retrieve() {
        if (built) { return theApp; }
        throw new RuntimeException("Cannot retrieve a model that was not created!");
    }

    /*******************
     ** Symbol tables **
     *******************/
    private Map<String, SerialBrick> serialBricks = new HashMap<>();
    private Map<String, PinSensor> sensors = new HashMap<>();
    private Map<String, Actuator> actuators = new HashMap<>();
    private Map<String, State> states = new HashMap<>();
    private Map<String, Binding> bindings = new HashMap<>();
    private Map<String, Transition> transitions = new HashMap<>();

    private ArrayList<Condition> currentCondition = new ArrayList<>(2);
    private Transition currentTransition = null;
    private String nextStateName = null;

    private class Binding {
        String to;
        Condition condition;
    }

    private State currentState = null;

    /**************************
     ** Listening mechanisms **
     **************************/

    @Override
    public void enterRoot(ArduinomlParser.RootContext ctx) {
        built = false;
        theApp = new App();
    }

    @Override
    public void exitRoot(ArduinomlParser.RootContext ctx) {
        this.built = true;
    }

    @Override
    public void enterDeclaration(ArduinomlParser.DeclarationContext ctx) {
        theApp.setName(ctx.name.getText());
    }

    @Override
    public void enterSensor(ArduinomlParser.SensorContext ctx) {
        PinSensor sensor = new PinSensor();
        sensor.setName(ctx.location().id.getText());
        sensor.setPin(Integer.parseInt(ctx.location().port.getText()));
        this.theApp.getBricks().add(sensor);
        sensors.put(sensor.getName(), sensor);
    }

    @Override
    public void enterActuator(ArduinomlParser.ActuatorContext ctx) {
        PinActuator actuator = new PinActuator();
        actuator.setName(ctx.location().id.getText());
        actuator.setPin(Integer.parseInt(ctx.location().port.getText()));
        this.theApp.getBricks().add(actuator);
        actuators.put(actuator.getName(), actuator);
    }

    @Override
    public void enterSerial(ArduinomlParser.SerialContext ctx) {
        SerialBrick serial = new SerialBrick();
        serial.setName("serial");
        serial.setBaudeRate(Integer.parseInt(ctx.baudRate.getText()));
        this.theApp.getBricks().add(serial);
        serialBricks.put(serial.getName(), serial);
    }

    @Override
    public void enterState(ArduinomlParser.StateContext ctx) {
        State local = new NormalState();
        if (states.get(ctx.name.getText()) != null) {
            local = states.get(ctx.name.getText());
        }
        local.setName(ctx.name.getText());
        this.currentState = local;
        this.states.put(local.getName(), local);
    }
    @Override
    public void exitState(ArduinomlParser.StateContext ctx) {
        this.theApp.getStates().add(this.currentState);
        this.currentState = null;
    }

    @Override
    public void enterAction(ArduinomlParser.ActionContext ctx) {
        if (ctx.receiver.getText().equals("serial")) {
            SerialAction action = new SerialAction();
            String message = ctx.value.getText();
            // Remove quotes if present
            if (message.startsWith("\"") && message.endsWith("\"")) {
                message = message.substring(1, message.length() - 1);
            }
            action.setMessage(message);
            currentState.getActions().add(action);
        } else {
            Action action = new Action();
            action.setActuator(actuators.get(ctx.receiver.getText()));
            action.setValue(SIGNAL.valueOf(ctx.value.getText()));
            currentState.getActions().add(action);
        }
    }

    @Override
    public void enterSignalCondition(ArduinomlParser.SignalConditionContext ctx) {
        SignalCondition condition = new SignalCondition();
        condition.setSensor(sensors.get(ctx.trigger.getText()));
        condition.setValue(SIGNAL.valueOf(ctx.value.getText()));

        if (currentTransition != null) {
            currentTransition.setCondition(condition);
        }
        currentCondition.add(condition);

        if (condition.getSensor() == null) {
            throw new RuntimeException("Sensor not found: " + ctx.trigger.getText());
        }
    }

    @Override
    public void enterSerialCondition(ArduinomlParser.SerialConditionContext ctx) {
        RemoteCondition condition = new RemoteCondition();
        String value = ctx.value.getText();
        // Remove quotes if present
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        condition.setKey(value.charAt(0));

        if (currentTransition != null) {
            currentTransition.setCondition(condition);
        }
        currentCondition.add(condition);
    }

    @Override
    public void enterTransition(ArduinomlParser.TransitionContext ctx) {
        Transition transition = new Transition();
        this.nextStateName = ctx.next.getText();

        if (states.get(this.nextStateName) == null) {
            State state = new State();
            state.setName(this.nextStateName);
            states.put(this.nextStateName, state);
        }

        transition.setNext(states.get(this.nextStateName));
        currentState.setTransition(transition);
        this.currentTransition = transition;
    }

    @Override
    public void exitTransition(ArduinomlParser.TransitionContext ctx) {
        if (this.currentState instanceof NormalState) {
            ((NormalState)this.currentState).addTransition(this.currentTransition);
        } else {
            // Fallback pour les State normaux si nécessaire
            this.currentState.setTransition(this.currentTransition);
        }
        this.currentTransition = null;
        this.nextStateName = null;
        this.currentCondition.clear();
    }
    @Override
    public void enterInitial(ArduinomlParser.InitialContext ctx) {
        this.theApp.setInitial(this.currentState);
    }
}