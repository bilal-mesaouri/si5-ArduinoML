package main.groovy.groovuinoml.dsl

import io.github.mosser.arduinoml.kernel.behavioral.TimeUnit
import io.github.mosser.arduinoml.kernel.behavioral.Action
import io.github.mosser.arduinoml.kernel.behavioral.State
import io.github.mosser.arduinoml.kernel.behavioral.Condition
import io.github.mosser.arduinoml.kernel.behavioral.SignalCondition
import io.github.mosser.arduinoml.kernel.behavioral.CompositeCondition
import io.github.mosser.arduinoml.kernel.structural.Actuator
import io.github.mosser.arduinoml.kernel.structural.Sensor
import io.github.mosser.arduinoml.kernel.structural.SIGNAL
import io.github.mosser.arduinoml.kernel.behavioral.Operator

abstract class GroovuinoMLBasescript extends Script {
//	public static Number getDuration(Number number, TimeUnit unit) throws IOException {
//		return number * unit.inMillis;
//	}

	// sensor "name" pin n
	def sensor(String name) {
		[pin: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createSensor(name, n) },
		onPin: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createSensor(name, n)}]
	}
	
	// actuator "name" pin n
	def actuator(String name) {
		[pin: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createActuator(name, n) }]
	}
	
	// state "name" means actuator becomes signal [and actuator becomes signal]*n
	def state(String name) {
		List<Action> actions = new ArrayList<Action>()
		((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createState(name, actions)
		// recursive closure to allow multiple and statements
		def closure
		closure = { actuator -> 
			[becomes: { signal ->
				Action action = new Action()
				action.setActuator(actuator instanceof String ? (Actuator)((GroovuinoMLBinding)this.getBinding()).getVariable(actuator) : (Actuator)actuator)
				action.setValue(signal instanceof String ? (SIGNAL)((GroovuinoMLBinding)this.getBinding()).getVariable(signal) : (SIGNAL)signal)
				actions.add(action)
				[and: closure]
			}]
		}
		[means: closure]
	}
	
	// initial state
	def initial(state) {
		((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().setInitialState(state instanceof String ? (State)((GroovuinoMLBinding)this.getBinding()).getVariable(state) : (State)state)
	}
	

	// from state1 to state2 when sensor becomes signal Operator sensor becomes signal
	def from(state1) {
		def condition = null
		CompositeCondition compositeCondition = new CompositeCondition()
		def sensorHandlerAfterOr
		State state1Dup = state1 instanceof String ? (State)((GroovuinoMLBinding)this.getBinding()).getVariable(state1) : (State)state1

		def sensorHandlerAfterAnd = {sensor ->
			[becomes: { signal ->
				compositeCondition = new CompositeCondition()
				compositeCondition.setOperator(Operator.AND)
				SignalCondition signalCondition = new SignalCondition()
				signalCondition.setSensor(sensor instanceof String ? (Sensor)((GroovuinoMLBinding)this.getBinding()).getVariable(sensor) : (Sensor)sensor)
				signalCondition.setValue(signal instanceof String ? (SIGNAL)((GroovuinoMLBinding)this.getBinding()).getVariable(signal) : (SIGNAL)signal)
				compositeCondition.setFirstCondition(condition)
				compositeCondition.setSecondCondition(signalCondition)
				state1Dup.getTransition().setCondition(compositeCondition)
				condition = compositeCondition
				[
					and: delegate,
					or: sensorHandlerAfterOr
					
				]
			}]
		}

		sensorHandlerAfterOr = {sensor ->
			[becomes: { signal ->
				compositeCondition = new CompositeCondition()
				compositeCondition.setOperator(Operator.OR)
				SignalCondition signalCondition = new SignalCondition()
				signalCondition.setSensor(sensor instanceof String ? (Sensor)((GroovuinoMLBinding)this.getBinding()).getVariable(sensor) : (Sensor)sensor)
				signalCondition.setValue(signal instanceof String ? (SIGNAL)((GroovuinoMLBinding)this.getBinding()).getVariable(signal) : (SIGNAL)signal)
				compositeCondition.setFirstCondition(condition)
				compositeCondition.setSecondCondition(signalCondition)
				state1Dup.getTransition().setCondition(compositeCondition)
				condition = compositeCondition
				[
					and: sensorHandlerAfterAnd,
					or: delegate
					
				]
			}]
		}

		[to: { state2 -> 
					((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createTransition(
						state1Dup,
						state2 instanceof String ? (State)((GroovuinoMLBinding)this.getBinding()).getVariable(state2) : (State)state2, 
						condition)
			[when: { sensor ->
				[becomes: { signal ->
					SignalCondition signalCondition = new SignalCondition()
					signalCondition.setSensor(sensor instanceof String ? (Sensor)((GroovuinoMLBinding)this.getBinding()).getVariable(sensor) : (Sensor)sensor)
					signalCondition.setValue(signal instanceof String ? (SIGNAL)((GroovuinoMLBinding)this.getBinding()).getVariable(signal) : (SIGNAL)signal)
					state1Dup.getTransition().setCondition(signalCondition)
					condition = signalCondition
					[
						and: sensorHandlerAfterAnd,
						or: sensorHandlerAfterOr
					]
				}
				]
				}]
			},
			]
	}
	
	// export name
	def export(String name) {
		println(((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().generateCode(name).toString())
	}
	
	// disable run method while running
	int count = 0
	abstract void scriptBody()
	def run() {
		if(count == 0) {
			count++
			scriptBody()
		} else {
			println "Run method is disabled"
		}
	}
}
