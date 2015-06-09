import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

import org.junit.Test;
import org.openqa.selenium.By;

public class SelenideTest {

	public static void main(String[] args) {
		SelenideTest st = new SelenideTest();
		st.userCanLoginByUsername();
	}

	@Test
	public void userCanLoginByUsername() {

		open("/login");
		$(By.name("user.name")).setValue("johny");
		$("#submit").click();
		
		// Waits until element disappears
		$(".loading_progress").should(disappear); 
		
		// Waits until element gets text
		$("#username").shouldHave(text("Hello, Johny!")); 
	}
}
