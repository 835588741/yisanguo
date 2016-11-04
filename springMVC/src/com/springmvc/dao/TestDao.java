package com.springmvc.dao;

import org.springframework.stereotype.Component;

import com.springmvc.framework.IBaseDao;
@Component
public class TestDao extends IBaseDao{

	public String[] dosomething() {
//		String[] row = this.baseDao.getRow("select 1 from dual");
//		row = this.baseDao.getRow("select * from book where book_id=?",new Object[]{"1"});
//		return row;
		return new String[]{"1","2","3"};
	}

}
