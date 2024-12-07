package io.github.mosser.arduinoml.kernel.generator;

import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.structural.*;

/**
 * Quick and dirty visitor to support the generation of Wiring code
 */
public class ToWiring extends Visitor<StringBuffer> {
	enum PASS {ONE, TWO}


	public ToWiring() {
		this.result = new StringBuffer();
	}

	private void w(String s) {
		result.append(String.format("%s",s));
	}

	@Override
	public void visit(App app) {
		//first pass, create global vars
		context.put("pass", PASS.ONE);
		w("// Wiring code generated from an ArduinoML model\n");
		w(String.format("// Application name: %s\n", app.getName())+"\n");

		w("long debounce = 200;\n");
		w("\nenum STATE {");
		String sep ="";
		for(State state: app.getStates()){
			w(sep);
			state.accept(this);
			sep=", ";
		}
		w("};\n");
		if (app.getInitial() != null) {
			w("STATE currentState = " + app.getInitial().getName()+";\n");
		}

		for(Brick brick: app.getBricks()){
			brick.accept(this);
		}

		//second pass, setup and loop
		context.put("pass",PASS.TWO);
		w("\nvoid setup(){\n");
		for(Brick brick: app.getBricks()){
			brick.accept(this);
		}
		w("}\n");

		w("\nvoid loop() {\n" +
			"\tswitch(currentState){\n");
		for(State state: app.getStates()){
			state.accept(this);
		}
		w("\t}\n" +
			"}");
	}

	@Override
	public void visit(PinActuator actuator) {
		if(context.get("pass") == PASS.ONE) {
			return;
		}
		if(context.get("pass") == PASS.TWO) {
			w(String.format("  pinMode(%d, OUTPUT); // %s [Actuator]\n", actuator.getPin(), actuator.getName()));
			return;
		}
	}
	@Override
	public void visit(BusActuator actuator) {
		if (context.get("pass") == PASS.ONE) {
			w("#include <Wire.h>\n#include <LiquidCrystal.h>\n");
			w(String.format("LiquidCrystal %s(%s); // %s \n", actuator.getName(), actuator.getAddressString(), actuator.getName()));
			return;
		}
		if (context.get("pass") == PASS.TWO) {
			w("\t\t\tlcd.begin(16, 2);");
			w("// Initialize the LCD\n");
			w(String.format("\t\t\t%s.print(\"Press Button\");\n", actuator.getName()));
			return;
		}
	}


	@Override
	public void visit(PinSensor sensor) {
		if(context.get("pass") == PASS.ONE) {
			w(String.format("\nboolean %sBounceGuard = false;\n", sensor.getName()));
			w(String.format("long %sLastDebounceTime = 0;\n", sensor.getName()));
			return;
		}
		if(context.get("pass") == PASS.TWO) {
			w(String.format("  pinMode(%d, INPUT);  // %s [Sensor]\n", sensor.getPin(), sensor.getName()));
			return;
		}
	}

	@Override
	public void visit(State state) {
		if(context.get("pass") == PASS.ONE) {
			w(state.getName());
			return;
		}
		if(context.get("pass") == PASS.TWO) {
			w("\t\tcase " + state.getName() + ":\n");

			// Actions normales
			for (Action action : state.getActions()) {
				action.accept(this);
			}

			// Si c'est un NormalState, gérer les actions série et transitions multiples
			if(state instanceof NormalState) {
				NormalState normalState = (NormalState) state;

				// Actions série
				for(SerialAction serialAction : normalState.getSerialActions()) {
					serialAction.accept(this);
				}

				// Gestion du debounce
				w("\t\t\tbuttonBounceGuard = millis() - buttonLastDebounceTime > debounce;\n");

				// Transitions multiples
				boolean isFirst = true;
				for(Transition transition : normalState.getTransitions()) {
					if(isFirst) {
						w("\t\t\tif( ");
						isFirst = false;
					} else {
						w("\t\t\telse if( ");
					}
					transition.getCondition().accept(this);
					w("){\n\t\t\t\tcurrentState = " + transition.getNext().getName() + ";\n");
					w("\t\t\t\tbuttonLastDebounceTime = millis();\n");
					w("\t\t\t}\n");
				}
			}
			// État normal avec une seule transition
			else if (state.getTransition() != null) {
				state.getTransition().accept(this);
			}

			w("\t\tbreak;\n");
			return;
		}
	}
	@Override
	public void visit(TimeCondition condition) {
		if(context.get("pass") == PASS.ONE) {
			return;
		}
		if(context.get("pass") == PASS.TWO) {
			int delayInMS = condition.getDelay();
			w(String.format("\t\t\tdelay(%d);\n", delayInMS));
			return;
		}
	}

