package test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestWikipedia {
	
	public static void main(String[] args) {

		// Open new driver
		WebDriver driver = new FirefoxDriver();
		driver.get("http://www.wikipedia.org");

		// Click on English link
		WebElement link;
		link = driver.findElement(By.linkText("English"));
		link.click();

		// Wait 5 seconds
		wait(5);
		
		// Click on id searchInput element
		WebElement searchBox;
		searchBox = driver.findElement(By.id("searchInput"));
		searchBox.sendKeys("Software");

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
