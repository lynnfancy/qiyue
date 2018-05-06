package com.qiyue.service.annouce;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.qiyue.constant.Constant;
import com.qiyue.dao.annouce.entity.AnnouceCollectorInfo;
import com.qiyue.dao.annouce.entity.AnnouceCollectorInfoUnionKey222;
import com.qiyue.dao.annouce.entity.AnnouceCollectorResult;
import com.qiyue.dao.annouce.entity.AnnouceWebsiteInfo;
import com.qiyue.dao.annouce.manage.AnnouceEntityManager;
import com.qiyue.dao.annouce.repository.AnnouceCollectorInfoRepository;
import com.qiyue.dao.annouce.repository.AnnouceCollectorResultRepository;
import com.qiyue.dao.annouce.repository.AnnouceWebsiteInfoRepository;
import com.qiyue.handler.AnnouceCollectorHandler;
import com.qiyue.redis.RedisHandler;
import com.qiyue.util.DateUtil;
import com.qiyue.util.HtmlUtil;
import com.qiyue.util.SqlUtil;
import com.qiyue.util.ZipUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AnnouceService {
	@Autowired
	private AnnouceCollectorHandler ach;
	@Autowired
	private RedisHandler rh;
	@Autowired
	private AnnouceEntityManager aem;
	@Autowired
	private AnnouceWebsiteInfoRepository awir;
	@Autowired
	private AnnouceCollectorInfoRepository acir;
	@Autowired
	private AnnouceCollectorResultRepository acrr;

	/*
	 * 自动任务
	 */
	@Async
	@Scheduled(cron="0 0 0/1 * * *")
	public void collect() throws Exception {
		ach.collect();
	}
	
	public void collect(String websiteCode) throws Exception {
		ach.collect(websiteCode);
	}
	
	public List<AnnouceWebsiteInfo> getAwiList() {
		List<AnnouceWebsiteInfo> awiList = awir.findByStateOrderByModifyTimeDesc("0");
		for (AnnouceWebsiteInfo awi : awiList) {
			awi.getAciList().addAll(acir.findByAwiFlowNoAndStateOrderByModifyTimeDesc(awi.getFlowNo(), "0"));
		}
		return awiList;
	}
	
	/*
	 * 增删改
	 */
	
	public AnnouceWebsiteInfo awiAdd(AnnouceWebsiteInfo awi) throws Exception {
		String url = awi.getWebsiteUrl();
		List<String> urlSplit = HtmlUtil.urlSpilt(url);
		String domain =  urlSplit.get(2).replaceAll(Constant.SYMBOL_SEPARATOR, Constant.SYMBOL_NULL_STRING);
		domain = domain.replaceAll(Constant.REGEX_DOT, Constant.SYMBOL_NULL_STRING);
		domain = domain.replaceAll("www", Constant.SYMBOL_NULL_STRING);
		domain = domain.replaceAll("cn$", Constant.SYMBOL_NULL_STRING);
		awi.setWebsiteCode(domain);
		
		awi.setCreateTime(DateUtil.getSystemTime());
		awi.setCreateUsername((String)rh.get("username"));
		awi.setModifyTime(DateUtil.getSystemTime());
		awi.setModifyUsername((String)rh.get("username"));
		awi.setState("0");
		awi = awir.save(awi);
		return awi;
	}
	
	@Transactional
	public Optional<AnnouceWebsiteInfo> findAwiById(String flowNo) {
		return awir.findById(flowNo);
	}
	
//	@Transactional
	public AnnouceWebsiteInfo awiModify(AnnouceWebsiteInfo awi) {
		awi.setModifyTime(DateUtil.getSystemTime());
		awi.setModifyUsername((String)rh.get("username"));
		Optional<AnnouceWebsiteInfo> awiOp = findAwiById(awi.getFlowNo());
		awi = (AnnouceWebsiteInfo)SqlUtil.updateNoNull(awi, awiOp.get());
		awi = awir.saveAndFlush(awi);
		return awi;
	}
	
	@Transactional
	public int awiDelete(String flowNo) {
		int count = awir.awiDelete(flowNo);
		return count;
	}
	
	public AnnouceCollectorInfo aciAdd(AnnouceCollectorInfo aci) {
		String username = (String)rh.get("username");
		String systemTime = DateUtil.getSystemTime();
		aci.setState("0");
		aci.setCreateTime(systemTime);
		aci.setCreateUsername(username);
		aci.setModifyTime(systemTime);
		aci.setModifyUsername(username);
		aci = acir.save(aci);
		return aci;
	}
	
	public Optional<AnnouceCollectorInfo> findAciById(String flowNo) {
		return acir.findById(flowNo);
	}
	
	public AnnouceCollectorInfo aciModify(AnnouceCollectorInfo aci) {
		aci.setModifyTime(DateUtil.getSystemTime());
		aci.setModifyUsername((String)rh.get("username"));
		Optional<AnnouceCollectorInfo> aciOp = findAciById(aci.getFlowNo());
		aci = (AnnouceCollectorInfo)SqlUtil.updateNoNull(aci, aciOp.get());
		aci = acir.save(aci);
		
		return aci;
	}
	
	@Transactional
	public int aciDelete(String flowNo) {
		int count = acir.aciDelete(flowNo);
		return count;
	}
	
	/*
	 * 显示区
	 */
	
	public List<AnnouceWebsiteInfo> awiShow() throws Exception {
		List<AnnouceWebsiteInfo> awiList = awir.findByStateOrderByModifyTimeDesc("0");
		awiList.forEach(awi->{
			try {
				awi.setCreateTime(DateUtil.formatDate(awi.getCreateTime(), Constant.DATE_FORMATER_WITH_HYPHEN));
				awi.setModifyTime(DateUtil.formatDate(awi.getModifyTime(), Constant.DATE_FORMATER_WITH_HYPHEN));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		});
		return awiList;
	}
	
	public List<AnnouceCollectorInfo> aciShow(String awiFlowNo){
		List<AnnouceCollectorInfo> aciList = acir.findByAwiFlowNoAndStateOrderByModifyTimeDesc(awiFlowNo,"0");
		aciList.forEach(aci->{
			try {
				aci.setCreateTime(DateUtil.formatDate(aci.getCreateTime(), Constant.DATE_FORMATER_WITH_HYPHEN));
				aci.setModifyTime(DateUtil.formatDate(aci.getModifyTime(), Constant.DATE_FORMATER_WITH_HYPHEN));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		});
		return aciList;
	}
	
	public List<AnnouceWebsiteInfo> awiList(){
		@SuppressWarnings("unchecked")
		List<AnnouceWebsiteInfo> awiList = (List<AnnouceWebsiteInfo>) rh.get("awiList");
		return awiList;
	}
	
	
	public List<AnnouceCollectorResult> titleShow(int page,String awiFlowNo,String aciFlowNo) throws Exception {
		String hql = "FROM AnnouceCollectorResult where ACR_AWI_FLOW_NO = ?1 AND ACR_ACI_FLOW_NO = ?2 "
				+ "ORDER BY ACR_PUBLISH_TIME DESC,ACR_RECORD_TIME DESC";
		List<String>inputList = new ArrayList<String>();
		inputList.add(awiFlowNo);
		inputList.add(aciFlowNo);
		@SuppressWarnings("unchecked")
		List<AnnouceCollectorResult>list = (List<AnnouceCollectorResult>)aem.findByPage(hql,page,inputList);
		return dealAcrList(list);
	}
	
	public List<AnnouceCollectorResult> dealAcrList(List<AnnouceCollectorResult>list){
		list = list.stream().map(acr->{
			try {
				acr.setText(ZipUtil.unGzip(acr.getText()));
				acr.setPublishTime(DateUtil.formatDate(acr.getPublishTime(), Constant.DATE_FORMATER_WITH_HYPHEN));
				acr.setRecordTime(DateUtil.formatDate(acr.getRecordTime(), Constant.DATE_FORMATER_WITH_HYPHEN));
			} catch (Exception e) {
				log.error("格式化处理标题列表异常");
				e.printStackTrace();
			}
			return acr;
		})
		.collect(Collectors.toList());
		return list;
	}

	public int countByAwiFlowNoAndAciFlowNo(String awiFlowNo,String aciFlowNo) {
		return acrr.countByAwiFlowNoAndAciFlowNo(awiFlowNo, aciFlowNo);
	}
	
	/** 测试区 */    
	
	public void aciTestAdd(AnnouceCollectorInfo aci) throws Exception {
		
		@SuppressWarnings("unchecked")
		List<AnnouceWebsiteInfo> awiList = (List<AnnouceWebsiteInfo>)rh.get("awiList");
		Optional<AnnouceWebsiteInfo> awiOp = awiList.stream().filter(awi->{
			return awi.getFlowNo().equals(aci.getAwiFlowNo());
		})
		.findFirst();
		AnnouceWebsiteInfo awi = null;
		if (awiOp.isPresent()) {
			awi = awiOp.get();
			awi.getAciList().clear();
			awi.getAciList().add(aci);
		}
		rh.set("awiTest", awi);
	}
	
	public List<AnnouceCollectorResult> aciTestShow() throws Exception{
		AnnouceWebsiteInfo awi = (AnnouceWebsiteInfo)rh.get("awiTest");
		log.debug("awi:{}",awi);
		List<AnnouceCollectorResult> list = ach.collect(awi);
		return dealAcrList(list);
	}

}
