package com.huan.model;

import java.util.ArrayList;

import com.huan.definition.Position;

public class allData {
	public int teacherIndex;
	public String teacherName;
	public String courseName;
	public int perWeekClassNum;
	public int perWeekTimeNum;
	public boolean IsHead;
	public boolean IsNext;
	public int courseIndex;
	public ArrayList<Integer>classes;
	public ArrayList<Position>arrangeCells;
	public ArrayList<Integer>weekY;
	public ArrayList<Position>conflictCells;
	public ArrayList<Position>connectCells;
	public allData(){
		classes=new ArrayList<Integer>();
		arrangeCells=new ArrayList<Position>();
		weekY=new ArrayList<Integer>();
		conflictCells=new ArrayList<Position>();
		connectCells=new ArrayList<>();
	}
//	public allData(String teacherName, String courseName, int perWeekClassNum, int perWeekTimeNum, boolean isHead) {
//		super();
//		this.teacherName = teacherName;
//		this.courseName = courseName;
//		this.perWeekClassNum = perWeekClassNum;
//		this.perWeekTimeNum = perWeekTimeNum;
//		IsHead = isHead;
//		classes=new ArrayList<Integer>();
//		arrangeCells=new ArrayList<Position>();
//		weekY=new ArrayList<Integer>();
//		conflictCells=new ArrayList<Position>();
//		connectCells=new ArrayList<>();
//	}
	
	public allData(String teacherName, String courseName, int perWeekClassNum, int perWeekTimeNum, boolean isHead,boolean isNext,int teacherIndex) {
		super();
		this.teacherName = teacherName;
		this.courseName = courseName;
		this.perWeekClassNum = perWeekClassNum;
		this.perWeekTimeNum = perWeekTimeNum;
		this.IsHead = isHead;
		this.IsNext=isNext;
		this.teacherIndex=teacherIndex;
		classes=new ArrayList<Integer>();
		arrangeCells=new ArrayList<Position>();
		weekY=new ArrayList<Integer>();
		conflictCells=new ArrayList<Position>();
		connectCells=new ArrayList<>();
	}
	
	
	
}
