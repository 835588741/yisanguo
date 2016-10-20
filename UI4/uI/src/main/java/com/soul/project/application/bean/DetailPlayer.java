package com.soul.project.application.bean;

public class DetailPlayer
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
