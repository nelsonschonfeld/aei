package aei

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(['ROLE_ADMIN', 'ROLE_SECRETARIA'])
class PersonController {

    def springSecurityService
    def exportService
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 20, 100)
        respond Person.list(params), model:[personCount: Person.count()]

        def personList = Person.createCriteria().list (params) {
            if ( params.query ) {
                or {
                    and {ilike("name", "%${params.query}%")}
                    and {ilike("surname", "%${params.query}%")}
                    and {ilike("dni", "%${params.query}%")}
                }
            }
        }

        if(params?.f && params.f != "html"){
            List fields = ["dni", "name", "surname", "email", "address", "cellphone", "telephone"]
			Map labels = ["dni": g.message(code: 'person.dni.label'), 
                            "name": g.message(code: 'person.name.label'),
                            "surname": g.message(code: 'person.surname.label'),
                            "email": g.message(code: 'person.email.label'),
                            "address": g.message(code: 'person.address.label'),
                            "cellphone": g.message(code: 'person.cellphone.label'),
                            "telephone": g.message(code: 'person.telephone.label')]

            response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=people.${params.extension}")

            exportService.export(params.f, response.outputStream, personList, fields, labels, [:], [:])
        }

        respond personList, model:[personCount: Person.count()]
    }

    def show(Person person) {
        respond person
    }

    def create() {
        respond new Person(params)
    }

    @Transactional
    def save(Person person) {
        if (person == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (person.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond person.errors, view:'create'
            return
        }
        person.updatedByUser = springSecurityService.currentUser.username

        person.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message.a', args: [message(code: 'person.label', default: 'Person'), person.id])
                redirect person
            }
            '*' { respond person, [status: CREATED] }
        }
    }

    def edit(Person person) {
        respond person
    }

    @Transactional
    def update(Person person) {
        if (person == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (person.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond person.errors, view:'edit'
            return
        }

        person.updatedByUser = springSecurityService.currentUser.username
        person.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message.a', args: [message(code: 'person.label', default: 'Person'), person.id])
                redirect person
            }
            '*'{ respond person, [status: OK] }
        }
    }

    @Transactional
    @Secured(['ROLE_ADMIN' ])
    def delete(Person person) {

        if (person == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        person.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message.a', args: [message(code: 'person.label', default: 'Person'), person.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
