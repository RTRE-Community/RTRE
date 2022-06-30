

describe('Admin Test', () => {
    
  it('Login to second user to check if user has access', () => {
    cy.window().then((win) => {
      win.sessionStorage.clear()
    });

    cy.visit('http://localhost:8080')
    cy.url().should('include', 'Login')

    cy.get('input#input-38').type('admin@admin.com')
    cy.get('input#input-41').type('password')
    cy.intercept("**/api/login?*").as('adminLogin')
    cy.get('.col-sm-3 > .v-btn > .v-btn__content').click()

    cy.wait('@adminLogin').then((intercept) => {
      expect(intercept.response.statusCode).to.equal(200)
    })
    cy.get('.rounded-0 > .v-toolbar__content > .v-toolbar__title').should('contain', 'User Profile')
    cy.get('.v-expansion-panel-header').click()
  })
})