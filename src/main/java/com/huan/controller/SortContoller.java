package com.huan.controller;

import java.io.BufferedReader;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.huan.definition.Mytime;
import com.huan.definition.ResultType;
import com.huan.model.Teacher;
import com.huan.sort.util.startSortCourse;
import com.huan.teacher.service.ITeacherService;
import com.huan.teacher.service.imp.DealService;

@Controller
public class SortContoller {

	@Autowired
	private ITeacherService teacherService;

	@Autowired
	HttpServletRequest request;

	@Autowired
	DealService dealService;

	@RequestMapping(value = "/input.action", method = RequestMethod.GET)
	public String input() {
		return "input";
	}

	@RequestMapping(value = "/test.action", method = RequestMethod.POST)
	public String test() {
		try {
  			request.setCharacterEncoding("UTF-8");
 			StringBuffer json = new StringBuffer();
 			String line = null;
 			BufferedReader reader = request.getReader();
 			while ((line = reader.readLine()) != null) {
  				json.append(line);
  			}
 			String reslut=URLDecoder.decode(json.toString(), "utf-8");
  			System.out.println(reslut);
 		}catch (Exception e) {
  			System.out.println(e.getMessage());
  		}
		
		String fixTable=request.getParameter("fixTable");
		String changeStr=request.getParameter("changeStr");
		System.out.println("fix: "+fixTable);
		System.out.println("change: "+changeStr);
		
  		return "test";
	}

	@RequestMapping(value = "/deal.action", method = RequestMethod.POST)
	public String deal() {
		Mytime.start = System.currentTimeMillis();
		System.out.println("simulator start!");
		try {
			request.setCharacterEncoding("UTF-8");
			dealService.excute(request);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "result";
	}

	@RequestMapping(value = "/testdeal.action", method = RequestMethod.POST)
	public void testdeal() {
		System.out.println("test start!");
		try {

			String firstRet[] = request.getParameterValues("first");
			for (String s : firstRet) {
				System.out.print(s + " ");
			}
			System.out.println();
			String secondRet[] = request.getParameterValues("second");
			System.out.println("num:= " + secondRet.length);
			for (String s : secondRet) {
				System.out.print(s + " ");
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	@RequestMapping(value = "/simulator.action", method = RequestMethod.GET)
	public String simulator() {
		List<Teacher> ts = this.teacherService.findAll();
		request.setAttribute("teachers", ts);
		return "simulator";
	}

	@RequestMapping(value = "/change.action", method = RequestMethod.POST)
	public String change() {
		try {

			Object obj = request.getSession().getAttribute("myCourse");
			startSortCourse myCourse = (startSortCourse) obj;
			ResultType ret = myCourse.changeAndDeal();
			request.setAttribute("result", ret);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return "result";
	}

}
