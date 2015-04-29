package com.studytrails.tutorials.springinjectionproperties;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestInjectionProperties {

	public static void main(String[] args) {
		System.out.println("************** BEGINNING PROGRAM **************");

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"spring-config.xml");

		Car team = (Car) context.getBean("car");

		System.out.println("The specifications of the car are : "
				+ team.getSpecifications());

		System.out.println("************** ENDING PROGRAM **************");
	}
}