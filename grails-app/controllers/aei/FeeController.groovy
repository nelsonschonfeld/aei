package aei

import enums.FeeStatusEnum
import enums.Months
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(['ROLE_ADMIN', 'ROLE_SECRETARIA'])
class FeeController {

    def springSecurityService
    def exportService
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
            feeList = feeList.findAll { it.status?.value == params.status }
        }

        if(params?.f && params.f != "html"){
            List fields = ["identificationCode", "course", "student", "amount", "amountPaid", "amountFull", "status", "month", "year", "dateCreated"]
			Map labels = ["identificationCode": g.message(code: 'fee.identificationCode.label'), 
                            "course": g.message(code: 'fee.course.label'),
                            "student": g.message(code: 'fee.student.label'),
                            "amount": g.message(code: 'fee.amount.label'),
                            "amountPaid": g.message(code: 'fee.amountPaid.label'),
                            "amountFull": g.message(code: 'fee.amountFull.label'),
                            "status": g.message(code: 'fee.status.label'),
                            "month": g.message(code: 'fee.month.label'),
                            "year": g.message(code: 'fee.year.label'),
                            "dateCreated": g.message(code: 'fee.dateCreated.label')]

            response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=fee.${params.extension}")

            exportService.export(params.f, response.outputStream, feeList, fields, labels, [:], [:])
        }

        respond feeList, model: [feeCount: Fee.count()]
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

        //All params validations
        String errorMesagge = validate(params)
        if (errorMesagge) {
            flash.error = errorMesagge
            respond fee, view: 'create'
            return
        }

        def pdfData = []
        fee.updatedByUser = springSecurityService.currentUser.username

        for (String student : Eval.me(params.studentsInscriptos.toString())) {
            def newFee = fee.clone()
            try {
                def lastFee = Fee.last()
                def identificationCode = "10000"
                if (lastFee){
                    identificationCode = Integer.toString(lastFee.id + 1)
                }
                newFee.identificationCode = identificationCode
                def inscriptionObject = Inscription.get(params.inscriptionsSelect)
                newFee.inscription = inscriptionObject
                def studentObject = Person.findByDni(student)
                newFee.student = studentObject

                //caso de registramos solo la inscripción
                def isInscriptionWithoutFee = false
                if ((params.courseAmount == null || params.courseAmount == 0) && params.checkCourseInscriptionCost){
                    isInscriptionWithoutFee = true
                    newFee.amount = 0
                } else {
                    newFee.amount = Double.parseDouble(params.courseAmount)
                }
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
                            amountToPaid = amountToPaid + (prevFee.amountFirstExpiredDate - prevFee.amountPaid)
                            //actualizamos el estado de la cuota a Trasladado ya que se pasa a la próxima cuota
                            prevFee.status = FeeStatusEnum.Trasladado
                            prevFee.save flush: true
                        }
                    }
                }

                def totalToPaid = (newFee.amount + newFee.inscriptionCost + newFee.printCost + (newFee.testCost / 2))
                def totalToPaidWithDiscount = (newFee.amount * newFee.discountAmount) / 100
                newFee.amountFull = newFee.extraCost + amountToPaid + totalToPaid - totalToPaidWithDiscount

                //caso de registramos solo la inscripción
                def courseFirstDueCost = course.firstDueCost
                def courseSecondDueCost = course.secondDueCost
                if (isInscriptionWithoutFee) {
                    courseFirstDueCost = 0
                    courseSecondDueCost = 0
                }
                def totalToPaidFirst = (courseFirstDueCost + newFee.inscriptionCost + newFee.printCost + (newFee.testCost / 2))
                newFee.amountFirstExpiredDate = newFee.extraCost + amountToPaid + totalToPaidFirst

                def totalToPaidSecond = (courseSecondDueCost + newFee.inscriptionCost + newFee.printCost + (newFee.testCost / 2))
                newFee.amountSecondExpiredDate = newFee.extraCost + amountToPaid + totalToPaidSecond
                if (newFee.hasErrors()) {
                    transactionStatus.setRollbackOnly()
                    respond newFee.errors, view: 'create'
                    return
                }

                //Validation for duplicated fee in the same month and year
                def duplicateFee = Fee.findWhere(student: newFee.student, course: newFee.course, month: newFee.month, year: newFee.year)

                if (duplicateFee) {
                    flash.error = "La Cuota para el ALUMNO: ${student} y el MES: ${newFee.month} ya se genero anteriormente."
                    respond newFee, view: 'create'
                    return
                }

                pdfData.add(newFee)
                newFee.save flush: true

            } catch (Exception e) {
                transactionStatus.setRollbackOnly()
                flash.error = "No se pudo generar la cuota. Intente nuevamente."
                respond newFee, view: 'create'
                return
            }
        }

        flash.message = "La/s Cuota/s fueron generadas correctamente."
        //redirect action: "download", method: "GET"
        render( view: "/fee/download",  model: [pdf: pdfData])

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

        // if (fee.amountPaid == fee.amountFull) {
        //     fee.status = FeeStatusEnum.Pagado
        // } else {
        //     fee.status = FeeStatusEnum.Parcial
        // }

        // Actualizo valores con costos de reimpresión
        def coursePrintCost = Course.findById(fee.course).printCost
        fee.printCost = coursePrintCost + fee.printCost
        fee.amountFull = fee.amountFull + coursePrintCost
        fee.amountFirstExpiredDate = fee.amountFirstExpiredDate + coursePrintCost
        fee.amountSecondExpiredDate = fee.amountSecondExpiredDate + coursePrintCost

        fee.updatedByUser = springSecurityService.currentUser.username
        fee.save flush: true

        def pdfData = []
        pdfData.add(fee)

        flash.message = "La Cuota fue reimpresa correctamente."
        render( view: "/fee/download",  model: [pdf: pdfData])
        // request.withFormat {
        //     form multipartForm {
        //         flash.message = message(code: 'default.updated.message', args: [message(code: 'fee.label', default: 'Fee'), fee.id])
        //         redirect fee
        //     }
        //     '*' { respond fee, [status: OK] }
        // }
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

    /** Validations for fee params */

    String validate(def params) {

        String message = null

        //validacion Cursos
        if (params.inscriptionsSelect == null || params.inscriptionsSelect == "0" || params.inscriptionsSelect == 0) {
            message = "Debe seleccionar el Curso Inscripto"
            return message
        }
        //validacion alumnos
        if (params.studentsInscriptos == null || params.studentsInscriptos == [] || params.studentsInscriptos.size() == 0) {
            message = "Debe seleccionar el o los Alumnos"
            return message
        }
        //validacion cuota para cuando no posee costo de inscripcion
        if ((params.courseAmount == null || params.courseAmount == "" || params.courseAmount == 0) && (!params.checkCourseInscriptionCost || !params._checkCourseTestCost || !params._checkCourseTestCost)) {
            if (!params.checkCourseInscriptionCost) {
                message = "La Cuota no puede ser vacio o cero"
                return message
            }
        }

        /* Due dates validations */

        def today = new Date().format( 'yyyy-MM-dd' )
        def firstDate = params.firstExpiredDate.format( 'yyyy-MM-dd' )
        def secondDate = params.secondExpiredDate.format( 'yyyy-MM-dd' )

        if (firstDate <= today) {
            message = "La fecha del primer vencimiento debe ser mayor al día de hoy"
            return message
        }

        if (secondDate <= today) {
            message = "La fecha del segundo vencimiento debe ser mayor al día de hoy"
            return message
        }

        if (secondDate <= firstDate) {
            message = "La fecha del segundo vencimiento debe ser mayor a la fecha del primer vencimiento"
            return message
        }

        if (params.firstExpiredDate.getMonth() != Months.valueOf(params.month).ordinal() ||
                params.firstExpiredDate.format( 'yyyy' ) != params.courseYear) {
            message = "La fecha del primer vencimiento debe ser del mismo mes y año de la cuota que intenta generar"
            return message
        }

        if (params.secondExpiredDate.getMonth() != Months.valueOf(params.month).ordinal() ||
                params.secondExpiredDate.format( 'yyyy' ) != params.courseYear) {
            message = "La fecha del segundo vencimiento debe ser del mismo mes y año de la cuota que intenta generar"
            return message
        }

        return message
    }

    /*@Secured(['ROLE_ADMIN', 'ROLE_SECRETARIA'])
    def download() {
        println("nelson download")
        println(fee.amountPaid)
        println(params)
        def pdf = []
        def student = [:]
        student.id = 0001
        student.student = "Nelson Schonfeld"
        student.descBill = "Cuota Nov + 1/2 Dic 2018 - Segment VIII A"
        student.hours = "HORARIO DE COBRO: 14:00 a 20:30 - Lunes a Viernes"
        student.firstExpirationDate = "1er venc 12/11/2018: \$1500"
        student.secondExpirationDate = "1er venc 12/11/2018: \$1500"

        pdf.add(student)

        student = [:]
        student.id = 0002
        student.student = "Brian Ramseyer"
        student.descBill = "Cuota Nov + 1/2 Dic 2018 - Segment VIII A"
        student.hours = "HORARIO DE COBRO: 14:00 a 20:30 - Lunes a Viernes"
        student.firstExpirationDate = "1er venc 12/11/2018: \$1500"
        student.secondExpirationDate = "1er venc 12/11/2018: \$1500"

        pdf.add(student)

        student = [:]
        student.id = 0003
        student.student = "Ayelen Cian"
        student.descBill = "Cuota Nov + 1/2 Dic 2018 - Segment VIII A"
        student.hours = "HORARIO DE COBRO: 14:00 a 20:30 - Lunes a Viernes"
        student.firstExpirationDate = "1er venc 12/11/2018: \$1500"
        student.secondExpirationDate = "1er venc 12/11/2018: \$1500"

        pdf.add(student)

        respond("pdf": pdf)
    }*/

}