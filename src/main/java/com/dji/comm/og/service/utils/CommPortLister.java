package com.dji.comm.og.service.utils;

import java.util.Enumeration;
import java.util.HashMap;

import com.fazecast.jSerialComm.SerialPort;


/**
 * List the ports.
 * 
 * @author Ian F. Darwin, http://www.darwinsys.com/
 * @version $Id: CommPortLister.java,v 1.4 2004/02/09 03:33:51 ian Exp $
 */
public class CommPortLister {

  /** Simple test program. */
  public static void main(String[] ap) {
	  SerialPort[] ports=SerialPort.getCommPorts();
	  for(SerialPort serialPort : ports) {
		  System.out.println(serialPort.getPortDescription()+" - "+serialPort.getSystemPortName());
	  }
  }
  
  public static HashMap<String,String> getSerialPorts(){
	  HashMap<String,String> portsMap=new HashMap<String,String>();
	  SerialPort[] serialPorts=SerialPort.getCommPorts();
	  for(SerialPort serialPort : serialPorts) {
		  portsMap.put(serialPort.getPortDescription()+" - "+serialPort.getSystemPortName(),serialPort.getSystemPortName());
	  }	  
	  return portsMap;
  }
  

}
