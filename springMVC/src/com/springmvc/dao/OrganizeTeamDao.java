package com.springmvc.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class OrganizeTeamDao
{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Object sendSystemMessage(String receiver_uuid, String keyword)
	{
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 发系统消息提示
		String mid = UUID.randomUUID().toString().replaceAll("-", "");
		String notiMassgae = "insert into message(mid,senderid,receiveid,message,sendtime,readflag,sendername,ordertime,type) "
				+ "values("+ "'"+ mid+ "','0000','"+receiver_uuid+"','"+keyword+"','"+sdf.format(new Date(System.currentTimeMillis()))+"','false','组队消息',"+System.currentTimeMillis()+",2)";
		jdbcTemplate.update(notiMassgae);
		return null;
	}
	
	public Object buildTeam(String teammaster, String member,int type)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select countryid,uuid,teamid,name,grade from person where uuid='"+teammaster+"' or uuid='"+member+"'");
		String teamidmaster = null;
		String teamidmember = null;
		String uuidmaster = null;
		String uuidmember = null;
		String name = null;
		String name2 = null;
		String countryidmember = null;
		String countryidmaster = null;
		int grademaster = 0;
		int grademember = 0;
		int count = 0;
		
		while (rowSet.next())
		{
			count ++;
			String uuid = rowSet.getString("uuid");
			
			if(uuid != null && uuid.equals(member))
			{
				teamidmember = rowSet.getString("teamid");
				uuidmember = uuid;
				name2 = rowSet.getString("name");
				grademember = rowSet.getInt("grade");
				countryidmember = rowSet.getString("countryid");
			}
			else if(uuid != null && uuid.equals(teammaster))
			{
				teamidmaster = rowSet.getString("teamid");
				uuidmaster = uuid;
				name = rowSet.getString("name");
				grademaster = rowSet.getInt("grade");
				countryidmaster = rowSet.getString("countryid");
			}
		}
		
		
		
		if(type == 2)
		{
			jdbcTemplate.update("delete from team_request where teammasterid='"+teammaster+"' and teammemberid='"+member+"'");
			sendSystemMessage(uuidmaster, name2+"拒绝了你的组队邀请。");
			JSONObject object = new JSONObject();
			object.put("code", 200);
			object.put("message", "你成功拒绝了"+name+"的组队邀请。");
			jdbcTemplate.update("delete from team_request where teammasterid='"+teammaster+"' and teammemberid='"+member+"'");
			return object.toString();
		}
		
		if(count == 0)
		{
			JSONObject object = new JSONObject();
			object.put("code", 201);
			object.put("message", "未查询到用户信息");
			return object.toString();
		}
		
		if(!countryidmaster.equals(countryidmember))
		{
			JSONObject object = new JSONObject();
			object.put("code", 201);
			object.put("message", "私通敌国可是叛国大罪！还是找自己人组队吧！");
			return object.toString();
		}
		
		if(Math.abs(grademaster - grademember) > 5)
		{
			JSONObject object = new JSONObject();
			object.put("code", 201);
			object.put("message", "等级差距太大，无法组队！（最大等级差5级）");
			return object.toString();
		}
		
			// 已有组队
			if( teamidmember != null && !"0".equals(teamidmember))
			{
				JSONObject object = new JSONObject();
				object.put("code", 201);
				object.put("message", "对方已经在另一个队伍里了，组队失败!");
				return object.toString();
			}
			else
			{
				// 邀请
				if(type == 0)
				{
					String requestid = UUID.randomUUID().toString().replaceAll("-", "");	
					jdbcTemplate.update("insert into team_request(teammessageid,teammasterid,teammemberid,messagetime,message) values('"+requestid+"','"+uuidmaster+"','"+uuidmember+"',UNIX_TIMESTAMP(NOW()),'"+(name+"邀请你加入队伍")+"')");
					
					JSONObject object = new JSONObject();
					object.put("code", 200);
					object.put("message", "你成功向 ["+name2+"] 发出组队邀请。");
					return object.toString();
				}
				else
				{
					if(type == 1)
					{
						// 已存在队伍
						if( teamidmaster != null && !"0".equals(teamidmaster))
						{
							// 1.检查人数是否已满
							int memberNumber = jdbcTemplate.queryForInt("select count(1) from team_list where member < maxmenber and teamid='"+teamidmaster+"'");
							if(memberNumber == 0)
							{
								JSONObject object = new JSONObject();
								object.put("code", 201);
								object.put("message", "组队失败！队伍已满员");
								return object.toString();
							}
							else
							{
								// 2.更新个人队伍id
								jdbcTemplate.update("update person set teamid='"+teamidmaster+"' where uuid='"+uuidmaster+"' or uuid='"+uuidmember+"'");
								
								// 3.更新队伍人数
								jdbcTemplate.update("update team_list set member=menber+1 where teamid='"+teamidmaster+"'");
								
								// 4.删除邀请
								jdbcTemplate.update("delete from team_request where teammasterid='"+teammaster+"' and teammemberid='"+member+"'");
								
								JSONObject object = new JSONObject();
								object.put("code", 200);
								object.put("message", "你成功加入"+name+"的队伍。");
								
								sendSystemMessage(teamidmaster, "["+name2+"] 同意加入你的队伍,你晋级为队长了。");
								
								return object.toString();
							}
						}
						else
						{
							String teamid = UUID.randomUUID().toString().replaceAll("-", "");
							
							String sql = "insert into team_list(teamid,teamname,teamcaptainid,teamcreatetime,member) values('"+teamid+"','"+(name+"战队")+"','"+uuidmaster+"',UNIX_TIMESTAMP(NOW()),2)";
							// 1.创建队伍
							jdbcTemplate.update(sql);
							// 2.更新个人队伍id
							jdbcTemplate.update("update person set teamid='"+teamid+"' where uuid='"+uuidmaster+"' or uuid='"+uuidmember+"'");
							// 3.删除邀请
							jdbcTemplate.update("delete from team_request where teammasterid='"+teammaster+"' and teammemberid='"+member+"'");

							
							JSONObject object = new JSONObject();
							object.put("code", 200);
							object.put("message", "你成功加入"+name+"的队伍。");
							
							sendSystemMessage(teamidmaster, "["+name2+"] 同意加入你的队伍,你晋级为队长了。");
							
							return object.toString();
						}
					}
				}
			}
			return null;
	}
//
//	// 回应
//	public Object responeBuildTeam(String teammaster, String member, int type)
//	{
//		return null;
//	}

	public String getinvitation(String uuid)
	{
		String queryInvitation = "select teammasterid,teammemberid,(select name from person where uuid=teammasterid) as mname from team_request where teammemberid ='"+uuid+"'";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(queryInvitation);
		if(rowSet.next())
		{
			String teammemberid = rowSet.getString("teammemberid");
			String teammasterid = rowSet.getString("teammasterid");
			String teammastername = rowSet.getString("mname");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 200);
			jsonObject.put("teammasterid", teammasterid);
			jsonObject.put("teammemberid", teammemberid);
			jsonObject.put("mess", teammastername+"邀请你加入队伍。");
			return jsonObject.toString();
		}
		
		return null;
	}
}
