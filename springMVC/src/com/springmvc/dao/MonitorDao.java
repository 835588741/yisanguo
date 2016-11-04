package com.springmvc.dao;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class MonitorDao
{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	public String cheat(String uuid, String name)
	{
		try
		{
			SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select uuid from black_list where uuid='"+uuid+"' and name='"+name+"'");
			if(rowSet.next())
			{
				// 更新
				jdbcTemplate.update("update black_list set count_time=count_time+1 where uuid='"+uuid+"' and name='"+name+"'");
			}
			else
			{
				// 插入
				jdbcTemplate.update("insert into black_list(uuid,name,count_time) values ('"+uuid+"','"+name+"',"+1+")");
			}
			try
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 200);
				jsonObject.put("message", "报告提交成功");
				return jsonObject.toString();
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{
			try
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "报告提交失败"+e.toString());
				return jsonObject.toString();
			}
			catch (JSONException e2)
			{
				// TODO Auto-generated catch block
				e2.printStackTrace();
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
}
