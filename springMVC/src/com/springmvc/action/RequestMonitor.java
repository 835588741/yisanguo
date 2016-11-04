package com.springmvc.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springmvc.dao.MonitorDao;

@Controller
@RequestMapping("requestMonitorAction")
public class RequestMonitor
{
	@Autowired
	private MonitorDao oprea;
	
	// 外挂记录
	@RequestMapping(value = "cheat.action",method = RequestMethod.GET)
	public void cheat(HttpServletResponse response,String uuid,String name) throws IOException
	{
		name = new String(name.getBytes("ISO-8859-1"),"utf-8");
		String json = oprea.cheat(uuid,name);//oprea.query("place", "areaid = '"+id+"'", 0);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
}
