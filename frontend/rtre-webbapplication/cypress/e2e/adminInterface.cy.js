
let url = 'http://localhost:8080'
import Chance from 'chance'
import 'cypress-file-upload';
const chance = new Chance()


const adminEmail = 'admin@admin.com'
const userEmail ='cypress@test.com'
const projectName = 'cypressTest'
const password = 'password'


describe('User and project management test', () => {

    it('Create a new user', () => {
      cy.window().then((win) => {
        win.sessionStorage.clear()
      });
      cy.visit(url)
      cy.get('[aria-selected="false"]').click()
      cy.get('#input-55').type('not')
      cy.get('#input-58').type('admin')
      cy.get('#input-61').type(userEmail)
      cy.get('#input-64').type(password)
      cy.get('#input-68').type(password)
      cy.intercept("**/api/register?*").as('register')
      cy.get('.v-window-item--active > .px-4 > .v-card__text > .v-form > .row > .d-flex > .v-btn').click()
      cy.wait('@register').then((intercept) => {
        expect(intercept.response.statusCode).to.equal(200)
      })
    })

    it('Sign in as admin user', () => {

      cy.window().then((win) => {
        win.sessionStorage.clear()
      });

      cy.visit(url)
      cy.url().should('include', 'Login')

      cy.get('input#input-38').type(adminEmail)
      cy.get('input#input-41').type(password)
      cy.intercept("**/api/login?*").as('adminLogin')
      cy.get('.col-sm-3 > .v-btn > .v-btn__content').click()

      cy.wait('@adminLogin').then((intercept) => {
        expect(intercept.response.statusCode).to.equal(200)
      })
      cy.get('.rounded-0 > .v-toolbar__content > .v-toolbar__title').should('contain', 'User Profile')
    })

    it('Create a Project', () => {
      
      cy.get(':nth-child(7) > .v-btn > .v-btn__content').click()
      cy.get('.v-slide-group__content').should('exist')
      cy.get('.v-card__text > .v-input > .v-input__control > .v-input__slot').type(projectName)
      cy.get('.v-select__selections').click()
      cy.get('.v-list-item__content').contains('Ifc4').click()
      cy.intercept("**/api/CreateProject?*").as('createProject')
      cy.get('.ml-11 > .v-btn__content').click()
      cy.wait('@createProject').then((intercept) => {
        expect(intercept.response.statusCode).to.equal(200)
      })
      cy.intercept("**/api/getProjectList?*").as('getProjectList')
      cy.get(':nth-child(4) > .v-btn').click()
      cy.wait('@getProjectList').then((intercept) => {
        expect(intercept.response.body.length).to.be.greaterThan(0)
      })
      //cy.wait(2000)
    //  cy.get('.v-expansion-panel-header').should('contain', projectName)
    })

    it('Add User to project the latest project, also check if users exist in the project after ', () =>{
      cy.intercept("**/api/getProjectList?*").as('getProjectList')
      cy.get(':nth-child(7) > .v-btn > .v-btn__content').click()
      cy.wait('@getProjectList', {timeout:20000}).then((intercept) => {
        expect(intercept.response.body.length).to.be.greaterThan(0)
        var number = intercept.response.body[0].oid
        let id = number
        console.log(id)
        cy.wrap(id).as('id')
      })
      cy.get('.v-slide-group__content > :nth-child(3)').click()

      cy.get('@id').then((id) => {
        cy.get('.v-window-item--active > .pb-4 > .v-form > .v-card__text > .v-input > .v-input__control > .v-input__slot').type(id)
        cy.get('.v-window-item--active > .pb-4 > .v-form > :nth-child(2) > .v-input__control > .v-input__slot').type(userEmail)
        cy.intercept("**/api/AddUserToProject?*").as('AddUserToProject')
        cy.get(':nth-child(3) > .ml-11').click()
        cy.wait('@AddUserToProject').then((intercept) => {
          expect(intercept.response.statusCode).to.equal(200)
        })
        cy.get('.v-slide-group__content > :nth-child(5)').click()
        cy.get('.v-window-item--active > :nth-child(1) > .pb-4 > .v-form > .v-card__text > .v-input > .v-input__control > .v-input__slot').type(id)
        cy.intercept("**/api/ViewUsers?*").as("viewUsers")
        cy.get('.v-window-item--active > :nth-child(1) > .pb-4 > .v-form > .ml-11').click()
        cy.wait('@viewUsers').then((intercept) => {
          expect(intercept.response.statusCode).to.equal(200)
        })
        cy.get('.v-window-item--active > :nth-child(1) > .pb-4').should('contain', userEmail)
      
      })
    })

    it('Login to second user to check if user has access', () => {
      cy.window().then((win) => {
        win.sessionStorage.clear()
      });
  
      cy.visit(url)
      cy.url().should('include', 'Login')
  
      cy.get('input#input-38').type(userEmail)
      cy.get('input#input-41').type(password)
      cy.intercept("**/api/getProjectList?*").as('getProjectList')
      cy.intercept("**/api/login?*").as('userLogin')
      cy.get('.col-sm-3 > .v-btn > .v-btn__content').click()
      cy.wait('@userLogin').then((intercept) => {
        expect(intercept.response.statusCode).to.equal(200)
      })
      cy.wait('@getProjectList').then((intercept) => {
        expect(intercept.response.body.length).to.be.greaterThan(0)
      cy.get('.rounded-0 > .v-toolbar__content > .v-toolbar__title').should('contain', 'User Profile')
      })
      cy.get('.v-expansion-panel-header').should('contain', projectName)
    })

    it('logout and gain access to admin interface for the next step', () => {

      cy.window().then((win) => {
        win.sessionStorage.clear()
      });
      cy.visit(url)
      cy.url().should('include', 'Login')
      cy.get('input#input-38').type(adminEmail)
      cy.get('input#input-41').type(password)
      cy.get('.col-sm-3 > .v-btn > .v-btn__content').click()
       cy.window().then((win) => {
        win.sessionStorage.clear()
      });
    })

    it('Remove user from latest project, also check if user has been removed from project', () =>{
      cy.intercept("**/api/getProjectList?*").as('getProjectList')
      cy.get(':nth-child(7) > .v-btn > .v-btn__content').click()
      cy.wait('@getProjectList').then((intercept) => {
        expect(intercept.response.body.length).to.be.greaterThan(0)
        var number = intercept.response.body[0].oid
        let id = number
        console.log(id)
        cy.wrap(id).as('id')
      })
      cy.get('.v-slide-group__content > :nth-child(4)').click()

      cy.get('@id').then((id) => {
        cy.get('.v-window-item--active > .pb-4 > .v-form > .v-card__text > .v-input > .v-input__control > .v-input__slot').type(id)
        cy.get('.v-window-item--active > .pb-4 > .v-form > :nth-child(2) > .v-input__control > .v-input__slot').type(userEmail)
        cy.intercept("**/api/RemoveUserFromProject?*").as('remove')
        cy.get(':nth-child(4) > .ml-11').click()
        cy.wait('@remove').then((intercept) => {
          expect(intercept.response.statusCode).to.equal(200)
        })
        cy.get('.v-slide-group__content > :nth-child(5)').click()
        cy.get('.v-window-item--active > :nth-child(1) > .pb-4 > .v-form > .v-card__text > .v-input > .v-input__control > .v-input__slot').type(id)
        cy.intercept("**/api/ViewUsers?*").as("viewUsers")
        cy.get('.v-window-item--active > :nth-child(1) > .pb-4 > .v-form > .ml-11').click()
        cy.wait('@viewUsers').then((intercept) => {
          expect(intercept.response.statusCode).to.equal(200)
        })
        cy.get('.v-window-item--active > :nth-child(1) > .pb-4').should('not.contain', userEmail)
      })
    })
    
    it('Login to second user to check if user has lost access', () => {
      cy.window().then((win) => {
        win.sessionStorage.clear()
      });
  
      cy.visit(url)
      cy.url().should('include', 'Login')
  
      cy.get('input#input-38').type(userEmail)
      cy.get('input#input-41').type(password)
      cy.intercept("**/api/login?*").as('userLogin')
      cy.intercept("**/api/getProjectList?*").as('getProjectList')
      cy.get('.col-sm-3 > .v-btn > .v-btn__content').click()
  
      cy.wait('@userLogin').then((intercept) => {
        expect(intercept.response.statusCode).to.equal(200)
      })
      cy.wait('@getProjectList').then((intercept) => {
        expect(intercept.response.body.length).to.equal(0)
      })
      cy.get('.rounded-0 > .v-toolbar__content > .v-toolbar__title').should('contain', 'User Profile')
      cy.get('.v-expansion-panel-header').should('not.exist')
      
    })

    //TESTING GIVING A USER ACCESS TO PROJECT // LOGIN TO ALT AND SEE IF IT WENT TROUGH

    // TESTING SEEING A LIST OF ALL USERS IN PROJECT

    // POST A IFC AND CHECK IF IT SSHOWS UP IN PROJECTS USING REFRESH BUTTON// LOGIN TO ALT AND SEE IF THEY ALSO GOT IT

    // DOWNLOAD A IFC FILE

    //MERGE IFC FILE

    //CHECK NOTIFICATIONS// FOR BOTH ACCOUNTS

    //DELETE NOTIFICATIONS // SEE IF THE NOTICE IS USER BASED

    // REMOVE USER FROM PROJECT

    let url = 'http://localhost:8080'
    const adminEmail = 'admin@admin.com'
    const password = 'password'
    
    const userEmail ='cypress@test.com'
    const projectName = 'cypressTest'
    //hell
    describe('File Transfer Test', () => {
    
        before(() => {
            
            cy.window().then((win) => {
                win.sessionStorage.clear()
              });
              
            cy.visit(url)
            
          cy.get('input#input-38').type(adminEmail)
          cy.get('input#input-41').type(password)
          cy.intercept("**/api/login?*").as('adminLogin')
          cy.intercept("**/api/getProjectList?*").as('getProjectList')
          cy.get('.col-sm-3 > .v-btn > .v-btn__content').click()
    
             // admin has to give access
    
             cy.get(':nth-child(7) > .v-btn > .v-btn__content').click()
             cy.get('.v-slide-group__content > :nth-child(3)').click()

             cy.wait('@getProjectList', {timeout:20000}).then((intercept) => {
              expect(intercept.response.body.length).to.be.greaterThan(0)
              var number = intercept.response.body[0].oid
              let id = number
              console.log(id)
              cy.wrap(id).as('id')
             })

             cy.get('@id').then((id) => {
               cy.get('.v-window-item--active > .pb-4 > .v-form > .v-card__text > .v-input > .v-input__control > .v-input__slot').type(id)
               cy.get('.v-window-item--active > .pb-4 > .v-form > :nth-child(2) > .v-input__control > .v-input__slot').type(userEmail)
               cy.intercept("**/api/AddUserToProject?*").as('AddUserToProject')
               cy.get(':nth-child(3) > .ml-11').click()
               cy.wait('@AddUserToProject').then((intercept) => {
                 expect(intercept.response.statusCode).to.equal(200)
               })
               cy.get('.v-slide-group__content > :nth-child(5)').click()
               cy.get('.v-window-item--active > :nth-child(1) > .pb-4 > .v-form > .v-card__text > .v-input > .v-input__control > .v-input__slot').type(id)
               cy.get('.v-window-item--active > :nth-child(1) > .pb-4 > .v-form > .ml-11').click()
               cy.wait(2000)
               cy.get('.v-window-item--active > :nth-child(1) > .pb-4').should('contain', userEmail)
               })
       
       
               // logout
    
        })
    
        it('Check in a file to project', () => {
    
                             
          cy.window().then((win) => {
            win.sessionStorage.clear()
          });
          
        cy.visit(url)
    
        cy.get('input#input-38').type(adminEmail)
        cy.get('input#input-41').type(password)
        cy.intercept("**/api/login?*").as('adminLogin')
        cy.intercept("**/api/getProjectList?*").as('getProjectList')
        cy.get('.col-sm-3 > .v-btn > .v-btn__content').click()
    
    
          cy.wait('@getProjectList', {timeout:20000}).then((intercept) => {
            expect(intercept.response.body.length).to.be.greaterThan(0)
            var number = intercept.response.body[0].oid
            let id = number
            console.log(id)
            cy.wrap(id).as('id')
    
            cy.get('@id').then((id) => {
              cy.get('[name="fileButtonCheckIn"]').attachFile('stud.ifc')
              cy.get('.shrink > .v-input__control > .v-input__slot').type(id)
              cy.get('.v-input--dense > .v-input__control > .v-input__slot').click()
              cy.get('.v-list-item__content').contains('Ifc4').click()
              cy.intercept('**/api/postIfcAsSubProject?*').as('postAProject')
              // cy.intercept("**/api/getAllNotification?*", {timeout:20000}).as('notification')
              cy.get('.ml-11').click()
              cy.wait('@postAProject').then((intercept) => {
                  expect(intercept.response.statusCode).to.equal(200)
                  cy.get('[name="refresh"]').click()
                    })
    
              // cy.wait('@notification').then((intercept) => {
              //   expect(intercept.response.body.length).to.equal(0)
              // })
                })
    
            })
      
        })
    
         
        // it('Check Notification', () => {
        //     cy.get('.text-center').click()
        //     cy.get('.v-list').should('contain','Newly added project!')
        //     cy.intercept('**/api/deleteNotification?*').as('deleteNotification')
        //     cy.get('[name="0"]').click()
        //     cy.wait('@deleteNotification').then((intercept) => {
        //         expect(intercept.response.statusCode).to.equal(200)
        //     })
        // })
    
        //logout and see if the other user has a notification
    
        it('Check out file', () => {
          cy.window().then((win) => {
            win.sessionStorage.clear()
          });
          
        cy.visit(url)
    
        cy.get('input#input-38').type(adminEmail)
        cy.get('input#input-41').type(password)
        cy.intercept("**/api/login?*").as('adminLogin')
        cy.intercept("**/api/getProjectList?*").as('getProjectList')
        cy.get('.col-sm-3 > .v-btn > .v-btn__content').click()
    
    
          cy.wait('@getProjectList', {timeout:20000}).then((intercept) => {
            expect(intercept.response.body.length).to.be.greaterThan(0)
            var number = intercept.response.body[1].oid
            let id = number
            console.log(id)
            cy.wrap(id).as('id')
    
    
            cy.get('.v-slide-group__content > :nth-child(3)').click()
            cy.get('.v-card__text > .v-form > .v-input > .v-input__control > .v-input__slot').type(id)
            cy.intercept('**/api/getIfc?*').as('getIfcFile')
            cy.get('.v-card__text > .v-form > .blue').click()
            cy.wait('@getIfcFile').then((intercept) => {
                expect(intercept.response.statusCode).to.equal(200)
                cy.wrap(intercept.response.body.length).should('be.gt',13000)
          })
        })
        })
    
        it('Merge file', () => {
          cy.window().then((win) => {
            win.sessionStorage.clear()
          });
          
        cy.visit(url)
    
        cy.get('input#input-38').type(adminEmail)
        cy.get('input#input-41').type(password)
        cy.intercept("**/api/login?*").as('adminLogin')
        cy.intercept("**/api/getProjectList?*").as('getProjectList')
        cy.get('.col-sm-3 > .v-btn > .v-btn__content').click()
    
    
          cy.wait('@getProjectList', {timeout:20000}).then((intercept) => {
            expect(intercept.response.body.length).to.be.greaterThan(0)
            var number = intercept.response.body[1].oid
            let id = number
        
        cy.get('.v-slide-group__content > :nth-child(4)').click()
        cy.get('.v-card__text > .v-form > .v-input--dense > .v-input__control > .v-input__slot').type(id)
      })
        cy.get('[name="mergeFileInput"').attachFile('stud.ifc')
        cy.intercept('**/api/merge?*').as('merge')
        cy.get('[name="mergeButton"]').click()
        cy.wait('@merge').then((intercept) => {
          expect(intercept.response.statusMessage).to.equal("Success")
          expect(intercept.response.statusCode).to.equal(200)
        })
        cy.get('[name="refresh"]').click()
        })
    
        it('Button check out', () => {
                                     
          cy.window().then((win) => {
            win.sessionStorage.clear()
          });
          
        cy.visit(url)
    
        cy.get('input#input-38').type(adminEmail)
        cy.get('input#input-41').type(password)
        cy.intercept("**/api/login?*").as('adminLogin')
        cy.intercept("**/api/getProjectList?*").as('getProjectList')
        cy.get('.col-sm-3 > .v-btn > .v-btn__content').click()
    
    
          cy.wait('@getProjectList', {timeout:20000}).then((intercept) => {
            expect(intercept.response.body.length).to.be.greaterThan(0)
            var number = intercept.response.body[0].oid
            let id = number
            console.log(id)
            cy.wrap(id).as('id')
          })
          
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
                                         
          cy.window().then((win) => {
            win.sessionStorage.clear()
          });
          
        cy.visit(url)
    
        cy.get('input#input-38').type(adminEmail)
        cy.get('input#input-41').type(password)
        cy.intercept("**/api/login?*").as('adminLogin')
        cy.intercept("**/api/getProjectList?*").as('getProjectList')
        cy.get('.col-sm-3 > .v-btn > .v-btn__content').click()
    
    
          cy.wait('@getProjectList', {timeout:20000}).then((intercept) => {
            expect(intercept.response.body.length).to.be.greaterThan(0)
            var number = intercept.response.body[0].oid
            let id = number
            console.log(id)
            cy.wrap(id).as('id')
          })
            cy.get('.v-expansion-panel-header').click()
            cy.get('[popout=""] > :nth-child(2) > :nth-child(1) > :nth-child(1)').siblings()
            .should('have.length',4)
        })
    
        it('Search Function', () => {
            cy.get('.container > :nth-child(1) > .v-input > .v-input__control > .v-input__slot').type('file')
            cy.get('.v-expansion-panel-header').should('contain', 'file-')
        })
    
    })
  
  })