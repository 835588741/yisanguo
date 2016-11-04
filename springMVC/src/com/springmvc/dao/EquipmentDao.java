package com.springmvc.dao;

import java.util.Random;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.springmvc.util.Constant;

@Component
public class EquipmentDao
{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	private String getNumToString(int num)
	{
		switch (num)
		{
			case 1:return "一";
			case 2:return "二";
			case 3:return "三";
			case 4:return "四";
			case 5:return "五";
			case 6:return "六";
			case 7:return "七";
			case 8:return "八";
			case 9:return "九";
			default:
				return "无";
		}
	}

	
	public String checkFail(int level,int type,int ggrade,String name,String uuid,String gid)
	{
		Random random=new Random();
		// 执行失败概率 判断
		int n = random.nextInt(100);
		// 每一级成功率降低10%
		if(n > (100 - level*10))
		{
			// 查询材料是否满足
			/** 玄铁 */
			int queryCondition1 = 291+level;
			/** 琥珀 */
			int queryCondition2 = 311+level;
			/** 白虎皮 */
			int queryCondition3 = 301+level;
			/** 天蚕丝 */
			int queryCondition4 = 321+level;
			/** 金丝楠 */
			int queryCondition5 = 281+level;	
			
			int numCondition = level*2+1;
			
			// 提取原来的名字
			if(name.contains("+"))
			{
				name = name.substring(0, name.length()-2);
			}
			// 之前提升装备的总属性
			int add = (ggrade/2)*level;
			
			if(type==101 || type == 102 || type == 103)
			{
				// 1.删除升级所用材料  扣除升级所需银子
				jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition1+") and state=2 limit "+numCondition);
				jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition2+") and state=2 limit "+numCondition);
				
				// 2.清除升级之前的等级和属性
				jdbcTemplate.update("update goods set name='"+name+"', addattack=addattack-"+add+" where masterid='"+uuid+"' and gid='"+gid+"'");
			}
			// 盔甲
			else if(type==4)
			{
				// 盔甲的每级加防多3点，所以需要特殊再计算
				add = (ggrade/2+3)*level;
				// 1.删除升级所用材料  扣除升级所需银子
				jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition1+") and state=2 limit "+numCondition);
				jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition3+") and state=2 limit "+numCondition);
				jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition5+") and state=2 limit "+numCondition);
				
				// 2.清除升级之前的等级和属性
				jdbcTemplate.update("update goods set name='"+name+"', adddefence=adddefence-"+add+" where masterid='"+uuid+"' and gid='"+gid+"'");
			}
			// 腰带
			else if(type == 5)
			{
				// 1.删除升级所用材料  扣除升级所需银子
				jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition3+") and state=2 limit "+numCondition);
				jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition4+") and state=2 limit "+numCondition);
				jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition5+") and state=2 limit "+numCondition);
				
				// 2.清除升级之前的等级和属性
				jdbcTemplate.update("update goods set name='"+name+"',addhp=addhp-"+add+" where masterid='"+uuid+"' and gid='"+gid+"'");
			}
			// 头盔
			else if(type ==2)
			{
				// 1.删除升级所用材料  扣除升级所需银子
				jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition1+") and state=2 limit "+numCondition);
				jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition5+") and state=2 limit "+numCondition);
				
				// 2.清除升级之前的等级和属性
				jdbcTemplate.update("update goods set name='"+name+"', adddefence=adddefence-"+add+" where masterid='"+uuid+"' and gid='"+gid+"'");
			}
			// 披风
			else if(type == 3)
			{
				// 1.删除升级所用材料  扣除升级所需银子
				jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition3+") and state=2 limit "+numCondition);
				jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition4+") and state=2 limit "+numCondition);
				
				// 2.清除升级之前的等级和属性
				jdbcTemplate.update("update goods set name='"+name+"', adddefence=adddefence-"+add+" where masterid='"+uuid+"' and gid='"+gid+"'");
			}
			// 鞋子
			else if(type == 6)
			{
				// 1.删除升级所用材料  扣除升级所需银子
				jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition5+") and state=2 limit "+numCondition);
				jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition4+") and state=2 limit "+numCondition);
				
				// 2.清除升级之前的等级和属性
				jdbcTemplate.update("update goods set name='"+name+"', adddefence=adddefence-"+add+" where masterid='"+uuid+"' and gid='"+gid+"'");
			}
			// 扣除钱
			jdbcTemplate.update("update person set money=money-"+(Math.pow(level+1, 2) * 1000)+" where uuid='"+uuid+"'");
			
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 202);
			jsonObject.put("message", "很遗憾，装备升级失败！");
			return jsonObject.toString();
		}
		return null;
	}
	
	public String upgradeequip(String uuid, String gid)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select (select money from person where uuid='"+uuid+"') as money,level,adddefence,type,name,addattack,addhp,ggrade from goods where masterid='"+uuid+"' and gid='"+gid+"'");
		if(rowSet.next())
		{
			long money = rowSet.getLong("money"); 
			int level = rowSet.getInt("level");
			int type  = rowSet.getInt("type");
			String name = rowSet.getString("name");
			int ggrade = rowSet.getInt("ggrade");
			// 数量限制
			int numCondition = level*2+1;
			
			/** 玄铁 */
			int queryCondition1 = 291+level;
			/** 琥珀 */
				int queryCondition2 = 311+level;
			/** 白虎皮 */
			int queryCondition3 = 301+level;
			/** 天蚕丝 */
			int queryCondition4 = 321+level;
			/** 金丝楠 */
			int queryCondition5 = 281+level;	
			
			if(money < (Math.pow(level+1, 2)) * 1000)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "未能进行升级操作"
						+ "<br/>【升级需要:白银"+(Math.pow(level+1, 2))+"两.】"
						);
				return jsonObject.toString();
			}
			
			if(type == 101 || type == 102 || type == 103)
			{
				int addattack = rowSet.getInt("addattack");
				
				SqlRowSet querySet = jdbcTemplate.queryForRowSet("select *,count(1) from goods where masterid='"+uuid+"' and state=2 and (gtype="+queryCondition1+" or gtype="+queryCondition2+")  GROUP BY gtype");
				int result = 0;
				while(querySet.next())
				{
					int count = querySet.getInt("count(1)");
					// 条件不满足 材料不足
					if(count < numCondition)
					{
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("code", 201);
						jsonObject.put("message", "<font color='red'>材料不足升级失败! 升级所需要的条件如下:</font>"
								+ "<br/><br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级玄铁."
								+ "<br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级琥珀."
								+ "<br/>需要:白银"+(Math.pow(level+1, 2))+"两."
								);
						return jsonObject.toString();
					}
					
					result ++;
				}
				
				if(result >= 2)
				{
					String res = checkFail(level, type, ggrade, name, uuid, gid);
					if(res != null)
						return res;
					
					if(name.contains("+"))
					{
						name = name.substring(0, name.length()-2);
					}
					name = name+"+"+(level+1);
							
					// 经过上面while循环的判断处理，到此处已经满足升级条件
					// 1.删除升级所用材料  扣除升级所需银子
					jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition1+") and state=2 limit "+numCondition);
					jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition2+") and state=2 limit "+numCondition);
					jdbcTemplate.update("update person set money=money-"+(Math.pow(level+1, 2) * 1000)+" where uuid='"+uuid+"'");
					// 2.提升装备属性
					int add = (ggrade/2);
					jdbcTemplate.update("update goods set level=level+1,name='"+name+"',addattack=addattack+"+add+" where masterid='"+uuid+"' and gid='"+gid+"'");
					// 3.更改装备名字
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code", 200);
					jsonObject.put("message", "<font color='red'>恭喜你成功升级装备到 "+(level+1)+"级！</font><br/><br/>攻击属性(+"+add+"):"+addattack+"-->"+(addattack+add));
					return jsonObject.toString();
				}
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "<font color='red'>条件未满足！升级失败！升级所需要的条件如下:</font>"
						+ "<br/><br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级玄铁."
						+ "<br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级琥珀."
						+ "<br/>需要:白银"+(Math.pow(level+1, 2))+"两."
						);
				return jsonObject.toString();
			}
			else if(type == 4)
			{
				int adddefence = rowSet.getInt("adddefence");
				
				SqlRowSet querySet = jdbcTemplate.queryForRowSet("select *,count(1) from goods where masterid='"+uuid+"' and state=2 and (gtype="+queryCondition1+" or gtype="+queryCondition3+" or gtype="+queryCondition5+")  GROUP BY gtype");
				int result = 0;
				while(querySet.next())
				{
					int count = querySet.getInt("count(1)");
					// 条件不满足 材料不足
					if(count < numCondition)
					{
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("code", 201);
						jsonObject.put("message", "<font color='red'>材料不足升级失败! 升级所需要的条件如下:</font>"
						        + "<br/><br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级玄铁."
								+ "<br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级金丝楠."
								+ "<br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级白虎皮."
								+ "<br/>需要:白银"+(Math.pow(level+1, 2))+"两."
								);
						return jsonObject.toString();
					}
					
					result ++;
				}
				
				if(result >= 3)
				{
					// 概率失败
					String res = checkFail(level, type, ggrade, name, uuid, gid);
					if(res != null)
						return res;
					
					if(name.contains("+"))
					{
						name = name.substring(0, name.length()-2);
					}
					name = name+"+"+(level+1);
							
					// 经过上面while循环的判断处理，到此处已经满足升级条件
					// 1.删除升级所用材料  扣除升级所需银子
					// 1.删除升级所用材料  扣除升级所需银子
					jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition1+") and state=2 limit "+numCondition);
					jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition3+") and state=2 limit "+numCondition);
					jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition5+") and state=2 limit "+numCondition);
					
					jdbcTemplate.update("update person set money=money-"+(Math.pow(level+1, 2) * 1000)+" where uuid='"+uuid+"'");
					// 2.提升装备属性
					int add = (ggrade/2+3);
					jdbcTemplate.update("update goods set level=level+1,name='"+name+"',adddefence=adddefence+"+add+" where masterid='"+uuid+"' and gid='"+gid+"'");
					// 3.更改装备名字
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code", 200);
					jsonObject.put("message", "<font color='red'>恭喜你成功升级装备到 "+(level+1)+"级！</font><br/><br/>防御属性(+"+add+"):"+adddefence+"-->"+(adddefence+add));
					return jsonObject.toString();
				}
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "<font color='red'>条件未满足！升级失败！升级所需要的条件如下:</font>"
				        + "<br/><br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级玄铁."
						+ "<br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级金丝楠."
						+ "<br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级白虎皮."
						+ "<br/>需要:白银"+(Math.pow(level+1, 2))+"两."
						);
				return jsonObject.toString();
			}
			// 腰带
			else if(type == 5)
			{
				int addhp = rowSet.getInt("addhp");
				
				SqlRowSet querySet = jdbcTemplate.queryForRowSet("select *,count(1) from goods where masterid='"+uuid+"' and state=2 and (gtype="+queryCondition3+" or gtype="+queryCondition4+" or gtype="+queryCondition5+")  GROUP BY gtype");
				int result = 0;
				while(querySet.next())
				{
					int count = querySet.getInt("count(1)");
					// 条件不满足 材料不足
					if(count < numCondition)
					{
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("code", 201);
						jsonObject.put("message", "<font color='red'>材料不足升级失败! 升级所需要的条件如下:</font>"
								+ "<br/><br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级天蚕丝."
								+ "<br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级金丝楠."
								+ "<br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级白虎皮."
								+ "<br/>需要:白银"+(Math.pow(level+1, 2))+"两."
								);
						return jsonObject.toString();
					}
					
					result ++;
				}
				
				if(result >= 3)
				{
					// 概率失败
					String res = checkFail(level, type, ggrade, name, uuid, gid);
					if(res != null)
						return res;
					
					if(name.contains("+"))
					{
						name = name.substring(0, name.length()-2);
					}
					name = name+"+"+(level+1);
							
					// 经过上面while循环的判断处理，到此处已经满足升级条件
					// 1.删除升级所用材料  扣除升级所需银子
					// 1.删除升级所用材料  扣除升级所需银子
					jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition3+") and state=2 limit "+numCondition);
					jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition4+") and state=2 limit "+numCondition);
					jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition5+") and state=2 limit "+numCondition);
					
					jdbcTemplate.update("update person set money=money-"+(Math.pow(level+1, 2) * 1000)+" where uuid='"+uuid+"'");
					// 2.提升装备属性
					int add = (ggrade/2);
					jdbcTemplate.update("update goods set level=level+1,name='"+name+"',addhp=addhp+"+add+" where masterid='"+uuid+"' and gid='"+gid+"'");
					// 3.更改装备名字
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code", 200);
					jsonObject.put("message", "<font color='red'>恭喜你成功升级装备到 "+(level+1)+"级！</font><br/><br/>血值属性(+"+add+"):"+addhp+"-->"+(addhp+add));
					return jsonObject.toString();
				}
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "<font color='red'>条件未满足！升级失败！升级所需要的条件如下:</font>"
						+ "<br/><br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级天蚕丝."
						+ "<br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级金丝楠."
						+ "<br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级白虎皮."
						+ "<br/>需要:白银"+(Math.pow(level+1, 2))+"两."
						);
				return jsonObject.toString();
			}
			// 头盔
			else if(type == 2)
			{
				int adddefence = rowSet.getInt("adddefence");
				
				SqlRowSet querySet = jdbcTemplate.queryForRowSet("select *,count(1) from goods where masterid='"+uuid+"' and state=2 and (gtype="+queryCondition1+" or gtype="+queryCondition5+")  GROUP BY gtype");
				int result = 0;
				while(querySet.next())
				{
					int count = querySet.getInt("count(1)");
					// 条件不满足 材料不足
					if(count < numCondition)
					{
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("code", 201);
						jsonObject.put("message", "<font color='red'>材料不足升级失败! 升级所需要的条件如下:</font>"
						        + "<br/><br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级玄铁."
								+ "<br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级金丝楠."
								+ "<br/>需要:白银"+(Math.pow(level+1, 2))+"两."
								);
						return jsonObject.toString();
					}
					
					result ++;
				}
				
				if(result >= 2)
				{
					// 概率失败
					String res = checkFail(level, type, ggrade, name, uuid, gid);
					if(res != null)
						return res;
					
					if(name.contains("+"))
					{
						name = name.substring(0, name.length()-2);
					}
					name = name+"+"+(level+1);
							
					// 经过上面while循环的判断处理，到此处已经满足升级条件
					// 1.删除升级所用材料  扣除升级所需银子
					// 1.删除升级所用材料  扣除升级所需银子
					jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition1+") and state=2 limit "+numCondition);
					jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition5+") and state=2 limit "+numCondition);
					
					jdbcTemplate.update("update person set money=money-"+(Math.pow(level+1, 2) * 1000)+" where uuid='"+uuid+"'");
					// 2.提升装备属性
					int add = (ggrade/2);
					jdbcTemplate.update("update goods set level=level+1,name='"+name+"',adddefence=adddefence+"+add+" where masterid='"+uuid+"' and gid='"+gid+"'");
					// 3.更改装备名字
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code", 200);
					jsonObject.put("message", "<font color='red'>恭喜你成功升级装备到 "+(level+1)+"级！</font><br/><br/>防御属性(+"+add+"):"+adddefence+"-->"+(adddefence+add));
					return jsonObject.toString();
				}
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "<font color='red'>条件未满足！升级失败！升级所需要的条件如下:</font>"
				        + "<br/><br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级玄铁."
						+ "<br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级金丝楠."
						+ "<br/>需要:白银"+(Math.pow(level+1, 2))+"两."
						);
				return jsonObject.toString();
			}
			// 披风
			else if(type ==3)
			{
				int adddefence = rowSet.getInt("adddefence");
				
				SqlRowSet querySet = jdbcTemplate.queryForRowSet("select *,count(1) from goods where masterid='"+uuid+"' and state=2 and (gtype="+queryCondition3+" or gtype="+queryCondition4+")  GROUP BY gtype");
				int result = 0;
				while(querySet.next())
				{
					int count = querySet.getInt("count(1)");
					// 条件不满足 材料不足
					if(count < numCondition)
					{
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("code", 201);
						jsonObject.put("message", "<font color='red'>材料不足升级失败! 升级所需要的条件如下:</font>"
						        + "<br/><br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级天蚕丝."
								+ "<br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级白虎皮."
								+ "<br/>需要:白银"+(Math.pow(level+1, 2))+"两."
								);
						return jsonObject.toString();
					}
					
					result ++;
				}
		
				if(result >= 2)
				{
					// 概率失败
					String res = checkFail(level, type, ggrade, name, uuid, gid);
					if(res != null)
						return res;
					
					if(name.contains("+"))
					{
						name = name.substring(0, name.length()-2);
					}
					name = name+"+"+(level+1);
							
					// 经过上面while循环的判断处理，到此处已经满足升级条件
					// 1.删除升级所用材料  扣除升级所需银子
					// 1.删除升级所用材料  扣除升级所需银子
					jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition3+") and state=2 limit "+numCondition);
					jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition4+") and state=2 limit "+numCondition);
					
					jdbcTemplate.update("update person set money=money-"+(Math.pow(level+1, 2) * 1000)+" where uuid='"+uuid+"'");
					// 2.提升装备属性
					int add = (ggrade/2);
					jdbcTemplate.update("update goods set level=level+1,name='"+name+"',adddefence=adddefence+"+add+" where masterid='"+uuid+"' and gid='"+gid+"'");
					// 3.更改装备名字
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code", 200);
					jsonObject.put("message", "<font color='red'>恭喜你成功升级装备到 "+(level+1)+"级！</font><br/><br/>防御属性(+"+add+"):"+adddefence+"-->"+(adddefence+add));
					return jsonObject.toString();
				}
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "<font color='red'>条件未满足！升级失败！升级所需要的条件如下:</font>"
				        + "<br/><br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级天蚕丝."
						+ "<br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级白虎皮."
						+ "<br/>需要:白银"+(Math.pow(level+1, 2))+"两."
						);
				return jsonObject.toString();
			}
			// 鞋子   ss
			else if(type ==6)
			{
				int adddefence = rowSet.getInt("adddefence");
				
				SqlRowSet querySet = jdbcTemplate.queryForRowSet("select *,count(1) from goods where masterid='"+uuid+"' and state=2 and (gtype="+queryCondition4+" or gtype="+queryCondition5+")  GROUP BY gtype");
				int result = 0;
				while(querySet.next())
				{
					int count = querySet.getInt("count(1)");
					// 条件不满足 材料不足
					if(count < numCondition)
					{
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("code", 201);
						jsonObject.put("message", "<font color='red'>材料不足升级失败! 升级所需要的条件如下:</font>"
						        + "<br/><br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级天蚕丝."
								+ "<br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级金丝楠."
								+ "<br/>需要:白银"+(Math.pow(level+1, 2))+"两."
								);
						return jsonObject.toString();
					}
					
					result ++;
				}
				
				if(result >= 2)
				{
					// 概率失败
					String res = checkFail(level, type, ggrade, name, uuid, gid);
					if(res != null)
						return res;
					
					if(name.contains("+"))
					{
						name = name.substring(0, name.length()-2);
					}
					name = name+"+"+(level+1);
							
					// 经过上面while循环的判断处理，到此处已经满足升级条件
					// 1.删除升级所用材料  扣除升级所需银子
					// 1.删除升级所用材料  扣除升级所需银子
					jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition4+") and state=2 limit "+numCondition);
					jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and (gtype="+queryCondition5+") and state=2 limit "+numCondition);
					
					jdbcTemplate.update("update person set money=money-"+(Math.pow(level+1, 2) * 1000)+" where uuid='"+uuid+"'");
					// 2.提升装备属性
					int add = (ggrade/2);
					jdbcTemplate.update("update goods set level=level+1,name='"+name+"',adddefence=adddefence+"+add+" where masterid='"+uuid+"' and gid='"+gid+"'");
					// 3.更改装备名字
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code", 200);
					jsonObject.put("message", "<font color='red'>恭喜你成功升级装备到 "+(level+1)+"级！</font><br/><br/>防御属性(+"+add+"):"+adddefence+"-->"+(adddefence+add));
					return jsonObject.toString();
				}
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "<font color='red'>条件未满足！升级失败！升级所需要的条件如下:</font>"
				        + "<br/><br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级天蚕丝."
						+ "<br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级金丝楠."
						+ "<br/>需要:白银"+(Math.pow(level+1, 2))+"两."
						);
				return jsonObject.toString();
			}
			else
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 202);
				jsonObject.put("message", "暂未放其他装备的升级，相关内容敬请期待。");
				return jsonObject.toString();
			}
		}
		else
		{
			JSONObject 	jsonObject = new JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "未查询到你的物品信息。");
			return jsonObject.toString();
		}
		
