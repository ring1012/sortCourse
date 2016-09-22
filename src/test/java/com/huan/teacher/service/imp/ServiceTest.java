package com.huan.teacher.service.imp;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.huan.model.Teacher;
import com.huan.teacher.service.ITeacherService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:configs/applicationContext.xml","classpath:configs/dispatcher-servlet.xml", "classpath:mybatis-config.xml" })
public class ServiceTest {
	@Autowired
	private ITeacherService teacherService;

	@Test
	public void testQueryAll() {
		List<Teacher> userInfos = teacherService.findAll();
		System.out.println(userInfos.size());
	}


}