package com.studytrails.tutorials.springinjectionlist;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestInjectionList {

	public static void main(String[] args) {
		System.out.println("************** BEGINNING PROGRAM **************");

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"spring-config.xml");
		ToDoList toDoList = (ToDoList) context.getBean("toDoList");
		System.out.println(toDoList.getTasks());

		System.out.println("************** ENDING PROGRAM **************");
	}
}