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

package org.jirduino.helpers;

import java.util.LinkedList;
import java.util.List;

import org.jirduino.config.JSONController;
import org.jirduino.core.Signal;
import org.jirduino.translators.SignalRule;
import org.json.simple.JSONObject;

public class JSONConfigHelper {
	
	/**
	 * Probe configurations by inputs key, event and hex Value.
	 * @param key configurations key
	 * @param hexValue value to check
	 * @param event can be press, release or down
	 * @return JSONController list with configs
	 * 
	 * **/
	public static List<JSONController> probeConfigByKeyEvent(String key, String hexValue, String event) {
		List<String> configList=JSONController.getConfigs();
		List<JSONController> keyPressList=new LinkedList<JSONController>();
		List<JSONController> keyReleaseList=new LinkedList<JSONController>();
		List<JSONController> keyDownList=new LinkedList<JSONController>();
		
		for(String current: configList) {
			JSONController controller=new JSONController(current);
			controller.load();
			if (controller.isPresent(key)) {
				if (event.equalsIgnoreCase("press")) {
					if(controller.getKeyPress(key).getValue().equals(hexValue))
						keyPressList.add(controller);
				}else if (event.equalsIgnoreCase("release")) {
					if(controller.getKeyRelease(key).getValue().equals(hexValue))
						keyReleaseList.add(controller);
				}
				else if (event.equalsIgnoreCase("down"))
					if(controller.getKeyDown(key).getValue().equals(hexValue))
							keyDownList.add(controller);
			}
				
		}
		
		if (event.equalsIgnoreCase("press"))
			return keyPressList;
		else if (event.equalsIgnoreCase("release"))
			return keyReleaseList;
		else if (event.equalsIgnoreCase("down"))
			return keyDownList;
		
		return new LinkedList<JSONController>();
	}
	
	
	/**
	 * Probe configurations  by inputs key event press and hex Value.
	 * @param key configurations key
	 * @param hexValue value to check
	 * @return JSONController list with configs
	 * 
	 * **/
	public static List<JSONController> probeConfigByKeyPress(String key, String hexValue) {
		return probeConfigByKeyEvent(key, hexValue, "press");
		
	}
	
	
	/**
	 * Probe configurations by inputs keyPress, event Press and hex Value.
	 * @param key configurations key
	 * @param signal signal to check
	 * @return JSONController list with configs
	 * 
	 * **/
	public static List<JSONController> probeConfigByKeyPress(String key, Signal signal) {
		return probeConfigByKeyEvent(key, signal.getValue(), "press");
		
	}
	
	
	/**
	 * Probe configurations by inputs key, event release and hex Value.
	 * @param key configurations key
	 * @param signal signal to check
	 * @return JSONController list with configs
	 * 
	 * **/
	public static List<JSONController> probeConfigByKeyRelease(String key, Signal signal) {
		return probeConfigByKeyEvent(key, signal.getValue(), "release");
		
	}
	
	
	/**
	 * Probe configurations by inputs key, event release and hex Value.
	 * @param key configurations key
	 * @param hexValue value to check
	 * @return JSONController list with configs
	 * 
	 * **/
	public static List<JSONController> probeConfigByKeyRelease(String key, String hexValue) {
		return probeConfigByKeyEvent(key, hexValue, "release");
		
	}
	
	
	/**
	 * Probe configurations by inputs key, event down and hex Value.
	 * @param key configurations key
	 * @param hexValue value to check
	 * @return JSONController list with configs
	 * 
	 * **/
	public static List<JSONController> probeConfigByKeyDown(String key, String hexValue) {
		return probeConfigByKeyEvent(key, hexValue, "down");
	}
	
	
	/**
	 * Probe configurations by inputs key, event down and hex Value.
	 * @param key configurations key
	 * @param signal signal to check
	 * @return JSONController list with configs
	 * 
	 * **/
	public static List<JSONController> probeConfigByKeyDown(String key, Signal signal) {
		return probeConfigByKeyEvent(key, signal.getValue(), "down");
	}
	
	
public static Signal parseJSONSignal(JSONObject signal) {
		
		if(signal.containsKey("protocol_id") && signal.containsKey("value")) {
			
			//load Protocol ID
			Long tmp=((Long) signal.get("protocol_id"));
			int id = tmp != null ? tmp.intValue() : 0;
			
			Long tmp2=((Long) signal.get("cmd_len"));
			int cmdLength = tmp != null ? tmp2.intValue() : 0;
			
			
			String value=(String) signal.get("value");
			return new Signal(id, value, cmdLength);
		}
		
		return Signal.getEmptySignal();
	}
	
	
	public static SignalRule parseJSONSignalRule(JSONObject rule) {
		
		if(rule.containsKey("signal_in") && rule.containsKey("signal_out") 
				&& rule.containsKey("delay") && rule.containsKey("enabled"))
		{
			Signal signalIn=parseJSONSignal((JSONObject)rule.get("signal_in"));
			Signal signalOut=parseJSONSignal((JSONObject)rule.get("signal_out"));
			long delay=(Long)rule.get("delay");
			boolean enabled=(boolean)rule.get("enabled");
			
			return new SignalRule(signalIn, signalOut, delay, enabled);
		}
		
		return SignalRule.getEmptySignalRule();
	}
	

}
