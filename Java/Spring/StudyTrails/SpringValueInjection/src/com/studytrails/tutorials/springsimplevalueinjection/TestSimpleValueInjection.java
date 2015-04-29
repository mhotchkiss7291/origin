package com.studytrails.tutorials.springsimplevalueinjection;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSimpleValueInjection {

	public static void main(String[] args) {
		System.out.println("************** BEGINNING PROGRAM **************");

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"spring-config.xml");
		Person person = (Person) context.getBean("person");
		System.out.println(person);

		System.out.println("************** ENDING PROGRAM **************");
	}
}