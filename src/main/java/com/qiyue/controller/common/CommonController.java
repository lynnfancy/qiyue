package com.qiyue.controller.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiyue.entity.announcement.AnnouncementEntity;
import com.qiyue.service.announcement.AnnouncementShowService;
import com.qiyue.util.ZipUtil;

@Controller
public class CommonController {
	@RequestMapping("/")
	String hello(HashMap<String, Object> map) {
		map.put("hello", "Welcome qiyue.com");
		return "/index";
	}
	
}
