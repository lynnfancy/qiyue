package com.qiyue.dao.annouce.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.qiyue.dao.annouce.entity.AnnouceCollectorInfo;
import com.qiyue.dao.annouce.entity.AnnouceCollectorInfoUnionKey222;
import com.qiyue.dao.annouce.entity.AnnouceCollectorResult;
import com.qiyue.dao.annouce.entity.AnnouceWebsiteInfo;
import com.qiyue.util.BaseUtil;

import io.netty.util.internal.StringUtil;

@Component
public class AnnouceEntityManager {
	@PersistenceContext(unitName="annouce")
	private EntityManager annouce ;
	
	private Query query ;
	
//	@Value("${sql.query.default.size}")
//	private int defaultQuerySize;
	@Value("${sql.batch.size}")
	private int batchSize;
	@Value("${sql.query.page.size}")
	private int pageSize;
	
	/**
	 * 分页查询
	 * @param hql
	 * @param currentPage
	 * @return
	 */
	public List<?> findByPage(String hql,int currentPage) {
		query = annouce.createQuery(hql);
		query.setFirstResult(startPos(currentPage,this.pageSize));
		query.setMaxResults(this.pageSize);
		List<?> list = query.getResultList();
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
		query = annouce.createQuery(hql);
		for (int i=0;i<inputList.size();i++) {
			query.setParameter(i+1, inputList.get(i));
		}
		query.setFirstResult(startPos(currentPage,this.pageSize));
		query.setMaxResults(this.pageSize);
		List<?> list = query.getResultList();
		return list;
	}
	
	public List<?> findByCustom(String sql){
		query = annouce.createNativeQuery(sql);
		List<?> list = query.getResultList();
		return list;
	}
	
	public List<Object[]> findByCustom(String sql,List<String>inputList){
		query = annouce.createNativeQuery(sql);
		for (int i=0;i<inputList.size();i++) {
			query.setParameter(i+1, inputList.get(i));
		}
		@SuppressWarnings("unchecked")
		List<Object[]> list = query.getResultList();
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
		query = annouce.createQuery(hql);
		query.setMaxResults(1);
		Object obj = null;
		if (query.getFirstResult()!=0) {
			obj = query.getSingleResult();
		}
		return obj;
	}
	
	@Transactional
	public Object findOne(String hql,List<String>inputList) {
		query = annouce.createQuery(hql);
		for (int i=0;i<inputList.size();i++) {
			query.setParameter(i+1, inputList.get(i));
		}
		query.setMaxResults(1);
		Object obj = null;
		if (query.getFirstResult()!=0) {
			obj = query.getSingleResult();
		}
		return obj;
	}
	
	@Transactional
	public void batchMerge(List<?> list) {
		for (int i=0;i<list.size();i++) {
			annouce.merge(list.get(i));
			if (i==batchSize) {
				annouce.flush();
			}
		}
		annouce.flush();
	}
	
	@Transactional
	public Object merge(Object obj) {
		obj = annouce.merge(obj);
		annouce.flush();
		return obj;
	}

	@SuppressWarnings("unchecked")
	public List<AnnouceWebsiteInfo> findAllAwi() {
		String hql = "from AnnouceWebsiteInfo where AWI_STATE = ?1";
		query = annouce.createQuery(hql);
		query.setParameter(1, "0");
		return (List<AnnouceWebsiteInfo>)query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<AnnouceCollectorInfo> findAllAci(String awiFlowNo) {
		String hql = "from AnnouceCollectorInfo where ACI_AWI_FLOW_NO = ?1 AND ACI_STATE = ?2";
		query = annouce.createQuery(hql);
		query.setParameter(1, awiFlowNo);
		query.setParameter(2, "0");
		return (List<AnnouceCollectorInfo>)query.getResultList();
	}
	
	
	public void close() {
		annouce.flush();
		annouce.clear();
		annouce.close();
	}
}
