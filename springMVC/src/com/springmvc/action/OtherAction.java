package com.springmvc.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springmvc.dao.OtherDao;

@Controller
@RequestMapping("otherAction")
public class OtherAction
{
	@Autowired
	private OtherDao otherDao;
	
	// 其他
	@RequestMapping(value = "wish.action", method = RequestMethod.POST)
	public void wish(HttpServletResponse response,String content, String name,String uuid) throws IOException
	{
		String json = otherDao.wish(content,name,uuid);
		// 空，无此账号或密码
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(json);
	}
	
	
	// 捞取许愿瓶子
	@RequestMapping(value = "getwish.action", method = RequestMethod.GET)
	public void getwish(HttpServletResponse response,String uuid) throws IOException
	{
		String json = otherDao.getwish(uuid);
		// 空，无此账号或密码
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(json);
	}
	
	// 回复许愿瓶
	@RequestMapping(value = "replaywish.action", method = RequestMethod.POST)
	public void replaywish(HttpServletResponse response,String uuid,String message) throws IOException
	{
		String json = otherDao.sendSystemMessage(uuid, message);//;(uuid);
		// 空，无此账号或密码
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(json);
	}
	
	// 贿赂或挖矿
	@RequestMapping(value = "dowakuangorhuilu.action", method = RequestMethod.GET)
	public void dowakuangorhuilu(HttpServletResponse response,String uuid,int type) throws IOException
	{
		String json = otherDao.dowakuangorhuilu(uuid,type);
		// 空，无此账号或密码
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(json);
	}
	
//	String[] strs = new String[]{"松鼠","兔子","飞鹰","野猪","野狼","梅花鹿","大巨熊","凶恶猛虎","大型金钱豹","墨玉麒麟","赌场护卫","山野樵夫","外乡游客","赌坊老板"};
//	String[] strdesc = new String[]{"一只可爱的小松鼠飞快地蹿到你面前的草地上，还没注意到你的利箭已经悄悄瞄准它了。","一只白白的小兔子，虽然属于狩猎低等品，但也聊胜于无。","你注意到一只飞鹰飞掠而来，暗自搭弓引箭。","一只肥硕的野猪拖着笨重的身躯闯入你的视线内，你暗自一阵欢喜。","一只饥饿的野狼拖着疲惫的身躯来到你的视线范围内，你连忙打起精神。","一只色彩斑斓的梅花麋鹿一蹦一跳进入你的伏击圈内。","一只独眼的大巨熊挥舞着比你大腿还粗的双臂（前肢）朝你奔来，你吓得一个哆嗦。","一声虎啸，你吓得心头一惊，正想用手捂住双眼，差点手中弓箭脱手而落，你一头冷汗，原来是一只凶恶猛虎觅食而来。","一只大型金钱豹飞奔过来，躺在此地休息。","你一眨眼间发现居然有一只墨玉麒麟在此地降落，这可是预兆着祥瑞的神兽啊！","一个赌场护卫正在巡视猎场，还一路哼着歌正巧立马路过你这边。","一名山野樵夫莫名来到你面前，应该是正巧路过来砍柴的。","一名外乡游客来到此地游览山色风光，还独立吟诗作对","赌坊老板也正在狩猎之中，气着高头大马，拿着精良弓箭，哼唱着小二郎。"};
	// 射猎
	@RequestMapping(value = "shelie.action", method = RequestMethod.POST)
	public void shelie(HttpServletResponse response,String uuid,String sign,int level,String type) throws IOException
	{
		String json = otherDao.shelie(uuid,sign,level,type);
		// 空，无此账号或密码
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(json);
	}
	
	// 赌场拼大小开盘
	@RequestMapping(value = "bigorsmall.action", method = RequestMethod.GET)
	public void pinBigOrSmall(HttpServletResponse response,String uuid,int money) throws IOException
	{
		String json = otherDao.pinBigOrSmall(uuid,money);
		// 空，无此账号或密码
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(json);
	}
	
	
	// 我要坐庄 | 查看庄位收支  type=1|查看  type=2|注资     去除注资功能 type=3|追加注入资金
	@RequestMapping(value = "metohousemaster.action", method = RequestMethod.GET)
	public void metohousemaster(HttpServletResponse response,String uuid,long money,int type) throws IOException
	{
		String json = otherDao.metohousemaster(uuid,money,type);
		// 空，无此账号或密码
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(json);
	}
	
	// 我要撤庄 
	@RequestMapping(value = "cancelhousemaster.action", method = RequestMethod.GET)
	public void cancelhousemaster(HttpServletResponse response,String uuid) throws IOException
	{
		String json = otherDao.cancelhousemaster(uuid);
		// 空，无此账号或密码
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(json);
	}
	
	// 加载坐庄列表 
	@RequestMapping(value = "gethousemasterlist.action", method = RequestMethod.GET)
	public void gethousemasterlist(HttpServletResponse response) throws IOException
	{
		String json = otherDao.gethousemasterlist();
		// 空，无此账号或密码
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(json);
	}
	
	// 玩家坐庄的读牌 
	@RequestMapping(value = "readcard.action", method = RequestMethod.GET)
	public void readcard(HttpServletResponse response,String hid,String uuid,long money) throws IOException
	{
		String json = otherDao.getPai(hid, uuid, money);
		// 空，无此账号或密码
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(json);
	}
}
