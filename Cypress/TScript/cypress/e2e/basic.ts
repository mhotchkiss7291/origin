it('google test', function() {

    cy.visit('https://google.com')
    cy.get('[id="input').type('wsj{enter}')
    //cy.get(':nth-child(4) > .cIkxbf > .usJj9c > h3 > .l').click()
    //cy.wait(4000)
    //cy.contains('Opinion').click()

})