package com.studytrails.tutorials.springinjectionset;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestInjectionSet {

	public static void main(String[] args) {
		System.out.println("************** BEGINNING PROGRAM **************");

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"spring-config.xml");
		Team team = (Team) context.getBean("team");
		System.out
				.println("The players in the team are : " + team.getPlayers());

		System.out.println("************** ENDING PROGRAM **************");
	}
}