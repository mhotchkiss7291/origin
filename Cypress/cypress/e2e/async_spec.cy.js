/// <reference types="cypress" />

const navbarText = Cypress.env('navbarText')

context('Asyncronous', () => {

    it('it types into an email field', () => {
        cy.visit('/commands/actions')
        cy.findByPlaceholderText('Email').type('test.email.com')
        // Debugging - no extraction of data
        console.log('test is finished')
        cy.log('1')
        cy.log('2')
        cy.log('3')
    })

    it('it waits for 5 seconds before saying test is finished', () => {

        cy.visit('/commands/actions')
        cy.findByPlaceholderText('Email').type('test.email.com')

        // Use the then() command to hijack cypress asynchronously
        cy.wait(2000).then(() => {

            // What is this?
            // eslint-disable-next-line no-console
            console.log('then command test is finished')
            fetch('https://api.spacexdata.com/v3/missions')
            .then((res) => res.json())
            .then((data) => {
                console.log(data)
            })

        })
        // Debugging - no extraction of data
        console.log('test is finished')
    })

    
})