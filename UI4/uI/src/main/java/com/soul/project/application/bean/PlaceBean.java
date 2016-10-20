package com.soul.project.application.bean;

import java.util.ArrayList;
import java.util.List;

public class PlaceBean
{
//	{
//    "westid": "3",
//    "westname": "驿站",
//    "northname": "黄府大院",
//    "southid": "4",
//    "name": "新手村小广场",
//    "descript": "这里是新手村小广场，人流汇集之地，人来人往好不热闹",
//    "areaid": "0000000000",
//    "southname": "钱庄",
//    "pid": "000",
//    "eastid": "1",
//    "eastname": "新手村客栈",
//    "northid": "2"
//}
	int westid;
	int southid;
	int areaid;
	int northid;
	int eastid;
	int type;
	float version = 1.1f;
	String function1 = "";
	String function2 = "";
	String function3 = "";
	String function4 = "";
	String name;
	String descript;
	String westname;
	String northname;
	String southname;
	String eastname;
	String battle_string;
	String countryid;
	String countryname;
	String message;
	String transaction;
	String invitation;
	
	List<player> npc = new ArrayList<PlaceBean.player>();
	List<player> player = new ArrayList<PlaceBean.player>();
	List<goods> goods = new ArrayList<PlaceBean.goods>();
	//ArrayList<message> message = new ArrayList<PlaceBean.message>();
	List<leavelist> leavelist = new ArrayList<leavelist>();
	List<achievelist> achievelist = new ArrayList<PlaceBean.achievelist>();
	notice notice = new notice();
	userdate userdata = new userdate();
	
	
	
	public String getInvitation()
	{
		return invitation;
	}

	public void setInvitation(String invitation)
	{
		this.invitation = invitation;
	}

	public String getBattle_string()
	{
		return battle_string;
	}

	public void setBattle_string(String battle_string)
	{
		this.battle_string = battle_string;
	}

	public String getTransaction()
	{
		return transaction;
	}

	public void setTransaction(String transaction)
	{
		this.transaction = transaction;
	}

	public float getVersion()
	{
		return version;
	}

	public void setVersion(float version)
	{
		this.version = version;
	}


	public static class leavelist
	{
		public String uuid;
		public String message;
		public int type;
		public int areaid;
		
		@Override
		public String toString()
		{
			return "leavelist [uuid=" + uuid + ", message=" + message + ", type=" + type + ", areaid=" + areaid + "]";
		}
		public String getUuid()
		{
			return uuid;
		}
		public void setUuid(String uuid)
		{
			this.uuid = uuid;
		}
		public String getMessage()
		{
			return message;
		}
		public void setMessage(String message)
		{
			this.message = message;
		}
		public int getType()
		{
			return type;
		}
		public void setType(int type)
		{
			this.type = type;
		}
		public int getAreaid()
		{
			return areaid;
		}
		public void setAreaid(int areaid)
		{
			this.areaid = areaid;
		}
	}
	
	public static class achievelist extends leavelist
	{
		
	}
	
	public static class notice
	{
		String notice;
		String author;
		String time;
		String id;
		public String getNotice()
		{
			return notice;
		}
		public void setNotice(String notice)
		{
			this.notice = notice;
		}
		public String getAuthor()
		{
			return author;
		}
		public void setAuthor(String author)
		{
			this.author = author;
		}
		public String getTime()
		{
			return time;
		}
		public void setTime(String time)
		{
			this.time = time;
		}
		public String getId()
		{
			return id;
		}
		public void setId(String id)
		{
			this.id = id;
		}
		@Override
		public String toString()
		{
			return "notice [notice=" + notice + ", author=" + author + ", time=" + time + ", id=" + id + "]";
		}
	}
	
	public static class goods
	{
		int  addattack;
		String masterid;
		int life;
		int price;
		String name;
		int state;
		String gid;//": "0001",
		String descript;
		int addhp;
		/** 1|武器  2|头盔  3|披风  4|铠甲  5|腰带  6|靴子  7|戒指  8|无属性物品  9|黄金  10|白银  11|铜板*/
		int type;
		int star;
		int grade;
		int adddexterous;
		int adddefence;
		

		public int getStar()
		{
			return star;
		}
		public void setStar(int star)
		{
			this.star = star;
		}
		public int getAddattack()
		{
			return addattack;
		}
		public void setAddattack(int addattack)
		{
			this.addattack = addattack;
		}

