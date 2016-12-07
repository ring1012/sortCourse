package com.huan.teacher.service.imp;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.huan.course.util.ConvertUtil;
import com.huan.definition.ResultType;
import com.huan.exception.Myexception;
import com.huan.model.HalfTeacher;
import com.huan.model.Teacher;
import com.huan.model.WholeTeacher;
import com.huan.sort.util.startSortCourse;

@Component
public class DealService {

	public void excute(HttpServletRequest request) throws Myexception {

		int classNum = Integer.parseInt(request.getParameter("classNum"));
		int morning = Integer.parseInt(request.getParameter("morning"));
		int afternoon = Integer.parseInt(request.getParameter("afternoon"));
		int saturday = Integer.parseInt(request.getParameter("saturday"));
		int sunday = Integer.parseInt(request.getParameter("sunday"));
		String teacherName[] = request.getParameterValues("teacherName");
		String courseName[] = request.getParameterValues("courseName");
		String perWeekClassNum_s[] = request.getParameterValues("perWeekClassNum");
		String perWeekTimeNum_s[] = request.getParameterValues("perWeekTimeNum");
		String IsHead_s[] = request.getParameterValues("IsHead");
		String IsNext_s[] = request.getParameterValues("IsNext");
		String allowMorning_s = request.getParameter("allowMorning");
		boolean allowMorning = false;
		if (allowMorning_s != null) {
			allowMorning = true;
		}
		int perWeekClassNum[] = ConvertUtil.parsIntArray(perWeekClassNum_s);
		int perWeekTimeNum[] = ConvertUtil.parsIntArray(perWeekTimeNum_s);
		int isHead[] = ConvertUtil.parsIntArray(IsHead_s);
		int isNext[] = ConvertUtil.parsIntArray(IsNext_s);
		boolean head[] = new boolean[teacherName.length];
		boolean next[] = new boolean[teacherName.length];
		for (int k : isHead) {
			head[k - 1] = true;
		}
		for (int k : isNext) {
			next[k - 1] = true;
		}

		List<Teacher> tsList = new ArrayList<Teacher>();
		for (int i = 0; i < teacherName.length; i++) {
			Teacher temp = null;
			if (!next[i]) {
				temp = new WholeTeacher(teacherName[i], courseName[i], perWeekClassNum[i], perWeekTimeNum[i], head[i],
						i + 1);
			} else {
				temp = new HalfTeacher(teacherName[i], courseName[i], perWeekClassNum[i], perWeekTimeNum[i], head[i],
						i + 1);
			}

			tsList.add(temp);
		}

		startSortCourse myCourse = new startSortCourse(classNum, morning, afternoon, saturday, sunday, tsList,
				allowMorning);
		myCourse.paramCheck();
		myCourse.allocateClasses();
		ResultType ret = myCourse.allocateLessones();
		request.getSession().setAttribute("myCourse", myCourse);
		request.setAttribute("result", ret);

	}

}
