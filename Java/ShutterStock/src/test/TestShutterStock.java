package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestShutterStock {
	
	public static void main(String[] args) {
		TestShutterStock tss = new TestShutterStock();
		tss.runTest();
	}

		
	@Test
	public void runTest() {
		
		// Open new driver
		WebDriver driver = new FirefoxDriver();
		driver.get("http://www.shutterstock.com");

		// Click on id searchInput element
		WebElement searchInput;
		searchInput = driver.findElement(By.id("search-input"));
		searchInput.sendKeys("Hotchkiss Colorado" + Keys.RETURN);

		// Wait 5 seconds and then quit
		wait(5);
		searchInput = driver.findElement(By.className("gc_clip"));
		searchInput.click();
		
		wait(5);
		WebElement download_button;
		download_button = driver.findElement(By.name("submit_jpg"));
		download_button.click();
		wait(5);

		String pageTitle = driver.getTitle();
		assertEquals("Current page title", "Stock Photos, Royalty-Free Images and Vectors - Shutterstock", pageTitle);
		
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