		public String getDescript()
		{
			return descript;
		}
		public void setDescript(String descript)
		{
			this.descript = descript;
		}
		public int getGrade()
		{
			return grade;
		}
		public void setGrade(int grade)
		{
			this.grade = grade;
		}
		public String getMasterid()
		{
			return masterid;
		}
		public void setMasterid(String masterid)
		{
			this.masterid = masterid;
		}
		public int getLife()
		{
			return life;
		}
		public void setLife(int life)
		{
			this.life = life;
		}
		public int getPrice()
		{
			return price;
		}
		public void setPrice(int price)
		{
			this.price = price;
		}
		public String getName()
		{
			return name;
		}
		public void setName(String name)
		{
			this.name = name;
		}
		/** 1|随身已装备  2|行囊中*/
		public int getState()
		{
			return state;
		}
		public void setState(int state)
		{
			this.state = state;
		}
		public String getGid()
		{
			return gid;
		}
		public void setGid(String gid)
		{
			this.gid = gid;
		}
		public int getAddhp()
		{
			return addhp;
		}
		public void setAddhp(int addhp)
		{
			this.addhp = addhp;
		}
		/** 1|武器  2|头盔  3|披风  4|铠甲  5|腰带  6|靴子  7|戒指  8|无属性物品  9|黄金  10|白银  11|铜板*/
		public int getType()
		{
			return type;
		}
		public void setType(int type)
		{
			this.type = type;
		}
		public int getAdddexterous()
		{
			return adddexterous;
		}
		public void setAdddexterous(int adddexterous)
		{
			this.adddexterous = adddexterous;
		}
		public int getAdddefence()
		{
			return adddefence;
		}
		public void setAdddefence(int adddefence)
		{
			this.adddefence = adddefence;
		}
		@Override
		public String toString()
		{
			return "Goods [addattack=" + addattack + ", masterid=" + masterid + ", life=" + life + ", price=" + price
					+ ", name=" + name + ", state=" + state + ", gid=" + gid + ", addhp=" + addhp + ", type=" + type
					+ ", adddexterous=" + adddexterous + ", adddefence=" + adddefence + "]";
		}
		
		

//		   "addattack": "65",
//		    "masterid": "0001",
//		    "life": "1000",
//		    "price": "1000",
//		    "name": "道剑忘机",
//		    "state": "1",
//		    "gid": "0001",
//		    "addhp": "0",
//		    "type": "1",
//		    "adddexterous": "0",
//		    "adddefence": "0"
	}
	
	
	public static class userdate
	{
		int areaid;
		int  grade;
		long exp;
		long money;
		int hp;
		int maxhp;
		int type;
		int attack; //攻击
		int defence; // 防御
		int dodge; //闪避
		long positionexp;
		int positiongrade;
		int gender = 1;
		String isbattle;
		String position;
		String dialog;
		String countryid;
		String countryname;
		String account;
		String password;
		String uuid;
		String name;
		String descript;
		// 是否管理员
		String manager;
		String groupname;
		String token;
		String question;
		
