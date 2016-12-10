package com.huan.teacher.service.imp;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.huan.definition.ResultType;
import com.huan.exception.Myexception;
import com.huan.model.BaseTeacher;
import com.huan.model.HalfTeacher;
import com.huan.model.Teacher;
import com.huan.model.WholeTeacher;
import com.huan.sort.util.startSortCourse;

@Component
public class DealService {

	@Autowired
	HttpServletRequest request;

	public void excute(List<Teacher> form) throws Myexception {

		int classNum = Integer.parseInt(request.getParameter("classNum"));
		int morning = Integer.parseInt(request.getParameter("morning"));
		int afternoon = Integer.parseInt(request.getParameter("afternoon"));
		int saturday = Integer.parseInt(request.getParameter("saturday"));
		int sunday = Integer.parseInt(request.getParameter("sunday"));
		String allowMorning_s = request.getParameter("allowMorning");
		boolean allowMorning = false;
		if (allowMorning_s != null) {
			allowMorning = true;
		}
		int i = 0;
		List<BaseTeacher>tsList=new ArrayList<>();
		for (Teacher each : form) {
			BaseTeacher temp = null;
			if (!each.IsNext) {
				temp = new WholeTeacher(each.teacherName, each.courseName, each.perWeekClassNum, each.perWeekTimeNum,
						each.IsHead, i + 1);
			} else {
				temp = new HalfTeacher(each.teacherName, each.courseName, each.perWeekClassNum, each.perWeekTimeNum,
						each.IsHead, i + 1);
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
