package com.springmvc.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import net.sf.json.JSON;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springmvc.util.Constant;
import com.springmvc.util.NumberConverUtil;

@Component
public class ThingDao
{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	// 用递归来增加保证不重复的概率性
	private String getGGID(String ggid)
	{
		String newGid = UUID.randomUUID().toString().replaceAll("-", "");
		if(newGid.equals(ggid))
		{
			return getGGID(ggid);
		}
		else
			return newGid;
	}
	
	private void  cleanUpMoney(String masterid, long money)
	{
		jdbcTemplate.update("update person set moneytongban=0,moneybaiying=0,moneyhuangjin=0 where uuid='"+masterid+"'");
		
		if(money>=1000)
		{
			long surplusTongBan = money % 1000;
			long baiying = money / 1000;
			
			if(baiying >= 1000)
			{
				long surplusBaiYin = baiying % 1000;
				long huangjin = baiying / 1000;
				jdbcTemplate.update("update person set moneytongban="+surplusTongBan+",moneybaiying="+surplusBaiYin+",moneyhuangjin="+huangjin+"  where uuid='"+masterid+"'");
			}
			else
			{
				jdbcTemplate.update("update person set moneytongban="+surplusTongBan+",moneybaiying="+baiying+" where uuid='"+masterid+"'");
			}
		}
		else
		{
			jdbcTemplate.update("update person set moneytongban="+money+" where uuid='"+masterid+"'");
		}
	}
	
