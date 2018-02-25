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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import org.jirduino.core.Signal;
import org.jirduino.settings.RemoteDefaultSettings;


public class PropertiesController implements ConfigController{
	
	private File configFile;
	
	private String configFolder=RemoteDefaultSettings.CONFIG_FOLDER;
	
	private String configSuffix=RemoteDefaultSettings.CONFIG_SUFFIX;
	
	private Properties properties;
	
	private InputStream is; 
	
	private OutputStream os; 
	
	//attributes
	
	/**
	 * protocol ID field
	 * */
	private int protocolId=0;
	
	/**
	 * command length field
	 * */
	private int cmdLength=32;
	
	/**
	 * protocol name field
	 * */
	private String protocolName="unknown";

	
	/**
	 * device name field
	 * */
	private String deviceName="unknown";
	

	/**
	 * Key name List
	 * */
	private LinkedList<String> keySet;
	
	
	/**
	 * Key press Hash Table
	 * */
	private Map<String, String> keyPressMap;
	
	
	/**
	 * Key down Hash Table
	 * */
	private Map<String, String> keyDownMap;
	
	
	/**
	 * Key release Hash Table
	 * */
	private Map<String, String> keyReleaseMap;
	
	
	
	public PropertiesController() {
		keyPressMap=new ConcurrentHashMap<String, String>();
		keyReleaseMap=new ConcurrentHashMap<String, String>();
		keyDownMap=new ConcurrentHashMap<String, String>();
		keySet=new LinkedList<String>();
		properties=new Properties();
	}
	

	public File getPropertiesFile() {
		return configFile;
	}


	public boolean isPresent(String keyName) {
		return keySet.contains(keyName);
	}


	@Override
	public LinkedList<String> getAvailableKeys() {
		return keySet;
	}

	/**
	 * return command length field (bits)
	 * */
	public int getCommandLength() {
		return cmdLength;
	}

	/**
	 * set command length field
	 * @param length new length value
	 * */
	public void setCommandLength(int length) {
		cmdLength = length;
	}
	
	
	/**
	 * Add keys to config
	 * @param keyName key name (i.e. POWER)
	 * @param valuePress hex value press command (i.e. E0E040BF)
	 * @param valueDown hex value down command
	 * @param valueRelease hex value release command
	 * */
	public Signal addKey(String keyName, String valuePress, String valueRelease, String valueDown) 
	{
		Signal keyPress=new Signal(protocolId, valuePress, cmdLength);
		Signal keyRelease=new Signal(protocolId, valueRelease, cmdLength);
		Signal keyDown=new Signal(protocolId, valueDown, cmdLength);
		
		return addKey(keyName, keyPress, keyRelease, keyDown);
	}
	
	
	/**
	 * Add keys to config
	 * @param keyName key name (i.e. POWER)
	 * @param keyPress signal press command (i.e. E0E040BF)
	 * @param keyDown signal down command
	 * @param keyRelease signal release command
	 * */
	public synchronized Signal addKey(String keyName, Signal keyPress, Signal keyRelease, Signal keyDown) 
	{
		if(!isPresent(keyName) && isProtocolOK(keyPress, keyDown, keyRelease)) 
		{
			keySet.add(keyName);
			keyPressMap.put(keyName, keyPress.getValue());
			keyDownMap.put(keyName, keyRelease.getValue());
			keyReleaseMap.put(keyName, keyDown.getValue());
			return keyPress;
		}
		return Signal.getEmptySignal();
		
	}

	
	@Override
	public Signal getKeyPress(String keyName) {
		if (isPresent(keyName))
			return new Signal(protocolId, keyPressMap.get(keyName), cmdLength);
		
		return Signal.getEmptySignal();
	}

	@Override
	public Signal getKeyDown(String keyName) {
		if (isPresent(keyName))
			return new Signal(protocolId, keyDownMap.get(keyName), cmdLength);
		
		return Signal.getEmptySignal();
	}

	@Override
	public Signal getKeyRelease(String keyName) {
		if (isPresent(keyName))
			return new Signal(protocolId, keyReleaseMap.get(keyName), cmdLength);
		
		return Signal.getEmptySignal();
	}

	@Override
	public Signal setKeyPress(String keyName, Signal key) {
		if (isPresent(keyName)) {
			keyPressMap.remove(keyName);
			keyPressMap.put(keyName, key.getValue());
			return new Signal(protocolId, keyPressMap.get(keyName), cmdLength);
		}
		return Signal.getEmptySignal();
		
	}

	@Override
	public Signal setKeyDown(String keyName, Signal key) {
		if (isPresent(keyName)) {
			keyDownMap.remove(keyName);
			keyDownMap.put(keyName, key.getValue());
			return new Signal(protocolId, keyDownMap.get(keyName), cmdLength);
		}
		return Signal.getEmptySignal();
	}

	@Override
	public Signal setKeyRelease(String keyName, Signal key) {
		if (isPresent(keyName)) {
			keyReleaseMap.remove(keyName);
			keyReleaseMap.put(keyName, key.getValue());
			return new Signal(protocolId, keyReleaseMap.get(keyName), cmdLength);
		}
		return Signal.getEmptySignal();
	}
	

	@Override
	public Signal removeKeyPress(String keyName) {
		if (isPresent(keyName)) 
		{
			String removed=keyPressMap.get(keyName);
			keyPressMap.remove(keyName);
			keyPressMap.put(keyName, "0");
			return new Signal(protocolId, removed, cmdLength);
		}
		
		return Signal.getEmptySignal();		
	}

	@Override
	public Signal removeKeyDown(String keyName) {
		if (isPresent(keyName)) 
		{
			String removed=keyDownMap.get(keyName);
			keyDownMap.remove(keyName);
			keyDownMap.put(keyName, "0");
			return new Signal(protocolId, removed, cmdLength);
		}
		
		return Signal.getEmptySignal();	
	}

