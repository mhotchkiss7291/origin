package test;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestShutterStock {
	
	public static void main(String[] args) {

		// Open new driver
		WebDriver driver = new FirefoxDriver();
		driver.get("http://www.shutterstock.com");

		// Click on id searchInput element
		WebElement searchInput;
		searchInput = driver.findElement(By.id("search-input"));
		searchInput.sendKeys("Hotchkiss Colorado" + Keys.RETURN);

		// Wait 5 seconds and then quit
		wait(5);
		driver.quit();
		
	}
	
	static void wait(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
