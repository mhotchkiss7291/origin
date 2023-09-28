/// <reference types="cypress" />

context('Actions', () => {
    beforeEach(() => {
      cy.visit('https://example.cypress.io/commands/actions')
    })

    it('has an h1 on the pate', () => {
        cy.get('h1').should('exist');
    })

    it('renders the correct h1 text', () => {
        cy.get('h1').should('contain.text', 'Actions');
    })

    it('renders a paragraph under the h1', () => {
        // Use class syntax for container class
        // Gets all containers with p
        cy.get('.container').find('p').should('exist');
        // Narrow it to the second container and within
        // that container find the paragraph
        cy.get('.container').eq(1).find('p').should('exist');

    })

    it('renders a section with correct elements', () => {
        // Look within the third container
        cy.get('.container').eq(2).within(() => {
            // h4 text should exist in the third container
            cy.get('h4').should('exist');
        })
    })

    it.only('correctly renders the cypress website link', () => {
        cy.findByText('cypress.io').should('exist')
    })


    
})