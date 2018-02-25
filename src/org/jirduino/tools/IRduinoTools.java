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
	
	public static String version="0.9.5";
	
	public static void main(String[] args) {
		if (args.length>0) {
			
			if(args[0].equalsIgnoreCase("sniff")) 
				IRSerialSnifferTools.runSniffer(args);
			
			
			else if(args[0].equalsIgnoreCase("send"))
				IRSenderTools.runSender(args);
			
			else if(args[0].equalsIgnoreCase("generate-json"))
				JSONGeneratorTools.runGenerator(args);
			
			else if(args[0].equalsIgnoreCase("probe-json"))
				JSONDetectorTools.runDetector(args);
			
			else if(args[0].equalsIgnoreCase("convert-signal"))
				SignalConverterTools.runSignalConverter(args);
			
			else if(args[0].equalsIgnoreCase("show-protocols"))
				showProtocolList();
			
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
		System.out.println("> show-protocols 	 show supported protocols");
		System.out.println("> send 			 send IR command");
		System.out.println("> sniff 		 receive and print IR signals");
		System.out.println("> generate-json  	 generate json config for IR controller");
		System.out.println("> probe-json    	 probe exists json config, by key pressing");
		System.out.println("> convert-signal     	 convert single signal between different protocols");
		System.out.println("");
		System.out.println("Run java -jar irduino-tools.jar <command> for advanced help");
		System.out.println("");
		
		
	}

	public static void showProtocolList() {
		System.out.println("Supported IR Protocols: ");
		System.out.println("");
		System.out.println("id 0 for Unknown Protocol");
		System.out.println("id 1 for NEC");
		System.out.println("id 2 for Sony");
		System.out.println("id 3 for RC5");
		System.out.println("id 4 for RC6");
		System.out.println("id 5 for PANASONIC_OLD");
		System.out.println("id 6 for JVC");
		System.out.println("id 7 for NECx");
		System.out.println("id 8 for SAMSUNG36");
		System.out.println("id 9 for GICABLE");
		System.out.println("id 10 for DIRECT_TV");
		System.out.println("id 11 for RCMM");
		System.out.println("id 12 for CYKM");
		System.out.println("");
	}
	
}
