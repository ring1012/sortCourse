package com.huan.teacher.service.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

public class UtilTest {

	@Test
	public void test01() {
		List<Integer>myList=new ArrayList<>(1);
		myList.add(3);
		myList.add(5);
		myList.add(8);
		System.out.println(myList.size());
		
	}
	
	@Test
	public void test02(){
		Random random=new Random();
		for(int i=0;i<10;i++){
			System.out.println(random.nextInt(2));
		}
	}

}