		public String getQuestion()
		{
			return question;
		}
		public void setQuestion(String question)
		{
			this.question = question;
		}
		public int getGender()
		{
			return gender;
		}
		public void setGender(int gender)
		{
			this.gender = gender;
		}
		public String getToken()
		{
			return token;
		}
		public void setToken(String token)
		{
			this.token = token;
		}
		public String getGroupname()
		{
			return groupname;
		}
		public void setGroupname(String groupname)
		{
			this.groupname = groupname;
		}
		public String getManager()
		{
			return manager;
		}
		public void setManager(String manager)
		{
			this.manager = manager;
		}
		public int getType()
		{
			return type;
		}
		public void setType(int type)
		{
			this.type = type;
		}
		public int getPositiongrade()
		{
			return positiongrade;
		}
		public void setPositiongrade(int positiongrade)
		{
			this.positiongrade = positiongrade;
		}
		public long getPositionexp()
		{
			return positionexp;
		}
		public void setPositionexp(long positionexp)
		{
			this.positionexp = positionexp;
		}
		public String getPosition()
		{
			return position;
		}
		public void setPosition(String position)
		{
			this.position = position;
		}
		public int getMaxhp()
		{
			return maxhp;
		}
		public void setMaxhp(int maxhp)
		{
			this.maxhp = maxhp;
		}
		public String getIsbattle()
		{
			return isbattle;
		}
		public void setIsbattle(String isbattle)
		{
			this.isbattle = isbattle;
		}
		public int getAttack()
		{
			return attack;
		}
		public void setAttack(int attack)
		{
			this.attack = attack;
		}
		public int getDefence()
		{
			return defence;
		}
		public void setDefence(int defence)
		{
			this.defence = defence;
		}
		public int getDodge()
		{
			return dodge;
		}
		public void setDodge(int dodge)
		{
			this.dodge = dodge;
		}
		public String getDialog()
		{
			return dialog;
		}
		public void setDialog(String dialog)
		{
			this.dialog = dialog;
		}
		public String getCountryid()
		{
			return countryid;
		}
		public void setCountryid(String countryid)
		{
			this.countryid = countryid;
		}
		public String getCountryname()
		{
			return countryname;
		}
		public void setCountryname(String countryname)
		{
			this.countryname = countryname;
		}
		public String getAccount()
		{
			return account;
		}
		public void setAccount(String account)
		{
			this.account = account;
		}
		public String getPassword()
		{
			return password;
		}
		public void setPassword(String password)
		{
			this.password = password;
		}
		public int getHp()
		{
			return hp;
		}
		public void setHp(int hp)
		{
			this.hp = hp;
		}
		public int getAreaid()
		{
			return areaid;
		}
		public void setAreaid(int areaid)
		{
			this.areaid = areaid;
		}
		public int getGrade()
		{
			return grade;
		}
		public void setGrade(int grade)
		{
			this.grade = grade;
		}
		public long getExp()
		{
			return exp;
		}
		public void setExp(long exp)
		{
			this.exp = exp;
		}
		public long getMoney()
		{
			return money;
		}
		public void setMoney(long money)
		{
			this.money = money;
		}
		public String getUuid()
		{
			return uuid;
		}
		public void setUuid(String uuid)
		{
			this.uuid = uuid;
		}
		public String getName()
		{
			return name;
		}
		public void setName(String name)
		{
			this.name = name;
		}
		public String getDescript()
		{
			return descript;
		}
		public void setDescript(String descript)
		{
			this.descript = descript;
		}
		@Override
		public String toString()
		{
			return "player [areaid=" + areaid + ", grade=" + grade + ", exp=" + exp + ", money=" + money + ", hp=" + hp
					+ ", account=" + account + ", password=" + password + ", uuid=" + uuid + ", name=" + name
					+ ", descript=" + descript + "]";
		}
	}
	
	
	public userdate getUserdata()
	{
		return userdata;
	}
	
