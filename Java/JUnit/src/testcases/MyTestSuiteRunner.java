package testcases;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	UnderstandingAssertions.class,
	FirstTestCase.class, 
	SecondTestCase.class
	}
)
public class MyTestSuiteRunner {

}
