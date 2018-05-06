package com.qiyue.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.qiyue.dao.annouce.entity.AnnouceWebsiteInfo;
import com.qiyue.dao.annouce.manage.AnnouceEntityManager;
import com.qiyue.dao.annouce.repository.AnnouceCollectorInfoRepository;
import com.qiyue.dao.annouce.repository.AnnouceWebsiteInfoRepository;
import com.qiyue.dao.user.entity.NavigationEntity;
import com.qiyue.dao.user.repository.NavigationRepository;
import com.qiyue.node.Node;
import com.qiyue.node.NodeTree;
import com.qiyue.redis.RedisHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(value=1)
public class RedisCommandLineRunner implements CommandLineRunner {
	@Autowired
	private RedisHandler rh;

	@Autowired
	private AnnouceWebsiteInfoRepository awir; 
	@Autowired
	private AnnouceCollectorInfoRepository acir; 

	@Override
	public void run(String... args) throws Exception {
		List<AnnouceWebsiteInfo> awiList = awir.findByStateOrderByModifyTimeDesc("0");
		for (AnnouceWebsiteInfo awi : awiList) {
			awi.getAciList().addAll(acir.findByAwiFlowNoAndStateOrderByModifyTimeDesc(awi.getFlowNo(), "0"));
		}
		rh.set("awiList", awiList);
		log.info("加载redis缓存:网站设置信息,awiList:{}",awiList);
		

		
	}

}
