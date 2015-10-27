package testcases;

import org.junit.Ignore;
import org.junit.Test;

public class FirstTestCase {
	
	// Test methods are run from top to bottom
	// when methods are named *Test()

	@Test
	public void loginTest() {

		System.out.println("Log in user");
	}

	@Ignore
	@Test
	public void registerTest() {

		System.out.println("Registering a user");

	}

	@Test
	public void databaseTest() {

		System.out.println("Testing the database");
	}
}
