package com.soul.project.application.bean;

public class DetailNPC
{
	String countryid;
	String countryname;
	int nstate;
	int nhp;
	int nareaid;
	int type;
	int ngrade;
	String nid;
	String ndialog;
	String nname;
	String ndescript;
	
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
	public int getNgrade()
	{
		return ngrade;
	}
	public void setNgrade(int ngrade)
	{
		this.ngrade = ngrade;
	}
	public int getNstate()
	{
		return nstate;
	}
	public void setNstate(int nstate)
	{
		this.nstate = nstate;
	}
	public int getNhp()
	{
		return nhp;
	}
	public void setNhp(int nhp)
	{
		this.nhp = nhp;
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
	public String getNdialog()
	{
		return ndialog;
	}
	public void setNdialog(String ndialog)
	{
		this.ndialog = ndialog;
	}
	public String getNname()
	{
		return nname;
	}
	public void setNname(String nname)
	{
		this.nname = nname;
	}
	public String getNdescript()
	{
		return ndescript;
	}
	public void setNdescript(String ndescript)
	{
		this.ndescript = ndescript;
	}
	@Override
	public String toString()
	{
		return "npc [nstate=" + nstate + ", nhp=" + nhp + ", nareaid=" + nareaid + ", ngrade=" + ngrade + ", nid="
				+ nid + ", ndialog=" + ndialog + ", nname=" + nname + ", ndescript=" + ndescript + "]";
	}
}
