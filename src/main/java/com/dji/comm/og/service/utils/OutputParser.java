package com.dji.comm.og.service.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.dji.comm.og.service.Application;
import com.dji.comm.og.service.dto.Param;

public class OutputParser {
	
	//Logger
	private final static Logger LOGGER = Logger.getLogger(OutputParser.class.getName());
	
	
    public static String CSV_SPLIT_BY = ";";
    private static List<String> ignoreEntries=new ArrayList<String>();
    
    static {
    	ignoreEntries=new ArrayList<String>();
    	ignoreEntries.add("name");
    }

	public static Param parseGetOutput(String output){
		LOGGER.info("try parsing get output");
		Param param=null;
		BufferedReader reader = new BufferedReader(new StringReader(output));
		String line="";
		try {
			while ((line = reader.readLine()) != null) {
				String[] parameterConfig = line.split(CSV_SPLIT_BY);
				if(line.contains(";name;")) {
					continue;
				}
				else {
					param=new Param(parameterConfig[2]);
					param.setMinVal(parameterConfig[5]);
					param.setMaxVal(parameterConfig[6]);
					param.setDefaultVal(parameterConfig[7]);
					param.setActVal(parameterConfig[8]);
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return param;
	}
	
	public static List<Param> parseListOutput(String output){
		LOGGER.info("try parsing list output");
		List<Param> paramList=new ArrayList<Param>();
		
		BufferedReader reader = new BufferedReader(new StringReader(output));
		String line="";
		try {
			while ((line = reader.readLine()) != null) {
				String[] parameterConfig = line.split(CSV_SPLIT_BY);
				if(ignoreEntries.contains(parameterConfig[2])) {
					continue;
				}
				Param param=new Param(parameterConfig[2]);
				paramList.add(param);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return paramList;
	}
	
	public static List<String> getNameList(List<Param> params) {
		LOGGER.info("convert to params list");
		List<String> paramNames=new ArrayList<String>();
		
		for(Param param : params) {
			paramNames.add(param.getName());
		}
		
		return paramNames;
	}
	
}
