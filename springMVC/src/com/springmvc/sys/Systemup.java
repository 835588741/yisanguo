package com.springmvc.sys;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


public class Systemup implements Servlet {

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public void init(ServletConfig arg0){
		try{
			loadProperties();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void loadProperties() throws IOException {
		Properties p = new Properties();
		p.load(this.getClass().getClassLoader().getResourceAsStream("system.properties"));
		Config.set(p);
	}

	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}
	
}
