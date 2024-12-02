package io.github.mosser.arduinoml.kernel.samples;

import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.generator.ToWiring;
import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.*;

import java.util.Arrays;

public class Switch {

	public static void main(String[] args) {
		// Declaring elementary bricks
		PinSensor button = new PinSensor();
		button.setName("button");
		button.setPin(9);

		PinActuator led = new PinActuator();
		led.setName("LED");
		led.setPin(12);

		BusActuator lcd = new BusActuator();
		lcd.setName("lcd");

		lcd.setAddress(new int[]{2, 3, 4, 5, 6, 7, 8});

		// Declaring states
		State on = new State();
		on.setName("on");

		State off = new State();
		off.setName("off");

		// Creating actions
		Action switchTheLightOn = new Action();
		switchTheLightOn.setActuator(led);
		switchTheLightOn.setValue(SIGNAL.HIGH);

		Action switchTheLightOff = new Action();
		switchTheLightOff.setActuator(lcd);
		switchTheLightOff.setValue(SIGNAL.LOW);

		// Binding actions to states
		on.setActions(Arrays.asList(switchTheLightOn));
		off.setActions(Arrays.asList(switchTheLightOff));

		// Creating transitions
		Transition on2off = new Transition();
		on2off.setNext(off);
		SignalCondition on2offCondition = new SignalCondition();
		on2offCondition.setSensor(button);
		on2offCondition.setValue(SIGNAL.HIGH);
		on2off.setCondition(on2offCondition);


		Transition off2on = new Transition();
		off2on.setNext(on);
		SignalCondition off2onCondition = new SignalCondition();
		off2onCondition.setSensor(button);
		off2onCondition.setValue(SIGNAL.HIGH);
		SignalCondition off2onCondition2 = new SignalCondition();
		off2onCondition2.setSensor(button);
		off2onCondition2.setValue(SIGNAL.HIGH);
		
		CompositeCondition compositeCondition = new CompositeCondition(Operator.AND, off2onCondition2, off2onCondition);
		off2on.setCondition(compositeCondition);
		// Binding transitions to states
		on.setTransition(on2off);
		off.setTransition(off2on);

		// Building the App
		App theSwitch = new App();
		theSwitch.setName("Switch!");
		theSwitch.setBricks(Arrays.asList(button, led, lcd ));
		theSwitch.setStates(Arrays.asList(on, off));
		theSwitch.setInitial(off);

		// Generating Code
		Visitor codeGenerator = new ToWiring();
		theSwitch.accept(codeGenerator);

		// Printing the generated code on the console
		System.out.println(codeGenerator.getResult());
	}

}
