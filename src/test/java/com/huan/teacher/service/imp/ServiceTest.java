package com.huan.teacher.service.imp;

import org.junit.Test;
public class ServiceTest {

	public int a=7;
	@Test
	public void testQueryAll() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		System.out.println(ServiceTest.class);
		ServiceTest myTest=new ServiceTest();
		System.out.println(myTest.getClass());
		Class<ServiceTest> test2=(Class<ServiceTest>) Class.forName("com.huan.teacher.service.imp.ServiceTest");
		ServiceTest test3=	 test2.newInstance();
		System.out.println(test3.a);
		}

}