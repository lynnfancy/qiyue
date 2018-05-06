package com.qiyue.dao.annouce.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qiyue.dao.annouce.entity.AnnouceCollectorInfo;
import com.qiyue.dao.annouce.entity.AnnouceWebsiteInfo;

public interface AnnouceWebsiteInfoRepository extends JpaRepository<AnnouceWebsiteInfo, String> {
	
	@Modifying
	@Query("update AnnouceWebsiteInfo set AWI_WEBSITE_NAME = ?1,AWI_WEBSITE_URL = ?2,AWI_MODIFY_TIME = ?3,AWI_MODIFY_USERNAME = ?4 where AWI_WEBSITE_CODE = ?5")
	int updateWebsiteNameAndWebsiteUrl(String websiteName,String websiteUrl,String modifyTime,String modifyUsername,String websiteCode);

	@Modifying
	@Query("update AnnouceWebsiteInfo set AWI_STATE = '1' where AWI_FLOW_NO = ?1")
	int awiDelete(String flowNo);
	
	List<AnnouceWebsiteInfo> findByStateOrderByModifyTimeDesc(String state);
}
