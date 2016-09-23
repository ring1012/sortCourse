package com.huan.course.util;

public class ConvertUtil {
	public static int[] parsIntArray(String []param){
		int ret[]=new int [param.length];
		try {
			for(int i=0;i<param.length;i++){
				ret[i]=Integer.parseInt(param[i]);
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return ret;
	}

}
