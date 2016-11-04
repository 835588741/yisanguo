package com.springmvc.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import net.sf.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
public class MessageDao
{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private String[] hpstate = new String[]{"你怎么了，看起来似乎受了点伤","你受了很重的伤，赶紧去休息一下吧","你伤的太重了，我背你去客栈吧","记得保护好自己，受伤就去休息或吃药"};
	private String[] gradestate = new String[]{"你还小能力还不足以保护自己，得加紧锻炼了","如果打不过别人时,就选择逃跑吧,跑到安全的地方","被人追杀时赶紧跑到守卫叔叔身边,他会保护你","你的能力已经很强了,可以保护自己保护别人了","你的成就已然是登峰造极了。"};

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Object sendSystemMessage(String receiver_uuid, String keyword,String type)
	{
//		if("1".equals(type))
//		{
//		}
//		else if("2".equals(type))
//		{
//		}
//		else if("3".equals(type))
//		{
//		}
//		
//		
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 发系统消息提示
		String mid = UUID.randomUUID().toString().replaceAll("-", "");
		String notiMassgae = "insert into message(mid,senderid,receiveid,message,sendtime,readflag,sendername,ordertime,type) "
				+ "values("+ "'"+ mid+ "','0000','"+receiver_uuid+"','"+keyword+"','"+sdf.format(new Date(System.currentTimeMillis()))+"','false','系统消息',"+System.currentTimeMillis()+",2)";
		jdbcTemplate.update(notiMassgae);
		return null;
	}


	/** 发送消息插入新数据 
	 * @param isreplay */
	@ResponseBody
	public String sendMassage(String table, String receiveId, String senderId, String message,String sendername, String isreplay,String mid)
	{  
		try
		{
			int iscansend = jdbcTemplate.queryForInt("select count(1) from black_list_for_player where (btmasterid='"+receiveId+"' and bttarget='"+senderId+"') or (btmasterid='"+senderId+"' and bttarget='"+receiveId+"')");
			if(iscansend >= 1)
			{
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "你已被对方拉黑或对方在你的黑名单中，无法发送私信给对方");
				return jsonObject.toString();
			}
			
			if(isreplay != null && "2".equals(isreplay))
			{
				// replay
				jdbcTemplate.update("update message set replaymessage='"+message+"' where mid='"+mid+"'");
				
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				String uuid = UUID.randomUUID().toString().replaceAll("-", "");
				String sendMassgae = "insert into message(mid,senderid,receiveid,message,sendtime,readflag,sendername,ordertime) "
						+ "values("+ "'"+ uuid+ "',"+ "'"+ senderId+ "','"+receiveId+ "','"+ message+ "','"+sdf.format(new Date(System.currentTimeMillis()))+ "','" + false +"','"+sendername+"',"+System.currentTimeMillis()+")";
				jdbcTemplate.execute(sendMassgae);

				JSONObject object = new JSONObject();
				object.put("code", 200);
				object.put("message", "回复发送成功!");
				
				return object.toString();
			}
			else
			{
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				String uuid = UUID.randomUUID().toString().replaceAll("-", "");
				String sendMassgae = "insert into message(mid,senderid,receiveid,message,sendtime,readflag,sendername,ordertime) "
						+ "values("+ "'"+ uuid+ "',"+ "'"+ senderId+ "','"+receiveId+ "','"+ message+ "','"+sdf.format(new Date(System.currentTimeMillis()))+ "','" + false +"','"+sendername+"',"+System.currentTimeMillis()+")";
				jdbcTemplate.execute(sendMassgae);
				
				JSONObject object = new JSONObject();
				object.put("code", 200);
				object.put("message", "消息发送成功!");
				
				return object.toString();
			}
		}
		catch (Exception e)
		{
			JSONObject object = new JSONObject();
			try
			{
				object.put("code", 405);
				object.put("message", "发送失败!异常错误：code=405 error="+e.toString());
			}
			catch (JSONException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return object.toString();
		}
	}


