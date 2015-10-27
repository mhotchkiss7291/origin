package testcases;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SecondTestCase {
	
	//@Test
	//@Ignore
	//@Before
	//@After
	//@BeforeClass
	//@AfterClass
	
	// hypothetical method
	public static boolean checkLogin() {
		return false;
	}
	
	@BeforeClass // Has to be static
	public static void beginning() {
		System.out.println("*****************beginning*******************");
		
		//Stop the execution of this test
		//Assume.assumeTrue(checkLogin()); //false - skip all of the tests
		                                 //in this file
	}

	@AfterClass // Has to be static
	public static void ending() {
		System.out.println("*****************ending*******************");
	}

	// Before each @Test method
	@Before
	public void openBrowser() {
		
		System.out.println("Open the browser");
		
	}

	@Test
	public void sendEmailTest() {
		
		System.out.println("Testing sending email");
		
	}
	
	@Test
	public void sendMessageTest() {
		
		System.out.println("Send message");
		
	}

	//browser should close after each @Test method
	@After
	public void closeBrowser() {
		
		System.out.println("Close the browser");
	}

}
