package com.qiyue.handler;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.qiyue.interf.encrypt.EncryptInterface;
import com.qiyue.util.BaseUtil;
import com.qiyue.util.Md5Util;

/**
 * 参数MD5加密处理
 * @author muxianliangqin
 *
 */
public class ParamsHandler{

	private Map<String, String> paramsMap;
	private String apiSecret;

	public ParamsHandler() {
		TreeMap<String, String> map = new TreeMap<String,String>();
		this.paramsMap = map;
	}
	
	public void put(String key,String value){
		if (!BaseUtil.isEmpty(value)){
			this.paramsMap.put(key, value);
		}
	}
	
	public void clear(){
		this.paramsMap.clear();
	}
	
	public String md5Encrypt() throws Exception{
		StringBuffer sb = new StringBuffer();
		Iterator<Entry<String, String>> iterator = this.paramsMap.entrySet().iterator();
		while (iterator.hasNext()){
			Entry<String,String> entry = iterator.next();
			if (BaseUtil.isEmpty(entry.getValue())){
				continue;
			} else {
				sb.append(entry.getKey());
				sb.append("=");
				sb.append(entry.getValue());
				sb.append("&");
			}
		}
		sb.append("apiSecret=");
		sb.append(this.apiSecret);
		String md5 = "";
		try {
			System.out.println(sb.toString());
			EncryptInterface ei = new Md5Util();
			md5 = ei.encrypt(sb.toString());
		} catch (Exception e) {
			throw new Exception("MD5加密失败");
		}
		return md5;
	}
	

	public String getApiSecret() {
		return apiSecret;
	}

	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}
	
	public Map<String, String> getParamsMap() {
		return this.paramsMap;
	}

	public void setParamsMap(Map<String, String> paramsMap) {
		Iterator<Entry<String, String>> iterator = paramsMap.entrySet().iterator();
		while (iterator.hasNext()){
			Entry<String,String> entry = iterator.next();
			if (!BaseUtil.isEmpty(entry.getValue())){
				this.paramsMap.put(entry.getKey(), entry.getValue());
			} else {
				continue;
			}
		}
	}

}
