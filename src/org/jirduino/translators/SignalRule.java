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

package org.jirduino.translators;

import org.jirduino.core.Signal;

/**
 * Signals Rule item, a SignalRule object will be stored into Signal Rules Table.
 * It contains input/output signals and attributes like delay time
 * (to wait before send output signal) and enable/disable sending flag.
 * 
 * Rule fields:
 * 
 * <pre>
 *     --------------------------------------
 *    | IN   |   OUT   |  DELAY  |  ENABLED  |
 *     --------------------------------------
 * </pre>
 * 
 * <ul>
 * 	 <li> IN: Input Signal</li>
 *	 <li> OUT: Output Signal</li>
 *	 <li> DELAY: Delay time (in millis) to wait before sending (by Arduino) output signal</li>
 *	 <li> ENABLED: if false, output signal won't be sent by Arduino.</li>
 * </ul>
 * @see Signal   
 * 
 * @author Emanuele Paiano
 * 
 * */
public class SignalRule {
	
	/**
	 * if false, output signal won't be sent by Arduino
	 ***/
	private boolean enabled;
	
	/**
	 * input signal 
	 */
	private Signal in;
	
	/**
	 * output (translated) signal
	 */
	private Signal out;
	
	
	/**
	 * Delay time (in millis) to wait before sending (by Arduino) output signal
	 */
	private long delaySend;
	
	
	/**
	 * Constructor.
	 * @param in input (received) signal
	 * @param out output (translated) signal
	 * @param delaySend Delay time (in millis) to wait before sending (by Arduino) output signal 
	 * @param if false, output signal won't be sent by Arduino
	 **/
	public SignalRule(Signal in, Signal out, long delaySend, boolean enabled) 
	{
		this.in=in;
		this.out=out;
		this.delaySend=delaySend;
		this.enabled=enabled;
	}
	
	/**
	 * Constructor.
	 * @param in input (received) signal
	 * @param out output (translated) signal
	 **/
	public SignalRule(Signal in, Signal out) 
	{
		this(in, out, 0, true);
	}
	
	/**
	 * Constructor.
	 * @param in input (received) signal
	 * @param out output (translated) signal
	 * @param delaySend Delay time (in millis) to wait before sending (by Arduino) output signal 
	 **/
	public SignalRule(Signal in, Signal out, long delaySend) 
	{
		this(in, out, delaySend, true);
	}
	
	/**
	 * Constructor.
	 * @param in input (received) signal
	 * @param out output (translated) signal
	 * @param if false, output signal won't be sent by Arduino
	 **/
	public SignalRule(Signal in, Signal out, boolean enabled) 
	{
		this(in, out, 0, enabled);
	}
	
	/**
	 * return enabled rule status
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Enable or disable rule
	 * @param enabled true for enabled, false for disabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * get input signal
	 **/
	public Signal getIn() {
		return in;
	}

	/**
	 * get output signal
	 **/
	public Signal getOut() {
		return out;
	}

	/**
	 * set output signal
	 **/
	public void setOut(Signal out) {
		this.out = out;
	}

	/**
	 * get delay time (in millis) before sending output signal
	 **/
	public long getDelaySend() {
		return delaySend;
	}

	/**
	 * set delay time (in millis) before sending output signal
	 * @param delaySend value in milliseconds
	 **/
	public void setDelaySend(long delaySend) {
		this.delaySend = delaySend;
	}
	
	/**
	 * generate hash key to storage into HashMap Table
	 **/
	public String hashKey() 
	{
		return in.getProtocol()+in.getValue();
	}

	/**
	 * generate and return empty signal rule.
	 **/
	public static SignalRule getEmptySignalRule() 
	{
		return new SignalRule(Signal.getEmptySignal(), Signal.getEmptySignal());
	}
	
	
	/**
	 * return true if SignalRule is equals to another
	 * @param another other signal rule to match
	 **/
	public boolean isEquals(SignalRule another) 
	{
		return this.hashKey()==another.hashKey();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SignalRule)
			return ((SignalRule) obj).hashCode()==hashCode();
		return false;
		
	}
	
	@Override
	public int hashCode() {
		return hashKey().hashCode();
		
	}
	
	
	@Override
	public String toString() {
		String res="[ IN: "+in.toString()+", OUT: "+out.toString();
		res+=", DELAY: "+this.delaySend+", ENABLED: "+enabled+" ]";
		return res;
		
	}
	
}
