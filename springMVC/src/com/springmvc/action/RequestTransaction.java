package com.springmvc.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springmvc.dao.TransactionDao;

@Controller
@RequestMapping("requestTransaction")
public class RequestTransaction
{
	@Autowired
	private TransactionDao dao;
	
//	http://169.254.85.186:8080/springMVC/requestTransaction/transaction.action?&seller_uuid=0040&buyer_uuid=0001&gid=58354ce34c744859829d7e71df4250aa
	// 发起交易请求
	@RequestMapping(value = "transaction.action",method = RequestMethod.GET)
	public void transaction(HttpServletResponse response,String seller_uuid,String buyer_uuid,String gid,String price) throws IOException
	{
		String json = dao.transaction(seller_uuid,buyer_uuid,gid,price);//oprea.query("place", "areaid = '"+id+"'", 0);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 获取交易请求
	@RequestMapping(value = "gettransactionlist.action",method = RequestMethod.GET)
	@ResponseBody
	public Object getTransactionList(HttpServletResponse response,String buyer_uuid) throws IOException
	{
		return dao.getTransactionList(buyer_uuid);
	}
	
	// 接受交易请求
	@RequestMapping(value = "agree.action",method = RequestMethod.GET)
	public void agree(HttpServletResponse response,String tid,String buyer_uuid) throws IOException
	{
		String json = dao.agree(tid,buyer_uuid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 拒绝交易请求
	@RequestMapping(value = "refuse.action",method = RequestMethod.GET)
	public void refuse(HttpServletResponse response,String tid,String buyer_uuid) throws IOException
	{
		String json = dao.refuse(tid,buyer_uuid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}

	// 黑市委托挂售交易请求
	@RequestMapping(value = "sell.action",method = RequestMethod.GET)
	public void sell(HttpServletResponse response,String seller_uuid,String gid,String price) throws IOException
	{
		String json = dao.sell(seller_uuid,gid,price);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 黑市取消交易取回交易物品请求
	@RequestMapping(value = "cancelsell.action",method = RequestMethod.GET)
	public void cancelsell(HttpServletResponse response,String seller_uuid,String gid) throws IOException
	{
		String json = dao.cancelsell(seller_uuid,gid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 获取黑市所有物品请求
	@RequestMapping(value = "getallsell.action",method = RequestMethod.GET)
	public void getallsell(HttpServletResponse response,String start,String size,String type) throws IOException
	{
		String json = dao.getallsell(start,size,type);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 获取黑市我挂售的所有物品请求
	@RequestMapping(value = "getmysell.action",method = RequestMethod.GET)
	public void getmysell(HttpServletResponse response,String uuid) throws IOException
	{
		String json = dao.getMySell(uuid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}

	
	// 购买黑市物品 （也可以判断当买家卖家uuid相同时是取回操作）
	@RequestMapping(value = "buy.action",method = RequestMethod.GET)
	public void buy(HttpServletResponse response,String tid,String gid,String buyer_uuid) throws IOException
	{
		String json = dao.buy(tid,gid,buyer_uuid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 领取工资
	@RequestMapping(value = "receivesalary.action",method = RequestMethod.GET)
	public void receivesalary(HttpServletResponse response,String uuid,String country) throws IOException
	{
		String json = dao.receivesalary(uuid,country);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}
	
	// 国战捐资 (单位1两起)
	@RequestMapping(value = "donatemoney.action",method = RequestMethod.GET)
	public void donatemoney(HttpServletResponse response,String countryid,String uuid,long money) throws IOException
	{
		String json = dao.donatemoney(countryid,uuid,money);
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().println(json);
	}

}
