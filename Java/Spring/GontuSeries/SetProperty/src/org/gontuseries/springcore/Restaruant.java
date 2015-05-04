package org.gontuseries.springcore;

public class Restaruant {
	
	String welcomeNote;

	public String getWelcomeNote() {
		return welcomeNote;
	}

	public void setWelcomeNote(String welcomeNote) {
		this.welcomeNote = welcomeNote;
	}

	public void greetCustomer() {
		System.out.println("Welcome to our Restaruant!!");
	}

}
