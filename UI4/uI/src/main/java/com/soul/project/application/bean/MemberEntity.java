package com.soul.project.application.bean;

public class MemberEntity
{
	String uuid;
	String name;
	String position;
	String country;
	String isonline ;
	int grade;
	
	public String getIsonline()
	{
		return isonline;
	}
	public void setIsonline(String isonline)
	{
		this.isonline = isonline;
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
	public String getPosition()
	{
		return position;
	}
	public void setPosition(String position)
	{
		this.position = position;
	}
	public String getCountry()
	{
		return country;
	}
	public void setCountry(String country)
	{
		this.country = country;
	}
	public int getGrade()
	{
		return grade;
	}
	public void setGrade(int grade)
	{
		this.grade = grade;
	}
}