	public void setUserdata(userdate userdata)
	{
		this.userdata = userdata;
	}
	public List<leavelist> getLeavelist()
	{
		return leavelist;
	}
	public void setLeavelist(List<leavelist> leavelist)
	{
		this.leavelist = leavelist;
	}
	public List<achievelist> getAchievelist()
	{
		return achievelist;
	}
	public void setAchievelist(List<achievelist> achievelist)
	{
		this.achievelist = achievelist;
	}
	public List<goods> getGoods()
	{
		return goods;
	}
	public void setGoods(List<goods> goods)
	{
		this.goods = goods;
	}
	public notice getNotice()
	{
		return notice;
	}
	public void setNotice(notice notice)
	{
		this.notice = notice;
	}
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type = type;
	}
	public String getCountryid()
	{
		return countryid;
	}
	public void setCountryid(String countryid)
	{
		this.countryid = countryid;
	}
	public String getCountryname()
	{
		return countryname;
	}
	public void setCountryname(String countryname)
	{
		this.countryname = countryname;
	}

	
	public static class npc
	{
		String nid;
		String nname;
		int nareaid;
		String dialog;
		String countryname;
		int type;
		
		public int getType()
		{
			return type;
		}
		public void setType(int type)
		{
			this.type = type;
		}
		public String getCountryname()
		{
			return countryname;
		}
		public void setCountryname(String countryname)
		{
			this.countryname = countryname;
		}
		public String getDialog()
		{
			return dialog;
		}
		public void setDialog(String dialog)
		{
			this.dialog = dialog;
		}
		public int getNareaid()
		{
			return nareaid;
		}
		public void setNareaid(int nareaid)
		{
			this.nareaid = nareaid;
		}
		public String getNid()
		{
			return nid;
		}
		public void setNid(String nid)
		{
			this.nid = nid;
		}
		public String getNname()
		{
			return nname;
		}
		public void setNname(String nname)
		{
			this.nname = nname;
		}
		@Override
		public String toString()
		{
			return "npc [nid=" + nid + ", nname=" + nname + "]";
		}

	}
	
	public static class player
	{
		int areaid;
		int type;
		String uuid;
		String name;
		String dialog;
		String countryname;
		String position;
		
		public String getPosition()
		{
			return position;
		}
		public void setPosition(String position)
		{
			this.position = position;
		}
		public int getType()
		{
			return type;
		}
		public void setType(int type)
		{
			this.type = type;
		}
		public String getCountryname()
		{
			return countryname;
		}
		public void setCountryname(String countryname)
		{
			this.countryname = countryname;
		}
		public String getDialog()
		{
			return dialog;
		}
		public void setDialog(String dialog)
		{
			this.dialog = dialog;
		}
		@Override
		public String toString()
		{
			return "player [areaid=" + areaid + ", uuid=" + uuid + ", name=" + name + "]";
		}
		public int getAreaid()
		{
			return areaid;
		}
		public void setAreaid(int areaid)
		{
			this.areaid = areaid;
		}
		public String getUuid()
		{
			return uuid;
		}
		public void setUuid(String uuid)
		{
			this.uuid = uuid;
		}
		public String getName()
		{
			return name;
		}
		public void setName(String name)
		{
			this.name = name;
		}
		
	}
	

	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	public List<player> getNpc()
	{
		return npc;
	}
	public void setNpc(List<player> npc)
	{
		this.npc = npc;
	}
	public List<player> getPlayer()
	{
		return player;
	}
	public void setPlayer(List<player> player)
	{
		this.player = player;
	}
	public int getWestid()
	{
		return westid;
	}
	public void setWestid(int westid)
	{
		this.westid = westid;
	}
	public int getSouthid()
	{
		return southid;
	}
	public void setSouthid(int southid)
	{
		this.southid = southid;
	}
	public int getAreaid()
	{
		return areaid;
	}
	public void setAreaid(int areaid)
	{
		this.areaid = areaid;
	}
	public int getNorthid()
	{
		return northid;
	}
	public void setNorthid(int northid)
	{
		this.northid = northid;
	}
	public int getEastid()
	{
		return eastid;
	}
	public void setEastid(int eastid)
	{
		this.eastid = eastid;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getDescript()
	{
		return descript;
	}
	public void setDescript(String descript)
	{
		this.descript = descript;
	}
	public String getWestname()
	{
		return westname;
	}
	public void setWestname(String westname)
	{
		this.westname = westname;
	}
	public String getNorthname()
	{
		return northname;
	}
	public void setNorthname(String northname)
	{
		this.northname = northname;
	}
	public String getSouthname()
	{
		return southname;
	}
	public void setSouthname(String southname)
	{
		this.southname = southname;
	}
	public String getEastname()
	{
		return eastname;
	}
	public void setEastname(String eastname)
	{
		this.eastname = eastname;
	}
	@Override
	public String toString()
	{
		return "PlaceBean [westid=" + westid + ", southid=" + southid + ", areaid=" + areaid + ", northid=" + northid
				+ ", eastid=" + eastid + ", name=" + name + ", descript=" + descript + ", westname=" + westname
				+ ", northname=" + northname + ", southname=" + southname + ", eastname=" + eastname + "]";
	}
	public String getFunction1()
	{
		return function1;
	}
	public void setFunction1(String function1)
	{
		this.function1 = function1;
	}
	public String getFunction2()
	{
		return function2;
	}
	public void setFunction2(String function2)
	{
		this.function2 = function2;
	}
	public String getFunction3()
	{
		return function3;
	}
	public void setFunction3(String function3)
	{
		this.function3 = function3;
	}
	public String getFunction4()
	{
		return function4;
	}
	public void setFunction4(String function4)
	{
		this.function4 = function4;
	}
	
	
}