	@Override
	public Signal removeKeyRelease(String keyName) {
		if (isPresent(keyName)) 
		{
			String removed=keyReleaseMap.get(keyName);
			keyReleaseMap.remove(keyName);
			keyReleaseMap.put(keyName, "0");
			return new Signal(protocolId, removed, cmdLength);
		}
		
		return Signal.getEmptySignal();	
	}


	/**
	 * get availables configs list. They are json files name
	 * into folder specified by RemoteDefaultSettings.JSON_FOLDER
	 * 
	 * */
	public static LinkedList<String> getConfigs() 
	{
		LinkedList<String> profiles=new LinkedList<String>();
		File folder = new File(RemoteDefaultSettings.JSON_FOLDER + "/");
		File[] listOfFiles = folder.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	       profiles.add(listOfFiles[i].getName().replace("."+RemoteDefaultSettings.JSON_SUFFIX, ""));
	      } 
	    }
	    
		return profiles;
	}
	
	
	@Override
	public String getDeviceName() {
		return this.deviceName;
	}


	@Override
	public String setDeviceName(String value) {
		this.deviceName=value;
		return value;
	}


	@Override
	public int getProtocolId() {
		return protocolId;
	}


	@Override
	public int setProtocolId(int value) {
		protocolId=value;
		return value;
	}


	@Override
	public String getProtocolName() {
		return protocolName;
	}


	@Override
	public String setProtocolName(String value) {
		this.protocolName=value;
		return value;
	}
	
	
	@Override
	public boolean load(String name) {	
		return loadProperties(name);
	}


	@Override
	public boolean save(String name) {
		configFile=new File(this.configFolder+name+"."+configSuffix);
		System.out.println(configFile);
		
		try {
			saveProperties();
			os=new FileOutputStream(configFile.getAbsolutePath());
			properties.store(os, "Controller Settings");
		} catch (IOException e) {
			return false;
		}
		return true;
	}


	/**
	 * Check if signals have the same protocol IDs and if it equals
	 * to current remote protocol
	 * @param keyPress key press signal
	 * @param keyDown key down signal
	 * @param keyRelease key release signal
	 * @return true if protocol is ok, false otherwise
	 * 
	 * */
	private boolean isProtocolOK(Signal keyPress, Signal keyDown, Signal keyRelease) 
	{
		boolean ok= keyPress.getProtocol()==protocolId;
		ok=ok && keyDown.getProtocol()==protocolId;
		ok=ok && keyRelease.getProtocol()==protocolId;
		
		return ok;
	}

	
	
	private boolean loadProperties(String configName) {
		configFile=new File(this.configFolder+configName+"."+configSuffix);
		try {
			is=new FileInputStream(configFile);
			properties.load(is);
			Enumeration<Object> en=properties.keys();
			while(en.hasMoreElements()) {
				String key=(String) en.nextElement();
				loadProperty(key);
				
				System.out.println(key);
			}
			is.close();
		} catch (IOException e) {
			return false;
		}
		
		return true;
	}
	
	private boolean saveProperties() {
		try {
			
			saveProperty("controller.device_name", deviceName);
			
			saveProperty("controller.protocol_id", protocolId);
			
			saveProperty("controller.protocol_name", protocolName);
			
			saveProperty("controller.cmd_len", cmdLength);
			
			savePropertySet(keyPressMap, keySet,"controller.key_press.");
			
			savePropertySet(keyReleaseMap, keySet, "controller.key_release.");
			
			savePropertySet(keyDownMap, keySet, "controller.key_down.");
			
		} catch (IOException e) {
			return false;
		}
		
		return true;

	}
	
	
	
	private void loadProperty(String key) {
		
		if (key.contains("controller.key_press")) {
			keyPressMap.put(key.replace("controller.key_press.", ""), properties.getProperty(key,"0"));
		}else if (key.contains("controller.key_release")) {
			keyReleaseMap.put(key.replace("controller.key_release.", ""), properties.getProperty(key,"0"));
		}else if (key.contains("controller.key_down")) {
			keyDownMap.put(key.replace("controller.key_down.", ""), properties.getProperty(key,"0"));
		}else if(key.equalsIgnoreCase("controller.protocol_id")) {
			protocolId=Integer.valueOf(properties.getProperty(key,"0"));
		}else if(key.equalsIgnoreCase("controller.protocol_name")) {
			protocolName=properties.getProperty(key,"Unknown");
		}else if(key.equalsIgnoreCase("controller.device_name")) {
			deviceName=properties.getProperty(key,"Unknown");
		}else if(key.equalsIgnoreCase("controller.cmd_len")) {
			cmdLength=Integer.valueOf(properties.getProperty(key,"32"));
		}
		
	}
	
	
	private void saveProperty(String key, String value) throws IOException {
		properties.setProperty(key, value);
		
	}
	
	private void saveProperty(String key, int value) throws IOException {
		saveProperty(key, String.valueOf(value));
	}
	
	private void savePropertySet(final Map<String, String> map, LinkedList<String> keyList, String prefix, String suffix) throws IOException {
				
		for(String key: keyList) 
			saveProperty(prefix+key+suffix, map.get(key));
	}
	
	private void savePropertySet(final Map<String, String> map, LinkedList<String> keyList, String prefix) throws IOException {
		savePropertySet(map, keyList, prefix, "");
	}
	
	
	public static void main(String[] args) {
		PropertiesController settings=new PropertiesController();
		settings.addKey("power", "FFF", "EEE", "000");
		settings.save("test");
	}


	@Override
	public boolean hasKey(String key) {
		return this.isPresent(key);
	}
	
}
