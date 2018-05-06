package com.qiyue.interceptor;

import java.util.Arrays;
import java.util.Map;

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

import com.qiyue.redis.RedisHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SessionInterceptor implements HandlerInterceptor {

	@Autowired
	private RedisHandler rh;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String requestDo = request.getRequestURI();
		log.info("请求:{}",request.getContextPath() + requestDo);
		Map<String,String[]> map = request.getParameterMap();
		map.forEach((k,v)->log.info("输入参数:{},{}",k,Arrays.toString(v)));
		String redisSessionId = (String)rh.get("sessionId");
		String sessionId = request.getSession().getId();
		if (sessionId.equals(redisSessionId)) {
			return true;
		} else {
			String requestType = request.getHeader("X-Requested-With");
			if ("XMLHttpRequest".equals(requestType)) {
				response.sendError(999);
			}
			response.sendRedirect(request.getContextPath() + "/");
			return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}

}
