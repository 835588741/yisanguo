package com.springmvc.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springmvc.dao.TaskDao;

@Controller
@RequestMapping("taskAction")
public class TaskAction
{
	@Autowired
	private TaskDao taskDao;
	
	// 迷城
	@RequestMapping(value = "michengzhengbazhan.action", method = RequestMethod.GET)
	public void michengzhengbazhan(HttpServletResponse response,String content,String uuid) throws IOException
	{
		String json = taskDao.michengzhengbazhan(uuid);
		// 空，无此账号或密码
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(json);
	}
	
	// 占领王座
	@RequestMapping(value = "takewangzuo.action", method = RequestMethod.GET)
	public void takewangzuo(HttpServletResponse response,String content,String uuid) throws IOException
	{
		String json = taskDao.takewangzuo(uuid);
		// 空，无此账号或密码
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(json);
	}
	
	
	// 缔约
	@RequestMapping(value = "diyue.action", method = RequestMethod.GET)
	public void diyue(HttpServletResponse response,String content,String uuid) throws IOException
	{
		String json = taskDao.diyue(uuid);
		// 空，无此账号或密码
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(json);
	}
	
	
	// 获取藏宝图
	@RequestMapping(value = "getcangbaotu.action", method = RequestMethod.GET)
	public void getcangbaotu(HttpServletResponse response,String uuid) throws IOException
	{
		String json = taskDao.getcangbaotu(uuid);
		// 空，无此账号或密码
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(json);
	}

}
