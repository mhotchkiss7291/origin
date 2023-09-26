/// <reference types="cypress" />

	it('google test', function() {
		cy.visit('https://google.com')
		cy.get('#APjFqb')
			.type('wsj')
			.type('{enter}')
		//cy.get(':nth-child(4) > .cIkxbf > .usJj9c > h3 > .l').click()
		//cy.wait(4000)
		cy.contains('Opinion')
			.click()
	})

	it('login test', function() {
		cy.visit('https://opensource-demo.orangehrmlive.com')
		cy.get(':nth-child(2) > .oxd-input-group > :nth-child(2) > .oxd-input')
			.type('Admin')
			.type('{enter}')
		cy.get(':nth-child(3) > .oxd-input-group > :nth-child(2) > .oxd-input').
			type('admin123')
		cy.get('.oxd-button')
			.click()
	})

	it.only('learning assertions', function() {

		cy.visit('https://example.cypress.io')
		cy.contains('get')
			.click()
		
		// Implicit Assertions
		cy.get('#query-btn')
		//cy.get('#query-btn', {timeout: 6000})
			.should('contain', 'Button')
			.should('have.class', 'query-btn')
			.should('be.visible')
			.should('be.enabled')
		cy.get('#query-btn')
			.invoke('attr', 'id')
			.should('equal', 'query-btn')
		cy.get('#query-btn')
			.should('contain', 'Button')
			.and('have.class', 'query-btn')

		// Explicit Assertions
		expect(true).to.be.true

		// Check for strings
		let name = 'Groucho'
		expect(name).to.be.equal('Groucho')

		assert.equal(4, 4, 'Not Equal')

		// assert.strictEqual(4, '4', 'Not Strictly Equal')
		assert.strictEqual(4, 4, 'Not Strictly Equal')
		//    .notEqual
		//    .strictEqual
		//    .isAbove
		//    .isBelow
		//    .exists
		//    .notExists
		//    .true
		//    .false
		//    .isString
		//    .isNotString
		//    .isNumber
		//    .isNotNumber

	})

