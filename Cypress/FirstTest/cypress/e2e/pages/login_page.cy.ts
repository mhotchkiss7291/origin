export class LoginPage {

    // Page locators
    loginPage_url = 'https://opensource-demo.orangehrmlive.com'
    loginPage_username = ':nth-child(2) > .oxd-input-group > :nth-child(2) > .oxd-input'
    loginPage_password = ':nth-child(3) > .oxd-input-group > :nth-child(2) > .oxd-input'
    loginPage_loginButton = '.oxd-button' 
    
    navigate(){
        cy.visit(this.loginPage_url)
    }

    enterUsername(username: string){
		cy.get(this.loginPage_username)
			.type(username)
			.type('{enter}')
    }

    enterPassword(password: string){
		cy.get(this.loginPage_password).
			type(password)
    }

    clickLogin(){
		cy.get(this.loginPage_loginButton)
			.click()
    }
}