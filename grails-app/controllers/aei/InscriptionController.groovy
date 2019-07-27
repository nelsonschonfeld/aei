package aei

import grails.converters.JSON
import org.springframework.security.access.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(['ROLE_ADMIN', 'ROLE_SECRETARIA'])
class InscriptionController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 20, 100)

        def course
        def student
        def inscripcionList

        if (!params.student && !params.course) {
            inscripcionList = Inscription.list()
        } else {

            if (params.course) {
                course = Course.findById(params.course)
            }

            if (params.student) {
                student = Person.findById(params.student)
            }

            if (course && student) {
                inscripcionList = Inscription.findAllWhere(course: course, student: student)
            } else if (student) {
                inscripcionList = Inscription.findAllWhere(student: student)
            } else {
                inscripcionList = Inscription.findAllWhere(course: course)
            }
        }

        respond inscripcionList, model:[inscriptionCount: Inscription.count()]
    }

    def show(Inscription inscription) {
        respond inscription
    }

    def create() {
        respond new Inscription(params)
    }

    @Transactional
    def save(Inscription inscription) {

        if (!params.students){
            flash.error = "Debes seleccionar un alumno"
            respond inscription, view:'create'
            return
        }

        if (inscription == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (!inscription?.course){
            flash.error = "Debes seleccionar un curso"
            respond inscription, view:'create'
            return
        }

        Person person = Person.findByDni(params.students)
        if (person) {
            inscription.student = person
        } else {
            flash.error = "El Alumno no existe"
            respond inscription, view:'create'
            return
        }


        if (inscription.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond inscription.errors, view:'create'
            return
        }

        try {
            inscription.id = inscription.course.id + "_" + inscription.student.toString()
            inscription.save flush:true
        }catch (Exception e){
            transactionStatus.setRollbackOnly()
            flash.error = "El alumno ya está inscripto en este curso."
            respond inscription, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'inscription.label', default: 'Inscription'), inscription.id])
                redirect inscription
            }
            '*' { respond inscription, [status: CREATED] }
        }
    }

    def edit(Inscription inscription) {
        respond inscription
    }

    @Transactional
    def update(Inscription inscription) {
        if (inscription == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (inscription.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond inscription.errors, view:'edit'
            return
        }

        inscription.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'inscription.label', default: 'Inscription'), inscription.id])
                redirect inscription
            }
            '*'{ respond inscription, [status: OK] }
        }
    }

    @Transactional
    @Secured(['ROLE_ADMIN'])
    def delete(Inscription inscription) {

        if (inscription == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        inscription.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'inscription.label', default: 'Inscription'), inscription.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    def findPerson(String dni) {
        def c = Person.createCriteria()
        def personList = c.list { like("dni", "%${dni}%") }
        render personList.getAt(0)  as JSON
    }

    def findPersons() {

        def personList = Person.createCriteria().list (params) {
            if ( params.query ) {
                or {
                    and {ilike("name", "%${params.query}%")}
                    and {ilike("surname", "%${params.query}%")}
                    and {ilike("dni", "%${params.query}%")}
                }
            }
            maxResults(50)
        }

        render g.select(id:'students', name:'students', optionKey:"dni", from:(personList as ArrayList))
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'inscription.label', default: 'Inscription'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
