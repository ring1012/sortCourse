package com.huan.model;

import java.util.ArrayList;

import com.huan.definition.Position;

public class allData {
	public String teacherName;
	public String courseName;
	public int perWeekClassNum;
	public int perWeekTimeNum;
	public boolean IsHead;
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
}