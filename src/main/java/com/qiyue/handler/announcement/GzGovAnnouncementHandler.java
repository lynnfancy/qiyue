package com.qiyue.handler.announcement;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import com.qiyue.constant.Constant;
import com.qiyue.interf.web.announcement.AnnouncementCollectorHandler;
import com.qiyue.util.DateUtil;

@SpringBootApplication
@Service
public class GzGovAnnouncementHandler extends AnnouncementCollectorHandler{
	
	public GzGovAnnouncementHandler() {
		this.url = "http://www.gz.gov.cn/gzgov/index.shtml";
		this.regex = "<a\\s*href=\"(http(s)?\\:\\/\\/)?[\\.\\/_A-z0-9]*\\.(shtml|html|htm)\"\\s*?[^>]*>[^<]*</a>";
		
		this.websiteCode = "gzgov";
		this.websiteName = "广州市人民政府门户网站";
		
		this.textUrlWhite.put("http\\:\\/\\/www\\.gz\\.gov\\.cn\\/gzgov\\/gsgg\\/[0-9]{6}.*","tzgg");
		this.textUrlWhite.put("http\\:\\/\\/www\\.gz\\.gov\\.cn\\/gzgov\\/s[0-9]{4}\\/[0-9]{6}.*","fggw");
		this.textUrlWhite.put("http\\:\\/\\/www\\.gz\\.gov\\.cn\\/[0-9]{4}cwhy\\/.*","cwhy");
		
		this.categoryMap.put("tzgg", "通知公告");
		this.categoryMap.put("fggw", "法规公文");
		this.categoryMap.put("cwhy", "常务会议");
		
		this.titleUrlWhite.add("http\\:\\/\\/www.gz.gov.cn\\/gzgov\\/jrgz\\/xw_list.shtml");
		this.titleUrlWhite.add("http\\:\\/\\/www.gz.gov.cn\\/gzgov\\/zscd/xw_list_zscd.shtml");
		this.titleUrlWhite.add("http\\:\\/\\/www.gz.gov.cn\\/gzgov\\/s2342/xw_list.shtml");
		this.titleUrlWhite.add("http\\:\\/\\/www.gz.gov.cn\\/gzgov\\/s2344/xw_list.shtml");
		this.titleUrlWhite.add("http\\:\\/\\/www.gz.gov.cn\\/gzgov\\/s2345/xw_list.shtml");
		this.titleUrlWhite.add("http\\:\\/\\/www.gz.gov.cn\\/gzgov\\/gsgg/xw_list.shtml");
		this.titleUrlWhite.add("http\\:\\/\\/www.gz.gov.cn\\/gzgov\\/s[0-9]{4}/gk_fggw_list[0-9].shtml");
		this.titleUrlWhite.add("http\\:\\/\\/www.gz.gov.cn\\/[0-9]{4}cwhy/index.shtml");
		this.titleUrlWhite.add("http\\:\\/\\/www.gz.gov.cn\\/[0-9]{4}cwhy/zdgz/list.shtml");
		
	}

	@Override
	public String getText(String url,String categoryCode) throws Exception {
		String html = getHtml(url);
		Elements es = null;
		if ("cwhy".equals(categoryCode)) {
			es = Jsoup.parse(html).select("#container p");
		} else if ("tzgg".equals(categoryCode)) {
			es = Jsoup.parse(html).select("#zoomcon");
		} else if ("fggw".equals(categoryCode)) {
			es = Jsoup.parse(html).select("div.info_cont p");
		} else {
			es = new Elements();
		}
		return es.toString();
	}

	@Override
	public Date textWebPublishTime(String url,String categoryCode) throws ParseException {
		Date date = null;
		String reg = "";
		String time = "";
		if ("fggw".equals(categoryCode)) {
			reg = "[0-9]{4}-[0-9]{2}-[0-9]{2}\\s[0-9]{2}:[0-9]{2}:[0-9]{2}";
		} else if ("tzgg".equals(categoryCode)) {
			reg = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}";
		} else if ("cwhy".equals(categoryCode)) {
			reg = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}";
		}
		List<String> listTime = selectTextCurrent(url, reg);
		if (listTime.isEmpty()) {
			date = new Date();
		} else {
			time= listTime.get(0);
			if ("fggw".equals(categoryCode)) {
				date = DateUtil.parseDate(time, Constant.DATE_FORMATER_WITH_HYPHEN);
			} else if ("tzgg".equals(categoryCode)||"cwhy".equals(categoryCode)) {
				date = DateUtil.parseDate(time, Constant.DATE_FORMATER_WITH_HYPHEN_NO_TIME);
			}
		}
		return date;
	}

}
