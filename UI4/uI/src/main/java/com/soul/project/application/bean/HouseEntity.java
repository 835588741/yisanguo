package com.soul.project.application.bean;

public class HouseEntity
{
	String hid;
	String housemasterid;
	long houseinitmoney;
	long housecurrendmoney;
	String name;
	String state;
	
	public String getHid()
	{
		return hid;
	}
	public void setHid(String hid)
	{
		this.hid = hid;
	}
	public String getHousemasterid()
	{
		return housemasterid;
	}
	public void setHousemasterid(String housemasterid)
	{
		this.housemasterid = housemasterid;
	}
	public long getHouseinitmoney()
	{
		return houseinitmoney;
	}
	public void setHouseinitmoney(long houseinitmoney)
	{
		this.houseinitmoney = houseinitmoney;
	}
	public long getHousecurrendmoney()
	{
		return housecurrendmoney;
	}
	public void setHousecurrendmoney(long housecurrendmoney)
	{
		this.housecurrendmoney = housecurrendmoney;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getState()
	{
		long d = housecurrendmoney - houseinitmoney;
		if(d < 0)
			return "亏损";
		else if(d > 0)
			return "盈余";
		else
			return "持平";
	}
	public void setState(String state)
	{
		this.state = state;
	}
	@Override
	public String toString()
	{
		return "HouseEntity [hid=" + hid + ", housemasterid=" + housemasterid + ", houseinitmoney=" + houseinitmoney
				+ ", housecurrendmoney=" + housecurrendmoney + ", name=" + name + ", state=" + state + "]";
	}
	
}
