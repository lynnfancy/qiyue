package com.qiyue.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.qiyue.constant.Constant;
import com.qiyue.util.web.http.HttpUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HtmlUtil {
	
	public static String urlFormater(String currentUrl,String nextUrl) throws Exception {
		List<String> cList = urlSpilt(currentUrl);
		List<String> nList = urlSpilt(nextUrl);
		if (Pattern.matches(Constant.REGEX_URL_ABSOLUTE, nextUrl)) {
			if (!cList.get(2).equals(nList.get(2))) {
				return currentUrl;
			}
			return nextUrl;
		}
		List<String> nList_clone = new ArrayList<String>();
		nList_clone.addAll(nList);
		for(int i=0;i<nList.size();i++) {
			String str = nList.get(i);
			if (i==0) {
				if (Constant.SYMBOL_SEPARATOR.equals(str)) {
					if (Pattern.matches(Constant.REGEX_URL_HTML, currentUrl)) {
						cList = cList.subList(0,3);
						nList_clone.remove(0);
					} else {
						nList_clone.remove(0);
					}
					break;
				} else if (Constant.SYMBOL_URL_LEVEL_SAME.equals(str)) {
					if (cList.size()>3) {
						cList.remove(cList.size()-1);
					}
					nList_clone.remove(0);
				} else if (Constant.SYMBOL_URL_LEVEL_UP.equals(str)) {
					nList_clone.remove(0);
					if (cList.size()>3) {
						cList.remove(cList.size()-1);
					}
					if (cList.size()>3) {
						cList.remove(cList.size()-1);
					}
				} else {
					if (cList.size()>3) {
						cList.remove(cList.size()-1);
					}
					break;
				}
			} else {
				if (Constant.SYMBOL_URL_LEVEL_SAME.equals(str)) {
					nList_clone.remove(0);
				} else if (Constant.SYMBOL_URL_LEVEL_UP.equals(str)) {
					nList_clone.remove(0);
					if (cList.size()>3) {
						cList.remove(cList.size()-1);
					}
				}
			}
		}
		StringBuffer sb = new StringBuffer();
		for (String str:cList) {
			sb.append(str);
		}
		
		if (sb.lastIndexOf(Constant.SYMBOL_SEPARATOR)!=(sb.length()-1)) {
			sb.append(Constant.SYMBOL_SEPARATOR);
		}
		for (String str:nList_clone) {
			sb.append(str);
		}
		log.debug("url格式化后:{}",sb.toString());
		return sb.toString();
	}
	
	/**
	 * html 中所有的链接替换为绝对路径
	 * @param textUrl
	 * @param html
	 * @return
	 * @throws Exception
	 */
	public static String dealTextLink(String textUrl,String html) throws Exception {
		Pattern p = Pattern.compile(Constant.REGEX_WEBSITE_LINK);
		Matcher m = p.matcher(html);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String link = m.group();
			String url = "";
			if (link.startsWith("href")) {
				url = link.substring(6,link.length()-1);
			} else if (link.startsWith("src")) {
				url = link.substring(5,link.length()-1);
			}
			String newUrl = HtmlUtil.urlFormater(textUrl, url);
			link = link.replace(url, newUrl);
		    m.appendReplacement(sb, link);
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	/**
	 * 获取页面的body部分
	 * @param url
	 * @param encode
	 * @return
	 */
	public static String getBody(String url,String encode){
		String html = HttpUtil.sendGet(url,encode);
		Document document = Jsoup.parse(html);
		Elements es = document.select("body");
		return es.toString();
	}
	
	/**
	 * 获取当前页面的url，通过regex匹配
	 * @param url
	 * @param regex
	 * @return
	 * @throws Exception
	 */
	public static List<String> selectCurrent(String url,String regex,String encode) throws Exception {
		List<String> list = new ArrayList<String>();
		Pattern pUrl = Pattern.compile(Constant.REGEX_WEBSITE_LINK);
		String html = getBody(url,encode);
		log.debug(html);
		Matcher mUrl = pUrl.matcher(html);
		while (mUrl.find()){
			String link = mUrl.group();
			if (link.startsWith("href")) {
				link = link.substring(6,link.length()-1);
			} else if (link.startsWith("src")) {
				link = link.substring(5,link.length()-1);
			}
			String subUrl = HtmlUtil.urlFormater(url, link);
			if (subUrl.matches(regex)) {
				list.add(subUrl);
			}
		}
		
		return list;
	}
	
	public static List<String> urlSpilt(String url) throws Exception{
		Pattern p = Pattern.compile(Constant.REGEX_SEPARATOR);
		Matcher m = p.matcher(url);
		List<String>list = new ArrayList<String>();
		int startPos = 0;
		int endPos = 0;
		while (m.find()) {
			if (Pattern.matches(Constant.REGEX_URL_ABSOLUTE, url)) {
				endPos = m.start();
			} else {
				endPos = m.end();
			}
			list.add(url.substring(startPos, endPos));
			startPos = endPos;
			
		}
		if (startPos<(url.length()-1)) {
			list.add(url.substring(startPos));
		} else {
			if (Pattern.matches(Constant.REGEX_URL_ABSOLUTE, url)) {
				list.add(url.substring(startPos));
			}
		}
		if (list.isEmpty()) {
			list.add(Constant.SYMBOL_SEPARATOR + url);
		}
		return list;
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
