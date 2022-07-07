import 'cypress-file-upload';

let url = 'http://localhost:8080'
const adminEmail = 'admin@admin.com'
const password = 'password'

const userEmail ='cypress@test.com'
const projectName = 'cypressTest'

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

         // admin has to give access

         cy.get(':nth-child(7) > .v-btn > .v-btn__content').click()
         cy.get('.v-slide-group__content > :nth-child(3)').click()
         cy.get('.v-expansion-panel-header').click()
         cy.get('.v-expansion-panel-content__wrap > :nth-child(1)').then(($id) => {
   
             var fullText = $id.text();
             var pattern = /[0-9]+/g;
             var number = fullText.match(pattern);
           let id = number
           cy.wrap(id).as('id')
         })
   
         cy.get('@id').then((id) => {
           cy.get('.v-window-item--active > .pb-4 > .v-form > .v-card__text > .v-input > .v-input__control > .v-input__slot').type(id[0])
           cy.get('.v-window-item--active > .pb-4 > .v-form > :nth-child(2) > .v-input__control > .v-input__slot').type(userEmail)
           cy.intercept("**/api/AddUserToProject?*").as('AddUserToProject')
           cy.get(':nth-child(3) > .ml-11').click()
           cy.wait('@AddUserToProject').then((intercept) => {
             expect(intercept.response.statusCode).to.equal(200)
           })
           cy.get('.v-slide-group__content > :nth-child(5)').click()
           cy.get('.v-window-item--active > :nth-child(1) > .pb-4 > .v-form > .v-card__text > .v-input > .v-input__control > .v-input__slot').type(id[0])
           cy.get('.v-window-item--active > :nth-child(1) > .pb-4 > .v-form > .ml-11').click()
           cy.wait(2000)
           cy.get('.v-window-item--active > :nth-child(1) > .pb-4').should('contain', userEmail)
           })
   
   
           // logout
                   
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

    //logout and see if the other user has a notification

    it('Check out file', () => {
        cy.get('.v-expansion-panel-header').click()
       cy.get('[name*="1"]').click()
        cy.get('[name*="1"]').find('[name*="id"]')
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

      cy.get('[name*="1"]').find('[name*="id"]')
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
        cy.get('[name*="1"]').click()
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

    it('Check Other users access', () => {
        // logout
                
        cy.window().then((win) => {
            win.sessionStorage.clear()
          });
          
        cy.visit(url)
        
        // login as second user

      cy.get('input#input-38').type(userEmail)
      cy.get('input#input-41').type(password)
      cy.intercept("**/api/login?*").as('adminLogin')
      cy.get('.col-sm-3 > .v-btn > .v-btn__content').click()
      cy.wait(5000)
      cy.get('.text-center > .v-btn').click()
      cy.get('.v-list').should('contain', 'Newly added project!')
        // go to that user and check 2 notifications
        cy.intercept('**/api/deleteNotification?*').as('deleteNotification')
        cy.get('#list-item-129 > .v-btn > .v-btn__content > .v-icon').click()
        cy.get('.v-expansion-panel-header').click()
        cy.wait('@deleteNotification').then((intercept) => {
            expect(intercept.response.statusCode).to.equal(200)
        })
        // One main project and one sub project
        cy.get('[name*="1"]').should('contain','file')

    })

})