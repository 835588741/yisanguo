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

import com.springmvc.util.NumberConverUtil;

@Component
public class TransactionDao
{
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/** 提交私人交易请求*/
	public String transaction(String seller_uuid, String buyer_uuid, String gid, String price)
	{
		// 先检查该玩家是否有该物品
		SqlRowSet queryIsNull = jdbcTemplate.queryForRowSet("select name,type from goods where masterid='"+seller_uuid+"' and gid='"+gid+"' and state=2");
		if(queryIsNull.next())
		{
			int type = queryIsNull.getInt("type");
			if(type == 9 || type == 10 || type == 11)
			{
				JSONObject jsonObject = new JSONObject();
				try
				{
					jsonObject.put("code", 202);
					jsonObject.put("message", "你484傻呀,不能拿金钱类作为交易物品!!!");
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return jsonObject.toString();
			}
			
			SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select tid from transaction_list where buyer_uuid='"+buyer_uuid+"' and gid='"+gid+"'");
			if(rowSet.next())
			{
				JSONObject jsonObject = new JSONObject();
				try
				{
					jsonObject.put("code", 202);
					jsonObject.put("message", "这个物品的交易请求已经提交给对方了，不要重复提交。");
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return jsonObject.toString();
			}
			
			// 测查是否恶意骚扰，多次提交交易给对方
			int transtioncount = jdbcTemplate.queryForInt("select count(1) from transaction_list where buyer_uuid='"+buyer_uuid+"' and seller_uuid='"+seller_uuid+"'");
			if(transtioncount >= 4)
			{
				JSONObject jsonObject = new JSONObject();
				try
				{
					long money = jdbcTemplate.queryForLong("select money from person where uuid='"+seller_uuid+"'");
					if(money > 5000)
						jdbcTemplate.update("update person set money=money-5000 where uuid='"+seller_uuid+"'");
					else
						jdbcTemplate.update("update person set money=money-"+money+" where uuid='"+seller_uuid+"'");
					
					jsonObject.put("code", 202);
					jsonObject.put("message", "你的当前行为已经构成交易骚扰，本次扣除5两银子。 (提示:如果对方未同意交易，5分钟内请勿多次向对方提交交易请求，对同一玩家未同意交易的订单>=4次将被系统视为骚扰扣除5两银子做为惩罚)");
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return jsonObject.toString();
			}
			
			String tid = UUID.randomUUID().toString().replaceAll("-", "");
			// 插入交易列表中
			//System.out.println("url:"+"insert into transaction_list(tid,seller_uuid,buyer_uuid,gid,price,isvalid,create_time,seller_name,gname) values('"+tid+"','"+seller_uuid+"','"+buyer_uuid+"','"+gid+"',"+price+",'true',UNIX_TIMESTAMP(NOW()),select name from person where uuid='"+seller_uuid+"',select name from goods where gid='"+gid+"'"+")");
			jdbcTemplate.update("insert into transaction_list(tid,seller_uuid,buyer_uuid,gid,price,isvalid,create_time,seller_name,gname) values('"+tid+"','"+seller_uuid+"','"+buyer_uuid+"','"+gid+"',"+price+",'true',UNIX_TIMESTAMP(NOW()),(select name from person where uuid='"+seller_uuid+"'),(select name from goods where gid='"+gid+"'"+"))");
			JSONObject jsonObject = new JSONObject();
			try
			{
				jsonObject.put("code", 200);
				jsonObject.put("message", "你的交易请求已经提交给对方 (提示:如果对方未同意交易，5分钟内请勿多次向对方提交交易请求，对同一玩家未同意交易的订单>=4次将被系统视为骚扰扣除5两银子做为惩罚)");
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
				jsonObject.put("message", "你没有这件物品!");
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jsonObject.toString();
		}
	}
	
	@ResponseBody
	public Object getTransactionList(String buyer_uuid)
	{
		String sql = "select tid,gid,price,seller_name,gname from transaction_list where buyer_uuid='"+buyer_uuid+"' and isvalid='true'";
		return jdbcTemplate.queryForList(sql);
	}

	// 同意交易
	public String agree(String tid, String buyer_uuid)
	{
		// 验证确实存在该交易请求且该记录还有效
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from transaction_list where tid='"+tid+"' and buyer_uuid='"+buyer_uuid+"' and isvalid='true'");
		if(rowSet.next())
		{
			String seller_uuid = rowSet.getString("seller_uuid");
			String gid = rowSet.getString("gid");
			String gname = rowSet.getString("gname");
			// 白银为单位，最低一两白银
			long price = rowSet.getLong("price");
			
			// 1.验证该物品是否还在卖家身上
			SqlRowSet valid = jdbcTemplate.queryForRowSet("select name,gtype from goods where masterid='"+seller_uuid+"' and state=2 and gid='"+gid+"'");
			if(valid.next())
			{
				int gtype = valid.getInt("gtype");
				
				// 验证买家身上是否有这么多银子
				SqlRowSet rowSetValidIsMoneyEnough = jdbcTemplate.queryForRowSet("select money,name from person where uuid='"+buyer_uuid+"'");
				if(rowSetValidIsMoneyEnough.next())
				{
					String name = rowSetValidIsMoneyEnough.getString("name");
					long money = rowSetValidIsMoneyEnough.getLong("money");
					money = money / 1000;
					
					// 不够支付
					if(money < price)
					{
						JSONObject jsonObject = new JSONObject();
						try
						{
							sendSystemMessage(seller_uuid, "对方并没有那么多钱可以买下你的物品");
							jsonObject.put("code", 201);
							jsonObject.put("message", "你没有那么多钱!");
							return jsonObject.toString();
						}
						catch (JSONException e)
						{
							e.printStackTrace();
						}
						return jsonObject.toString();
					}
					else
					{
//						// 同意交易材料
//						if(gtype >= 281 && gtype <= 331)
//						{
//							// 卖家身上材料总个数
//							int sellerc = jdbcTemplate.queryForInt("select count(1) from goods where masterid='"+seller_uuid+"' and gtype="+gtype+" and state=2");
//							// 如果个数多于1个，则丢弃隐藏的，并把显示的数量减一
//							if(sellerc > 1)
//							{
//								// 买家身上相同类型材料个数
//								int buyerc = jdbcTemplate.queryForInt("select count(1) from goods where masterid='"+buyer_uuid+"' and gtype="+gtype+" and state=2");
//								
//								// 卖家身上减一 
//								jdbcTemplate.update("update goods set count="+(sellerc-1)+" where masterid='"+seller_uuid+"' and gid='"+gid+"' and visibility=1");//count=count-1
//								
//								// 买家身上的材料也大于1
//								if(buyerc >= 1)
//								{
//									// 买家身上加一
//									jdbcTemplate.update("update goods set count="+(buyerc+1)+" where masterid='"+buyer_uuid+"' and gtype='"+gtype+"' and visibility=1");//count=count-1
//									// 物主变换
//									jdbcTemplate.update("update goods set masterid='"+buyer_uuid+"' where visibility=0 and gtype="+gtype+" and masterid='"+seller_uuid+"' limit 1");
//								}
//								else
//								{
//									// 如果小于1则可以直接变换物主来达到交易
//									jdbcTemplate.update("update goods set masterid='"+buyer_uuid+"',visibility=1 where visibility=0 and gtype="+gtype+" and masterid='"+seller_uuid+"' limit 1");
//								}
//							}
//							else
//							{
//								// 买家身上相同类型材料个数
//								int buyerc = jdbcTemplate.queryForInt("select count(1) from goods where masterid='"+buyer_uuid+"' and gtype="+gtype+" and state=2");
//
//								if(buyerc >= 1)
//								{
//									// 买家身上加一
//									jdbcTemplate.update("update goods set count="+(buyerc+1)+" where masterid='"+buyer_uuid+"' and gid='"+gid+"' and visibility=1");//count=count-1
//									jdbcTemplate.update("update goods set masterid='"+buyer_uuid+"' where visibility=0 and gtype="+gtype+" and masterid='"+seller_uuid+"' limit 1");
//								}
//								else
//								{
//									jdbcTemplate.update("update goods set masterid='"+buyer_uuid+"',visibility=1 where visibility=0 and gtype="+gtype+" and masterid='"+seller_uuid+"' limit 1");
//								}
//							}
//								//jdbcTemplate.update("delete from goods where where gtype="+gtype+" and masterid='"+buyer_uuid+"'");
//						}
//						// 其他物品
//						else
						{
							// 把物品主人id更变为买家的uuid
							jdbcTemplate.update("update goods set masterid='"+buyer_uuid+"' where masterid='"+seller_uuid+"' and gid='"+gid+"'");
						}
						
						// 把价格转为铜板数额增加到卖家账号
						jdbcTemplate.update("update person set money=money+"+price*1000+" where uuid='"+seller_uuid+"'");
						
						// 把价格转为铜板数额从买家账号减除
						jdbcTemplate.update("update person set money=money-"+price*1000+" where uuid='"+buyer_uuid+"'");
						// 删除这条交易请求记录
						jdbcTemplate.update("delete from transaction_list where tid='"+tid+"' and buyer_uuid='"+buyer_uuid+"'");
						
						sendSystemMessage(seller_uuid, "["+name+"] 同意你的交易请求，你的 ["+gname+"] 已经以 ["+price+" 两银子] 卖出了。");
						
						JSONObject jsonObject = new JSONObject();
						try
						{
							jsonObject.put("code", 200);
							jsonObject.put("message", "交易成功!");
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
						jsonObject.put("message", "交易异常!");
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
					jsonObject.put("message", "对方身上已经没有这件物品，交易失败!");
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
				jsonObject.put("message", "该交易已失效!");
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jsonObject.toString();
		}
	}

	
	public String refuse(String tid, String buyer_uuid)
	{
		// 验证确实存在该交易请求且该记录还有效
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from transaction_list where tid='"+tid+"' and buyer_uuid='"+buyer_uuid+"' and isvalid='true'");
		if(rowSet.next())
		{
			String sellerUUID = rowSet.getString("seller_uuid"); 
			jdbcTemplate.update("delete from transaction_list where tid='"+tid+"' and buyer_uuid='"+buyer_uuid+"'");
			
			JSONObject jsonObject = new JSONObject();
			try
			{
				sendSystemMessage(sellerUUID, "对方愤怒地拒绝了你的交易请求。");
				
				jsonObject.put("code", 200);
				jsonObject.put("message", "已拒绝交易!");
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jsonObject.toString();
		}
		JSONObject jsonObject = new JSONObject();
		try
		{
			jsonObject.put("code", 201);
			jsonObject.put("message", "该交易已经无效!");
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	// 黑市挂售
	public String sell(String seller_uuid, String gid, String price)
	{
		// 先检查该玩家是否有该物品
		SqlRowSet queryIsNull = jdbcTemplate.queryForRowSet("select ggrade,name,type,life,addhp,addattack,adddefence,adddexterous,descript,star from goods where masterid='"+seller_uuid+"' and gid='"+gid+"' and state=2");
		if(queryIsNull.next())
		{
			// 限制没人只能挂售10件
			SqlRowSet valid = jdbcTemplate.queryForRowSet("select count(*) from black_market_goods_list where masterid='"+seller_uuid+"'");
			if(valid.next())
			{
				int count = valid.getInt("count(*)");
				if(count > 10)
				{
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("code", 201);
					jsonObject.put("message", "每个人最多只能同时挂售十件物品!");
					return jsonObject.toString();
				}
			}
			
			String descript = queryIsNull.getString("descript");
			String name = queryIsNull.getString("name");
			int life = queryIsNull.getInt("life");
			int addhp = queryIsNull.getInt("addhp");
			int addattack = queryIsNull.getInt("addattack");
			int adddefence = queryIsNull.getInt("adddefence");
			int adddexterous = queryIsNull.getInt("adddexterous");
			int type = queryIsNull.getInt("type");
			int ggrade = queryIsNull.getInt("ggrade");
			int star = queryIsNull.getInt("star");
			
			String tid = UUID.randomUUID().toString().replaceAll("-", "");
			jdbcTemplate.update("insert into black_market_goods_list(tid,masterid,gid,price,type,life,addhp,addattack,adddefence,adddexterous,ggrade,descript,name,star) values('"+tid+"','"+seller_uuid+"','"+gid+"',"+price+","+type+","+life+","+addhp+","+addattack+","+adddefence+","+adddexterous+","+ggrade+",'"+descript+"','"+name+"',"+star+")");
			jdbcTemplate.update("update goods set state=6 where gid='"+gid+"' and masterid='"+seller_uuid+"'");
			
			JSONObject jsonObject = new JSONObject();
			try
			{
				jsonObject.put("code", 200);
				jsonObject.put("message", "委托成功，交易成功后会以私信通知你!");
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
				jsonObject.put("message", "你没有这件物品!");
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jsonObject.toString();
		}
	}

	// 黑市取回 取消挂售 http://192.168.245.1:8080/springMVC/requestTransaction/sell.action?&seller_uuid=0001&gid=58354ce34c744859829d7e71df4250aa&price=10
	public String cancelsell(String seller_uuid, String gid)
	{
		// 先检查该玩家是否是物主
		SqlRowSet queryIsNull = jdbcTemplate.queryForRowSet("select COUNT(name) as count,name,type from goods where masterid='"+seller_uuid+"' and gid='"+gid+"' and state=6 and gtype < 281");
		if(queryIsNull.next())
		{
			int count = queryIsNull.getInt("count");
			if(count < 20)
			{
				// 删除黑市记录
				jdbcTemplate.update("delete from black_market_goods_list where gid='"+gid+"' and masterid='"+seller_uuid+"'");
				// 状态变回在行囊
				jdbcTemplate.update("update goods set state=2 where gid='"+gid+"' and masterid='"+seller_uuid+"'");
				
				JSONObject jsonObject = new JSONObject();
				try
				{
					jsonObject.put("code", 200);
					jsonObject.put("message", "成功取回该物品!");
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
		else
		{
			JSONObject jsonObject = new JSONObject();
			try
			{
				jsonObject.put("code", 201);
				jsonObject.put("message", "物主身份确认失败，请勿使用第三方外挂!");
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jsonObject.toString();
		}
	}


	// 物品列表
	public String getallsell(String start, String size, String type)
	{
		try
		{
			String url;
			if("7".equals(type))
				url = "select name,ggrade,tid,masterid,gid,price,star,(select name from person where uuid=masterid) as mastername,life,addhp,addattack,adddefence,adddexterous,descript from black_market_goods_list where type=7 or type=8 limit "+start+","+size;
			else
				url = "select name,ggrade,tid,masterid,gid,price,star,(select name from person where uuid=masterid) as mastername,life,addhp,addattack,adddefence,adddexterous,descript from black_market_goods_list where type="+type+" limit "+start+","+size;
			List<Map<String, Object>> list = jdbcTemplate.queryForList(url);
			
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 200);
			jsonObject.put("message", "获取列表成功!");
			jsonObject.put("data", list);
			return jsonObject.toString();
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		
		net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
		jsonObject.put("code", 201);
		jsonObject.put("message", "获取列表失败!");
		return jsonObject.toString();
	}
	
	// 我的挂售物品列表
	public String getMySell(String uuid)
	{
		try
		{
			SqlRowSet valid = jdbcTemplate.queryForRowSet("select tid from black_market_goods_list where masterid='"+uuid+"'");
			if(valid.next())
			{
				List<Map<String, Object>> list = jdbcTemplate.queryForList("select name,ggrade,tid,masterid,gid,price,(select name from person where uuid=masterid) as mastername,life,addhp,addattack,adddefence,adddexterous,descript from black_market_goods_list where masterid='"+uuid+"'");
				
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 200);
				jsonObject.put("message", "获取列表成功!");
				jsonObject.put("data", list);
				return jsonObject.toString();
			}
			else
			{
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "你没有挂售的物品!");
				return jsonObject.toString();
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		
		net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
		jsonObject.put("code", 201);
		jsonObject.put("message", "获取列表失败!");
		return jsonObject.toString();
	}


	// 购买物品
	public String buy(String tid, String gid, String buyer_uuid)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select masterid,price,name from black_market_goods_list where tid='"+tid+"' and gid='"+gid+"'");
		if(rowSet.next())
		{
			String masterid = rowSet.getString("masterid");
			long price = rowSet.getLong("price");
			String name = rowSet.getString("name");
			
			// 本人自取
			if(masterid.equals(buyer_uuid))
			{
				// 删除黑市记录
				jdbcTemplate.update("delete from black_market_goods_list where gid='"+gid+"' and tid='"+tid+"'");
				jdbcTemplate.update("update goods set state=2,masterid='"+buyer_uuid+"' where gid='"+gid+"'");
				
				JSONObject jsonObject = new JSONObject();
				try
				{
					jsonObject.put("code", 202);
					jsonObject.put("message", "你从黑市取回了自己的 ["+name+"]!");
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return jsonObject.toString();
			}
			
			// 0 step (检验买家的财产是否够支付) 
			SqlRowSet validIsEnough = jdbcTemplate.queryForRowSet("select money,name from person where uuid='"+buyer_uuid+"'");
			if(validIsEnough.next())
			{
				String buyer_name = validIsEnough.getString("name");
				
				// 获取总财产数 并转白银计量
				Long money = validIsEnough.getLong("money");
				money = money / 1000;
				
				// 不够支付
				if(money < price)
				{
					JSONObject jsonObject = new JSONObject();
					try
					{
						jsonObject.put("code", 201);
						jsonObject.put("message", "你这穷逼，钱都不够还敢大摇大摆学人家来黑市买东西!再不走我叫人来打你了!");
						return jsonObject.toString();
					}
					catch (JSONException e)
					{
						e.printStackTrace();
					}
					return jsonObject.toString();
				}
				
				// 1 step 从黑市移除物品信息
				// 2 step 费用从买家扣除
				// 3 step 费用加到卖家身上 
				// 3 step 物主和状态更新
				// 删除黑市记录
				jdbcTemplate.update("delete from black_market_goods_list where gid='"+gid+"' and tid='"+tid+"'");
				jdbcTemplate.update("update person set money=money-"+(price*1000)+" where uuid='"+buyer_uuid+"'");
				
				long temp = price;
				long yongjin = 1;
				// 扣除佣金
				if(price > 10)
				{
					price -= price / 10;
					yongjin = price / 10;
				}
				else
				{
					price -= 1;
				}
				jdbcTemplate.update("update person set money=money+"+(price*1000)+" where uuid='"+masterid+"'");
				jdbcTemplate.update("update goods set state=2,masterid='"+buyer_uuid+"' where gid='"+gid+"'");
				
				sendSystemMessage(masterid, "你在黑市挂售的 ["+name+"] 被 ["+buyer_name+"] 以 ["+temp+"两白银] 买下了,扣除佣金 ["+yongjin+"两白银] ,剩余 ["+price+"两白银存入你账上了]");
				
				JSONObject jsonObject = new JSONObject();
				try
				{
					jsonObject.put("code", 200);
					jsonObject.put("message", "你购买成功!已放入行囊");
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
					jsonObject.put("message", "购买失败!未查询到您的用户信息!");
					return jsonObject.toString();
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
				jsonObject.put("message", "对方身上已经不存在该物品!");
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jsonObject.toString();
		}
	}
	
	public Object sendSystemMessage(String receiver_uuid, String keyword)
	{
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 发系统消息提示
		String mid = UUID.randomUUID().toString().replaceAll("-", "");
		String notiMassgae = "insert into message(mid,senderid,receiveid,message,sendtime,readflag,sendername,ordertime,type) "
				+ "values("+ "'"+ mid+ "','0000','"+receiver_uuid+"','"+keyword+"','"+sdf.format(new Date(System.currentTimeMillis()))+"','false','交易消息',"+System.currentTimeMillis()+",2)";
		jdbcTemplate.update(notiMassgae);
		return null;
	}

	// 领工资
	public String receivesalary(String uuid, String country)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select name,grade,position,positiongrade,countryid,(UNIX_TIMESTAMP() - lasttaketime) as timediff from person where uuid='"+uuid+"'");
		if(rowSet.next())
		{
			String countryString = rowSet.getString("countryid");
			String position = rowSet.getString("position");
			String name = rowSet.getString("name");
			int positiongrade = rowSet.getInt("positiongrade");
			long timediff = rowSet.getLong("timediff");
			
			// 6
			if(timediff < 86400)
			{
				timediff = 86400-timediff;
				String time = "还需等待-> "+timediff/60/60+" 小时: "+timediff/60%60+" 分: "+" 秒: "+timediff%60;
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 200);
				if(positiongrade == 1 || positiongrade == 0)
				{
					jsonObject.put("message", "'你这刁民！发饷时间还没到又想来领！'，军需官对你大声呵斥，吓得你退到墙角。\n("+time+")");
				}
				else if(positiongrade > 1 && positiongrade < 5) 
				{
					jsonObject.put("message", "'你一个小小"+position+"也敢在本官面前放肆不成？'，军需官不耐烦地抬头怒视你一眼，你已是战战兢兢的，不觉后退。\n("+time+")");
				}
				else if(positiongrade >= 5 && positiongrade < 10)
				{
					jsonObject.put("message", "'耐心再等一会，还没到领饷银的时间呢'，军需官温和说到。\n("+time+")");
				}
				else if(positiongrade >= 10 && positiongrade < 20)
				{
					jsonObject.put("message", "'"+name+"大人'，就别为难区区下官了，您还没到领饷时间'，军需官一面惶恐，似乎很怕你因此责罚他。\n("+time+")");
				}
				else if(positiongrade >= 20 && positiongrade<30)
				{
					jsonObject.put("'下官给大人请安，大人请上座歇息一会'，军需官对你毕恭毕敬，诚惶诚恐，把你扶上自己的座位，自己站立旁边侍候。", "\n("+time+")");
				}
				else if(positiongrade >= 30)
				{
					jsonObject.put("'哎呀，我的大人您怎么亲自来了，请上座吧，等到了发饷时间我立刻通知您'，军需官对你毕恭毕敬，把你扶上自己的座位，自己躬身站立旁边侍候。", "\n("+time+")");
				}
				return jsonObject.toString();
			}
			
			
			// 判断战胜还是战败国
			int success = jdbcTemplate.queryForInt("select success from country where countryid='"+country+"'");
			int grade = rowSet.getInt("grade");
			 
			if(!country.equals(countryString))
			{
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "非本国人员，竟敢冒领军饷！小心打断你的腿!");
				return jsonObject.toString();
			}
			else
			{
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				
				// 战胜
				if(success == 1)
				{
					int number = (int) (grade*150+positiongrade*500);
					jdbcTemplate.update("update person set money=money+"+number+",lasttaketime=UNIX_TIMESTAMP() where uuid='"+uuid+"'");
					int positionexp = 1;
					if(grade >= 30)
					{
						positionexp = grade / 50 < 0 ? 1:grade / 50;
						jdbcTemplate.update("update person set positionexp=positionexp+"+positionexp+" where uuid='"+uuid+"'");
					}
					
					jsonObject.put("code", 200);
					jsonObject.put("message", "你成功领取了["+NumberConverUtil.getNameByMoney(number)+"]，其中因为本国是战胜国有官府额外奖励 ["+NumberConverUtil.getNameByMoney(grade*50)+"]"+(grade >= 30?"，功勋奖励 [ "+positionexp+" ] 点(限35级含以上)":""));
				}
				// 战败
				else if(success == 2)
				{
					int number = (int) (grade*50+positiongrade*250);
					jdbcTemplate.update("update person set money=money+"+number+",lasttaketime=UNIX_TIMESTAMP() where uuid='"+uuid+"'");
					jsonObject.put("code", 200);
					jsonObject.put("message", "你只能领取["+NumberConverUtil.getNameByMoney(number)+"],因为本国战败需要赔款,所以你的饷银被扣除了 ["+NumberConverUtil.getNameByMoney(grade * 100+positiongrade*250)+"]");
				}
				// 普通无战事
				else
				{
					int number = (int) (grade*100+positiongrade*500);
					jdbcTemplate.update("update person set money=money+"+number+",lasttaketime=UNIX_TIMESTAMP() where uuid='"+uuid+"'");
					jsonObject.put("code", 200);
					jsonObject.put("message", "你成功领取了["+NumberConverUtil.getNameByMoney(number)+"]");
				}
				return jsonObject.toString();
			}
		}
		else
		{
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "没有查询到你的用户信息");
			return jsonObject.toString();
		}
	}

	
	public String donatemoney(String countryid, String uuid, long money)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select money,countryid,positiongrade from person where uuid='"+uuid+"'");
		if(rowSet.next())
		{
			long yz = rowSet.getLong("money");
			yz = yz / 1000;
			String userCountry = rowSet.getString("countryid");
			int positiongrade = rowSet.getInt("positiongrade");
			if(userCountry.equals(countryid))
			{
				if(money > yz)
				{
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("code", 201);
					jsonObject.put("message", "你身上的钱不足以捐助，府库主管一脸鄙视地看了你一眼");
					return jsonObject.toString();
				}
				// 只有爵位等级小于40以下才能靠捐助来获取功勋
				if(positiongrade >= 27)
				{
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("code", 201);
					jsonObject.put("message", "你当前爵位已经超过了捐助获取功勋的最高限制，捐钱不会再获得功勋，只能靠真枪实刀的打拼获取功勋了。");
					return jsonObject.toString();
				}
//				80 + 25 + 53 = 105+53
				jdbcTemplate.update("update person set money=money-"+(money*1000)+" where uuid='"+uuid+"'");
				// 增加功勋
				int value = (int) (money / 10);
				upposition(uuid, value);
				
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 200);
				jsonObject.put("message", "你捐助了 ["+money+" 两银子] 获得官府奖励功勋 ["+value+" 点] 注:捐助小于十两不会获得功勋");
				return jsonObject.toString();
			}
			else
			{
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "你不是本国公民，捐助资金也不会获得功勋爵位。");
				return jsonObject.toString();
			}
		}
		else
		{
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "未查询到你的用户信息");
			return jsonObject.toString();
		}
	}
	
	public void upposition(String uuid,int value)
	{
		//positiongrade*24+POW(positiongrade,2.2)
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select positionexp,position,positiongrade,nextpositionexp from person where uuid='"+uuid+"'");
		if(rowSet.next())
		{
			long positionexp = rowSet.getLong("positionexp");
			long nextpositionexp = rowSet.getLong("nextpositionexp");
			int positiongrade = rowSet.getInt("positiongrade");
			
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
				}
			}
			else
			{
				jdbcTemplate.update("update person set positionexp=positionexp+"+value+" where uuid='"+uuid+"'");
			}
		}
	}
}
