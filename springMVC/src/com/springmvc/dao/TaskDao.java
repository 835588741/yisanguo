package com.springmvc.dao;

import java.util.UUID;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class TaskDao
{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	
	public String michengzhengbazhan(String uuid)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select grade,name,money,(select state_micheng from state_list where sid=1001) as state_micheng from person where uuid='"+uuid+"'");
		if(rowSet.next())
		{
			int grade = rowSet.getInt("grade");
			long money = rowSet.getLong("money");
			int state_micheng = rowSet.getInt("state_micheng");
			
			if(grade < 15)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "你当前等级 [ "+grade+" ] 还未满15级，不能参加本次争霸战。");
				return jsonObject.toString();
			}
			else if(grade > 15)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "你当前等级 [ "+grade+" ] 已经超出15级，不能参加本次争霸战。");
				return jsonObject.toString();
			}
			
			if(money < 5000)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "你没有足够的报名费（5两银子），不能参加本次争霸战。");
				return jsonObject.toString();
			}
			
			// flag state
			if(state_micheng == 0)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "报名时间已经结束，等下一次系统通知吧。");
				return jsonObject.toString();
			}
			
			jdbcTemplate.update("update house set money=money+5000 where hid='0004'");
			jdbcTemplate.update("update person set money=money - 5000,areaid=(select areaid from place where type=10 order by rand() limit 0,1) where uuid='"+uuid+"'");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 200);
			jsonObject.put("message", "迷城险恶，虎狼环顾，请君珍重。");
			return jsonObject.toString();
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", 201);
		jsonObject.put("message", "未查询到你的用户信息。");
		return jsonObject.toString();
	}

	public String takewangzuo(String uuid)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select grade,name,areaid,(select state_micheng from state_list where sid=1001) as state_micheng from person where uuid='"+uuid+"'");
		if(rowSet.next())
		{
			int state_micheng = rowSet.getInt("state_micheng");
			int grade = rowSet.getInt("grade");
			int areaid = rowSet.getInt("areaid");
			String name = rowSet.getString("name");
			
			if(state_micheng == 1)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "还未到规定的时间，不能占领（至少需要在争霸战开始五分钟后）");
				return jsonObject.toString();
			}
			
			if(grade != 15)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "你的数据异常，请勿非法使用第三方外挂或利用漏洞");
				return jsonObject.toString();
			}
			// 判断是否还有人存活
			int pnum = jdbcTemplate.queryForInt("select count(1) from person where areaid >= 123001 and areaid <= 123013");
			if(pnum > 1)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "迷城中还有【"+(pnum-1)+"】名争霸勇士，你必须找到并击杀他或他们，才能登上王座。");
				return jsonObject.toString();
			}
			else if(pnum < 1)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "数据异常，请勿非法使用第三方外挂或利用游戏漏洞。");
				return jsonObject.toString();
			}
			
			if(areaid != 28)
			{
				long money = jdbcTemplate.queryForLong("select money from house where hid='0004'");
				jdbcTemplate.update("update person set money=money+"+(money+10000)+",areaid=28 where uuid='"+uuid+"'");
				jdbcTemplate.update("update person set areaid=28 where areaid >= 123001 and areaid <= 123013");
				jdbcTemplate.update("update house set money = 0 where hid='0004'");
				jdbcTemplate.update("UPDATE notice set notice = '恭喜【"+name+"】独占鳌头,成为本届15级小霸王,独享迷城累积财富和功勋' ,author = ''");
				//jdbcTemplate.update("update state_list set state_micheng = 0 where sid='0001'");
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 300);
				jsonObject.put("message", "恭喜你，独占鳌头，成为本届‘15级小霸王’，并独享当前迷城已有的所有累积财富【"+money+"】铜板，剑仙前辈推荐官府额外奖励你10两银子");
				
				return jsonObject.toString();
			}
			else
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "争霸战已经结束");
				return jsonObject.toString();
			}
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", 201);
		jsonObject.put("message", "未查询到你的用户信息。");
		return jsonObject.toString();
	}

	public String diyue(String uuid)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select grade,name,areaid from person where uuid='"+uuid+"'");
		if(rowSet.next())
		{
			int grade = rowSet.getInt("grade");
			if(grade == 15)
			{
				jdbcTemplate.update("update person set nextgradeexp=999999,exp=0 where uuid='"+uuid+"' and areaid=28");
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 200);
				jsonObject.put("message", "已经成功缔约");
				return jsonObject.toString();
			}
			else
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "非15级不能缔约");
				return jsonObject.toString();
			}
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", 201);
		jsonObject.put("message", "未查询到你的用户信息。");
		return jsonObject.toString();
	}

	public String getcangbaotu(String uuid)
	{
		// 301-309   321-329
		String sql = "select (select money from person where uuid='"+uuid+"') as money,(SELECT count(1) from goods where gtype=301 and masterid='0001') as count1,(SELECT count(1) from goods where gtype=321 and masterid='"+uuid+"') as count2 from goods where masterid='"+uuid+"' LIMIT 1";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
		if(rowSet.next())
		{
			int countBHP = rowSet.getInt("count1");
			int countTCS = rowSet.getInt("count2");
			int money    = rowSet.getInt("money");
			
			if(countBHP >= 1 && countTCS >= 1 && money>= 1000)
			{
				int count = jdbcTemplate.queryForInt("select COUNT(name) as count from goods where masterid='"+uuid+"' and state=2 and gtype < 281");
				if(count < 20)
				{
					// delete
					jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and gtype=301 limit 1");
					jdbcTemplate.update("delete from goods where masterid='"+uuid+"' and gtype=321 limit 1");
					jdbcTemplate.update("update person set money=money-1000 where uuid='"+uuid+"'");
					
					String gid = UUID.randomUUID().toString().replaceAll("-", "");
					jdbcTemplate.update("INSERT INTO `sanguo`.`goods` (`gid`, `name`, `masterid`, `price`, `life`, `state`, `addhp`, `addattack`, `adddefence`, `adddexterous`, `type`, `ggrade`, `descript`, `gareaid`, `gtype`, `star`, `unlawful_time`, `count`, `level`, `visibility`) VALUES ('"+gid+"', '秦地宫藏宝图', '"+uuid+"', '10000', '22000', '2', '0', '0', '0', '0', '8', '10', '一张秦始皇陵的地宫藏宝图，通过这张图的指示可以寻找到地宫入口，进入寻宝探险。', NULL, '50', '2', NULL, '0', '1', '1');");
					
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code", 200);
					jsonObject.put("message", "你交付给老先生一份一级白虎皮和一份一级天蚕丝以及一两银子的辛苦费，片刻过后老先生给了你【一份藏宝图】。");
					return jsonObject.toString();
				}
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "你身上行囊已满，无法存放更多物品。（本次行动取消）");
				return jsonObject.toString();
			}
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "你没有多余的材料或银子（条件：一份一级白虎皮，一份一级天蚕丝，一两银子）");
			return jsonObject.toString();
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", 201);
		jsonObject.put("message", "没有查询到你的用户信息。");
		return jsonObject.toString();
	}

}
