package com.huan.model;


import com.huan.sort.util.ProcessUtil;

public class HalfTeacher extends Teacher {

	public ProcessUtil oddUtil;
	public ProcessUtil evenUtil;

	public HalfTeacher(String teacherName, String courseName, int perWeekClassNum, int perWeekTimeNum, boolean isHead,int teacherIndex) {
		super(teacherName, courseName, perWeekClassNum, perWeekTimeNum, isHead);
		this.teacherIndex=teacherIndex;
		oddUtil=new ProcessUtil();
		evenUtil=new ProcessUtil();
	}
	

}
