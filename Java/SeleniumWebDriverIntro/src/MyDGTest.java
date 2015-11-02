import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
	List<WebElement> feature_list = null;

	public static void main(String[] args) {

		MyDGTest mdgt = new MyDGTest();
		mdgt.login();
		mdgt.goToAdvancedSearch();
		mdgt.drawRectangleAOI();
		mdgt.scanSifResultsGrid();
		mdgt.scanFeatureListForArchive();
		// mdgt.quit();

	}

	public void login() {

		// loadProperties();

		org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
		proxy.setSslProxy("moz-proxy://gdenwcflgmt.digitalglobe.com" + ":" + 8080);
		DesiredCapabilities dc = DesiredCapabilities.firefox();
		dc.setCapability(CapabilityType.PROXY, proxy);

		this.driver = new FirefoxDriver(dc);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.get("https://services-test.digitalglobe.com/myDigitalGlobe/login");
		// driver.manage().window().maximize();
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

	public void scanFeatureListForArchive() {

		java.util.Iterator<WebElement> i = this.feature_list.iterator();

		while (i.hasNext()) {
			WebElement row = i.next();
			System.out.println("data-featureid = " + row.getAttribute("data-featureid"));

			List<WebElement> cells = row.findElements(By.tagName("td"));
			java.util.Iterator<WebElement> j = cells.iterator();

			while (j.hasNext()) {
				WebElement cell = j.next();
				//System.out.println("cell text = " + cell.getText());
				//System.out.println("clss = " + cell.getAttribute(arg0));
			}
		}

		// driver.switchTo().defaultContent();
		// click on the 5th feature in the list's advFeatureOptionsButton
		// WebElement archive =
		// driver.findElement(By.xpath("//*[@id='sifResultsGrid']/table/tbody/tr[5]/td/*[@id='advFeatureOptionsButton']/a/i"));

		// WebElement archiveRows =
		// driver.findElement(By.xpath("//*[@id='sifResultsGrid']/table/tbody"));

		// Get List of cells
		// List<WebElement> cells = archiveRows.findElements(By.tagName("tr"));
		// java.util.Iterator<WebElement> i = rows.iterator();

		// while (i.hasNext()) {
		// WebElement row = i.next();
		// System.out.println("text = " + row.getText());
		// }

		// this.feature_list = rows;

		// archive.click();

		// highlightElement(archive);
		// wait(30);

		// Scan the feature sifResultsGrid for feature labels
		// WebElement archiveTable =
		// driver.findElement(By.xpath("//*[@id='sifResultsGrid']/table/tbody/tr"));

		// List<WebElement> rows = archiveTable.findElements(By.tagName("td"));
		// java.util.Iterator<WebElement> i = rows.iterator();

		// while (i.hasNext()) {
		// WebElement row = i.next();
		// System.out.println("text = " + row.getText());
		// System.out.println("tag = " + row.getTagName());
		// System.out.println("class = " + row.getClass());
		// }

		// 1030010049849E00
		// /html/body/div[1]/div/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/table/tbody/tr[7]

		// driver.findElement(By.cssSelector(".dataLabel,.dataLabelWide");

		// if (row.getText().equals("Archive 2015-09-17 WV02")) {

		// System.out.println("i = " + i);

		// wait(30);

		// WebElement desiredFeature =
		// driver.findElement(By.xpath("//*[@id='advFeatureOptionsButton']/a/i"));
		// highlightElement(desiredFeature);
		// List<WebElement> advFeatureOptionsRows =
		// archiveTable.findElements(By.tagName("li"));
		// java.util.Iterator<WebElement> j =
		// advFeatureOptionsRows.iterator();
		// while (j.hasNext()) {
		// WebElement advFeatureOptionsRow = j.next();
		// System.out.println(advFeatureOptionsRow.getText());
		// }
		// }
		// }

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

}
