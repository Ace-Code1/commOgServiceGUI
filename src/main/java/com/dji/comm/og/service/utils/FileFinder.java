package com.dji.comm.og.service.utils;

import java.io.File;

public class FileFinder {

	public static boolean isFilePresent(File directory,String fileName) {
		boolean isFileFound=false;
		
		for(File f : directory.listFiles()) {
			if(f.isFile() && f.getName().equals(fileName)) {
				return true;
			}
		}
		 
		return isFileFound;
	}
	
}
