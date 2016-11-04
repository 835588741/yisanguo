package com.springmvc.framework;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
public abstract class IBaseDao{
	protected Logger log = Logger.getLogger(this.getClass().getName());
	@Autowired
	protected BaseDao baseDao;

	protected BaseDao getBaseDao() {
		return baseDao; 
	}

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
}
