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
 * Supported Protocols in Arduino IRLib2
 * Additional protocol IDs can be added here as 
 * static integer constants field
 * 
 * 
 * @author Emanuele Paiano
 * */
public interface Protocols {
	
	public static int INVALID=0;
	
	public static int NEC=1;
	
	public static int SONY=2;
	
	public static int RC5=3;
	
	public static int RC6=4;
	
	public static int PANASONIC_OLD=5;
	
	public static int JVC=6;
	
	public static int NECx=7;
	
	public static int SAMSUNG36=8;
	
	public static int GICABLE=9;
	
	public static int DIRECT_TV=10;
	
	public static int RCMM=11;
	
	public static int CYKM=12;
	
	
	

}
