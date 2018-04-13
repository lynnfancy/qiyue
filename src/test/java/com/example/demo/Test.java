package com.example.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.tomcat.util.codec.binary.Base64;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.qiyue.constant.Constant;
import com.qiyue.entity.announcement.AnnouncementEntity;
import com.qiyue.handler.SqlHandler;
import com.qiyue.interf.web.announcement.AnnouncementCollectorHandler;
import com.qiyue.util.ZipUtil;

public class Test {
	
	public static void main(String[] args) throws Exception {
//		String url = "http://www.gz.gov.cn/gzgov/index.shtml";
//		System.out.println("Announcement_Collector".toUpperCase());
//		System.out.println(new AnnouncementCollectorHandler().getHtml(url));
		SqlHandler sh = new SqlHandler();
		String sql = "select DISTINCT(ACR_WEBSITE_CODE),ACR_CATEGORY_CODE FROM announcement_collector_result";
	}

}
