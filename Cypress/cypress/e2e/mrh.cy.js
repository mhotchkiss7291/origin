/// <reference types="cypress" />

	it('google test', function() {

		cy.visit('https://google.com')
		cy.get('#APjFqb').type('wsj{enter}')
		//cy.get('#APjFqb', {timeout: 6000}).type('wsj{enter}')
		//cy.get(':nth-child(5) > .cIkxbf > .usJj9c > h3 > .l').click()
		cy.get(':nth-child(4) > .cIkxbf > .usJj9c > h3 > .l').click()

	})
