import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class RosettaStonePlayer {

	// Parameters for test
	String targetUsername = null;
	String targetPassword = null;
	String targetCatID = null;
	String targetLocationByName = null;

	// Test framework member variables
	WebDriver driver = null;
	String username = null;
	String password = null;
	List<WebElement> feature_list = null;

	public static void main(String[] args) {

		RosettaStonePlayer rsp = new RosettaStonePlayer();

		// Which browser are you using?
		// mdgt.useFirefox();
		rsp.useChrome();

		rsp.goToAdvancedSearch();
		rsp.logout();
		rsp.quit();

	}

	public void useFirefox() {

		this.driver = new FirefoxDriver();

	}

	public void useChrome() {

		this.driver = new ChromeDriver();

	}

	public void loginToServicesInt() {

		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

		// Get to MyDG in #INT
		driver.get("https://services-int.digitalglobe.com/myDigitalGlobe/login");
		wait(3);
		driver.manage().window().maximize();

		// For services-int only
		// Get past the Ad Gov Portal
		WebElement adGovPortalCloseButton = driver
				.findElement(By.xpath(".//*[@id='loginAlert']/div/div/div[3]/button"));
		adGovPortalCloseButton.click();

		// Enter credentials and log in
		WebElement cbuUsernameText = driver.findElement(By.xpath("//*[@id='username']"));
		cbuUsernameText.sendKeys(this.targetUsername);
		wait(1);
		WebElement cbuPasswordText = driver.findElement(By.xpath("//*[@id='pw']"));
		cbuPasswordText.sendKeys(this.targetPassword);
		wait(2);

		// Click the Log In button
		WebElement loginButton = driver.findElement(By.xpath(".//*[@id='loginButton']"));
		loginButton.click();
		wait(1);

	}

	public void logout() {

		WebElement userMenuDropdown = driver.findElement(By.xpath("//*[@id='test_userMenu']/span[1]"));
		userMenuDropdown.click();
		wait(2);

		WebElement logout = driver.findElement(By.xpath("//*[@id='logout']"));
		logout.click();
		wait(2);

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

	public void goToAdvancedSearch() {

		WebElement searchTextWindow = driver.findElement(By.xpath(".//*[@id='leaflet-control-geosearch-qry']"));
		searchTextWindow.sendKeys(this.targetLocationByName);
		wait(2);

		WebElement searchButton = driver.findElement(By.xpath(".//*[@id='leaflet-control-geosearch-submit-qry']"));
		searchButton.click();
		wait(2);

		WebElement myImageryButton = driver.findElement(By.xpath(".//*[@id='test_myImagery']/span"));
		myImageryButton.click();
		wait(2);

		// .//*[@id='test_sifSearch']/span
		WebElement advancedSearchButton = driver.findElement(By.xpath(".//*[@id='test_sifSearch']/span"));
		advancedSearchButton.click();
		wait(2);

	}
}
