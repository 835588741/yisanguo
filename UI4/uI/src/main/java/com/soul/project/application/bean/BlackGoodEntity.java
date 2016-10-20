package com.soul.project.application.bean;

public class BlackGoodEntity
{
	String tid;
	String gid;
	String mastername;
	
	String descript;
	
	long price;
	int life;
	int addhp;
	int adddefence;
	int adddexterous;
	int addattack;
	public String getTid()
	{
		return tid;
	}
	public void setTid(String tid)
	{
		this.tid = tid;
	}
	public String getGid()
	{
		return gid;
	}
	public void setGid(String gid)
	{
		this.gid = gid;
	}
	public String getMastername()
	{
		return mastername;
	}
	public void setMastername(String mastername)
	{
		this.mastername = mastername;
	}
	public String getDescript()
	{
		return descript;
	}
	public void setDescript(String descript)
	{
		this.descript = descript;
	}
	public long getPrice()
	{
		return price;
	}
	public void setPrice(long price)
	{
		this.price = price;
	}
	public int getLife()
	{
		return life;
	}
	public void setLife(int life)
	{
		this.life = life;
	}
	public int getAddhp()
	{
		return addhp;
	}
	public void setAddhp(int addhp)
	{
		this.addhp = addhp;
	}
	public int getAdddefence()
	{
		return adddefence;
	}
	public void setAdddefence(int adddefence)
	{
		this.adddefence = adddefence;
	}
	public int getAdddexterous()
	{
		return adddexterous;
	}
	public void setAdddexterous(int adddexterous)
	{
		this.adddexterous = adddexterous;
	}
	public int getAddattack()
	{
		return addattack;
	}
	public void setAddattack(int addattack)
	{
		this.addattack = addattack;
	}
	public BlackGoodEntity(String tid, String gid, String mastername, String descript, long price, int life, int addhp, int adddefence, int adddexterous, int addattack)
	{
		super();
		this.tid = tid;
		this.gid = gid;
		this.mastername = mastername;
		this.descript = descript;
		this.price = price;
		this.life = life;
		this.addhp = addhp;
		this.adddefence = adddefence;
		this.adddexterous = adddexterous;
		this.addattack = addattack;
	}
	
	
//	tid,masterid,gid,price,(select name from person where uuid=masterid) as mastername ,life,addhp,addattack,adddefence,adddexterous,descript
}
