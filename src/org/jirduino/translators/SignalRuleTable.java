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

import java.util.LinkedHashMap;
import java.util.Map;

import org.jirduino.config.ConfigController;
import org.jirduino.core.Signal;
import org.jirduino.helpers.JIRduinoHelper;


/**
 * Signals translation table. It will contain rules needed to translate
 * signal, stored into HashMap: input protocol and value are used to 
 * generate hash key for SignalRule Object.
 * @see SignalRule   
 * 
 * @author Emanuele Paiano
 * 
 * */
public class SignalRuleTable {
	
	private Map<String, SignalRule> rules;
	
	/**
	 * Constructor. 
	 * */
	public SignalRuleTable() 
	{
		rules=new LinkedHashMap<String, SignalRule>();
	}
	
	/**
	 * Get stored rule associated to input protocol and value. 
	 * @param inProtocol input signal protocol
	 * @param inValue input signal value
	 * @return SignalRule object
	 * */
	public SignalRule getRule(int inProtocol, String inValue) 
	{
		return getRule(inProtocol+inValue);	
	}
	
	
	/**
	 * Get stored rule associated to input protocol and value.
	 * @param key key to recover rules from hashmap 
	 * @return SignalRule object
	 * */
	public SignalRule getRule(String key) 
	{
		if (rules.containsKey(key))
			return rules.get(key);
		else
			return SignalRule.getEmptySignalRule();
		
	}	
	
	/**
	 * Get stored rule associated to input protocol and value.
	 * @param in input signal 
	 * @return SignalRule object
	 * */
	public SignalRule getRule(Signal in) 
	{
		return getRule(in.getProtocol()+in.getValue());		
	}	
	

	/**
	 * Add rule to table
	 * @param rule signal rule to store
	 * @return SignalRule object
	 * */
	public synchronized SignalRule addRule(SignalRule rule) 
	{
		if (!match(rule.getIn().getProtocol(),rule.getIn().getValue())) 
			return rules.put(rule.hashKey(), rule);
		else 
			return new SignalRule(Signal.getEmptySignal(), Signal.getEmptySignal(), false);
	}
	
	/**
	 * Add rule to table
	 * @param in input signal to translate (i.e. new Signal(Protocols.NEC, "FFEA15", 32))
	 * @param out translated signal (i.e. new Signal(Protocols.NECx, "E0E040BF", 32))
	 * @return SignalRule object
	 * */
	public synchronized SignalRule addRule(Signal in, Signal out) 
	{
		return addRule(new SignalRule(in, out));
	}
	
	
	/**
	 * Add a new rule to table. 
	 * @param in input signal to translate (i.e. new Signal(Protocols.NEC, "FFEA15", 32))
	 * @param out translated signal (i.e. new Signal(Protocols.NECx, "E0E040BF", 32))
	 * @param delaySend delay pause (in millis) before send translated signal
	 * @see SignalRuleTable
	 * 
	 * */
	public synchronized SignalRule addRule(Signal in, Signal out, long delaySend) 
	{
		return addRule(new SignalRule(in, out, delaySend));
	}
	
	
	/**
	 * Add a new rule to table. 
	 * @param in input signal to translate (i.e. new Signal(Protocols.NEC, "FFEA15", 32))
	 * @param out translated signal (i.e. new Signal(Protocols.NECx, "E0E040BF", 32))
	 * @param enabled if true, translated signal will be sent by Arduino Pin 3, 
	 * otherwise signal won't be send  
	 * @see SignalRuleTable
	 * 
	 * */
	public synchronized SignalRule addRule(Signal in, Signal out, boolean enabled) 
	{
		return addRule(new SignalRule(in, out, enabled));
	}
	
	
	/**
	 * Add a new rule to protocol table. 
	 * @param in input signal to translate i.e. new Signal(Protocols.NEC, "FFEA15", 32))
	 * @param out translated signal (i.e. new Signal(Protocols.NECx, "E0E040BF", 32))
	 * @param delaySend delay pause (in millis) before send translated signal
	 * @param enabled if true, translated signal will be sent by Arduino Pin 3, 
	 * otherwise signal won't be send
	 * @see SignalRuleTable
	 * 
	 * */
	public synchronized SignalRule addRule(Signal in, Signal out, long delaySend, boolean enabled) 
	{
		return addRule(new SignalRule(in, out, delaySend, enabled));
	}
	
	
	/**
	 * Check if rule exists, using input signal protocol and value. 
	 * @param rule to check
	 * @return true if exists, false otherwise
	 * 
	 * */
	public synchronized boolean match(SignalRule rule) 
	{
		return rules.containsKey(rule.hashKey());
	}
	
	
	/**
	 * Check if rule exists, using input signal protocol and value. 
	 * @param in input signal 
	 * @return true if exists, false otherwise
	 * 
	 * */
	public boolean match(Signal in) 
	{
		return rules.containsKey(in.getProtocol()+in.getValue());
	}
	
	
	/**
	 * Check if rule exists, using input signal protocol and value. 
	 * @param inProtocol input signal protocol
	 * @param inValue input signal value  
	 * @return true if exists, false otherwise
	 * 
	 * */
	public boolean match(int inProtocol, String inValue) 
	{
		return rules.containsKey(inProtocol+inValue);
	}
	
	/**
	 * Remove rule containing given protocol and value
	 * @param inProtocol input signal protocol
	 * @param inValue input signal value  
	 * @return removed rule
	 * 
	 * */
	public synchronized SignalRule removeRule(int inProtocol, String inValue) 
	{
		if(this.match(inProtocol, inValue)) 
			return rules.remove(inProtocol+inValue);
		
		return SignalRule.getEmptySignalRule();
	}
	
	
	/**
	 * Remove rule containing given protocol and value
	 * @param in signal contained into remove-candidate rule  
	 * @return removed rule
	 * 
	 * */
	public synchronized SignalRule removeRule(Signal in) 
	{
		return removeRule(in.getProtocol(), in.getValue());
	}
	
	
	/**
	 * Generate Rule Table from input config remotes. For each key into target controller
	 * it associate same source controller key. All keys commons will be translated.
	 * @param source input config controller
	 * @param target output config controllers
	 * @param delaySend delay sleeping time before send translated signal
	 * @return table containing translations key 
	 * 
	 * */
	public static SignalRuleTable makeRuleTable(ConfigController source, ConfigController target, int delaySend) {
		return JIRduinoHelper.makeRuleTable(source, target, delaySend);
	}
	

}
