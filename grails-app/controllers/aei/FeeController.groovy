package aei

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(['ROLE_ADMIN', 'ROLE_SECRETARIA'])
class FeeController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def getInscription(Integer max) {
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
        respond courseList, view:'create', model:[feeCount: Course.count()]
    }

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Fee.list(params), model:[feeCount: Fee.count()]
    }

    def show(Fee fee) {
        respond fee
    }

    def create() {
        respond new Fee(params)
    }

    def findStudentForCourse(){
        def inscriptionCourse = Inscription.read(params.idInscription).course
        def studentsList = Inscription.findAllWhere(course: inscriptionCourse).student
        render g.select(id:'studentsInscriptos', name:'studentsInscriptos', multiple:"multiple",
                from:studentsList, optionKey:'dni'
        )
    }

    def findCourseDetail(){
        def inscriptionCourse = Inscription.read(params.idInscrip).course
        def courseDetail = Course.get(inscriptionCourse.id)
        List<String> stringList = new ArrayList<String>();
        !courseDetail.monday ?: stringList.add("Lunes")
        !courseDetail.tuesday ?: stringList.add("Martes")
        !courseDetail.wednesday ?: stringList.add("Miercoles")
        !courseDetail.thursday ?: stringList.add("Jueves")
        !courseDetail.friday ?: stringList.add("Viernes")
        !courseDetail.saturday ?: stringList.add("Sabado")
        def days = stringList.join(" - ")
        render ([courseAmount:courseDetail.amount, courseTestCost:courseDetail.testCost, coursePrintCost:courseDetail.printCost, courseSchedule:courseDetail.schedule, courseDays: days, courseStatus:courseDetail.status.toString()] as JSON)
    }

    @Transactional
    def save(Fee fee) {
        if (fee == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }
        println(params.inscriptionsSelect)
        println(params.studentsInscriptos)
        println(fee.status)
        println(fee.month)
        println(fee.firstExpiredDate)
        println(fee.secondExpiredDate)
        println(getParams())

        //for (String student : Eval.me(params.studentsInscriptos.toString())) {
            try {
                println("nels save")
                println(params.inscriptionsSelect)
                println(params.studentsInscriptos)
                fee.id = params.inscriptionsSelect + '_' + params.studentsInscriptos + '_' + fee.month
                println(Inscription.get(params.inscriptionsSelect))
                fee.inscription = Inscription.get(params.inscriptionsSelect)
                println(Person.findByDni(params.studentsInscriptos))
                fee.student = Person.findByDni(params.studentsInscriptos)

                if (fee.hasErrors()) {
                    transactionStatus.setRollbackOnly()
                    respond fee.errors, view:'create'
                    return
                }

                fee.save flush: true
            } catch (Exception e) {
                flash.error = "La cuota para del ESTUDIANTE: ${student} y MES: ${fee.month} ya se registro en el correspondiente Curso."
                respond fee, view: 'create'
                return
            }
        //}

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'fee.label', default: 'Fee'), fee.id])
                redirect fee
            }
            '*' { respond fee, [status: CREATED] }
        }
    }

    def edit(Fee fee) {
        respond fee
    }

    @Transactional
    def update(Fee fee) {
        if (fee == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (fee.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond fee.errors, view:'edit'
            return
        }

        fee.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'fee.label', default: 'Fee'), fee.id])
                redirect fee
            }
            '*'{ respond fee, [status: OK] }
        }
    }

    @Transactional
    def delete(Fee fee) {

        if (fee == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        fee.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'fee.label', default: 'Fee'), fee.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'fee.label', default: 'Fee'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}