/// <reference types="cypress" />

const navbarText = Cypress.env('navbarText')

context('Asyncronous', () => {

    it('it types into an email field', () => {
        cy.visit('/commands/actions')
        cy.findByPlaceholderText('Email').type('test.email.com')
        cy.wait(5000)
        console.log('test is finished')
    })


    
})