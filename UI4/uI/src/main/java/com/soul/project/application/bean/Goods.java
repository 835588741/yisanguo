package com.soul.project.application.bean;

public class Goods
{
	String tid;
	int  addattack;
	String masterid;
	int life;
	long price;
	String name;
	int state;
	String gid;//": "0001",
	String descript;
	int addhp;
	/** 1|武器  2|头盔  3|披风  4|铠甲  5|腰带  6|靴子  7|戒指  8|无属性物品  9|黄金  10|白银  11|铜板*/
	int type;
	int ggrade;
	int adddexterous;
	int adddefence;
	int gareaid;
	int star;
	int gtype;
	int count;
	int visibility;
	String mastername;
	
	
	public int getGtype()
	{
		return gtype;
	}
	public void setGtype(int gtype)
	{
		this.gtype = gtype;
	}
	public int getCount()
	{
		return count;
	}
	public void setCount(int count)
	{
		this.count = count;
	}
	public int getVisibility()
	{
		return visibility;
	}
	public void setVisibility(int visibility)
	{
		this.visibility = visibility;
	}
	public int getStar()
	{
		return star;
	}
	public void setStar(int star)
	{
		this.star = star;
	}
	public String getTid()
	{
		return tid;
	}
	public void setTid(String tid)
	{
		this.tid = tid;
	}

	public String getMastername()
	{
		return mastername;
	}
	public void setMastername(String mastername)
	{
		this.mastername = mastername;
	}
	public int getGareaid()
	{
		return gareaid;
	}
	public void setGareaid(int gareaid)
	{
		this.gareaid = gareaid;
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

	public int getGgrade()
	{
		return ggrade;
	}
	public void setGgrade(int ggrade)
	{
		this.ggrade = ggrade;
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

	public long getPrice()
	{
		return price;
	}
	public void setPrice(long price)
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
	
	

//	   "addattack": "65",
//	    "masterid": "0001",
//	    "life": "1000",
//	    "price": "1000",
//	    "name": "道剑忘机",
//	    "state": "1",
//	    "gid": "0001",
//	    "addhp": "0",
//	    "type": "1",
//	    "adddexterous": "0",
//	    "adddefence": "0"
}
