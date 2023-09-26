/// <reference types="cypress" />

	it('google test', function() {

		cy.visit('https://google.com')
		cy.get('#APjFqb').type('wsj{enter}')
		//cy.get(':nth-child(4) > .cIkxbf > .usJj9c > h3 > .l').click()
		//cy.wait(4000)
		cy.contains('Opinion').click()

	})

	it.only('login test', function() {

		cy.visit('https://opensource-demo.orangehrmlive.com')
		cy.get(':nth-child(2) > .oxd-input-group > :nth-child(2) > .oxd-input').type('Admin{enter}')
		cy.get(':nth-child(3) > .oxd-input-group > :nth-child(2) > .oxd-input').type('admin123')
		cy.get('.oxd-button').click()

	})
