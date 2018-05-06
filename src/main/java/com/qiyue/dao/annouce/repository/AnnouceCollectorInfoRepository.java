package com.qiyue.dao.annouce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qiyue.dao.annouce.entity.AnnouceCollectorInfo;
import com.qiyue.dao.annouce.entity.AnnouceCollectorInfoUnionKey222;

public interface AnnouceCollectorInfoRepository extends JpaRepository<AnnouceCollectorInfo, String> {

	@Modifying
	@Query("update AnnouceCollectorInfo set ACI_STATE = '1' where ACI_FLOW_NO = ?1 ")
	int aciDelete(String flowNo);
	
	List<AnnouceCollectorInfo> findByAwiFlowNoAndStateOrderByModifyTimeDesc(String awiFlowNo,String state);
	
}
