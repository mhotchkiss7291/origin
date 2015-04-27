package com.studytrails.tutorials.springcontructorinjection;

public class ATM {

	private Printer printer;

	public ATM(Printer printer) {
		this.printer = printer;
	}

	public void printBalanceInformation(String accountNumber) {
		printer.printBalanceInformation(accountNumber);

	}

}