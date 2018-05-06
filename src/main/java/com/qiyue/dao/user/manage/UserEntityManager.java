package com.qiyue.dao.user.manage;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

@Component
public class UserEntityManager {
	@PersistenceContext(unitName="user")
	private EntityManager user ;
	
	private Query query ;
	
	public List<String> findAllNavi() {
		String sql = "SELECT UNI_URL FROM USER_NAVIGATION_INFO";
		query = user.createNativeQuery(sql);
		@SuppressWarnings("unchecked")
		List<String> resultList = query.getResultList();
		return resultList;
	}
}
