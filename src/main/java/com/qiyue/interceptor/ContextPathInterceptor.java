package com.qiyue.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class ContextPathInterceptor implements HandlerInterceptor {
	@Value("${context.url.absoulte}")
	private String contextUrl;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		modelAndView.addObject("contextUrl", contextUrl);
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

}
