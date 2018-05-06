package com.qiyue.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.qiyue.interceptor.LoginInterceptor;
import com.qiyue.dao.user.manage.UserEntityManager;
import com.qiyue.interceptor.ContextPathInterceptor;
import com.qiyue.interceptor.RefreshRedisAwiInterceptor;
import com.qiyue.interceptor.SessionInterceptor;
import com.qiyue.redis.RedisHandler;

/**
//@EnableWebMvc//不要加此注解，与WebMvcConfigurationSupport不共存
 * <p>If {@link WebMvcConfigurer} does not expose some more advanced setting that
 * needs to be configured consider removing the {@code @EnableWebMvc}
 * annotation and extending directly from {@link WebMvcConfigurationSupport}
 * or {@link DelegatingWebMvcConfiguration}, e.g.:
 *
 */
@EnableTransactionManagement
@SpringBootApplication
@Component
public class WebMvcConfig extends WebMvcConfigurationSupport {
	
	@Autowired
	public UserEntityManager uem;
	
	/**
	 * 注册拦截器时 实现静态资源的映射
	 */
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**")//访问路径
		.addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/");//实际路径
	    registry.addResourceHandler("/templates/**")
	    .addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX +"/templates/");
		super.addResourceHandlers(registry);
	}

	/**
	 * 注册拦截器
	 */
	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		//静态资源
		List<String> htmlSourcePaths = new ArrayList<String>();
		htmlSourcePaths.add("/static/**");
		htmlSourcePaths.add("/templates/**");
		//校验session
		List<String> excludePaths = new ArrayList<String>();
		excludePaths.add("/");
		excludePaths.add("/login");
		registry.addInterceptor(getSessionInterceptor())
			.addPathPatterns("/**")//拦截的请求
			.excludePathPatterns(excludePaths)
			.excludePathPatterns(htmlSourcePaths);//不拦截的请求
		
		//校验登录
		registry.addInterceptor(getLoginInterceptor()).addPathPatterns("/login");
		
		//刷新redis中的awi集合
		List<String> rraPaths = new ArrayList<String>();
		rraPaths.add("/annouce/collector/awiAdd");
		rraPaths.add("/annouce/collector/awiModify");
		rraPaths.add("/annouce/collector/awiDelete");
		rraPaths.add("/annouce/collector/aciAdd");
		rraPaths.add("/annouce/collector/aciModify");
		rraPaths.add("/annouce/collector/aciDelete");
		registry.addInterceptor(getRefreshRedisAwiInterceptor()).addPathPatterns(rraPaths);
		
		//获取项目名称，校正html页面引用资源路径
//		List<String> contextUtlPaths = new ArrayList<String>();
//		contextUtlPaths.add("/index");
//		contextUtlPaths.add("/annouce/index");
//		contextUtlPaths.add("/annouce/collector/");
//		contextUtlPaths.add("/clan");
		List<String> contextUtlPaths = uem.findAllNavi();
		contextUtlPaths.add("/");
		contextUtlPaths.add("/login");
		registry.addInterceptor(getContextPathInterceptor())
			.addPathPatterns(contextUtlPaths);
		
		
		super.addInterceptors(registry);
	}
	
	@Bean
	SessionInterceptor getSessionInterceptor() {
		return new SessionInterceptor();
	}
	
	@Bean
	LoginInterceptor getLoginInterceptor() {
		return new LoginInterceptor();
	}
	
	@Bean
	RefreshRedisAwiInterceptor getRefreshRedisAwiInterceptor() {
		return new RefreshRedisAwiInterceptor();
	}
	
	@Bean
	ContextPathInterceptor getContextPathInterceptor() {
		return new ContextPathInterceptor();
	}
	
}