	@ResponseBody
	public String getMessage(String uuid,String isread)
	{
		// 消息数据
		String sqlMessage = "select * from message where receiveid='"+uuid+"' and readflag='"+isread+"'  order by ordertime desc limit 0,50";
		
		if("all".equals(isread))
			sqlMessage = "select * from message where receiveid='"+uuid+"' order by ordertime desc limit 0,50";
		
//		ResultSet sqlQueryMSGSet = jdbcTemplate.queryForList(sqlMessage);
		List<Map<String, Object>> stringMSG =  jdbcTemplate.queryForList(sqlMessage);//ResultSetTool.resultSetToJsonArry(sqlQueryMSGSet).toString();					
		JSONArray arrayMsg = JSONArray.fromObject(stringMSG);
		
		if("false".equals(isread) || "all".equals(isread))
		{
//			// 未读消息设置为已读
			String resetRead = "update message set readflag='true' where receiveid ='"+uuid+"'";
			jdbcTemplate.update(resetRead);
		}
		
		return arrayMsg.toString();
	}
	
	
	@ResponseBody
	public String clearMessage(String uuid)
	{
		// 消息数据
		String sqlMessage = "delete from message where receiveid='"+uuid+"'";
		
		String result = "";
//		ResultSet sqlQueryMSGSet = jdbcTemplate.queryForList(sqlMessage);
		try
		{
			int i = jdbcTemplate.update(sqlMessage);//.queryForList(sqlMessage);//ResultSetTool.resultSetToJsonArry(sqlQueryMSGSet).toString();					
			if(i > 0)
				result = "成功清空所有私信";
			else
				result = "清空失败!";
		}
		catch (Exception e)
		{
			// TODO: handle exception
			result = "服务器处理错误";
		}
		return result;
	}

