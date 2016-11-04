package com.springmvc.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
public class FormDao
{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	/** 发表贴子*/
	@ResponseBody
	public String publishPost(String title,String content,String posterid,String postername)
	{
		try
		{
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));//(TimeZone.getTimeZone("GMT+8:00"));//
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			String sendMassgae = "insert into post(pid,title,content,posterid,pubtime,commentcount,postername,state,isfirst,sorttime) "
					+ "values("+ "'"+ uuid+ "',"+ "'"+ title+ "','"+content+ "','"+ posterid+ "','"+sdf.format(new Date(System.currentTimeMillis()))+ "','" + 0 +"','"+postername+"','"+1+"','"+false+"','"+System.currentTimeMillis()+"')";
			
			jdbcTemplate.execute(sendMassgae);// executeQuery(registerStr);
			
			JSONObject object = new JSONObject();
			object.put("code", 200);
			object.put("message", "发帖成功!");
			
			return object.toString();
		}
		catch (Exception e)
		{
			// TODO: handle exception
			//System.out.println("出现异常" + e.toString());
			JSONObject object = new JSONObject();
			try
			{
				object.put("code", 405);
				object.put("message", "发帖失败!");
			}
			catch (JSONException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return object.toString();
		}
	}
	
	/** 回复帖子*/
	@ResponseBody
	public String publishComment(String content,String commenterid,String commentername,String belongpostid)
	{
		try
		{
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			// 更新帖子表里的排序字段（sorttime），这样可以使回帖后帖子被置到列表首位
			String updatesorttime = "update post set sorttime="+System.currentTimeMillis() + " where pid='"+belongpostid+"'";
			jdbcTemplate.update(updatesorttime);
			
			TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));//(TimeZone.getTimeZone("GMT+8:00"));//
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			String sendMassgae = "insert into comment(cid,content,commenterid,commentername,commenttime,belongpostid,sorttime)"
					+ " values("+ "'"+ uuid+ "',"+ "'"+ content+ "','"+commenterid+ "','"+ commentername+ "','"+sdf.format(new Date(System.currentTimeMillis()))+ "','" + belongpostid +"','"+System.currentTimeMillis()+"')";
			jdbcTemplate.execute(sendMassgae);
			
			// update comment number
			jdbcTemplate.update("update post set commentnum = (select count(1) from comment where belongpostid='"+belongpostid+"') where pid='"+belongpostid+"'");
			
			// 发系统消息提示用户查看论坛
			String mid = UUID.randomUUID().toString().replaceAll("-", "");
			String notiMassgae = "insert into message(mid,senderid,receiveid,message,sendtime,readflag,sendername,ordertime,type) "
					+ "values("+ "'"+ mid+ "',"+ "'"+ commenterid+ "',(select posterid from post where pid='"+belongpostid+"'),'"+ " 有人回复了您的帖子 "+ "','"+sdf.format(new Date(System.currentTimeMillis()))+ "','" + false +"','系统消息',"+System.currentTimeMillis()+",2)";
			jdbcTemplate.update(notiMassgae);
			
			JSONObject object = new JSONObject();
			object.put("code", 200);
			object.put("message", "回帖成功!");
			
			return object.toString();
		}
		catch (Exception e)
		{
			JSONObject object = new JSONObject();
			try
			{
				object.put("code", 405);
				object.put("message", "回帖失败!");
			}
			catch (JSONException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return object.toString();
		}
	}

	/**
	 * 获取评论列表
	 * @author 许仕永(xsy)
	 * des: 
	 * @param belongpostid 评论所属的帖子的id
	 * @param page
	 * @param pagesize
	 * @return
	 */
	@ResponseBody
	public List<Map<String, Object>> getComment(String belongpostid,String page,String pagesize)
	{
		try
		{
			//connSQL();
			List<Map<String, Object>> result = null;
			
			// update read number
			jdbcTemplate.update("update post set readnum = readnum+1 where pid='"+belongpostid+"'");
			
			String sql = "select * from comment where belongpostid='"+belongpostid +"' order by sorttime desc limit 0,"+pagesize;
			//System.out.println("评论 sql="+sql);
//			ResultSet resultSet = jdbcTemplate.executeQuery(sql);
//			result = ResultSetTool.resultSetToJsonArry(resultSet).toString();
			result = jdbcTemplate.queryForList(sql);
//			closeDB();
			return result;
		}
		catch (Exception e)
		{
			System.out.println(" 异常君"+e.toString());
			// TODO: handle exception
		}
		return null;
	}

	public String deletecomment(String commenterid, String belongpostid, String managerid)
	{
		// 校验该账号是否有权限进行此操作
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from forum_manager where uuid='"+managerid+"'");
		if(rowSet.next())
		{
			jdbcTemplate.update("delete from comment where cid='"+commenterid+"' and belongpostid='"+belongpostid+"'");
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 200);
			jsonObject.put("message", "删除成功!");
			return jsonObject.toString();
		}
		else
		{
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "你没有权限进行此操作，请勿非法操作!");
			return jsonObject.toString();
		}
	}

	public String deletepost(String postid, String managerid)
	{
		// 校验该账号是否有权限进行此操作
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from forum_manager where uuid='"+managerid+"'");
		if(rowSet.next())
		{
			// 删除帖子
			jdbcTemplate.update("delete from post where pid='"+postid+"'");
			// 删除帖子下的评论
			jdbcTemplate.update("delete from comment where belongpostid='"+postid+"'");
			
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 200);
			jsonObject.put("message", "删除成功!");
			return jsonObject.toString();
		}
		else
		{
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "你没有权限进行此操作，请勿非法操作!");
			return jsonObject.toString();
		}
	}

	// 置顶
	public String firstpost(String postid, String managerid)
	{ 
		// 校验该账号是否有权限进行此操作
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from forum_manager where uuid='"+managerid+"'");
		if(rowSet.next())
		{
			// 检查已有的置顶文章,一个版面只允许三篇置顶存在
			SqlRowSet rowSet2 = jdbcTemplate.queryForRowSet("select count(*) from post where isfirst='true'");
			if(rowSet2.next())
			{
				int count = rowSet2.getInt("count(*)");
				if(count >= 3)
				{
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("code", 200);
					jsonObject.put("message", "置顶失败,一个版面最多只允许三篇置顶贴同时存在!");
					return jsonObject.toString();
				}
			}
			// //时间加上三天，等于置顶三天  1469338317836
			jdbcTemplate.update("update post set isfirst='true',sorttime=sorttime*2 where pid='"+postid+"'");
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 200);
			jsonObject.put("message", "置顶成功!");
			return jsonObject.toString();
		}
		else
		{
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "你没有权限进行此操作，请勿非法操作!");
			return jsonObject.toString();
		}
	}
	
	// 取消置顶
	public String cancelfirstpost(String postid, String managerid)
	{ 
		// 校验该账号是否有权限进行此操作
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from forum_manager where uuid='"+managerid+"'");
		if(rowSet.next())
		{
			jdbcTemplate.update("update post set isfirst='false',sorttime="+System.currentTimeMillis()+" where pid='"+postid+"'");
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 200);
			jsonObject.put("message", "取消置顶成功!");
			return jsonObject.toString();
		}
		else
		{
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "你没有权限进行此操作，请勿非法操作!");
			return jsonObject.toString();
		}
	}

	public String boutiquepost(String postid, String managerid)
	{
		// 校验该账号是否有权限进行此操作
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from forum_manager where uuid='"+managerid+"'");
		if(rowSet.next())
		{
			jdbcTemplate.update("update post set isboutique='true' where pid='"+postid+"'");
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 200);
			jsonObject.put("message", "加精华成功!");
			return jsonObject.toString();
		}
		else
		{
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "你没有权限进行此操作，请勿非法操作!");
			return jsonObject.toString();
		}
	}

	public String cancelboutiquepost(String postid, String managerid)
	{
		// 校验该账号是否有权限进行此操作
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from forum_manager where uuid='"+managerid+"'");
		if(rowSet.next())
		{
			jdbcTemplate.update("update post set isboutique='false' where pid='"+postid+"'");
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 200);
			jsonObject.put("message", "取消加精华成功!");
			return jsonObject.toString();
		}
		else
		{
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "你没有权限进行此操作，请勿非法操作!");
			return jsonObject.toString();
		}
	}
}
