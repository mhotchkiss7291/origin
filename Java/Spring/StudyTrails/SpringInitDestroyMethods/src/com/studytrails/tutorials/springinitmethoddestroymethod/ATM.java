package com.studytrails.tutorials.springinitmethoddestroymethod;

public class ATM {

	public void init() {
		System.out
				.println("ATM init method called. Connecting to bank's network.");
	}

	public void cleanUp() {
		System.out
				.println("ATM destroy method called. Disconnecting from bank's network.");
	}

}