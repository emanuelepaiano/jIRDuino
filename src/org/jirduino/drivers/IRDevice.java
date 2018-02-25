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

package org.jirduino.drivers;

import org.jirduino.core.Signal;
import org.serialduino.arduino.ArduinoSerialMonitor;


/**
 * IRDuino Java generic interface for IR devices.
 * This is an Abstract interface communications for
 * Arduino different IR libraries 
 *
 * 
 * @author Emanuele Paiano
 * */
public interface IRDevice {
	
	/**
	 * Initialize IR Device 
	 * @return true if success, false otherwise
	 * */
	public boolean init();
	
	/**
	 * Get Arduino Serial Monitor
	 * */
	public ArduinoSerialMonitor getArduino();
	
	
	/**
	 * send Hex Value command. Simulate a single pressing remote button
	 * @param command hex command string (i.e. E0E040BF)
	 * @param protocolId protocol number
	 * @param bits bit number (protocol)
	 * @return true if success, false otherwise
	 * */
	public boolean sendHexValue(int protocolId, String command, int bits);
		
	
	/**
	 * send signal command. It work like sendHexValue(), but with signal object 
	 * @param value command signal (i.e. new Signal(7, "E0E040BF")).
	 * @return true if success, false otherwise
	 * */
	public boolean sendData(Signal value);
	
	
	/**
	 * Wait for receive response from remote IR Device and extract HEX value. 
	 * You should enable receiver first, calling setReceiverEnable(true).
	 * @return Comamnd hex value as string
	 * */
	public String receiveHexValue();
	
	/**
	 * Wait for receive response from remote IR Device and return object containing protocolID and Hex value command. 
	 * You should enable receiver first, calling setReceiverEnable(true).
	 * @return [ProtocolID, HexValue] as Item
	 * */
	public Signal receiveData();
	
	
	/**
	 * Wait for receive response from remote IR Device and extract Protocol ID. 
	 * You should enable receiver first, calling setReceiverEnable(true)
	 * @return ImLib2 Protocol ID as string
	 * */
	public String receiveProtocolID();
	
	
	/**
	 * return true if receiver enabled, false otherwise
	 * */
	public boolean isReceiverEnabled();

	/**
	 * Disconnect IR Device, closing connection with Arduino
	 * @return true if success, false otherwise
	 * */
	public boolean disconnect();
	
	
	/**
	 * Wait for IR Remote response from Arduino.
	 * @return response as string
	 * */
	public String waitResponse(); 
	
	
	
	/**
	 * @return true if dumpResult() mode is on, false otherwise
	 * */
	public boolean isPassiveSnifferMode();

	
	/**
	 * Enable/Disable passiveSnifferMode
	 * @param value true to enable, false to disable
	 * @return true if success, false otherwise
	 * */
	public boolean setPassiveSnifferMode(boolean value);

	
	/**
	 * @return true if Receiver Led is on, false otherwise
	 * */
	public boolean isRcvLedOn();
	
	
	/**
	 * Enable/Disable ReceiverLed
	 * @param value true to enable, false to disable
	 * @return true if success, false otherwise
	 * */
	public boolean setRcvLedOn(boolean value);
	
	/**
	 * Enable/Disable IR Receiver
	 * @param value true for enable, false otherwise
	 * @return true if success, false otherwise
	 * */
	public boolean setReceiverEnabled(boolean value);

}
