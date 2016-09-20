package com.huan.controller;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SortContoller {

	@Autowired
	HttpServletRequest request;

	@RequestMapping(value = "/input.action", method = RequestMethod.GET)
	public String input() {
		return "input";
	}

	@RequestMapping(value = "/test.action", method = RequestMethod.GET)
	public String test() {
		System.out.println("test");
		return "test";
	}

	@RequestMapping(value="/deal.action", method = RequestMethod.POST)
	public String deal(){
		System.out.println("simulator start!");
		try {
			request.setCharacterEncoding("UTF-8");
			StringBuffer json = new StringBuffer();
			String line = null;
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				json.append(line);
			}
			System.out.println(json.toString());
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return "test";
		}
	
}
