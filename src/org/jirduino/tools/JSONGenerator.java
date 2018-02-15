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

package org.jirduino.tools;

import java.util.LinkedList;

import org.jirduino.config.JSONController;
import org.jirduino.core.Signal;
import org.jirduino.drivers.IRDevice;
import org.jirduino.drivers.IRLib2Device;
import org.jirduino.settings.RemoteDefaultSettings;
import org.serialduino.drivers.ComLinkDevice;

public class JSONGenerator {
	
	public volatile static int protocol=0;
	
	public static IRDevice ir;
	
	public static void runGenerator(String[] args) {
		
		if (args.length>2) {
			ComLinkDevice port=new ComLinkDevice(ComLinkDevice.getPortByName(args[1]), ComLinkDevice.BAUDRATE_9600);
			
			ir=new IRLib2Device(port);
			
			ir.init();
			
			ir.setReceiverEnabled(true);
			
			JSONController controller=new JSONController();
			
			controller.load("templates/new");
			
			generateKeyPress(controller);
			
			if(controller.save(args[0]))
				System.out.println("Configuration written to "+
			RemoteDefaultSettings.JSON_FOLDER+"/"+args[2]+"."+RemoteDefaultSettings.JSON_SUFFIX);
			
		}else printHelp();
		
		
	}
	
	
	private static void generateKeyPress(JSONController controller) {
		
		LinkedList<String> keys=controller.getAvailableKeys();
		
		for(String key: keys) {
			
			System.out.println("=> Push button on remote for key "+key+"...");
			Signal in=ir.receiveData();
			Signal empty=Signal.getEmptySignal();
			controller.addKey(key, in, empty, empty);
			System.out.println("=> ["+key+": "+in.getValue()+", PROTOCOL: "+in.getProtocol()+"]");
			
			if (protocol==0) 
				protocol=in.getProtocol();			
		}
		
		controller.setProtocolId(protocol);
			
	}
	
	
	public static void printHelp() 
	{
		System.out.println("");
		System.out.println("Use: java -jar irduino-tools.jar generate-json <port> <output_name>");
		System.out.println("");	
	}
	
	

}
