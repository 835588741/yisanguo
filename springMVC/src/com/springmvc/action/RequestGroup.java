package com.springmvc.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springmvc.dao.GroupDao;

@Controller
@RequestMapping("requestGroup")
public class RequestGroup
{
	@Autowired
	GroupDao dao;

	// 获取部曲信息
	@RequestMapping(value = "getgroupinfo.action",method = RequestMethod.GET)
	public void getGroupInfo(HttpServletResponse response,String groupid) throws IOException 
	{
		String json = dao.getGroupInfo(groupid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 创建部曲
	@RequestMapping(value = "creategroup.action",method = RequestMethod.GET)
	public void createGroup(HttpServletResponse response,String groupidname,String uuid) throws IOException 
	{
		groupidname = new String(groupidname.getBytes("ISO-8859-1"), "utf-8");
		String json = dao.creategroup(groupidname,uuid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 获取部曲列表
	@RequestMapping(value = "getgrouplist.action",method = RequestMethod.GET)
	public void getGroupList(HttpServletResponse response,String page,String pagesize) throws IOException 
	{
		String json = dao.getGroupList(page,pagesize);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	
	// 获取部曲成员列表
	@RequestMapping(value = "getgroupmemberlist.action",method = RequestMethod.GET)
	public void getGroupMemberList(HttpServletResponse response,String groupid) throws IOException 
	{
		String json = dao.getGroupMemberList(groupid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 获取申请列表
	@RequestMapping(value = "getjoinlist.action",method = RequestMethod.GET)
	public void getJoinList(HttpServletResponse response,String uuid,String groupid) throws IOException 
	{
		String json = dao.getJoinList(uuid, groupid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 申请加入
	@RequestMapping(value = "join.action",method = RequestMethod.GET)
	public void requestJoin(HttpServletResponse response,String uuid,String groupid) throws IOException 
	{
		String json = dao.requestJoinGroup(uuid, groupid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 批准加入
	@RequestMapping(value = "acceptjoin.action",method = RequestMethod.GET)
	public void acceptJoin(HttpServletResponse response,String applyer_uuid,String groupid) throws IOException 
	{
		String json = dao.acceptJoin(applyer_uuid, groupid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 拒绝加入
	@RequestMapping(value = "refusejoin.action",method = RequestMethod.GET)
	public void refuseJoin(HttpServletResponse response,String applyer_uuid,String groupid) throws IOException 
	{
		String json = dao.refuseJoin(applyer_uuid, groupid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 查找部曲
	@RequestMapping(value = "lookup.action",method = RequestMethod.GET)
	public void lookup(HttpServletResponse response,String likename) throws IOException 
	{
		String json = dao.lookup(likename);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	
	// 开除成员
	@RequestMapping(value = "dismissed.action",method = RequestMethod.GET)
	public void dismissed(HttpServletResponse response,String master_uuid,String bedismisser_uuid,String groupid) throws IOException 
	{
		String json = dao.dismissed(master_uuid,bedismisser_uuid, groupid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
//	
//	// 获取成员列表
//	@RequestMapping(value = "getmemberlist.action",method = RequestMethod.GET)
//	public void getMemberList(HttpServletResponse response,String master_uuid,String groupid) throws IOException 
//	{
//		String json = dao.getMemberList(applyer_uuid, groupid);
//		response.setHeader("Content-type", "text/html;charset=UTF-8");  
//		response.setCharacterEncoding("UTF-8");  
//		response.getWriter().println(json);
//	}
//	
	// 退出部曲
	@RequestMapping(value = "backedout.action",method = RequestMethod.GET)
	public void backedOut(HttpServletResponse response,String uuid,String groupid) throws IOException 
	{
		String json = dao.backedout(uuid, groupid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
//	
//	// 任命成员
//	@RequestMapping(value = "appoint.action",method = RequestMethod.GET)
//	public void appoint(HttpServletResponse response,String applyer_uuid,String groupid) throws IOException 
//	{
//		String json = dao.appoint(applyer_uuid, groupid);
//		response.setHeader("Content-type", "text/html;charset=UTF-8");  
//		response.setCharacterEncoding("UTF-8");  
//		response.getWriter().println(json);
//	}
}
