package com.springmvc.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springmvc.dao.BattleGroupDao;


@Controller
@RequestMapping("requestBattleGroup")
public class RequestBattleGroup
{
	@Autowired
	BattleGroupDao dao;
	
	@RequestMapping(value = "attack.action",method = RequestMethod.GET)
	public void attack(HttpServletResponse response,String uuid,String countryid,String doorareaid)  throws IOException
	{
		String json = dao.attack(uuid,countryid,doorareaid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	@RequestMapping(value = "testup.action",method = RequestMethod.GET)
	public void testup(HttpServletResponse response,String uuid)  throws IOException
	{
		dao.upposition(uuid, 25);//(uuid,countryid,doorareaid);
//		response.setHeader("Content-type", "text/html;charset=UTF-8");  
//		response.setCharacterEncoding("UTF-8");  
//		response.getWriter().println(json);
	}
	
	
	@RequestMapping(value = "battle.action",method = RequestMethod.GET)
	public void battle(HttpServletResponse response,String uuid)  throws IOException
	{
		String json = dao.battle(uuid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	@RequestMapping(value = "battleout.action",method = RequestMethod.GET)
	public void battleout(HttpServletResponse response,String uuid)  throws IOException
	{
		String json = dao.battleout(uuid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	@RequestMapping(value = "zhanling.action",method = RequestMethod.GET)
	public void zhanling(HttpServletResponse response,String uuid)  throws IOException
	{
		String json = dao.zhanling(uuid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
}
