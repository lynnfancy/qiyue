package com.qiyue.handler;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import com.qiyue.constant.Constant;
import com.qiyue.dao.annouce.entity.AnnouceCollectorInfo;
import com.qiyue.dao.annouce.entity.AnnouceCollectorResult;
import com.qiyue.dao.annouce.entity.AnnouceWebsiteInfo;
import com.qiyue.dao.annouce.manage.AnnouceEntityManager;
import com.qiyue.dao.annouce.repository.AnnouceCollectorResultRepository;
import com.qiyue.redis.RedisHandler;
import com.qiyue.util.BaseUtil;
import com.qiyue.util.DateUtil;
import com.qiyue.util.HtmlUtil;
import com.qiyue.util.ZipUtil;
import com.qiyue.util.web.http.HttpUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@SpringBootApplication
@Configuration
public class AnnouceCollectorHandler {
	@Autowired
	public AnnouceEntityManager aem;
	@Autowired 
	private RedisHandler rh;
	@Autowired
	private AnnouceCollectorResultRepository acrr;
	
//	public Map<String,Integer> allTitleUrl = new HashMap<String,Integer>();//所有搜索到title的url
//	public List<String> allTitleUrlDone = new ArrayList<String>();//所有搜索过title的url
//	public Map<String,Object> levelUrlMap = new HashMap<String,Object>();//url树
//	public List<String> allTextUrl = new ArrayList<String>();//所有搜索过text的url
//	@Value("${spider.select.level}")
//	public int selectLevel;
	
	public String encode = Constant.ENCODE_UTF8;
	
	public void collect(String websiteCode) throws Exception {
		@SuppressWarnings("unchecked")
		Map<String,AnnouceWebsiteInfo> awiMap = (Map<String,AnnouceWebsiteInfo>)rh.get("awiList");
		AnnouceWebsiteInfo awi = awiMap.get(websiteCode);
		collect(awi);
	}
	
	public void collect() throws Exception {
		@SuppressWarnings("unchecked")
		List<AnnouceWebsiteInfo> awiList = (List<AnnouceWebsiteInfo>)rh.get("awiList");
		awiList.forEach(awi->{
			new Runnable() {
				@Override
				public void run() {
					try {
						List<AnnouceCollectorResult> acrLit = collect(awi);
						aem.batchMerge(acrLit);
					} catch (Exception e) {
						log.error("搜索归集网站[{}]异常",awi.getWebsiteName());
						e.printStackTrace();
					}
				}
			}.run();
		});
	}
	
