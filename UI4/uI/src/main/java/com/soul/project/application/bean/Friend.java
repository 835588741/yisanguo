package com.soul.project.application.bean;

public class Friend
{
	String fuuid;
	String fname;
	String buuid;
	String alias;
	String bname;
	String areaname;
	int state;
	public String getFuuid()
	{
		return fuuid;
	}
	public void setFuuid(String fuuid)
	{
		this.fuuid = fuuid;
	}
	public String getFname()
	{
		return fname;
	}
	public void setFname(String fname)
	{
		this.fname = fname;
	}
	public String getBuuid()
	{
		return buuid;
	}
	public void setBuuid(String buuid)
	{
		this.buuid = buuid;
	}
	public String getAlias()
	{
		return alias;
	}
	public void setAlias(String alias)
	{
		this.alias = alias;
	}
	public String getBname()
	{
		return bname;
	}
	public void setBname(String bname)
	{
		this.bname = bname;
	}
	public String getAreaname()
	{
		return areaname;
	}
	public void setAreaname(String areaname)
	{
		this.areaname = areaname;
	}
	public int getState()
	{
		return state;
	}
	public void setState(int state)
	{
		this.state = state;
	}
	@Override
	public String toString()
	{
		return "Friend [fuuid=" + fuuid + ", fname=" + fname + ", buuid=" + buuid + ", alias=" + alias + ", bname="
				+ bname + ", areaname=" + areaname + ", state=" + state + "]";
	}
	
	
}
