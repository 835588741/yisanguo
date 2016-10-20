package com.soul.project.application.bean;

public class GroupEntity
{
	String groupid = "";
    String group_name  = "";
    String group_master_uuid = "";
    String group_master_uuid2 = "";
    String group_count = "";
    String group_grade = "";
    String group_max_people = "";
    String group_state = "";
    String group_create_time = "";
    String group_desc = "";
    String group_master;
    String group_master2;
    
	public String getGroup_master()
	{
		return group_master;
	}
	public void setGroup_master(String group_master)
	{
		this.group_master = group_master;
	}
	public String getGroup_master2()
	{
		return group_master2;
	}
	public void setGroup_master2(String group_master2)
	{
		this.group_master2 = group_master2;
	}
	public String getGroup_desc()
	{
		return group_desc;
	}
	public void setGroup_desc(String group_desc)
	{
		this.group_desc = group_desc;
	}
	public String getGroupid()
	{
		return groupid;
	}
	public void setGroupid(String groupid)
	{
		this.groupid = groupid;
	}
	public String getGroup_name()
	{
		return group_name;
	}
	public void setGroup_name(String group_name)
	{
		this.group_name = group_name;
	}
	public String getGroup_master_uuid()
	{
		return group_master_uuid;
	}
	public void setGroup_master_uuid(String group_master_uuid)
	{
		this.group_master_uuid = group_master_uuid;
	}
	public String getGroup_master_uuid2()
	{
		return group_master_uuid2;
	}
	public void setGroup_master_uuid2(String group_master_uuid2)
	{
		this.group_master_uuid2 = group_master_uuid2;
	}
	public String getGroup_count()
	{
		return group_count;
	}
	public void setGroup_count(String group_count)
	{
		this.group_count = group_count;
	}
	public String getGroup_grade()
	{
		return group_grade;
	}
	public void setGroup_grade(String group_grade)
	{
		this.group_grade = group_grade;
	}
	public String getGroup_max_people()
	{
		return group_max_people;
	}
	public void setGroup_max_people(String group_max_people)
	{
		this.group_max_people = group_max_people;
	}
	public String getGroup_state()
	{
		return group_state;
	}
	public void setGroup_state(String group_state)
	{
		this.group_state = group_state;
	}
	public String getGroup_create_time()
	{
		return group_create_time;
	}
	public void setGroup_create_time(String group_create_time)
	{
		this.group_create_time = group_create_time;
	}
	public GroupEntity(String groupid, String group_name, String group_master_uuid, String group_master_uuid2, String group_count, String group_grade, String group_max_people, String group_state,
			String group_create_time)
	{
		super();
		this.groupid = groupid;
		this.group_name = group_name;
		this.group_master_uuid = group_master_uuid;
		this.group_master_uuid2 = group_master_uuid2;
		this.group_count = group_count;
		this.group_grade = group_grade;
		this.group_max_people = group_max_people;
		this.group_state = group_state;
		this.group_create_time = group_create_time;
	}
	@Override
	public String toString()
	{
		return "GroupEntity [groupid=" + groupid + ", group_name=" + group_name + ", group_master_uuid=" + group_master_uuid + ", group_master_uuid2=" + group_master_uuid2 + ", group_count="
				+ group_count + ", group_grade=" + group_grade + ", group_max_people=" + group_max_people + ", group_state=" + group_state + ", group_create_time=" + group_create_time + "]";
	}
    
    
}
