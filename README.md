# jIRDuino
A Java library for sending / receiving IR signals

# What is jIRDuino?
JIRDuino is a Java library, for receive, send and convert IR signals by a Arduino (or compatible).
You can emulate IR controllers inside your Java threads or translating signal between IR protocols.

![alt tag](https://github.com/emanuelepaiano/jirduino/blob/master/irduino-device/irduino.jpg?raw=true)

Main Java features:
<ul>
  <li>IR Sniffer/receiver</li>
  <li>IR Sender</li>
  <li>IR Remote signals converter</li>
  <li>IR Remote controllers translators</li>
  <li>IR Remote controller simulator, with JSON keys mapped</li>
</ul>

## Easy to use

<pre>

     /* Initializing Device */		
    IRDevice ir=new IRLib2Device(arduinoPort);
    
    // .. Other code

     /* Sending 0xE0E040BF commands on Samsung TV */		
    ir.sendData(new Signal(Protocols.NECx, "E0E040BF", 32));
</pre>

For setup, configuration and tutorials, see jIRDuino Wiki: 
[Getting Started](https://goo.gl/wXvFBh)

# License
- JIRduino is released under Apache 2.0
- IRLib2-based Arduino sketch is released under GNU-GPL3
