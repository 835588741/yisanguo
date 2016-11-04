package com.springmvc.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springmvc.dao.RankDao;

@Controller
@RequestMapping("rankAction")
public class RankAction
{
	@Autowired
	private RankDao rankDao;
	
	// 登陆
	@RequestMapping(value = "countryrank.action", method = RequestMethod.GET)
	@ResponseBody
	public void countryrank(HttpServletResponse response) throws IOException
	{
		String data = rankDao.countryrank();
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(data);
	}
}
