package com.springmvc.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springmvc.dao.EquipmentDao;

@Controller
@RequestMapping("equipmentAction")
public class EquipmentAction
{
	@Autowired
	private EquipmentDao equipmentDao;
	
	// 评估
	@RequestMapping(value = "pingu.action", method = RequestMethod.GET)
	public void pingu(HttpServletResponse response,String uuid,String gid) throws IOException
	{
		String data = equipmentDao.pingu(uuid,gid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(data);
	}
	
	// 升级申请
	@RequestMapping(value = "upgraderequest.action", method = RequestMethod.GET)
	public void upgraderequest(HttpServletResponse response,String uuid,String gid) throws IOException
	{
		String data = equipmentDao.upgraderequest(uuid,gid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(data);
	}
	
	// 执行升级
	@RequestMapping(value = "upgradeequip.action", method = RequestMethod.GET)
	public void upgradeequip(HttpServletResponse response,String uuid,String gid) throws IOException
	{
		String data = equipmentDao.upgradeequip(uuid,gid);
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(data);
	}
}