	public String sendpublicmessage(String uuid,String message)
	{
		String mid = UUID.randomUUID().toString().replaceAll("-", "");
		SqlRowSet isvalid = jdbcTemplate.queryForRowSet("select name,uuid,position from person where uuid='"+uuid+"'");
		if(!isvalid.next())
		{
			JSONObject object = new JSONObject();
			try
			{
				object.put("code", 201);
				object.put("message", "消息发送失败!未查询到您的用户信息！");
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return object.toString();
		}
		else
		{
			String authorname = isvalid.getString("name");
			String position = isvalid.getString("position");
			
			jdbcTemplate.update("insert into publicchat values('"+mid+"','"+message+"','"+uuid+"','"+authorname+"',UNIX_TIMESTAMP(NOW()),NOW(),0,'"+position+"');");
			JSONObject object = new JSONObject();
			try
			{
				object.put("code", 200);
				object.put("message", "消息发送成功!");
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return object.toString();
		}
	}

	public String getpublicmessage(String uuid)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select authorname,message,position from publicchat where state = 1");
		if(rowSet.next())
		{
			String mess = rowSet.getString("message");
			String authorname = rowSet.getString("authorname");
			String position = rowSet.getString("position");
			
			SqlRowSet rowSet2 = jdbcTemplate.queryForRowSet("select teammessage from team_list where teamid=(select teamid from person where uuid='"+uuid+"')");
			String teammessage = null;
			if(rowSet2.next())
			{
				teammessage = rowSet2.getString("teammessage");
			}
			
			
			JSONObject object = new JSONObject();
			try
			{
				object.put("code", 200);
				object.put("message", mess);
				object.put("position", position);
				object.put("authorname", authorname);
				object.put("teammessage", teammessage);
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return object.toString();
		}
		else
		{
			SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select *,(select notice from notice where id=3) as notice,(select author from notice where id=3) as author from person where uuid='"+uuid+"'");
			String message = "好好保护自己，不要让人伤了你";
			if(sqlRowSet.next())
			{
				String notice = sqlRowSet.getString("notice");
				String author = sqlRowSet.getString("author");
				
				if(notice != null && !"".equals(notice))
				{
					SqlRowSet rowSet2 = jdbcTemplate.queryForRowSet("select teammessage from team_list where teamid=(select teamid from person where uuid='"+uuid+"')");
					String teammessage = null;
					if(rowSet2.next())
					{
						teammessage = rowSet2.getString("teammessage");
					}
					
					JSONObject object = new JSONObject();
					try
					{
						if(author == null || !"".equals(author))
							author = "系统通知";
						object.put("code", 207);
						object.put("message","<font color='red'>【"+author+"】: </font><font color='blue'>"+notice+"</font>");
						object.put("teammessage", teammessage);
					}
					catch (JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return object.toString();
				}
				
				int hp = sqlRowSet.getInt("hp");
				int grade = sqlRowSet.getInt("grade");
				int gender = sqlRowSet.getInt("gender");
				String name = sqlRowSet.getString("name");
				long money = sqlRowSet.getLong("money");
				String country = sqlRowSet.getString("countryname");
				Random random = new Random();
				int rand = random.nextInt(7);
				// 从血量总结反馈
				if(rand == 0)
				{
					int maxhp = sqlRowSet.getInt("maxhp");
					if(hp == maxhp)
						message = "你现在气血充盈,想想去哪做什么好呢";
					else if(hp < maxhp*0.8 && hp > maxhp * 0.6)
						message = hpstate[0];
					else if(hp < maxhp*0.6 && hp > maxhp * 0.4)
						message = hpstate[1];
					else if(hp < maxhp*0.4 && hp > maxhp * 0.2)
						message = hpstate[2];
					else
						message = hpstate[3];
				}
				// 从性别总结反馈
				else if(rand == 1)
				{
					if(gender == 1)
					{
						int r = random.nextInt(4);
						if(r == 0)
							message = "公子,咱们去大街上调戏一下良家妇女吧";
						else if(r == 1)
							message = "公子,好闷啊，我们去四处走走逛逛吧";
						else if(r == 2)
							message = "公子,你有喜欢的小姐吗?";
						else if(r == 3)
							message = "公子,你的眼睛很好看，很有神";
					}
					else
					{
						int r = random.nextInt(4);
						if(r == 0)
							message = "小姐,咱们去大街上调戏一下哪家公子吧";
						else if(r == 1)
							message = "小姐,走，去逛街去,看看四处风景";
						else if(r == 2)
							message = "小姐，你可有意中人了？";
						else if(r == 3)
							message = "小姐，仔细一看你长得真清秀可人";
					}
				}
				// 从等级总结反馈
				else if(rand == 2)
				{
					if(grade < 15)
					{
						int r = random.nextInt(7);
						if(r == 0)
							message = "如果想提升等级，就去找相对自己同等的怪打";
						else if(r == 1)
							message = "如果有钱可以多买一下药放在身上，这样打怪没血可以吃药";
						else if(r == 2)
							message = "记住装备很重要，攒点钱买装备";
						else if(r == 3)
							message = "如果没钱就去找大哥哥大姐姐们要吧";
						else if(r == 4)
							message = "打怪会有33%的机率爆到钱";
						else if(r == 5)
							message = "你15级了，可去入城可以每天领军饷";
						else
							message = gradestate[0];
					}
					else if(grade >= 15 && grade <= 20)
					{
						int r = random.nextInt(6);
						if(r == 0)
							message = "想打怪的话，可以去城市猎场和监狱看看";
						else if(r == 1)
							message = "刚入城不久的你，可以先四处走走，熟悉下环境";
						else if(r == 2)
							message = "国战时可以通过参战获取到很多白银";
						else if(r == 3)
							message = "是不是该换装了，去商店看看没有合适的吧";
						else if(r == 4)
							message = "如果有闲钱可以拿出几两去新手村赌场或者钓鱼台娱乐一下";
						else
							message = gradestate[1];
					}
					else if(grade >= 21 && grade <= 50)
					{
						int r = random.nextInt(4);
						if(r == 0)
							message = "如果打怪闷了，不妨去和小伙伴们聊聊吧";
						else if(r == 1)
							message = "20 - 50级是比较容易升级的阶段";
						else if(r == 2)
							message = "由于等级差会附加属性，等级越高，pk越有利";
						else
							message = gradestate[2];
					}
					else if(grade >= 51 && grade <= 75)
					{
						int r = random.nextInt(4);
						if(r == 0)
							message = "上了50级以上之后，后面的路会越来越来有挑战性";
						else if(r == 1)
							message = "75级是一个小登峰噢";
						else if(r == 2)
							message = "去找小伙伴们切磋切磋刀法吧";
						else
							message = gradestate[3];
					}					
					else if(grade >= 76 && grade <= 100)
					{
						int r = random.nextInt(3);
						if(r == 0)
							message = "你已经突破了小登峰,现在已经是位列高手了";
						else if(r == 1)
						{
							if(gender == 1)
								message = "公子，我们去乐台听听歌吧";
							else
								message = "小姐，我们去乐台听听歌吧";
						}
						else
							message = gradestate[4];
					}					
					else
						message = "要记住等级高了别欺负别人";
				}
				// 从名字总结反馈
				else if(rand == 3)
				{
					if(gender == 1)
						message = "公子你的名字["+name+"]有什么意义吗";
					else
						message = "小姐你的名字["+name+"]有什么意义吗";
				}
				else if(rand == 4)
				{
					if(gender == 1)
						message = "公子";
					else
						message = "小姐";

					if(money <= 800)
					{
						message += "你知道吗，你可能是全三国最穷的人了";
					}
					else if( money > 800 && money <= 5000)
					{
						message += "你身上这点钱还不够我买个冰糖葫芦的,哎";
					}
					else if( money > 5000 && money <= 20000)
					{
						message += "你应该想办法赚点银子了,以后各种东西都需要银子";
					}
					else if( money > 20000 && money <= 100000)
					{
						message += ",现在也算有点积蓄了，好好攒着吧";
					}
					else if( money > 100000 && money <= 300000)
					{
						message += ",你现在算是中上的富人了，跟你一起真有面子";
					}
					else if( money > 100000 && money <= 300000)
					{
						message += "，其实我想说'土豪我们做朋友吧'";
					}
					else if( money > 300000 && money <= 500000)
					{
						message += "，我们去赌场玩一两把吧，娱乐一下";
					}
					else if( money > 500000 && money <= 1000000)
					{
						if(gender == 1)
							message = "已经是富甲一方的["+name+"]公子";
						else
							message = "已经是富甲一方的["+name+"]小姐";
					}
					else if(money > 1000000)
					{
						if(gender == 1)
							message = "已经是富可敌国的["+name+"]公子";
						else
							message = "已经是富可敌国的["+name+"]小姐";
					}
				}
				else if(rand == 5)
				{
					if(gender == 1)
						message = "公子";
					else
						message = "小姐";
					int r = random.nextInt(7);
					if(r == 0)
						message += "为什么要投入"+country+"呢";
					else if(r == 1)
						message += ",南阳是五国中富商最多的一方";
					else if(r == 2)
						message += ",洛阳是五国中高手最多的一方";
					else if(r == 3)
						message += ",新手村是低手最多的地方,但也不乏可造之材";
					else if(r == 4)
						message += ",许昌是野怪爆率最高的一方";
					else if(r == 5)
						message += ",长安是，我也不知道了";
					else if(r == 6)
						message += ",邺城是最远的地方，人口也比较稀少";
				}
				else 
				{
					int r = random.nextInt(6);
					if(r == 0)
						message = "国战是八点开始的";
					else if(r == 1)
						message = "飞流直下三千尺,疑是银河落九天";
					else if(r == 2)
						message = "人生若只如初见，何事秋风悲画扇";
					else if(r == 3)
						message = "天苍苍野茫茫，风吹草地现牛羊";
					else if(r == 4)
						message = "你有脾气去洛阳找吕布单挑啊";
					else if(r == 5)
						message = "柿子还是要挑软的捏，别得罪守卫";
				}
				
				
				SqlRowSet rowSet2 = jdbcTemplate.queryForRowSet("select teammessage from team_list where teamid=(select teamid from person where uuid='"+uuid+"')");
				String teammessage = "";
				if(rowSet2.next())
				{
					teammessage = rowSet2.getString("teammessage");
				}
				
				if(teammessage == null)
					teammessage = "";
				
				JSONObject object = new JSONObject();
				try
				{
					object.put("code", 207);
					object.put("message","<font color='red'>【貂蝉姑娘】:</font> "+message);
					object.put("teammessage", teammessage);
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return object.toString();
			}
			
			JSONObject object = new JSONObject();
			try
			{
				object.put("code", 206);
				object.put("message","未查询到你的用户信息");
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return object.toString();
		}
	}

	
	
	
	
	
	
	
	
	public String pullblacelist(String uuid, String targetuuid)
	{
		if(uuid!=null && targetuuid != null)
		{
			// can not same 
			if(uuid.equals(targetuuid))
			{
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "你不能拉黑自己。");
				return jsonObject.toString();
			}
			
			int count = jdbcTemplate.queryForInt("select count(1) from black_list_for_player where btmasterid='"+uuid+"' and bttarget='"+targetuuid+"'");
			if(count >= 1)
			{
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "对方已经在你的黑名单中，不需要重复拉黑。");
				return jsonObject.toString();
			}
			else
			{
				String mid = UUID.randomUUID().toString().replaceAll("-", "");
				jdbcTemplate.update("insert into black_list_for_player(btid,btmasterid,bttarget) values('"+mid+"','"+uuid+"','"+targetuuid+"')");
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 200);
				jsonObject.put("message", "你成功将对方拉黑了。");
				return jsonObject.toString();
			}
		}
		else
		{
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "操作的数据有误！");
			return jsonObject.toString();
		}
	}

	
	public String getblacklist(String uuid)
	{
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select (select name from person where uuid=bttarget) as fname,bttarget as fuuid from black_list_for_player where btmasterid='"+uuid+"' and bttarget!='0000' limit 0,50");
		if(list != null)
		{
			JSONArray arrayMsg = JSONArray.fromObject(list);
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 200);
			jsonObject.put("message", "黑名单加载成功");
			jsonObject.element("data", arrayMsg);
			return jsonObject.toString();
		}
		else
		{
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "黑名单加载出错，数据异常");
			return jsonObject.toString();
		}
	}

	public String deletefromblack(String uuid, String targetuuid)
	{
		try
		{
			jdbcTemplate.update("delete from black_list_for_player where btmasterid='"+uuid+"' and bttarget='"+targetuuid+"'");
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 200);
			jsonObject.put("message", "删除成功！已将对方从黑名单移除！");
			return jsonObject.toString();
		}
		catch (Exception e)
		{
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "删除失败！数据异常！");
			return jsonObject.toString();
		}
	}
	
}
