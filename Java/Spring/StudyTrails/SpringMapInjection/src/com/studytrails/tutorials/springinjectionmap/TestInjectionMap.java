package com.studytrails.tutorials.springinjectionmap;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestInjectionMap {

	public static void main(String[] args) {
		System.out.println("************** BEGINNING PROGRAM **************");

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"spring-config.xml");
		TelephoneDirectory team = (TelephoneDirectory) context
				.getBean("telephoneDirectory");
		System.out.println("The contents of the telephone directory are : "
				+ team.getDirectoryMap());

		System.out.println("************** ENDING PROGRAM **************");
	}
}