package com.qiyue.controller.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiyue.dao.user.entity.NavigationEntity;
import com.qiyue.dao.user.entity.UserEntity;
import com.qiyue.node.Node;
import com.qiyue.service.user.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService us;
	
	@RequestMapping("/")
	String welcome(HashMap<String, Object> map) {
		map.put("hello", "Welcome aiqiyue.com");
		return "login";
	}
	
	@RequestMapping("/index")
	String index() {
		return "index";
	}
	
	@RequestMapping("/login")
	String login(String username,String password,Model model) {
		UserEntity ue = us.checkLogin(username, password);
		if (ue==null) {
			model.addAttribute("errorCode", 0000);
			model.addAttribute("errorMsg", "用户名或密码不正确");
			return "login";
		} else {
			return "index";
		}
	}
	
	@RequestMapping("/navi")
	@ResponseBody
	Node navi(){
		return us.navi();
	}
	
}
