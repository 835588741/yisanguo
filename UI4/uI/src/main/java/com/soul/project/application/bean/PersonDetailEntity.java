package com.soul.project.application.bean;

import java.util.ArrayList;
import java.util.List;

public class PersonDetailEntity
{
	userdata userdata = new userdata();
	List<goods> goods = new ArrayList<goods>();
	String groupname;
	
	
	public String getGroupname()
	{
		return groupname;
	}

	public void setGroupname(String groupname)
	{
		this.groupname = groupname;
	}

	public static class userdata
	{
		int areaid;
		int  grade;
		long exp;
		long money;
		long nextgradeexp;
		long nextpositionexp;
		int hp;
		int maxhp;
		int type;
		int attack; //攻击
		int defence; // 防御
		int dodge; //闪避
		int gender;
		long positionexp;
		int positiongrade;
		String chenghaodescript;
		String chenghao;
		String sign;
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
		String groupid;
		String wife = "无";
		String brother = "无";
//		String groupname;
//		
//		public String getGroupname()
//		{
//			return groupname;
//		}
//		public void setGroupname(String groupname)
//		{
//			this.groupname = groupname;
//		}
		
		public String getGroupid()
		{
			return groupid;
		}
		public String getWife()
		{
			return wife;
		}
		public void setWife(String wife)
		{
			this.wife = wife;
		}
		public String getBrother()
		{
			return brother;
		}
		public void setBrother(String brother)
		{
			this.brother = brother;
		}
		public void setGroupid(String groupid)
		{
			this.groupid = groupid;
		}
		public int getGender()
		{
			return gender;
		}
		public void setGender(int gender)
		{
			this.gender = gender;
		}
		public String getChenghaodescript()
		{
			return chenghaodescript;
		}
		public void setChenghaodescript(String chenghaodescript)
		{
			this.chenghaodescript = chenghaodescript;
		}
		public String getChenghao()
		{
			return chenghao;
		}
		public void setChenghao(String chenghao)
		{
			this.chenghao = chenghao;
		}
		public String getSign()
		{
			return sign;
		}
		public void setSign(String sign)
		{
			this.sign = sign;
		}
		public long getNextpositionexp()
		{
			return nextpositionexp;
		}
		public void setNextpositionexp(long nextpositionexp)
		{
			this.nextpositionexp = nextpositionexp;
		}
		public long getNextgradeexp()
		{
			return nextgradeexp;
		}
		public void setNextgradeexp(long nextgradeexp)
		{
			this.nextgradeexp = nextgradeexp;
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
		int grade;
		int adddexterous;
		int adddefence;
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

	public userdata getUserdata()
	{
		return userdata;
	}

	public void setUserdata(userdata userdata)
	{
		this.userdata = userdata;
	}



	public List<goods> getGoods()
	{
		return goods;
	}

	public void setGoods(List<goods> goods)
	{
		this.goods = goods;
	}

	@Override
	public String toString()
	{
		return "PersonDetailEntity [userdata=" + userdata + ", goods=" + goods + "]";
	}
}
