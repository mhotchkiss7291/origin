import {LoginPage} from "..//e2e/pages/login_page.cy" 

const loginPage = new LoginPage()

it('page object model', function() {
    loginPage.navigate();
    loginPage.enterUsername('Admin');
    loginPage.enterPassword('admin123');
    loginPage.clickLogin();
})