# jIRDuino
A Java library for sending / receiving IR signals

![alt tag](https://github.com/emanuelepaiano/jirduino/blob/master/irduino-device/irduino.jpg?raw=true)

Main Java features:
<ul>
  <li>IR Sniffer/receiver</li>
  <li>IR Sender</li>
  <li>IR Remote signals converter</li>
  <li>IR Remote controllers translators</li>
  <li>IR Remote controller simulator, with JSON keys mapped</li>
</ul>

## Sending signal example

<pre>
    /* It return /dev/ttyUSB0 on Linux */
    String port=ComLinkDevice.getPortByName("ttyUSB0")

    /* Port Speed */
    int speedPort=ComLinkDevice.BAUDRATE_9600;

    /* Creating Arduino port by ComLinkDevice interface */
    ComLinkDevice port=new ComLinkDevice(port, speedPort);

    /* creating IR virtual device based on IRLib2 Sketch */	
    IRDevice ir=new IRLib2Device(port);

    /* Initializing device */	
    ir.init();

     /* Sending 0xE0E040BF commands on Samsung TV */		
    ir.sendData(new Signal(Protocols.NECx, "E0E040BF", 32));
</pre>


#License
- JIRduino is released under Apache 2.0
- IRLib2-based Arduino sketch is released under GNU-GPL3
