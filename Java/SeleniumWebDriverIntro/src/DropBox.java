import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DropBox {

	public static void main(String[] args) {

		WebDriver driver = new FirefoxDriver();
		driver.get("http://dropbox.com");
		driver.findElement(By.xpath("//*[@id='sign-in']")).click();;
		driver.findElement(By.xpath("//*[@id='pyxl8816']")).click();;
		

	}

}
