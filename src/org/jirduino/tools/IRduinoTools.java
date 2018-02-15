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

public class IRduinoTools {
	
	public static String version="0.9";
	
	public static void main(String[] args) {
		if (args.length>0) {
			
			if(args[0].equalsIgnoreCase("sniff")) 
				IRSerialSniffer.runSniffer(args);
			
			
			else if(args[0].equalsIgnoreCase("send"))
				IRSender.runSender(args);
			
			else if(args[0].equalsIgnoreCase("generate-json"))
				JSONGenerator.runGenerator(args);
			
			else if(args[0].equalsIgnoreCase("probe-json"))
				JSONDetector.runDetector(args);
			else
				System.out.println("irduino: Unknown command");
			
			return;
		}
		
		printHelp();
		
	}

	
	public static void printHelp() {
		System.out.println("-------------------------");
		System.out.println(" IRDuino Tools "+version);
		System.out.println("-------------------------");
		System.out.println("");
		System.out.println("Use: java -jar irduino-tools.jar <command> <port> [parametres]");
		System.out.println("");
		System.out.println("Available commands:");
		System.out.println("> send 			 send IR command");
		System.out.println("> sniff 		 receive and print IR signals");
		System.out.println("> generate-json  	 generate json config for IR controller");
		System.out.println("> probe-json    	 probe exists json config, by key pressing");
		System.out.println("");
		System.out.println("Run java -jar irduino-tools.jar <command> for advanced help");
		System.out.println("");
		
		
	}

}
