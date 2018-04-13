package com.qiyue.controller.announcement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiyue.entity.announcement.AnnouncementEntity;
import com.qiyue.service.announcement.AnnouncementCollectorService;
import com.qiyue.service.announcement.AnnouncementShowService;
import com.qiyue.util.ZipUtil;

@RequestMapping("/announcement")
@Controller
public class AnnouncementController {
	@Autowired
	private AnnouncementCollectorService acs;
	@Autowired
	private AnnouncementShowService ass;
	
	@RequestMapping("/gzgov")
	@ResponseBody
	public String gzgov() {
		try {
			acs.gzgov();
		} catch (Exception e) {
			return "FAIL";
		}
		return "OK";
	}
	
	@RequestMapping("/szgov")
	@ResponseBody
	public String szgov() {
		try {
			acs.szgov();
		} catch (Exception e) {
			return "FAIL";
		}
		return "OK";
	}
	
	@RequestMapping("/show")
	String show(HashMap<String, Object> map) {
		return "announcement/announcementShow";
	}
	
	@RequestMapping("/navi")
	@ResponseBody
	List<Map<String,String>> navi () throws Exception{
		@SuppressWarnings("unchecked")
		List<Object[]> list = (List<Object[]>)ass.navi();
		List<Map<String,String>> list1 = new ArrayList<Map<String,String>>();
		for (Object[] objArr: list){
			Map<String,String> map1 = new HashMap<String,String>();
			map1.put("websiteCode", (String)objArr[0]);
			map1.put("categoryCode", (String)objArr[1]);
			list1.add(map1);
		}
		return list1;
	}

	@RequestMapping("/titles")
	@ResponseBody
	List<Map<String,String>> findByPage (int page,String websiteCode,String categoryCode) throws Exception{
		List<AnnouncementEntity> list = ass.show(page,websiteCode,categoryCode);
		List<Map<String,String>> list1 = new ArrayList<Map<String,String>>();
		for (AnnouncementEntity hs: list){
			Map<String,String> map1 = new HashMap<String,String>();
			map1.put("url", hs.getUrl());
			map1.put("title", hs.getTitle());
			String text = hs.getText();
			text = ZipUtil.unGzip(text);
			map1.put("text", text);
			list1.add(map1);
		}
		return list1;
	}
	
	@RequestMapping("/pageTotal")
	@ResponseBody
	int pageTotal (String websiteCode,String categoryCode) throws Exception{
		return ass.pageTotal(websiteCode,categoryCode);
	}
}