//		武器升级  需要材料： 玄铁x等级  琥珀x等级 
//		盔甲        需要材料： 白虎皮x等级  天蚕丝x等级  金丝楠x等级
	}
	
	public String pingu(String uuid,String gid)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select type,name,addattack,addhp,adddefence,ggrade from goods where masterid='"+uuid+"' and gid='"+gid+"'");
		if(rowSet.next())
		{
			int type = rowSet.getInt("type");
			String name = rowSet.getString("name");
			int ggrade = rowSet.getInt("ggrade");
			int addattack = rowSet.getInt("addattack");
			int adddefence= rowSet.getInt("adddefence");
			int addhp = rowSet.getInt("addhp");
			
			if(type == 101 || type == 102 || type == 103)
			{
				if(addattack < ggrade * Constant.eattack)
				{
					JSONObject 	jsonObject = new JSONObject();
					jsonObject.put("code", 201);
					jsonObject.put("message", "店铺老板把你的东西一把扔了出来，大骂到‘就你的这个破烂装备属性平平也敢拿出来丢人现眼，一边玩去！’");
					return jsonObject.toString();
				}
				else
				{
					int attack0 = ggrade * Constant.eattack+50+ggrade*1;
					int attack1 = ggrade * Constant.eattack+50+ggrade*2;
					int attack2 = ggrade * Constant.eattack+50+ggrade*4;
					int attack3 = ggrade * Constant.eattack+50+ggrade*5;
					//50+random.nextInt(ggrade*6);
					String res = "[ "+ggrade+"级 "+name+" 攻:"+addattack+" ]<br/><br/>此兵器质地优良,实属佳品,经和议之后一致";
					if(addattack > attack0 && addattack <= attack1)
					{
						res = res+"给出的等级评定是:<font color='red'>【丁等】</font>";
					}
					else if(addattack > attack1 && addattack <= attack2)
					{
						res = res+"给出的等级评定是:<font color='red'>【丙等】</font>";
					}
					else if(addattack > attack2 && addattack <= attack3)
					{
						res = res+"给出的等级评定是:<font color='red'>【乙等】</font>";
					}
					else if(addattack > attack3 && addattack < attack3-ggrade)
					{
						res = res+"给出的等级评定是:<font color='red'>【甲等】</font>";
					}
					else if(addattack >= attack3+ggrade)
					{
						res = res+"给出的等级评定是:<font color='red'>【极品】</font>";
					}
					else
					{
						res = res+"给出的等级评定是:<font color='red'>【普通】</font>";
					}
					
					res = res +"<br/><br/>极品: >="+(attack3+ggrade)+"<br/>甲等: >"+attack3+"<br/>乙等: >"+attack2+"<br/>丙等: >"+attack1+"<br/>丁等: >"+attack0;
					
					JSONObject 	jsonObject = new JSONObject();
					jsonObject.put("code", 200);
					jsonObject.put("message", res);
					return jsonObject.toString();
				}
			}
			else if(type == 2)
			{
				return systemPinggu(adddefence, ggrade, Constant.edefence_toukui,20,name, "防");
			}
			else if(type == 3)
			{
				return systemPinggu(adddefence, ggrade, Constant.edefence_pifeng,20,name, "防");
			}
			else if(type == 4)
			{
				return systemPinggu(adddefence, ggrade, Constant.edefence_kuijia,20,name, "防");
			}		
			else if(type == 5)
			{
				return systemPinggu(addhp, ggrade, Constant.ehp,80,name, "血");
			}	
			else if(type == 6)
			{
				return systemPinggu(adddefence, ggrade, Constant.edefence_kuijia,20,name, "防");
			}	
			else
			{
				JSONObject 	jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "本店暂不评估其他物品");
				return jsonObject.toString();
			}
		}
		else
		{
			JSONObject 	jsonObject = new JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "未查询到你的物品信息。");
			return jsonObject.toString();
		}
	}
	
	private String systemPinggu(int shuxingzhi,int ggrade,float addvalue,int fujiazhi,String name,String tag)
	{
		if(shuxingzhi < ggrade * addvalue)
		{
			JSONObject 	jsonObject = new JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "店铺老板把你的东西一把扔了出来，大骂到‘就你的这个破烂装备属性平平也敢拿出来丢人现眼，一边玩去！’");
			return jsonObject.toString();
		}
		else
		{
			int attack0 = (int) (ggrade * addvalue+fujiazhi+ggrade*1);
			int attack1 = (int) (ggrade * addvalue+fujiazhi+ggrade*2);
			int attack2 = (int) (ggrade * addvalue+fujiazhi+ggrade*4);
			int attack3 = (int) (ggrade * addvalue+fujiazhi+ggrade*5);
			//50+random.nextInt(ggrade*6);
			String res = "[ "+ggrade+"级 "+name+" "+tag+":"+shuxingzhi+" ]<br/><br/>此装备质地优良,实属佳品,经和议之后一致";
			if(shuxingzhi > attack0 && shuxingzhi <= attack1)
			{
				res = res+"给出的等级评定是:<font color='red'>【丁等】</font>";
			}
			else if(shuxingzhi > attack1 && shuxingzhi <= attack2)
			{
				res = res+"给出的等级评定是:<font color='red'>【丙等】</font>";
			}
			else if(shuxingzhi > attack2 && shuxingzhi <= attack3)
			{
				res = res+"给出的等级评定是:<font color='red'>【乙等】</font>";
			}
			else if(shuxingzhi > attack3 && shuxingzhi < attack3-ggrade)
			{
				res = res+"给出的等级评定是:<font color='red'>【甲等】</font>";
			}
			else if(shuxingzhi >= attack3+ggrade)
			{
				res = res+"给出的等级评定是:<font color='red'>【极品】</font>";
			}
			else
			{
				res = res+"给出的等级评定是:<font color='red'>【普通】</font>";
			}
			
			res = res +"<br/><br/>极品: >="+(attack3+ggrade)+"<br/>甲等: >"+attack3+"<br/>乙等: >"+attack2+"<br/>丙等: >"+attack1+"<br/>丁等: >"+attack0;
			
			JSONObject 	jsonObject = new JSONObject();
			jsonObject.put("code", 200);
			jsonObject.put("message", res);
			return jsonObject.toString();
		}
	}

	public String upgraderequest(String uuid, String gid)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select level,addattack,type,name,addattack,ggrade from goods where masterid='"+uuid+"' and gid='"+gid+"'");
		if(rowSet.next())
		{
			int level = rowSet.getInt("level");
			int type  = rowSet.getInt("type");
			
			// 武器   玄铁 琥珀
			if(type == 101 || type == 102 || type == 103)
			{
				// 数量限制
				int numCondition = level*2+1;
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "已有的等级越高则失败率也会越高(等级每高一级成功率降低10%),升级失败将清除已升等级和升级所加属性以及升级的材料和银子。<br/><br/><font color='red'>当前("+level+"级)升级所需要的条件如下:</font>"
						+ "<br/><br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级玄铁."
						+ "<br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级琥珀."
						+ "<br/>需要:白银"+(Math.pow(level+1, 2))+"两."
						);
				return jsonObject.toString();
			}
			// 铠甲   玄铁  金丝楠  白虎皮
			else if(type == 4)
			{
				// 数量限制
				int numCondition = level*2+1;
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "已有的等级越高则失败率也会越高(等级每高一级成功率降低10%),升级失败将清除已升等级和升级所加属性以及升级的材料和银子。<br/><br/><font color='red'>当前("+level+"级)升级所需要的条件如下:</font>"
						+ "<br/><br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级玄铁."
						+ "<br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级金丝楠."
						+ "<br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级白虎皮."
						+ "<br/>需要:白银"+(Math.pow(level+1, 2))+"两."
						);
				return jsonObject.toString();
			}
			// 腰带  天蚕丝  白虎皮  金楠丝
			else if(type == 5)
			{
				// 数量限制
				int numCondition = level*2+1;
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "已有的等级越高则失败率也会越高(等级每高一级成功率降低10%),升级失败将清除已升等级和升级所加属性以及升级的材料和银子。<br/><br/><font color='red'>当前("+level+"级)升级所需要的条件如下:</font>"
						+ "<br/><br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级天蚕丝."
						+ "<br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级金丝楠."
						+ "<br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级白虎皮."
						+ "<br/>需要:白银"+(Math.pow(level+1, 2))+"两."
						);
				return jsonObject.toString();
			}
			else if(type == 2)
			{
				// 数量限制
				int numCondition = level*2+1;
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "已有的等级越高则失败率也会越高(等级每高一级成功率降低10%),升级失败将清除已升等级和升级所加属性以及升级的材料和银子。<br/><br/><font color='red'>当前("+level+"级)升级所需要的条件如下:</font>"
						+ "<br/><br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级玄铁."
						+ "<br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级金丝楠."
						+ "<br/>需要:白银"+(Math.pow(level+1, 2))+"两."
						);
				return jsonObject.toString();
			}
			else if(type == 3)
			{
				// 数量限制
				int numCondition = level*2+1;
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "已有的等级越高则失败率也会越高(等级每高一级成功率降低10%),升级失败将清除已升等级和升级所加属性以及升级的材料和银子。<br/><br/><font color='red'>当前("+level+"级)升级所需要的条件如下:</font>"
				        + "<br/><br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级天蚕丝."
						+ "<br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级白虎皮."
						+ "<br/>需要:白银"+(Math.pow(level+1, 2))+"两."
						);
				return jsonObject.toString();
			}
			else if(type == 6)
			{
				// 数量限制
				int numCondition = level*2+1;
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "已有的等级越高则失败率也会越高(等级每高一级成功率降低10%),升级失败将清除已升等级和升级所加属性以及升级的材料和银子。<br/><br/><font color='red'>当前("+level+"级)升级所需要的条件如下:</font>"
				        + "<br/><br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级天蚕丝."
						+ "<br/>需要:"+numCondition+"份"+getNumToString(level+1)+"级金丝楠."
						+ "<br/>需要:白银"+(Math.pow(level+1, 2))+"两."
						);
				return jsonObject.toString();
			}
			else
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 202);
				jsonObject.put("message", "目前测试阶段，暂不开放其他装备的升级，相关内容敬请期待。");
				return jsonObject.toString();
			}
		}
		else
		{
			JSONObject 	jsonObject = new JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "未查询到你的物品信息。");
			return jsonObject.toString();
		}
	}


	
}
