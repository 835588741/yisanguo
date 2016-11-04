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

import com.springmvc.dao.ThingDao;

@Controller
@RequestMapping("requestThings")
public class RequestThings
{
	@Autowired
	private ThingDao thingDao;
	
	// 寄存物品  type 1|单存   2|全部存入
	@RequestMapping(value = "deposit.action",method = RequestMethod.GET)
	public void deposit(HttpServletResponse response,String uuid,String gid,String type) throws IOException
	{
		String json = thingDao.deposit(uuid,gid,type);//oprea.query("place", "areaid = '"+id+"'", 0);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 装备维修
	@RequestMapping(value = "repair.action",method = RequestMethod.GET)
	public void repair(HttpServletResponse response,String uuid,String gid) throws IOException
	{
		String json = thingDao.repair(uuid,gid);//oprea.query("place", "areaid = '"+id+"'", 0);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 使用续灵宝石维修
	@RequestMapping(value = "repairbyxlbs.action",method = RequestMethod.GET)
	public void repairbyxlbs(HttpServletResponse response,String uuid,String gid) throws IOException
	{
		String json = thingDao.repairbyxlbs(uuid,gid);//oprea.query("place", "areaid = '"+id+"'", 0);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 装备维修 确认维修
	@RequestMapping(value = "repairconfirm.action",method = RequestMethod.GET)
	public void repairconfirm(HttpServletResponse response,String uuid,String gid) throws IOException
	{
		String json = thingDao.repairconfirm(uuid,gid);//oprea.query("place", "areaid = '"+id+"'", 0);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}

	
	// 购买
	@RequestMapping(value = "buything.action",method = RequestMethod.GET)
	public void buyThing(HttpServletResponse response,String uuid,String gid) throws IOException
	{
		String json = thingDao.buyThing(uuid,gid);//(title, content, posterid,postername);//(id,uuid);//oprea.query("place", "areaid = '"+id+"'", 0);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 出售物品
	@RequestMapping(value = "sell.action",method = RequestMethod.GET)
	public void sell(HttpServletResponse response,String uuid,String gid) throws IOException
	{
		String json = thingDao.sell(uuid,gid);//oprea.query("place", "areaid = '"+id+"'", 0);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 提取物品  type 1|单存   2|全部存入
	@RequestMapping(value = "take.action",method = RequestMethod.GET)
	public void take(HttpServletResponse response,String uuid,String gid,String type) throws IOException
	{
		String json = thingDao.take(uuid,gid,type);//oprea.query("place", "areaid = '"+id+"'", 0);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	
	// 捡起某个东西物品
	@RequestMapping(value = "pickup.action",method = RequestMethod.GET)
	public void pickUp(HttpServletResponse response,String uuid,String gid) throws IOException
	{
		String json = thingDao.pickUp(uuid,gid);//oprea.query("place", "areaid = '"+id+"'", 0);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 丢弃某个东西物品
	@RequestMapping(value = "throwthing.action",method = RequestMethod.GET)
	public void throwThing(HttpServletResponse response,String uuid,String gid,String areaid) throws IOException
	{
		String json = thingDao.throwThing(uuid,gid,areaid);//oprea.query("place", "areaid = '"+id+"'", 0);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 丢弃财务
	@RequestMapping(value = "throwmoney.action",method = RequestMethod.GET)
	public void throwMoney(HttpServletResponse response,String uuid,String gid,String number,String areaid) throws IOException
	{
		String json = thingDao.throwMoney(uuid,gid,number,areaid);//oprea.query("place", "areaid = '"+id+"'", 0);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	
	// 出售物品
	@RequestMapping(value = "getselllist.action",method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getselllist(HttpServletResponse response,String uuid) throws IOException
	{
		List<Map<String, Object>> json = thingDao.getSellList(uuid);//oprea.query("place", "areaid = '"+id+"'", 0);
		return json;
	}
	
	// 解除装备物品
	@RequestMapping(value = "disarmed.action",method = RequestMethod.GET)
	public void disarmed(HttpServletResponse response,String uuid,String gid) throws IOException
	{
		String json = thingDao.disarmed(uuid,gid);//(title, content, posterid,postername);//(id,uuid);//oprea.query("place", "areaid = '"+id+"'", 0);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 获取个人物品列表  type 1|已穿戴  2|随身物品
	@RequestMapping(value = "getgoods.action",method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getGoods(HttpServletResponse response,String masterid,String state) throws IOException
	{
		List<Map<String, Object>> json = thingDao.getGoods("goods", masterid, state);//query("goods", "masterid = '"+uuid+"'", 0);
		return json;
	}
	
	
	// 装备物品
	@RequestMapping(value = "armed.action",method = RequestMethod.GET)
	public void armed(HttpServletResponse response,String uuid,String gid) throws IOException
	{
		String json = thingDao.armed(uuid,gid);//(title, content, posterid,postername);//(id,uuid);//oprea.query("place", "areaid = '"+id+"'", 0);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 获取武器装备等
	@RequestMapping(value = "getbuygoodslist.action",method = RequestMethod.GET)
	@ResponseBody
	public java.util.List<Map<String, Object>> getbuygoods(HttpServletResponse response,String type) throws IOException
	{
		java.util.List<Map<String, Object>> list = thingDao.getBuyGoodsList(type);//oprea.query("place", "areaid = '"+id+"'", 0);
		return list;
	}
	
	// 购买药品
	@RequestMapping(value = "drugs.action",method = RequestMethod.GET)
	public void buyDrugs(HttpServletResponse response,String uuid,String type) throws IOException
	{
		String json = thingDao.buyDrugs(uuid,type);//(title, content, posterid,postername);//(id,uuid);//oprea.query("place", "areaid = '"+id+"'", 0);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 兑换戒指
	@RequestMapping(value = "duihuan.action",method = RequestMethod.GET)
	public void duihuan(HttpServletResponse response,String uuid) throws IOException
	{
		String json = thingDao.duihuan(uuid);//(title, content, posterid,postername);//(id,uuid);//oprea.query("place", "areaid = '"+id+"'", 0);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
}
