package com.qiyue.interceptor;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.qiyue.dao.user.entity.NavigationEntity;
import com.qiyue.dao.user.repository.NavigationRepository;
import com.qiyue.node.Node;
import com.qiyue.node.NodeTree;
import com.qiyue.redis.RedisHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private RedisHandler rh;
	
	@Autowired
	private NavigationRepository nr;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
//		ServletContext servletContext = request.getServletContext();
//		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
//		RedisHandler rh = (RedisHandler)ctx.getBean("redisHandler");
		String username = request.getParameter("username");
		String sessionId = request.getSession().getId();
		rh.set("username", username);
		rh.set("sessionId", sessionId);
		log.info("redis初始化用户信息:用户名,username:{}",username);
		
		List<NavigationEntity> naviList = nr.getNaviList((String)rh.get("username"));
		Node node = new Node();
		naviList.forEach(ne->{
			NodeTree.insert(node, ne);
		});
		rh.set("naviNode", node);
		log.info("redis初始化用户信息:导航栏信息,naviNode:{}",node);
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

}
