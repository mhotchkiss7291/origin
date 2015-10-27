package testcases;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

public class UnderstandingAssertions {
	
	@Rule
	public ErrorCollector errorCollector = new ErrorCollector(); 

	@Test
	public void testFriendlistFacebook() {
		
		int actual_total_friends = 100;
		int expected_total_friends = 100;
		
		//if(actual_total_friends == expected_total_friends ) {
		//	System.out.println("Pass");
		//} else {
		//	System.out.println("Fail");
			// Report the error
		//}
		
		System.out.println("A");
		try {
			Assert.assertEquals(expected_total_friends, actual_total_friends);
		} catch (Throwable t) {
			System.out.println("Error Encountered");
			// Write some code to report the error
			errorCollector.addError(t);
		}
		
		// Not printed because assertEquals stops the test
		System.out.println("B");
				
		try {
			Assert.assertEquals("A","B");
		} catch (Throwable t) {
			System.out.println("Error Encountered");
			errorCollector.addError(t);
		}

		try {
			Assert.assertEquals("A","A");
		} catch (Throwable t) {
			System.out.println("Error Encountered");
			errorCollector.addError(t);
		}
		
		Assert.assertTrue("error msg", 4 < 3);
	}

}
