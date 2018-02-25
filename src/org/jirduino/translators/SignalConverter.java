/**
 * Copyright 2017 Emanuele Paiano
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * */

package org.jirduino.translators;


import org.jirduino.core.Signal;
import org.jirduino.drivers.IRDevice;
import org.jirduino.helpers.LogEvent;


/**
 * IR Signal Converter thread. It converts received signals between
 * protocols, reading rules from Signal-Rules Table.
 * from a protocol table.
 * @see SignalRuleTable
 * 
 * @author Emanuele Paiano
 * 
 * */
public class SignalConverter implements Runnable{
	
	
	/**
	 * Translation rules table
	 * */
	private SignalRuleTable ruleTable;
	
	/**
	 * true if running
	 * */
	private volatile boolean isRunning=false;
	
	
	/**
	 * IRDuino Device
	 * */
	private IRDevice irDevice;
	
	
	/**
	 * if on all messages will be printed on Java Console
	 * */
	private volatile boolean debugModeOn=false;
	
	/**
	 * Constructor. 
	 * @param RuleTable a SignalRuleTables object (i.e. new SignalRuleTable())
	 * @param irDevice IRDevice object
	 * */
	public SignalConverter(SignalRuleTable RuleTable, IRDevice irDevice) 
	{
		this.ruleTable=RuleTable;
		this.irDevice=irDevice;
		
	}
	
	/**
	 * Return Rules Table
	 * */
	public SignalRuleTable getRuleTable() {
		return ruleTable;
	}

	/**
	 * Return IRDevice object
	 * */
	public IRDevice getIrDevice() {
		return irDevice;
	}
	
	
	/**
	 * Add a new rule to protocol table. 
	 * @param in input signal to translate (i.e. new Signal(Protocols.NEC, "FFEA15", 32))
	 * @param out translated signal (i.e. new Signal(Protocols.NECx, "E0E040BF", 32))
	 * @see SignalRuleTable
	 * 
	 * */
	public synchronized SignalRule addRule(Signal in, Signal out) 
	{
		return this.ruleTable.addRule(in, out);
	}
	
	/**
	 * Add a new rule to protocol table. 
	 * @param in input signal to translate (i.e. new Signal(Protocols.NEC, "FFEA15", 32))
	 * @param out translated signal (i.e. new Signal(Protocols.NECx, "E0E040BF", 32))
	 * @param delaySend delay pause (in millis) before send translated signal
	 * @param enabled if true, translated signal will be sent by Arduino Pin 3, 
	 * otherwise signal won't be send
	 * @see SignalRuleTable
	 * 
	 * */
	public synchronized SignalRule addRule(Signal in, Signal out, long delaySend, boolean enabled) 
	{
		return this.ruleTable.addRule(in, out, delaySend, enabled);
	}
	
	/**
	 * Add a new rule to protocol table. 
	 * @param in input signal to translate (i.e. new Signal(Protocols.NEC, "FFEA15", 32))
	 * @param out translated signal (i.e. new Signal(Protocols.NECx, "E0E040BF", 32))
	 * @param delaySend delay pause (in millis) before send translated signal
	 * @see SignalRuleTable
	 * 
	 * */
	public synchronized SignalRule addRule(Signal in, Signal out, long delaySend) 
	{
		return this.ruleTable.addRule(in, out, delaySend);
	}
	
	/**
	 * Add a new rule to protocol table. 
	 * @param in input signal to translate (i.e. new Signal(Protocols.NEC, "FFEA15", 32))
	 * @param out translated signal (i.e. new Signal(Protocols.NECx, "E0E040BF", 32))
	 * @param enabled if true, translated signal will be sent by Arduino Pin 3, 
	 * otherwise signal won't be send  
	 * @see SignalRuleTable
	 * 
	 * */
	public synchronized SignalRule addRule(Signal in, Signal out, boolean enabled) 
	{
		return this.ruleTable.addRule(in, out, enabled);
	}
	
	/**
	 * get output code.
	 * @param in input signal 
	 * @return output signal if exists in the table, empty signal if otherwise
	 * 
	 * */
	public Signal resolve(Signal in) 
	{
		return ruleTable.getRule(in.getProtocol(), in.getValue()).getOut();
	}
	
	
	
	/**
	 * Return true if VERBOSE/DEBUG mode is on, false otherwise
	 * 
	 * */
	public boolean isDebugModeOn() {
		return debugModeOn;
	}
	
	/**
	 * 
	 * Enable VERBOSE/DEBUG mode. Enabling this mode, you can see 
	 * log messages con Console.
	 * 
	 * @param debugModeOn true to enable, false to disable
	 * 
	 * */
	public synchronized void setDebugModeOn(boolean debugModeOn) {
		this.debugModeOn = debugModeOn;
	}

	@Override
	public void run() {
		isRunning=true;
		
		irDevice.setReceiverEnabled(true);
		
		while(isRunning) 
		{
			if(!irDevice.isPassiveSnifferMode()) 
			{				
				try {
					Signal in=irDevice.receiveData();
					
					if (debugModeOn)		
						LogEvent.receivedSignal(in);
					
					translateAndSend(in);
					
				} catch (InterruptedException e) {
				}
				
			}else
				System.out.println(irDevice.waitResponse());
		}
		
	}
	
	
	/**
	 * Translated and send routine
	 * 
	 * */
	private void translateAndSend(Signal in) throws InterruptedException 
	{
		
		if(ruleTable.match(in)) {
			Thread.sleep(300);
			
			SignalRule rule=ruleTable.getRule(in);
			Signal out=rule.getOut();
			
			if (debugModeOn)  
				LogEvent.resolvedRule(in, out);
			
			
			if(!out.isEquals(Signal.getEmptySignal()) && rule.isEnabled()) {
				
				Thread.sleep(rule.getDelaySend());
				
				irDevice.sendData(out);
				
				if (debugModeOn)  
					LogEvent.sentSignal(out);
				
			}
		}else 
			if (debugModeOn)
				LogEvent.noRuleMatching(in);
	}


}
