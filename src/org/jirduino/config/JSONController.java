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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jirduino.core.Signal;
import org.jirduino.helpers.JIRduinoHelper;
import org.jirduino.helpers.JSONConfigHelper;
import org.jirduino.helpers.LogEvent;
import org.jirduino.settings.RemoteDefaultSettings;
import org.jirduino.translators.SignalRule;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * Controller JSON Configuration Class, used for IR remote controllers configuration.
 * JSON files are stored into folder specified by RemoteDefaultSettings.JSON_FOLDER
 * (default: data/config/remotes)
 * 
 * @author Emanuele Paiano
 * 
 * */
public class JSONController implements ConfigController {

	/**
	 * JSON protocol ID field
	 * */
	private int protocolId;
	
	/**
	 * JSON command length field
	 * */
	private int cmdLength;
	
	/**
	 * JSON protocol name field
	 * */
	private String protocolName;

	
	/**
	 * JSON device name field
	 * */
	private String deviceName;

	/**
	 * profile name JSON
	 * */
	private String jsonName;


	/**
	 * JSON string dumped from file
	 * */
	private String json;

	/**
	 * JSON parser
	 * */
	private JSONParser parser;

	/**
	 * main json Object
	 * */
	private JSONObject jsonObject;

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

	public JSONController() {
		this("noname.json");
	}
	
	
	/**
	 * Constructor. 
	 * @param name profile name to load. Use static method getConfigs() to get
	 * available JSON config name.
	 * */
	public JSONController(String name) {
		
		keySet = new LinkedList<String>();
		keyPressMap=new HashMap<String, String>();
		keyDownMap=new HashMap<String, String>();		
		keyReleaseMap=new HashMap<String, String>();
		parser = new JSONParser();
		this.jsonName=name;	
	}

	@Override
	public String getDeviceName() {

		return this.deviceName;
	}

	@Override
	public String setDeviceName(String value) {
		this.deviceName = value;
		return deviceName;
	}

	@Override
	public int getProtocolId() {

		return this.protocolId;
	}
	
	public String getJSONProfile() {
		return jsonName;
	}

	@Override
	public int setProtocolId(int value) {
		this.protocolId = value;
		return value;
	}

	@Override
	public String getProtocolName() {

		return this.protocolName;
	}

	@Override
	public String setProtocolName(String value) {
		this.protocolName = value;
		return this.protocolName;
	}

	/**
	 * return JSON profile dumped from file.
	 * */
	public String getJson() {
		return json;
	}

	/**
	 * load JSON config specified in costructor.
	 * @return true if success, false otherwise
	 * */
	public boolean load() 
	{
		return load(jsonName);
	}

	/**
	 * load specified JSON config
	 * @param config name
	 * @return true if success, false otherwise
	 * */
	@Override
	public boolean load(String name) {
		jsonName=name;
		String filepath = RemoteDefaultSettings.JSON_FOLDER + "/"+ jsonName + "." + RemoteDefaultSettings.JSON_SUFFIX;
		try {
			jsonObject = (JSONObject) parser.parse(new FileReader(filepath));
			
			//load generic
			parseGenericJSON(jsonObject);
			
			//loading all keys
			parseKeysJSON(jsonObject);
					
			return true;
		} catch (IOException e) {
			LogEvent.JSONException(e);
			return false;
		}catch (ParseException e) {
			LogEvent.JSONException(e);
			return false;
		}

	}

	/**
	 * save current JSON config
	 * @param config name
	 * @return true if success, false otherwise
	 * */
	@Override
	public boolean save(String name) {
		jsonName=name;
		String filepath = RemoteDefaultSettings.JSON_FOLDER + "/"+ jsonName + "." + RemoteDefaultSettings.JSON_SUFFIX;		
		String output=JIRduinoHelper.JSONPrettyFormatObject(generateJSON());
		
		try (FileWriter file = new FileWriter(filepath)) {
			file.write(output);
			return true;
		} catch (IOException e) {
			LogEvent.JSONException(e);
		}
		
		return false;
	}

