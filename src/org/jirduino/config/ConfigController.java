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

import java.util.LinkedList;

import org.jirduino.core.Signal;


/**
 * Interface for Remote Configurations
 * @author Emanuele Paiano
 * 
 * */
public interface ConfigController {
	
	/**
	 * return hex value while key pressing
	 * @param keyName key's name (i.e. POWER)
	 * @return key Signal associated keyName 
	 * */
	public Signal getKeyPress(String keyName);
	
	/**
	 * return hex value while key is down
	 * @param keyName key's name (i.e. POWER)
	 * @return key Signal associated keyName 
	 * */
	public Signal getKeyDown(String keyName);
	
	
	/**
	 * return hex value while releasing key
	 * @param keyName key's name (i.e. POWER)
	 * @return key Signal associated keyName 
	 * */
	public Signal getKeyRelease(String keyName);
	
	
	/**
	 * return all keys's name 
	 * */
	public LinkedList<String> getAvailableKeys();
	
	
	/**
	 * replace exiting key press
	 * @param keyName key's name
	 * @param key new key signal 
	 * */
	public Signal setKeyPress(String keyName, Signal key);
	
	/**
	 * replace exiting key down
	 * @param keyName key's name
	 * @param key new key signal 
	 * */
	public Signal setKeyDown(String keyName, Signal key);
	
	
	/**
	 * replace exiting key release
	 * @param keyName key's name
	 * @param key new key signal 
	 * */
	public Signal setKeyRelease(String keyName, Signal key);
	
	/**
	 * return device's name
	 * */
	public String getDeviceName();
	
	
	/**
	 * edit device's name
	 * @param value new name
	 * */
	public String setDeviceName(String value);
	
	
	/**
	 * return protocol number
	 * */
	public int getProtocolId();
	
	/**
	 * set protocol number
	 * */
	public int setProtocolId(int value);
	
	
	/**
	 * return protocol's name
	 * */
	public String getProtocolName();
	
	
	/**
	 * set protocol's name
	 * @param value new name
	 * */
	public String setProtocolName(String value);
	
	/**
	 * add a key press value
	 * @param keyName key's name (i.e. POWER)
	 * @param key key signal
	 * */
	public Signal addKey(String keyName, Signal keyPress, Signal keyRelease, Signal keyDown);
	
	
	/**
	 * remove a key press value
	 * @param keyName key's name (i.e. POWER)
	 * */
	public Signal removeKeyPress(String keyName);
	
	/**
	 * remove a key down value
	 * @param keyName key's name (i.e. POWER)
	 * */
	public Signal removeKeyDown(String keyName);
	
	
	/**
	 * remove a key release value
	 * @param keyName key's name (i.e. POWER)
	 * */
	public Signal removeKeyRelease(String keyName); 
	
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
	 * check if is present a key
	 * @param key key to check
	 * @return true if success, false otherwise
	 * */
	public boolean hasKey(String key);
	

}
