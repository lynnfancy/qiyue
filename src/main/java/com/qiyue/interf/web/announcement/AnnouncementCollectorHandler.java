package com.qiyue.interf.web.announcement;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import com.qiyue.constant.Constant;
import com.qiyue.entity.announcement.AnnouncementEntity;
import com.qiyue.handler.SqlHandler;
import com.qiyue.util.DateUtil;
import com.qiyue.util.HtmlUtil;
import com.qiyue.util.ZipUtil;
import com.qiyue.util.web.http.HttpUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@Service
public abstract class AnnouncementCollectorHandler {
	@Autowired
	public SqlHandler sh;
	public String websiteCode ;
	public String websiteName ;
	
	public String url ;
	public String regex ;
	
	public Map<String,String> textUrlWhite = new HashMap<String,String>();//正文url白名单
	public Map<String,String> categoryMap = new HashMap<String,String>();//类别代码映射
	public List<String> titleUrlWhite = new ArrayList<String>();//标题链接url白名单
	
	public Map<String,Integer> allUrlMap = new HashMap<String,Integer>();//所有搜索到title的url
	public Map<String,Object> levelUrlMap = new HashMap<String,Object>();//url树
	public List<String> allTextUrl = new ArrayList<String>();//所有搜索过text的url
	@Value("${spider.select.level}")
	public int selectLevel;
	
	
	public abstract String getText(String url,String categoryCode) throws Exception ;
	public abstract Date textWebPublishTime(String url,String categoryCode) throws ParseException ;
	
	public void collect() throws Exception {
		collect(this.url, this.regex);
	}
	
	public void collect(String url, String regex) throws Exception {
		clear();
		Date date1 = latestSqlPublishTime();
		Map<String, List<String>> map = selectTitleMulti(url, regex);
		log.debug(map.toString());
		Iterator<Entry<String,List<String>>> iterator = map.entrySet().iterator();
		List<AnnouncementEntity> list = new ArrayList<AnnouncementEntity>();
		while (iterator.hasNext()) {
			Entry<String,List<String>> entry = iterator.next();
			for (String str : entry.getValue()) {
				Map<String,String> titleMap = getTitleUrl(str);
				String titleUrl = HtmlUtil.formatUrl(entry.getKey(), titleMap.get("titleUrl"));
				String categoryCode = isText(titleUrl);
				if (StringUtil.isBlank(categoryCode)) {
					continue;
				}
				if (isTitle(titleUrl)) {
					continue;
				}
				if (allTextUrl.contains(titleUrl)) {
					continue;
				}
				allTextUrl.add(titleUrl);
				Date date2 = textWebPublishTime(titleUrl,categoryCode);
				if (date2.compareTo(date1)>=0) {
					AnnouncementEntity an = new AnnouncementEntity();
					String text = getText(titleUrl,categoryCode);
					text = ZipUtil.gzip(text);
					an.setSupUrl(entry.getKey());
					an.setCategoryCode(categoryCode);
					an.setCategoryName(this.categoryMap.get(categoryCode));
					an.setWebsiteCode(websiteCode);
					an.setWebsiteName(websiteName);
					an.setUrl(titleUrl);
					an.setTitle(titleMap.get("title"));
					an.setText(text);
					an.setPublishTime(DateUtil.formatDate(date2, Constant.DATE_FORMATER_NO_INTERVAL));
					an.setRecordTime(DateUtil.getSystemTime());
					list.add(an);
				}
			}
		}
		sh.batchMerge(list);
	}
	
	public Date latestSqlPublishTime() throws ParseException {
		String sql = "select ACR_PUBLISH_TIME from announcement_collector_result where ACR_WEBSITE_CODE = ?1 ORDER BY ACR_PUBLISH_TIME DESC LIMIT 1";
		List<String> inputList = new ArrayList<String>();
		inputList.add(websiteCode);
		List<?>outputList = sh.findByCustom(sql,inputList);
		Date date = null;
		if (outputList.isEmpty()) {
			date = new Date();
			date.setTime(0);
		} else {
			date = DateUtil.parseDate((String)outputList.get(0), Constant.DATE_FORMATER_NO_INTERVAL);
		}
		return date;
	}
	
	public Map<String,String> getTitleUrl(String titleStr) {
		Map<String,String> map = new HashMap<String,String>();
		Document doc = Jsoup.parse(titleStr);
		Element e = doc.selectFirst("a");
		String href = e.attr("href");
		map.put("titleUrl", href);
		map.put("title", e.html());
		return map;
	}
	
