package org.jirduino.drivers;

import org.jirduino.core.Signal;
import org.serialduino.arduino.ArduinoSerialMonitor;


/**
 * Raw Driver Class: template for creating new driver
 * 
 * */
public class RawDriver implements IRDevice{

	@Override
	public boolean init() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArduinoSerialMonitor getArduino() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendHexValue(int protocolId, String command, int bits) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendData(Signal value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String receiveHexValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Signal receiveData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String receiveProtocolID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isReceiverEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean disconnect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String waitResponse() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPassiveSnifferMode() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setPassiveSnifferMode(boolean value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRcvLedOn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setRcvLedOn(boolean value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setReceiverEnabled(boolean value) {
		// TODO Auto-generated method stub
		return false;
	}

}
