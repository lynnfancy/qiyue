package com.qiyue.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.qiyue.constant.Constant;

public class HtmlUtil {
	public static String formatUrl(String currentUrl,String nextUrl) {
		String resutlUrl = "";
		StringBuffer sb = new StringBuffer();
		String origUrl = "";
		Pattern cp = Pattern.compile(Constant.REGEX_ABSOLUTE_URL);
		Matcher cm = cp.matcher(currentUrl);
		List<String> list = new ArrayList<String>();
		if (cm.find()){
			origUrl = cm.group(0);
			currentUrl = currentUrl.substring(cm.end(0));
			Pattern lp = Pattern.compile(Constant.REGEX_URL_LEVEL_NAME);
			Matcher lm = lp.matcher(currentUrl);
			while (lm.find()){
				list.add(lm.group());
			}
		}
		sb.append(origUrl);
		int s = list.size();
		if(s>0){//
			list.remove(s-1);
		}
		
		if (Pattern.matches(Constant.REGEX_ABSOLUTE_URL, nextUrl)){//
			resutlUrl = nextUrl;
		} else if (Pattern.matches(Constant.REGEX_RELATIVE_URL, nextUrl)){
			if (nextUrl.startsWith(Constant.SYMBOL_UP_LEVEL)){
				Pattern p = Pattern.compile(Constant.REGEX_UP_LEVEL);
				Matcher m = p.matcher(nextUrl);
				int cn = 0;
				while (m.find()){
					cn++;
				}
				for (int i=0;i<list.size()-cn;i++){
					sb.append(list.get(i));
				}
				sb.append(Constant.SYMBOL_SEPARATOR);
				nextUrl = nextUrl.substring(cn*3);
				sb.append(nextUrl);
			} else if (nextUrl.startsWith(Constant.SYMBOL_SAME_LEVEL)){
				for (int i=0;i<list.size();i++){
					sb.append(list.get(i));
				}
				sb.append(Constant.SYMBOL_SEPARATOR).append(nextUrl.substring(2));
			} else if (nextUrl.startsWith(Constant.SYMBOL_SEPARATOR)){
				sb.append(Constant.SYMBOL_SEPARATOR).append(nextUrl.substring(1));
			} else {
				for (int i=0;i<list.size();i++){
					sb.append(list.get(i));
				}
				sb.append(Constant.SYMBOL_SEPARATOR).append(nextUrl);
			}
			resutlUrl = sb.toString();
		}
		return resutlUrl;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean putMultilevelMap(Map<String,Object> origMap,String key,Object value){
		while (true){
			if (origMap.containsKey(key)){
				origMap.put(key, value);
				return true;
			} else {
				Iterator<Entry<String,Object>> i = origMap.entrySet().iterator();
				while (i.hasNext()){
					Entry<String,Object> e = i.next();
					Object o = e.getValue();
					if (o==null){
						continue;
					} else if(o instanceof Map ) {
						if (((Map) o).containsKey(key)){
							Map m =(Map) origMap.get(e.getKey());
							m.put(key, value);
							return true;
						} else {
							if (putMultilevelMap((Map) o,key,value)){
								return true;
							} else {
								continue;
							}
						}
					} 
				}
				return false;
			}
		}
	}

	
}