	public List<AnnouceCollectorResult> collect(AnnouceWebsiteInfo awi) throws Exception {
		if (!BaseUtil.isEmpty(awi.getEncode())) {
			encode = awi.getEncode();
		}
		String currentTime = DateUtil.getSystemTime();
		Map<String,String> timeMap = latestSqlPublishTime(awi.getFlowNo());
		List<AnnouceCollectorResult> acrList = new ArrayList<AnnouceCollectorResult>();
		
		awi.getAciList()
			.parallelStream()
			.forEach(aci->{
				try {
					List<String> list = HtmlUtil.selectCurrent(aci.getTitleUrlWhite(),aci.getTextUrlRegexWhite(),encode);
					List<AnnouceCollectorResult> acrList1 = list.parallelStream()
						.filter(url->{
							return !url.matches(aci.getTextUrlRegexBlack());
						})
						.map(url->{
							AnnouceCollectorResult acr = new AnnouceCollectorResult();
							acr.setUrl(url);
							acr.setAwiFlowNo(awi.getFlowNo());
							acr.setAciFlowNo(aci.getFlowNo());
							acr.setSupUrl(aci.getTitleUrlWhite());
							acr.setWebsiteCode(awi.getWebsiteCode());
							acr.setWebsiteName(awi.getWebsiteName());
							acr.setCategoryCode(aci.getCategoryCode());
							acr.setCategoryName(aci.getCategoryName());
							return acr;
						})
						.filter(acr->{
							String url = acr.getUrl();
							String title = "";
							String text = "";
							String publishTime = "";
							try {
								String html = HtmlUtil.getBody(url,encode);
								Document doc = Jsoup.parse(html);
								//获取标题
								Elements tes = doc.select(aci.getTitleSelector());
								title = tes.html();
								if (BaseUtil.isEmpty(title)) {
									log.error("{}未正确获取标题，请检查标题选择器[{}]是否正确",url,aci.getTitleSelector());
									return false ;
								}
								//获取正文
								text = doc.select(aci.getTextSelector()).toString();
								if (BaseUtil.isEmpty(text)) {
									log.error("{}未正确获取正文，请检查正文选择器[{}]是否正确",url,aci.getTextSelector());
									return false ;
								}
								//处理正文中的链接
								text = HtmlUtil.dealTextLink(url, text);
								//压缩
								text = ZipUtil.gzip(text);
								//获取发布时期
								publishTime = doc.select(aci.getPublishTimeSelector()).toString();
								if (BaseUtil.isEmpty(publishTime)) {
									log.error("{}未正确获取发布时间，请检查时间正则表达式[{}]是否正确",url,aci.getPublishTimeSelector());
									return false ;
								}
								publishTime = publishTime.replaceAll("[^0-9]", "");
								publishTime = StringUtils.rightPad(publishTime, 14, "0");
								String latestPublishTime = "";
								if (timeMap.containsKey(aci.getFlowNo())) {
									latestPublishTime = timeMap.get(aci.getFlowNo());
								} else {
									latestPublishTime = DateUtil.dateHandle(currentTime,-30);
								}
								if (publishTime.compareTo(latestPublishTime)<0) {
									log.error("发布时间{}，数据库已收录:{},url:{}",publishTime,latestPublishTime,url);
									return false ;
								}
							} catch (Exception e) {
								log.error("处理网页内容异常");
								e.printStackTrace();
								return false ;
							}
							acr.setRecordTime(DateUtil.getSystemTime());
							acr.setTitle(title);
							acr.setText(text);
							acr.setPublishTime(publishTime);
							return true;
						}).collect(Collectors.toList());
					acrList.addAll(acrList1);
				} catch (Exception e) {
					log.error("获取标题页中正文url异常，titleUrl：{}",aci.getTitleUrlWhite());
					e.printStackTrace();
				}
		});
		return acrList;
	}
	
	
	public Map<String,String> latestSqlPublishTime(String awiFlowNo) throws Exception {
		List<Object[]>outputList = acrr.findLatestPublishTime(awiFlowNo);
		Map<String,String> map = new HashMap<String,String>();
		for (Object[] objArr:outputList) {
			map.put((String)objArr[0], (String)objArr[1]);
		}
		return map;
	}

	
	/*	
	public void clear() {
		this.allTitleUrl = new HashMap<String,Integer>();
		this.levelUrlMap = new HashMap<String,Object>();
		this.allTextUrl = new ArrayList<String>();
		this.allTitleUrlDone = new ArrayList<String>();
	}
	public Map<String, Integer> getNextUrlMap(Map<String, Integer> map,int level) throws Exception {
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
	
	public Map<String, Integer> getNextUrlMap(String url,int level) throws Exception {
		Map<String, Integer> next = new HashMap<String, Integer>();
		if (!this.allTitleUrl.containsKey(url)){
			this.allTitleUrl.put(url, level);
			//搜索页面所有符合条件的url
			Pattern pUrl = Pattern.compile(Constant.REGEX_WEBSITE_LINK);
			String html = getHtml(url);
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
				if (!subUrl.matches(Constant.REGEX_URL_HTML)||this.allTitleUrl.containsKey(subUrl)) {
					continue;
				}
				next.put(subUrl,level+1);
				this.allTitleUrl.put(subUrl, level);
			}
			HtmlUtil.putMultilevelMap(this.levelUrlMap, url, next);
		}
		return next;
	}
	
	public Map<String,List<String>> selectTitleMulti(String url,String regex) throws Exception{
		Map<String, Integer> urlMap = new HashMap<String, Integer>();
		urlMap.put(url, 0);
		return selectTitleMulti(urlMap,regex);
	}
	
	public Map<String,List<String>> selectTitleMulti(Map<String, Integer> urlMap,String regex){
		Map<String,List<String>> multi = new HashMap<String,List<String>>();
		urlMap.forEach((url,level)->{
			try {
				multi.putAll(selectTitleCurrent(url,regex));
				if (this.allTitleUrlDone.contains(url)) {
					return;
				}
				this.allTitleUrlDone.add(url);
				if (level<this.selectLevel) {
					Map<String, Integer> subUrlMap =  getNextUrlMap(url,level);
					Map<String,List<String>> subTitleMap = selectTitleMulti(subUrlMap,regex);
					multi.putAll(subTitleMap);
				}
			} catch (Exception e) {
				log.error("获取{}下url失败",url);
				e.printStackTrace();
			}
		});
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
*/
}
