package com.huan.teacher.service.imp;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.huan.course.util.ConvertUtil;
import com.huan.definition.ResultType;
import com.huan.exception.Myexception;
import com.huan.model.allData;
import com.huan.sort.util.startSortCourse;
@Component
public class DealService {

	public void excute(HttpServletRequest request) throws Myexception{
		
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
		String IsNext_s[]=request.getParameterValues("IsNext");
		int perWeekClassNum[]=ConvertUtil.parsIntArray(perWeekClassNum_s);
		int perWeekTimeNum[]=ConvertUtil.parsIntArray(perWeekTimeNum_s);
		int isHead[]=ConvertUtil.parsIntArray(IsHead_s);
		int isNext[]=ConvertUtil.parsIntArray(IsNext_s);
		boolean head[]=new boolean[teacherName.length];
		boolean next[]=new boolean[teacherName.length];
		for(int k:isHead){
			head[k-1]=true;
		}
		for(int k:isNext){
			next[k-1]=true;
		}
		
		List<allData>tsList=new ArrayList<allData>();
		for(int i=0;i<teacherName.length;i++){
			allData temp=new allData(teacherName[i], courseName[i], perWeekClassNum[i], perWeekTimeNum[i],head[i],next[i],i+1);
			tsList.add(temp);
		}
		
		startSortCourse myCourse=new startSortCourse(classNum, morning, afternoon,saturday,sunday,tsList);
		myCourse.paramCheck();
		myCourse.allocateClasses();
		ResultType ret=myCourse.allocateLessones();
		request.getSession().setAttribute("myCourse", myCourse);
		request.setAttribute("result", ret);
		
	}
	
	
}
