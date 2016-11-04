package com.springmvc.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springmvc.dao.MessageDao;

@Controller
@RequestMapping("messageAction")
public class RequestMessage
{
	@Autowired
	private MessageDao dao;
	
	//system message
	@RequestMapping(value = "systemmessage.action",method = RequestMethod.GET)
	public void sendSystemMessage(HttpServletResponse response,String receiver_uuid,String keyword,String type) throws IOException{
		// type :  1|回帖通知楼主  2|回复评论通知楼层主
		keyword = new String(keyword.getBytes("ISO-8859-1"),"utf-8");
		String data = dao.sendSystemMessage(receiver_uuid,keyword,type).toString();
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(data);
	}
	
	// 发送消息
	@RequestMapping(value = "sendmessage.action",method = RequestMethod.POST)
	public void sendMessage(HttpServletResponse response,String senderId,String receiveId,String message,String sendername,String isreplay,String mid) throws IOException
	{
		String json = dao.sendMassage("message", receiveId, senderId, message,sendername,isreplay,mid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 获取消息
	@RequestMapping(value = "getmessage.action",method = RequestMethod.GET)
	public void getMessage(HttpServletResponse response,String uuid,String isread) throws IOException
	{
		String json = dao.getMessage(uuid, isread);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 清空消息
	@RequestMapping(value = "clearmessage.action",method = RequestMethod.GET)
	public void clearMessage(HttpServletResponse response,String uuid,String isread) throws IOException
	{
		String json = dao.clearMessage(uuid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 发送公聊消息
	@RequestMapping(value = "sendpublicmessage.action",method = RequestMethod.POST)
	public void sendpublicmessage(HttpServletResponse response,String uuid,String message) throws IOException
	{
		String json = dao.sendpublicmessage(uuid,message);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 获取公聊消息
	@RequestMapping(value = "getpublicmessage.action",method = RequestMethod.GET)
	public void getpublicmessage(HttpServletResponse response,String uuid) throws IOException
	{
		String json = dao.getpublicmessage(uuid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 拉黑发信者
	@RequestMapping(value = "pullblacelist.action",method = RequestMethod.GET)
	public void pullblacelist(HttpServletResponse response,String uuid,String targetuuid) throws IOException
	{
		String json = dao.pullblacelist(uuid,targetuuid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 获取黑名单
	@RequestMapping(value = "getblacklist.action",method = RequestMethod.GET)
	public void getblacklist(HttpServletResponse response,String uuid) throws IOException
	{
		String json = dao.getblacklist(uuid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 从黑名单删除
	@RequestMapping(value = "deletefromblack.action",method = RequestMethod.GET)
	public void deletefromblack(HttpServletResponse response,String uuid,String targetuuid) throws IOException
	{
		String json = dao.deletefromblack(uuid,targetuuid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
}
