package com.dji.comm.og.service.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import com.dji.comm.og.service.Application;

public class CallProcess {

//Logger
private final static Logger LOGGER = Logger.getLogger(CallProcess.class.getName());
			
	
 public static String executeCommand(String command) throws IOException {
	  LOGGER.info(command);
	  Runtime rt = Runtime.getRuntime(); 
	  Process proc = rt.exec(command);
			  
	  BufferedReader inStreamReader = new BufferedReader(new InputStreamReader(proc.getInputStream())); 
	  StringBuilder output=new StringBuilder();
	  
	  for (String line = inStreamReader.readLine(); line != null; line = inStreamReader.readLine()) {
	  	  output.append(line+"\n");
	  }  
	  
	  LOGGER.info(output.toString());
	  
	  return output.toString();
  }
	
	
}

