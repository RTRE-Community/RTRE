import 'cypress-file-upload';

let url = 'http://localhost:8080'
const adminEmail = 'admin@admin.com'
const password = 'password'

describe('Admin Test', () => {

    before(() => {
        cy.window().then((win) => {
            win.sessionStorage.clear()
          });
          
        cy.visit(url)
        
      cy.get('input#input-38').type(adminEmail)
      cy.get('input#input-41').type(password)
      cy.intercept("**/api/login?*").as('adminLogin')
      cy.get('.col-sm-3 > .v-btn > .v-btn__content').click()
    })

    it('Check in a file to project', () => {
        cy.get('.v-expansion-panel-header').click()
        cy.get('.v-expansion-panel-content__wrap > :nth-child(1)').then(($id) => {
            var fullText = $id.text();
            var pattern = /[0-9]+/g;
            var number = fullText.match(pattern);
          let id = number
          cy.wrap(id).as('id')
            cy.get('@id').then((id) => {
                cy.get('[name="fileButtonCheckIn"]').attachFile('stud.ifc')
                cy.get('.shrink > .v-input__control > .v-input__slot').type(id[0])
                cy.get('.v-input--dense > .v-input__control > .v-input__slot').click()
                cy.get('.v-list-item__content').contains('Ifc4').click()
                cy.intercept('**/api/postIfcAsSubProject?*').as('postAProject')
                cy.get('.ml-11').click()
                cy.wait('@postAProject').then((intercept) => {
                    expect(intercept.response.statusCode).to.equal(200)
                    cy.get('[name="refresh"]').click()
                })
            })

        })
  
    })

     
    it('Check Notification', () => {
        cy.wait(4000)
        cy.get('.text-center').click()
        cy.get('.v-list').should('contain','Newly added project!')
        cy.intercept('**/api/deleteNotification?*').as('deleteNotification')
        cy.get('[name="0"]').click()
        cy.wait('@deleteNotification').then((intercept) => {
            expect(intercept.response.statusCode).to.equal(200)
        })
    })

    it('Check out file', () => {
        cy.get('.v-expansion-panel-header').click()
        cy.get(':nth-child(4) > .v-expansion-panel > .v-expansion-panel-header').click()
        cy.get(':nth-child(4) > .v-expansion-panel > .v-expansion-panel-content > .v-expansion-panel-content__wrap > :nth-child(2)')
        .then(($id) => {
            var fullText = $id.text();
            var pattern = /[0-9]+/g;
            var number = fullText.match(pattern);
        let id = number
        cy.wrap(id).as('id')
        cy.get('.v-slide-group__content > :nth-child(3)').click()
        cy.get('.v-card__text > .v-form > .v-input > .v-input__control > .v-input__slot').type(id[0])
        cy.intercept('**/api/getIfc?*').as('getIfcFile')
        cy.get('.v-card__text > .v-form > .blue').click()
        cy.wait('@getIfcFile').then((intercept) => {
            expect(intercept.response.statusCode).to.equal(200)
            cy.wrap(intercept.response.body.length).should('be.gt',13000)
      })
    })
    })

    it('Merge file', () => {

    cy.get(':nth-child(4) > .v-expansion-panel > .v-expansion-panel-content > .v-expansion-panel-content__wrap > :nth-child(2)')
    .then(($id) => {
        var fullText = $id.text();
        var pattern = /[0-9]+/g;
        var number = fullText.match(pattern);
    let id = number
    
    cy.get('.v-slide-group__content > :nth-child(4)').click()
    cy.get('.v-card__text > .v-form > .v-input--dense > .v-input__control > .v-input__slot').type(id[0])
  })
    cy.get('[name="mergeFileInput"').attachFile('stud.ifc')
    cy.intercept('**/api/merge?*').as('merge')
    cy.get('[name="mergeButton"]').click()
    cy.wait('@merge').then((intercept) => {
      expect(intercept.response.statusCode).to.equal(200)
    })
    cy.get('[name="refresh"]').click()
    })

    it('Button check out', () => {
        cy.get('.v-expansion-panel-header').click()
        cy.get(':nth-child(4) > .v-expansion-panel > .v-expansion-panel-header').click()
        cy.intercept('**/api/getIfc?*').as('getIfcFile')
        cy.get('.green').click()
        cy.wait('@getIfcFile').then((intercept) => {
            expect(intercept.response.statusCode).to.equal(200)
            cy.wrap(intercept.response.body.length).should('be.gt',20000)
      })
    })

    it('Button delete', () => {
        cy.intercept('**/api/deleteProject?*').as('deleteProject')
        cy.get('.red > .v-btn__content').click()
        cy.wait('@deleteProject').then((intercept) => {
            expect(intercept.response.statusCode).to.equal(200)
        })
        cy.get('[name="refresh"]').click()
        cy.wait(2000 )
        cy.get('.v-expansion-panel-header').click()
        cy.get('[popout=""] > :nth-child(2) > :nth-child(1) > :nth-child(1)').siblings()
        .should('have.length',4)
    })

    it('Search Function', () => {
        cy.get('.container > :nth-child(1) > .v-input > .v-input__control > .v-input__slot').type('file')
        cy.get('.v-expansion-panel-header').should('contain', 'file-')
    })
})