package com.huan.controller;

import java.io.BufferedReader;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.huan.course.util.ConvertUtil;
import com.huan.definition.Mytime;
import com.huan.model.Teacher;
import com.huan.teacher.service.ITeacherService;

@Controller
public class SortContoller {

	@Autowired
	private ITeacherService teacherService;
	
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
	public String deal() {
		Mytime.start=System.currentTimeMillis();
		System.out.println("simulator start!");
		try {
			request.setCharacterEncoding("UTF-8");
			
			int classNum=Integer.parseInt(request.getParameter("classNum"));
			int morning=Integer.parseInt(request.getParameter("morning"));
			int afternoon=Integer.parseInt(request.getParameter("afternoon"));
			int saturday=Integer.parseInt(request.getParameter("saturday"));
			int sunday=Integer.parseInt(request.getParameter("sunday"));
			String teacherName[]=request.getParameterValues("teacherName");
			String courseName[]=request.getParameterValues("courseName");
			String perWeekClassNum_s[]=request.getParameterValues("perWeekClassNum");
			String perWeekTimeNum_s[]=request.getParameterValues("perWeekTimeNum");
			String IsHead_s[]=request.getParameterValues("IsHead");
			int perWeekClassNum[]=ConvertUtil.parsIntArray(perWeekClassNum_s);
			int perWeekTimeNum[]=ConvertUtil.parsIntArray(perWeekTimeNum_s);
			for(String s:IsHead_s){
				System.out.println(s);
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return "test";
	}
	
	@RequestMapping(value="/testdeal.action",method=RequestMethod.POST)
	public void testdeal(){
		System.out.println("test start!");
		try {
			
			String firstRet[]=request.getParameterValues("first");			
			for(String s:firstRet){
				System.out.print(s+" ");
			}
			System.out.println();
			String secondRet[]=request.getParameterValues("second");
			System.out.println("num:= "+secondRet.length);
			for(String s:secondRet){
				System.out.print(s+" ");
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/simulator.action", method = RequestMethod.GET)
	public String simulator() {
		List<Teacher>ts=this.teacherService.findAll();
		request.setAttribute("teachers", ts);
		return "simulator";
	}
	
	
}
