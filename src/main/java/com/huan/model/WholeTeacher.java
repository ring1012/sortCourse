package com.huan.model;

import com.huan.sort.util.ProcessUtil;

public class WholeTeacher extends Teacher{
	
	public ProcessUtil wholePro;
	
	public WholeTeacher(String teacherName, String courseName, int perWeekClassNum, int perWeekTimeNum, boolean isHead,int teacherIndex) {
		super(teacherName, courseName, perWeekClassNum, perWeekTimeNum, isHead);
		this.teacherIndex=teacherIndex;
		wholePro=new ProcessUtil();
	}
	
	
	
}
