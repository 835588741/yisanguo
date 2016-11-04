package com.springmvc.dao;

import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public class RankDao
{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public String countryrank()
	{
//		select countryname,SUM(money)/1000,COUNT(1) as people from person where countryid = '0002' and type =1
//		select countryname,SUM(money)/1000,COUNT(name) as people from person where countryid = '0000'
//		System.out.println("select countryname,SUM(money)/1000 as money,COUNT(1) as people from person where countryid = '0002' and type =1");
		Map<String, Object> mapLuoyang    = jdbcTemplate.queryForMap("select countryname,SUM(money)/1000 as money,COUNT(1) as people from person where countryid = '0002' and type =1");
		Map<String, Object> mapNanyang    = jdbcTemplate.queryForMap("select countryname,SUM(money)/1000 as money,COUNT(1) as people from person where countryid = '0001' and type =1");
		Map<String, Object> mapXuChang    = jdbcTemplate.queryForMap("select countryname,SUM(money)/1000 as money,COUNT(1) as people from person where countryid = '0003' and type =1");
		Map<String, Object> mapXinshoucun = jdbcTemplate.queryForMap("select countryname,SUM(money)/1000 as money,COUNT(1) as people from person where countryid = '0000' and type =1");
		
		//{"nanyang":{"countryname":"南阳","money":12115.54,"people":65},"luoyang":{"countryname":"洛阳","money":3921.522,"people":44},"xinshoucun":{"countryname":"新手村","money":13858.371,"people":524}} 
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("nanyang", mapNanyang);
		jsonObject.put("luoyang", mapLuoyang);
		jsonObject.put("xuchang", mapXuChang);
		jsonObject.put("xinshoucun", mapXinshoucun);
		
		return jsonObject.toString();
	}
	
}
