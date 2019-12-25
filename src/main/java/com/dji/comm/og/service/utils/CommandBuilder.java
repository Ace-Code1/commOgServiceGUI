package com.dji.comm.og.service.utils;

import java.io.File;

public class CommandBuilder {
	private String command=null; 
	public static final int GET=0;
	public static final int SET=1;
	public static final int LIST=2;
	
	
	public CommandBuilder(int type,File path,String scriptingLanguage,String script,String com,String aircraft,String...parameters) throws Exception {
		switch(type) {
			case GET: this.command=buildGetCommand(path,scriptingLanguage,script,com,aircraft,parameters[0]); break;
			case SET: this.command=buildSetCommand(path,scriptingLanguage,script,com,aircraft,parameters[0],parameters[1]); break;	
			case LIST: this.command=buildListCommand(path,scriptingLanguage,script,com,aircraft); break;
			default: throw new Exception("Unknown Type");
		}
	}
	
	private String buildGetCommand(File path,String scriptingLanguage,String script,String com,String aircraft,String paramName) {
		return scriptingLanguage+" "+path.getAbsolutePath()+File.separator+script+" "+com+" "+aircraft+" "+"FlycParam get "+paramName+" --fmt=csv";
	}
	
	private String buildSetCommand(File path,String scriptingLanguage,String script,String com,String aircraft,String paramName,String value) {
		return scriptingLanguage+" "+path.getAbsolutePath()+File.separator+script+" "+com+" "+aircraft+" "+"FlycParam set "+paramName+" "+value;	
	}
		
	private String buildListCommand(File path,String scriptingLanguage,String script,String com,String aircraft) {
		return scriptingLanguage+" "+path.getAbsolutePath()+File.separator+script+" "+com+" "+aircraft+" "+"FlycParam list --start=0 --count=900 --fmt=csv";
	}

	public String getCommand() {
		return command;
	}
	
}
