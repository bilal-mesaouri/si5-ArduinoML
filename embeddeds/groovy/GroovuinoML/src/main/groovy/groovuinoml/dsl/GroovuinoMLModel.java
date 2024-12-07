package main.groovy.groovuinoml.dsl;

import java.util.ArrayList;
import java.util.List;

import groovy.lang.Binding;

import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.generator.ToWiring;
import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.Brick;
import io.github.mosser.arduinoml.kernel.structural.PinActuator;
import io.github.mosser.arduinoml.kernel.structural.BusActuator;
import io.github.mosser.arduinoml.kernel.structural.PinSensor;
import io.github.mosser.arduinoml.kernel.structural.SIGNAL;

public class GroovuinoMLModel {
	private List<Brick> bricks;
	private List<State> states;
	private State initialState;
	
	private Binding binding;
	
	public GroovuinoMLModel(Binding binding) {
		this.bricks = new ArrayList<Brick>();
		this.states = new ArrayList<State>();
		this.binding = binding;
	}
	
	public void createPinSensor(String name, Integer pinNumber) {
		PinSensor sensor = new PinSensor();
		sensor.setName(name);
		sensor.setPin(pinNumber);
		this.bricks.add(sensor);
		this.binding.setVariable(name, sensor);
//		System.out.println("> sensor " + name + " on pin " + pinNumber);
	}
	public void createBusActuator(String name, String bus, String message) {
		BusActuator busActuator = new BusActuator();
		busActuator.setName(name);
		busActuator.setActuatorMessage(message);
		int[] adresses = bus == "bus1" ? new int[]{2, 3, 4, 5, 6, 7, 8} : new int[]{9, 10, 11, 12, 13, 14, 15};
		busActuator.setAddress(adresses);
		this.bricks.add(busActuator);
		this.binding.setVariable(name, busActuator);
//		System.out.println("> sensor " + name + " on pin " + pinNumber);
	}
	
	public void createActuator(String name, Integer pinNumber) {
		PinActuator actuator = new PinActuator();
		actuator.setName(name);
		actuator.setPin(pinNumber);
		this.bricks.add(actuator);
		this.binding.setVariable(name, actuator);
	}
	
	public void createState(String name, List<Action> actions) {
		State state = new State();
		state.setName(name);
		state.setActions(actions);
		this.states.add(state);
		this.binding.setVariable(name, state);
	}
	
	public void createSignalCondition(PinSensor sensor, SIGNAL value) {
		SignalCondition signalCondition = new SignalCondition();
		signalCondition.setSensor(sensor);
		signalCondition.setValue(value);
	}
	
	public void createCompositeCondition(Operator operator, Condition first_condition, Condition second_condition) {
		CompositeCondition compositeCondition = new CompositeCondition();
		compositeCondition.setOperator(operator);
		compositeCondition.setFirstCondition(first_condition);
		compositeCondition.setSecondCondition(second_condition);

	}
	
	public void createTransition(State from, State to, Condition condition) {
		Transition transition = new Transition();
		transition.setNext(to);
		transition.setCondition(condition);
		from.setTransition(transition);
	}
	
	

	public void createTimeCondition(State from, State to, int delay) {
		TimeCondition timeCondition = new TimeCondition();
		timeCondition.setDelay(delay);
		
	}
	
	
	
	public void setInitialState(State state) {
		this.initialState = state;
	}
	
	@SuppressWarnings("rawtypes")
	public Object generateCode(String appName) {
		App app = new App();
		app.setName(appName);
		app.setBricks(this.bricks);
		app.setStates(this.states);
		app.setInitial(this.initialState);
		Visitor codeGenerator = new ToWiring();
		app.accept(codeGenerator);
		
		return codeGenerator.getResult();
	}
}
