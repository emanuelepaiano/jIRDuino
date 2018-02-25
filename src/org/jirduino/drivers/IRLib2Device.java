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
import org.jirduino.helpers.JIRduinoHelper;
import org.serialduino.arduino.ArduinoSerialMonitor;
import org.serialduino.drivers.LinkDevice;
import org.serialduino.settings.DefaultArduinoSerialMonitor;


/**
 * Java IRLib2 Driver for jIRDuino.
 * This class works with IRDuino-IRLib2 Sketch
 * 
 * sketch based Cyborg5's library
 * (https://github.com/cyborg5/IRLib2)
 * 
 * 
 * 
 * @author Emanuele Paiano
 * */
public class IRLib2Device implements IRDevice{
	
	private static ArduinoSerialMonitor arduino;
	
	private static boolean isReceiverEnabled=false;
	
	private static boolean passiveSnifferMode=false;
	
	private boolean rcvLedOn=false;
	
	
		
	/**
	 * Constructor. 
	 * @param arduino ArduinoSerialMonitor object
	 * */
	public IRLib2Device(LinkDevice port) 
	{
		IRLib2Device.arduino=new ArduinoSerialMonitor(port);
	}

	
	@Override
	public boolean init() 
	{
		return arduino.open();
	}
	
	@Override
	public ArduinoSerialMonitor getArduino() {
		return arduino;
	}
	
	
	
	@Override
	public boolean sendHexValue(int protocolId, String command, int bits) 
	{
		return sendDecValue(protocolId, String.valueOf(JIRduinoHelper.hex2decimal(command)), bits);
	}
	
	/**
	 * send Dec Value command. It work like sendHexValue(), but with decimal 
	 * values. 
	 * @param protocol protocol id
	 * @param command hex command string (i.e. 3772793023 for E0E040BF).
	 * @param bits command bit numbers 
	 * @return true if success, false otherwise
	 * */
	private boolean sendDecValue(int protocol, String command, int bits) 
	{
		boolean success=false;
		if(!arduino.isReady())
			return false;
		else {
			if (this.isPassiveSnifferMode())
				return false;
			
			String param1=String.valueOf(protocol);
			String param2=String.valueOf(command);
			
			boolean wasReceiverEnabled=false;
			
			if (isReceiverEnabled) {
				this.setReceiverEnabled(false);
				wasReceiverEnabled=true;
			}
			
			success=execIRDuino("0",param1, param2);
			
			
			if(wasReceiverEnabled)
				this.setReceiverEnabled(true);
			
			return success;
		}
	}
	
	
	@Override
	public synchronized boolean sendData(Signal value) 
	{
		return sendHexValue(value.getProtocol(), value.getValue(), value.getBits());
	}
	
	
	@Override
	public String receiveHexValue() 
	{
		while (isPassiveSnifferMode())
			try {
				wait();
			} catch (InterruptedException e) {}
		
		return JIRduinoHelper.getHexValue(waitResponse());
	}
	
	
	
	@Override
	public Signal receiveData() 
	{
		while (isPassiveSnifferMode())
			try {
				wait();
			} catch (InterruptedException e) {}
		
		String data=waitResponse().replace("\n", "").replace("\r", "");
		int id=Integer.valueOf(JIRduinoHelper.getProtocolID(data));
		String value=JIRduinoHelper.getHexValue(data);		
		int bits=Integer.valueOf(JIRduinoHelper.getProtocolBits(data));
		
		return new Signal(id, value, bits);
	}
	
	
	@Override
	public String receiveProtocolID() 
	{ 
		while (isPassiveSnifferMode())
			try {
				wait();
			} catch (InterruptedException e) {}
	
		return JIRduinoHelper.getProtocolID(waitResponse());
	}
	
	
	@Override
	public boolean isReceiverEnabled() {
		return isReceiverEnabled;
	}

	/**
	 * Disconnect IR Device, closing connection with Arduino
	 * @return true if success, false otherwise
	 * */
	public synchronized boolean disconnect() 
	{
		return arduino.close();
	}
	
	
	@Override
	public synchronized String waitResponse() 
	{
		String resp="";
		
		while(!arduino.bufferAvailable())
			try {
				Thread.sleep(5);
					
			} catch (InterruptedException e) {}
		
			
		arduino.setEndChar("\r\n");
		
		resp=arduino.receive();
		
		arduino.setEndChar(DefaultArduinoSerialMonitor.END_WITH);
				
		return resp;
	}
	
	
	/**
	 * Run single command string to Arduino
	 * @return true if success, false otherwise
	 * */
	protected synchronized boolean execIRDuino(String opcode, String param1, String param2) 
	{
		if(arduino.isReady())
			arduino.send(opcode+","+param1+","+param2);
		
		return waitResponse().contains("250 OK");
		
	}
	
	
	@Override
	public boolean isPassiveSnifferMode() {
		return passiveSnifferMode;
	}

	
	@Override
	public synchronized boolean setPassiveSnifferMode(boolean value) {
		boolean success=false;
		
		if(value && !passiveSnifferMode) {
			success=execIRDuino("1","3","1");
			
			if (success) 
				IRLib2Device.passiveSnifferMode=true;
		}else {
			success=execIRDuino("1","3","0");
			
			if(success) {
				IRLib2Device.passiveSnifferMode=false;
				notifyAll();
			}
		}
		
		return success;
		
	}

	
	@Override
	public boolean isRcvLedOn() {
		return rcvLedOn;
	}
	
	
	@Override
	public boolean setRcvLedOn(boolean value) {
				
		boolean success=false;
		
		if(value && !rcvLedOn) {
			success=execIRDuino("1","2","1");
			
			if (success) 
				this.rcvLedOn=true;
		}else {
			success=execIRDuino("1","2","0");
			
			if(success)
				this.rcvLedOn=false;
		}
		
		return success;
		
	}
	
	@Override
	public synchronized boolean setReceiverEnabled(boolean value) {
				
		boolean success=false;
		
		if(value && !isReceiverEnabled) {
			success=execIRDuino("1","1","1");
			
			if (success) 
				IRLib2Device.isReceiverEnabled=true;
		}else {
			success=execIRDuino("1","1","0");
			
			if(success)
				IRLib2Device.isReceiverEnabled=false;
		}
		
		return success;
		
	}

}
