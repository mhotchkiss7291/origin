import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class MyDGTest {

	WebDriver driver = null;
	String username = null;
	String password = null;

	public static void main(String[] args) {

		MyDGTest mdgt = new MyDGTest();
		mdgt.login();
		mdgt.goToAdvancedSearch();
		mdgt.drawRectangleAOI();
		mdgt.quit();

	}

	public void login() {

		loadProperties();

		org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
		proxy.setSslProxy("moz-proxy://gdenwcflgmt.digitalglobe.com" + ":" + 8080);
		proxy.setFtpProxy("moz-proxy://gdenwcflgmt.digitalglobe.com" + ":" + 8080);
		proxy.setSocksUsername("EWICO\\username");
		proxy.setSocksPassword("password");

		DesiredCapabilities dc = DesiredCapabilities.firefox();
		dc.setCapability(CapabilityType.PROXY, proxy);

		this.driver = new FirefoxDriver(dc);
		driver.get("https://services-test.digitalglobe.com/myDigitalGlobe/login");
		driver.manage().window().maximize();
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

		WebElement drawAOIButton = driver.findElement(By.xpath("//*[@id='mapDiv']/div[3]/div[1]/div[5]/a/i"));

		Point buttonLocation = drawAOIButton.getLocation();
		Point start = new Point(buttonLocation.getX(), buttonLocation.getY());
		System.out.println("x = " + start.getX() + " , y = " + start.getY());

		Actions actions = new Actions(driver);
		actions.moveToElement(drawAOIButton).build().perform();
		actions.moveByOffset(30, 0).build().perform();
		actions.clickAndHold().build().perform();
		actions.moveByOffset(300, 300).build().perform();
		actions.release().build().perform();
		wait(5);
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

	public void loadProperties() {

		InputStream inputStream = null;

		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";

			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			this.username = prop.getProperty("username");
			this.password = prop.getProperty("password");
			System.out.println("username = " + username + " , password = " + password);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
