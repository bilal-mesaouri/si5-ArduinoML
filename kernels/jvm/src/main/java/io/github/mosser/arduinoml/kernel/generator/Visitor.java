package io.github.mosser.arduinoml.kernel.generator;

import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.structural.*;
import io.github.mosser.arduinoml.kernel.App;

import java.util.HashMap;
import java.util.Map;

public abstract class Visitor<T> {

	public abstract void visit(App app);

	public abstract void visit(State state);
	public abstract void visit(SignalCondition condition);
	public abstract void visit(TimeCondition condition);
	public abstract void visit(CompositeCondition compositeCondition);
	public abstract void visit(Action action);

	public abstract void visit(PinActuator pinActuator);
	public abstract void visit(PinSensor pinSensor);

	public abstract void visit(BusActuator busActuator);

	public abstract void visit(SerialAction action);

	public abstract void visit(SerialBrick serialbrick);



	
	public abstract void visit(Transition sensor);
	public abstract void visit(Condition condition);

	/***********************
	 ** Helper mechanisms **
	 ***********************/

	protected Map<String,Object> context = new HashMap<>();

	protected T result;

	public T getResult() {
		return result;
	}

	public abstract void visit(RemoteCondition condition);
}

