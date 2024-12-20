package main.groovy.groovuinoml.dsl

import io.github.mosser.arduinoml.kernel.behavioral.TimeUnit
import io.github.mosser.arduinoml.kernel.behavioral.Action
import io.github.mosser.arduinoml.kernel.behavioral.State
import io.github.mosser.arduinoml.kernel.behavioral.Condition
import io.github.mosser.arduinoml.kernel.behavioral.SignalCondition
import io.github.mosser.arduinoml.kernel.behavioral.CompositeCondition
import io.github.mosser.arduinoml.kernel.structural.Actuator
import io.github.mosser.arduinoml.kernel.structural.PinSensor
import io.github.mosser.arduinoml.kernel.structural.SIGNAL
import io.github.mosser.arduinoml.kernel.behavioral.Operator
import io.github.mosser.arduinoml.kernel.structural.BusActuator

abstract class GroovuinoMLBasescript extends Script {
//	public static Number getDuration(Number number, TimeUnit unit) throws IOException {
//		return number * unit.inMillis;
//	}

	// pinSensor "name" pin n
	def pinSensor(String name) {
		[pin: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createPinSensor(name, n) },
		 onPin: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createPinSensor(name, n)}]
	}
	
	// actuator "name" pin n
	def actuator(String name) {
		[pin: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createActuator(name, n) }]
	}

	def lcdActuator(String name) {
		[bus: { bus ->
			[
				message: { message -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createBusActuator(name, bus, message) },
			]	
		}	
		]
	}

	def busActuator(String name) {
		[lcd: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createActuator(name, n) }]
	}

	def serial(Integer baudRate) {
		((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createSerialCommunication(baudRate)
	}
	// state "name" means actuator becomes signal [and actuator becomes signal]*n
	def state(String name) {
		List<Action> actions = new ArrayList<Action>()
		((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createState(name, actions)
		// recursive closure to allow multiple and statements
		def closure
		closure = { actuator -> 
			[
					becomes: { signal ->
						Action action = new Action()
						action.setActuator(actuator instanceof String ? (Actuator)((GroovuinoMLBinding)this.getBinding()).getVariable(actuator) : (Actuator)actuator)
						action.setValue(signal instanceof String ? (SIGNAL)((GroovuinoMLBinding)this.getBinding()).getVariable(signal) : (SIGNAL)signal)
						actions.add(action)
						[and: closure]
					},
					displays: { message ->
						Action action = new Action()
						action.setActuator(actuator instanceof String ? (Actuator)((GroovuinoMLBinding)this.getBinding()).getVariable(actuator) : (Actuator)actuator)
						action.setMessage(message)
						actions.add(action)
						[and: closure]
					},
					prints: { message ->
						((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().addSerialAction(
								((GroovuinoMLBinding)this.getBinding()).getVariable(name),
								message
						)
						[and: closure]
					}
			]
		}
		[means: closure]
	}
	
	// initial state
	def initial(state) {
		((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().setInitialState(state instanceof String ? (State)((GroovuinoMLBinding)this.getBinding()).getVariable(state) : (State)state)
	}
	

	// from state1 to state2 when pinSensor becomes signal Operator pinSensor becomes signal
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
				signalCondition.setSensor(sensor instanceof String ? (PinSensor)((GroovuinoMLBinding)this.getBinding()).getVariable(sensor) : (PinSensor)sensor)
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
				signalCondition.setSensor(sensor instanceof String ? (PinSensor)((GroovuinoMLBinding)this.getBinding()).getVariable(sensor) : (PinSensor)sensor)
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
			def targetState = state2 instanceof String ? (State)((GroovuinoMLBinding)this.getBinding()).getVariable(state2) : (State)state2

			[when: { sensor ->
				if (sensor == "remote") {
					[receives: { key ->
						condition = ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createRemoteCondition(key as char)
						((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createTransition(state1Dup, targetState, condition)
					}]
				} else {
					[becomes: { signal ->
						SignalCondition signalCondition = new SignalCondition()
						signalCondition.setSensor(sensor instanceof String ? (PinSensor)((GroovuinoMLBinding)this.getBinding()).getVariable(sensor) : (PinSensor)sensor)
						signalCondition.setValue(signal instanceof String ? (SIGNAL)((GroovuinoMLBinding)this.getBinding()).getVariable(signal) : (SIGNAL)signal)
						condition = signalCondition

						def transition = ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createTransition(state1Dup, targetState, condition)

						[and: sensorHandlerAfterAnd, or: sensorHandlerAfterOr]
					}]
				}
			}]
		}]
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
