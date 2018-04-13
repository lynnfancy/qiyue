package com.qiyue.handler.announcement;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import com.qiyue.constant.Constant;
import com.qiyue.entity.announcement.AnnouncementEntity;
import com.qiyue.handler.SqlHandler;
import com.qiyue.interf.web.announcement.AnnouncementCollectorHandler;
import com.qiyue.util.DateUtil;
import com.qiyue.util.HtmlUtil;
import com.qiyue.util.ZipUtil;
import com.qiyue.util.web.http.HttpUtil;

@SpringBootApplication
@Service
public class SzGovAnnouncementHandler extends AnnouncementCollectorHandler{
	
	public SzGovAnnouncementHandler() {
//		this.url = "http://www.sz.gov.cn/cn/";
		this.url = "http://www.sz.gov.cn/cn/xxgk/index_sl_23448.htm";
		this.regex = "<a[^>]*href=\"(http(s)?\\:\\/\\/)?[\\.\\/_A-z0-9]*\"\\s*?[^>]*>[^<]*</a>";
		
		this.websiteCode = "szgov";
		this.websiteName = "深圳政府在线";
		
		this.textUrlWhite.put("http:\\/\\/www.sz.gov.cn\\/cn\\/xxgk\\/bmtx\\/.*","bmtx");
		this.textUrlWhite.put("http:\\/\\/www.sz.gov.cn\\/cn\\/xxgk\\/zfxxgj\\/gqdt\\/.*","gqdt");
		this.textUrlWhite.put("http:\\/\\/www.sz.gov.cn\\/cn\\/xxgk\\/zfxxgj\\/bmdt\\/.*","bmdt");
		this.textUrlWhite.put("http:\\/\\/www.sz.gov.cn\\/cn\\/xxgk\\/zfxxgj\\/zwdt\\/.*","zwdt");
		this.textUrlWhite.put("http:\\/\\/www.sz.gov.cn\\/cn\\/xxgk\\/zfxxgj\\/tzgg\\/.*","tzgg");
		
		this.categoryMap.put("bmtx", "便民提醒");
		this.categoryMap.put("gqdt", "各区动态");
		this.categoryMap.put("bmdt", "部门动态");
		this.categoryMap.put("zwdt", "政务动态");
		this.categoryMap.put("tzgg", "通知公众");
		
		this.titleUrlWhite.add("http:\\/\\/www.sz.gov.cn\\/cn\\/xxgk\\/bmtx\\/");
		this.titleUrlWhite.add("http:\\/\\/www.sz.gov.cn\\/cn\\/xxgk\\/zfxxgj\\/gqdt\\/");
		this.titleUrlWhite.add("http:\\/\\/www.sz.gov.cn\\/cn\\/xxgk\\/zfxxgj\\/bmdt\\/");
		this.titleUrlWhite.add("http:\\/\\/www.sz.gov.cn\\/cn\\/xxgk\\/zfxxgj\\/zwdt\\/");
		this.titleUrlWhite.add("http:\\/\\/www.sz.gov.cn\\/cn\\/xxgk\\/zfxxgj\\/tzgg\\/");
	}
	
	@Override
	public String getHtml(String url) {
		String html = HttpUtil.sendGet(url,Constant.ENCODE_GBK);
		Document document = Jsoup.parse(html);
		Elements es = document.select("body");
		return es.toString();
	}

	@Override
	public String getText(String url,String categoryCode) throws Exception {
		String html = getHtml(url);
		Elements es = Jsoup.parse(html).select("div.TRS_Editor");
		return es.toString();
	}

	@Override
	public Date textWebPublishTime(String url,String categoryCode) throws ParseException {
		Date date = null;
		String reg = "";
		String time = "";
		reg = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}";
		List<String> listTime = selectTextCurrent(url, reg);
		if (listTime.isEmpty()) {
			date = new Date();
		} else {
			time= listTime.get(0);
			date = DateUtil.parseDate(time, Constant.DATE_FORMATER_WITH_HYPHEN_NO_TIME);
		}
		return date;
	}
	
}
