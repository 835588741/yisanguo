package com.springmvc.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springmvc.util.MySQLOprea;

@Controller
@RequestMapping("requestAction")
public class RequestAction
{
	@Autowired
	private MySQLOprea oprea;
	
	// set or update sign 签名
	@RequestMapping(value = "sign.action",method = RequestMethod.GET)
	public void sign(HttpServletResponse response,String uuid,String sign) throws IOException
	{
		sign = new String(sign.getBytes("ISO-8859-1"),"utf-8");
		String json = oprea.sign(uuid,sign);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}

	// 删除好友
	@RequestMapping(value = "deletefriend.action",method = RequestMethod.GET)
	public void deleteFriend(HttpServletResponse response,String fuuid,String buuid) throws IOException
	{ 
		String json = oprea.deleteFriend(fuuid,buuid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 添加好友
	@RequestMapping(value = "addfriend.action",method = RequestMethod.GET)
	public void addFriend(HttpServletResponse response,String fuuid,String buuid) throws IOException
	{
		String json = oprea.addFriend(fuuid,buuid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 获取好友列表
	@RequestMapping(value = "getfriendlist.action",method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getFriendList(HttpServletResponse response,String uuid) throws IOException
	{
		List<Map<String, Object>> json = oprea.getFriendList(uuid);
		return json;
	}
	
	// task check 任务完成度检查
	@RequestMapping(value = "taskcheck.action",method = RequestMethod.GET)
	public void taskcheck(HttpServletResponse response,String taskmasteruuid,String taskreceiveruuid) throws IOException
	{
		String json = oprea.taskcheck(taskmasteruuid,taskreceiveruuid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// task check 获取任务状态
	@RequestMapping(value = "gettaskstate.action",method = RequestMethod.GET)
	public void getTaskState(HttpServletResponse response,String taskreceiveruuid) throws IOException
	{
		String json = oprea.getTaskState(taskreceiveruuid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// task check 返回连续随机任务的状态
	@RequestMapping(value = "getdailytaskstate.action",method = RequestMethod.GET)
	public void getDailyTaskState(HttpServletResponse response,String taskreceiveruuid,int type) throws IOException
	{
		String json = oprea.getDailyTaskState(taskreceiveruuid,type);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// task check 更换连续随机任务
	@RequestMapping(value = "changetask.action",method = RequestMethod.GET)
	public void change(HttpServletResponse response,String uuid) throws IOException
	{
		String json = oprea.changeTask(uuid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 获取某个地区id的所有信息
	@RequestMapping(value = "getplaceinfoall.action",method = RequestMethod.GET)
	public void getPlaceInfoAll(HttpServletResponse response,String name,String curid,String id,String uuid,String type,String token) throws IOException
	{
		String json = oprea.queryPlaceAllInfo(name,curid,id,uuid,type,token);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 获取某个用户的信息
	@RequestMapping(value = "getuserinfo.action",method = RequestMethod.GET)
	public void getUserInfo(HttpServletResponse response,String uuid) throws IOException
	{
		String json = oprea.queryUserInfo(uuid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 加入某城
	@RequestMapping(value = "join.action",method = RequestMethod.GET)
	public void join(HttpServletResponse response,String uuid,String countryid) throws IOException
	{
		String json = oprea.join(uuid,countryid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 重置改用户的所有消息为已读
	@RequestMapping(value = "setmessagestate.action",method = RequestMethod.GET)
	public void resetMessageState(HttpServletResponse response,String uuid) throws IOException
	{
		oprea.updateMessageState(uuid);
	}
	
	// 住店休息 回血
	@RequestMapping(value = "rest.action",method = RequestMethod.GET)
	public void rest(HttpServletResponse response,String uuid) throws IOException
	{
		String json = oprea.rest(uuid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 解除战斗状态
	@RequestMapping(value = "releasebattle.action",method = RequestMethod.GET)
	public void releaseBattle(HttpServletResponse response,String uuid) throws IOException
	{
		String json = oprea.releaseBattle(uuid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}

	// 获取排序数据接口
	@RequestMapping(value = "ranking.action",method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getQueryData(HttpServletResponse response,String order) throws IOException
	{
		List<Map<String, Object>> json = oprea.queryRanking(order);
		return json;
	}
	
	// 通用查询接口
	@RequestMapping(value = "querydata.action",method = RequestMethod.GET)
	@ResponseBody
	public Object getQueryData(HttpServletResponse response,String table,String condition,String type,String page,String pagesize) throws IOException
	{
		Object json = oprea.query(table, condition, Integer.valueOf(type),page,pagesize);
		return json;
	}
	
	// 验证账号唯一性  {}|账号可用  
	@RequestMapping(value = "valid.action",method = RequestMethod.GET)
	public void isValidOfAccount(HttpServletResponse response,String account) throws IOException
	{
		String json = oprea.query("person", " account="+"'"+account+"'", 0).toString();
		// 空，无此账号或密码
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	/** battle 
	 * @throws IOException */
	@RequestMapping(value = "battle.action",method = RequestMethod.GET)
	public void battle(HttpServletResponse response,String attackerid,String defencerid,String type) throws IOException
	{
		String json = oprea.battle(attackerid, defencerid,type);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	/** go back  回城
	 * @throws IOException */
	@RequestMapping(value = "goback.action",method = RequestMethod.GET)
	public void goback(HttpServletResponse response,String uuid) throws IOException
	{
		String json = oprea.goback(uuid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	@RequestMapping(value = "diaoyu.action",method = RequestMethod.GET)
	public void diaoyu(HttpServletResponse response,String uuid,String type) throws IOException
	{
		String json = oprea.diaoyu(uuid,type);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
}
