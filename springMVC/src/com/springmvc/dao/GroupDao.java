package com.springmvc.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class GroupDao
{
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// get group list
	public String getGroupList(String page,String pagesize)
	{
		JSONObject jsonObject = new JSONObject();
		
		List<Map<String, Object>> map = jdbcTemplate.queryForList("select group_desc,groupid,group_name,(select name from person where uuid=group_master_uuid) as group_master,(select name from person where uuid=group_master_uuid2) as group_master2,group_count,group_grade from group_list where group_state=1 LIMIT "+page+","+pagesize);
		if(map != null)
		{
			jsonObject.put("code", 200);
			jsonObject.put("data", map);
			jsonObject.put("message", "获取列表成功!");
			return jsonObject.toString();
		}
		else
		{
			jsonObject.put("code", 201);
			jsonObject.put("message", "没有数据");
			
			return jsonObject.toString();
		}
	}
	
	// request join group 申请加入
	public String requestJoinGroup(String uuid,String groupid)
	{
		// 1.valid is aleady have group
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select name,groupid,(select group_master_uuid from group_list where groupid='"+groupid+"') as master_uuid from person where uuid='"+uuid+"'");
		if(rowSet.next())
		{
			String master_uuid = rowSet.getString("master_uuid");
			String group_id = rowSet.getString("groupid");
			String name = rowSet.getString("name");
			if("0000".equals(group_id))
			{
				String applyid = UUID.randomUUID().toString().replaceAll("-", "");
				
				SqlRowSet rowSet2 = jdbcTemplate.queryForRowSet("select applyid from group_apply_list where applyer_uuid='"+uuid+"' and apply_groupid='"+groupid+"'");
				if(rowSet2.next())
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code", 201);
					jsonObject.put("message", "你已经申请加入，不必重复申请!");
					return jsonObject.toString();
				}
				else
				{
					jdbcTemplate.update("insert into group_apply_list(applyid,applyer_uuid,apply_groupid,apply_state) values('"+applyid+"','"+uuid+"','"+groupid+"',1"+")");
					
					TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));//(TimeZone.getTimeZone("GMT+8:00"));//
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					// 发系统消息提示
					String mid = UUID.randomUUID().toString().replaceAll("-", "");
					String notiMassgae = "insert into message(mid,senderid,receiveid,message,sendtime,readflag,sendername,ordertime,type) "
							+ "values("+ "'"+ mid+ "','0000','"+master_uuid+"','【"+name+"】申请加入你的部曲!','"+sdf.format(new Date(System.currentTimeMillis()))+"','false','部曲通知',"+System.currentTimeMillis()+",2)";
					jdbcTemplate.update(notiMassgae);

					
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code", 200);
					jsonObject.put("message", "申请已成功提交,等待统领审核！");
					return jsonObject.toString();
				}
			}
			else
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "你已经在另一个部曲，不能加入多个部曲! 如果要加入新部曲，请先退出当前部曲。");
				return jsonObject.toString();
			}
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", 201);
		jsonObject.put("message", "申请失败！");
		return jsonObject.toString();
	}

	// get join request list  获取申请列表
	public String getJoinList(String uuid, String groupid)
	{
//		System.out.println("uuu = "+"select group_name from group_list where (group_master_uuid='"+uuid+"' or group_master_uuid2='"+uuid+"') and groupid='"+groupid+"'");
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select group_name from group_list where (group_master_uuid='"+uuid+"' or group_master_uuid2='"+uuid+"') and groupid='"+groupid+"'");
		if(rowSet.next())
		{
			// id is valid 
			List<Map<String, Object>> list = jdbcTemplate.queryForList("select applyid,applyer_uuid as uuid,(select name from person where uuid=applyer_uuid) as name,(select grade from person where uuid=applyer_uuid) as grade,(select countryname from person where uuid=applyer_uuid) as country from group_apply_list where apply_groupid='"+groupid+"'");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 200);
			jsonObject.put("data", list);
			jsonObject.put("message", "获取列表成功!");
			return jsonObject.toString();
		}
		else
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "你没有权限操作!");
			return jsonObject.toString();
		}
	}

	// 批准申请
	public String acceptJoin(String applyer_uuid, String groupid)
	{
		//System.out.println("select applyid from group_apply_list where applyer_uuid='"+applyer_uuid+"' and apply_groupid='"+groupid+"'");
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select applyid from group_apply_list where applyer_uuid='"+applyer_uuid+"' and apply_groupid='"+groupid+"'");
		if(rowSet.next())
		{
			SqlRowSet queryName = jdbcTemplate.queryForRowSet("select group_name from group_list where groupid='"+groupid+"'");
			queryName.next();
			String name = queryName.getString("group_name");
			// delete all record of this man or woman
			jdbcTemplate.update("delete from group_apply_list where applyer_uuid='"+applyer_uuid+"'");
		    // update person's group_id
			jdbcTemplate.update("update person set groupid='"+groupid+"' where uuid='"+applyer_uuid+"'");
			
			// update group menber number
			jdbcTemplate.update("update group_list set group_count=group_count+1 where groupid='"+groupid+"'");
			
			TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));//(TimeZone.getTimeZone("GMT+8:00"));//
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// 发系统消息提示
			String mid = UUID.randomUUID().toString().replaceAll("-", "");
			String notiMassgae = "insert into message(mid,senderid,receiveid,message,sendtime,readflag,sendername,ordertime,type) "
					+ "values("+ "'"+ mid+ "','0000','"+applyer_uuid+"','你的加入部曲【"+name+"】的申请已经被批准!','"+sdf.format(new Date(System.currentTimeMillis()))+"','false','部曲通知',"+System.currentTimeMillis()+",2)";
			jdbcTemplate.update(notiMassgae);
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 200);
			jsonObject.put("message", "已成功批准加入部曲!");
			return jsonObject.toString();
		}
		else
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "没有查询到这个人的申请记录!");
			return jsonObject.toString();
		}
	}

	// 拒绝申请
	public String refuseJoin(String applyer_uuid, String groupid)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select applyid from group_apply_list where applyer_uuid='"+applyer_uuid+"' and apply_groupid='"+groupid+"'");
		if(rowSet.next())
		{
			SqlRowSet queryName = jdbcTemplate.queryForRowSet("select group_name from group_list where groupid='"+groupid+"'");
			queryName.next();
			String name = queryName.getString("group_name");
			// delete record only condition of this group
			jdbcTemplate.update("delete from group_apply_list where applyer_uuid='"+applyer_uuid+"' and apply_groupid='"+groupid+"'");
			
			TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));//(TimeZone.getTimeZone("GMT+8:00"));//
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			// 发系统消息提示
			String mid = UUID.randomUUID().toString().replaceAll("-", "");
			String notiMassgae = "insert into message(mid,senderid,receiveid,message,sendtime,readflag,sendername,ordertime,type) "
					+ "values("+ "'"+ mid+ "','0000','"+applyer_uuid+"','你的申请加入【"+name+"】的请求被拒绝了!','"+sdf.format(new Date(System.currentTimeMillis()))+"','false','部曲通知',"+System.currentTimeMillis()+",2)";
			jdbcTemplate.update(notiMassgae);
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 200);
			jsonObject.put("message", "已成功拒绝此人的加入请求!");
			return jsonObject.toString();
		}
		else
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "没有查询到这个人的申请记录!");
			return jsonObject.toString();
		}
	}

	// get group info
	public String getGroupInfo(String groupid)
	{
		try
		{
			Map<String, Object> rowSet = jdbcTemplate.queryForMap("select * from group_list where groupid='"+groupid+"'");
			if(rowSet != null)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 200);
				jsonObject.put("data", rowSet);
				jsonObject.put("message", "成功获取数据!");
				return jsonObject.toString();
			}
			else
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "没有查询到相关信息!");
				return jsonObject.toString();
			}
		}
		catch (org.springframework.dao.EmptyResultDataAccessException e)
		{
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", 201);
		jsonObject.put("message", "你没有加入任何组织!没有查询到相关信息!");
		return jsonObject.toString();
	}

	public String lookup(String likename)
	{
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select group_desc,groupid,group_name,(select name from person where uuid=group_master_uuid) as group_master,(select name from person where uuid=group_master_uuid2) as group_master2,group_count,group_grade from group_list where group_state=1 and group_name like '%"+likename+"%'");
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", 200);
		jsonObject.put("message", "获取查询结果成功!");
		jsonObject.put("data", list);
		
		return jsonObject.toString();
	}

	public String getGroupMemberList(String groupid)
	{
//		String uuid;
//		String name;
//		String position;
//		String country;
//		int online ;
//		int grade;
		JSONObject jsonObject = new JSONObject();
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select uuid,name,position,countryname as country,grade from person where groupid='"+groupid+"'");
		jsonObject.put("code", 200);
		jsonObject.put("message", "获取部曲成员成功!");
		jsonObject.put("data", list);
		return jsonObject.toString();
	}

	public String dismissed(String master_uuid, String bedismisser_uuid, String groupid)
	{
		if(master_uuid.equals(bedismisser_uuid))
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "你不能开除自己!");
			return jsonObject.toString();
		}
		
		// 第一层验证，开除者是否是统领或副统领的权限
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select group_master_uuid,group_name from group_list where group_master_uuid='"+master_uuid+"' or group_master_uuid2='"+master_uuid+"'");
		if(rowSet.next())
		{
			String group_master_uuid = rowSet.getString("group_master_uuid");
			String group_name = rowSet.getString("group_name");
			if(bedismisser_uuid.equals(group_master_uuid))
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "你想造反吗!竟敢试图开除放逐统领!");
				return jsonObject.toString();
			}
			
			// 第二层验证，严重此人是否是次部曲的成员
			SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select name from person where groupid='"+groupid+"' and uuid='"+bedismisser_uuid+"'");
			if(sqlRowSet.next())
			{
				jdbcTemplate.update("update person set groupid='0000' where uuid='"+bedismisser_uuid+"' and groupid='"+groupid+"'");
				// update group menber number
				jdbcTemplate.update("update group_list set group_count=group_count-1 where groupid='"+groupid+"'");
				
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));//(TimeZone.getTimeZone("GMT+8:00"));//
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// 发系统消息提示
				String mid = UUID.randomUUID().toString().replaceAll("-", "");
				String notiMassgae = "insert into message(mid,senderid,receiveid,message,sendtime,readflag,sendername,ordertime,type) "
						+ "values("+ "'"+ mid+ "','0000','"+bedismisser_uuid+"','你已经被部曲管理从【"+group_name+"】开除出去了!','"+sdf.format(new Date(System.currentTimeMillis()))+"','false','部曲通知',"+System.currentTimeMillis()+",2)";
				jdbcTemplate.update(notiMassgae);
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 200);
				jsonObject.put("message", "你成功开除了此人!");
				return jsonObject.toString();
			}
			else
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "警告，非法操作，后天记录一次!");
				return jsonObject.toString();
			}
		}
		else
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "你没有进行此操作的权限");
			return jsonObject.toString();
		}
	}

	public String creategroup(String groupidname,String uuid)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select money,groupid from person where uuid='"+uuid+"'");
		if(rowSet.next())
		{
			long money = rowSet.getLong("money");
			String groupid = rowSet.getString("groupid");
			if(!"0000".equals(groupid))
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "你已经在部曲组织中，需要先退出组织才能再创建!");
				return jsonObject.toString();
			}
			
			if(money < (500000))
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "你没有那么多钱!");
				return jsonObject.toString();
			}
			
			groupidname = groupidname.replace(" ", "");
			if(groupidname == null || "".equals(groupidname.trim()))
			{
				JSONObject object = new JSONObject();
				object.put("code", 404);
				object.put("message", "不能使用空格作为名字!");
				return object.toString();
			}
			
			String[] cannotuse = new String[]{"艹","干","cnm","CNM","cao","ma","主管","B","b","13","贱","策划","群主","妓","设计","阉","鸡鸡","鸡巴","卵","洞","穴","管理","GM","逼","妈","操","你妈","尼妈","高潮","出水","淫","管理","强奸","乱伦","毛泽东","江泽民","邓小平","周恩来","习近平","温家宝","日","狗","猪","烂","玄","*","@","$","%","&"};
			for (int i = 0; i < cannotuse.length; i++)
			{
				if(groupidname.contains(cannotuse[i]))
				{
					JSONObject object = new JSONObject();
					object.put("code", 404);
					object.put("message", "名称中包含禁用字!");
					return object.toString();
				}
			}
			
			SqlRowSet rowSet2 = jdbcTemplate.queryForRowSet("select group_name from group_list where group_name='"+groupidname+"'");
			if(rowSet2.next())
			{
				JSONObject object = new JSONObject();
				object.put("code", 404);
				object.put("message", "这个名称已经有人使用了:"+groupidname);
				return object.toString();
			}
			
			String gid = UUID.randomUUID().toString().replaceAll("-", "");
			jdbcTemplate.update("insert into group_list(groupid,group_name,group_master_uuid,group_create_time) values('"+gid+"','"+groupidname+"','"+uuid+"',UNIX_TIMESTAMP()"+")");
			jdbcTemplate.update("update person set money=money-500000,groupid='"+gid+"' where uuid='"+uuid+"'");
			JSONObject object = new JSONObject();
			object.put("code", 200);
			object.put("message", "你成功创建了一个部曲组织["+groupidname+"].");
			return object.toString();
		}
		return null;
	}

	public String backedout(String uuid, String groupid)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select name,(select group_master_uuid from group_list where groupid='"+groupid+"') as group_master_uuid from person where uuid='"+uuid+"' and groupid='"+groupid+"'");
		if(rowSet.next())
		{
			String group_master_uuid = rowSet.getString("group_master_uuid");
			String name = rowSet.getString("name");
			
		    // update person's group_id
			jdbcTemplate.update("update person set groupid='0000' where uuid='"+uuid+"'");
			
			// update group menber number
			jdbcTemplate.update("update group_list set group_count=group_count-1 where groupid='"+groupid+"'");
			
			TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));//(TimeZone.getTimeZone("GMT+8:00"));//
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// 发系统消息提示
			String mid = UUID.randomUUID().toString().replaceAll("-", "");
			String notiMassgae = "insert into message(mid,senderid,receiveid,message,sendtime,readflag,sendername,ordertime,type) "
					+ "values("+ "'"+ mid+ "','0000','"+group_master_uuid+"','部曲成员【"+name+"】已经退出你的部曲！','"+sdf.format(new Date(System.currentTimeMillis()))+"','false','部曲通知',"+System.currentTimeMillis()+",2)";
			jdbcTemplate.update(notiMassgae);

			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 200);
			jsonObject.put("message", "你已经成功退出了部曲!");
			return jsonObject.toString();
		}
		else
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "没有查询到你的相关信息!");
			return jsonObject.toString();
		}
//		// delete all record of this man or woman
//		jdbcTemplate.update("delete from group_apply_list where applyer_uuid='"+applyer_uuid+"'");
//	    // update person's group_id
//		jdbcTemplate.update("update person set groupid='"+groupid+"' where uuid='"+applyer_uuid+"'");
//		
//		// update group menber number
//		jdbcTemplate.update("update group_list set group_count=group_count+1 where groupid='"+groupid+"'");
	}

	
}
