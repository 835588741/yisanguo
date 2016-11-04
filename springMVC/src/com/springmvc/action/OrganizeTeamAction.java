/**
 * 
 */
package com.springmvc.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springmvc.dao.OrganizeTeamDao;

/** 组队
 * @file BattleGroupAction.java
 * @author 许仕永(xsy)
 * @package_name com.springmvc.action
 * @todo  TODO
 * @date 2016年9月5日 下午4:27:13
 */
@Controller
@RequestMapping("organizeteamAction")
public class OrganizeTeamAction
{
	@Autowired
	private OrganizeTeamDao orgabizeTeamDao;
	

	@RequestMapping(value = "buildTeam.action", method = RequestMethod.GET)
	public void buildTeam(HttpServletResponse response, String teammaster,String member) throws IOException
	{
		String data = orgabizeTeamDao.buildTeam(teammaster,member,0).toString();
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(data);
	}


	// type = 1  同意组队   type = 2拒绝
	@RequestMapping(value = "responeBuildTeam.action", method = RequestMethod.GET)
	public void responeBuildTeam(HttpServletResponse response, String teammaster,String member,int type) throws IOException
	{
		String data = orgabizeTeamDao.buildTeam(teammaster,member,type).toString();
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(data);
	}
	
	// get invital message
	@RequestMapping(value = "getinvitation.action", method = RequestMethod.GET)
	public void getinvitation(HttpServletResponse response, String uuid) throws IOException
	{
		String data = orgabizeTeamDao.getinvitation(uuid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(data);
	}
}
