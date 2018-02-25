package org.jirduino.tools;
import org.jirduino.core.Signal;
import org.jirduino.drivers.IRDevice;
import org.jirduino.drivers.IRLib2Device;
import org.serialduino.drivers.ComLinkDevice;

public class IRSenderTools {
	public static void runSender(String[] args) {
		if (args.length>4) {
			ComLinkDevice port=new ComLinkDevice(ComLinkDevice.getPortByName(args[1]), ComLinkDevice.BAUDRATE_9600);
		
			IRDevice ir=new IRLib2Device(port);
		
			ir.init();
		
			ir.sendData(new Signal(Integer.valueOf(args[2]), args[3], Integer.valueOf(args[4])));
		}else printHelp();
	}
	
	public static void printHelp()
	{
		System.out.println("");
		System.out.println("Use: java -jar irduino-tools.jar send <port> <id> <value> <bits>");
		System.out.println("");
		System.out.println("where:");
		System.out.println("- id, integer id for infrared protocol (i.e. 1 for NEC). See Protocol.java.");
		System.out.println("");
		System.out.println("- value, integer in hex format for command value (i.e. E13650AF).");
		System.out.println("");
		System.out.println("- port, arduino-connected serial port (i.e. /dev/ttyUSB0)");
		System.out.println("");
	}
	
}
