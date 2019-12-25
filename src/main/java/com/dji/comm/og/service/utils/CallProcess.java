package com.dji.comm.og.service.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CallProcess {

 public static String executeCommand(String command) throws IOException {
	  Runtime rt = Runtime.getRuntime(); 
	  Process proc = rt.exec(command);
			  
	  BufferedReader inStreamReader = new BufferedReader(new InputStreamReader(proc.getInputStream())); 
	  StringBuilder output=new StringBuilder();
	  
	  
	  for (String line = inStreamReader.readLine(); line != null; line = inStreamReader.readLine()) {
	  	  output.append(line+"\n");
	  }
	  
	  
	  return output.toString();
  }
	
	
}

