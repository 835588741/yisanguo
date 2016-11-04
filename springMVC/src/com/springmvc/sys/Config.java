package com.springmvc.sys;

import java.util.Properties;

public class Config {
	private static Properties properties;
	static void set(Properties p){
		if(properties==null){
			properties = p;
		}
	}
	
	public static String get(String key){
		return properties.getProperty(key);
	}
}