	/**
	 * save current JSON config, using specified name into constructor
	 * @return true if success, false otherwise
	 * */
	public boolean save() {
		return save(jsonName);
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
	 * @return keyPress signal
	 * */
	public Signal addKey(String keyName, Signal keyPress, Signal keyRelease, Signal keyDown) 
	{

		if (!keySet.contains(keyName))
			keySet.add(keyName);
		keyPressMap.put(keyName, keyPress.getValue());
		keyDownMap.put(keyName, keyRelease.getValue());
		keyReleaseMap.put(keyName, keyDown.getValue());
		return keyPress;
		
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
	
	
	/**
	 * parse generic attributes. called by load()
	 * */
	private void parseGenericJSON(JSONObject jsonObject) 
	{
		//loading properties object
		JSONObject generic=(JSONObject) jsonObject.get("generic");
		
		//load device name
		deviceName = (String) generic.get("device_name");
		
		//protocol name
		protocolName = (String) generic.get("protocol_name");
		
		//load Protocol ID
		Long tmp=((Long) generic.get("protocol_id"));
		protocolId = tmp != null ? tmp.intValue() : 0;
		
		//load Protocol ID
		Long tmp2=((Long) generic.get("cmd_len"));
		this.cmdLength = tmp2 != null ? tmp2.intValue() : 0;
	}

	/**
	 * parse keys. called by load()
	 * */
	private void parseKeysJSON(JSONObject jsonObject) {
		//loading properties object
		JSONObject keys=(JSONObject) jsonObject.get("keys");
		
		//load key set array
		JSONArray key_set = (JSONArray) keys.get("key_set");
		JSONObject keyPress=(JSONObject) keys.get("key_press");
		JSONObject keyDown=(JSONObject) keys.get("key_down");
		JSONObject keyRelease=(JSONObject) keys.get("key_release");
		
		@SuppressWarnings("unchecked")
		Iterator<String> iterator = key_set.iterator();
		
		while (iterator.hasNext()) {
			
			String currentKey=iterator.next();
			
			if (!keySet.contains(currentKey)) { 
				keySet.add(currentKey);
				keyPressMap.put(currentKey, (String) keyPress.get(currentKey));
				keyDownMap.put(currentKey, (String) keyDown.get(currentKey));
				keyReleaseMap.put(currentKey, (String) keyRelease.get(currentKey));
			}
		}
	}

	
	
	/**
	 * generate json output. called by save()
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String generateJSON() 
	{	    
	    Map main=new LinkedHashMap();
	    main.put("generic", generateJSONGeneric());
	    main.put("keys", generateJSONKeys());
	    StringWriter out = new StringWriter();
	
	    try {
			JSONValue.writeJSONString(main, out);
		} catch (IOException e) {
		}  
	    
	    json=out.toString();
	
	    return json;
	}

	
	/**
	 * generate json output for keys. called by save()
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map generateJSONKeys() {
		Map keys=new LinkedHashMap();
	    Map key_press=new LinkedHashMap();	    
	    Map key_down=new LinkedHashMap();	    
	    Map key_release=new LinkedHashMap();	    
	
	    JSONArray keySetArray = new JSONArray();
	     
	    for(int i=0;i<keySet.size();i++) 
	    {
	     keySetArray.add(keySet.get(i));
	    }
	    
	    for(int j=0;j<keyPressMap.size();j++) 
	    {
	     key_press.put(keySet.get(j), keyPressMap.get(keySet.get(j)));
	     key_down.put(keySet.get(j), keyDownMap.get(keySet.get(j)));
	     key_release.put(keySet.get(j), keyReleaseMap.get(keySet.get(j)));
	    }
	    
	    keys.put("key_set", keySetArray);
	    keys.put("key_press", key_press);
	    keys.put("key_release", key_release);
	    keys.put("key_down", key_down);
	    
	    return keys;
	}

	
	/**
	 * generate json output for generic. called by save()
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map generateJSONGeneric() {
		Map generic=new LinkedHashMap();
	    generic.put("device_name", deviceName);
	    generic.put("protocol_id", protocolId);
	    generic.put("protocol_name", protocolName);
	    generic.put("cmd_len", cmdLength);
	    
	    return generic;
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
	@SuppressWarnings("unused")
	private boolean isSameProtocol(Signal keyPress, Signal keyDown, Signal keyRelease) 
	{
		boolean ok= keyPress.getProtocol()==protocolId;
		ok=ok && keyDown.getProtocol()==protocolId;
		ok=ok && keyRelease.getProtocol()==protocolId;
		
		return ok;
	}


	@Override
	public boolean hasKey(String key) {
		return this.keySet.contains(key);
	}
	
	
	/**
	 * Probe configurations by inputs key, event and hex Value.
	 * @param key configurations key
	 * @param hexValue value to check
	 * @param event can be press, release or down
	 * @return JSONController list with configs
	 * 
	 * **/
	public static List<JSONController> probeConfigByKeyEvent(String key, String hexValue, String event) {
				
		return JSONConfigHelper.probeConfigByKeyEvent(key, hexValue, event);
	}
	
	
	/**
	 * Probe configurations  by inputs key event press and hex Value.
	 * @param key configurations key
	 * @param hexValue value to check
	 * @return JSONController list with configs
	 * 
	 * **/
	public static List<JSONController> probeConfigByKeyPress(String key, String hexValue) {
		return JSONConfigHelper.probeConfigByKeyEvent(key, hexValue, "press");
		
	}
	
	
	/**
	 * Probe configurations by inputs keyPress, event Press and hex Value.
	 * @param key configurations key
	 * @param signal signal to check
	 * @return JSONController list with configs
	 * 
	 * **/
	public static List<JSONController> probeConfigByKeyPress(String key, Signal signal) {
		return JSONConfigHelper.probeConfigByKeyEvent(key, signal.getValue(), "press");
		
	}
	
	
	/**
	 * Probe configurations by inputs key, event release and hex Value.
	 * @param key configurations key
	 * @param signal signal to check
	 * @return JSONController list with configs
	 * 
	 * **/
	public static List<JSONController> probeConfigByKeyRelease(String key, Signal signal) {
		return JSONConfigHelper.probeConfigByKeyEvent(key, signal.getValue(), "release");
		
	}
	
	
	/**
	 * Probe configurations by inputs key, event release and hex Value.
	 * @param key configurations key
	 * @param hexValue value to check
	 * @return JSONController list with configs
	 * 
	 * **/
	public static List<JSONController> probeConfigByKeyRelease(String key, String hexValue) {
		return JSONConfigHelper.probeConfigByKeyEvent(key, hexValue, "release");
		
	}
	
	
	/**
	 * Probe configurations by inputs key, event down and hex Value.
	 * @param key configurations key
	 * @param hexValue value to check
	 * @return JSONController list with configs
	 * 
	 * **/
	public static List<JSONController> probeConfigByKeyDown(String key, String hexValue) {
		return JSONConfigHelper.probeConfigByKeyEvent(key, hexValue, "down");
	}
	
	
	/**
	 * Probe configurations by inputs key, event down and hex Value.
	 * @param key configurations key
	 * @param signal signal to check
	 * @return JSONController list with configs
	 * 
	 * **/
	public static List<JSONController> probeConfigByKeyDown(String key, Signal signal) {
		return JSONConfigHelper.probeConfigByKeyEvent(key, signal.getValue(), "down");
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
