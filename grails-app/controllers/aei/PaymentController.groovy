package aei

import enums.FeeStatusEnum
import enums.PaymentStatusEnum
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

import java.text.SimpleDateFormat

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(['ROLE_ADMIN', 'ROLE_SECRETARIA'])
class PaymentController {

    def springSecurityService
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def findFeeByIdentificationCode() {

        def feeList = Fee.createCriteria().list(params) {
            if (params.query) {
                or {
                    and { ilike("identificationCode", "%${params.query}%") }
                   /* and { ilike("status", FeeStatusEnum.Iniciado) }
                    and { ilike("status", FeeStatusEnum.Parcial) }*/
                }
            }
            maxResults(50)
        }

        render g.select(id: 'selectIdentificationCode', name: 'selectIdentificationCode', optionKey: "id", optionValue: "identificationCode", from: (feeList as ArrayList))
    }

    def findFeeDetails() {
        def feeDetails = Fee.read(params.idFee)
        def today = new Date().format( 'yyyy-MM-dd' )
        def firsExp = feeDetails.firstExpiredDate.format('yyyy-MM-dd')
        def secondExp = feeDetails.secondExpiredDate.format('yyyy-MM-dd')
        def fullAmount = feeDetails.amountFull
        if (today > firsExp && today <= secondExp) {
            fullAmount = feeDetails.amountFirstExpiredDate
        }
        if (today > secondExp) {
            fullAmount = feeDetails.amountSecondExpiredDate
        }
        feeDetails.firstExpiredDate
        render([course: feeDetails.course.name, student: feeDetails.student.surname + " " + feeDetails.student.name, year: feeDetails.year, month: feeDetails.month.toString(), feeAmount: feeDetails.amount, feeAmountPaid: feeDetails.amountPaid, feeAmountFull: fullAmount, discountAmount: feeDetails.discountAmount, inscriptionCost: feeDetails.inscriptionCost, testCost: feeDetails.testCost, printCost: feeDetails.printCost, extraCost: feeDetails.extraCost, firstExpiredDate: feeDetails.firstExpiredDate.format('yyyy-MM-dd hh:mm:SS'), amountFirstExpiredDate: feeDetails.amountFirstExpiredDate, secondExpiredDate: feeDetails.secondExpiredDate.format('yyyy-MM-dd hh:mm:SS'), amountSecondExpiredDate: feeDetails.amountSecondExpiredDate, status: feeDetails.status.toString()] as JSON)
    }

    def index(Integer max) {

        params.max = Math.min(max ?: 10, 100)
        respond Payment.list(params), model: [paymentCount: Payment.count()]
    }

    def show(Payment payment) {
        respond payment
    }

    def create() {
        respond new Payment(params)
    }

    @Transactional
    def save(Payment payment) {
        if (payment == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        //validaciones de params
        if (!params.selectIdentificationCode) {
            flash.error = "Debe Completar los campos requeridos"
            respond payment, view: 'create'
            return
        }

        try {
            Fee fee = Fee.get(params.selectIdentificationCode)
            // validamos si la cuota esta pagada
            if (fee.status  == FeeStatusEnum.Trasladado) {
                flash.error = "La cuota no se encuentra disponible para pagar, ya fue trasladada a una cuota posterior."
                respond payment, view: 'create'
                return
            }
            if ((fee.amountFull == fee.amountPaid) || (fee.status == FeeStatusEnum.Pagado)) {
                flash.error = "La cuota ya fue cancelada."
                respond payment, view: 'create'
                return
            }
            payment.student = fee.student
            payment.course = fee.course
            payment.inscription = fee.inscription
            payment.feeCode = fee.identificationCode
            payment.fee = fee
            //buscar lo que falta y cancelar cuotas atrasadas
            payment.amountDelivered =  Double.parseDouble(params.amountPaid)
            Double vuelto = 0
            if (params.valueAmountReturned) {
                vuelto = Double.parseDouble(params.valueAmountReturned)
                payment.amountReturned = vuelto
            }
            payment.amountPaid =  Double.parseDouble(params.amountPaid) - vuelto

            //calculamos el monto a pagar en base a las fechas de vencimiento
            def today = new Date().format( 'yyyy-MM-dd' )
            def firsExp = fee.firstExpiredDate.format('yyyy-MM-dd')
            def secondExp = fee.secondExpiredDate.format('yyyy-MM-dd')
            def fullAmount = fee.amountFull
            if (today > firsExp && today <= secondExp) {
                fullAmount = fee.amountFirstExpiredDate
            }
            if (today > secondExp) {
                fullAmount = fee.amountSecondExpiredDate
            }
            if (Double.parseDouble(params.amountPaid) < fullAmount) {
                payment.status = PaymentStatusEnum.Parcial
            } else {
                payment.status = PaymentStatusEnum.Total
            }

            if (payment.hasErrors()) {
                transactionStatus.setRollbackOnly()
                respond payment.errors, view: 'create'
                return
            }

            payment.updatedByUser = springSecurityService.currentUser.username
            payment.save flush: true

            //actualizamos el fee
            if (fullAmount == (fee.amountPaid + payment.amountPaid)) {
                fee.status = FeeStatusEnum.Pagado
            } else {
                fee.status = FeeStatusEnum.Parcial
            }
            fee.amountPaid = fee.amountPaid + payment.amountPaid
            fee.save flush: true
        } catch (Exception e) {
            println(e.getMessage())
            transactionStatus.setRollbackOnly()
            flash.error = "Error al generar el pago"
            respond payment, view: 'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'payment.label', default: 'Payment'), payment.id])
                redirect payment
            }
            '*' { respond payment, [status: CREATED] }
        }
    }

    @Secured(['ROLE_ADMIN'])
    def edit(Payment payment) {
        respond payment
    }

    @Secured(['ROLE_ADMIN'])
    @Transactional
    def update(Payment payment) {
        if (payment == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (payment.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond payment.errors, view: 'edit'
            return
        }

        payment.updatedByUser = springSecurityService.currentUser.username
        payment.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'payment.label', default: 'Payment'), payment.id])
                redirect payment
            }
            '*' { respond payment, [status: OK] }
        }
    }

    @Secured(['ROLE_ADMIN'])
    @Transactional
    def delete(Payment payment) {

        if (payment == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        payment.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'payment.label', default: 'Payment'), payment.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'payment.label', default: 'Payment'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
