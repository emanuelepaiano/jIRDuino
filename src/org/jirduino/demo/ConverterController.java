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

package org.jirduino.demo;

import org.jirduino.config.JSONController;
import org.jirduino.drivers.IRDevice;
import org.jirduino.drivers.IRLib2Device;
import org.jirduino.translators.SignalConverter;
import org.jirduino.translators.SignalRuleTable;
import org.serialduino.drivers.ComLinkDevice;

/**
 * This sample convert signals between Remote Controller. You need put data/ json
 * source into project root directory
 * */
public class ConverterController {

	public static void main(String[] args) {
		ComLinkDevice port=new ComLinkDevice(ComLinkDevice.getPorts()[0], ComLinkDevice.BAUDRATE_9600);
		
		IRDevice ir=new IRLib2Device(port);
		
		ir.init();
		
		// Input signal from DVD controller: PHILIPS_DVP3350 (RC6)
		JSONController source=new JSONController("PHILIPS_DVP3350");
		
		source.load();
		
		// Output signal to simulate HiFi controller: DAEWOO_HIFI (NEC)
		JSONController target=new JSONController("DAEWOO_HIFI");
		
		target.load();
		
		SignalRuleTable rules=SignalRuleTable.makeRuleTable(source, target, 0);
		
		SignalConverter converter=new SignalConverter(rules, ir);
		
		converter.setDebugModeOn(true);
		
		Thread T1=new Thread(converter);
		
		T1.start();
		
	}
	
	
	

}
