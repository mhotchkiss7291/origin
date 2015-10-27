import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestApp {
	
	public static void main(String[] args) {
		
		WebDriver d1 = new FirefoxDriver();
		d1.get("http://yahoo.com");

		WebDriver d2 = new FirefoxDriver();
		d2.get("http://bbc.com");

		System.out.println(d1.getTitle());
		System.out.println(d2.getTitle());
		
		d1.quit();
		d2.quit();
	}

}
