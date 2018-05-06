package com.qiyue.controller.annouce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiyue.constant.Constant;
import com.qiyue.dao.annouce.entity.AnnouceCollectorInfo;
import com.qiyue.dao.annouce.entity.AnnouceCollectorInfoUnionKey222;
import com.qiyue.dao.annouce.entity.AnnouceCollectorResult;
import com.qiyue.dao.annouce.entity.AnnouceWebsiteInfo;
import com.qiyue.dao.annouce.manage.AnnouceEntityManager;
import com.qiyue.service.annouce.AnnouceService;
import com.qiyue.util.DateUtil;
import com.qiyue.util.ZipUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/annouce")
@Controller
public class AnnouceController {
	@Autowired
	private AnnouceService as;
	
	@RequestMapping(value="/collect",method=RequestMethod.GET)
	@ResponseBody
	public String collect(String websiteCode) {
		try {
			as.collect();
//			acs.collect(websiteCode);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return "success";  
	}
	
	@RequestMapping("/index")
	String annouceIndex() {
		return "annouce/annouceIndex";
	}
	
	@RequestMapping("/awiList")
	@ResponseBody
	List<AnnouceWebsiteInfo> awiList () throws Exception{
		return as.awiList();
	}

	@RequestMapping("/titleShow")
	@ResponseBody
	List<AnnouceCollectorResult> titleShow (int page,String awiFlowNo,String aciFlowNo) throws Exception{
		return as.titleShow(page,awiFlowNo,aciFlowNo);
	}
	
	@RequestMapping("/pagination")
	@ResponseBody
	int pageTotal (String awiFlowNo,String aciFlowNo) throws Exception{
		return as.countByAwiFlowNoAndAciFlowNo(awiFlowNo, aciFlowNo);
	}
	
	@RequestMapping("/collector")
	String collectorShow () throws Exception{
		return "annouce/collector";
	}
	
	@RequestMapping("/collector/awiShow")
	@ResponseBody
	List<AnnouceWebsiteInfo> awiShow () throws Exception{
		return as.awiShow();
	}
	
	@RequestMapping("/collector/awiAdd")
	@ResponseBody
	void awiAdd (AnnouceWebsiteInfo awi) throws Exception {
		as.awiAdd(awi);
	}
	
	@RequestMapping("/collector/awiModify")
	@ResponseBody
	AnnouceWebsiteInfo awiModify (AnnouceWebsiteInfo awi) {
		return as.awiModify(awi); 
	}
	
	@RequestMapping("/collector/awiDelete")
	@ResponseBody
	int awiDelete (String flowNo) {
		return as.awiDelete(flowNo);
	}
	
	@RequestMapping("/collector/aciShow")
	@ResponseBody
	public List<AnnouceCollectorInfo> aciShow(String awiFlowNo) {
		return as.aciShow(awiFlowNo);
	}
	
	@RequestMapping("/collector/aciAdd")
	@ResponseBody
	void aciAdd (AnnouceCollectorInfo aci) {
		as.aciAdd(aci);
	}
	
	@RequestMapping("/collector/aciModify")
	@ResponseBody
	void aciModify (AnnouceCollectorInfo aci) {
		as.aciModify(aci);
	}
	
	@RequestMapping("/collector/aciDelete")
	@ResponseBody
	int aciDelete (String flowNo) {
		return as.aciDelete(flowNo);
	}
	
/*	
	@RequestMapping("/collector/awiRefresh")
	@ResponseBody
	void awiRefresh () {
		acs.awiRefresh();
	}
	
 */
	
	/** 测试区       */
	
	@RequestMapping("/collector/aciTestAdd")
	@ResponseBody
	void aciTestAdd (AnnouceCollectorInfo aci) throws Exception{
		as.aciTestAdd(aci);
	}
	
	@RequestMapping("/collector/aciTestShow")
	@ResponseBody
	List<AnnouceCollectorResult> aciTestShow () throws Exception{
		return as.aciTestShow();
	}
	
}
