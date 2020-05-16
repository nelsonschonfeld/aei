package aei

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(['ROLE_ADMIN', 'ROLE_SECRETARIA'])
class CourseController {
    def springSecurityService
    def exportService
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 20, 100)
        def courseList = Course.createCriteria().list(params) {
            if(params.query){
                or {
                    and {ilike('name', "%${params.query}%")}
                    and {ilike('type', "%${params.query}%")}
                    and {ilike('year', "%${params.query}%")}
                    and {ilike('teacher', "%${params.query}%")}
                }
            }
        }

        if(params?.f && params.f != "html"){
            List fields = ["name", "type", "year", "teacher", "amount", "schedule", "status"]
			Map labels = ["name": g.message(code: 'course.name.label'), 
                            "type": g.message(code: 'course.type.label'),
                            "year": g.message(code: 'course.year.label'),
                            "teacher": g.message(code: 'course.teacher.label'),
                            "amount": g.message(code: 'course.amount.label'),
                            "schedule": g.message(code: 'course.schedule.label'),
                            "status": g.message(code: 'course.status.label')]

            response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=course.${params.extension}")

            exportService.export(params.f, response.outputStream, courseList, fields, labels, [:], [:])
        }

        respond courseList, model:[courseCount: Course.count()]
    }

    def show(Course course) {
        respond course
    }

    def create() {
        respond new Course(params)
    }

    @Transactional
    def save(Course course) {
        if (course == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (course.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond course.errors, view:'create'
            return
        }

        try {
            course.id = course.name +'_'+ course.type +'_'+ course.year
            course.updatedByUser = springSecurityService.currentUser.username
            course.save flush:true
        }catch (Exception e){
            flash.error = "El course con la combinación del NOMBRE, TIPO y AÑO ya se registró. Verifícalo."
            respond course, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'course.label', default: 'Course'), course.id])
                redirect course
            }
            '*' { respond course, [status: CREATED] }
        }
    }

    def edit(Course course) {
        respond course
    }

    @Transactional
    def update(Course course) {
        if (course == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (course.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond course.errors, view:'edit'
            return
        }

        course.updatedByUser = springSecurityService.currentUser.username
        course.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'course.label', default: 'Course'), course.id])
                redirect course
            }
            '*'{ respond course, [status: OK] }
        }
    }

    @Transactional
    @Secured(['ROLE_ADMIN'])
    def delete(Course course) {

        if (course == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        course.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'course.label', default: 'Course'), course.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'course.label', default: 'Course'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
