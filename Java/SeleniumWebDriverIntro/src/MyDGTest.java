import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class MyDGTest {

	WebDriver driver = null;

	public static void main(String[] args) {

		MyDGTest mdgt = new MyDGTest();
		mdgt.login();
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
	
	public void quit() {
		
		this.driver.quit();
		
	}

}
