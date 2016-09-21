package com.huan.model;

public class Teacher {
	private String teacherName;
	private String courseName;
	private int perWeekClassNum;
	private int perWeekTimeNum;
	private boolean IsHead;
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public int getPerWeekClassNum() {
		return perWeekClassNum;
	}
	public void setPerWeekClassNum(int perWeekClassNum) {
		this.perWeekClassNum = perWeekClassNum;
	}
	public int getPerWeekTimeNum() {
		return perWeekTimeNum;
	}
	public void setPerWeekTimeNum(int perWeekTimeNum) {
		this.perWeekTimeNum = perWeekTimeNum;
	}
	public boolean isIsHead() {
		return IsHead;
	}
	public void setIsHead(boolean isHead) {
		IsHead = isHead;
	}
	
	
}
