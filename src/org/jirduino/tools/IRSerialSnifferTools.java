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
import org.jirduino.drivers.IRLib2Device;
import org.serialduino.drivers.ComLinkDevice;


/**
 * Trivial jIRDuino Sniffer. Print received HEX codes and protocol info
 * 
 * @author Emanuele Paiano
 * */
public class IRSerialSnifferTools {
	
	public static void runSniffer(String[] args){
		if (args.length>1) {
			ComLinkDevice port=new ComLinkDevice(ComLinkDevice.getPortByName(args[1]), ComLinkDevice.BAUDRATE_9600);
			IRLib2Device ir=new IRLib2Device(port);
			ir.init();
			ir.setPassiveSnifferMode(true);
			ir.setReceiverEnabled(true);
			while(true)
				System.out.println(ir.waitResponse());
			
		}else
			printHelp();
	}
	
	public static void printHelp() {
		System.out.println("");
		System.out.println("Use: java -jar irduino-tools.jar sniff <port>");
		System.out.println("");
	}
	

}
