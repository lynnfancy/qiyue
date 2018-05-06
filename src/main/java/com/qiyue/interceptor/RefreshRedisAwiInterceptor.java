package com.qiyue.interceptor;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.qiyue.dao.annouce.entity.AnnouceWebsiteInfo;
import com.qiyue.dao.annouce.manage.AnnouceEntityManager;
import com.qiyue.redis.RedisHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RefreshRedisAwiInterceptor  implements HandlerInterceptor {
	@Autowired
	private RedisHandler rh;
	@Autowired
	private AnnouceEntityManager aem;
	
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String username = (String)rh.get("username");
		if ("observer".equals(username)) {
			return false;
		}
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}



	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		List<AnnouceWebsiteInfo> awiList = aem.findAllAwi();
		for (AnnouceWebsiteInfo awi : awiList) {
			awi.getAciList().addAll(aem.findAllAci(awi.getFlowNo()));
		}
		rh.set("awiList", awiList);
		log.info("更新redis缓存成功awiList:{}",awiList);
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

}
