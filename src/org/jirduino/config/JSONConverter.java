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


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jirduino.core.Signal;
import org.jirduino.helpers.JIRduinoHelper;
import org.jirduino.helpers.JSONConfigHelper;
import org.jirduino.helpers.LogEvent;
import org.jirduino.settings.ConverterDefaultSettings;
import org.jirduino.translators.SignalRule;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * Converter JSON Configuration Class, useful SignalRule table configuration storage.
 * JSON files are stored into folder specified by ConverterDefaultSettings.JSON_FOLDER
 * (default: data/json/convertes)
 * 
 * @author Emanuele Paiano
 * 
 * */
public class JSONConverter implements ConfigConverter{

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
	private Object jsonObject;
	
	
	private ArrayList<SignalRule> rules;


	/**
	 * Constructor.
	 * @param name profile name (default "new_converter")
	 * 
	 * */
	public JSONConverter(String name) {
		rules=new ArrayList<SignalRule>();
		parser = new JSONParser();
		this.jsonName=name;
	}
	
	
	/**
	 * Constructor.
	 * @param name profile name
	 * 
	 * */
	public JSONConverter() {
		this("new_converter");
	}


	/**
	 * load specified JSON config
	 * @param config name
	 * @return true if success, false otherwise
	 * */
	@Override
	public boolean load(String config) {
		jsonName=config;
		String filepath = ConverterDefaultSettings.JSON_FOLDER + "/"+ jsonName + "." + ConverterDefaultSettings.JSON_SUFFIX;
		try {
			jsonObject = (JSONArray) parser.parse(new FileReader(filepath));
			
			//load generic
			parseJSON(jsonObject);
								
			return true;
		} catch (IOException e) {
			LogEvent.JSONException(e);
			return false;
		}catch (ParseException e) {
			LogEvent.JSONException(e);
			return false;
		}

	}

	
	@Override
	public boolean save(String name) {
		this.jsonName=name;
		String filepath = ConverterDefaultSettings.JSON_FOLDER + "/"+ jsonName + "." + ConverterDefaultSettings.JSON_SUFFIX;		
		String output=JIRduinoHelper.JSONPrettyFormatArray(generateJSON());
		this.json=output;
		try (FileWriter file = new FileWriter(filepath)) {
			file.write(output);
			return true;
		} catch (IOException e) {
			LogEvent.JSONException(e);
		}
		
		return false;
	}
	
	
	@SuppressWarnings("unchecked")
	private String generateJSON() {
		JSONArray rulesJSON = new JSONArray();
		
		
		for(SignalRule sr: rules)
			rulesJSON.add(generateJSONRule(sr));
		
	    StringWriter out = new StringWriter();
	
	    try {
			JSONValue.writeJSONString(rulesJSON, out);
		} catch (IOException e) {
		}  
	    
	    json=out.toString();
	
	    return json;
		
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map generateJSONRule(SignalRule rule) {
		
		Map main=new LinkedHashMap();
	    main.put("signal_in", generateJSONSignal(rule.getIn()));
	    main.put("signal_out", generateJSONSignal(rule.getOut()));
	    main.put("delay", rule.getDelaySend());
	    main.put("enabled", rule.isEnabled());
	    
	    return main;
		
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map generateJSONSignal(Signal signal) {
		
		Map js=new LinkedHashMap();
	    js.put("protocol_id", signal.getProtocol());
	    js.put("value", signal.getValue());
	    
	    return js;
		
	}
	

	/**
	 * save current profile
	 * */
	public boolean save() {
		return save(jsonName);
	}

	@Override
	public boolean contains(Signal in) {
		for(SignalRule r: rules)
			if(r.getIn().isEquals(in))
				return true;
		return false;
	}


	@Override
	public boolean contains(SignalRule rule) {
		return rules.contains(rule);
	}


	/**
	 * Return rules list from cache
	 * */
	public ArrayList<SignalRule> getRuleTable() {
		return rules;
	}

	
	/**
	 * Set new rule table
	 * @param ruleTable rule list
	 * */
	
	public ArrayList<SignalRule> setRuleTable(ArrayList<SignalRule> ruleTable) {
		rules = ruleTable;
		return rules;
	}


	/**
	 * Return JSON file-dumped source 
	 * 
	 * */
	public String getJSONCode() {
		return json;
	}

	
	/**
	 * Return JSON profile name
	 * 
	 * */
	public String getProfileName() {
		return jsonName;
	}


	/**
	 * Set JSON profile name
	 * @param name profile name
	 * 
	 * */
	public void setProfileName(String name) {
		this.jsonName = name;
	}
	
	
	private SignalRule parseJSONSignalRule(JSONObject rule) {
		
		return JSONConfigHelper.parseJSONSignalRule(rule);
	}
	
	
	/**
	 * parse generic attributes. called by load()
	 * */
	private void parseJSON(Object jsonObject) 
	{
		JSONArray mainList=((JSONArray) jsonObject);
		
		for(Object current: mainList) {
			
			SignalRule rule=parseJSONSignalRule((JSONObject) current);
			
			if(!rules.contains(rule))
				rules.add(rule);
		
		}
		
		this.json=mainList.toJSONString();
	
	}
		

}
