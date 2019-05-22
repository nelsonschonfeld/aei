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
        render ([courseAmount:courseDetail.amount, courseInscriptionCost:courseDetail.inscriptionCost, courseTestCost:courseDetail.testCost, coursePrintCost:courseDetail.printCost, courseSchedule:courseDetail.schedule, courseDays: days, courseStatus:courseDetail.status.toString(), courseTeacher:courseDetail.teacher, courseYear:courseDetail.year] as JSON)
    }

    @Transactional
    def save(Fee fee) {
        if (fee == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }
        //validacion Cursos
        if (params.inscriptionsSelect == null || params.inscriptionsSelect == "0" || params.inscriptionsSelect == 0) {
            flash.error = "Debe seleccionar el Curso Inscripto."
            respond fee, view: 'create'
            return
        }
        //validacion alumnos
        if (params.studentsInscriptos == null || params.inscriptionsSelect == [] || params.inscriptionsSelect.size() == 0) {
            flash.error = "Debe seleccionar el o los Alumnos"
            respond fee, view: 'create'
            return
        }
        //validacion cuota
        if (params.courseAmount == null || params.courseAmount == "" || params.courseAmount == "0") {
            flash.error = "La Cuota no puede ser vacio o cero"
            respond fee, view: 'create'
            return
        }

        for (String student : Eval.me(params.studentsInscriptos.toString())) {
            def newFee = fee.clone()
            try {
                newFee.id = params.inscriptionsSelect + '_' + student + '_' + newFee.month
                newFee.inscription = Inscription.get(params.inscriptionsSelect)
                newFee.student = Person.findByDni(student)
                newFee.amount = Double.parseDouble(params.courseAmount)
                newFee.inscriptionCost = params.checkCourseInscriptionCost ? Double.parseDouble(params.courseInscriptionCost) : null
                newFee.testCost = params.checkCourseTestCost ? Double.parseDouble(params.courseTestCost) : null
                newFee.printCost = params.checkCoursePrintCost ? Double.parseDouble(params.coursePrintCost) : null
                newFee.year = params.courseYear

                if (newFee.hasErrors()) {
                    transactionStatus.setRollbackOnly()
                    respond newFee.errors, view: 'create'
                    return
                }

                newFee.save flush: true
            } catch (Exception e) {
                transactionStatus.setRollbackOnly()
                flash.error = "La Cuota para el ALUMNO: ${student} y el MES: ${newFee.month} ya se genero anteriormente."
                respond newFee, view: 'create'
                return
            }
        }

        flash.message = "La/s Cuota/s fueron generadas correctamente."
        respond new Fee(params), view: 'create'

//        request.withFormat {
//            form multipartForm {
//                flash.message = message(code: 'default.created.message', args: [message(code: 'fee.label', default: 'Fee'), fee.id])
//                redirect fee
//            }
//            '*' { respond fee, [status: CREATED] }
//        }
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