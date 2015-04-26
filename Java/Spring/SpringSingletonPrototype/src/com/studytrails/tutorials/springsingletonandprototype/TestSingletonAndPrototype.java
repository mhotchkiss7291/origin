package com.studytrails.tutorials.springsingletonandprototype;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSingletonAndPrototype {

	public static void main(String[] args) {
		System.out.println("************** BEGINNING PROGRAM **************");

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"spring-config.xml");

		TicketVendingMachine ticketVendingMachine1 = (TicketVendingMachine) context
				.getBean("ticketVendingMachine");
		TicketVendingMachine ticketVendingMachine2 = (TicketVendingMachine) context
				.getBean("ticketVendingMachine");
		boolean areTicketVendingMachineInstancesSame = ticketVendingMachine1
				.equals(ticketVendingMachine2);
		System.out.println("Are ticket vending machine instances same : "
				+ areTicketVendingMachineInstancesSame);

		Ticket ticket1 = (Ticket) context.getBean("ticket");
		Ticket ticket2 = (Ticket) context.getBean("ticket");
		boolean areTicketInstancesSame = ticket1.equals(ticket2);
		System.out.println("Are ticket instances same : "
				+ areTicketInstancesSame);

		System.out.println("************** ENDING PROGRAM **************");
	}
}