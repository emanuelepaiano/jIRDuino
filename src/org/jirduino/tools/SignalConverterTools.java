package org.jirduino.tools;

import org.jirduino.core.Signal;
import org.jirduino.drivers.IRDevice;
import org.jirduino.drivers.IRLib2Device;
import org.jirduino.translators.SignalConverter;
import org.jirduino.translators.SignalRuleTable;
import org.serialduino.drivers.ComLinkDevice;

public class SignalConverterTools {
	public static void runSignalConverter(String[] args){
		if (args.length>7) {
			ComLinkDevice port=new ComLinkDevice(ComLinkDevice.getPortByName(args[1]), ComLinkDevice.BAUDRATE_9600);
			IRDevice ir=new IRLib2Device(port);
			ir.init();
			SignalConverter converter=new SignalConverter(new SignalRuleTable(),ir);
			Signal source=new Signal(Integer.parseInt(args[2]), args[3], Integer.parseInt(args[4]));
			Signal target=new Signal(Integer.parseInt(args[5]), args[6], Integer.parseInt(args[7]));
			converter.addRule(source, target);
			converter.setDebugModeOn(true);
			Thread T1=new Thread(converter);
			T1.start();
			
		}else
			printHelp();
	}
	
	public static void printHelp() {
		System.out.println("");
		System.out.println("Use: java -jar irduino-tools.jar convert-signal <port> <source> <target>");
		System.out.println("");
		System.out.println("options list:");
		System.out.println("");
		System.out.println("<source> := <source protocols> <source command hex> <source bits>");
		System.out.println("<target> := <target protocols> <target command hex> <target bits>");
		System.out.println("");
		System.out.println("<source protocols>: IR command source's protocol (e.g. 1 for NEC)");
		System.out.println("<source hex>: IR command source's protocol (e.g. E13650AF)");
		System.out.println("<source bits>: IR bits source's protocol (e.g. 32)");
		System.out.println("<target protocols>: IR command target's protocol (e.g. 2 for Sony)");
		System.out.println("<target hex>: IR command target's protocol (e.g. A90)");
		System.out.println("<target bits>: IR bits target's protocol (e.g. 12)");
		System.out.println("");
		System.out.println("");
		System.out.println("Example: For converting NEC hex E13650AF to Sony A90 hex, run");
		System.out.println("");
		System.out.println("java -jar irduino-tools.jar convert-signal 1 E13650AF 2 A90");
		System.out.println("");
		
	}
}



