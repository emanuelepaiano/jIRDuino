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

import java.io.IOException;
import java.util.logging.Logger;

import org.jirduino.core.Signal;
import org.json.simple.parser.ParseException;

public class LogEvent {
	public static Logger logger=Logger.getGlobal();
	
	public static void receivedSignal(Signal signal) 
	{
		System.out.println("SUCCESS => Received: "+signal);
	}
	
	
	public static void resolvedRule(Signal in, Signal out) 
	{
		System.out.println("SUCCESS => "+in+" resolved to "+out);
	}
	
	public static void sentSignal(Signal signal) 
	{
		System.out.println("SUCCESS => Sent: "+signal);
	}
	
	public static void noRuleMatching(Signal in) 
	{
		System.out.println("FAIL: => No rule matching for "+in);
	}
	
	public static void JSONException(IOException e) 
	{
		System.out.println("FAIL: => I/O Error: "+e.getMessage());
	}
	
	public static void JSONException(ParseException e) 
	{
		System.out.println("FAIL: => Error while parsing: "+e);
	}

}
