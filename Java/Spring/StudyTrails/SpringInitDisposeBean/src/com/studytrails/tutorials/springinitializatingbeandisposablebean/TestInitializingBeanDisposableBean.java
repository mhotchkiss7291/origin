package com.studytrails.tutorials.springinitializatingbeandisposablebean;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestInitializingBeanDisposableBean {

	public static void main(String[] args) {
		System.out.println("************** BEGINNING PROGRAM **************");

		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				"spring-config.xml");

		context.close();

		System.out.println("************** ENDING PROGRAM **************");
	}
}