package com.springmvc.util;

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
public class MySQLOprea {
	
	int multiple = 1;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String descript = "你还只是个十三四岁的小孩童，天真的脸庞，那份纯真可爱的稚气显露无遗。";
	Random random = new Random();
	
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/** 排序查询  引入数据库框架*/
	@ResponseBody
	public List<Map<String, Object>> queryRanking(String orderBy)
	{
		try
		{
			String queryRanking ;
			queryRanking = "select * from person where uuid!='0001' and uuid!='1209' and uuid!='1210' and type = 1 order by "+orderBy+" desc limit 0,50";
			return jdbcTemplate.queryForList(queryRanking);
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		return null;
	}
	
	
	public String rest(String uuid)
	{
		String repanse = "你轻松地住店休息了一会，体力已经恢复了。";
		try
		{
			SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select areaid,money from person where uuid='"+uuid+"'");
			if(rowSet.next())
			{
				int areaid = rowSet.getInt("areaid");
				long money = rowSet.getInt("money");
				
				if(money < 10 && money > 2)
				{
					repanse = "掌柜：你身上就几个铜板，再这么下去连住店都住不起了，非得露宿街头不可。\n"+repanse;
				}
				else if(money < 2)
				{
					JSONObject object = new JSONObject();
					object.put("code", 200);
					object.put("message", "掌柜：'哎你没钱就别进来了！' 掌柜的似乎不太欢迎你，但你也确实身上连2个铜板都没有，怪不得别人。");
					return object.toString();
				}
				
				if(areaid!=1 && areaid!=126 && areaid != 214 && areaid != 306)
				{
					JSONObject object = new JSONObject();
					object.put("code", 1024);
					object.put("message", "你当前的地区码并不在各城客栈范围内，涉嫌作弊行为!已经后台记录本次行为。");
					return object.toString();//ResultSetTool.resultSetToJsonObject(rs).toString();
				}
			}
					String sql = "update person set hp=maxhp,money=money-2 where uuid='"+uuid+"'";
					
					jdbcTemplate.update(sql);
					
					JSONObject object = new JSONObject();
					object.put("code", 200);
					object.put("message", repanse);
					return object.toString();
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
//	
	/***
	 * 解除
	 * @author 许仕永(xsy)
	 * des: 
	 * @param uuid
	 * @return
	 */
	public String releaseBattle(String uuid)
	{
		try
		{
			String sql = "update person set isbattle='false' where uuid='"+uuid+"'";
			String sql2 = "delete from battle where attackerid='"+uuid+"' or defencerid='"+uuid+"'";
			jdbcTemplate.update(sql);
			jdbcTemplate.execute(sql2);
			
			JSONObject object = new JSONObject();
			object.put("code", 200);
			object.put("message", "你已逃离!");
			return object.toString();//ResultSetTool.resultSetToJsonObject(rs).toString();
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	// type 1|start battle   2|refresh descript
	@ResponseBody
	public synchronized String battle(String attackerid,String defencerid,String type)
	{
		try
		{
			if((defencerid == null || "".equals(defencerid)) || (attackerid == null || "".equals(attackerid)))
			{
				jdbcTemplate.update("update person set isbattle = 'false' where uuid='"+attackerid+"' or uuid='"+defencerid+"'");
				jdbcTemplate.execute("delete from battle where attackerid='"+attackerid+"' and defencerid='"+defencerid+"'");

				JSONObject object = new JSONObject();
				object.put("descript", "服务器校验错误，或您的状异常。");
				object.put("result", "");
				object.put("code",0);
				object.put("statecode", 0);
				return object.toString();
			}
			
			
			// 点击杀戮之后的战斗刷新
			if("2".equals(type))
			{
				SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select count(1) from battle where attackerid='"+attackerid+"' and defencerid='"+defencerid+"'");
				if(!sqlRowSet.next())
				{
					// 不注释掉下面这条的话，则群P时，一人死亡落逃跑，则其他全部参与者都会停止战斗
					jdbcTemplate.update("update person set isbattle = 'false' where uuid='"+attackerid+"' or uuid='"+defencerid+"'");

					JSONObject object = new JSONObject();
					object.put("descript", "\n战斗结束,按离开键可以离开。");
					object.put("result", "");
					object.put("statecode", 0);
					object.put("code",0);
					return object.toString();
				}
			}
			
			// 首次点击了戮
			if("1".equals(type))
			{
				// 触发战斗状态
				jdbcTemplate.update("update person set enemyid ='"+defencerid+"',enemyname='"+defencerid+"',isbattle = 'true' where uuid='"+attackerid+"'");
				jdbcTemplate.update("update person set enemyid ='"+attackerid+"',enemyname='"+attackerid+"',isbattle = 'true' where uuid='"+defencerid+"'");
				jdbcTemplate.update("delete from battle where attackerid='"+attackerid+"' and defencerid='"+defencerid+"'");
				// 且不是npc  置空，以免上次异常残留未清除的dialog数据长度溢出
			}
			
			
			String result = "别紧张，慢慢出招，别露出破绽!";
			String attackerArmsName = null;
			String defencerArmsName = null;
			
			// attacker info
			String queryAttacker = "select position,dailytaskid,dailytaskcount,dailytaskruntimecount,teamid,countryid,name,attack,areaid,defence,hp,redvalue,grade,maxhp,amount,type,state,taskid,isbattle from person where uuid='"+attackerid+"'";
			SqlRowSet attackerSet = jdbcTemplate.queryForRowSet(queryAttacker);
			attackerSet.next();
			String attackerTeamid = attackerSet.getString("teamid");
			String attackCountryId = attackerSet.getString("countryid");
			String attackername = attackerSet.getString("name");
			String attackerposition = attackerSet.getString("position");
			String attackertaskid = attackerSet.getString("dailytaskid");
			int dailytaskcount = attackerSet.getInt("dailytaskcount");
			int dailytaskruntimecount = attackerSet.getInt("dailytaskruntimecount");
			int attackerAttack = attackerSet.getInt("attack");
			int attackerareaid = attackerSet.getInt("areaid");
			int attackerDefence = attackerSet.getInt("defence");
			int attackerHp = attackerSet.getInt("hp");
			int redvalueAttack = attackerSet.getInt("redvalue");
			int attackerGrade = attackerSet.getInt("grade");
			int maxHPAttack = attackerSet.getInt("maxhp");
			int amountAttack = attackerSet.getInt("amount");
			int typeAttacker = attackerSet.getInt("type");
			int stateAttacker = attackerSet.getInt("state");
			int taskidAttacker = attackerSet.getInt("taskid");
			String isbattleA = attackerSet.getString("isbattle");
			///** 物品属性从装备后开始加入到个人属性中，不在战斗中叠加
			String queryGoodsAttacker = "select type,name from goods where masterid ='"+attackerid+"' and state=1 and (type=101 or type=102 or type=103)";
			SqlRowSet resultSetGoodsAttacker = jdbcTemplate.queryForRowSet(queryGoodsAttacker);
			int armsTypeAttack = 101;
			if(resultSetGoodsAttacker.next())
			{
				armsTypeAttack = resultSetGoodsAttacker.getInt("type");
				if(armsTypeAttack == 101 || armsTypeAttack== 102 || armsTypeAttack==103)
					attackerArmsName = resultSetGoodsAttacker.getString("name");
			}
			else
			{
				if(typeAttacker == 2 || typeAttacker == 3)
				{
					if(attackerGrade <= 5)
					{
						armsTypeAttack = 101;
						attackerArmsName = "木长刀";
					}
					else if(attackerGrade > 5 && attackerGrade <= 10)
					{
						armsTypeAttack = 101;
						attackerArmsName = "铜长刀";
					}
					else if(attackerGrade > 10 && attackerGrade <= 15)
					{
						armsTypeAttack = 101;
						attackerArmsName = "月牙刀";
					}
					else if(attackerGrade > 15 && attackerGrade <= 20)
					{
						armsTypeAttack = 101;
						attackerArmsName = "长杆刀";
					}
					else if(attackerGrade > 20 && attackerGrade <= 25)
					{
						armsTypeAttack = 101;
						attackerArmsName = "赤铜长刀";
					}
					else if(attackerGrade > 25 && attackerGrade <= 45)
					{
						armsTypeAttack = 101;
						attackerArmsName = "黄铜长刀";
					}
					else if(attackerGrade > 45 && attackerGrade <= 60)
					{
						armsTypeAttack = 101;
						attackerArmsName = "折铁刀";
					}
					else if(attackerGrade > 60 && attackerGrade <= 75)
					{
						armsTypeAttack = 101;
						attackerArmsName = "横刀";
					}
					else if(attackerGrade > 75 && attackerGrade <= 90)
					{
						armsTypeAttack = 101;
						attackerArmsName = "仪刀";
					}
					else if(attackerGrade > 90 && attackerGrade <= 105)
					{
						armsTypeAttack = 101;
						attackerArmsName = "戟刀";
					}
					else if(attackerGrade > 105 && attackerGrade <= 120)
					{
						armsTypeAttack = 101;
						attackerArmsName = "破甲长刀";
					}
					else if(attackerGrade > 120 && attackerGrade <= 150)
					{
						armsTypeAttack = 101;
						attackerArmsName = "复式云纹军刀";
					}
					else if(attackerGrade > 150 && attackerGrade <= 200)
					{
						armsTypeAttack = 101;
						defencerArmsName = "玄铁断魂刀";
					}
					else if(attackerGrade > 200 && attackerGrade <= 250)
					{
						armsTypeAttack = 102;
						defencerArmsName = "素缨白莲枪";
					}
					else if(attackerGrade > 250)
					{
						armsTypeAttack = 101;
						defencerArmsName = "麒麟吞日斩";
					}
				}
			}
			
			String queryDefencer = "select position,dailytaskid,sign,teamid,countryid,name,attack,areaid,defence,hp,redvalue,grade,maxhp,amount,type,state,taskid,isbattle from person where uuid='"+defencerid+"'";
			SqlRowSet defenceSet = jdbcTemplate.queryForRowSet(queryDefencer);
			defenceSet.next();
			
			//String defencerTeamid = defenceSet.getString("teamid");
			String defenceCountryId = defenceSet.getString("countryid");
			String defencerposition = defenceSet.getString("position");
			String defencername = defenceSet.getString("name");
			String defenceSign = defenceSet.getString("sign");
			String defencetaskid = defenceSet.getString("dailytaskid");
			int defencerAttack = defenceSet.getInt("attack");
			int defencerDefence = defenceSet.getInt("defence");
			int defencerHp = defenceSet.getInt("hp");
			int defencerGrade = defenceSet.getInt("grade");
			int typeDefencer = defenceSet.getInt("type");
			int stateDefence = defenceSet.getInt("state");
			int redvalueDefence = defenceSet.getInt("redvalue");
			int amountDefence = defenceSet.getInt("amount");
			int maxHPDefence = defenceSet.getInt("maxhp");
			int defenceAreaid = defenceSet.getInt("areaid");
			int taskidDefence = defenceSet.getInt("taskid");
			String isbattleD = defenceSet.getString("isbattle");
			int stateCode = 0; 
			String resultSeccUUID = "";
			
			
			if(attackerareaid != defenceAreaid || stateAttacker != stateDefence)
			{
				jdbcTemplate.update("update person set isbattle = 'false' where uuid='"+attackerid+"' or uuid='"+defencerid+"'");
				JSONObject object = new JSONObject();
				object.put("message", "对方已经不在这里");
				object.put("code",205);
				object.put("statecode", 0);
				return object.toString();
			}
			
			
			// 等级属性加成
			{
				//70 80 = -10
				// 等级差
				int gdiff = attackerGrade - defencerGrade;
				
				int temp = attackerAttack  + gdiff * 65;
				int temp2 = attackerDefence + gdiff * 30;
				
				attackerAttack = (temp <= 0 ? 1:(temp));
				attackerDefence= (temp2 <= 0 ? 1:temp2);
			}
			
			// 首次点击了戮 ，杀戮限制条件
			if("1".equals(type))
			{
				if((typeAttacker != 2 && typeAttacker != 3))
				{
					if((typeDefencer != 2 && typeDefencer != 3))
					{
						// 取消了在迷城中杀戮的同城限制
						if(attackerareaid < 123001 || attackerareaid > 123013)
						{
							// 不允许本城相残
							if(attackCountryId.equals(defenceCountryId))
							{
								jdbcTemplate.update("update person set isbattle = 'false' where uuid='"+attackerid+"' or uuid='"+defencerid+"'");
								jdbcTemplate.execute("delete from battle where attackerid='"+attackerid+"' and defencerid='"+defencerid+"'");
								
								JSONObject object = new JSONObject();
								object.put("message", "入城条约规定，你不能杀害本城之人！");
								object.put("code",205);
								object.put("statecode", 0);
								return object.toString();
							}
							
							// 不能杀15级以下玩家
							if(defencerGrade <= 15)
							{
								jdbcTemplate.update("update person set isbattle = 'false' where uuid='"+attackerid+"' or uuid='"+defencerid+"'");
								jdbcTemplate.execute("delete from battle where attackerid='"+attackerid+"' and defencerid='"+defencerid+"'");
								
								JSONObject object = new JSONObject();
								object.put("message", "对方涉世未深，你怎忍心下毒手!(等ta长大点再杀吧)");
								object.put("code",206);
								object.put("statecode", 0);
								return object.toString();
							}
						}
					}
				}
			}
			
				if(stateAttacker == 0 || stateDefence == 0)
				{
					jdbcTemplate.update("update person set isbattle = 'false' where uuid='"+attackerid+"' or uuid='"+defencerid+"'");
					jdbcTemplate.execute("delete from battle where attackerid='"+attackerid+"' and defencerid='"+defencerid+"'");

					JSONObject object = new JSONObject();
					object.put("code", 0);
					object.put("descript", "对方已经不在了。\n战斗结束,按离开键可以离开。");
					object.put("result", "");
					object.put("statecode", 0);
					return object.toString();
				}
			
			// 判断是否库中已经有添加了pk记录
			SqlRowSet isHasSet = jdbcTemplate.queryForRowSet("select count(1) from battle where attackerid='"+attackerid+"' and defencerid='"+defencerid+"'");
			if(!isHasSet.next())
			{
				String uuid = UUID.randomUUID().toString().replaceAll("-", "");
				String sql = "insert into battle(battleid,attackerid,defencerid,attackername,defencername,descript) values('"+uuid+"','"+attackerid+"','"+defencerid+"','"+attackername+"','"+defencername+"','"+result+"')";
				jdbcTemplate.execute(sql);
			}

			if("false".equals(isbattleA) || "false".equals(isbattleD))
			{
				// 解除战斗状态
				jdbcTemplate.update("update person set isbattle = 'false' where uuid='"+attackerid+"' or uuid='"+defencerid+"'");
				jdbcTemplate.update("update person set enemyid ='null',enemyname='null' where uuid='"+attackerid+"'");
				jdbcTemplate.update("update person set enemyid ='null',enemyname='null' where uuid='"+defencerid+"'");
				jdbcTemplate.execute("delete from battle where attackerid='"+attackerid+"' and defencerid='"+defencerid+"'");

				if(attackerHp > 0 && defencerHp < 0 )
				{
					resultSeccUUID = attackerid;
				}
				else if(attackerHp <0 && defencerHp > 0)
				{
					resultSeccUUID = defencerid;
				}
				else if(attackerHp <0 && defencerHp < 0)
				{
					resultSeccUUID = "alldie";
				}
				else if(attackerHp > 0 && defencerHp > 0)
				{
					resultSeccUUID = "cant";
				}
				result = "战斗结束,按离开键可以离开";
				stateCode = 0;
				JSONObject object = new JSONObject();
				object.put("descript", result);
				object.put("code",0);
				object.put("result", resultSeccUUID);
				object.put("statecode", stateCode);
				return object.toString();
			}
			
			///**
			String queryGoodsDefencer = "select type,name from goods where masterid ='"+defencerid+"' and state=1  and (type=101 or type=102 or type=103)";
			SqlRowSet resultSetGoodsDefencer = jdbcTemplate.queryForRowSet(queryGoodsDefencer);
			int armsTypeDefence = 0 ;
			if(resultSetGoodsDefencer.next())
			{
				armsTypeDefence = resultSetGoodsDefencer.getInt("type");
				if(armsTypeDefence == 101 || armsTypeDefence== 102 || armsTypeDefence==103)
					defencerArmsName = resultSetGoodsDefencer.getString("name");
			}
			else
			{
				if(typeDefencer == 2 || typeDefencer == 3)
				{
					if(defencerGrade <= 5)
					{
						armsTypeDefence = 101;
						defencerArmsName = "木长刀";
					}
					else if(defencerGrade > 5 && defencerGrade <= 10)
					{
						armsTypeDefence = 101;
						defencerArmsName = "铜长刀";
					}
					else if(defencerGrade > 10 && defencerGrade <= 15)
					{
						armsTypeDefence = 101;
						defencerArmsName = "月牙刀";
					}
					else if(defencerGrade > 15 && defencerGrade <= 20)
					{
						armsTypeDefence = 101;
						defencerArmsName = "长杆刀";
					}
					else if(defencerGrade > 20 && defencerGrade <= 25)
					{
						armsTypeDefence = 101;
						defencerArmsName = "赤铜长刀";
					}
					else if(defencerGrade > 25 && defencerGrade <= 45)
					{
						armsTypeDefence = 101;
						defencerArmsName = "黄铜长刀";
					}
					else if(defencerGrade > 45 && defencerGrade <= 60)
					{
						armsTypeDefence = 101;
						defencerArmsName = "折铁刀";
					}
					else if(defencerGrade > 60 && defencerGrade <= 75)
					{
						armsTypeDefence = 101;
						defencerArmsName = "横刀";
					}
					else if(defencerGrade > 75 && defencerGrade <= 90)
					{
						armsTypeDefence = 101;
						defencerArmsName = "仪刀";
					}
					else if(defencerGrade > 90 && defencerGrade <= 105)
					{
						armsTypeDefence = 101;
						defencerArmsName = "戟刀";
					}
					else if(defencerGrade > 105 && defencerGrade <= 120)
					{
						armsTypeDefence = 101;
						defencerArmsName = "破甲长刀";
					}
					else if(defencerGrade > 120 && defencerGrade <= 150)
					{
						armsTypeDefence = 101;
						defencerArmsName = "复式云纹军刀";
					}
					else if(attackerGrade > 150 && attackerGrade <= 200)
					{
						armsTypeAttack = 101;
						defencerArmsName = "玄铁断魂刀";
					}
					else if(attackerGrade > 200 && attackerGrade <= 250)
					{
						armsTypeAttack = 102;
						defencerArmsName = "素缨白莲枪";
					}
					else if(attackerGrade > 250)
					{
						armsTypeAttack = 101;
						defencerArmsName = "麒麟吞日斩";
					}
				}
			}

			//原
			//int attackerByHart = (int) (defencerAttack/(attackerDefence/90.0f+0.8));//(defencerAttack*((1-1.0f/(1.0+attackerDefence/10.0f))));//(int) (defencerAttack*0.7/(attackerDefence/100.0f+1));//(defencerAttack - attackerDefence > 0 ? defencerAttack - attackerDefence : attackerHp / (Math.abs(defencerGrade - attackerGrade)));
			//int defencerByHart = (int) (attackerAttack/(defencerDefence/90.0f+0.8));//(attackerAttack*((1-1.0f/(1.0+defencerDefence/10.0f))));//(attackerAttack*0.7/(defencerDefence/100.0f+1));//0.06/(1+0.06*defencerDefence)));
			
			//现 加强防御的作用
			int attackerByHart = (int) (defencerAttack/(attackerDefence/90.0f+0.8));//(defencerAttack*((1-1.0f/(1.0+attackerDefence/10.0f))));//(int) (defencerAttack*0.7/(attackerDefence/100.0f+1));//(defencerAttack - attackerDefence > 0 ? defencerAttack - attackerDefence : attackerHp / (Math.abs(defencerGrade - attackerGrade)));
			int defencerByHart = (int) (attackerAttack/(defencerDefence/90.0f+0.8));//(attackerAttack*((1-1.0f/(1.0+defencerDefence/10.0f))));//(attackerAttack*0.7/(defencerDefence/100.0f+1));//0.06/(1+0.06*defencerDefence)));
			
//			float f1 = (float) (defencerGrade * 0.001);
//			float f2 = (float) (attackerGrade * 0.001);
//			int attackerByHart = (int) ((defencerAttack > attackerDefence ? (defencerAttack - attackerDefence)+defencerAttack*0.05 : defencerAttack*0.05) * (0.1 + (f1 > 0.3? 0.3:f1)));
//			int defencerByHart = (int) ((attackerAttack > defencerDefence ? (attackerAttack - defencerDefence)+attackerAttack*0.05 : attackerAttack*0.05) * (0.1 + (f2 > 0.3? 0.3:f2)));

			
			Random random = new Random();
			// 随机数=0时代表躲过此次攻击  =18代表遭受的伤害加重
			int randomAttacker = random.nextInt(10);
			if(randomAttacker == 0)
				attackerByHart = 0;
			else if(randomAttacker == 1)
				attackerByHart += attackerByHart / 10;
			
			int randomDefencer = random.nextInt(10);
			if(randomDefencer == 0)
				defencerByHart = 0;
			else if(randomDefencer == 1)
				defencerByHart += defencerByHart / 10;
			//System.out.println("attackerHp："+attackerHp+"  defencerHp:"+defencerHp+"  attackerByHar="+attackerByHart+"   defencerByHart="+defencerByHart+"  maxHPAttack="+maxHPAttack+"   maxHPDefence="+maxHPDefence);
			attackerHp -= attackerByHart;
			defencerHp -= defencerByHart;
			
			// 0|over battle  1|continue battle 
			
			if(attackerHp > 0 && defencerHp > 0)
			{
				stateCode = 1;
				result = getDescript(attackername, attackerArmsName, defencername, defencerArmsName, attackerByHart, defencerByHart,attackerHp,defencerHp,maxHPAttack,maxHPDefence);
			}
			else if(attackerHp > 0 && defencerHp <= 0)
			{
				stateCode = 0;
				result ="最后一式决死战,<font color=red>【"+defencername+"】</font>终是不敌连翻猛攻，败下阵来，<font color=red>【"+attackername+"】</font>岂肯放过这机会，紧赶上前一步，手中"+attackerArmsName+"无情"+getAction()+"，耳听得<font color=red>【"+defencername+"】</font>一声惨叫。";
				if(typeDefencer != 2 && typeDefencer != 3)
				{
					jdbcTemplate.update("update person set areaid="+getKeZhanId(defenceCountryId)+" where uuid='"+defencerid+"'");
				}
			}
			else if(attackerHp <= 0 && defencerHp > 0)
			{
				stateCode = 0;
				result ="<font color=red>【"+defencername+"】</font>杀心已下，眼神一凛，目露凶光，看的你是心惊胆寒，只心里暗苦不该关公门前耍大刀，面对如此强手，战至最后，已无力再抵抗，只能束手就戮，<font color=red>【"+defencername+"】</font>更不待留情，起手操起"+defencerArmsName+"无情"+getAction()+"，耳听得<font color=red>【"+attackername+"】</font>一声惨叫。";
				if(typeAttacker != 2 && typeAttacker!=3)
				{
					jdbcTemplate.update("update person set areaid="+getKeZhanId(attackCountryId)+" where uuid='"+attackerid+"'");
				}
			}
			else 
			{
				stateCode = 0;
				result ="两相争斗各有所伤，两败俱伤，各自逃散。";
			}
			
			String placeDesc = "";
			
			// 两人中其中一人血量为0则战斗结束
			if(attackerHp > 0 && defencerHp > 0)
			{
				jdbcTemplate.update("update person set hp ="+attackerHp+" where uuid='"+attackerid+"'");
				jdbcTemplate.update("update person set hp ="+defencerHp+" where uuid='"+defencerid+"'");
				
				placeDesc = result;
			}
			// 有人死亡了
			else
			{
				// 红名大于20 死后直接去天牢
				if(redvalueDefence > 20)
					jdbcTemplate.update("update person set areaid=10000 where uuid='"+defencerid+"' and type=1");
				if(redvalueAttack > 20)
					jdbcTemplate.update("update person set areaid=10000 where uuid='"+attackerid+"' and type=1");
				
				subLifeValue(attackerid, defencerid, typeAttacker, typeDefencer);
				jdbcTemplate.execute("delete from battle where (attackerid='"+attackerid+"' and defencerid='"+defencerid+"') or (attackerid='"+defencerid+"' and defencerid='"+attackerid+"')");
				
				// 重置战斗状态
				jdbcTemplate.update("update person set isbattle = 'false' where uuid='"+attackerid+"' or uuid='"+defencerid+"'");

				if(defencerHp <= 0 )
				{
					if(typeDefencer != 2 && typeDefencer!=3)	
					{
//						jdbcTemplate.update("update person set hp ="+maxHPDefence +", areaid="+getKeZhanId(defenceCountryId)+" where uuid='"+defencerid+"'");
						jdbcTemplate.update("update person set hp ="+maxHPDefence +" where uuid='"+defencerid+"'");
						
						if(typeAttacker != 2 && typeAttacker != 3)
						{
							// 红名统计增减
							redValueAdd(attackerid, attackername, attackerGrade, defencerid, defencerGrade,attackCountryId,attackerareaid);
							redValueSubtract(defencerid);
							
							// 等级限制，打死高于或低于自身等级5级的经验只有1
							if(Math.abs(defencerGrade - attackerGrade) > 5)
								amountDefence = 5;
							else   
								if(amountDefence > 100)
									amountDefence = amountDefence+(defencerGrade - attackerGrade)*6;
							
							String returnStr =  shazhao(armsTypeAttack, attackername, defencername)+"<font color=red>【"+attackername+"】</font>获得"+(amountDefence*multiple)+"点经验。";
							returnStr += dropPlayer(defencerid,defenceAreaid,defencername);
							
							placeDesc = result + shazhao(armsTypeAttack, attackername, defencername);
							result = placeDesc+"<font color=red>【"+attackername+"】</font>获得"+(amountDefence*multiple)+"点经验。"+((upgrade(attackerid) ?"<br/>【"+attackername+"升级了！！！】":""));
							
							jdbcTemplate.update("update person set exp =exp+"+(amountDefence*multiple) +", areaid="+getKeZhanId(defenceCountryId)+",dialog='"+returnStr+"' where uuid='"+defencerid+"'");
							// 生成战报
//							jdbcTemplate.update("update person set areaid="+getKeZhanId(defenceCountryId)+",dialog='"+returnStr+"' where uuid='"+defencerid+"'");
						}
						else
						{
							// 红名减除
							redValueSubtract(defencerid);

							result +=  shazhao(armsTypeAttack, attackername, defencername);
							result += dropPlayer(defencerid,defenceAreaid,defencername);
						}
						
						notice(defencerid,defencername,attackerid,attackername);
					}
					else
					{
						jdbcTemplate.update("update person set hp ="+maxHPDefence +", state=0 where uuid='"+defencerid+"'");
						if(typeAttacker != 2 && typeAttacker!=3)
						{
							int shareAmount = 0;
							// 组队享受额外经验
							if(attackerTeamid != null && !"0".equals(attackerTeamid))
							{
								int temp = (int) (amountDefence*multiple * 0.025);
								shareAmount = temp < 1 ? 1:temp;
								jdbcTemplate.update("update team_list set teammessage='["+attackername+"] 打死 ["+defencername+"] 分享组队额外经验 [ "+shareAmount+" ]' where teamid='"+attackerTeamid+"'");
								// 组队额外经验
								jdbcTemplate.update("update person set exp =exp+"+shareAmount +" where teamid='"+attackerTeamid+"'");
							}
							
							// 等级限制，打死高于或低于自身等级6级的经验只有1
							if(Math.abs(defencerGrade - attackerGrade) > 5)
								amountDefence = 1;
							else
								if(amountDefence > 100)
									amountDefence = amountDefence+(defencerGrade - attackerGrade)*6;
							jdbcTemplate.update("update person set exp =exp+"+(amountDefence*multiple) +" where uuid='"+attackerid+"'");
							
							placeDesc = result+shazhao(armsTypeAttack, attackername, defencername);
							result = placeDesc + "<font color=red>【"+attackername+"】</font>获得"+(amountDefence*multiple)+"点经验。"+(shareAmount > 0 ? "全队获得组队额外经验"+shareAmount:"")+((upgrade(attackerid) ?"<br/>【"+attackername+"升级了！！！】":""));							
							
							//掉物
							String s = dropNPC(defencerGrade, attackerareaid,defencername,defencerid,attackername,attackerid,attackertaskid,defencetaskid,dailytaskcount,dailytaskruntimecount);
							result += s;
							
							// 任务进度更新
							if(taskidAttacker == taskidDefence)
							jdbcTemplate.update("update person set taskprogress = taskprogress+1 where taskprogress < tasktarget and uuid='"+attackerid+"'");
						}
						else
							result +=  shazhao(armsTypeAttack, attackername, defencername);
					}
				}
				// 血低点
				else if(attackerHp <= 0)
				{
					// 且不是npc
					if(typeAttacker != 2 && typeAttacker != 3)
					{
						jdbcTemplate.update("update person set hp ="+maxHPAttack +" where uuid='"+attackerid+"'");
						if(typeDefencer != 2 && typeDefencer != 3)
						{
							if(Math.abs(defencerGrade - attackerGrade) > 8)
								amountAttack = 1;
							else
								if(amountAttack > 100)
									amountAttack = amountAttack+(attackerGrade - defencerGrade)*6;
							
							jdbcTemplate.update("update person set exp =exp+"+(amountAttack*multiple) +" where uuid='"+defencerid+"'");
							
							placeDesc = result+shazhao(armsTypeDefence, defencername,attackername);
//							result =  shazhao(armsTypeDefence, defencername,attackername)+"<font color=red>【"+defencername+"】</font>获得"+(amountAttack*multiple)+"点经验。"+((upgrade(defencerid) ?"\n\n【"+defencername+"升级了！！！】":""));							
							result = placeDesc+"<font color=red>【"+defencername+"】</font>获得"+(amountAttack*multiple)+"点经验。"+((upgrade(defencerid) ?"<br/>【"+defencername+"升级了！！！】":""));	
							
							String dialog = shazhao(armsTypeDefence, defencername,attackername)+" <font color=red>【"+defencername+"】</font>获得"+(amountAttack*multiple)+"点经验。";
							dialog += dropPlayer(attackerid,attackerareaid,attackername);
							// 生成战报
							jdbcTemplate.update("update person set areaid="+getKeZhanId(attackCountryId)+",dialog='"+dialog+"' where uuid='"+attackerid+"'");
						}
						else
						{
							//System.out.println("掉落物品--->");
							result += shazhao(armsTypeDefence, defencername,attackername);
							result += dropPlayer(attackerid,attackerareaid,attackername);
						}
						
						notice(attackerid,attackername,defencerid,defencername);
					}
					// 是npc
					else
					{
						jdbcTemplate.update("update person set hp ="+maxHPAttack +",state=0 where uuid='"+attackerid+"'");
						if(typeDefencer != 2 && typeDefencer != 3)
						{
							if(Math.abs(defencerGrade - attackerGrade) > 8)
								amountAttack = 1*multiple;
							else
								if(amountAttack > 100)
									amountAttack = amountAttack+(attackerGrade - defencerGrade)*6;
							
							jdbcTemplate.update("update person set exp =exp+"+(amountAttack*multiple) +" where uuid='"+defencerid+"'");
							
							placeDesc = result + shazhao(armsTypeDefence, defencername,attackername);
							result = placeDesc+" <font color=red>【"+defencername+"】</font>获得"+(amountAttack*multiple)+"点经验。"+((upgrade(defencerid) ?"<br/>【"+defencername+"升级了！！！】":""));		
						}
						else
						{
							result += shazhao(armsTypeDefence, defencername,attackername);
							
							//掉物
							String s = dropNPC(defencerGrade, attackerareaid,attackername,attackerid,defencername,defencerid,attackertaskid,defencetaskid,dailytaskcount,dailytaskruntimecount);
							result += s;
							
							// 任务进度更新
							if(taskidAttacker == taskidDefence)
							jdbcTemplate.update("update person set taskprogress = taskprogress+1 where taskprogress < tasktarget and uuid='"+defencerid+"'");							
						}
					}
				}
			}
			
			jdbcTemplate.update("update place set battle_string='"+placeDesc+"',last_time=UNIX_TIMESTAMP() where areaid="+attackerareaid);
			
			if("1".equals(type))
			{
				if(defenceSign == null || "".equals(defenceSign.trim()))
				{
					String res = "敢在太岁爷头上动土,不知好歹纳命来！";
					switch (random.nextInt(6))
					{
						case 0:res = "敢在太岁爷头上动土,不知好歹纳命来！";break;
						case 1:res = "恭恭敬敬给本大爷叩个头,饶你一命。";break;
						case 2:res = "来打我啊！笨蛋！";break;
						case 3:res = defencername+"老仙，法力无边！";break;
						case 4:res = "找死！看我怎么收拾你！";break;
						case 5:res = "有本事别打我啊！";break;
						default:
							break;
					}
					result = "<font color='red'>【"+defencername+"】:"+res+"</font><br/><br/>"+result;
				}
				else
				{
					result = "<font color='red'>【"+defencername+"】:"+defenceSign+"</font><br/><br/>"+result;
				}
			}
			
			//System.out.println("100-maxHPAttack/attackerHp="+(100-attackerHp*1.0/maxHPAttack)+"100-maxHPDefence/defencerHp="+(100-defencerHp*1.0/maxHPDefence));
			JSONObject object = new JSONObject();
			object.put("descript", result);
			object.put("code",0);
			object.put("attackerhp", 100*(attackerHp*1.0/maxHPAttack));
			object.put("defencerhp", 100*(defencerHp*1.0/maxHPDefence));
			object.put("defencername", defencerposition==null?defencername:"["+defencerposition+"] "+defencername);
			object.put("attackername", attackerposition==null?attackerposition:"["+attackerposition+"] "+attackername);
			object.put("at", attackerByHart);
			object.put("dt", defencerByHart);
			object.put("statecode", stateCode);
			object.put("result", "");
			return object.toString();
		}
		catch (Exception e)
		{
			// TODO: handle exception
			System.out.println("战斗出现异常：-->"+e.toString());
		}

		JSONObject object = new JSONObject();
		try
		{
			object.put("descript", "PK数据异常，服务器数据处理状态异常 错误码503");
			object.put("statecode", 503);
			object.put("code",0);
			object.put("result", "");
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object.toString();
	}
	
	private synchronized void subLifeValue(String attackerid,String defencerid,int attackertype,int defencertype)
	{
		String sql = null;
		if( attackertype != 2 && attackertype != 3)
			sql = "select life,name,gid,addhp,addattack,adddefence,masterid,type,gtype from goods where state = 1 and (masterid='"+attackerid+"')";
		else if( defencertype != 2 && defencertype != 3)
			sql = "select life,name,gid,addhp,addattack,adddefence,masterid,type,gtype from goods where state = 1 and (masterid='"+defencerid+"')";
		
		if(( attackertype != 2 && attackertype != 3) && ( defencertype != 2 && defencertype != 3))
			sql = "select life,name,gid,addhp,addattack,adddefence,masterid,type,gtype from goods where state = 1 and (masterid='"+defencerid+"' or masterid='"+attackerid+"')";
		
		if(sql == null)return;
		
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
		while(rowSet.next())
		{
			int gtype = rowSet.getInt("gtype");
			int life = rowSet.getInt("life");
			String muuid = rowSet.getString("masterid");
			String name = rowSet.getString("name");
			String gid = rowSet.getString("gid");
			int type = rowSet.getInt("type");

			// 戒指不扣除耐久值
			if(type != 7 && gtype != 1024)
			{
				// 发出提醒
				if(life <= 30)
				{
					sendSystemMessage(muuid, "你的装备:["+name+"] 已经快报废了，当前耐久值仅剩:"+life+",请及时前往铁匠铺维修，一旦为0报废，则无法维修。");
				}
				
				//System.out.println("清算耐久值:"+life);
				
				if(life >= 1)
				{
					// 减除耐久值1点
					jdbcTemplate.update("update goods set life = life - 1 where masterid='"+muuid+"' and gid='"+gid+"'");
				}
				else
				{
					int addhp = rowSet.getInt("addhp");
					int addattack = rowSet.getInt("addattack");
					int adddefence = rowSet.getInt("adddefence");
					
					//System.out.println("update person set hp=hp-"+addhp+",maxhp=maxhp-"+addhp+" ,attack=attack-"+addattack+",defence=defence-"+adddefence+" where uuid='"+muuid+"'");
					jdbcTemplate.update("update person set hp=hp-"+addhp+",maxhp=maxhp-"+addhp+" ,attack=attack-"+addattack+",defence=defence-"+adddefence+" where uuid='"+muuid+"'");
					jdbcTemplate.update("update goods set life = 0,star=0,state = 2,addhp=0,addattack=0,adddefence=0 where masterid='"+muuid+"' and gid='"+gid+"'");
					sendSystemMessage(muuid, "你的装备:["+name+"] 已经报废，碎裂不堪无法装备，已经自动卸除到行囊。");
				}
			}
		}
	}

	private void notice(String dierid, String diername, String killerid, String killername)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select redvalue,countryname from person where uuid='"+dierid+"'");
		if(rowSet.next())
		{
			int redvalue = rowSet.getInt("redvalue");
			String countryname = rowSet.getString("countryname");
			String chenghu = getDesignation(countryname, redvalue);
			if(redvalue >= 5 && redvalue < 20)
			{
				String notice = "["+killername+"] 为民除害击杀 "+chenghu+" ["+diername+"]，获官府赏金一百铜板.";
				jdbcTemplate.update("update notice set author='官府',last_time=UNIX_TIMESTAMP(),notice='"+notice+"' where author!='管理员'");
				jdbcTemplate.update("update person set money=money+100 where uuid='"+killerid+"'");
			}
			else if(redvalue >= 20)
			{
				String notice = "["+killername+"] 为国除害击杀 "+chenghu+" ["+diername+"]，获官府赏金二百铜板，["+diername+"]被投入死囚牢";
				jdbcTemplate.update("update notice set author='官府',last_time=UNIX_TIMESTAMP(),notice='"+notice+"' where author!='管理员'");
				jdbcTemplate.update("update person set money=money+200 where uuid='"+killerid+"'");
				
				jdbcTemplate.update("update person set areaid=10000 where uuid='"+dierid+"'");
			}
		}
	}

	private void redValueAdd(String killerid,String killername,int killergrade,String bekillerid,int bekillergrade,String countryid,int areaid)
	{
		int red = killergrade - bekillergrade;
		
		if((red <= 10 && red >= 0 ) || red < 0)
		{
			red = 1;
		}
		else if(red > 10 && red <= 30)
		{
			red = 2;
		}
		else  if(red > 30)
		{
			red = 3;
		}
		
		// 国战期间四城门广场，杀人无罪
		SqlRowSet count = jdbcTemplate.queryForRowSet("select countryname from country where isbattle='true' and (eareaid="+areaid+" or wareaid="+areaid+" or sareaid="+areaid+" or nareaid="+areaid+" or careaid="+areaid+")");
		//		SqlRowSet count = jdbcTemplate.queryForRowSet("select countryname from country where countryid='"+countryid+"' and isbattle='true' and (eareaid="+areaid+" or wareaid="+areaid+" or sareaid="+areaid+" or nareaid="+areaid+" or careaid="+areaid+")");

		if(!count.next())
		{
			jdbcTemplate.update("update person set redvalue=redvalue+"+red+" where uuid='"+killerid+"' and redvalue < 50");
			jdbcTemplate.update("insert into enemy(killerid,killername,bekillerid,reward) values('"+killerid+"','"+killername+"','"+bekillerid+"',"+0+")");
		}
	}
	
	private void redValueSubtract(String uuid)
	{
		jdbcTemplate.update("update person set redvalue=redvalue-"+1+" where uuid='"+uuid+"' and redvalue>1");
	}
	
	private String dropPlayer(String dieruuid,int areaid,String diername)
	{
		// 处理如果死掉的这个人正在出战，则重置他正在出战的那个要塞
//		SqlRowSet checkIsHaveBattle = jdbcTemplate.queryForRowSet("select bid from battle_gateway where battlerid='"+dieruuid+"'");
//		while (checkIsHaveBattle.next())
//		{
			jdbcTemplate.update("update battle_gateway set battletype=1,battlerid='',battlername='' where battlerid='"+dieruuid+"'");
//		}
		
		// 暂时不掉落玩家身上装备，以及材料不掉 仅获取除金银外的所有物品 
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from goods where masterid='"+dieruuid+"' and state = 2 and (type != 9 and type != 10 and type != 11) and gtype < 281");// and (state != 1 and (type != 103 or type != 102 or type != 101))");
		
		if(rowSet.next())
		{
			String gid = rowSet.getString("gid");
			String name = rowSet.getString("name");
			
			jdbcTemplate.update("update goods set masterid=-1,state=3,gareaid="+areaid+",unlawful_time=UNIX_TIMESTAMP() where masterid='"+dieruuid+"' and gid='"+gid+"'");
			
			return "<br/><br/><font color=red>"+diername+" 掉落了["+name+"]</font><br/>";
		}
		
		/**[ 注意  如果掉落玩家身上的装备一定要解除该装备附加在玩家身上的属性值]**/
		
		return null;
	}
	
	private String dropSpecial(int dropgoodtype,int grade,int areaid,String diername,String successeruuid)
	{
		if(dropgoodtype == 1)
		{
			long num = 500+random.nextInt(500);
			
			String gid = UUID.randomUUID().toString().replaceAll("-", "");
			String gname = NumberConverUtil.getNameByMoney(num);
			
			jdbcTemplate.update("insert into goods(gid,name,masterid,price,state,type,gareaid,unlawful_time) values('"+gid+"','"+gname+"','"+successeruuid+"',"+num+","+3+","+11+","+areaid+",UNIX_TIMESTAMP())");
			return "<br/><font color=red>"+diername+" 掉落了["+gname+"]</font>";
		}
		// 盗跖
		else if(dropgoodtype == 2)
		{
			int[] arrgood = new int[]{2,5,8,11,14,17,20,23,3,6,9,12,15,18,21,24,26,29,30,31,32,35,334};//,101};
			int secondRandom = random.nextInt(3);
			int t1 = grade / 5; 
			int ggrade = 15;
			
			if(secondRandom == 0)
			{
				ggrade = t1 * 5;
			}
			else if(secondRandom == 1)
			{
				ggrade = t1 * 5-5;
				// 135级后 每隔15级一更新
				if(ggrade >= 135)
					ggrade = t1 * 5-15;
			}
			else if(secondRandom == 2)
			{
				ggrade = t1 * 5+5;
				if(ggrade >= 135)
					ggrade = t1 * 5+15;
			}
			
			if(ggrade > 255)
				ggrade = 255;
			
			int index = random.nextInt(arrgood.length);
			int gtype = arrgood[index];
			
			String gid = UUID.randomUUID().toString().replaceAll("-", "");
			SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from goods_system where ggrade="+ggrade+" and gtype="+gtype);
			if(rowSet.next())
			{
				String name= rowSet.getString("name");
				long price = rowSet.getLong("price");
				int addhp = rowSet.getInt("addhp");
				int addattack = rowSet.getInt("addattack");
				int adddexterous = rowSet.getInt("adddexterous"); 
				int adddefence = rowSet.getInt("adddefence");
				int type = rowSet.getInt("type");
//				int gtype = rowSet.getInt("gtype");
				String descript = rowSet.getString("descript");
				
				int[] add = additional(ggrade,gtype);
				addhp += add[0];
				addattack += add[1];
				adddefence += add[2];
				jdbcTemplate.update("insert into goods(gid,name,masterid,price,life,addhp,addattack,adddefence,adddexterous,state,type,gareaid,descript,ggrade,unlawful_time,gtype) "
						+ "values('"+gid+"','"+name+"','"+successeruuid+"',"+price+","+Constant.life+","+addhp+","+addattack+","+adddefence+","+adddexterous+",3"+","+type+","+areaid+",'"+descript+"',"+ggrade+",UNIX_TIMESTAMP(),"+gtype+")");
				
				return "<br/><font color=red>"+diername+" 掉落了["+name+"]</font>";
			}
			return "";
		}
		// 续灵宝石 修复装备
		else if(dropgoodtype == 8)
		{
			int[] baoshi = new int[]{334};
			int r2 = random.nextInt(baoshi.length);
			String gid = UUID.randomUUID().toString().replaceAll("-", "");
			String name="";
//			if(r2 == 0)
//			{
				name = "续灵宝石";
				String sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '续灵宝石', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '可以给装备增续灵力耐久的宝石。', "+areaid+", '334', '2', '1', '0', '1',UNIX_TIMESTAMP());";
				jdbcTemplate.update(sql);
				return "<br/><br/><font color=red>"+diername+" 掉落了["+name+"]</font><br/>";
//			}
		}
		// 温芸灵玉 （兑换戒指必要）
		else if(dropgoodtype == 9)
		{
			SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select name,type,descript,gtype,price from goods_system where gtype=330");
			if(rowSet.next())
			{
				String descript = rowSet.getString("descript");
				String name = rowSet.getString("name");
				int gtype = rowSet.getInt("gtype");
				int type = rowSet.getInt("type");
				long price = rowSet.getInt("price");
				String gid = UUID.randomUUID().toString().replaceAll("-", "");
				String sql = "INSERT INTO goods (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`,`unlawful_time`) VALUES ('"+gid+"', '"+name+"', '"+successeruuid+"', '"+price+"', '"+Constant.life+"', '3', '0', '0', '0', '0', '"+type+"', '3', '"+descript+"', "+areaid+", '"+gtype+"',UNIX_TIMESTAMP());";
				jdbcTemplate.update(sql);
				return "<br/><br/><font color=red>"+diername+" 掉落了["+name+"]</font><br/>";
			}
			return "";
		}
		// 15级精品装
		else if(dropgoodtype == 7)
		{
			int[] arrgood = new int[]{1,4,7,10,13,16,19,2,5,8,11,14,17,20,23,3,6,9,12,15,18,21,24,26,29,30,31,32,35};//,101};
//			int n = random.nextInt(3);
			int ggrade = 15;
//			if(n == 0)
				ggrade = 15;
//			else if(n == 1)
//				ggrade = 10;
//			else 
//				ggrade = 5;
				
				int index = random.nextInt(arrgood.length);
				int gtype = arrgood[index];
			
//				if("c194a97b46b5498b878505c31db7e121".equals(successeruuid))
//				{
//					gtype = 32;
//				}
				
			if(gtype >= 26)
			{
				if(random.nextInt(5) != 0);
				{
					index = random.nextInt(arrgood.length);
					gtype = arrgood[index];
				}
			}
			
			String gid = UUID.randomUUID().toString().replaceAll("-", "");
			SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from goods_system where ggrade="+ggrade+" and gtype="+gtype);
			if(rowSet.next())
			{
				String name= rowSet.getString("name");
				long price = rowSet.getLong("price");
				int addhp = rowSet.getInt("addhp");
				int addattack = rowSet.getInt("addattack");
				int adddexterous = rowSet.getInt("adddexterous"); 
				int adddefence = rowSet.getInt("adddefence");
				int type = rowSet.getInt("type");
				String descript = rowSet.getString("descript");
				
				int[] add = additional(ggrade,gtype);
				addhp += add[0];
				addattack += add[1];
				adddefence += add[2];
				jdbcTemplate.update("insert into goods(gid,name,masterid,price,life,addhp,addattack,adddefence,adddexterous,state,type,gareaid,descript,ggrade,unlawful_time,gtype) "
						+ "values('"+gid+"','"+name+"','"+successeruuid+"',"+price+","+Constant.life+","+addhp+","+addattack+","+adddefence+","+adddexterous+",3"+","+type+","+areaid+",'"+descript+"',"+ggrade+",UNIX_TIMESTAMP(),"+gtype+")");
				
				return "<br/><font color=red>"+diername+" 身上爆落了["+name+"]</font>";
			}
			return "";
		}
		else
		{
			String gid = UUID.randomUUID().toString().replaceAll("-", "");
			String sql = null; 
			String name = "";
			name = "三级药水";
			sql = "INSERT INTO goods (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`) VALUES ('"+gid+"', '三级药水', '-1', '200', '"+Constant.life+"', '3', '2200', '0', '0', '0', '8', '3', '三级药水，可以补充体能，恢复血量2200.', "+areaid+", '33');";
			jdbcTemplate.update(sql);
			return "<br/><font color=red>"+diername+" 掉落了["+name+"]</font>";
		}
	}
	
	// 掉落物品
	private String dropNPC(int grade,int areaid,String diername,String dieruuid,String successername,String successeruuid,String attackertaskid,String defencetaskid,int dailytaskcount,int dailytaskruntimecount)
	{
//		if(true)
//		{
//			int[] baoshi = new int[]{334};
//			int r2 = random.nextInt(baoshi.length);
//			String gid = UUID.randomUUID().toString().replaceAll("-", "");
//			String name="";
//			if(r2 == 0)
//			{
//				name = "续灵宝石";
//				String sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '续灵宝石', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '可以给装备增续灵力耐久的宝石。', "+areaid+", '334', '2', '1', '0', '1',UNIX_TIMESTAMP());";
//				jdbcTemplate.update(sql);
//				return "<br/><br/><font color=red>"+diername+" 掉落了["+name+"]</font><br/>";
//			}
//		}
		
		int persontype = jdbcTemplate.queryForInt("select persontype from person where uuid='"+dieruuid+"'");
		if(persontype == 10)
		{
			// 打死特殊NPC 盗跖团伙的奖励
			if("3071".equals(dieruuid))
			{
				int r = random.nextInt(3);
				if(r == 1 || successeruuid.equals("0001"))
				{
					int timeCount = random.nextInt(6)+13;
					String dropMoneyDesc = "";
					dropMoneyDesc += dropSpecial(8, grade, areaid, diername,successeruuid);
					dropMoneyDesc += dropSpecial(9, grade, areaid, diername,successeruuid);
					dropMoneyDesc += dropSpecial(9, grade, areaid, diername,successeruuid);
					
					for (int i = 0; i < timeCount; i++)
					{
						// 药品和铜板
						if(i < 12)
						{
							dropMoneyDesc += dropSpecial(1, grade, areaid, diername,successeruuid);
						}
						else
						{
							dropMoneyDesc+= dropSpecial(3, grade, areaid, diername,successeruuid);
						}
					}
					String sss = "【"+successername+"】擒杀【盗跖】,盗跖身上爆落大量财物药物宝石名牌装备,但未发现藏宝图碎片和十殿阎王镇鬼戒";
					jdbcTemplate.update("update notice set author='太守府',notice='"+sss+"'");
					return "<br/><br/><font color='red'>盗跖：</font>纵横一世，最.最终还..是难逃厄运，呃...噗（吐血）,嗙(倒地身亡)。<br/>"+dropSpecial(2,grade, areaid, diername,successeruuid)+dropSpecial(2,grade, areaid, diername,successeruuid)+dropSpecial(2,grade, areaid, diername,successeruuid)+dropMoneyDesc;
				}
				else
				{
					int timeCount = random.nextInt(6)+15;
					String dropMoneyDesc = "";
					
					dropMoneyDesc += dropSpecial(9, grade, areaid, diername,successeruuid);
					dropMoneyDesc += dropSpecial(9, grade, areaid, diername,successeruuid);
					dropMoneyDesc += dropSpecial(9, grade, areaid, diername,successeruuid);
					
					for (int i = 0; i < timeCount; i++)
					{
						// 药品和铜板
						if(i < 13)
						{
							dropMoneyDesc += dropSpecial(1, grade, areaid, diername,successeruuid);
						}
						else
						{
							dropMoneyDesc+= dropSpecial(3, grade, areaid, diername,successeruuid);
						}
					}
					return "<br/><br/><font color='red'>盗跖：</font>想从我盗王之王手里夺宝，你们似乎把本王想得太简单了！不过能击退本王，这些就留给你们吧，大部分宝藏都藏在深山老林里了，我死了你们永远都找不到！哈哈哈。。。";
				}
			}
			else
			{
//				if(random.nextInt(2) == 1)
//				{
					return "<br/><br/><font color='red'>玄风飞盗：</font>大哥，对不起你，宝物丢失殆尽..无颜见你了，噗嗤(自刎而死)。<br/>"+dropSpecial(2,grade, areaid, diername,successeruuid)+dropSpecial(9, grade, areaid, diername,successeruuid);
//				}
//				else
//				{
//					return "<br/><br/><font color='red'>玄风飞盗：</font>贪婪的人类，哈哈，可惜财宝都不在我身上，都在我大哥身上！咳咳咳...呃";
//				}
			}
		}
		// 练洗之地 10%高爆率
		else if(persontype == 12)
		{
			// 10%概率
			if(random.nextInt(100) > 90 )// || "c194a97b46b5498b878505c31db7e121".equals(successeruuid))
			return dropSpecial(7, grade, areaid, diername,successeruuid);
		}
		
		if(defencetaskid != null && !"".equals(defencetaskid.trim()) && attackertaskid != null && !"".equals(attackertaskid.trim()) && attackertaskid.equals(defencetaskid))
		{
			//1.判断任务类型，执行相应的逻辑
			//2.检查任务完成度
			SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select tasktype,taskrequest,taskthings,taskname from daily_task where taskid='"+attackertaskid+"'");
			if(rowSet.next())
			{
//				int taskrequest = rowSet.getInt("taskrequest");
				int tasktype = rowSet.getInt("tasktype");
				String taskname = rowSet.getString("taskname");
				String taskthings = rowSet.getString("taskthings");
				// task aleady success
//				if(taskrequest <= dailytaskcount)
//				{
//					return "<br/><br/><font color=red>你完成了 【"+taskname+"】</font>";
//				}
//				else
//				{
					if(tasktype == 1)
					{
						jdbcTemplate.update("update person set dailytaskcount = dailytaskcount+1 where uuid='"+successeruuid+"'");
					}
					else if(tasktype == 2)
					{
						if(random.nextInt(3)== 0 )
						{
							jdbcTemplate.update("update person set dailytaskcount = dailytaskcount+1 where uuid='"+successeruuid+"'");
							return "<br/><br/><font color=red>你获得一个"+taskthings+"  任务:【"+taskname+"】</font>";
						}
					}
//				}
			}
			else
			{
				
			}
		}
		
		
		int rand = random.nextInt(100);
		
		if(rand > 70)
		{
			long num = grade;//(10+grade/2);
			
			String gid = UUID.randomUUID().toString().replaceAll("-", "");
			String gname = NumberConverUtil.getNameByMoney(num);
			
			jdbcTemplate.update("insert into goods(gid,name,masterid,price,state,type,gareaid,unlawful_time) values('"+gid+"','"+gname+"','"+successeruuid+"',"+num+","+3+","+11+","+areaid+",UNIX_TIMESTAMP()"+")");
			return "<br/><br/><font color=red>"+diername+" 掉落了["+gname+"]</font><br/>";
		}
		else if(rand == 6 || rand == 7 || rand == 8)
		{
			String gid = UUID.randomUUID().toString().replaceAll("-", "");
			String sql = null; 
			int type = random.nextInt(3);
			String name = "";
			if(0 == type)
			{
				name = "一级药水";
				sql = "INSERT INTO goods (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`,`unlawful_time`) VALUES ('"+gid+"', '一级药水', '"+successeruuid+"', '30', '"+Constant.life+"', '3', '300', '0', '0', '0', '8', '1', '一级药水，可以补充体能，恢复血量300.',"+areaid+" , '33',UNIX_TIMESTAMP());";
			}
			else if(type == 1)
			{
				name = "二级药水";
				sql = "INSERT INTO goods (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`,`unlawful_time`) VALUES ('"+gid+"', '二级药水', '"+successeruuid+"', '100', '"+Constant.life+"', '3', '1000', '0', '0', '0', '8', '2', '二级药水，可以补充体能，恢复血量1000.', "+areaid+", '33',UNIX_TIMESTAMP());";
			}
			else if(type == 2)
			{
				name = "三级药水";
				sql = "INSERT INTO goods (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`,`unlawful_time`) VALUES ('"+gid+"', '三级药水', '"+successeruuid+"', '200', '"+Constant.life+"', '3', '2200', '0', '0', '0', '8', '3', '三级药水，可以补充体能，恢复血量2200.', "+areaid+", '33',UNIX_TIMESTAMP());";
			}
			jdbcTemplate.update(sql);
			
			return "<br/><br/><font color=red>"+diername+" 掉落了["+name+"]</font><br/>";
		}
		// 戒指碎片
		else if(rand == 9)
		{
			if(random.nextInt(8) == 1)
			{
				SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select name,type,descript,gtype,price from goods_system where gtype=330");
				if(rowSet.next())
				{
					String descript = rowSet.getString("descript");
					String name = rowSet.getString("name");
					int gtype = rowSet.getInt("gtype");
					int type = rowSet.getInt("type");
					long price = rowSet.getInt("price");
					String gid = UUID.randomUUID().toString().replaceAll("-", "");
					String sql = "INSERT INTO goods (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`,`unlawful_time`) VALUES ('"+gid+"', '"+name+"', '"+successeruuid+"', '"+price+"', '"+Constant.life+"', '3', '0', '0', '0', '0', '"+type+"', '3', '"+descript+"', "+areaid+", '"+gtype+"',UNIX_TIMESTAMP());";
					jdbcTemplate.update(sql);
					return "<br/><br/><font color=red>"+diername+" 掉落了["+name+"]</font><br/>";
				}
			}
		}
		else 
			if(rand == 3)
		{
			if(grade < 20)
			{
				if(random.nextInt(5) != 0)
				{
					String gid = UUID.randomUUID().toString().replaceAll("-", "");
					String name = "一级药水";
					String sql = "INSERT INTO goods (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`,`unlawful_time`) VALUES ('"+gid+"', '一级药水', '"+successeruuid+"', '30', '"+Constant.life+"', '3', '300', '0', '0', '0', '8', '1', '一级药水，可以补充体能，恢复血量300.',"+areaid+" , '33',UNIX_TIMESTAMP());";
					jdbcTemplate.update(sql);
					return "<br/><br/><font color=red>"+diername+" 掉落了["+name+"]</font><br/>";
				}
			}
			String gid = UUID.randomUUID().toString().replaceAll("-", "");
			String sql = "";
			String goodName = "";
			int n = random.nextInt(32);
			// 一级材料  5/15概率
			if(n>0 && n <= 15)   // 5  4  3  2  1   ||  30  :  15  10   3  2  1   
			{
				int n2 = random.nextInt(5);
				switch (n2)
				{
					case 0:
						goodName = "一级金丝楠";
						// 金丝楠
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '一级金丝楠', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份一级金丝楠', "+areaid+", '281', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					case 1:
						goodName = "一级玄铁";
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '一级玄铁', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份一级玄铁', "+areaid+", '291', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					case 2:
						goodName = "一级白虎皮";
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '一级白虎皮', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份一级白虎皮', "+areaid+", '301', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					case 3:
						goodName = "一级琥珀";
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '一级琥珀', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份一级琥珀', "+areaid+", '311', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					case 4:
						goodName = "一级天蚕丝";
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '一级天蚕丝', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份一级天蚕丝', "+areaid+", '321', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					default:
						break;
				}
			}
			// 二级材料  4/15 概率
			else if(n > 15 && n <= 25)
			{
				int n2 = random.nextInt(5);
				switch (n2)
				{
					case 0:
						goodName = "二级金丝楠";
						// 金丝楠
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '二级金丝楠', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份二级金丝楠', "+areaid+", '282', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					case 1:
						goodName = "二级玄铁";
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '二级玄铁', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份二级玄铁', "+areaid+", '292', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					case 2:
						goodName = "二级白虎皮";
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '二级白虎皮', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份二级白虎皮', "+areaid+", '302', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					case 3:
						goodName = "二级琥珀";
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '二级琥珀', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份二级琥珀', "+areaid+", '312', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					case 4:
						goodName = "二级天蚕丝";
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '二级天蚕丝', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份二级天蚕丝', "+areaid+", '322', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					default:
						break;
				}
			}
			// 三级材料  3/15概率
			else if(n > 25 && n <=28)
			{
				int n2 = random.nextInt(5);
				switch (n2)
				{
					case 0:
						goodName = "三级金丝楠";
						// 金丝楠
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '三级金丝楠', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份三级金丝楠', "+areaid+", '283', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					case 1:
						goodName = "三级玄铁";
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '三级玄铁', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份三级玄铁', "+areaid+", '293', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					case 2:
						goodName = "三级白虎皮";
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '三级白虎皮', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份三级白虎皮', "+areaid+", '303', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					case 3:
						goodName = "三级琥珀";
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '三级琥珀', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份三级琥珀', "+areaid+", '313', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					case 4:
						goodName = "三级天蚕丝";
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '三级天蚕丝', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份三级天蚕丝', "+areaid+", '323', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					default:
						break;
				}
			}
			// 四级材料   2/15概率
			else if(n >28 && n <= 30)
			{
				int n2 = random.nextInt(5);
				switch (n2)
				{
					case 0:
						goodName = "四级金丝楠";
						// 金丝楠
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '四级金丝楠', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份四级金丝楠', "+areaid+", '284', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					case 1:
						goodName = "四级玄铁";
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '四级玄铁', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份四级玄铁', "+areaid+", '294', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					case 2:
						goodName = "四级白虎皮";
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '四级白虎皮', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份四级白虎皮', "+areaid+", '304', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					case 3:
						goodName = "四级琥珀";
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '四级琥珀', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份四级琥珀', "+areaid+", '314', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					case 4:
						goodName = "四级天蚕丝";
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '四级天蚕丝', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份四级天蚕丝', "+areaid+", '324', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					default:
						break;
				}
			}
			// 五级材料   1/15概率
			else if(n>30)
			{
				int n2 = random.nextInt(5);
				switch (n2)
				{
					case 0:
						goodName = "五级金丝楠";
						// 金丝楠
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '五级金丝楠', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份五级金丝楠', "+areaid+", '285', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					case 1:
						goodName = "五级玄铁";
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '五级玄铁', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份五级玄铁', "+areaid+", '295', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					case 2:
						goodName = "五级白虎皮";
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '五级白虎皮', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份五级白虎皮', "+areaid+", '305', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					case 3:
						goodName = "五级琥珀";
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '五级琥珀', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份五级琥珀', "+areaid+", '315', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					case 4:
						goodName = "五级天蚕丝";
						sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '五级天蚕丝', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '一份五级天蚕丝', "+areaid+", '325', '2', '1', '0', '1',UNIX_TIMESTAMP());";
						break;
					default:
						break;
				}
			}
			
			jdbcTemplate.update(sql);
			return "<br/><br/><font color=red>"+diername+" 掉落了["+goodName+"]</font><br/>";
		}
		else if(rand == 2 || rand == 5 || rand == 1)
		{
				// 二次随机
				int secondRandom = random.nextInt(3);
				int t1 = grade / 5;  // 0  1   2   3
				int[] arr = new int[]{1,4,7,10,13,16,19,22};
				int[] arrgood = new int[]{2,5,8,11,14,17,20,23,3,6,9,12,15,18,21,24};//,101};
				
//				101 玄武刀
//				201 玄武枪
//				301 玄武剑
//
//				401 玄武铠甲
//				501 玄武头盔
//				601 玄武披风
//				701 玄武腰带
//				801 玄武鞋子  
				
				// 掉落高5级的装备
				if(secondRandom == 2)
				{
					int ggrade = t1 * 5+5;
					
					if(ggrade > 255)
						ggrade = 255;
						
					// 掉带牌子的装备
					if(random.nextInt(5) == 1)
					{
						int index = random.nextInt(arrgood.length);
						int gtype = arrgood[index];
						
						// 玄武装备
//						if(gtype == 101)
//						{
//							if(random.nextInt(5)!=1)
//							{
//								index = random.nextInt(arrgood.length);
//								gtype = arrgood[index];
//							}
//						}
						
						String gid = UUID.randomUUID().toString().replaceAll("-", "");
						SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from goods_system where ggrade="+ggrade+" and gtype="+gtype);
						if(rowSet.next())
						{
							String name= rowSet.getString("name");
							long price = rowSet.getLong("price");
							int addhp = rowSet.getInt("addhp");
							int addattack = rowSet.getInt("addattack");
							int adddexterous = rowSet.getInt("adddexterous"); 
							int adddefence = rowSet.getInt("adddefence");
							int type = rowSet.getInt("type");
							String descript = rowSet.getString("descript");
							
							int[] add = additional(ggrade,gtype);
							addhp += add[0];
							addattack += add[1];
							adddefence += add[2];
//							adddexterous += add[0];
							jdbcTemplate.update("insert into goods(gid,name,masterid,price,life,addhp,addattack,adddefence,adddexterous,state,type,gareaid,descript,ggrade,unlawful_time) "
									+ "values('"+gid+"','"+name+"','"+successeruuid+"',"+price+","+Constant.life+","+addhp+","+addattack+","+adddefence+","+adddexterous+",3"+","+type+","+areaid+",'"+descript+"',"+ggrade+",UNIX_TIMESTAMP())");
							
							return "<br/><br/><font color=red>"+diername+" 掉落了["+name+"]</font><br/>";
						}
					}
					// 普通装备
					else
					{
						int index = random.nextInt(arr.length);
						int gtype = arr[index];
						
						String gid = UUID.randomUUID().toString().replaceAll("-", "");
						SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from goods_system where ggrade="+ggrade+" and gtype="+gtype);
						if(rowSet.next())
						{
							String name= rowSet.getString("name");
							long price = rowSet.getLong("price");
							int addhp = rowSet.getInt("addhp");
							int addattack = rowSet.getInt("addattack");
							int adddexterous = rowSet.getInt("adddexterous"); 
							int adddefence = rowSet.getInt("adddefence");
							int type = rowSet.getInt("type");
							String descript = rowSet.getString("descript");
							
							jdbcTemplate.update("insert into goods(gid,name,masterid,price,life,addhp,addattack,adddefence,adddexterous,state,type,gareaid,descript,ggrade,unlawful_time) "
									+ "values('"+gid+"','"+name+"','"+successeruuid+"',"+price+","+Constant.life+","+addhp+","+addattack+","+adddefence+","+adddexterous+",3"+","+type+","+areaid+",'"+descript+"',"+ggrade+",UNIX_TIMESTAMP())");
							return "<br/><br/><font color=red>"+diername+" 掉落了["+name+"]</font><br/>";
						}
					}
				}
				// 掉落同等级阶段的装备
				else if(secondRandom == 1)
				{
					int ggrade = t1 * 5;
					if(ggrade > 255)
						ggrade = 255;
					
					// 掉带牌子的装备
					if(random.nextInt(5) == 1)
					{
						int index = random.nextInt(arrgood.length);
						int gtype = arrgood[index];
						String gid = UUID.randomUUID().toString().replaceAll("-", "");
						SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from goods_system where ggrade="+ggrade+" and gtype="+gtype);
						if(rowSet.next())
						{
							String name= rowSet.getString("name");
							long price = rowSet.getLong("price");
							int addhp = rowSet.getInt("addhp");
							int addattack = rowSet.getInt("addattack");
							int adddexterous = rowSet.getInt("adddexterous"); 
							int adddefence = rowSet.getInt("adddefence");
							int type = rowSet.getInt("type");
							String descript = rowSet.getString("descript");
							
							int[] add = additional(ggrade,gtype);
							addhp += add[0];
							addattack += add[1];
							adddefence += add[2];
							
							jdbcTemplate.update("insert into goods(gid,name,masterid,price,life,addhp,addattack,adddefence,adddexterous,state,type,gareaid,descript,ggrade,unlawful_time) "
									+ "values('"+gid+"','"+name+"','"+successeruuid+"',"+price+","+Constant.life+","+addhp+","+addattack+","+adddefence+","+adddexterous+",3"+","+type+","+areaid+",'"+descript+"',"+ggrade+",UNIX_TIMESTAMP())");
							
							return "<br/><br/><font color=red>"+diername+" 掉落了["+name+"]</font><br/>";
						}
					}
					// 普通装备
					else
					{
						int index = random.nextInt(arr.length);
						int gtype = arr[index];
						
						String gid = UUID.randomUUID().toString().replaceAll("-", "");
						SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from goods_system where ggrade="+ggrade+" and gtype="+gtype);
						if(rowSet.next())
						{
							String name= rowSet.getString("name");
							long price = rowSet.getLong("price");
							int addhp = rowSet.getInt("addhp");
							int addattack = rowSet.getInt("addattack");
							int adddexterous = rowSet.getInt("adddexterous"); 
							int adddefence = rowSet.getInt("adddefence");
							int type = rowSet.getInt("type");
							String descript = rowSet.getString("descript");
							
							jdbcTemplate.update("insert into goods(gid,name,masterid,price,life,addhp,addattack,adddefence,adddexterous,state,type,gareaid,descript,ggrade,unlawful_time) "
									+ "values('"+gid+"','"+name+"','"+successeruuid+"',"+price+","+Constant.life+","+addhp+","+addattack+","+adddefence+","+adddexterous+",3"+","+type+","+areaid+",'"+descript+"',"+ggrade+",UNIX_TIMESTAMP())");
							return "<br/><br/><font color=red>"+diername+" 掉落了["+name+"]</font><br/>";
						}
					}
				}
				else 
				{
					int ggrade = t1 * 5 - 5;
					
					if(ggrade > 255)
						ggrade = 255;
					
					// 掉带牌子的装备
					if(random.nextInt(5) == 1)
					{
						int index = random.nextInt(arrgood.length);
						int gtype = arrgood[index];
						String gid = UUID.randomUUID().toString().replaceAll("-", "");
						SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from goods_system where ggrade="+ggrade+" and gtype="+gtype);
						if(rowSet.next())
						{
							String name= rowSet.getString("name");
							long price = rowSet.getLong("price");
							int addhp = rowSet.getInt("addhp");
							int addattack = rowSet.getInt("addattack");
							int adddexterous = rowSet.getInt("adddexterous"); 
							int adddefence = rowSet.getInt("adddefence");
							int type = rowSet.getInt("type");
							String descript = rowSet.getString("descript");
							
							int[] add = additional(ggrade,gtype);
							addhp += add[0];
							addattack += add[1];
							adddefence += add[2];
							
							jdbcTemplate.update("insert into goods(gid,name,masterid,price,life,addhp,addattack,adddefence,adddexterous,state,type,gareaid,descript,ggrade,unlawful_time) "
									+ "values('"+gid+"','"+name+"','"+successeruuid+"',"+price+","+Constant.life+","+addhp+","+addattack+","+adddefence+","+adddexterous+",3"+","+type+","+areaid+",'"+descript+"',"+ggrade+",UNIX_TIMESTAMP())");
							
							return "<br/><br/><font color=red>"+diername+" 掉落了["+name+"]</font><br/>";
						}
					}
					// 普通装备
					else
					{
						int index = random.nextInt(arr.length);
						int gtype = arr[index];
						
						String gid = UUID.randomUUID().toString().replaceAll("-", "");
						SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from goods_system where ggrade="+ggrade+" and gtype="+gtype);
						if(rowSet.next())
						{
							String name= rowSet.getString("name");
							long price = rowSet.getLong("price");
							int addhp = rowSet.getInt("addhp");
							int addattack = rowSet.getInt("addattack");
							int adddexterous = rowSet.getInt("adddexterous"); 
							int adddefence = rowSet.getInt("adddefence");
							int type = rowSet.getInt("type");
							String descript = rowSet.getString("descript");
							
							jdbcTemplate.update("insert into goods(gid,name,masterid,price,life,addhp,addattack,adddefence,adddexterous,state,type,gareaid,descript,ggrade,unlawful_time) "
									+ "values('"+gid+"','"+name+"','"+successeruuid+"',"+price+","+Constant.life+","+addhp+","+addattack+","+adddefence+","+adddexterous+",3"+","+type+","+areaid+",'"+descript+"',"+ggrade+",UNIX_TIMESTAMP())");
							return "<br/><br/><font color=red>"+diername+" 掉落了["+name+"]</font><br/>";
						}
					}
				}
		}
		// 掉落续灵宝石
		else if(rand == 10)
		{
			if(random.nextInt(500) == 95)
			{
				int[] baoshi = new int[]{334};
				int r = random.nextInt(baoshi.length);
				String gid = UUID.randomUUID().toString().replaceAll("-", "");
				String name="";
				if(r == 0)
				{
					name = "续灵宝石";
					String sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '续灵宝石', '"+successeruuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '可以给装备增续灵力耐久的宝石。', "+areaid+", '334', '2', '1', '0', '1',UNIX_TIMESTAMP());";
					jdbcTemplate.update(sql);
					return "<br/><br/><font color=red>"+diername+" 掉落了["+name+"]</font><br/>";
				}
			}
		}
		return "";
	}
	
	public Object sendSystemMessage(String receiver_uuid, String keyword)
	{
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 发系统消息提示
		String mid = UUID.randomUUID().toString().replaceAll("-", "");
		String notiMassgae = "insert into message(mid,senderid,receiveid,message,sendtime,readflag,sendername,ordertime,type) "
				+ "values("+ "'"+ mid+ "','0000','"+receiver_uuid+"','"+keyword+"','"+sdf.format(new Date(System.currentTimeMillis()))+"','false','系统消息',"+System.currentTimeMillis()+",2)";
		jdbcTemplate.update(notiMassgae);
		return null;
	}
	
	// 增加额外属性
	private int[] additional(int ggrade,int gtype)
	{
		int[] add = new int[4];
		Random random = new Random();
		switch (gtype)
		{
			// 猛虎
			case 2:
			case 5:
			case 8:
				add[0] = 0;
				add[1] = 60+random.nextInt(ggrade*6);
				add[2] = 0;
				add[3] = 0;
				return add;
		    // 蛮牛
			case 11:
			case 14:
			case 17:
			case 20:
			case 23:
				add[0] = 100+random.nextInt(ggrade*6);
				add[1] = 0;
				add[2] = 0;
				add[3] = 0;
				return add;
			// 雄鹰
			case 3:
			case 6:
			case 9:
				add[0] = 0;
				add[1] = 0;
				add[2] = 12+random.nextInt(ggrade);
				add[3] = 0;
				return add;
			// 巨熊
			case 12:
			case 15:
			case 18:
			case 21:
			case 24:
				add[0] = 0;
				add[1] = 0;
				add[2] = 50+random.nextInt(ggrade);
				add[3] = 0;
				return add;
				// 玄武  26,29,30,31,32,35
			case 26:
				add[0] = 60+random.nextInt(ggrade);
				add[1] = 35+random.nextInt(ggrade);
				add[2] = 12+random.nextInt(ggrade);
				add[3] = 0;
				return add;
			case 29:
			case 30:
			case 31:
			case 32:
			case 35:
				add[0] = 80+random.nextInt(ggrade);
				add[1] = 0;
				add[2] = 40+random.nextInt(ggrade);
				add[3] = 0;
				return add;		
//			case 30:
//				add[0] = 12+random.nextInt(ggrade);
//				add[1] = 0;
//				add[2] = 20+random.nextInt(ggrade);
//				add[3] = 0;
//				return add;		
//			case 31:
//				add[0] = 10+random.nextInt(ggrade);
//				add[1] = 0;
//				add[2] = 10+random.nextInt(ggrade);
//				add[3] = 0;
//				return add;					
//			case 32:
//				add[0] = 110+random.nextInt(ggrade);
//				add[1] = 0;
//				add[2] = 10+random.nextInt(ggrade);
//				add[3] = 0;
//				return add;	
//			case 35:
//				add[0] = 8+random.nextInt(ggrade);
//				add[1] = 0;
//				add[2] = 25+random.nextInt(ggrade);
//				add[3] = 0;
//				return add;					
				
			default:
				add[0] = 0;
				add[1] = 0;
				add[2] = 0;
				add[3] = 0;
				return add;
		}
	}


	private String shazhao(int type,String killer,String dier)
	{
		switch (type)
		{
			case 101:
				switch (random.nextInt(3))
				{
					case 0:return "<font color=red>【"+killer+"】</font>气运丹田，沉声一喝，提刀瞬斩，<font color=red>【"+dier+"】</font>躲闪不及，一声惨嚎，刹那身首异处。";
					case 1:return "<font color=red>【"+dier+"】</font>负隅顽抗之际，一个不留神，<font color=red>【"+killer+"】</font>乘隙迅出，手中大刀如风似电般甩出飞奔<font color=red>【"+dier+"】</font>心中，一声惨叫，劈为两段。";
					case 2:return "<font color=red>【"+dier+"】</font>连连败退，<font color=red>【"+killer+"】</font>紧追不舍，威然气势若黑云压城，但见<font color=red>【"+killer+"】</font>缓抬双手握紧手中刀，猛然一斩，势如烈火之刃，刀光过处，<font color=red>【"+dier+"】</font>惨嚎声中伴随鲜血飞溅。";
					default:
						break;
				}
				break;
			case 102:
				switch (random.nextInt(3))
				{
					case 0:return "<font color=red>【"+killer+"】</font>手中长枪，犹如猛龙，左右突进，令人防无可防，一式威势无匹的铁索横江，磅礴一击，山河动荡，须臾已将<font color=red>【"+dier+"】</font>钉死在地上。";
					case 1:return "两人，强招过后，各有损伤，但终是<font color=red>【"+killer+"】</font>更强一筹，长枪横扫，<font color=red>【"+dier+"】</font>眼花一瞬，立时被砸的脑浆飞溅，爆体而亡。";
					case 2:return "<font color=red>【"+killer+"】</font>不愿纠缠，收枪运使一招天下无双，顿时，枪如龙，气如山河，势如闪电，威如惊雷霹雳，<font color=red>【"+dier+"】</font>心知难逃死关，欲寻逃匿，转身急走，熟料身后威势已临，乍然已被扎成马蜂窝一般。";

					default:
						break;
				}
				
				break;
			case 103:
				switch (random.nextInt(3))
				{
					case 0:return "<font color=red>【"+killer+"】</font>聚神凝力，俄顷，狂剑旋扫，激荡出翩翩剑花，须臾之刻，剑灵之力倾注于剑身，一招百步飞剑蓄势而发，若白虹贯日，剑势携带强大剑气一式贯体，<font color=red>【"+dier+"】</font>顿感心中空缺，倒地而亡。";
					case 1:return "<font color=red>【"+killer+"】</font>手中剑翩翩舞，身形剑式毫无破绽，<font color=red>【"+dier+"】</font>急攻不下，一时气急，聚气冒险强进，<font color=red>【"+killer+"】</font>见机故露破绽，诱敌再入，寻得良机一招破敌，不容喘息，连连提剑再攻，<font color=red>【"+dier+"】</font>强挡数招后终是不敌，一个不留神，已是脑袋搬家。";
					case 2:return "<font color=red>【"+killer+"】</font>久攻不下，怒火再燃，不再留招，运使绝式，敌强我更强，挥舞出三道剑气，随即掌运飞剑尾随剑气，<font color=red>【"+dier+"】</font>勉强挡住剑气，熟料飞剑紧随，一剑贯体而出，<font color=red>【"+dier+"】</font>顿时血喷，倒地而亡。";
					default:
						break;
				}
				break;

			default:
				break;
		}
		return "<font color=red>【"+dier+"】</font>躲闪不及，一声惨嚎，登时丧命。";
	}
	
	// 获取客栈的地区码
	private int getKeZhanId(String countryid)
	{
		if("0000".equals(countryid))
		{
			return 1;
		}
		else if("0001".equals(countryid))
		{
			return 126;
		}
		else if("0002".equals(countryid))
		{
			return 214;	
		}
		else if("0003".equals(countryid))
		{
			return 306;
		}
		return 1;
	}
	
	@ResponseBody
	private String getDescript(String attackername,String attackerArmsName,String defencername,String defencerArmsName,int attackerByHart,int defencerByHart, int attackerHp, int defencerHp, int maxHPAttack, int maxHPDefence)
	{
		Random random = new Random();
		int spanAtt = maxHPAttack / 5;
		int spanDef = maxHPDefence / 5;
		String hpDescriptAtt = "";
		String hpDescriptDef = "";
		
		if(attackerHp > 0 && attackerHp < spanAtt)
		{
			hpDescriptAtt = "<font color=red>【"+attackername+"】</font>如风中残烛，危在旦夕，奄奄一息，似乎一阵风也能要了你的命。";
		}
		else if( attackerHp >= spanAtt && attackerHp<spanAtt * 2)
		{
			hpDescriptAtt = "<font color=red>【"+attackername+"】</font>已是身受重伤，创痕累累，身上满是血迹。";
		}
		else if(attackerHp >= spanAtt*2 && attackerHp < spanAtt * 3)
		{
			hpDescriptAtt = "<font color=red>【"+attackername+"】</font>受了点伤，正气喘吁吁的";
		}
		else if(attackerHp >= spanAtt * 3 && attackerHp < spanAtt * 4)
		{
			hpDescriptAtt = "<font color=red>【"+attackername+"】</font>似乎是受了点轻伤，气息不如平时，手脚似乎也有点不灵光了。";
		}
		else if(attackerHp >= spanAtt * 4)
		{
			hpDescriptAtt = "<font color=red>【"+attackername+"】</font>气血充盈，神采奕奕，活力无限。";
		}
		
		if(defencerHp > 0 && defencerHp < spanDef)
		{
			hpDescriptDef = "<font color=red>【"+defencername+"】</font>如风中残烛，危在旦夕，奄奄一息，似乎一阵风也能要了你的命。";
		}
		else if( defencerHp >= spanDef && defencerHp<spanDef * 2)
		{
			hpDescriptDef = "<font color=red>【"+defencername+"】</font>已是身受重伤，创痕累累，身上满是血迹。";
		}
		else if(defencerHp >= spanDef*2 && defencerHp < spanDef * 3)
		{
			hpDescriptDef = "<font color=red>【"+defencername+"】</font>受了点伤，正气喘吁吁的";
		}
		else if(defencerHp >= spanDef * 3 && defencerHp < spanDef * 4)
		{
			hpDescriptDef = "<font color=red>【"+defencername+"】</font>似乎是受了点轻伤，气息不如平时，手脚似乎也有点不灵光了。";
		}
		else if(defencerHp >= spanDef * 4)
		{
			hpDescriptDef = "<font color=red>【"+defencername+"】</font>气血充盈，神采奕奕，活力无限。";
		}
		
		switch (random.nextInt(6))
		{
			case 0:
				descript = hpDescriptAtt +"<font color=red>【"+attackername+"】</font>手持"+attackerArmsName+getAction()+(defencerByHart == 0 ? "可惜<font color=red>【"+defencername+"】</font>身手敏捷,急急往旁一闪,躲过你这一招":"结结实实打在<font color=red>【"+defencername+"】</font>身上，只听得闷哼一声，倒退一步。")+"旋即，对方发动反攻，你定了定神，只见<font color=red>【"+defencername+"】</font>操持着"+defencerArmsName+"飞速疾奔而来，倏地人已近前"+defencerArmsName+"寒光一闪，犹如出深之龙，但见"+getAction()+
				(attackerByHart == 0 ? (",堪堪殊料，<font color=red>【"+attackername+"】</font>就地往旁一滚，轻轻松松避过锋芒"):("<font color=red>【"+attackername+"】</font>霎时走神，寒芒逼近时已来不及躲闪，<font color=red>【"+defencername+"】</font>的"+defencerArmsName+"凌厉扎进<font color=red>【"+attackername+"】</font>的身体，好个透心凉"))+hpDescriptDef;
				break;
			case 1:
				descript = hpDescriptAtt+"<font color=red>【"+attackername+"】</font>手持"+attackerArmsName+getAction()+(defencerByHart == 0 ? "反手一击，掣天一击龙抬头，但看<font color=red>【"+defencername+"】</font>身法更胜敏捷,急急往旁一闪,躲过你这一招":"结结实实打在<font color=red>【"+defencername+"】</font>身上，只听得一声惨叫，倒退数步，一口鲜血喷涌而出。")+"旋即，对方亦发动反攻，你定了定神，只见<font color=red>【"+defencername+"】</font>操持着"+defencerArmsName+"飞速疾奔而来，倏地人已近前"+defencerArmsName+"寒光一闪，犹如出深之龙，但见"+getAction()+
				(attackerByHart == 0 ? (",却见，<font color=red>【"+attackername+"】</font>就地往旁一滚，婉然竞避过了锋芒，毫发未损，"):("<font color=red>【"+attackername+"】</font>霎时走神，寒芒逼近时已来不及躲闪，<font color=red>【"+defencername+"】</font>的"+defencerArmsName+"凌厉扎进<font color=red>【"+attackername+"】</font>的身体，好个透心凉"));
				break;
			case 2:
				descript = "杀风起，杀意升，劲锋走点，强者逞强！交织出一片刀剑禁区，只剩两人以命博命。狂风暴雨过后，又是一片宁静。两人内心，一者沉而稳，一者冷而沉！"+hpDescriptAtt+"<font color=red>【"+defencername+"】</font>再发奇招，一挺"+defencerArmsName+"祭出最后杀招，如风似电般疾奔<font color=red>【"+attackername+"】</font>,<font color=red>【"
				+attackername+"】</font>亦不示弱，倚仗手中"+attackerArmsName+"以强对强，直面对手"+(attackerByHart == 0? "两相对抗，终还是<font color=red>【"+defencername+"】</font>稍逊一筹,这一招落了下风，"+hpDescriptDef+"<font color=red>【"+attackername+"】</font>竟全身而退，未伤分毫":"纵使强如<font color=red>【"+attackername+"】</font>亦难过此招，又逢续补一掌，恰大中胸口，咳血不止")+hpDescriptDef;
				break;
			case 3:
				descript = "身法、战影，速度的较量、力量的对决，正是刺激之战，<font color=red>【"+defencername+"】</font>忽快忽慢的身法、大开大合、又带冥翳招式法，以快制快、以守化攻的<font color=red>【"+attackername+"】</font>，冷冽的双眼，紧紧盯住<font color=red>【"+defencername+"】</font>的身影了！"+hpDescriptAtt;
				break;
			case 4:
				descript = "交缠争斗的身影、如轻点苹花倚清风掌飞，"+hpDescriptAtt+"<font color=red>【"+attackername+"】</font>厉掌辅助，天衣无逢，招示相连，三次交手，不耐对方屡次纠缠的<font color=red>【"+defencername+"】</font>，"+defencerArmsName+"转身疾旋，旋如九霄鱼龙耀水晶，一招伤重<font color=red>【"+attackername+"】</font>,<font color=red>【"+attackername+"】</font>一呼：「走」见情势不利，<font color=red>【"+attackername+"】</font>虚应一招抽身而走,熟料<font color=red>【"+defencername+"】</font>怒火越炽，亟亟追上点划直逼，"+hpDescriptAtt+"<font color=red>【"+attackername+"】</font>一时措守不及，步步退败。"+hpDescriptDef;
				break;
			default:
				descript = hpDescriptAtt+"<font color=red>【"+attackername+"】</font>紧握"+attackerArmsName+getAction()+(defencerByHart == 0 ? "磅礴一击，撼山动海，对手眼前一惊，但旋即回过神来，摆开阵脚，运使奇招，霎时但观<font color=red>【"+defencername+"】</font>身法凌厉,险险躲过你这杀招":"结结实实打在<font color=red>【"+defencername+"】</font>身上，只听得闷哼一声，倒退一步。")+"旋即，对方发动反攻，你定了定神，只见<font color=red>【"+defencername+"】</font>操持着"+defencerArmsName+"飞速疾奔而来，倏地人已近前"+defencerArmsName+"寒光一闪，犹如出深之龙，但见"+getAction()+
				(attackerByHart == 0 ? (",人世本无常，又岂料，<font color=red>【"+attackername+"】</font>就地往旁一滚，竟轻轻松松避过锋芒"):("<font color=red>【"+attackername+"】</font>霎时走神，寒芒逼近时已来不及躲闪，<font color=red>【"+defencername+"】</font>的"+defencerArmsName+"凌厉扎进<font color=red>【"+attackername+"】</font>的身体，好个透心凉"))+hpDescriptDef;
				break;
		}
		return descript;
	}

	@ResponseBody
	private String getAction()
	{
		Random random = new Random();
		String action = "横扫";
		switch (random.nextInt(7))
		{
			case 0:action = "直取突刺";break;
			case 1:action = "顺势疾斩";break;
			case 2:action = "寒锋突转";break;
			case 3:action = "寒芒疾驰";break;
			case 4:action = "如燕飞掠";break;
			case 5:action = "疾风逆斩";break;
			case 6:action = "强势横扫";break;

			default:
				break;
		}
		return action;
	}
	

	



	
	/**
	 * 查询语句
	 * @author 许仕永(xsy)
	 * des: 
	 * @param table		 表名
	 * @param condition 条件  条件为空则将会查询并返回表中所有数据记录
	 * @param type		 类型  0|JSONObject  1|JSONArray
	 * @param pagesize 
	 * @param page 
	 * @return 查询结果的json格式字符串
	 */
	public Object query(String table,String condition,int type)
	{
		try
		{
			String sql;
			if(condition == null || condition.length() == 0 || "".equals(condition) || "null".equals(condition))
				sql = "select * from "+table;
			else
			{
				sql = "select * from "+table+" where "+condition;
			}
			
			if(condition == null || condition.isEmpty())
			{
				sql = "select * from "+table;
				type = 1;
			}
			
			if(type == 1)
			{
				return jdbcTemplate.queryForList(sql);
			}
			else
			{
				return jdbcTemplate.queryForMap(sql);
			}
		}
		catch (Exception e)
		{
			System.out.println("xushiyong登录异常="+e.toString());
		}
		return null;
	}
	
	/**
	 * 查询语句
	 * @author 许仕永(xsy)
	 * des: 
	 * @param table		 表名
	 * @param condition 条件  条件为空则将会查询并返回表中所有数据记录
	 * @param type		 类型  0|JSONObject  1|JSONArray
	 * @param pagesize 
	 * @param page 
	 * @return 查询结果的json格式字符串
	 */
	@ResponseBody
	public Object query(String table,String condition,int type, String page, String pagesize)
	{
		try
		{
			//connSQL();
			String sql;
			if(condition == null || condition.length() == 0 || "".equals(condition) || "null".equals(condition))
			{
				//System.out.println("分页查询sql1 1111");
				if(page == null || "".equals(page))
				{
					sql = "select * from "+table;
				}
				else
				{
					//System.out.println("分页查询sql1 33333");
					int p = Integer.valueOf(page);
					int ps;
					
					if(pagesize == null || "".equals(pagesize))
					{
						ps = 12;
					}
					else
					{
						ps= Integer.valueOf(pagesize);
					}
						
					sql = "select * from "+table+" order by sorttime desc "+" limit "+(p-1) * ps+","+ps;
					//System.out.println("分页查询sql="+sql);
				}
			}
			else
			{
				if(page == null || "".equals(page))
				{
					sql = "select * from "+table+" where "+condition;
					//System.out.println("分页查询sql1 22222");					
				}
				else
				{
					//System.out.println("分页查询sql1 33333");
					int p = Integer.valueOf(page);
					int ps;
					
					if(pagesize == null || "".equals(pagesize))
					{
						ps = 12;
					}
					else
					{
						ps= Integer.valueOf(pagesize);
					}
						
					sql = "select * from "+table+" where "+condition+" limit "+(p-1) * ps+","+ps;
				}
			}
			
			if(condition == null || condition.isEmpty())
			{
				sql = "select * from "+table;
				type = 1;
			}
			
			if(type == 1)
			{
				return jdbcTemplate.queryForList(sql);
			}
			else
			{
				return jdbcTemplate.queryForMap(sql);
			}	
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		return null;
	}
	
	
//	@ResponseBody
	public boolean upgrade(String uuid)
	{
		try
		{
			SqlRowSet set = jdbcTemplate.queryForRowSet("select gender,grade,exp,nextgradeexp,attack,defence,dodge,maxhp from person where uuid='"+uuid+"'");
			if(set.next())
			{
				int grade = set.getInt("grade");
				long exp = set.getLong("exp");
				int gender = set.getInt("gender");
				long nextgradeexp = set.getLong("nextgradeexp");
				
				int gardeTemp = grade + 1;
				long nextExp = 0;
				
				nextExp = (long) Math.pow(gardeTemp, 2.85)+gardeTemp*300;
				
				// 升级策略
				// valid

				if(exp >= (nextgradeexp) && nextgradeexp != 0)
				{
					// 查询出已装备的武装的属性
					SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select addhp,addattack,adddefence,adddexterous from goods where masterid='"+uuid+"' and state=1");
					int addhp = 0;
					int addattack = 0;
					int adddefence = 0;
					int adddexterous = 0;
					
					while (rowSet.next())
					{
						addhp += rowSet.getInt("addhp");
						addattack += rowSet.getInt("addattack");
						adddefence += rowSet.getInt("adddefence");
						adddexterous += rowSet.getInt("adddexterous");
					}
					
					String desc = updatePersonDescript(grade, gender);
					// upgrade !
					jdbcTemplate.update("update person set descript='"+desc+"',grade = grade+1,attack=("+Constant.initAttack+"+grade*"+Constant.attack+"+"+addattack+"),defence=("+Constant.initDefence+"+grade*"+Constant.defence+"+"+adddefence+"),dodge=("+Constant.initDodge+"+grade*"+Constant.dodge+"+"+adddexterous+"),maxhp=("+Constant.initHp+"+grade*"+Constant.hp+"+"+addhp+"),exp="+(exp - (nextgradeexp)) +",nextgradeexp="+nextExp+" where uuid='"+uuid+"'");
					
					return true;//object.toString();
				}
				else
				{
					return false;
				}
			}
		}
		catch (Exception e)
		{
				return false;
		}
		return false;
	}
	
	private String updatePersonDescript(int grade,int gender)
	{
		String[] string = new String[]
				{
					"你还只是个总角之年活泼可爱的孩童，天真无邪的脸庞，那份纯真可爱的稚气显露无遗。",
					"你已到了束发的年纪，虽尚显稚气，但俊朗的脸上已经有了两分成熟的气息。",
					"你已到了"+(gender == 0?"弱冠":"花信")+"之年，这个年纪代表你已经成人了，未来的一切都在自己手中。",
					"而立之年的你，脸上多了一份英姿，在乱世中闯荡了这么久，性情也变得刚毅坚强。",
					"时光飞逝，不惑之年的你，已然身经百战，小有一番成就，成熟而稳重，待人接物处乱不惊。",
					"知命之年，你已看透世事，通晓人情世故，内心对外界一片豁达。",
					"年近花甲，两鬓有了少许白发，你感叹世事无常，老已将至。",
					"你皓然白首，满头白发，已似风中残烛的年纪，唯有眼神依然凌厉有神。"
				};
		int i = 0;
		
		if(grade<15)
		{
			i = 0;
		}
		else if(grade>=15 && gender< 25)
		{
			i = 1;
		}
		else if(grade >= 25&&grade < 40)
		{
			i = 2;
		}
		else if(grade >=40 && grade < 50)
		{
			i = 3;
		}
		else if(grade >=50 && grade<60)
		{
			i=4;
		}
		else if(grade >= 60 && gender< 80)
		{
			i=5;
		}
		else if(gender >=80 && gender<100)
		{
			i=6;
		}
		else if(gender >=100 && gender<120)
		{
			i=7;
		}
		return string[(i < string.length?i:0)];
	}
	

	
	@ResponseBody
	public String queryUserInfo(String uuid)
	{
		 Map<String, Object> userdate = jdbcTemplate.queryForMap("select groupid,uuid,chenghao,chenghaodescript,sign,dialog,nextpositionexp,name,descript,nextgradeexp,grade,maxhp,attack,defence,dodge,areaid,countryid,gender,countryname,hp,type,state,position,positionexp,exp from person where uuid='"+uuid+"'");
		 List<Map<String, Object>> goods =  jdbcTemplate.queryForList("select * from goods where masterid='"+uuid+"' and state="+1);
		 String groupid = (String) userdate.get("groupid");
		 
		 String groupname = null;
		 SqlRowSet groupnameSet = jdbcTemplate.queryForRowSet("select group_name from group_list where groupid='"+groupid+"'");
		 
		 if(groupnameSet.next())
			 groupname = groupnameSet.getString("group_name");
			 
		 net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();//.fromObject(string);
		 jsonObject.element("userdata", userdate);
		 jsonObject.element("goods", goods);
		 jsonObject.element("groupname", groupname);
		 
		return jsonObject.toString();
	}
	
	/**
	 * 特定查询语句 查询地区的所有数据信息
 	 * @author 许仕永(xsy)
	 * des: 
	 * @param table		 表名
	 * @param condition 条件  条件为空则将会查询并返回表中所有数据记录
	 * @param uuid 
	 * @param type 
	 * @param type		 类型  0|JSONObject  1|JSONArray
	 * @param token 
	 * @param version 
	 * @return 查询结果的json格式字符串
	 */
	@ResponseBody
	public String queryPlaceAllInfo(String name,String curid,String condition, String uuid, String type, String token)
	{
		try
		{
			// 未开发城市限制
			if("521".equals(condition) || "421".equals(condition))
			{
				net.sf.json.JSONObject jsonObjectPK = new net.sf.json.JSONObject(); 
				jsonObjectPK.element("code", 1025);
				jsonObjectPK.element("message", "该城市还在黄巾军手中，无法前往!");
				return jsonObjectPK.toString();
			}
			else if("111".equals(condition) || "221".equals(condition) || "003".equals(condition) )
			{
				SqlRowSet subCost = jdbcTemplate.queryForRowSet("select money,grade from person where uuid='"+uuid+"'");
				if(subCost.next())
				{
					int grade = subCost.getInt("grade");
					Long money = subCost.getLong("money");
					
					if(grade < 10)
					{
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("code", 1025);
						jsonObject.put("message", "外面的世界太危险，还是先留在新手村吧");
						return jsonObject.toString();
					}
					
					if(money < 50)
					{
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("code", 1025);
						jsonObject.put("message", "你身上的钱并不足以支付传送费用，你还是靠腿走吧！");
						return jsonObject.toString();
					}
					else
					{
						// 是传送
						if("2".equals(type))
						{
							jdbcTemplate.update("update person set money=money-50 where uuid='"+uuid+"'");
						}
					}
				}
			}
			else if("30".equals(condition))
			{
				int grade = jdbcTemplate.queryForInt("select grade from person where uuid='"+uuid+"'");
				if(grade > 15)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code", 1025);
					jsonObject.put("message", "你的等级已经超出限制，【练洗之地】不是你该来的地方");
					return jsonObject.toString();
				}
				else
				{
					int state = jdbcTemplate.queryForInt("select state_micheng from state_list where sid=1002");
					if(state == 0)
					{
						if(curid.equals(condition))
						{
							condition = "28";
						}
						else
						{
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("code", 1025);
							jsonObject.put("message", "【练洗之地】本时段尚未开放，请等待系统通知。");
							return jsonObject.toString();
						}
					}
				}
			}
			
			
			/** 功能 : 死亡后回到新手村客栈 start*/
			// 校验客户端的地区码是否和服务器上数据的地区码一致。
			SqlRowSet rowSetValid = jdbcTemplate.queryForRowSet("select areaid,isbattle,enemyid from person where uuid='"+uuid+"'");
			rowSetValid.next();
			String isbattle = rowSetValid.getString("isbattle");
			String enemyid = rowSetValid.getString("enemyid");
			
			
			int aid = rowSetValid.getInt("areaid");
			// 刷新
			if("0".equals(type))
			{
				// 地区码取服务器数据库版本的地区码
				if(!curid.equals(aid+""))
					condition = aid+"";
			}
			// 移动行走
			else if("1".equals(type))
			{
				// 如果不一致，则条件变更为服务器数据库地区码
				if(!curid.equals(aid+""))
				{
					condition = aid+"";
				}
			}
			
			/** 功能 : 死亡后回到新手村客栈 end*/

			int updateRow;
			if("0003".equals(uuid))
			{
				String sqlUpdate = "update person set areaid="+condition+" where uuid ="+"'"+uuid+"' or uuid='1209'"; 
				updateRow = jdbcTemplate.update(sqlUpdate);
			}
			else if("13511e6f01d84945aff526444ebcd944".equals(uuid))
			{
				String sqlUpdate = "update person set areaid="+condition+" where uuid ="+"'"+uuid+"' or uuid='1210'"; 
				updateRow = jdbcTemplate.update(sqlUpdate);
			}
			else
			{
				String sqlUpdate = "update person set areaid="+condition+" where uuid ="+"'"+uuid+"'"; 
				updateRow = jdbcTemplate.update(sqlUpdate);
			}
			
//			System.out.println("多方登陆验证:"+"select count(*) from online where uuid='"+uuid+"' and oid='"+token+"'");
			int count = jdbcTemplate.queryForInt("select count(*) from online where uuid='"+uuid+"' and oid='"+token+"'");
			// 在线表中没有当前客户端的登录信息，则认为有人已经把这个登录顶下去了
			if(count < 1)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 1024);
				jsonObject.put("message", "您的账号在另一个设备上登录了,您被迫下线。");
				return jsonObject.toString();
			}
				
			// 缉捕
			String isjibu = jibu(uuid,condition);
			if(isjibu != null)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("enemy", isjibu);
				jsonObject.put("message", "守卫冷眼一撇，目光正盯在你身上，缓缓拔出剑朝你走过来。。。看来你的罪行已经被看出来了");
				return jsonObject.toString();
			}
			
			
			if("true".equals(isbattle))
			{
				net.sf.json.JSONObject jsonObjectPK = new net.sf.json.JSONObject(); 
				jsonObjectPK.element("code", 201);
				jsonObjectPK.element("enemy", enemyid);
				jsonObjectPK.element("message", "似乎有人要杀你");
				return jsonObjectPK.toString();
			}


			
			// 更新最后一次动作时间,并恢复state=1在线状态  用于超时无动作下线功能
			jdbcTemplate.update("update person set last_action_time = UNIX_TIMESTAMP(),state=1,isonline='true',isbattle='false' where uuid='"+uuid+"'");
			// 好友表同步更新
			jdbcTemplate.update("update friend set state = 1,last_time=UNIX_TIMESTAMP() where fuuid='"+uuid+"'");
			

			
			
			
			
			// 地点信息
			String sqlQueryPlace = "select * from place where areaid = "+condition;
			Map<String, Object> string = jdbcTemplate.queryForMap(sqlQueryPlace);//ResultSetTool.resultSetToJsonObject(sqlQueryPlaceSet).toString();
			net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(string);//.fromObject(string);
			
			// npc数据
			String sqlNPC = "select uuid,name,areaid,countryname,type from person where areaid="+condition+" and (type=2 or type=3) and state=1";
			List<Map<String, Object>> stringNPC = jdbcTemplate.queryForList(sqlNPC);
			JSONArray array = JSONArray.fromObject(stringNPC);
			
			// 消息数据
			String sqlMessage = "select sendername from message where receiveid='"+uuid+"' and readflag='false'";
			String messageTip = "false";
			SqlRowSet rowSet1 = jdbcTemplate.queryForRowSet(sqlMessage);
			if(rowSet1.next())
				messageTip = "true";
			
			// 交易请求
			String transaction = "false";
			String queryTransaction = "select count(1) from transaction_list where buyer_uuid ='"+uuid+"'";
			SqlRowSet rowSetTransaction = jdbcTemplate.queryForRowSet(queryTransaction);
			if(rowSetTransaction.next())
				transaction = "true";
				
			
			// 邀请消息
			String invitation = "false";
			String queryInvitation = "select teammasterid from team_request where teammemberid ='"+uuid+"'";
			SqlRowSet queryInvitationSet = jdbcTemplate.queryForRowSet(queryInvitation);
			if(queryInvitationSet.next())
				invitation = "true";
			

			// 玩家自身信息
			String selfData = "select uuid,manager,chenghao,chenghaodescript,sign,dialog,nextpositionexp,name,descript,nextgradeexp,grade,maxhp,attack,defence,dodge,areaid,countryid,gender,countryname,hp,type,state,position,positionexp,question,exp from person where uuid='"+uuid+"'";
			Map<String, Object> userMap = null;
			try
			{
				userMap = jdbcTemplate.queryForMap(selfData);
			}
			catch (Exception e)
			{
				net.sf.json.JSONObject res = new net.sf.json.JSONObject();
				res.put("code", "201");
				res.put("message", "很遗憾！未查询到您的用户信息！");
				return res.toString();
			}
			userMap.put("token", token);
			net.sf.json.JSONObject userJSON = net.sf.json.JSONObject.fromObject(userMap);
			
			// 战斗消息数据
			String sqlBattle = "select descript from battle where attackerid='"+uuid+"' or defencerid='"+uuid+"'";
			List<Map<String, Object>> stringBT = jdbcTemplate.queryForList(sqlBattle);//ResultSetTool.resultSetToJsonArry(sqlQueryBTSet).toString();					
			JSONArray arrayBT = JSONArray.fromObject(stringBT);
			
			Map<String, Object> notice = null;
			try
			{
				String getNotice = "select * from notice where id='1'";
				notice = jdbcTemplate.queryForMap(getNotice);
			}
			catch (Exception e)
			{
				// TODO: handle exception
				System.out.println("yic =="+e.toString());
			}
			
			// player 数据
			String sqlPlayer = "select uuid,dialog,name,areaid,countryname,type from person where areaid="+condition+" and type=1 and state=1";
			List<Map<String, Object>> stringPlayer = jdbcTemplate.queryForList(sqlPlayer);//ResultSetTool.resultSetToJsonArry(sqlQueryPlayerSet).toString();					
			JSONArray arrayPlayer = JSONArray.fromObject(stringPlayer);
			
			String sqlGoods = "select * from goods where gareaid='"+condition+"' and state=3";
			List<Map<String, Object>> goods = jdbcTemplate.queryForList(sqlGoods);
			JSONArray arrayGoods = JSONArray.fromObject(goods);
			
			// 非原地刷新
			if(!curid.equals(condition))
			{
				name = new String(name.getBytes("ISO-8859-1"),"utf-8");
				String position =null;
				String countryname = null;
				int redvalue = 0;
				SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select position,taskid,redvalue,countryname from person where uuid='"+uuid+"'");

				if(rowSet.next())
				{
					position = rowSet.getString("position");
					redvalue = rowSet.getInt("redvalue");
					countryname = rowSet.getString("countryname");
				}
					
				
				SqlRowSet rowSetForgoto = jdbcTemplate.queryForRowSet("select name from place where areaid="+condition);
				String placeName = null;
				if(rowSetForgoto.next())
					placeName = rowSetForgoto.getString("name");
				
				jdbcTemplate.update("delete from move_message where uuid='"+uuid+"'");
				if(position != null && !"平民".equals(position))
				{
					jdbcTemplate.update("INSERT into move_message(uuid,areaid,type,message) VALUES ('"+uuid+"',"+condition+",1,'"+"<font color=red>【"+getDesignation(countryname, redvalue)+"】【"+position+"】</font>"+name+"来到此地')");
					jdbcTemplate.update("INSERT into move_message(uuid,areaid,type,message) VALUES ('"+uuid+"',"+curid+",2,'"+"<font color=red>【"+getDesignation(countryname, redvalue)+"】【"+position+"】</font>"+name+"离开此地，往"+placeName+"方向去了。')");
				}
				else
				{
					jdbcTemplate.update("INSERT into move_message(uuid,areaid,type,message) VALUES ('"+uuid+"',"+condition+",1,'"+"【"+getDesignation(countryname, redvalue)+"】"+name+"来到此地')");
					jdbcTemplate.update("INSERT into move_message(uuid,areaid,type,message) VALUES ('"+uuid+"',"+curid+",2,'"+"【"+getDesignation(countryname, redvalue)+"】"+name+"离开此地，往"+placeName+"方向去了。')");
				}
			}

			// 到达
			String sqlMoveMessageAchieve = "select * from move_message where type=1 and areaid="+condition;
			List<Map<String, Object>> listAchieve = jdbcTemplate.queryForList(sqlMoveMessageAchieve);
			JSONArray arrayAchieve = JSONArray.fromObject(listAchieve);

			// 离开
			String sqlMoveMessageLeave = "select * from move_message where type=2 and areaid="+condition;
			List<Map<String, Object>> listLeave = jdbcTemplate.queryForList(sqlMoveMessageLeave);
			JSONArray arrayLeave = JSONArray.fromObject(listLeave);
			
			// 获取一次后就立刻清理掉消息
			jdbcTemplate.update("update person set isbattle = 'false',dialog='' where uuid='"+uuid+"'");

			String battle_string = null;
			SqlRowSet battleSet = jdbcTemplate.queryForRowSet("select battle_string from place where areaid="+condition);
			if(battleSet.next())
				battle_string = battleSet.getString("battle_string");
			
			jsonObject.element("code", 200);
			jsonObject.elementOpt("npc", array);
			jsonObject.element("player", arrayPlayer);
			jsonObject.element("result", updateRow);
			jsonObject.element("message", messageTip);
			jsonObject.element("battlelist", arrayBT);
			jsonObject.element("goods", arrayGoods);
			jsonObject.element("notice", notice);
			jsonObject.element("userdata", userJSON);
			jsonObject.element("achievelist", arrayAchieve);
			jsonObject.element("leavelist", arrayLeave);
			jsonObject.element("transaction", transaction);
			jsonObject.element("battle_string", battle_string);
			jsonObject.element("invitation", invitation);
			
			String result = jsonObject.toString();
			
			return result;
		}
		catch (Exception e)
		{
			// TODO: handle exception
			System.out.println("移动方法出异常 = "+e.toString());
		}
		return null;
	}
	
	private String jibu(String uuid, String condition)
	{
		// 1.判断红名
		int redvalue = 0;
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select name,redvalue,countryid from person where uuid='"+uuid+"'");
		if(rowSet.next())
		{
			redvalue = rowSet.getInt("redvalue");
			String countryid = rowSet.getString("countryid");
			if(redvalue >= 5)
			{
				String sql = "select uuid from person where state=1 and areaid='"+condition+"' and isbattle='false' and (name like '%城市守卫' or name='新手村守卫' or name='解府总管' or name='洛阳士兵' or name='南阳士兵' or name='解凌云' or name='解语嫣') and countryid!='"+countryid+"'";
				SqlRowSet rowSet2 = jdbcTemplate.queryForRowSet(sql);
				String attackerid = null;
				if(rowSet2.next())
				{
					attackerid = rowSet2.getString("uuid");
					battle(attackerid, uuid, "1");
				}
				return attackerid;
			}	
		}
		return null;
	}
	
	private String getDesignation(String countryName,int redValue)
	{
		if(redValue >= 5 && redValue < 10)
		{
			return "三等通缉犯";
		}
		else if(redValue >= 10 && redValue < 15)
		{
			return "二等通缉犯";
		}
		else if(redValue >= 15 && redValue< 20)
		{
			return "一等通缉犯";
		}
		else if(redValue >= 20 && redValue< 25)
		{
			return "特级死刑犯";
		}
		else if(redValue >= 25 && redValue < 30)
		{
			return "御批死刑犯";
		}
		else if(redValue >=30)
		{
			return "十恶不赦";
		}
		else if(redValue < 5)
		{
			return countryName;
		}
		
		return "御批死刑犯";
	}
	
	/** 设置改uuid的消息为已读*/
	@ResponseBody
	public void updateMessageState(String uuid)
	{
		try
		{
//			connSQL();
			// 设置为已读
			String resetRead = "update message set readflag='true' where receiveid ='"+uuid+"'";
			jdbcTemplate.update(resetRead);
//			closeDB();
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
	}
	



//	
//	public String getGoods(String uuid)
//	{
//		
//	}

	public String taskcheck(String taskmasteruuid, String taskreceiveruuid)
	{
//		System.out.println("request="+taskrequest+"   uuid="+taskmasteruuid);
		System.out.println("select (select dailytaskid from person where uuid='"+taskmasteruuid+"') as masterdailytaskid,dailytaskid,taskid,taskstate,taskprogress,gender,tasktarget from person where uuid='"+taskreceiveruuid+"'");
		SqlRowSet rowSetPlayer = jdbcTemplate.queryForRowSet("select (select dailytaskid from person where uuid='"+taskmasteruuid+"') as masterdailytaskid,dailytaskid,taskid,taskstate,taskprogress,gender,tasktarget from person where uuid='"+taskreceiveruuid+"'");
		if(!rowSetPlayer.next())
			return null;
		
		String dailytaskid = rowSetPlayer.getString("dailytaskid");
		String masterdailytaskid = rowSetPlayer.getString("masterdailytaskid");
		int playerTaskId = rowSetPlayer.getInt("taskid");
		int gender = rowSetPlayer.getInt("gender");
		int tasktarget = rowSetPlayer.getInt("tasktarget");
		int taskprogress = rowSetPlayer.getInt("taskprogress");
		int taskstate = rowSetPlayer.getInt("taskstate");
		
		// 送信任务
		if(masterdailytaskid != null && !"".equals(masterdailytaskid.trim()) && masterdailytaskid.equals(dailytaskid))
		{
			jdbcTemplate.update("update person set dailytaskcount=dailytaskcount+1 where uuid='"+taskreceiveruuid+"'");
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 2);
			jsonObject.put("message", "劳烦你送信前来，辛苦了，回去回报吧。");
			
			return jsonObject.toString();
		}
		
		SqlRowSet rowSetTask = jdbcTemplate.queryForRowSet("select * from task where taskmasterid='"+taskmasteruuid+"' and taskid="+playerTaskId);
		if(!rowSetTask.next())
			return null;
		
		String taskovermessage = rowSetTask.getString("taskovermessage");
		int taskrewardexp = rowSetTask.getInt("taskrewardexp");
		int taskID = rowSetTask.getInt("taskid");
		String taskname = rowSetTask.getString("taskname");
		String message = rowSetTask.getString("taskdescript");
		long taskrewardmoney = rowSetTask.getLong("taskrewardmoney");
		int taskrequest = rowSetTask.getInt("taskrequest");
		
		
		try
		{
			// 任务有效
			if(taskID == playerTaskId)
			{
				JSONObject object = new JSONObject();
				// 任务还未完成
				if((taskprogress < tasktarget) || (tasktarget == 0))
				{
					// 接任务时的对话
					if(taskstate == 0)
					{
						object.put("code", 0);
						object.put("message", "任务:["+taskname+"]\n"+message);
						jdbcTemplate.update("update person set taskstate=1,taskprogress=0,tasktarget="+taskrequest+" where uuid='"+taskreceiveruuid+"'");
					}
					// 任务未完成重新进入房间对话
					else
					{
						object.put("code", 1);
						object.put("message", "你刚进门，一拍脑门哎呀一声道:‘任务还没完成，还是赶紧出门快去行动吧");
					}
				}
				// 任务完成
				else
				{
					String type = "moneytongban";
					long number = 0;
					if(taskrewardmoney < 1000)
					{
						type = "moneytongban";
						number = taskrewardmoney;
					}
					else 
					{
						if(taskrewardmoney/1000 < 1000)
						{
							type = "moneybaiying";
							number = taskrewardmoney/1000;
						}
						else
						{
							type = "moneyhuangjin";
							number = taskrewardmoney/1000/1000;
						}
					}
					
					object.put("code", 2);
					object.put("message", "这些就都给你吧。你得到[经验:"+taskrewardexp+",钱物:"+(taskrewardmoney < 1000 ?taskrewardmoney+"个铜板":(taskrewardmoney/1000 < 1000? taskrewardmoney/1000+"两白银":taskrewardmoney/1000/1000+"两黄金"))+"]。\n"+taskovermessage);
					
					// 奖励加到总钱币中
					jdbcTemplate.update("update person set taskid=taskid+1,taskstate=0,taskprogress=0,tasktarget=0,exp=exp+"+taskrewardexp+","+"money=money+"+taskrewardmoney+" where uuid='"+taskreceiveruuid+"'");
				}
				
				return object.toString();
			}
			// 任务已经做过
			else if(taskID < playerTaskId)
			{
				JSONObject object = new JSONObject();
				object.put("code", 3);
				object.put("message", "多谢"+(gender == 1? "公子":"小姐")+"出手相助!来日定当重谢!");
				
				return object.toString();
			}
			// 任务跨界  任务异常
			else if(taskID > playerTaskId)
			{
				JSONObject object = new JSONObject();
				object.put("code", 4);
				object.put("message", "你能力还不够，还不能接受任务，先去四处转转吧。");
				
				return object.toString();
			}
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 
	 * @author 许仕永(xsy)
	 * des: 
	 * @param taskreceiveruuid
	 * @return
	 */
	public String getTaskState(String taskreceiveruuid)
	{
		// TODO Auto-generated method stub
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select taskid,taskprogress,dailytaskid,tasktarget from person where uuid='"+taskreceiveruuid+"'");
		
		if(rowSet.next())
		{
			int tasktarget = rowSet.getInt("tasktarget");
			int taskprogress = rowSet.getInt("taskprogress");
			int taskid = rowSet.getInt("taskid");
			
			SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select * from task where taskid="+taskid);
			if(sqlRowSet.next())
			{
				String tip = "";
				if(tasktarget == 0)
				{
					tip = "【请先去找到任务主，和ta交谈接到任务，然后再去执行任务，任务完成后回去找任务主领取奖励。】";
				}
				
				String name = sqlRowSet.getString("taskname");
				String taskdescript = sqlRowSet.getString("taskdescript");
				String taskmastername = sqlRowSet.getString("taskmastername");
				return tip+"\n任务:"+name+"\n\n任务主:"+taskmastername+"\n\n任务进度:"+taskprogress+"/"+tasktarget+"\n"+taskdescript;
			}
			else
			{
				return null;
			}
		}
		
		return null;
	}



	public String sign(String uuid, String sign)
	{
		try
		{
			if(sign == null || "".equals(sign.trim()))
			{
				JSONObject object = new JSONObject();
				try
				{
					object.put("code", 405);
					object.put("message", "不允许为空格!");
				}
				catch (JSONException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return object.toString();
			}
			
			jdbcTemplate.update("update person set sign='"+sign+"' where uuid='"+uuid+"'");
			JSONObject object = new JSONObject();
			object.put("code", 200);
			object.put("message", "设置签名成功");
			return object.toString();
		}
		catch (Exception e)
		{
			JSONObject object = new JSONObject();
			try
			{
				object.put("code", 405);
				object.put("message", "设置签名失败! error :"+e.toString());
			}
			catch (JSONException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return object.toString();
		}
	}



	public String join(String uuid, String countryid)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select grade,name,countryid from person where uuid='"+uuid+"'");
		if(rowSet.next())
		{
			String countryidtemp = rowSet.getString("countryid");
			int grade = rowSet.getInt("grade");
			
			
			// 判断战胜还是战败国
			int success = jdbcTemplate.queryForInt("select success from country where countryid='"+countryidtemp+"'");

			if(countryid != null && countryid.equals(countryidtemp))
			{
				JSONObject jsonObject = new JSONObject();
				try
				{
					jsonObject.put("message", "你本身就是本国国民!");
					jsonObject.put("code", 201);
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return jsonObject.toString();
			}
			
			// 战败国国民无资格叛城
			if(success == 2)
			{
				JSONObject jsonObject = new JSONObject();
				try
				{
					jsonObject.put("message", "亡国之奴，岂有资格加入本城!回去吧!");
					jsonObject.put("code", 201);
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return jsonObject.toString();
			}
			
			if(grade >= 15)
			{
				SqlRowSet countrySet = jdbcTemplate.queryForRowSet("select * from country where countryid='"+countryid+"'");
				if(countrySet.next())
				{
					String countryname = countrySet.getString("countryname");
					jdbcTemplate.update("update person set countryid='"+countryid+"',countryname='"+countryname+"' where uuid='"+uuid+"'");
					
					JSONObject jsonObject = new JSONObject();
					try
					{
						jsonObject.put("message", "你已成功加入【"+countryname+"】希望你能为本城做出贡献!");
						jsonObject.put("code", 200);
					}
					catch (JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return jsonObject.toString();
				}
				else
				{
					JSONObject jsonObject = new JSONObject();
					try
					{
						jsonObject.put("message", "城市数据有异!请勿使用第三方非法操作!");
						jsonObject.put("code", 405);
					}
					catch (JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return jsonObject.toString();
				}
			}
			else
			{
				JSONObject jsonObject = new JSONObject();
				try
				{
					jsonObject.put("message", "你的能力还不足以加入本城!回去继续锻炼吧!");
					jsonObject.put("code", 405);
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return jsonObject.toString();
			}
		}
		
		JSONObject jsonObject = new JSONObject();
		try
		{
			jsonObject.put("message", "用户信息异常，未查询到该账号信息。");
			jsonObject.put("code", 405);
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject.toString();
	}


	public String addFriend(String fuuid, String buuid)
	{
		SqlRowSet rowSet2 = jdbcTemplate.queryForRowSet("select name,type,uuid from person where uuid='"+fuuid+"'");
		
		if(fuuid != null && buuid != null)
		{
			if(fuuid.equals(buuid))
			{
				JSONObject jsonObject = new JSONObject();
				try
				{
					jsonObject.put("code", 207);
					jsonObject.put("message", "你不能和自己做朋友!");
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return jsonObject.toString();
			}
		}
		
		if(rowSet2.next())
		{
			int type = rowSet2.getInt("type");
			String fname = rowSet2.getString("name");
			
			if(type == 2 || type==3)
			{
					JSONObject jsonObject = new JSONObject();
					try
					{
						jsonObject.put("code", 208);
						jsonObject.put("message", "对方不屑与你为伍!");
					}
					catch (JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return jsonObject.toString();
			}
			
			SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from friend where fuuid='"+fuuid+"' and buuid='"+buuid+"'");
			if(!rowSet.next())
			{
				jdbcTemplate.update("insert into friend(fuuid,fname,state,buuid) values('"+fuuid+"','"+fname+"','"+"1"+"','"+buuid+"'"+")");
				
				JSONObject jsonObject = new JSONObject();
				try
				{
					jsonObject.put("code", 200);
					jsonObject.put("message", "你已成功添加["+fname+"]为好友!");
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return jsonObject.toString();
			}
			else
			{
				JSONObject jsonObject = new JSONObject();
				try
				{
					jsonObject.put("code", 201);
					jsonObject.put("message", "对方已经是你的好友，不能重复添加");
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return jsonObject.toString();
			}
		}
		else
		{
			JSONObject jsonObject = new JSONObject();
			try
			{
				jsonObject.put("code", 201);
				jsonObject.put("message", "没有找到这个人！");
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jsonObject.toString();
		}
	}

	public List<Map<String, Object>> getFriendList(String uuid)
	{
		return jdbcTemplate.queryForList("select * from friend where buuid='"+uuid+"'");
	}

	
	public String deleteFriend(String fuuid, String buuid)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from friend where fuuid='"+fuuid+"' and buuid='"+buuid+"'");
		if(rowSet.next())
		{
			jdbcTemplate.update("delete from friend where fuuid='"+fuuid+"' and buuid='"+buuid+"'");
			
			JSONObject jsonObject = new JSONObject();
			try
			{
				jsonObject.put("code", 200);
				jsonObject.put("message", "删除成功");
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jsonObject.toString();
		}
		else
		{
			JSONObject jsonObject = new JSONObject();
			try
			{
				jsonObject.put("code", 201);
				jsonObject.put("message", "没有找到这个人！");
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jsonObject.toString();
		}
	}

	public String goback(String uuid)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select countryid,positiongrade,last_areaid,money,areaid from person where uuid='"+uuid+"'");
		if(rowSet.next())
		{
			int positiongrade = rowSet.getInt("positiongrade");
			int areaid = rowSet.getInt("areaid");
			long money = rowSet.getLong("money");
			int last_areaid = rowSet.getInt("last_areaid");
			String countryid = rowSet.getString("countryid");
			
			try
			{
				if(areaid == 10000)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code", 201);
					jsonObject.put("message", "试图越狱，小心被打断腿!");
					return jsonObject.toString();
				}
				
				if(areaid==getKeZhanId(countryid))
				{
					if(money < 10000)
					{
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("code", 200);
						jsonObject.put("message", "你身上钱不够，无法传送回记录点(需要费用10两白银)...");
						return jsonObject.toString();
					}
					
					if(last_areaid >= 10240001)
					{
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("code", 200);
						jsonObject.put("message", "你想要传送的地方十分凶险,给再多钱都不去！");
						return jsonObject.toString();
					}
					
					long m = 10000 - positiongrade * 200;
					
					jdbcTemplate.update("update person set money=money-"+m+",areaid=last_areaid  where uuid='"+uuid+"'");
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code", 200);
					jsonObject.put("message", "正在传送回记录点(花费"+NumberConverUtil.getNameByMoney(m)+")...");
					return jsonObject.toString();
				}
				else
				{
					jdbcTemplate.update("update person set last_areaid="+areaid+" ,areaid="+getKeZhanId(countryid) +" where uuid='"+uuid+"'");
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code", 200);
					jsonObject.put("message", "正在传送回客栈中...");
					return jsonObject.toString();
				}
				
				//System.out.println("回城--->"+"update person set areaid="+getKeZhanId(countryid) +" where uuid='"+uuid+"'");
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "查无此人!");
				return jsonObject.toString();
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	
	
	public String diaoyu(String uuid,String type)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select money,grade,name,diaoyucount from person where uuid='"+uuid+"' and type=1");
		if(rowSet.next())
		{
			long money = rowSet.getLong("money");
			int diaoyucount = rowSet.getInt("diaoyucount");
			int grade = rowSet.getInt("grade");
			
			if(grade < 5)
			{
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "钓鱼最低等级限制 >= 5");
				return jsonObject.toString();
			}
			
			if(money < 100)
			{
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "你身上没有几个铜板了,你还是一边玩泥巴去吧!");
				return jsonObject.toString();
			}
			else if(diaoyucount < 1)
			{
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "你今天的钓鱼次数已经用完，明天再来吧。");
				return jsonObject.toString();
			}
			else
			{
				// 入座
				if("1".equals(type))
				{
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("code", 200);
					jsonObject.put("message", getMessage(uuid,1,diaoyucount,grade));
					return jsonObject.toString();
				}
				// 收杆
				else if("2".equals(type))
				{
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("code", 200);
					String result = getMessage(uuid,2,diaoyucount,grade);
					jsonObject.put("message", "【剩余"+(diaoyucount-1)+"次】"+(result == null ? "你困乏之下，打了个小盹，睡得好香，还流口水了，也不知是梦到鸡腿还是鸡翅。":result));
					return jsonObject.toString();
				}
				else
				// 刷新
				{
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("code", 200);
					jsonObject.put("message", "【剩余"+diaoyucount+"次】"+getMessage(uuid,3,diaoyucount,grade));
					return jsonObject.toString();
				}
			}
		}
		else
		{
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "状态异常!");
			return jsonObject.toString();
		}
	}

	private String getMessage(String uuid,int type,int counttime,int grade)
	{
		if(random == null)
			random = new Random();
		
		String mess = null;
		
		if(type == 1)
		{
			// 缴费
			jdbcTemplate.update("update person set money=money-"+counttime*10+" where uuid='"+uuid+"'");
			
			switch (random.nextInt(10))
			{
				case 0:mess = "你怡然自得，沐浴着和风暖阳。";break;
				case 1:mess = "你伸了个懒腰，差点不小心把鱼竿给丢下荷塘了，还好手速和反应快。";break;
				case 2:mess = "你气定神闲，盘腿坐了下来，一个优雅的动作，把鱼钩抛出,但是动作太大,把鱼都惊跑了,周围的其他人看起来好像很愤怒,把你围殴胖揍了一顿。";break;
				case 3:mess = "你信心十足,坚信今天会钓到些好东东。";break;
				case 4:mess = "你今天似乎很得意的样子，但是解府副总管好像看你不顺眼，对你吼到'得意什么!快给我坐下',你心情似乎不好了。";break;
				case 5:mess = "小丫么小二郎,背着书包上学堂,不怕风和雨...你十分开心地哼着小曲。";break;
				case 6:mess = "你一派大宗师的气质，缓缓盘腿坐下，上饵甩钩一气呵成。";break;
				case 7:mess = "你轻轻松松坐下，期待着可以钓到什么好东西。";break;
				case 8:mess = "你一坐下不久就开始呼呼呼的瞌睡起来。";break;
				case 9:mess = "鱼呀鱼儿呦，快呀么快上钩，你坐下不久就已经沉不住气了，跟疯了似的自娱自乐。";break;
				
				default:
					mess = "你怡然自得，沐浴着和风暖阳。";
					break;
			}
		}
		else if(type == 2)
		{
			// 缴费
			jdbcTemplate.update("update person set money=money-30,diaoyucount=diaoyucount-1 where uuid='"+uuid+"'");
			// 3 6 9 12 15
			int ran = random.nextInt(10);
			// 钓到铜板
			if(ran == 1)
			{
				switch (random.nextInt(10))
				{
					case 0:
						int m = (200+random.nextInt(100));
						mess = "你正在沉思中，忽然感觉手里鱼竿变重了，鱼竿一收，发现居然鱼钩上钓着一串铜板，你数了数一共【"+m+"个铜板】";
						jdbcTemplate.update("update person set money=money+"+m+" where uuid='"+uuid+"'");
						break;
					case 1:
						int m2 = (200+random.nextInt(100));
						mess = "你正聚精会神，忽然感觉手里鱼竿变重了，鱼竿一收，钩上似乎钓着一袋东西，你打开一看是铜板，数了数一共【"+m2+"个铜板】";
						jdbcTemplate.update("update person set money=money+"+m2+" where uuid='"+uuid+"'");
						break;
					case 2:
						mess = "你正聚精会神，忽然感觉手里鱼竿变重了，鱼竿一收，一条鱼，可是一摸鱼肚子，一块东西硬硬，用刀切开鱼肚，里面竟然有【150】个铜板,你高兴的手舞足蹈。";
						jdbcTemplate.update("update person set money=money+150 where uuid='"+uuid+"'");
						break;
					case 3:
						int m3 = (250+random.nextInt(100));
						mess = "你正在沉思中，忽然感觉手里鱼竿变重了，鱼竿一收，发现居然鱼钩上钓着一串铜板，你数了数一共【"+m3	+"个铜板】";
						jdbcTemplate.update("update person set money=money+"+m3+" where uuid='"+uuid+"'");
						break;
					case 4:
						mess = "你感觉鱼钩吃力，心知必有大鱼上钩了，奋力把鱼钩收拉回来，竟然是一只硕大的鲶鱼，鱼嘴里还衔着一颗珍珠,解府副总管见状过来提出以【800】铜板收购,于是你兴高采烈的卖掉了";
						jdbcTemplate.update("update person set money=money+800 where uuid='"+uuid+"'");
						break;

					default:
						int m4 = (100+random.nextInt(25));
						mess = "只听得一声咕咚，你心思大鱼上钩了，连忙提钩，是一条肥美的鲤鱼,你把鱼卖给了解府换取了【"+m4+"个铜板】";
						jdbcTemplate.update("update person set money=money+"+m4+" where uuid='"+uuid+"'");
						break;
				}
				
			}
			else if(ran == 2)
			{
				if(random.nextInt(80) == 1)
				{
					if(random.nextInt(3) != 0)
					{
						int[] baoshi = new int[]{334};
						int r2 = random.nextInt(baoshi.length);
						String gid = UUID.randomUUID().toString().replaceAll("-", "");
						String name="";
						if(r2 == 0)
						{
							name = "续灵宝石";
							String sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '续灵宝石', '"+uuid+"', '300', '1200', '3', '0', '0', '0', '0', '8', '0', '可以给装备增续灵力耐久的宝石。', "+22+", '334', '2', '1', '0', '1',UNIX_TIMESTAMP());";
							jdbcTemplate.update(sql);
							mess = "你感觉鱼竿被往下拉了一下，凭借多年经验判断大鱼上钩了，猛地一收杆，砰的一声确实一块石头刚好砸你脑门上了，你气的捡起石头就正要扔却发现原来是一块【"+name+"】，赶快放下鱼竿去钓鱼池看看吧。";
						}
					}
					else
					{
						mess = "你感觉鱼竿被往下拉了一下，凭借多年经验判断大鱼上钩了，猛地一收杆，砰的一声确实一块石头刚好砸你脑门上了，你气的捡起石头就往湖里扔了回去！";
					}
				}
				else if(random.nextInt(15)==1)
				{
					SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select name,type,descript,gtype,price from goods_system where gtype=330");
					if(rowSet.next())
					{
						String descript = rowSet.getString("descript");
						String name = rowSet.getString("name");
						int gtype = rowSet.getInt("gtype");
						int types = rowSet.getInt("type");
						long price = rowSet.getInt("price");
						String gid = UUID.randomUUID().toString().replaceAll("-", "");
						String sql = "INSERT INTO goods (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`,`unlawful_time`) VALUES ('"+gid+"', '"+name+"', '"+uuid+"', '"+price+"', '"+Constant.life+"', '3', '0', '0', '0', '0', '"+types+"', '3', '"+descript+"', "+22+", '"+gtype+"',UNIX_TIMESTAMP());";
						jdbcTemplate.update(sql);
						mess = "你感觉大鱼上钩，猛地一提一拽，居然跟上来一块美玉,一块【"+name+"】掉落在钓鱼池边，赶快去找找吧";
					}
				}
			}
			else
			{
				switch (random.nextInt(6))
				{
					case 0:mess = "你正在沉思中，忽然感觉手里鱼竿变重了，鱼竿一收，发现居然鱼钩上钓着一串铜板，你再仔细一看，呵呵，居然还是假铜板。";break;
					case 1:mess = "你正聚精会神，忽然感觉手里鱼竿变重了，鱼竿一收，钩上似乎钓着一袋东西，你打开一看只是一袋石子";break;
					case 2:mess = "你正聚精会神，忽然感觉手里鱼竿变重了，鱼竿一收，一条鱼，可是一摸鱼肚子，一块东西硬硬，用刀切开鱼肚，里面竟然有一块破石头。";break;
					case 3:mess = "你正在沉思中，忽然感觉手里鱼竿变重了，鱼竿一收，可惜只是一个破草帽子，于是你破口大骂。";break;
					case 4:mess = "你感觉鱼钩吃力，心里念叨必有大鱼上钩了，奋力把鱼钩收拉回来，结果只是一只小小的鱼儿，你一发善心给放生了。";break;
					case 5:mess = "只听得一声咕咚，你心思大鱼上钩了,费了九牛二虎之力把鱼钩收上来，结果，居然是一段没用的烂木材";
				}
				
				long exp = (long) ((grade*1.2));
				jdbcTemplate.update("update person set exp=exp+"+exp +" where uuid='"+uuid+"'");
				mess += "\n\n"+"你额外获得经验值:【"+exp+"】。";
			}
//			// 钓到装备
//			else if(ran == 99 || ran == 88 || ran == 77)
//			{
//				
//			}
		}
		else 
		{
			switch (random.nextInt(12))
			{
				case 0:mess = "水面似乎风平浪静的，所以你还是决定沉下心等待。";break;
				case 1:mess = "水面上起了几圈波纹，但是以以往的经验来看似乎不像是大鱼上钩";break;
				case 2:mess = "你坐着坐着居然犯困瞌睡了，才发现鱼饵已经被鱼儿偷吃的干干净净的。";break;
				case 3:mess = "天大地大，何处是我家...你感慨自己的一生，不禁唱了起来。";break;
				case 4:mess = "你右手托着腮帮子，左手剔着牙，无聊地等待着大鱼上钩。";break;
				case 5:mess = "你百无聊赖之际，对面走过来一位姑娘，你喜足颜开，假装气定神闲地垂钓。";break;
				case 6:mess = "水面荡起了一圈圈水纹，这个迹象似乎有鱼吃饵了。";break;
				case 7:mess = "你对面树上一只鸟儿叽叽喳喳地一直叫，你不耐心烦，扔下鱼竿去捡石子丢向树上。";break;
				case 8:mess = "你坐了一会便觉得心神不定，叹息一声。";break;
				case 9:mess = "你满身大汗，一阵风拂过，你顿感一丝清爽凉意。";break;
				case 10:mess = "你聚精会神盯着荷塘水面，不料一只乌龟爬上了岸边，在你脚下盘旋。";break;
				case 11:mess = "你心不在焉的，好像并没有心思垂钓的样子。";break;

				default:
					break;
			}
		}
		
		
		return mess;
	}
	
	public String getNewTask(String uuid)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select dailytaskid,daliytaskstarttime,dailytaskcount,dailytaskruntimecount from person where uuid='"+uuid+"'");
		
		if(rowSet.next())
		{
			// 判断任务id
			// 判断任务时间
			// 判断任务执行的进度
			// 判断任务执行次数
			
			String dailytaskid = rowSet.getString("dailytaskid");
			// 任务开始时间
			long daliytaskstarttime = rowSet.getLong("daliytaskstarttime");
			// 任务完成进度
			int dailytaskcount = rowSet.getInt("dailytaskcount");
			// 连续任务次数
			int dailytaskruntimecount = rowSet.getInt("dailytaskruntimecount");
			
			if(dailytaskid != null && !"".equals(dailytaskid.trim()))
			{
				SqlRowSet taskSet = jdbcTemplate.queryForRowSet("select *,UNIX_TIMESTAMP() as nowtime from daily_task where taskid='"+dailytaskid+"'");
				if(taskSet.next())
				{
					// 当前时间
					long nowtime = taskSet.getLong("nowtime");
					// 任务时限
					long tasktime= taskSet.getLong("tasktime");
					// 任务名
					String taskname = taskSet.getString("taskname");
					// 任务描述
					String taskdescript = taskSet.getString("taskdescript");
					// 任务物品
					String taskthings = taskSet.getString("taskthings");
					// 任务要求的进度
					int taskrequest = taskSet.getInt("taskrequest");
					
					if((nowtime - daliytaskstarttime) > tasktime)
					{
						String respone = "";
						SqlRowSet nextTask = jdbcTemplate.queryForRowSet("select taskdescript,taskid from daily_task where taskid!='"+dailytaskid+"' order by rand()");
						if(nextTask.next())
						{
							respone += "<br/><br/>麒麟星：你的下一任务是-->"+nextTask.getString("taskdescript");
							jdbcTemplate.update("update person set dailytaskid = '"+nextTask.getString("taskid")+"',daliytaskstarttime=UNIX_TIMESTAMP(),dailytaskruntimecount=0,dailytaskcount=0 where uuid='"+uuid+"'");
						}
						
						net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
						jsonObject.put("code", 201);
						jsonObject.put("message", "<font color='red'>真是令人失望，更令本城蒙羞，你的任务【"+taskname+"】超时失败！</font><br/><br/>"+respone);
						return jsonObject.toString();
					}
					else
					{
						String respone = "";
							respone = "<font color='red'>当前任务：【"+taskname+"】</font>";
							respone += "<br/><br/>任务完成度：【"+dailytaskcount+"/"+taskrequest+" "+taskthings+"】";
							respone += "<br/><br/>任务剩余时间:【"+(tasktime - (nowtime - daliytaskstarttime))/60+"分"+(tasktime - (nowtime - daliytaskstarttime))%60+"秒】";
							respone += "<br/><br/>连续任务次数:【"+dailytaskruntimecount+"】";
							respone += "<br/><br/>任务基础奖励:【经验-"+dailytaskruntimecount*5+",银钱-???"+"】";
							respone += "<br/><br/>任务描述:【"+taskdescript+"】";
							respone += "<br/><br/><font color='blue'>任务完成后请速回报【麒麟星】</font>";
						
						return respone;
					}
				}
				else
				{
					return "任务无效！";
				}
			}
		}
		else
		{
			return "未查询到用户信息。";
		}
		return null;
	}

	/**  获取随机任务的状态和描述*/
	public String getDailyTaskState(String taskreceiveruuid,int type)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select grade,dailytaskid,daliytaskstarttime,dailytaskcount,dailytaskruntimecount from person where uuid='"+taskreceiveruuid+"'");
		
		if(rowSet.next())
		{
			// 判断任务id
			// 判断任务时间
			// 判断任务执行的进度
			// 判断任务执行次数
			
			String dailytaskid = rowSet.getString("dailytaskid");
			// 任务开始时间
			long daliytaskstarttime = rowSet.getLong("daliytaskstarttime");
			// 任务完成进度
			int dailytaskcount = rowSet.getInt("dailytaskcount");
			// 连续任务次数
			int dailytaskruntimecount = rowSet.getInt("dailytaskruntimecount");
			// 人物等级
			int grade = rowSet.getInt("grade");
			
			if(dailytaskid != null && !"".equals(dailytaskid.trim()))
			{
				SqlRowSet taskSet = jdbcTemplate.queryForRowSet("select *,UNIX_TIMESTAMP() as nowtime from daily_task where taskid='"+dailytaskid+"'");
				if(taskSet.next())
				{
					// 当前时间
					long nowtime = taskSet.getLong("nowtime");
					// 任务时限
					long tasktime= taskSet.getLong("tasktime");
					// 任务名
					String taskname = taskSet.getString("taskname");
					// 任务描述
					String taskdescript = taskSet.getString("taskdescript");
					// 任务物品
					String taskthings = taskSet.getString("taskthings");
					// 任务要求的进度
					int taskrequest = taskSet.getInt("taskrequest");
					
					if((nowtime - daliytaskstarttime) > tasktime)
					{
						String respone = "";
						SqlRowSet nextTask = jdbcTemplate.queryForRowSet("select taskdescript,taskid from daily_task where taskid!='"+dailytaskid+"' order by rand()");
						if(nextTask.next())
						{
							jdbcTemplate.update("update person set dailytaskid = '"+nextTask.getString("taskid")+"',daliytaskstarttime=UNIX_TIMESTAMP(),dailytaskruntimecount=0,dailytaskcount=0 where uuid='"+taskreceiveruuid+"'");
							respone += "<br/><br/>麒麟星：你的下一任务详情如下-->"+nextTask.getString("taskdescript");
						}
						
						net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
						jsonObject.put("code", 201);
						jsonObject.put("message", "<font color='red'>真是令人失望，更令本城蒙羞，你的任务【"+taskname+"】超时失败！</font><br/><br/>"+respone);
						return jsonObject.toString();
					}
					else
					{
						String respone = "";
//						String taskname = taskSet.getString("taskname");
						
						// 满足了时限要求和任务指数要求
						if(dailytaskcount >= taskrequest)
						{
							if(type == 2)
							{
								SqlRowSet nextTask = jdbcTemplate.queryForRowSet("select taskdescript,taskid from daily_task where taskid!='"+dailytaskid+"' order by rand() LIMIT 0,1");
								if(nextTask.next())
								{
									// reward by dailytaskruntimecount
									// 奖励的最高限额是400
									if(dailytaskruntimecount > 400)
										dailytaskruntimecount = 400;
									int rewardexp = dailytaskruntimecount + grade*2;
									int rewardmoney = dailytaskruntimecount*(random.nextInt(25)+5)+grade*3;
									respone += "<font color='red'>你获得奖励【经验:"+(rewardexp)+"，银钱:"+getRewardName(rewardmoney)+"】</font>";
									respone += "<br/><br/>麒麟星：你的下一任务是-->"+nextTask.getString("taskdescript");
									jdbcTemplate.update("update person set money=money+"+rewardmoney+",exp=exp+"+rewardexp+",dailytaskid = '"+nextTask.getString("taskid")+"',daliytaskstarttime=UNIX_TIMESTAMP(),dailytaskruntimecount=dailytaskruntimecount+1,dailytaskcount=0 where uuid='"+taskreceiveruuid+"'");
									
									
									//连续任务超过100次之后，每次都有1/20概率获得一个碎片
									if(dailytaskruntimecount > 150)
									{
										if(random.nextInt(20) == 0)
										{
											// 检查行囊的可容数量    and gtype=(select gtype from goods where gid='"+gid+"'则为特殊的物品诸如材料宝石等
											SqlRowSet countSet = jdbcTemplate.queryForRowSet("select COUNT(name) as count from goods where masterid='"+taskreceiveruuid+"' and state=2 and gtype<281");
											if(countSet.next())
											{
												int count = countSet.getInt("count");
												
												if(count < 20)
												{
													SqlRowSet randToGetSP = jdbcTemplate.queryForRowSet("select name,type,descript,gtype,price from goods_system where gtype=330");
													if(randToGetSP.next())
													{
														String descript = randToGetSP.getString("descript");
														String name = randToGetSP.getString("name");
														int gtype = randToGetSP.getInt("gtype");
														int typeSP = randToGetSP.getInt("type");
														long price = randToGetSP.getInt("price");
														String gid = UUID.randomUUID().toString().replaceAll("-", "");
//														String sql = "INSERT INTO goods (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`) VALUES ('"+gid+"', '"+name+"', '"+successeruuid+"', '"+price+"', '"+Constant.life+"', '3', '0', '0', '0', '0', '"+type+"', '3', '"+descript+"', "+areaid+", '"+gtype+"');";
														String sql = "INSERT INTO goods (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`) VALUES ('"+gid+"', '"+name+"', '"+taskreceiveruuid+"', '"+price+"', '"+Constant.life+"', '2', '0', '0', '0', '0', '"+typeSP+"', '3', '"+descript+"', "+0+", '"+gtype+"');";
														jdbcTemplate.update(sql);
														respone+= "<br/><font color=red>"+" 由于你任务完成的十分出色，麒麟星还赏赐给你一个["+name+"]</font><br/>";
													}
												}
												else
												{
													respone+= "<br/><font color=red>"+" 由于你的行囊已满，无法接受麒麟星的额外赏赐。</font><br/>";
												}
											}
										}
									}
									else if(dailytaskruntimecount > 350)
									{
										if(random.nextInt(30) == 0)
										{
											int count = jdbcTemplate.queryForInt("select COUNT(name) as count from goods where masterid='"+taskreceiveruuid+"' and state=2 and gtype<281");
											if(count < 20)
											{
												int[] baoshi = new int[]{334};
												int r2 = random.nextInt(baoshi.length);
												String gid = UUID.randomUUID().toString().replaceAll("-", "");
												String name="";
												if(r2 == 0)
												{
													name = "续灵宝石";
													String sql = "INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `count`, `level`, `visibility`,`unlawful_time`) VALUES ('"+gid+"', '续灵宝石', '"+taskreceiveruuid+"', '300', '1200', '2', '0', '0', '0', '0', '8', '0', '可以给装备增续灵力耐久的宝石。', "+0+", '334', '2', '1', '0', '1',UNIX_TIMESTAMP());";
													jdbcTemplate.update(sql);
													respone+= "<br/><font color=red>"+" 麒麟星很满意你的行事手段，赏赐给你一个["+name+"]</font><br/>";
												}
											}
											else
											{
												respone+= "<br/><font color=red>"+" 由于你的行囊已满，无法接受麒麟星的额外赏赐。</font><br/>";
											}
										}
									}
								  }
							}
							else
							{
								// 1.反馈已完成任务
								// 2.报下一随机任务
								String taskovermessage = taskSet.getString("taskovermessage");
								respone = taskovermessage+"<br/><br/><font color='red'>你已完成任务：【"+taskname+"】 请速回不动城回报【麒麟星】，领取奖励。</font>";
							}
						}
						else
						{
							// 奖励的最高限额是400
							if(dailytaskruntimecount > 400)
								dailytaskruntimecount = 400;
							respone = "<font color='red'>当前任务：【"+taskname+"】</font>";
							respone += "<br/><br/>任务完成度：【"+dailytaskcount+"/"+taskrequest+" "+taskthings+"】";
							respone += "<br/><br/>任务剩余时间:【"+(tasktime - (nowtime - daliytaskstarttime))/60+"分"+(tasktime - (nowtime - daliytaskstarttime))%60+"秒】";
							respone += "<br/><br/>连续任务次数:【"+dailytaskruntimecount+"】";
							respone += "<br/><br/>任务基础奖励:【经验->"+(dailytaskruntimecount+grade*2)+",银钱->次数x(25内随机数+5)+等级x3"+"】";
							respone += "<br/><br/>任务描述:【"+taskdescript+"】";
							respone += "<br/><br/><font color='blue'>任务完成后请速回报【麒麟星】</font>";
						}
						
						net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
						jsonObject.put("code", 200);
						jsonObject.put("message", respone);
						return jsonObject.toString();
					}
				}
				else
				{
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("code", 201);
					jsonObject.put("message", "任务无效！");
					return jsonObject.toString();
				}
			}
			else
			{
				if(type != 2)
				{
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("code", 201);
					jsonObject.put("message", "你可以先去魔吞不动城找麒麟星接任务,只要你办得好，奖金不会少的。");
					return jsonObject.toString();
				}
				else
				{
					// type 是已经找到麒麟星想接任务。
					jdbcTemplate.update("update person set dailytaskid = '0001',daliytaskstarttime=UNIX_TIMESTAMP(),dailytaskruntimecount=0,dailytaskcount=0 where uuid='"+taskreceiveruuid+"'");
					return getDailyTaskState(taskreceiveruuid, type);
				}
			}
		}
		else
		{
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "未查询到用户信息。");
			return jsonObject.toString();
		}
	}
	
	public String getRewardName(int tongban)
	{
		int yz = tongban / 1000;
		int tb = tongban % 1000;
		return (yz > 0 ? yz+"两银子":"")+(tb>0?tb+"个铜板":"");
	}

	public String changeTask(String uuid)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select dailytaskruntimecount,money from person where uuid='"+uuid+"'");
//		long money = jdbcTemplate.queryForLong("select money from person where uuid='"+uuid+"'");
		if(rowSet.next())
		{
			long money = rowSet.getLong("money");
			// 连续任务次数
			int dailytaskruntimecount = rowSet.getInt("dailytaskruntimecount");
			int out = dailytaskruntimecount*100;
			if(money > out)
			{
				SqlRowSet nextTask = jdbcTemplate.queryForRowSet("select taskdescript,taskid,taskname from daily_task order by rand() LIMIT 0,1");
				String taskname = "";
				if(nextTask.next())
				{
					// reward by dailytaskruntimecount
	//				long rewardmoney = dailytaskruntimecount * 100;
	//				respone += "<br/><br/>麒麟星：你的下一任务是-->"+nextTask.getString("taskdescript");
					taskname = nextTask.getString("taskname");
					jdbcTemplate.update("update person set dailytaskid = '"+nextTask.getString("taskid")+"',daliytaskstarttime=UNIX_TIMESTAMP(),dailytaskcount=0 where uuid='"+uuid+"'");
				}
				
				jdbcTemplate.update("update person set money=money - "+out+" where uuid='"+uuid+"'");
	
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 200);
				jsonObject.put("message", "你的任务已经成功更换为-->【"+taskname+"】,总花费【"+getRewardName(out)+"】。");
				jsonObject.put("message2", getNewTask(uuid));
				return jsonObject.toString();
			}
			else
			{
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "你没有足够的银两可以用来更换任务。");
				return jsonObject.toString();
			}
		}
		else
		{
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "未查询到您的用户信息。");
			return jsonObject.toString();
		}
	}
}