	// 寄存物
	public String deposit(String uuid, String gid,String type)
	{
		if("1".equals(type))
		{
			SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select name,gtype from goods where masterid='"+uuid+"' and gid='"+gid+"' and state=2 and (type != 9 && type != 10 && type != 11)");
			if(rowSet.next())
			{
				String name = rowSet.getString("name");
				
				// 暂时限制材料的寄放
				int gtype = rowSet.getInt("gtype");
				if(gtype >= 281 && gtype <= 334)
				{
					JSONObject jsonObject = new JSONObject();
					try
					{
						jsonObject.put("code", 202);
						jsonObject.put("message", "这类物品暂时无法寄放。");
					}
					catch (JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return jsonObject.toString();
				}
				
				
				SqlRowSet countSet = jdbcTemplate.queryForRowSet("select COUNT(name) as count from goods where masterid='"+uuid+"' and state=5 and gtype<281");
				if(countSet.next())
				{
					int count = countSet.getInt("count");
					if(count < 20)
					{
						jdbcTemplate.update("update goods set state=5,visibility=1 where masterid='"+uuid+"' and gid='"+gid+"' and state=2");
						
						JSONObject jsonObject = new JSONObject();
						try
						{
							jsonObject.put("code", 200);
							jsonObject.put("message", "你将 ["+name+"] 寄存到钱庄。");
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
							jsonObject.put("code", 202);
							jsonObject.put("message", "你不能再存更多东西。");
						}
						catch (JSONException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return jsonObject.toString();
					}
				}
			}
			else
			{
				JSONObject jsonObject = new JSONObject();
				try
				{
					jsonObject.put("code", 201);
					jsonObject.put("message", "你没有该物品!(严禁使用第三方外挂)");
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return jsonObject.toString();
			}
		}
		return null;
	}
	

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public String sell(String uuid, String gid)
	{
		// 先查询该用户是否存在该gid的装备
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select name,price,life,gtype from goods where gid='"+gid+"' and masterid='"+uuid+"'");
		if(rowSet.next())
		{
			long price = rowSet.getLong("price");
			String name = rowSet.getString("name");
			int life = rowSet.getInt("life");
			int gtype = rowSet.getInt("gtype");
			
			float f = (life*1.0f) / Constant.life;
			price = (long) (price * f);
			
			if(price > 20)
			{
				price = price - price/10;
				
//				// 卖掉材料
//				if(gtype >= 281 && gtype <= 331)
//				{
//					// 身上材料总个数
//					int c = jdbcTemplate.queryForInt("select count(1) from goods where masterid='"+uuid+"' and gtype="+gtype+" and state");
//					// 如果个数多于1个，则丢弃隐藏的，并把显示的数量减一
//					if(c > 1)
//					{
//						jdbcTemplate.update("update goods set count=count-1 where masterid='"+uuid+"' and gid='"+gid+"' and visibility=1");//count=count-1
//						jdbcTemplate.update("delete from goods where where visibility=0 and gtype="+gtype+" and masterid='"+uuid+"' limit 1");
//					}
//					else
//						jdbcTemplate.update("delete from goods where where gtype="+gtype+" and masterid='"+uuid+"'");
//				}
//				else
				{
					// 删除这件装备
					jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and gid='"+gid+"'");
					jdbcTemplate.update("update person set money=money+"+price+" where uuid='"+uuid+"'");
				}
				
				JSONObject jsonObject = new JSONObject();
				try
				{
					jsonObject.put("message","你卖出["+name+"],扣除税费"+price/10+"铜板，获得"+price+"铜板");
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
//				// 卖掉材料
//				if(gtype >= 281 && gtype <= 331)
//				{
//					// 身上材料总个数
//					int c = jdbcTemplate.queryForInt("select count(1) from goods where masterid='"+uuid+"' and gtype="+gtype+" and state");
//					// 如果个数多于1个，则丢弃隐藏的，并把显示的数量减一
//					if(c > 1)
//					{
//						jdbcTemplate.update("update goods set count=count-1 where masterid='"+uuid+"' and gid='"+gid+"' and visibility=1");//count=count-1
//						jdbcTemplate.update("delete from goods where where visibility=0 and gtype="+gtype+" and masterid='"+uuid+"' limit 1");
//					}
//					else
//						jdbcTemplate.update("delete from goods where where gtype="+gtype+" and masterid='"+uuid+"'");
//				}
//				else
				{
					// 删除这件装备
					jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and gid='"+gid+"'");
					jdbcTemplate.update("update person set money=money+"+price+" where uuid='"+uuid+"'");
				}
				
				JSONObject jsonObject = new JSONObject();
				try
				{
					jsonObject.put("message","你的东西太破旧了，老板看你可怜巴巴地抱着这个破烂坐门口赖着不走，实在没辙了，勉强答应出10个铜板收购。");
					jsonObject.put("code", 200);
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
				jsonObject.put("message","不存在该物品!");
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

	// 提取
	public String take(String uuid, String gid, String type)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select name from goods where masterid='"+uuid+"' and gid='"+gid+"' and state=5 and (type != 9 && type != 10 && type != 11)");
		if(rowSet.next())
		{
			String name = rowSet.getString("name");
			
			// 检查行囊的可容数量
			SqlRowSet countSet = jdbcTemplate.queryForRowSet("select COUNT(name) as count from goods where masterid='"+uuid+"' and state=2 and gtype < 281");
			if(countSet.next())
			{
				int count = countSet.getInt("count");
				if(count < Constant.count)
				{
					jdbcTemplate.update("update goods set state=2 where masterid='"+uuid+"' and gid='"+gid+"' and state=5");
					
					JSONObject jsonObject = new JSONObject();
					try
					{
						jsonObject.put("code", 200);
						jsonObject.put("message", "你将 ["+name+"] 从钱庄提取出来。");
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
						jsonObject.put("code", 202);
						jsonObject.put("message", "你身上行囊已满不能放更多东西。");
					}
					catch (JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return jsonObject.toString();
				}
			}
		}
		else
		{
			JSONObject jsonObject = new JSONObject();
			try
			{
				jsonObject.put("code", 201);
				jsonObject.put("message", "你没有该物品!(严禁使用第三方外挂)");
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jsonObject.toString();
		}
		return null;
	}
	
	// 捡起
	public String pickUp(String uuid, String gid)
	{
		// 查询时必须带上条件state=3，确保
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from goods where gid ='"+gid+"' and state=3");
		if(rowSet.next())
		{
			String masteruuid = rowSet.getString("masterid");
			int gtype = rowSet.getInt("gtype");

			if(!uuid.equals(masteruuid))
			{
				long diff = jdbcTemplate.queryForLong("select (UNIX_TIMESTAMP() - unlawful_time) as diff from goods where gid='"+gid+"' and state=3");
				// 十秒的保护时间
				if(diff < 10)
				{
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("code", 1001);
					jsonObject.put("message", "这个物品暂时还不是你的！");
					return jsonObject.toString();
				}
			}
			
			int type = rowSet.getInt("type");
			int samecount = 0;
//			boolean b = false;// 判断是不是钱  如果是金钱类的则去除掉行囊格子数的判断
			if(type != 9 && type != 10 && type != 11)
			{
			// 检查行囊的可容数量    and gtype=(select gtype from goods where gid='"+gid+"'则为特殊的物品诸如材料宝石等
			SqlRowSet countSet = jdbcTemplate.queryForRowSet("select COUNT(name) as count,(select count(1) from goods where masterid='"+uuid+"' and state=2 and gtype=(select gtype from goods where gid='"+gid+"' and gtype >= 281))  as samecount  from goods where masterid='"+uuid+"' and state=2 and gtype<281");
			
			if(countSet.next())
			{
				int count = countSet.getInt("count");
				samecount = countSet.getInt("samecount");
				
				if(count >= Constant.count && samecount==0)
				{
					JSONObject jsonObject = new JSONObject();
					try
					{
						jsonObject.put("code", 201);
						jsonObject.put("message", "你的行囊已经满了，无法再装入更多东西，你只能放弃捡起地上的物品。");
					}
					catch (JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return jsonObject.toString();
				}
//				else if(samecount > 0)
//				{
//
//				}
			}
//			else
//			{
//				JSONObject jsonObject = new JSONObject();
//				try
//				{
//					jsonObject.put("code", 201);
//					jsonObject.put("message", "没有查询到您的用户信息");
//				}
//				catch (JSONException e)
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return jsonObject.toString();
//			}
//			
			
		}

			
			String name = rowSet.getString("name");
//			boolean b = false;// 判断是不是钱
			if(type != 9 && type != 10 && type != 11)
			{
				// 同类型材料 叠加
				if(samecount > 0)
				{
					// 捡起来的动作
					jdbcTemplate.update("update goods set visibility = 0,state=2,masterid='"+uuid+"' where gid='"+gid+"' and state=3");
					
					// 更新数量
					int c = jdbcTemplate.queryForInt("select count(1) from goods where gtype="+gtype+" and masterid='"+uuid+"' and state=2");
					jdbcTemplate.update("update goods set count="+c+" where gtype="+gtype+" and masterid='"+uuid+"' and visibility=1");
				}
				else
				{
					jdbcTemplate.update("update goods set masterid='"+uuid+"',state=2 where gid='"+gid+"'");
				}
			}
			// 是金钱类的物品，则取出物品价值，叠加到捡到者的总钱数里
			else
			{
//					b = true;
					long price = rowSet.getLong("price");
					//System.out.println("price = "+price);
					if(price > 0)
					{
//					if(uuid.equals(masteruuid))
//					{
						jdbcTemplate.update("update person set money=money+"+price+" where uuid='"+uuid+"'");
						jdbcTemplate.update("delete from goods where gid='"+gid+"'");
//					}
//					else
//					{
//						jdbcTemplate.update("update person set money=money+"+price+" where uuid='"+uuid+"'");
//						jdbcTemplate.update("delete from goods where gid='"+gid+"'");
//					}
				}
			}
			// 305   350   55
			
			JSONObject jsonObject = new JSONObject();
			try
			{
				jsonObject.put("code", 200);
				Random random = new Random();
				int n = random.nextInt(3);
				if(n == 0)
					jsonObject.put("message", "你捡起["+name+"]，偷偷放进自己行囊");
				else if(n == 1)
					jsonObject.put("message", "你捡起["+name+"]，弹了弹上面的灰尘，正大光明地放进自己行囊");
				else if(n == 2)
					jsonObject.put("message", "你弯腰捡起["+name+"]，欣喜若狂，连忙放进自己行囊");
					
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
				jsonObject.put("message", "已经不存在了，可能被别人捡走了，手短怨不得别人呀!");
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jsonObject.toString();
		}
	}

	public String throwThing(String uuid, String gid, String areaid)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from goods where gid='"+gid+"' and masterid='"+uuid+"' and state = 2");
		if(rowSet.next())
		{
			String name = rowSet.getString("name");
			int gtype = rowSet.getInt("gtype");
			int count = rowSet.getInt("count");
			
			if(count < 1 && gtype >= 281)
			{
				try
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code", "201");
					jsonObject.put("message", "已经没有该物品！");
					return jsonObject.toString();
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			JSONObject jsonObject = new JSONObject();
			
//			// 丢弃材料
//			if(gtype >= 281 && gtype <= 331)
//			{
//				// 身上材料总个数
//				int c = jdbcTemplate.queryForInt("select count(1) from goods where masterid='"+uuid+"' and gtype="+gtype+" and state = 2");
//				// 如果个数多于1个，则丢弃隐藏的，并把显示的数量减一
//				if(c > 1)
//				{
//					jdbcTemplate.update("update goods set count="+c+" where masterid='"+uuid+"' and gid='"+gid+"' and visibility=1 and state=2");//count=count-1
//					jdbcTemplate.update("update goods set visibility=1,masterid='"+uuid+"',state=3 ,gareaid="+areaid+",unlawful_time=UNIX_TIMESTAMP() where visibility=0 and gtype="+gtype+" and masterid='"+uuid+"' limit 1");
//				}
//				else
//				{
//					jdbcTemplate.update("update goods set count=1,visibility=1,masterid='"+uuid+"',state=3 ,gareaid="+areaid+",unlawful_time=UNIX_TIMESTAMP() where gid='"+gid+"'");
//				}
//			}
//			else
			{
				// 丢弃
				jdbcTemplate.update("update goods set masterid='"+uuid+"',state=3 ,gareaid="+areaid+",unlawful_time=UNIX_TIMESTAMP() where gid='"+gid+"'");
			}
			try
			{
				jsonObject.put("code", "200");
				jsonObject.put("message", "你潇洒地一甩手将【"+name+"】丢弃");
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
				jsonObject.put("code", "200");
				jsonObject.put("message", "数据错误!");
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return jsonObject.toString();
		}
	}

	public String throwMoney(String uuid, String gid, String number, String areaid)
	{
//		System.out.println("11111");
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select name,price,type from goods where masterid='"+uuid+"' and gid='"+gid+"'");
		if(rowSet.next())
		{
			long price = rowSet.getLong("price");
			int type = rowSet.getInt("type");
			String name = null;
			if(price < Long.valueOf(number))
			{
				try
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code", 200);
					jsonObject.put("message", "孩子你想多了,你并没有那么多钱");
					return jsonObject.toString();
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
//			else if(price == Long.valueOf(number))
//			{
//				// 第一步 把丢弃的钱数额从玩家身上减除掉
//				jdbcTemplate.update("update person set money=money-"+price +" where uuid='"+uuid+"'");
//				// 第二步 在这个地区码上生成一个新的物品
//				String tgid = UUID.randomUUID().toString().replaceAll("-", "");
//				String name = getNameByMoney(Long.valueOf(number));
//				jdbcTemplate.update("insert into goods(gid,name,masterid,price,state,type) values('"+tgid+"','"+name+"','"+uuid+"',"+price+","+3+","+type+")");
//			}
			else
			{
//				System.out.println("222222");

				// 第一步 把丢弃的钱数额从玩家身上减除掉
				jdbcTemplate.update("update person set money=money-"+Long.valueOf(number) +" where uuid='"+uuid+"'");
				// 第二步 在这个地区码上生成一个新的物品
				String tgid = UUID.randomUUID().toString().replaceAll("-", "");
				name = NumberConverUtil.getNameByMoney(Long.valueOf(number));
//				System.out.println("3333333");
//				unlawful_time=UNIX_TIMESTAMP()
				jdbcTemplate.update("insert into goods(gid,name,masterid,price,state,type,gareaid,unlawful_time) values('"+tgid+"','"+name+"','"+"-1"+"',"+Long.valueOf(number)+","+3+","+type+","+areaid+",UNIX_TIMESTAMP())");
			}
			
			
			try
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 200);
				jsonObject.put("message", "你丢弃了 ["+name+"]");
				return jsonObject.toString();
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
				jsonObject.put("message", "你没有这件物品。");
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
	
	public String buyThing(String uuid, String gid)
	{
		SqlRowSet rowSetCount = jdbcTemplate.queryForRowSet("select COUNT(*) from goods where masterid='"+uuid+"' and state=2 and gtype<281");
		rowSetCount.next();
		if(rowSetCount.getInt("count(*)") >= Constant.count)
		{
			JSONObject object = new JSONObject();
			try
			{
				object.put("code", 405);
				object.put("message", "你的行囊已满，无法再装更多东西");
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return object.toString();
		}
		
		//955,485,408,78
		
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select * from goods where gid='"+gid+"'");
		if(sqlRowSet.next())
		{
			String ggid = sqlRowSet.getString("gid");
			
			// 保证不重复
			String newGid = getGGID(ggid);
			
			String name = sqlRowSet.getString("name");
			String descript = sqlRowSet.getString("descript");
			String masterid = uuid;
			long price = sqlRowSet.getLong("price");
			long life = sqlRowSet.getInt("life");
			int state = 2;
			int addhp = sqlRowSet.getInt("addhp");
			int type = sqlRowSet.getInt("type");
			int addattack = sqlRowSet.getInt("addattack");
			int adddefence = sqlRowSet.getInt("adddefence");
			int adddexterous = sqlRowSet.getInt("adddexterous");
			int ggrade = sqlRowSet.getInt("ggrade");
			int gareaid = -1;
			
			// 获取传入的uuid主人身上的钱是否够买下物品
			SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select money from person where uuid='"+uuid+"'");
			if(rowSet.next())
			{
				try
				{		// 
						// 判断是否够付账
						long userMoney = rowSet.getLong("money");
						if(userMoney >= price)
						{
							// 添加一条已传入uuid为物主的新的物品记录
							jdbcTemplate.update("insert into goods(gid,name,masterid,descript,price,life,state,addhp,addattack,adddefence,adddexterous,ggrade,gareaid,type) values('"+newGid+"','"+name+"','"+masterid+"','"+descript+"',"+price+","+life+","+state+","+addhp+","+addattack+","+adddefence+","+adddexterous+","+ggrade+","+gareaid+","+type+")");
							jdbcTemplate.update("update person set money=(money-"+price+") where uuid='"+uuid+"'");
							JSONObject object = new JSONObject();
							try
							{
								object.put("code", 200);
								object.put("message", "购买成功！您获得【"+name+"】,亲爱的客官，货物已经放到您的行囊里了，还有什么其他需要吗？");
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
							JSONObject object = new JSONObject();
							try
							{
								object.put("code", 200);
								object.put("message", "购买失败！你这穷货还敢上我们这大商铺来蒙事，买不起就别瞎摸，也不掂量掂量自己的钱包里有几个筒子!!!");
							}
							catch (JSONException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return object.toString();
						}
					}
				catch (Exception e)
				{
					JSONObject object = new JSONObject();
					try
					{
						object.put("code", 405);
						object.put("message", "物品已经不知去向，如果我说我这是黑店，你会不会哭？\n"+e.toString());
					}
					catch (JSONException e2)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return object.toString();
				}
			}
			else
			{
				JSONObject object = new JSONObject();
				try
				{
					object.put("code", 405);
					object.put("message", "咦？怎么没有查到你的身份信息，你是黑户口吗？");
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return object.toString();
			}
		}
		else
		{
			JSONObject object = new JSONObject();
			try
			{
				object.put("code", 405);
				object.put("message", "这件物品已经不存在");
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return object.toString();
		}
	}

	
	public List<Map<String, Object>> getSellList(String uuid)
	{
		return jdbcTemplate.queryForList("select * from goods where masterid='"+uuid+"' and state=2 and (type=101 or type=102 or type=103 or type=2 or type=3 or type=4 or type=5 or type=6 or type=7 or type=8)");
	}
	
	/** 卸除装备*/
	public String disarmed(String uuid, String gid)
	{
		try
		{
			SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select gid,ggrade,addhp,addattack,adddefence,adddexterous,name,state,type from goods where gid='"+gid+"' and masterid='"+uuid+"' and state=1");
			if(sqlRowSet.next())
			{
				int addhp = sqlRowSet.getInt("addhp");
				int addattack = sqlRowSet.getInt("addattack");
				int adddefence = sqlRowSet.getInt("adddefence");
				int adddexterous = sqlRowSet.getInt("adddexterous");
				
				jdbcTemplate.update("update person set attack=attack-"+addattack+","+"defence=defence-"+adddefence+","+"maxhp=maxhp-"+addhp+","+"dodge=dodge-"+adddexterous+" where uuid='"+uuid+"'");
			}
			
			jdbcTemplate.update("update goods set state=2 where masterid='"+uuid+"' and gid='"+gid+"'");
			
			JSONObject jsonObject = new JSONObject();
			try
			{
				//System.out.println("gid="+gid);
				jsonObject.put("code", 200);
				jsonObject.put("message", "成功卸除装备！");
			}
			catch (JSONException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return jsonObject.toString();
		}
		catch (Exception e)
		{
			JSONObject jsonObject = new JSONObject();
			try
			{
				jsonObject.put("code", 405);
				jsonObject.put("message", "卸除装备失败");
			}
			catch (JSONException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return jsonObject.toString();
		}
	}
	
	/**
	 * 获取物品清单
	 * @author 许仕永(xsy)
	 * des: 
	 * @param table
	 * @param masterid 物主id
	 * @param state  1|已装备物品  2|随身物品
	 * @return
	 */
	@ResponseBody
	public List<Map<String, Object>> getGoods(String table,String masterid,String state)
	{
//		try
//		{
			List<Map<String, Object>> result = null;
			
			if("2".equals(state))
			{
				String queryMoneyCheck = "select money from person where uuid='"+masterid+"'";
				SqlRowSet querySetCheck = jdbcTemplate.queryForRowSet(queryMoneyCheck);
				if(querySetCheck.next())
				{
					long money = querySetCheck.getLong("money");
					// 整理钱币
					cleanUpMoney(masterid,money);
					String queryMoney = "select moneyhuangjin,moneybaiying,moneytongban,money from person where uuid='"+masterid+"'";
					SqlRowSet querySet = jdbcTemplate.queryForRowSet(queryMoney);
					querySet.next();
					
					long huangjin = querySet.getLong("moneyhuangjin");
					if(huangjin != 0)
					{
						UUID uuid = UUID.randomUUID();
						String gid = uuid.toString().replace("-", "");
						jdbcTemplate.update("delete from goods where masterid='"+masterid+"' and type=9 and state=2");
						jdbcTemplate.update("insert into goods(gid,name,masterid,state,type,price) values('"+gid+"','"+(NumberConverUtil.conver(huangjin+"")+"两黄金")+"','"+masterid+"',"+2+","+9+","+(huangjin*1000000)+")");
					}
					else if(huangjin == 0)
					{
						jdbcTemplate.update("delete from goods where masterid='"+masterid+"' and type=9 and state=2");
					}
					
					long moneybaiying = querySet.getLong("moneybaiying");
					if(moneybaiying != 0)
					{
						UUID uuid = UUID.randomUUID();
						String gid = uuid.toString().replace("-", "");
						jdbcTemplate.update("delete from goods where masterid='"+masterid+"' and type=10 and state=2");
						jdbcTemplate.update("insert into goods(gid,name,masterid,state,type,price) values('"+gid+"','"+(NumberConverUtil.conver(moneybaiying+"")+"两白银")+"','"+masterid+"',"+2+","+10+","+(moneybaiying*1000)+")");
					}
					else if(moneybaiying == 0)
					{
						jdbcTemplate.update("delete from goods where masterid='"+masterid+"' and type=10 and state=2");
					}
					
					long moneytongban = querySet.getLong("moneytongban");
					if(moneytongban != 0)
					{
						UUID uuid = UUID.randomUUID();
						String gid = uuid.toString().replace("-", "");
						jdbcTemplate.update("delete from goods where masterid='"+masterid+"' and type=11 and state=2");
						jdbcTemplate.update("insert into goods(gid,name,masterid,state,type,price) values('"+gid+"','"+(NumberConverUtil.conver(moneytongban+"")+"个铜板")+"','"+masterid+"',"+2+","+11+","+moneytongban+")");
					}
					else if(moneytongban == 0)
					{
						jdbcTemplate.update("delete from goods where masterid='"+masterid+"' and type=11 and state=2");
					}
					
					
					// 处理整理其他物品如材料的叠加
					String sql = "select name,gid,gtype,count,COUNT(1) from goods where masterid='"+masterid+"' and state=2 GROUP BY gtype";
					SqlRowSet thingsSet = jdbcTemplate.queryForRowSet(sql);
					
					// 所有特殊类型
					int[] gtypes = new int[60];
					for (int i = 0; i < 60; i++)
					{
						gtypes[i] = 281+i;
					}
					// 循环判断是否是特殊类型
					while (thingsSet.next())
					{
						int gtype = thingsSet.getInt("gtype");
						
						if(gtype > 280 && gtype < 340)
						{
							String gid = thingsSet.getString("gid");
							int count = thingsSet.getInt("COUNT(1)");
							
							for (int i = 0; i < gtypes.length; i++)
							{
								if(gtypes[i] == gtype)
								{
									// 如果有多个材料欲做叠加处理，第一步先把其中一个物品设置为可显示，并且count字段设置为该类物品的总数目
									jdbcTemplate.update("update goods set visibility=1,count="+count+" where masterid='"+masterid+"' and gid='"+gid+"' and gtype="+gtype+"");
									//System.out.println("第一句："+"update goods set visibility=1,count="+count+" where masterid='"+masterid+"' and gid='"+gid+"' and gtype="+gtype+"");
									// 第二步，把该类物品的其他物件设置为不可显示
									jdbcTemplate.update("update goods set visibility=0,count=0 where masterid='"+masterid+"' and gtype="+gtype+" and gid!='"+gid+"'");
									//System.out.println("第二句："+"update goods set visibility=0,count=0 where masterid='"+masterid+"' and gtype="+gtype+" and gid!='"+gid+"'");
								}
							}
						}
					}
				}
			}
			String sql = "select * from goods where masterid='"+masterid+"' and visibility = 1 and state="+state+" order by type desc";
			//System.out.println("第三句:"+sql);
			result = jdbcTemplate.queryForList(sql);
			return result;
//		}
//		catch (Exception e)
//		{
//			// TODO: handle exception
//			System.out.println("e="+e.toString());
//		}
//		return null;
	}
	
	public String armed(String uuid, String gid)
	{
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select gtype,gid,ggrade,addhp,addattack,adddefence,adddexterous,name,state,type,life from goods where gid='"+gid+"' and masterid='"+uuid+"'");
		if(sqlRowSet.next())
		{
			int ggrade = sqlRowSet.getInt("ggrade");
			int state = sqlRowSet.getInt("state");
			int type = sqlRowSet.getInt("type");
			int addhp = sqlRowSet.getInt("addhp");
			int gtype = sqlRowSet.getInt("gtype");
			int addattack = sqlRowSet.getInt("addattack");
			int adddefence = sqlRowSet.getInt("adddefence");
			int adddexterous = sqlRowSet.getInt("adddexterous");
			int life = sqlRowSet.getInt("life");
			String name = sqlRowSet.getString("name");
			String newgid = sqlRowSet.getString("gid");
			
			if(type == 8)
			{
				// 装备升级材料
				if(gtype >= 281 && gtype<= 329)
				{
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("code", 201);
					jsonObject.put("message", "这是装备升级需要的材料，不能直接使用，如果要升级装备请到铁匠铺。");
					return jsonObject.toString();
				}
				else if(gtype == 33)
				{
					SqlRowSet user = jdbcTemplate.queryForRowSet("select maxhp,hp from person where uuid='"+uuid+"'");
					if(user.next())
					{
						int maxhp = user.getInt("maxhp");
						int hp = user.getInt("hp");
						
						// 删除已使用的物品
						jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and gid='"+gid+"'");
						
						if((hp+addhp) < maxhp)
						{
							jdbcTemplate.update("update person set hp=hp+"+addhp+" where uuid='"+uuid+"'");
						}
						else
						{
							jdbcTemplate.update("update person set hp=maxhp where uuid='"+uuid+"'");
						}
						
						JSONObject jsonObject = new JSONObject();
						try
						{
							jsonObject.put("code", "200");
							jsonObject.put("message", "你成功使用了药水补充体力");
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
							jsonObject.put("code", "405");
							jsonObject.put("message", "未查询到您的用户信息");
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
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("code", 201);
					jsonObject.put("message", "这是特殊物品，不能直接使用！看下物品的详情吧。");
					return jsonObject.toString();
				}
			}

			if(life <= 5)
			{
				JSONObject jsonObject = new JSONObject();
				try
				{
					jsonObject.put("code", "405");
					jsonObject.put("message", "["+name+"] 已破烂不堪，无法装备!");
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return jsonObject.toString();
			}
			
			SqlRowSet sqlRowSet2 = jdbcTemplate.queryForRowSet("select grade from person where uuid='"+uuid+"'");
			
			if(sqlRowSet2.next() && state != 1)
			{
				int userGrade = sqlRowSet2.getInt("grade");
				if(userGrade >= ggrade )
				{
					String gname = "";
					//System.out.println("查询:"+"select type,name,gid,addhp,addattack,adddefence,adddexterous from goods where masterid='"+uuid+"' and state=1 and type="+type);
					SqlRowSet aleadyArmed;
					
					try
					{
					
					// 判断是否已装备
					if(type < 100)
						aleadyArmed = jdbcTemplate.queryForRowSet("select type,name,gid,addhp,addattack,adddefence,adddexterous from goods where masterid='"+uuid+"' and state=1 and type="+type);
					else
						aleadyArmed = jdbcTemplate.queryForRowSet("select type,name,gid,addhp,addattack,adddefence,adddexterous from goods where masterid='"+uuid+"' and state=1 and (type=101 or type =102 or type=103)");
						
					if(aleadyArmed.next())
					{
						// 解除属性
							gname = aleadyArmed.getString("name");
							String ggid = aleadyArmed.getString("gid");
							int gaddhp = aleadyArmed.getInt("addhp");
							int gaddattack = aleadyArmed.getInt("addattack");
							int gadddefence = aleadyArmed.getInt("adddefence");
							int gadddexterous = aleadyArmed.getInt("adddexterous");
							jdbcTemplate.update("update person set dodge=dodge-"+gadddexterous+",maxhp=maxhp-"+gaddhp+",attack=attack-"+gaddattack+",defence=defence-"+gadddefence+" where uuid='"+uuid+"'");
							jdbcTemplate.update("update goods set state=2 where gid='"+ggid+"'");
					}
					
				}
				catch (Exception e)
				{
					// TODO: handle exception
					System.out.println("e."+e.toString());
				}
					
						// 增加属性
						jdbcTemplate.update("update person set maxhp=maxhp+"+addhp+",attack=attack+"+addattack+",defence=defence+"+adddefence+",dodge=dodge+"+adddexterous+" where uuid='"+uuid+"'");
						jdbcTemplate.update("update goods set state=1 where gid='"+newgid+"'");
					
					JSONObject jsonObject = new JSONObject();
					try
					{
						jsonObject.put("code", "200");
						if("".equals(gname))
							jsonObject.put("message", "你已装备了:"+name);
						else
							jsonObject.put("message", "你卸下"+gname+",装备换上了:"+name+"\n");
							
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
						jsonObject.put("code", "405");
						jsonObject.put("message", "你的等级还不能用这个装备!等能力强点再装备吧!");
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
					jsonObject.put("code", "405");
					jsonObject.put("message", "未查询到用户信息!");
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
				jsonObject.put("code", "405");
				jsonObject.put("message", "物品错误!!!");
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jsonObject.toString();
		}
	}
	
//	public Object sendSystemMessage(String receiver_uuid, String keyword)
//	{
//		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		// 发系统消息提示
//		String mid = UUID.randomUUID().toString().replaceAll("-", "");
//		String notiMassgae = "insert into message(mid,senderid,receiveid,message,sendtime,readflag,sendername,ordertime,type) "
//				+ "values("+ "'"+ mid+ "','0000','"+receiver_uuid+"','"+keyword+"','"+sdf.format(new Date(System.currentTimeMillis()))+"','false','系统消息',"+System.currentTimeMillis()+",2)";
//		jdbcTemplate.update(notiMassgae);
//		return null;
//	}
	
	
	public List<Map<String, Object>> getBuyGoodsList(String type)
	{
		return jdbcTemplate.queryForList("select * from goods where state=4 and type = "+type);
	}

	public String buyDrugs(String uuid, String type)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select name,money from person where uuid='"+uuid+"'");
		if(rowSet.next())
		{
			long money = rowSet.getLong("money");
			int count = jdbcTemplate.queryForInt("select count(*) from goods where masterid='"+uuid+"' and state=2 and gtype < 281");
			if(count >= Constant.count)
			{
				JSONObject jsonObject = new JSONObject();
				try
				{
					jsonObject.put("code", "201");
					jsonObject.put("message", "你身上行囊已满，无法再放更多物品。");
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
				int m = 0;
				if("1".equals(type))
					m = 30;
				else if("2".equals(type))
					m = 100;
				else if("3".equals(type))
					m = 200;
				
				if(money >= m)
				{
					jdbcTemplate.update("update person set money=money-"+m+" where uuid='"+uuid+"'");
					String gid = UUID.randomUUID().toString().replaceAll("-", "");
					String sql = null; 
					if("1".equals(type))
						sql = "INSERT INTO goods (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`) VALUES ('"+gid+"', '一级药水', '"+uuid+"', '30', '255', '2', '300', '0', '0', '0', '8', '1', '一级药水，可以补充体能，恢复血量300.', NULL, '33');";
					else if("2".equals(type))
						sql = "INSERT INTO goods (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`) VALUES ('"+gid+"', '二级药水', '"+uuid+"', '100', '255', '2', '1000', '0', '0', '0', '8', '2', '二级药水，可以补充体能，恢复血量1000.', NULL, '33');";
					else if("3".equals(type))
						sql = "INSERT INTO goods (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`) VALUES ('"+gid+"', '三级药水', '"+uuid+"', '200', '255', '2', '2200', '0', '0', '0', '8', '3', '三级药水，可以补充体能，恢复血量2200.', NULL, '33');";
					jdbcTemplate.update(sql);
					
					JSONObject jsonObject = new JSONObject();
					try
					{
						jsonObject.put("code", "200");
						jsonObject.put("message", "你成功买下一瓶"+type+"级药水。");
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
						jsonObject.put("code", "201");
						jsonObject.put("message", "你没有足够的钱，只能眼巴巴看着。");
					}
					catch (JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return jsonObject.toString();
				}
			}
		}
		else
		{
			JSONObject jsonObject = new JSONObject();
			try
			{
				jsonObject.put("code", "201");
				jsonObject.put("message", "未查询到您的账号信息!");
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jsonObject.toString();
		}
	}

	public String repair(String uuid, String gid)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from goods where masterid='"+uuid+"' and gid='"+gid+"'");
		// 确认物主
		if(rowSet.next())
		{
			int type = rowSet.getInt("type");
			if(type == 101 || type == 102 || type == 103 || type == 2 || type == 3 || type == 4|| type==5 || type == 6|| type ==7)
			{
				String name = rowSet.getString("name");
				int star = rowSet.getInt("star");
				int life = rowSet.getInt("life");
				
				if(life > Constant.life - Constant.life / 3)
				{
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("code", 201);
					jsonObject.put("message", "客官，您的【"+name+"】还不算太残破，可以再将就用一段时间再来维修。");
					return jsonObject.toString();
				}
				
				if(star > 0)
				{
					// 费用
					int cost = (Constant.life - life)*2 ;
					
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("code", 200);
					jsonObject.put("message", "【 "+name+" 】 【 "+star+" 星】 维修好需要花费: "+cost+"个铜板，并且扣除一颗星(当星级为0时无法再维修)，是否要维修？");
					return jsonObject.toString();
				}
				else
				{
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("code", 201);
					jsonObject.put("message", "很遗憾，你的【"+name+"】已经残破不堪，无法维修了。");
					return jsonObject.toString();
				}
			}
			else
			{
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "这个东西不需要维修");
				return jsonObject.toString();
			}
		}
		else
		{
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "未查询到你拥有这个物品");
			return jsonObject.toString();
		}
	}

	public String repairconfirm(String uuid, String gid)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from goods where masterid='"+uuid+"' and gid='"+gid+"'");
		// 确认物主
		if(rowSet.next())
		{
			int type = rowSet.getInt("type");
			if(type == 101 || type == 102 || type == 103 || type == 2 || type == 3 || type == 4|| type==5 || type == 6|| type ==7)
			{
				String name = rowSet.getString("name");
				int star = rowSet.getInt("star");
				int life = rowSet.getInt("life");
				
				if(star > 0)
				{
					long money = jdbcTemplate.queryForLong("select money from person where uuid='"+uuid+"'");
					
					// 费用
					int cost = (Constant.life - life)*2 ;
					
					if(money >= cost)
					{
						jdbcTemplate.update("update person set money=money-"+cost+" where uuid='"+uuid+"'");
						jdbcTemplate.update("update goods set star= star-1,life = "+Constant.life+" where gid='"+gid+"' and masterid='"+uuid+"'");
						net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
						jsonObject.put("code", 200);
						jsonObject.put("message", "【"+name+"】 完全已经维修好，看上去跟新的一样");
						return jsonObject.toString();
					}
					else
					{
						net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
						jsonObject.put("code", 201);
						jsonObject.put("message", "铁匠铺老板大喝到“没钱还修什么！出去！”，把你轰出了铁匠铺。");
						return jsonObject.toString();
					}
				}
				else
				{
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("code", 201);
					jsonObject.put("message", "很遗憾，你的【"+name+"】已经残破不堪，无法维修了。");
					return jsonObject.toString();
				}

			}
			else
			{
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "这个东西不需要维修");
				return jsonObject.toString();
			}
		}
		else
		{
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "未查询到你拥有这个物品");
			return jsonObject.toString();
		}
	}

	
	// 槲寄生戒指 兑换
	public String duihuan(String uuid)
	{
		int count = jdbcTemplate.queryForInt("select count(1) from goods where masterid='"+uuid+"' and gtype=330");
		if(count >= 99)
		{
			// 检查行囊的可容数量    and gtype=(select gtype from goods where gid='"+gid+"'则为特殊的物品诸如材料宝石等
			SqlRowSet countSet = jdbcTemplate.queryForRowSet("select COUNT(name) as count from goods where masterid='"+uuid+"' and state=2 and gtype<281");
			
			if(countSet.next())
			{
				int countx = countSet.getInt("count");
				if(countx >= Constant.count)
				{
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("code", 201);
					jsonObject.put("message", "你的行囊已满，无法存放戒指！");
					return jsonObject.toString();
				}
			}
			
			SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select name,type,descript,gtype,price from goods_system where name='槲寄生戒指'");
			if(rowSet.next())
			{
				String descript = rowSet.getString("descript");
				String name = rowSet.getString("name");
				int gtype = rowSet.getInt("gtype");
				int type = rowSet.getInt("type");
				long price = rowSet.getInt("price");
				
				jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and gtype=330 limit 99");
				
				String gid = UUID.randomUUID().toString().replaceAll("-", "");
				String sql = "INSERT INTO goods (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`) VALUES ('"+gid+"', '"+name+"', '"+uuid+"', '"+price+"', '"+Constant.life+"', '2', '0', '0', '0', '0', '"+type+"', '3', '"+descript+"',0, '"+gtype+"');";
				jdbcTemplate.update(sql);
				
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 200);
				
				jsonObject.put("message", "恭喜你集齐了灵玉，【槲寄生戒指】已经成功发放到你的行囊，快去查看下吧！");
				return jsonObject.toString();
			}		
		}
		net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
		jsonObject.put("code", 201);
		jsonObject.put("message", "你还没有收集到足够的温芸灵玉("+count+"/99)（收集到99个温芸灵玉即可兑换一枚【槲寄生戒指】）");
		return jsonObject.toString();
	}

	public String repairbyxlbs(String uuid, String gid)
	{
		String sql = "select name,(select COUNT(1) from goods where masterid='"+uuid+"' and gtype=334 and state=2) as baoshicount,(select COUNT(1) from goods where masterid='"+uuid+"' and gid='"+gid+"' and state=2) as goodcount from goods where masterid='"+uuid+"' and gid='"+gid+"' limit 1";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
		if(rowSet.next())
		{
			String name = rowSet.getString("name");
			int baoshicount = rowSet.getInt("baoshicount");
			int goodcount   = rowSet.getInt("goodcount");
			
			if(baoshicount < 1)
			{
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "你身上并没有续灵宝石");
				return jsonObject.toString();
			}
			
			if(goodcount != 1)
			{
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "你身上并没有这个物品，或数据异常！");
				return jsonObject.toString();
			}
			jdbcTemplate.update("delete from goods where gtype=334 and masterid='"+uuid+"' limit 1 ");
			jdbcTemplate.update("update goods set life=life+500 where masterid='"+uuid+"' and gid='"+gid+"'");
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "你成功使用了续灵宝石，为["+name+"]增加了500耐久值。");
			return jsonObject.toString();
		}
		return null;
	}
}
