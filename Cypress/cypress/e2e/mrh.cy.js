/// <reference types="cypress" />

	it('google test', function() {

		cy.visit('https://google.com')
		cy.get('#APjFqb').type('wsj{enter}')
		//cy.get(':nth-child(5) > .cIkxbf > .usJj9c > h3 > .l').click()

	})
