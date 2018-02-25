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

import java.math.BigInteger;
import java.util.List;

import org.jirduino.config.ConfigController;
import org.jirduino.translators.SignalRule;
import org.jirduino.translators.SignalRuleTable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class JIRduinoHelper {
	
	/**
	 * Convert HEX String to decimal long integer
	 * */
	public static long hex2decimal(String s) {
		return Long.parseLong(new BigInteger(s, 16).toString());
    }
	
	
	/**
	 * Extract HEX value from full response (waitResponse() output)
	 * */
	public static String getHexValue(String response) 
	{	
		if(response.contains(",")) {
			return response.split(",")[0];
		}
			return "";
		
	}
		
	/**
	 * Extract PROTOCOL value from full response (waitResponse() output)
	 * */
	public static String getProtocolID(String response) 
	{	
		if(response.contains(",")) {
			return response.split(",")[1];
		}
		return "";
		
	}
	
	/**
	 * Extract PROTOCOL bits from full response (waitResponse() output)
	 * */
	public static String getProtocolBits(String response) 
	{	 
		if(response.contains(",")) {
			return response.split(",")[2];
		}
		return "";
	}
	
	/**
	 * get formatted JSON String
	 * @param input unformatted JSON string
	 * @return readable JSON string
	 * */
	public static String JSONPrettyFormatArray(String input) {
		  JsonParser parser = new JsonParser();
	      JsonArray json = parser.parse(input).getAsJsonArray();

	      Gson gson = new GsonBuilder().setPrettyPrinting().create();
	      String prettyJson = gson.toJson(json);

	      return prettyJson;
	}
	
	
	/**
	 * get formatted JSON String
	 * @param input unformatted JSON string
	 * @return readable JSON string
	 * */
	public static String JSONPrettyFormatObject(String input) {
		  JsonParser parser = new JsonParser();
	      JsonObject json = parser.parse(input).getAsJsonObject();

	      Gson gson = new GsonBuilder().setPrettyPrinting().create();
	      String prettyJson = gson.toJson(json);

	      return prettyJson;
	}
	
	
	/**
	 * Generate Rule Table from input config remotes. For each key into target controller
	 * it associate same source controller key. All keys commons will be translated.
	 * @param source input config controller
	 * @param target output config controllers
	 * @param delaySend delay sleeping time before send target signal
	 * @return table containing translations key 
	 * 
	 * */
	public static SignalRuleTable makeRuleTable(ConfigController source, ConfigController target, int delaySend) {
		SignalRuleTable ruleTable=new SignalRuleTable();
		List<String> targetKeys=target.getAvailableKeys();
		
		for(String key: targetKeys) {
			if (source.hasKey(key)) {
				ruleTable.addRule(new SignalRule(source.getKeyPress(key), target.getKeyPress(key), delaySend, true));				
			}
		}
		
		return ruleTable;
		
	}
	
	
}
