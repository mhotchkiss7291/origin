import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class MyDGCbuTest {

	// Parameters for test
	String targetUsername = null;
	String targetPassword = null;
	String targetCatID = null;
	
	// Test framework member variables
	WebDriver driver = null;
	String username = null;
	String password = null;
	List<WebElement> feature_list = null;

	public static void main(String[] args) {

		MyDGCbuTest mdgt = new MyDGCbuTest();
		
		mdgt.login();
		mdgt.goToAdvancedSearch();
		//mdgt.drawRectangleAOIOverLongBeach();
		//mdgt.scanSifResultsGrid();
		//mdgt.scanAndSelectArchiveFeatureFromList(this.targetCatID);
		//mdgt.enterOrderName();
		//mdgt.addToCartAndClose();
		//mdgt.goToCart();
		//mdgt.submitOrdersAndClose();
		//mdgt.logout();
		//mdgt.quit();

	}

	public void login() {
		
		this.targetUsername = "MRH_User";
		this.targetPassword = "test";
		this.targetCatID = "103001004A20CD00";
		
		// Not needed at the moment
		// loadProperties();

		// Get past the DG proxy for test
		org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
		proxy.setSslProxy("moz-proxy://gdenwcflgmt.digitalglobe.com" + ":" + 8080);
		DesiredCapabilities dc = DesiredCapabilities.firefox();
		dc.setCapability(CapabilityType.PROXY, proxy);

		// Open the firefox driver and set timeout for inaction
		this.driver = new FirefoxDriver(dc);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		
		// Get to MyDG in #INT
		driver.get("https://services-int.digitalglobe.com/myDigitalGlobe/login");
		driver.manage().window().maximize();
		
		// Get past the Ad Gov Portal
		WebElement adGovPortalCloseButton = driver.findElement(By.xpath("//*[@id='loginAlert']/div/div/div[3]/button"));
		adGovPortalCloseButton.click();

		// Enter credentials and log in
		WebElement cbuUsernameText = driver.findElement(By.xpath("//*[@id='username']"));
		cbuUsernameText.sendKeys(this.targetUsername);
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

	// not used right now
	/*
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

	public void highlightElement(WebElement element) {
		String originalStyle = element.getAttribute("style");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
		js.executeScript("arguments[0].setAttribute('style', '" + originalStyle + "');", element);
	}
	*/

	public void goToAdvancedSearch() {

		WebElement searchTextWindow = driver.findElement(By.xpath("//*[@id='leaflet-control-geosearch-qry']"));
		searchTextWindow.sendKeys("Long Beach, CA");

		WebElement searchButton = driver.findElement(By.xpath(".//*[@id='leaflet-control-geosearch-submit-qry']"));
		searchButton.click();

	}

	public void drawRectangleAOIOverLongBeach() {
		driver.findElement(By.xpath("//*[@id='mapDiv']/div[3]/div[1]/div[5]/a/i")).click();
		driver.findElement(By.xpath("//*[@id='sif-search-rect']")).click();
		wait(2);
		WebElement drawAOIButton = driver.findElement(By.xpath("//*[@id='mapDiv']/div[3]/div[1]/div[5]/a/i"));
		Actions actions = new Actions(driver);
		actions.moveToElement(drawAOIButton).build().perform();
		actions.moveByOffset(30, 0).build().perform();
		actions.clickAndHold().build().perform();
		actions.moveByOffset(600, 400).build().perform();
		actions.release().build().perform();
		wait(5);
	}

	public void scanSifResultsGrid() {

		driver.switchTo().defaultContent();
		WebElement archiveRows = driver.findElement(By.xpath("//*[@id='sifResultsGrid']/table/tbody"));
		// Get List of rows
		List<WebElement> rows = archiveRows.findElements(By.tagName("tr"));
		java.util.Iterator<WebElement> i = rows.iterator();
		this.feature_list = rows;
	}

	public void scanAndSelectArchiveFeatureFromList() {

		// Scan feature list for a particular archive CatID
		java.util.Iterator<WebElement> element = this.feature_list.iterator();

		// Count the rows to find the CatID arg and click the
		// advFeaturOptionsButton
		int i = 0;
		while (element.hasNext()) {
			WebElement row = element.next();
			i++;
			// System.out.println("data-featureid =
			// "row.getAttribute("data-featureid"));

			if (row.getAttribute("data-featureid").equals(this.targetCatID)) {
				driver.switchTo().defaultContent();

				// Print which row of the Feature table you are selecting
				// System.out.println("i = " + i);

				// This click action displays the dropdown just fine
				WebElement button = driver.findElement(By.xpath(
						"//*[@id='sifResultsGrid']/table/tbody/tr[" + i + "]/td/*[@id='advFeatureOptionsButton']/a/i"));
				button.click();

				WebElement addImageToCart = driver.findElement(By
						.xpath("//*[@id='sifResultsGrid']/table/tbody/tr[" + i + "]//*[@id='menuOptionOrderImage']/a"));
				addImageToCart.click();
			}
		}
	}

	public void enterOrderName() {

		WebElement orderNameText = driver.findElement(By.xpath("//*[@id='orderTitle']"));
		orderNameText.sendKeys("mrhLongBeach");
		wait(1);

	}

	public void addToCartAndClose() {

		// Due to multiple ids of "submitBtn" need to you absolute path with id
		// to help
		WebElement addToCartButton = driver.findElement(By.xpath("html/body/div[9]/div/div/div[3]/*[@id='submitBtn']"));
		addToCartButton.click();

		WebElement closeButton = driver.findElement(By.xpath("//*[@id='mapPage']/div[28]/div/div/div[2]/button[1]"));
		closeButton.click();
	}

	public void goToCart() {

		WebElement cartIcon = driver
				.findElement(By.xpath("//*[@id='mapPage']/div[1]/header/div/div/nav/div/div[2]/ul[2]/li[1]/a/i"));
		cartIcon.click();

		WebElement cartSelection = driver.findElement(By.xpath("//*[@id='test_cart']"));
		cartSelection.click();
		wait(2);

	}

	public void submitOrdersAndClose() {

		WebElement submitOrdersButton = driver.findElement(By.xpath("//*[@id='cartOrderBtn']"));
		submitOrdersButton.click();

		WebElement closeButton = driver.findElement(By.xpath("//*[@id='cartCancelBtn']"));
		closeButton.click();

	}
}
