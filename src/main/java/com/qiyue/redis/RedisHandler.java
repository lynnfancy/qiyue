package com.qiyue.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisHandler {
//	@Autowired
//	private StringRedisTemplate srt;
	
	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<Object, Object> rt;

	public void set(String key,Object value) {
		rt.opsForValue().set(key, value);;
	}
	
	public Object get(String key) {
		return rt.opsForValue().get(key);
	}
	
	public boolean deleteKey(String key) {
		return rt.delete(key);
	}
}
