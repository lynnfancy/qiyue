package com.qiyue.dao.annouce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qiyue.dao.annouce.entity.AnnouceCollectorResult;

public interface AnnouceCollectorResultRepository extends JpaRepository<AnnouceCollectorResult,String> {
	
	int countByAwiFlowNoAndAciFlowNo(String awiFlowNo,String aciFlowNo) ;
	
	@Query(value="select ACR_ACI_FLOW_NO ,max(ACR_PUBLISH_TIME) from ANNOUCE_COLLECTOR_RESULT where ACR_AWI_FLOW_NO = ?1 GROUP BY ACR_ACI_FLOW_NO",nativeQuery =true)
	List<Object[]> findLatestPublishTime(String aciFlowNo);
}
