package com.qiyue.controller.clan;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/clan")
@Controller
public class ClanController {
	
	@RequestMapping("/index")
	String index() {
		return "clan/index";
	}
}
