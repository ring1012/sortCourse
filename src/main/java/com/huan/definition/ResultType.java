package com.huan.definition;

import java.util.ArrayList;

import com.huan.model.allData;


public class ResultType {

	public ArrayList<allData> datas;
	public int[][] sheetInfor;
	public int classNum = 15;
	public int lessonNum = 7;
	public static final double conflictCost=1000.0;
	public static final double connectCost=50.0;
	public double sumCost;
	
	
	public ResultType(ArrayList<allData> datas, int[][] sheetInfor) {
		this.datas =new ArrayList<>(datas);
		this.sheetInfor=new int[sheetInfor.length][sheetInfor[0].length];
		for(int i=0;i<sheetInfor.length;i++){
			for(int j=0;j<sheetInfor[0].length;j++){
				this.sheetInfor[i][j] = sheetInfor[i][j] ;
			}
		}
		
	}
	public ResultType(){
		
	}
	
	public double getCost(ArrayList<Integer[]> definedCost){
		double sum=0,p1=0,p2=0,p3=0;
		for(int i=0;i<datas.size();i++){
			Integer certainCost[] = definedCost.get(datas.get(i).courseIndex);
			for(int j=0;j<datas.get(i).arrangeCells.size();j++){
				Position temp=datas.get(i).arrangeCells.get(j);
				p1+=certainCost[temp.timeY%lessonNum];
			}
			
			p2+=conflictCost*datas.get(i).conflictCells.size();
			p3+=connectCost*datas.get(i).connectCells.size()/2;
			
		}
				
				
				
		sum=p1+p2+p3;	
		sumCost=sum;
		return sum;
		
	}
	
}