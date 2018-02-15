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

package org.jirduino.core;


/**
 * A Signal object is a pair [id, value], where
 * 
 * <ul>
 * 	<li>id: protocol Imlib2 integer (i. e. 7 for NECx)</li>
 * 	<li>value: Hex Remote Command (i.e. E0E040BF for Samsung NECx Power key)</li>
 * </ul>
 * 
 * @author Emanuele Paiano
 * 
 * */
public class Signal {
	
	/**
	 * id protocol
	 * */
	private int protocol=0;
	
	
	/**
	 * hex value command
	 * */
	private String value;
	
	/**
	 * protocol bits
	 * */
	private int bits=0;
	

	/**
	 * Constructor.
	 * @param id protocol id (i.e. NECx.PROTOCOL_ID)
	 * @param value hex command (i.e. NECx.POWER_KEY)
	 * @param bits bit number (i.e. 32)
	 * */
	public Signal(int id, String value, int bits) 
	{
		this.setProtocol(id);
		
		this.setValue(value);
		
		this.setBits(bits);
	}

	/**
	 * Get protocol id
	 * @return integer value
	 * */
	public int getProtocol() {
		return protocol;
	}
	
	/**
	 * set protocol id
	 * */
	public void setProtocol(int id) {
		this.protocol = id;
	}

	/**
	 * Return HEX value command
	 * */
	public String getValue() {
		return value;
	}

	/**
	 * Set HEX value command
	 * */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Return bit number command
	 * */
	public int getBits() {
		return bits;
	}

	
	/**
	 * Set bit number command
	 * */
	public void setBits(int bits) {
		this.bits = bits;
	}
	
	
	/**
	 * Check id signal is equal to another.
	 * @param another is checking signal
	 * @return true if IDs and Protocols are same, false otherwise
	 * */
	public boolean isEquals(Signal another) {
		return (another.protocol==this.getProtocol()) && (another.value==this.getValue());
	}
	
	
	/**
	 * Override from Object
	 * */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Signal)
			return isEquals((Signal)obj);
		return false;
		
	}
	
	/**
	 * Override from Object
	 * */
	@Override
	public String toString() {
		return "("+this.getProtocol()+", "+this.getValue()+", "+this.getBits()+")";
	}
	
	
	/**
	 * Override from Object
	 * */
	@Override
	public int hashCode() {
		return (this.getProtocol()+ this.getValue()).hashCode();
	}	
	
	
	/**
	 * Return empty signal [0,"0"]
	 * */
	public static Signal getEmptySignal() 
	{
		return new Signal(0,"0", 0);
	}
	

}