	@Override
	public void visit(SignalCondition condition) {
		if(context.get("pass") == PASS.ONE) {
			return;
		}
		if(context.get("pass") == PASS.TWO) {

			String sensorName = condition.getSensor().getName();
			w(String.format("(digitalRead(%d) == %s && %sBounceGuard)",
			condition.getSensor().getPin(), condition.getValue(), sensorName));
			return;
		}
	}


	@Override
	public void visit(CompositeCondition compositeCondition) {
		if(context.get("pass") == PASS.ONE) {
			return;
		}
		if(context.get("pass") == PASS.TWO) {
				w("(");
				compositeCondition.getFirstCondition().accept(this);
				w(compositeCondition.getOperator().toString());
				compositeCondition.getSecondCondition().accept(this);
				w(")");
			return;
		}
	}

	@Override
	public void visit(Transition transition) {
		if(context.get("pass") == PASS.ONE) {
			return;
		}
		if(context.get("pass") == PASS.TWO) {
			w("\t\t\tbuttonBounceGuard = millis() - buttonLastDebounceTime > debounce;\n");
			w("\t\t\tif( ");
			transition.getCondition().accept(this);
			w("){\n\t\t\t\tcurrentState = " + transition.getNext().getName() + ";\n");
			w("\t\t\t\tbuttonLastDebounceTime = millis();\n");
			w("\t\t\t}\n");
			return;
		}
	}

	@Override
	public void visit(Action action) {
		if(context.get("pass") == PASS.ONE) {
			return;
		}
		if(context.get("pass") == PASS.TWO) {
			if( action.getActuator() instanceof PinActuator){
				w(String.format("\t\t\tdigitalWrite(%d,%s);\n",((PinActuator)action.getActuator()).getPin(),action.getValue()));
				return;
			}
			// Handle BusActuator actions (e.g., LCD message)
			else if (action.getActuator() instanceof BusActuator) {
				// Generate code to print a message on the LCD
				BusActuator busActuator = (BusActuator)action.getActuator();
				w("lcd.setCursor(0, 0);\n");
				w(String.format("%s.print(\"%s\");\n", busActuator.getName(), busActuator.getMessage()));
				return;
			}
		}
	}

	@Override
	public void visit(Condition condition) {
        if (condition instanceof SignalCondition) {
            visit((SignalCondition) condition);
        } else if (condition instanceof CompositeCondition) {
            visit((CompositeCondition) condition);
        } else {
            throw new IllegalArgumentException("Unsupported Condition type: " + condition.getClass());
        }
		
	}
	@Override
	public void visit(SerialAction action) {
		if (context.get("pass") == PASS.ONE) {
			return; // Pas besoin d'ajouter quoi que ce soit lors de la première passe
		}
		if (context.get("pass") == PASS.TWO) {
			w(String.format("  Serial.println(\"%s\");\n", action.getMessage()));
		}
	}


	@Override
	public void visit(SerialBrick serial) {
		if (context.get("pass") == PASS.ONE) {
			// First pass - declare serial communication
			w("// Serial communication setup\n");
			return;
		}
		if (context.get("pass") == PASS.TWO) {
			// Second pass - initialize serial in setup()
			w(String.format("  Serial.begin(%d);\n", serial.getBaudeRate()));
			return;
		}
	}

	@Override
	public void visit(RemoteCondition condition) {
		if (context.get("pass") == PASS.ONE) {
			return;
		}
		if (context.get("pass") == PASS.TWO) {
			w("(Serial.available() > 0 && Serial.read() == '");
			w(String.valueOf(condition.getKey()));
			w("')");
			return;
		}
	}

}
