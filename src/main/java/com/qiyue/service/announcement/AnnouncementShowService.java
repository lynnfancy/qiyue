package com.qiyue.service.announcement;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiyue.entity.announcement.AnnouncementEntity;
import com.qiyue.handler.SqlHandler;

@Service
public class AnnouncementShowService {
	@Autowired
	private SqlHandler sh;
	
	public List<AnnouncementEntity> show(int page,String websiteCode,String categoryCode) throws Exception {
		String hql = "FROM AnnouncementEntity where ACR_WEBSITE_CODE = ?1 AND (?2 IS NULL OR ACR_CATEGORY_CODE = ?3) "
				+ "ORDER BY ACR_PUBLISH_TIME DESC";
		List<String>inputList = new ArrayList<String>();
		inputList.add(websiteCode);
		inputList.add(categoryCode);
		inputList.add(categoryCode);
		@SuppressWarnings("unchecked")
		List<AnnouncementEntity>list = (List<AnnouncementEntity>)sh.findByPage(hql,page,inputList);
		return list;
	}
	public List<?> navi() throws Exception {
		String sql = "select DISTINCT(ACR_WEBSITE_CODE),ACR_CATEGORY_CODE FROM announcement_collector_result";
		return sh.findByCustom(sql);
	}
	
	public int pageTotal(String websiteCode,String categoryCode) {
		String sql = "select count(1) FROM announcement_collector_result "
				+ "where ACR_WEBSITE_CODE = ?1 AND (?2 IS NULL OR ACR_CATEGORY_CODE = ?3)";
		List<String>inputList = new ArrayList<String>();
		inputList.add(websiteCode);
		inputList.add(categoryCode);
		inputList.add(categoryCode);
		@SuppressWarnings("unchecked")
		List<BigInteger> list = (List<BigInteger>)sh.findByCustom(sql, inputList);
		return list.get(0).intValue();
	}
}
