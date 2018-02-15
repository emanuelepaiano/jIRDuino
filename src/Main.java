import org.jirduino.config.JSONController;
import org.jirduino.drivers.IRDevice;
import org.jirduino.drivers.IRLib2Device;
import org.jirduino.translators.SignalConverter;
import org.jirduino.translators.SignalRuleTable;
import org.serialduino.drivers.ComLinkDevice;

public class Main {

	public static void main(String[] args) {
		ComLinkDevice port=new ComLinkDevice(ComLinkDevice.getPorts()[0], ComLinkDevice.BAUDRATE_9600);
		
		IRDevice ir=new IRLib2Device(port);
		
		ir.init();
		
		
		JSONController source=new JSONController("PHILIPS_DVP3350");
		
		source.load();
		
		JSONController target=new JSONController("DAEWOO_HIFI");
		
		target.load();
		
		SignalRuleTable rules=SignalRuleTable.makeRuleTable(source, target, 0);
		
		SignalConverter converter=new SignalConverter(rules, ir);
		
		converter.setDebugModeOn(true);
		
		Thread T1=new Thread(converter);
		
		T1.start();
		
		
		
		
	}
	
	
	

}
