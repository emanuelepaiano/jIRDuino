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

package org.jirduino.config;

import java.util.ArrayList;

import org.jirduino.core.Signal;
import org.jirduino.translators.SignalRule;

/**
 * Interface for Remote Configurations
 * @author Emanuele Paiano
 * 
 * */
public interface ConfigConverter {
	
	/**
	 * load config
	 * @param name config name
	 * @return true if success, false otherwise
	 * */
	public boolean load(String name); 
	
	
	/**
	 * save config
	 * @param name config name
	 * @return true if success, false otherwise
	 * */
	public boolean save(String name);
	
	
	/**
	 * return SignalRule list
	 * */
	public ArrayList<SignalRule> getRuleTable();

	
	/**
	 * set SignalRule list
	 * */
	public ArrayList<SignalRule> setRuleTable(ArrayList<SignalRule> ruleTable);
	
	/**
	 * return true of contains rule, false otherwise 
	 * @param rule rule to check
	 * */
	public boolean contains(SignalRule rule);
	
	/**
	 * return true of a rule contains a signal, false otherwise 
	 * @param in signal to check
	 * */
	public boolean contains(Signal in);
	
	

}
