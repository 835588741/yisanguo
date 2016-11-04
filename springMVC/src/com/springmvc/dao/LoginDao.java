package com.springmvc.dao;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springmvc.util.Constant;
import com.springmvc.util.SHA1;

@Component
public class LoginDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private String validateUserSql = "";
	
	public Object validateUser(String username,String password, String version) 
	{
		// 版本验证
		SqlRowSet validVersion = jdbcTemplate.queryForRowSet("select * from version");
		if(validVersion.next())
		{
			int versionService = validVersion.getInt("version");
			String content = validVersion.getString("content");
			if(!(versionService+"").equals(version))
			{
				//System.out.println("  versionService="+versionService+"   version="+version);
				JSONObject jsonObject = new JSONObject();
				try
				{
					jsonObject.put("code", 405);
					if(content == null)
						jsonObject.put("message", "客户端当前不是最新版本，请更新版本!也可加Q群:362595000（玄异三国）获取。");
					else
						jsonObject.put("message", content+"\n客户端当前不是最新版本，请更新版本!也可加Q群:362595000（玄异三国）获取。");
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return jsonObject.toString();
			}
		}
		
		validateUserSql = "select name,uuid,areaid,account,password,question,state from person where account='"+username+"' and password = '"+password+"'";
		try
		{
			Map<String, Object> map = jdbcTemplate.queryForMap(validateUserSql);
			
			if(map != null && !map.isEmpty())
			{
				int state = (Integer) map.get("state");
				String uuid = (String) map.get("uuid");
				
				// state : 1|在线  2|离线  0|死亡   1024|封号中
				if(state == 1 || state == 2)
				{
					// 更新最后一次动作时间,并恢复state=1在线状态  用于超时无动作下线功能
					jdbcTemplate.update("update person set last_action_time = UNIX_TIMESTAMP(),state=1,isonline='true' where uuid='"+uuid+"'");
					// 好友表同步更新
					jdbcTemplate.update("update friend set state = 1,last_time=UNIX_TIMESTAMP() where fuuid='"+uuid+"'");
					
					// 用于限制不能一个账号多少同时登录在线
					jdbcTemplate.update("delete from online where uuid='"+uuid+"'");
					String oid = UUID.randomUUID().toString().replaceAll("-", "");
					jdbcTemplate.update("insert into online(oid,uuid,time,time_str) values('"+oid+"','"+uuid+"',UNIX_TIMESTAMP(),now()"+")");
					map.put("token", oid);
					
					JSONObject jsonObject = new JSONObject();
					try
					{
						jsonObject.put("code", 200);
						jsonObject.put("message", "登录成功!");
						jsonObject.put("data", map);
//						System.out.println("JSON = "+jsonObject.toString());
					}
					catch (JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return jsonObject;
				}
				else if(state == 1024)
				{
					JSONObject jsonObject = new JSONObject();
					try
					{
						jsonObject.put("code", 202);
						jsonObject.put("message", "您的账号暂时被禁封!请联系管理员");
					}
					catch (JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return jsonObject;
				}
				else
				{
					JSONObject jsonObject = new JSONObject();
					try
					{
						jsonObject.put("code", 203);
						jsonObject.put("message", "出现异常，无法登陆!请联系管理员");
					}
					catch (JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return jsonObject;
				}
			}
			else
			{
				JSONObject jsonObject = new JSONObject();
				try
				{
					jsonObject.put("code", 201);
					jsonObject.put("message", "账号或密码错误，未找到您的账号信息!");
					jsonObject.put("data", map);
//					System.out.println("JSON = "+jsonObject.toString());
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		catch (EmptyResultDataAccessException e)
		{
			JSONObject jsonObject = new JSONObject();
			try
			{
				jsonObject.put("code", 201);
				jsonObject.put("message", "账号或密码错误！");
			}
			catch (JSONException e2)
			{
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			return jsonObject;
		}
		
		JSONObject jsonObject = new JSONObject();
		try
		{
			jsonObject.put("code", 203);
			jsonObject.put("message", "出现异常，无法登陆!请联系管理员");
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	/** 注册
	 * @param gender */
	@ResponseBody
	public String register(String account,String password,String niname, String gender)
	{
		try
		{
			SqlRowSet selectResult = jdbcTemplate.queryForRowSet("select name from person where account='"+account+"'");//query("person", " account="+"'"+account+"' and password="+"'"+password+"'", 0,"","");
			
			// 账号可用
			if(!selectResult.next())
			{
					niname = niname.replace(" ", "");
					if(niname == null || "".equals(niname.trim()))
					{
						JSONObject object = new JSONObject();
						object.put("code", 404);
						object.put("message", "不能使用空格作为名字!");
						return object.toString();
					}
					
					String[] cannotuse = new String[]{"艹","干","cnm","CNM","cao","ma","主管","B","b","13","贱","策划","群主","妓","设计","阉","鸡鸡","鸡巴","卵","洞","穴","管理","GM","逼","妈","操","你妈","尼妈","高潮","出水","淫","管理","强奸","乱伦","毛泽东","江泽民","邓小平","周恩来","习近平","温家宝","日","狗","猪","烂","玄","*","@","$","%","&"};
					for (int i = 0; i < cannotuse.length; i++)
					{
						if(niname.contains(cannotuse[i]))
						{
							JSONObject object = new JSONObject();
							object.put("code", 404);
							object.put("message", "注册失败！昵称中包含禁用字!");
							return object.toString();
						}
					}
					
					SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select name from person where name='"+niname+"'");
					if(rowSet.next())
					{
						JSONObject object = new JSONObject();
						object.put("code", 404);
						object.put("message", "这个名字已经有人使用了:"+niname);
						return object.toString();
					}
					
					String uuid = UUID.randomUUID().toString().replaceAll("-", "");
					String descriptInit = "你还只是个十三四岁的小孩童，天真的脸庞，那份纯真可爱的稚气显露无遗。";
					String registerStr = "insert into person(uuid,name,avatar,descript,maxhp,state,money,exp,grade,areaid,hp,position,nextgradeexp,positionexp,positiongrade,taskid,taskprogress,tasktarget,moneyhuangjin,moneybaiying,moneytongban,taskstate,attack,defence,dodge,password,account,countryid,countryname,isbattle,type,amount,dialog,gender) "
							+ "values("+"'"+uuid+"',"+"'"+niname+"',"+"'',"+"'"+descriptInit+"',"+Constant.initHp+",1,200,0,1,0,"+Constant.initHp+",'平民',46,0,0,1001,0,0,0,0,0,0,"+Constant.initAttack+","+Constant.initDefence+","+Constant.initDodge+","+"'"+password+"',"+"'"+account+"','0000','新手村','false',"+1+","+1+",'"+" "+"',"+gender+")";
					jdbcTemplate.execute(registerStr);
					JSONObject object = new JSONObject();
					object.put("code", 200);
					object.put("message", "注册成功!");
					return object.toString();
				}
				else
				{
					JSONObject object = new JSONObject();
					object.put("code", 404);
					object.put("message", "已经存在该账号!");
					return object.toString();
				}
		}
		catch (Exception e)
		{
			// TODO: handle exception
			System.out.println("register 异常"+e.toString());
		}
		return null;
	}

	public String getmibaostate(String uuid)
	{
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select ISNULL(question) as mbstate,question from person where uuid='"+uuid+"'");
		if(sqlRowSet.next())
		{
			int mbstate = sqlRowSet.getInt("mbstate");
			// 已有密保
			if(mbstate == 0)
			{
				String question = sqlRowSet.getString("question");
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 200);
				jsonObject.put("message", "请输入原有密保答案以及新的密保问题和答案。");
				jsonObject.put("title", "修改密保");
				jsonObject.put("question", question);
				return jsonObject.toString();
			}
			// 无密保
			else
			{
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "请设置您的密保并妥善保存，以便日后可以通过密保找回密码或修改密码。");
				jsonObject.put("title", "初始设置密保");
				return jsonObject.toString();
			}
		}
		else
		{
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 202);
			jsonObject.put("message", "未查询到您的用户信息！");
			return jsonObject.toString();
		}
	}

	public String settingmibao(String uuid,String question, String answer, String confirmanswer)
	{
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select ISNULL(question) as mbstate,answer,question from person where uuid='"+uuid+"'");
		if(sqlRowSet.next())
		{
			int mbstate = sqlRowSet.getInt("mbstate");
			String answerOld = sqlRowSet.getString("answer");
			
			// 已有密保
			if(mbstate == 0)
			{
				if(answerOld != null)
				{
					// 密保验证失败
					if(!answerOld.equals(confirmanswer))
					{
						net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
						jsonObject.put("code", 202);
						jsonObject.put("message", "原密保问题验证失败！");
						return jsonObject.toString();
					}
					// 验证成功，可以修改密保
					else
					{
						jdbcTemplate.update("update person set question='"+question+"',answer='"+answer+"' where uuid='"+uuid+"'");
						net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
						jsonObject.put("code", 202);
						jsonObject.put("message", "密保设定成功！请牢记您的密保答案:【"+answer+"】");
						return jsonObject.toString();
					}
				}
				else
				{
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("code", 202);
					jsonObject.put("message", "用户数据异常!请勿非法操作！");
					return jsonObject.toString();
				}
			}
			else
			{
				jdbcTemplate.update("update person set question='"+question+"',answer='"+answer+"' where uuid='"+uuid+"'");
				jdbcTemplate.update("update person set question='"+question+"',answer='"+answer+"' where uuid='"+uuid+"'");
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 202);
				jsonObject.put("message", "密保设定成功！请牢记您的密保答案:【"+answer+"】");
				return jsonObject.toString();
			}
		}
		return null;
	}

	public String modmima(String uuid, String newpwd, String oldpwd)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select password from person where uuid='"+uuid+"'");
		if(rowSet.next())
		{
			String password = rowSet.getString("password");
			if(!password.equals(oldpwd))
			{
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 202);
				jsonObject.put("message", "原密码错误！无法完成修改！");
				return jsonObject.toString();
			}
			else
			{
				jdbcTemplate.update("update person set password='"+newpwd+"' where uuid='"+uuid+"'");
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 200);
				jsonObject.put("message", "密码修改成功!请牢记您的密码！");
				return jsonObject.toString();
			}
		}
		else
		{
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 202);
			jsonObject.put("message", "未查询到您的用户信息!");
			return jsonObject.toString();
		}
	}

	public String resetmima(String account, String answer)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select answer from person where account='"+account+"'");
		if(rowSet.next())
		{
			String oldanswer = rowSet.getString("answer");
			if(!oldanswer.equals(answer))
			{
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("code", 202);
				jsonObject.put("message", "密保答案不正确!");
				return jsonObject.toString();
			}
			
			int[] pwd = new int[]{1,2,3,4,5,6,7,8,9};
			Random random = new Random();
			String newpwd = ""; 
			for (int i = 0; i < 6; i++)
			{
				int index = random.nextInt(pwd.length-1);
				newpwd += pwd[index];
			}
			
			SHA1 sha1 = new SHA1();
			String newpwdsha = sha1.getDigestOfString(newpwd.getBytes());
			
			jdbcTemplate.update("update person set password='"+newpwdsha+"' where account='"+account+"'");
			
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 200);
			jsonObject.put("message", "您的密码已经成功重置，您的新密码是:【 "+newpwd+" 】，请妥善保管。如需修改密码请在设置里面点击‘修改密码’。");
			return jsonObject.toString();
		}
		else
		{
			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			jsonObject.put("code", 202);
			jsonObject.put("message", "未查询到您的用户信息!");
			return jsonObject.toString();
		}
	}

	public String getuuid(String account, String password)
	{
		return null;
	}
}
