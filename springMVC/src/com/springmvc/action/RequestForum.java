package com.springmvc.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springmvc.dao.FormDao;

@Controller
@RequestMapping("formAction")
public class RequestForum
{
	@Autowired
	private FormDao dao;
	
	// 发表帖子
	@RequestMapping(value = "publishpost.action",method = RequestMethod.POST)
	public void publishPost(HttpServletResponse response,String title,String content,String posterid,String postername) throws IOException
	{
		if(content.length() > 2000)
		{
			content = content.substring(0, 2000);
		}
		String json = dao.publishPost(title, content, posterid,postername);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 回复帖子
	@RequestMapping(value = "publishcomment.action",method = RequestMethod.POST)
	public void publishComment(HttpServletResponse response,String content,String commenterid,String commentername,String belongpostid) throws IOException
	{
		String json = dao.publishComment(content, commenterid, commentername, belongpostid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 获取评论
	@RequestMapping(value = "getcomment.action",method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getComment(HttpServletResponse response,String belongpostid,String page,String pagesize) throws IOException
	{
		List<Map<String, Object>> json = dao.getComment(belongpostid, page, pagesize);
		return json;
	}
	
	// 删除评论  192.168.245.1:8080/springMVC/formAction/deletecomment.action?&commentid=007e7c5bd074467296313b2766c509c8&belongpostid=bcce9a91263c4cb88357b7eff533b40b&managerid=0001
	@RequestMapping(value = "deletecomment.action",method = RequestMethod.GET)
	public void deletereplay(HttpServletResponse response,String commentid,String belongpostid,String managerid) throws IOException
	{
		String json = dao.deletecomment(commentid, belongpostid,managerid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 删除帖子 http://192.168.245.1:8080/springMVC/formAction/deletepost.action?&postid=f738e7ae357343fca61c9e84cf3bd5d9&managerid=0001
	@RequestMapping(value = "deletepost.action",method = RequestMethod.GET)
	public void deletepost(HttpServletResponse response,String postid,String managerid) throws IOException
	{
		String json = dao.deletepost(postid,managerid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 置顶帖子
	@RequestMapping(value = "firstpost.action",method = RequestMethod.GET)
	public void firstpost(HttpServletResponse response,String postid,String managerid) throws IOException
	{
		String json = dao.firstpost(postid,managerid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 取消置顶帖子
	@RequestMapping(value = "cancelfirstpost.action",method = RequestMethod.GET)
	public void cancelfirstpost(HttpServletResponse response,String postid,String managerid) throws IOException
	{
		String json = dao.cancelfirstpost(postid,managerid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	
	// 加精华帖子
	@RequestMapping(value = "boutiquepost.action",method = RequestMethod.GET)
	public void boutiquepost(HttpServletResponse response,String postid,String managerid) throws IOException
	{
		String json = dao.boutiquepost(postid,managerid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 取消精华帖子
	@RequestMapping(value = "cancelboutiquepost.action",method = RequestMethod.GET)
	public void cancelboutiquepost(HttpServletResponse response,String postid,String managerid) throws IOException
	{
		String json = dao.cancelboutiquepost(postid,managerid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
}
