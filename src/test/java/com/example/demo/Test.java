package com.example.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.tomcat.util.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.qiyue.constant.Constant;
import com.qiyue.dao.annouce.entity.AnnouceCollectorResult;
import com.qiyue.dao.annouce.manage.AnnouceEntityManager;
import com.qiyue.handler.AnnouceCollectorHandler;
import com.qiyue.util.BaseUtil;
import com.qiyue.util.HtmlUtil;
import com.qiyue.util.ZipUtil;
import com.qiyue.util.web.http.HttpUtil;

public class Test {
	
	public static void main(String[] args) throws Exception {
//		String url = "http://www.gz.gov.cn/gzgov/index.shtml";
//		System.out.println("Announcement_Collector".toUpperCase());
//		System.out.println(new AnnouncementCollectorHandler().getHtml(url));
		
		
		
		DefaultMutableTreeNode root=new DefaultMutableTreeNode("0000");
		DefaultMutableTreeNode root1=new DefaultMutableTreeNode("0001");
		DefaultMutableTreeNode root2=new DefaultMutableTreeNode("0002");
		root.add(root1);
		root.add(root2);
		System.out.println(root);
		
		/*
		StringBuffer sb = new StringBuffer();
		AnnouceEntityManager sh = new AnnouceEntityManager();
		String sql = "select DISTINCT(ACR_WEBSITE_CODE),ACR_CATEGORY_CODE FROM announcement_collector_result";
		String reg = "[0-9]{6}";//\\/
//		String cUrl = "http://www.gd.gov.cn/ywdt/cwhy/";
		String cUrl = "http://www.gd.gov.cn/gdgk/lyxx/201804/t20180420_269175.htm";//{http://www.gd.gov.cn/gzhd/zxdc/http://www.gdmz.gov.cn/gdsmzt/webpage/dcwj/dcwj.jsp?id=4=2
		String nUrl = "./W020180420398626668436.jpg";
//		String nUrl = "/gzgov/s2812/201804/ab44ae1c72184074830740cd1daf1393.shtml";
//		String html = textLinkhandler(nUrl);
//		System.out.println(Jsoup.parse(html));
		System.out.println(HtmlUtil.urlFormater(cUrl, nUrl));
		*/
		
	}

	public static String textLinkhandler(String textUrl) throws Exception {
//		String html = HttpUtil.sendGet(textUrl);
		String html = "";
		System.out.println(html);
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
			link.replace(url, newUrl);
		    m.appendReplacement(sb, link);
		}
		m.appendTail(sb);
		return sb.toString();
	}
}
