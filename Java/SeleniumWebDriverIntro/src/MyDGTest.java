import java.awt.AWTException;
import java.awt.Robot;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class MyDGTest {

	WebDriver driver = null;

	public static void main(String[] args) {

		MyDGTest mdgt = new MyDGTest();
		mdgt.login();
		mdgt.goToAdvancedSearch();
		mdgt.drawRectangleAOI();
		mdgt.quit();

	}

	public void login() {

		org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
		proxy.setSslProxy("moz-proxy://gdenwcflgmt.digitalglobe.com" + ":" + 8080);
		proxy.setFtpProxy("moz-proxy://gdenwcflgmt.digitalglobe.com" + ":" + 8080);
		proxy.setSocksUsername("EWICO\\username");
		proxy.setSocksPassword("password");

		DesiredCapabilities dc = DesiredCapabilities.firefox();
		dc.setCapability(CapabilityType.PROXY, proxy);

		this.driver = new FirefoxDriver(dc);
		driver.get("https://services-test.digitalglobe.com/myDigitalGlobe/login");
		driver.findElement(By.xpath("//*[@id='username']")).sendKeys("markh_dibu");
		driver.findElement(By.xpath("//*[@id='pw']")).sendKeys("test");
		driver.findElement(By.xpath("//*[@id='acceptTOS']")).click();
		driver.findElement(By.xpath("//*[@id='loginButton']")).click();

	}

	public void goToAdvancedSearch() {

		driver.findElement(By.xpath("//*[@id='leaflet-control-geosearch-qry']")).sendKeys("Long Beach, CA");
		driver.findElement(By.xpath("//*[@id='leaflet-control-geosearch-qry']")).sendKeys(Keys.RETURN);
		driver.findElement(By.xpath("//*[@id='test_myImagery']/span[1]")).click();
		driver.findElement(By.xpath("//*[@id='test_sifSearch']/span")).click();

	}

	public void drawRectangleAOI() {

		driver.findElement(By.xpath("//*[@id='mapDiv']/div[3]/div[1]/div[5]/a/i")).click();
		driver.findElement(By.xpath("//*[@id='sif-search-rect']")).click();

		wait(2);

		//Point drawAOILocation = driver.findElement(By.xpath("//*[@id='mapDiv']/div[3]/div[1]/div[5]/a/i")).getLocation();
		//System.out.println("x = " + drawAOILocation.x + " , y = " + drawAOILocation.y );
		
		//Point topLeft = new Point( drawAOILocation.x + 50, drawAOILocation.y);
		//System.out.println("x = " + topLeft.x + " , y = " + topLeft.y );

		WebElement drawAOIButton =  driver.findElement(By.xpath("//*[@id='mapDiv']/div[3]/div[1]/div[5]/a/i"));
		Actions builder = new Actions(driver);   
		builder.moveToElement(drawAOIButton, 80, 0).click().build().perform();

	}

	public void quit() {
		this.driver.quit();
	}

	public void wait(int seconds) {

		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
