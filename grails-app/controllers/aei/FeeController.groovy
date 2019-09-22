package aei

import enums.FeeStatusEnum
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
            if (params.query) {
                or {
                    and { ilike('name', "%${params.query}%") }
                    and { ilike('type', "%${params.query}%") }
                    and { ilike('year', "%${params.query}%") }
                    and { ilike('teacher', "%${params.query}%") }
                }
            }
        }
        respond courseList, view: 'create', model: [feeCount: Course.count()]
    }

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)

        def course
        def student
        def feeList

        if (!params.student && !params.course) {
            feeList = Fee.list()
        } else {

            if (params.course) {
                course = Course.findById(params.course)
            }

            if (params.student) {
                student = Person.findById(params.student)
            }

            if (course && student) {
                feeList = Fee.findAllWhere(course: course, student: student)
            } else if (student) {
                feeList = Fee.findAllWhere(student: student)
            } else {
                feeList = Fee.findAllWhere(course: course)
            }
        }

        if (params.status) {
            feeList = feeList.findAll {it.status?.value == params.status}
        }

        respond feeList, model:[feeCount: Fee.count()]
    }

    def show(Fee fee) {
        respond fee
    }

    def create() {
        respond new Fee(params)
    }

    def findStudentForCourse() {
        def inscriptionCourse = Inscription.read(params.idInscription).course
        def studentsList = Inscription.findAllWhere(course: inscriptionCourse).student
        render g.select(id: 'studentsInscriptos', name: 'studentsInscriptos', multiple: "multiple",
                from: studentsList, optionKey: 'dni'
        )
    }

    def findDiscountByStudent() {
        def inscriptionCourse = Inscription.read(params.idInscription).course
        def studentsList = Inscription.findAllWhere(course: inscriptionCourse).student
        def discountStudentsList = []
        studentsList.each { it ->
            def discount = Inscription.findWhere(student: it, course: inscriptionCourse).discountAmount
            if (discount > 0) {
                discountStudentsList << it.toString() + ", Descuento: " + discount + "%"
            }
        }
        //render ([discountStudentsList:discountStudentsList] as JSON)

        if (discountStudentsList.size() > 0) {
            render g.select(id: 'discountStudentsList', name: 'discountStudentsList', multiple: "multiple",
                    from: discountStudentsList
            )
        } else {
            render g.select(id: 'discountStudentsList', name: 'discountStudentsList', multiple: "multiple",
                    from: ["Los alumnos No tienen descuentos"]
            )
        }

    }

    def findAmountPaidByStudent() {
        def inscriptionCourse = Inscription.read(params.idInscription).course
        def studentsList = Inscription.findAllWhere(course: inscriptionCourse).student
        def owedStudentsList = []
        studentsList.each { it ->
            def allFee = Fee.findAllWhere(student: it, course: inscriptionCourse)
            def amountPaid = 0
            allFee.each { fee ->
                if (fee.status == FeeStatusEnum.Iniciado || fee.status == FeeStatusEnum.Parcial) {
                    amountPaid = amountPaid + (fee.amountFull - fee.amountPaid)
                }
            }
            if (amountPaid > 0) {
                owedStudentsList << it.toString() + ", Deuda: \$ " + amountPaid
            }
        }
        //render ([owedStudentsList:owedStudentsList] as JSON)
        if (owedStudentsList.size() > 0) {
            render g.select(id: 'owedStudentsList', name: 'owedStudentsList', multiple: "multiple",
                    from: owedStudentsList
            )
        } else {
            render g.select(id: 'owedStudentsList', name: 'owedStudentsList', multiple: "multiple",
                    from: ["Los alumnos No tienen deudas"]
            )
        }

    }

    def findCourseDetail() {
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
        render([courseAmount: courseDetail.amount, courseInscriptionCost: courseDetail.inscriptionCost, courseTestCost: courseDetail.testCost, coursePrintCost: courseDetail.printCost, courseSchedule: courseDetail.schedule, courseDays: days, courseStatus: courseDetail.status.toString(), courseTeacher: courseDetail.teacher, courseYear: courseDetail.year] as JSON)
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
        if (params.studentsInscriptos == null || params.studentsInscriptos == [] || params.studentsInscriptos.size() == 0) {
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
                newFee.id = params.inscriptionsSelect + ' ' + student + ' ' + newFee.month
                double random =  Math.random()
                String doubleAsString = String.valueOf(random);
                int indexOfDecimal = doubleAsString.indexOf(".");
                def identificationCode = doubleAsString.substring(indexOfDecimal + 1).toBigInteger()
                newFee.identificationCode = identificationCode
                def inscriptionObject = Inscription.get(params.inscriptionsSelect)
                newFee.inscription = inscriptionObject
                def studentObject = Person.findByDni(student)
                newFee.student = studentObject
                newFee.amount = Double.parseDouble(params.courseAmount)
                newFee.inscriptionCost = params.checkCourseInscriptionCost ? Double.parseDouble(params.courseInscriptionCost) : 0
                newFee.testCost = params.checkCourseTestCost ? Double.parseDouble(params.courseTestCost) : 0
                newFee.printCost = params.checkCoursePrintCost ? Double.parseDouble(params.coursePrintCost) : 0
                newFee.year = params.courseYear
                def course = Course.read(inscriptionObject.course.id)
                newFee.course = inscriptionObject.course
                newFee.discountAmount = Inscription.findWhere(student: studentObject, course: inscriptionObject.course).discountAmount

                //busco las deudas
                def studentsList = Inscription.findAllWhere(course: inscriptionObject.course, student: studentObject).student
                def amountToPaid = 0
                studentsList.each { it ->
                    def allFee = Fee.findAllWhere(student: it, course: inscriptionObject.course)
                    allFee.each { prevFee ->
                        if (prevFee.status == FeeStatusEnum.Iniciado || prevFee.status == FeeStatusEnum.Parcial) {
                            //buscamos el monto adeudado de las cuotas pasadas
                            amountToPaid = amountToPaid + (prevFee.amountFull - prevFee.amountPaid)
                            //actualizamos el estado de la cuota a Trasladado ya que se pasa a la próxima cuota
                            prevFee.status = FeeStatusEnum.Trasladado
                            prevFee.save flush: true
                        }
                    }
                }

                def totalToPaid = (newFee.amount + newFee.inscriptionCost + newFee.printCost + (newFee.testCost / 2))
                def totalToPaidWithDiscount = (totalToPaid * newFee.discountAmount) / 100
                newFee.amountFull = newFee.extraCost + amountToPaid + totalToPaid - totalToPaidWithDiscount

                def totalToPaidFirst = (course.firstDueCost + newFee.inscriptionCost + newFee.printCost + (newFee.testCost / 2))
                def totalToPaidWithDiscountFirst = (totalToPaidFirst * newFee.discountAmount) / 100
                newFee.amountFirstExpiredDate = newFee.extraCost + amountToPaid + totalToPaidFirst - totalToPaidWithDiscountFirst

                def totalToPaidSecond = (course.secondDueCost + newFee.inscriptionCost + newFee.printCost + (newFee.testCost / 2))
                def totalToPaidWithDiscountSecond = (totalToPaidSecond * newFee.discountAmount) / 100
                newFee.amountSecondExpiredDate = newFee.extraCost + amountToPaid + totalToPaidSecond - totalToPaidWithDiscountSecond

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
        redirect action: "index", method: "GET"

//        request.withFormat {
//            form multipartForm {
//                flash.message = message(code: 'default.created.message', args: [message(code: 'fee.label', default: 'Fee'), fee.id])
//                redirect fee
//            }
//            '*' { respond fee, [status: CREATED] }
//        }
    }

    @Secured(['ROLE_ADMIN'])
    def edit(Fee fee) {
        respond fee
    }

    @Secured(['ROLE_ADMIN'])
    @Transactional
    def update(Fee fee) {
        if (fee == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (fee.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond fee.errors, view: 'edit'
            return
        }

        if (fee.amountPaid == fee.amountFull) {
            fee.status = FeeStatusEnum.Pagado
        } else {
            fee.status = FeeStatusEnum.Parcial
        }
        fee.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'fee.label', default: 'Fee'), fee.id])
                redirect fee
            }
            '*' { respond fee, [status: OK] }
        }
    }

    @Secured(['ROLE_ADMIN'])
    @Transactional
    def delete(Fee fee) {

        if (fee == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        fee.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'fee.label', default: 'Fee'), fee.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'fee.label', default: 'Fee'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}