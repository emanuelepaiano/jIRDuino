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
import java.util.List;

import org.jirduino.config.JSONController;
import org.jirduino.drivers.IRDevice;
import org.jirduino.drivers.IRLib2Device;
import org.serialduino.drivers.ComLinkDevice;

public class JSONDetector {
	
	public static void runDetector(String[] args) {
		List<JSONController> devices;
		
		if (args.length==3) {
		
			ComLinkDevice port=new ComLinkDevice(ComLinkDevice.getPortByName(args[1]), ComLinkDevice.BAUDRATE_9600);
			
			IRDevice ir=new IRLib2Device(port);
			
			ir.init();
			
			ir.setReceiverEnabled(true);
			
			System.out.println("Press "+args[2].toUpperCase()+" key on remote controller..");
			
			devices=JSONController.probeConfigByKeyPress(args[2].toUpperCase(), ir.receiveData());
			
			if(devices.size()==0) {
				System.out.println("Configuration not found for key "+args[2].toUpperCase());
				return;
			}else {
				for (int i=0;i<devices.size();i++) {
					System.out.println("JSON device(s) found. Profile List: ");
					System.out.println("");
					System.out.print((i+1)+") Device: "+devices.get(0).getDeviceName());
					System.out.print(" ==> Name: ");
					System.out.print(devices.get(0).getJSONProfile());
					System.out.println("\n");
				}
			}
		}else
			printHelp();
		
		
		
	}
	
	
	public static void printHelp() 
	{
		System.out.println("");
		System.out.println("Use: java -jar irduino-tools.jar probe-json <port> <key_name>");
		System.out.println("");
		System.out.println("Example: java -jar irduino-tools.jar probe-json /dev/ttyUSB0 POWER");
		System.out.println("");
		
	}

}
