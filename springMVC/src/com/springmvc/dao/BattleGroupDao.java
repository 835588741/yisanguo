package com.springmvc.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class BattleGroupDao
{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	Random random = new Random();
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	

	// attack jianta
	public String attack(String uuid, String countryid, String doorareaid)
	{
		//System.out.println("uuid="+uuid+"   城门码"+doorareaid+"   国蚂"+countryid);
//		if(!"0003".equals(uuid) && !"0001".equals(uuid))
//		{
//			net.sf.json.JSONObject jsonObjectPK = new net.sf.json.JSONObject(); 
//			jsonObjectPK.element("code", 201);
//			jsonObjectPK.element("message", "请回吧！现在在测试，不是你能砍的。");
//			return jsonObjectPK.toString();
//		}
		try
		{
			SqlRowSet rowSetValid = jdbcTemplate.queryForRowSet("select areaid,isbattle,enemyid from person where uuid='"+uuid+"'");
			if(rowSetValid.next())
			{
				String isbattle = rowSetValid.getString("isbattle");
				String enemyid = rowSetValid.getString("enemyid");
				
				if("true".equals(isbattle))
				{
					net.sf.json.JSONObject jsonObjectPK = new net.sf.json.JSONObject(); 
					jsonObjectPK.element("code", 201);
					jsonObjectPK.element("enemy", enemyid);
					jsonObjectPK.element("message", "你受到别人攻击了");
					return jsonObjectPK.toString();
				}
			}
			
			if("0000".equals(countryid))
			{
				net.sf.json.JSONObject jsonObjectPK = new net.sf.json.JSONObject(); 
				jsonObjectPK.element("code", 205);
				jsonObjectPK.element("message", "队伍中一名小校大斥你道‘战场是你来的地吗，不要命啦！’，大国交战岂是你山野村夫可以参与的,城市套路深，还是回新村。");
				return jsonObjectPK.toString();
			}
			
			int grade = jdbcTemplate.queryForInt("select grade from person where uuid='"+uuid+"'");
			if(grade <= 15)
			{
				net.sf.json.JSONObject jsonObjectPK = new net.sf.json.JSONObject(); 
				jsonObjectPK.element("code", 205);
				jsonObjectPK.element("message", "你还是个小孩童，根本砍不动，只有在一旁助威摇旗呐喊的份了。");
				return jsonObjectPK.toString();
			}
			
			if("100".equals(doorareaid) || "200".equals(doorareaid) || "300".equals(doorareaid))
			{
				int centerV = 3000;
				int defencevalueForCenter = 5000;
				SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from citydoor where countryid='"+countryid+"'");
				boolean isCan = false;
				
				while (rowSet.next())
				{
					int defencevalue = rowSet.getInt("defencevalue");
					int areaid = rowSet.getInt("doorareaid");
					
					if(areaid==100 || areaid==200 || areaid == 300)
					{
						centerV = defencevalue;
					}
					
					if (defencevalue  <= 0)
					{
						// 是否还可以被攻击
						if(areaid==100 || areaid==200 || areaid == 300)
						{
							JSONObject jsonObject = new JSONObject();
							
							try
							{
								jsonObject.put("code", "203");
								jsonObject.put("message", "旗楼已经被攻破!战争结束!");
							}
							catch (JSONException e1)
							{
								e1.printStackTrace();
							}
							return jsonObject.toString();
						}
						// 是否已经攻破其他四个城门之一
						else
						{
							isCan = true;
						}
					}
				}
				
				
				if(isCan)
				{
					if(centerV == 1)
					{
						jdbcTemplate.update("update citydoor set defencevalue = defencevalue -1 where countryid='"+countryid+"' and doorareaid="+doorareaid +" and isbattle='true'");
						jdbcTemplate.execute("call stop_war");
						
						// 标志国家的胜败
						jdbcTemplate.update("update country set success = 1 where countryname=(SELECT countryname from person where uuid='"+uuid+"')");
						jdbcTemplate.update("update country set success = 2 where countryid='"+countryid+"'");
						
						jdbcTemplate.update("update notice set author='管理员',notice=CONCAT('[',(SELECT countryname from person where uuid='"+uuid+"'),' ',(SELECT name from person where uuid='"+uuid+"'),']',' 率先攻陷 ','[',(SELECT countryname from country where countryid='"+countryid+"'),']',' 获得赏银10两,功勋25点,所在国军饷翻倍.') where id = 1");
						jdbcTemplate.update("update person set money=money+10000 where uuid='"+uuid+"'");
						upposition(uuid, 25);
						
						JSONObject jsonObject = new JSONObject();
						
						try
						{
							jsonObject.put("code", "203");
							jsonObject.put("message", "该国已经被你率兵占领!");
						}
						catch (JSONException e1)
						{
							e1.printStackTrace();
						}
						return jsonObject.toString();
					}
					else if(centerV > 1)
					{
						// 增加士兵抵挡
						int enemyCount = jdbcTemplate.queryForInt("select count(1) from person where state=1 and persontype=11 and areaid="+doorareaid);
						if(enemyCount > 0)
						{
							net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
							jsonObject.put("code", 201);
							jsonObject.put("message", "箭塔还有敌军势力保护，必须先杀光箭塔附近的敌国士兵！");
							return jsonObject.toString();
						}
						
						jdbcTemplate.update("update citydoor set defencevalue = defencevalue -1 where countryid='"+countryid+"' and doorareaid="+doorareaid +" and isbattle='true'");
						jdbcTemplate.update("update person set money=money+50 where uuid='"+uuid+"'");
						JSONObject jsonObject = new JSONObject();
						try
						{
							jsonObject.put("code", "200");
							String message = "你手一滑，咣当一声，手里武器掉落地上了。";
							switch (new Random().nextInt(5))
							{
								case 0:	 message = "你握紧武器，心情激动,想着成功就在眼前了![耐久值:"+centerV+"-1] 为国而战获得50铜板。";break;
								case 1: message = "你发疯似的用刀砍旗楼，木屑横飞！[耐久值:"+centerV+"-1]  为国而战获得50铜板。";break;
								case 2: message = "你疯狂地攻击旗楼![耐久值:"+centerV+"-1]  为国而战获得50铜板。";	break;
								case 3:	 message = "旗楼被砍出了一道道口子![耐久值:"+centerV+"-1]  为国而战获得50铜板。";break;
								case 4:	 message = "攻击![耐久值:"+centerV+"-1]  为国而战获得50铜板。";break;
								default:
									break;
							}
							jsonObject.put("message", message);
						}
						catch (JSONException e1)
						{
							e1.printStackTrace();
						}
						return jsonObject.toString();
					}
					else
					{
						JSONObject jsonObject = new JSONObject();
						
						try
						{
							jsonObject.put("code", "203");
							jsonObject.put("message", "该国已经占领!");
						}
						catch (JSONException e1)
						{
							e1.printStackTrace();
						}
						return jsonObject.toString();
					}
				}
				else
				{
					//jdbcTemplate.update("update citydoor set isbattle = 'false' where countryid='"+countryid+"' and doorareaid="+doorareaid);
					JSONObject jsonObject = new JSONObject();
					try
					{
						jsonObject.put("code", "203");
						jsonObject.put("message", "你必须先攻破任何一个城门才能率领士兵进城攻击旗楼！");
					}
					catch (JSONException e1)
					{
						e1.printStackTrace();
					}
					return jsonObject.toString();
				}
			}
			else
			{
				SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select isbattle,doorareaid,defencevalue from citydoor where countryid='"+countryid+"' and doorareaid="+doorareaid);
				if(rowSet.next())
				{
					int defencevalue = rowSet.getInt("defencevalue");
					
					if(defencevalue <= 0)
					{
						jdbcTemplate.update("update citydoor set isbattle = 'false' where countryid='"+countryid+"' and doorareaid="+doorareaid);
						JSONObject jsonObject = new JSONObject();
						try
						{
							jsonObject.put("code", "203");
							jsonObject.put("message", "该城门已被攻破!前往攻陷广场的旗楼占领这个城市!");
						}
						catch (JSONException e1)
						{
							e1.printStackTrace();
						}
						return jsonObject.toString();
					}
					else 
					{
						// 增加士兵抵挡
						int enemyCount = jdbcTemplate.queryForInt("select count(1) from person where state=1 and persontype=11 and areaid="+doorareaid);
						if(enemyCount > 0)
						{
							net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
							jsonObject.put("code", 201);
							jsonObject.put("message", "箭塔还有敌军势力保护，必须先杀光箭塔附近的敌国士兵！");
							return jsonObject.toString();
						}
						
						if(defencevalue == 1)
						{
							jdbcTemplate.update("update citydoor set defencevalue = defencevalue -1 where countryid='"+countryid+"' and doorareaid="+doorareaid +" and isbattle='true'");
							JSONObject jsonObject = new JSONObject();
							try
							{
								
								jdbcTemplate.update("update notice set author='管理员',notice=CONCAT('[',(SELECT countryname from person where uuid='"+uuid+"'),' ',(SELECT name from person where uuid='"+uuid+"'),']',' 率先攻破 ','[',(SELECT countryname from country where countryid='"+countryid+"'),']','城门',',获得赏银5两,功勋10点.') where id = 1");
								jdbcTemplate.update("update person set money=money+5000 where uuid='"+uuid+"'");
								upposition(uuid, 10);
								
								jsonObject.put("code", "203");
								jsonObject.put("message", "你率先攻破了城门!!!");
							}
							catch (JSONException e1)
							{
								e1.printStackTrace();
							}
							return jsonObject.toString();
						}
						
						jdbcTemplate.update("update citydoor set defencevalue = defencevalue -1 where countryid='"+countryid+"' and doorareaid="+doorareaid +" and isbattle='true'");
						jdbcTemplate.update("update person set money=money+25 where uuid='"+uuid+"'");
						JSONObject jsonObject = new JSONObject();
						try
						{
							jsonObject.put("code", "200");
							String message = "你手一滑，咣当一声，手里武器掉落地上了。";
							switch (new Random().nextInt(5))
							{
								case 0:	 message = "你握紧武器，猛砍猛砸城门![耐久值:"+defencevalue+"-1] 为国而战获得25铜板";break;
								case 1: message = "你发疯似的用刀砍城门，城门留下斑斑裂迹。[耐久值:"+defencevalue+"-1] 为国而战获得25铜板";break;
								case 2: message = "你疯狂地攻击城门![耐久值:"+defencevalue+"-1] 为国而战获得25铜板";	break;
								case 3:	 message = "城门似乎也被砍出了一道口子![耐久值:"+defencevalue+"-1] 为国而战获得25铜板";break;
								case 4:	 message = "冲呀![耐久值:"+defencevalue+"-1] 为国而战获得25铜板";break;
								default:
									break;
							}
							jsonObject.put("message", message);
						}
						catch (JSONException e1)
						{
							e1.printStackTrace();
						}
						return jsonObject.toString();
					}
				}
				else
				{
					JSONObject jsonObject = new JSONObject();
					try
					{
						jsonObject.put("code", "202");
						jsonObject.put("message", "战争还未开始!");
					}
					catch (JSONException e1)
					{
						e1.printStackTrace();
					}
					return jsonObject.toString();
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("异常="+e.toString());
			JSONObject jsonObject = new JSONObject();
			try
			{
				jsonObject.put("code", "201");
				jsonObject.put("message", "你手一滑，咣当一声，手里武器掉落地上了。");
			}
			catch (JSONException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return jsonObject.toString();
		}
	}

	
	public String zhanling(String uuid)
	{
		// 
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select grade,name,countryname,countryid,position,areaid from person where uuid='"+uuid+"'");
		if(rowSet.next())
		{
			int areaid = rowSet.getInt("areaid");
			int grade = rowSet.getInt("grade");
			String name = rowSet.getString("name");
			String country = rowSet.getString("countryname");
			String position = rowSet.getString("position");
			String countryid = rowSet.getString("countryid");
			String operation = "select belongcountry,countryname,battletype,gatewayname,(select COUNT(1) from person where type = 2 and state=1 and areaid=gatewayeastid and persontype = 2) as eastcount,(select COUNT(1) from person where type = 2 and state=1 and areaid=gatewaywestid and persontype = 2) as westtcount,(select COUNT(1) from person where type = 2 and state=1 and areaid=gatewaysouthid and persontype = 2) as southtcount,(select COUNT(1) from person where type = 2 and state=1 and areaid=gatewaynorthid and persontype = 2) as northcount,(select COUNT(1) from person where type = 2 and state=1 and areaid=gatewayid and persontype = 2) as centercount from battle_gateway where  battlerid='"+uuid+"' and gatewayid="+areaid;
			SqlRowSet checkcondition = jdbcTemplate.queryForRowSet(operation);
			if(checkcondition.next())
			{
				String countryname = checkcondition.getString("countryname");
				String gatewayname = checkcondition.getString("gatewayname");
				String belongcountry = checkcondition.getString("belongcountry");
				
				int battletype = checkcondition.getInt("battletype");
				
				if(battletype == 1)
				{
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("code", 201);
					jsonObject.put("message", "没有战事，你现在不能占领要塞！");
					return jsonObject.toString();
				}
				
				int eastcount = checkcondition.getInt("eastcount");
				int westtcount = checkcondition.getInt("westtcount");
				int southtcount = checkcondition.getInt("southtcount");
				int northcount = checkcondition.getInt("northcount");
				int centercount = checkcondition.getInt("centercount");
				
				if(eastcount > 0)
				{
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("code", 201);
					jsonObject.put("message", "东部还有残敌未击退！");
					return jsonObject.toString();
				}
				else if(westtcount > 0)
				{
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("code", 201);
					jsonObject.put("message", "西部还有残敌未剪除！");
					return jsonObject.toString();
				}
				else if(southtcount > 0)
				{
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("code", 201);
					jsonObject.put("message", "南部还有残敌未歼灭！");
					return jsonObject.toString();
				}
				else if(northcount > 0)
				{
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("code", 201);
					jsonObject.put("message", "北部还有残敌未扫除！");
					return jsonObject.toString();
				}
				else if(centercount > 0)
				{
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("code", 201);
					jsonObject.put("message", "坐镇关口的敌军大将还在，不能占领！");
					return jsonObject.toString();
				}
				else
				{
					// 取消发布公告
					jdbcTemplate.update("update notice set author='"+country+"太守府',notice=' ["+position+" "+name+"] 勇冠三军，攻占["+countryname+" "+gatewayname+"]，对"+countryname+"宣战指数加1' where id = 3");
					// 宣战指数统计增加
					jdbcTemplate.update("update battle_count set count = count + 1 where countryid_attack='"+countryid+"' and countryid_defence='"+belongcountry+"'");
					
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					int m = 5+grade/50;
					if(random.nextInt(10) == 1)
					{
						int rand = random.nextInt(3);
						if(rand == 0)
						{
							jsonObject.put("code", 200);
							jsonObject.put("message", "你占领了这个要塞，城门楼子上插上了你的旗帜。太守府派人传令命你退兵回城，并封赏了你，获得 [ 3点功勋，白银"+m+"两 ]。由于你舅舅是太守府的高官显贵，他老人家还偷偷给你多报了军功，你额外获得[ 1 ]点功勋。");
							upposition(uuid, 4);
						}
						else if(rand == 1)
						{
							jsonObject.put("code", 200);
							jsonObject.put("message", "你占领了这个要塞，城门楼子上插上了你的旗帜。太守府派人传令命你退兵回城，并封赏了你，获得 [ 3点功勋，白银"+m+"两 ]。由于你叔父大人是太守大人面前红人，他老人家还给你说了很多好话，你额外获得[ 5 ]两银子。");
							upposition(uuid, 3);
							m = m+5;
						}
						else if(rand == 2)
						{
							jsonObject.put("code", 200);
							jsonObject.put("message", "你占领了这个要塞，城门楼子上插上了你的旗帜。太守府派人传令命你退兵回城，并封赏了你，获得 [ 3点功勋，白银"+m+"两 ]。由于你姑母是太守大人的正室，在太守大人吹了不少耳边风，你额外获得[ 5 ]两银子，[ 1 ]点功勋。");
							upposition(uuid, 4);
							m = m+5;
						}						
					}
					else
					{
						jsonObject.put("code", 200);
						jsonObject.put("message", "你占领了这个要塞，城门楼子上插上了你的旗帜。太守府派人传令命你退兵回城，并封赏了你，获得 [ 3点功勋，白银"+m+"两 ]");
						upposition(uuid, 3);
					}
					
					// 重置战场
					jdbcTemplate.update("update battle_gateway set battletype=1,battlerid='',battlername='' where battlerid='"+uuid+"'");
					// 奖励出战者
					jdbcTemplate.update("update person set money=money+"+(m*1000+10000)+",last_gobattle_time=UNIX_TIMESTAMP(),areaid = (select taishoufu from country where countryid = person.countryid) where uuid='"+uuid+"'");

					
					return jsonObject.toString();
				}
			}
			else
			{
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "你现在不能占领这个要塞！");
				return jsonObject.toString();
			}
		}
		else
		{
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "抱歉！未查询到你的用户信息！");
			return jsonObject.toString();
		}
	}
	
	
	public void upposition(String uuid,int value)
	{
		//positiongrade*24+POW(positiongrade,2.2)
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select name,countryname,positionexp,position,positiongrade,nextpositionexp from person where uuid='"+uuid+"'");
		if(rowSet.next())
		{
			long positionexp = rowSet.getLong("positionexp");
			long nextpositionexp = rowSet.getLong("nextpositionexp");
			int positiongrade = rowSet.getInt("positiongrade");
			String positionOld = rowSet.getString("position");
			String name = rowSet.getString("name");
			String countryname = rowSet.getString("countryname");
			
			positiongrade += 1;
			positionexp = positionexp + value;
			
			long nextExp = (long) ((positiongrade+1)*24+Math.pow((positiongrade+1),2.2));
			
			// can upgrade of position
			if(positionexp >= nextpositionexp)
			{
				SqlRowSet rowSet2 = jdbcTemplate.queryForRowSet("select positionname from position_rule where positiongrade = "+positiongrade);
				if(rowSet2.next())
				{
					String position = rowSet2.getString("positionname");
					positionexp = positionexp - nextpositionexp;
					jdbcTemplate.update("update person set positionexp="+positionexp+",positiongrade=positiongrade+1,nextpositionexp="+nextExp+",position='"+position+"' where uuid='"+uuid+"'");
					jdbcTemplate.update("update notice set author='"+countryname+"太守府',notice='["+positionOld+" "+name+"] 志虑忠纯,屡立战功,奉太守府官文特晋升为["+position+"]' where author!='管理员'");

					sendSystemMessage(uuid, "【"+positionOld+" "+name+"】志虑忠纯，屡立战功，为"+countryname+"贡献突出，奉太守府官文告书特晋升为【"+position+"】");
				}
			}
			else
			{
				jdbcTemplate.update("update person set positionexp=positionexp+"+value+" where uuid='"+uuid+"'");
			}
		}
	}
	
	public Object sendSystemMessage(String receiver_uuid, String keyword)
	{
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 发系统消息提示
		String mid = UUID.randomUUID().toString().replaceAll("-", "");
		String notiMassgae = "insert into message(mid,senderid,receiveid,message,sendtime,readflag,sendername,ordertime,type) "
				+ "values("+ "'"+ mid+ "','0000','"+receiver_uuid+"','"+keyword+"','"+sdf.format(new Date(System.currentTimeMillis()))+"','false','太守府官文',"+System.currentTimeMillis()+",2)";
		jdbcTemplate.update(notiMassgae);
		return null;
	}

	// 要塞出战
	public String battle(String uuid)
	{
		// 0.判断表中是否还存留本人的uuid下的房间id，有|处罚擅自弃军逃离战场。无|下一步
		// 1.随机更新battle_gateway表一条记录
		// 2.把出战者的地区设置为要塞的中心地区id
		String querySql = "select bid,gatewayname,gatewayid,(select money from person where uuid='"+uuid+"') as money,(select positionexp from person where uuid='"+uuid+"') as positionexp,(select countryname from person where uuid='"+uuid+"') as currcountryname,(select countryname from place where areaid=(select areaid from person where uuid='"+uuid+"')) as placecountryname from battle_gateway where battletype=1 and belongcountry != (select countryid from person where uuid='"+uuid+"' limit 0,1 order by rand())  ORDER BY RAND()";
//		String querySql = "select bid,gatewayname from battle_gateway where battletype=1 and belongcountry != (select countryid from person where uuid='"+uuid+"' limit 0,1 order by rand())";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(querySql);
		if(rowSet.next())
		{
			String currcountryname = rowSet.getString("currcountryname");
			String placecountryname= rowSet.getString("placecountryname");

			
			if(!currcountryname.equals(placecountryname))
			{
				JSONObject jsonObject = new JSONObject();
				try
				{
					jsonObject.put("code", "201");
					jsonObject.put("message", "你是哪国奸细竟敢到本城来出战！");
				}
				catch (JSONException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return jsonObject.toString();
			}
			
			long money = rowSet.getLong("money");
//			int positionexp = rowSet.getInt("positionexp");
			
			if(money < 10000)
			{
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 205);
				jsonObject.put("message", "你没有出战资格，为防范出战时的逃兵行为，或恶意占据要塞，施行制裁，必须要有至少10两银子保证金才能领兵进入要塞（占领要塞后会返还10银,出战超出时间或战死或逃亡则没收）");
				return jsonObject.toString();
			}
			
			// 国战期间禁止 出战要塞
			int battlecountrycount = jdbcTemplate.queryForInt("select count(1) from country where isbattle='true'");
			if(battlecountrycount > 0)
			{
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 205);
				jsonObject.put("message", "现在正在国战，无法对要塞出战！");
				return jsonObject.toString();
			}
			
			SqlRowSet ckeck = jdbcTemplate.queryForRowSet("select battlername from battle_gateway where battlerid='"+uuid+"'");
			if(ckeck.next())
			{
				JSONObject jsonObject = new JSONObject();
				try
				{
					jdbcTemplate.update("update person set last_gobattle_time=(UNIX_TIMESTAMP()) where uuid='"+uuid+"' and positionexp>=10");
					jdbcTemplate.update("update battle_gateway set battletype=1,battlerid='',battlername='' where battlerid='"+uuid+"'");
					jsonObject.put("code", "200");
					jsonObject.put("message", "太守府查到你上次领兵出战时渎职，抛弃军队独自逃离战场，现在不能出战！");
				}
				catch (JSONException e1)
				{
					e1.printStackTrace();
				}
				return jsonObject.toString();
			}
			
			int grade = jdbcTemplate.queryForInt("select grade from person where uuid='"+uuid+"'");
			if(grade < 35)
			{
				JSONObject jsonObject = new JSONObject();
				try
				{
					jsonObject.put("code", "205");
					jsonObject.put("message", "你还太小，太守府拒绝了你的出战请求！（出战最低等级要求:35级）");
				}
				catch (JSONException e1)
				{
					e1.printStackTrace();
				}
				return jsonObject.toString();
			}
			
			long diff = jdbcTemplate.queryForLong("select UNIX_TIMESTAMP()-last_gobattle_time as diff from person where uuid='"+uuid+"'");
			if(diff < 1800)
			{
				diff = 1800 - diff;
				String nextTime = diff / 60+" 分 "+diff % 60+" 秒";
				JSONObject jsonObject = new JSONObject();
				try
				{
					jsonObject.put("code", "205");
					jsonObject.put("message", "请先休息一会再出战吧！\n（下次出战还需要等待--> "+nextTime+"）");
				}
				catch (JSONException e1)
				{
					e1.printStackTrace();
				}
				return jsonObject.toString();
			}
			
			String bid = rowSet.getString("bid");
			int gatewayid = rowSet.getInt("gatewayid");
			String gatewayname = rowSet.getString("gatewayname");
			
			String update2 = "update battle_gateway set battletype=2,battlerid='"+uuid+"',battlername=(select name from person where uuid='"+uuid+"'),battletime=UNIX_TIMESTAMP(NOW()) where bid='"+bid+"'";
			// 更新该记录
			jdbcTemplate.update(update2);
			
			grade = grade + 2;
			// 更新守卫守将等级
			jdbcTemplate.update("update person set grade="+grade+" ,state=1,attack=100+"+grade+"*30,defence=50+"+grade+"*19,hp=500+"+grade+"*45,maxhp=500+"+grade+"*45,amount="+grade+"*5 where type=2 and position='"+(gatewayname+"偏将")+"'");
			grade = grade + 1;
			jdbcTemplate.update("update person set grade="+grade+" ,state=1,attack=100+"+grade+"*30,defence=50+"+grade+"*19,hp=500+"+grade+"*45,maxhp=500+"+grade+"*45,amount="+grade+"*5 where type=2 and position='"+(gatewayname+"主将")+"'");
			
			// 更新地区坐标
			jdbcTemplate.update("update person set money=money-10000,areaid = "+gatewayid+" where uuid='"+uuid+"'");
			
			JSONObject jsonObject = new JSONObject();
			try
			{
				jsonObject.put("code", "200");
				jsonObject.put("message", "你成功向太守府申请领兵出战【"+gatewayname+"】");
			}
			catch (JSONException e1)
			{
				e1.printStackTrace();
			}
			return jsonObject.toString();
		}
		else
		{
			JSONObject jsonObject = new JSONObject();
			try
			{
				jsonObject.put("code", "201");
				jsonObject.put("message", "各个要塞都已有人出战，太守府无兵可派，请暂待。");
			}
			catch (JSONException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return jsonObject.toString();
		}
	}

	public String battleout(String uuid)
	{
		jdbcTemplate.update("update person set last_gobattle_time=UNIX_TIMESTAMP(),areaid = (select taishoufu from country where countryid = person.countryid) where uuid='"+uuid+"'");
		jdbcTemplate.update("update battle_gateway set battletype=1,battlerid='',battlername='' where battlerid='"+uuid+"'");
		JSONObject jsonObject = new JSONObject();
		try
		{
			jsonObject.put("code", "200");
			jsonObject.put("message", "你成功退兵了！");
		}
		catch (JSONException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return jsonObject.toString();
	}
}
