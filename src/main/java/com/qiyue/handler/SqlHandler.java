package com.qiyue.handler;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SqlHandler {
	@PersistenceContext
	private EntityManager em ;
	
	@Value("${sql.query.default.size}")
	private int defaultQuerySize;
	@Value("${sql.batch.size}")
	private int batchSize;
	@Value("${sql.query.page.size}")
	private int pageSize;
	
	public SqlHandler() {
		super();
	}
	
	/**
	 * 分页查询
	 * @param hql
	 * @param currentPage
	 * @return
	 */
	public List<?> findByPage(String hql,int currentPage) {
		Query query = em.createQuery(hql);
		query.setFirstResult(startPos(currentPage,this.pageSize));
		query.setMaxResults(this.pageSize);
		List<?> list = query.getResultList();
		em.close();
		return list;
	}
	
	/**
	 * 分页查询
	 * @param hql
	 * @param currentPage
	 * @param inputList
	 * @return
	 */
	public List<?> findByPage(String hql,int currentPage,List<String>inputList) {
		Query query = em.createQuery(hql);
		for (int i=0;i<inputList.size();i++) {
			query.setParameter(i+1, inputList.get(i));
		}
		query.setFirstResult(startPos(currentPage,this.pageSize));
		query.setMaxResults(this.pageSize);
		List<?> list = query.getResultList();
		em.close();
		return list;
	}
	
	public List<?> findByCustom(String sql){
		Query query = em.createNativeQuery(sql);
		List<?> list = query.getResultList();
		em.close();
		return list;
	}
	
	public List<?> findByCustom(String sql,List<String>inputList){
		Query query = em.createNativeQuery(sql);
		for (int i=0;i<inputList.size();i++) {
			query.setParameter(i+1, inputList.get(i));
		}
		List<?> list = query.getResultList();
		em.close();
		return list;
	}
	
	public int startPos(int currentPage,int pageSize) {
		return (currentPage-1)*pageSize;
	}
	
	public int endPos(int currentPage,int pageSize) {
		return currentPage*pageSize;
	}
	
	@Transactional
	public Object findOne(String hql) {
		Query query = em.createQuery(hql);
		query.setMaxResults(1);
		Object obj = null;
		if (query.getFirstResult()!=0) {
			obj = query.getSingleResult();
		}
		clear();
		return obj;
	}
	
	@Transactional
	public Object findOne(String hql,List<String>inputList) {
		Query query = em.createQuery(hql);
		for (int i=0;i<inputList.size();i++) {
			query.setParameter(i+1, inputList.get(i));
		}
		query.setMaxResults(1);
		Object obj = null;
		if (query.getFirstResult()!=0) {
			obj = query.getSingleResult();
		}
		clear();
		return obj;
	}
	
	@Transactional
	public void batchMerge(List<?> list) {
		for (int i=0;i<list.size();i++) {
			em.merge(list.get(i));
			if (i==batchSize) {
				em.flush();
				em.clear();
			}
		}
		em.flush();
		em.clear();
		em.close();
	}
	
	public void clear() {
		em.clear();
		em.close();
	}
	
}