	public String getHtml(String url){
		String html = HttpUtil.sendGet(url);
		Document document = Jsoup.parse(html);
		Elements es = document.select("body");
		return es.toString();
	}
	
	public void clear() {
		this.allUrlMap.clear();
		this.levelUrlMap.clear();
	}
	
	public String isText(String url) {
		Set<String> set = this.textUrlWhite.keySet(); 
		for (String str : set) {
			if (url.matches(str)) {
				return this.textUrlWhite.get(str);
			}
		}
		return "";
	}
	
	public boolean isTitle(String url) {
		boolean flag = false;
		for (String str : this.titleUrlWhite) {
			if (url.matches(str)) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	public Map<String, Integer> getNextUrlMap(Map<String, Integer> map,int level) {
		Map<String, Integer> next = new HashMap<String, Integer>();
		Iterator<Entry<String,Integer>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()){
			Entry<String,Integer> entry = iterator.next();
			String url = entry.getKey();
			level = entry.getValue();
			next.putAll(getNextUrlMap(url,level));
		}
		return next;
	}
	
	public Map<String, Integer> getNextUrlMap(String url,int level) {
		Map<String, Integer> next = new HashMap<String, Integer>();
		if (!this.allUrlMap.containsKey(url)){
			this.allUrlMap.put(url, level);
			//搜索页面所有符合条件的url
			Pattern pUrl = Pattern.compile(Constant.REGEX_RELATIVE_URL);
			Matcher mUrl = pUrl.matcher(getHtml(url));
			while (mUrl.find()){
				String subUrl = HtmlUtil.formatUrl(url, mUrl.group());
				if (this.allUrlMap.containsKey(subUrl)) {
					continue;
				}
				if (isTitle(subUrl)) {
					next.put(subUrl,level+1);
				}
			}
			HtmlUtil.putMultilevelMap(this.levelUrlMap, url, next);
		}
		return next;
	}
	
	public Map<String,List<String>> selectTitleMulti(String url,String regex){
		Map<String, Integer> urlMap = new HashMap<String, Integer>();
		urlMap.put(url, 0);
		return selectTitleMulti(urlMap,regex);
	}
	
	public Map<String,List<String>> selectTitleMulti(Map<String, Integer> urlMap,String regex){
		Map<String,List<String>> multi = new HashMap<String,List<String>>();
		Iterator<Entry<String,Integer>> iterator = urlMap.entrySet().iterator();
		while (iterator.hasNext()){
			Entry<String,Integer> entry = iterator.next();
			String url = entry.getKey();
			int level = entry.getValue();
			multi.putAll(selectTitleCurrent(url,regex));
			if (level>this.selectLevel) {
				break;
			} else {
				Map<String, Integer> subUrlMap = getNextUrlMap(url,level);
				Map<String,List<String>> subTitleMap = selectTitleMulti(subUrlMap,regex);
				multi.putAll(subTitleMap);
			}
		}
		return multi;
	}
	
	public Map<String,List<String>> selectTitleCurrent(String url,String regex) {
		Map<String,List<String>> current = new HashMap<String,List<String>>();
		List<String> list = new ArrayList<String>();
		//搜索页面所有匹配项
		String html = getHtml(url);
		log.debug(html);
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(html);
		while (m.find()){
			String result = m.group();
			list.add(result);
		}
		current.put(url, list);
		return current;
	}
	
	public List<String> selectTextCurrent(String url,String regex) {
		List<String> list = new ArrayList<String>();
		//搜索页面所有匹配项
		String html = getHtml(url);
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(html);
		while (m.find()){
			String resultUrl = m.group();
			resultUrl = HtmlUtil.formatUrl(url, resultUrl);
			if (isTitle(resultUrl)) {
				list.add(resultUrl);
			}
		}
		return list;
	}
	
	public Map<String, Integer> getAllUrlMap() {
		return allUrlMap;
	}

	public void setAllUrlMap(Map<String, Integer> allUrlMap) {
		this.allUrlMap = allUrlMap;
	}

	public Map<String, Object> getLevelUrlMap() {
		return levelUrlMap;
	}

	public void setLevelUrlMap(Map<String, Object> levelUrlMap) {
		this.levelUrlMap = levelUrlMap;
	}

}
