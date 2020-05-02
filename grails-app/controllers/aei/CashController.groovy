package aei

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(['ROLE_ADMIN', 'ROLE_SECRETARIA'])
class CashController {

    def springSecurityService
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {

        def user
        def cashList

        if (params.user) {
            user = User.findById(params.user)
            cashList = Cash.findAllWhere(user: user)
        } else {
            cashList = Cash.list()
        }

        if (params.dateFrom) {
            cashList = cashList.findAll { it.dateCreated >= params.dateFrom }
        }
        if (params.dateTo) {
            cashList = cashList.findAll { it.dateCreated <= params.dateTo.plus(1) }
        }

        respond cashList, model: [cashCount: Cash.count()]
    }

    def show(Cash cash) {
        respond cash
    }

    def findLastCash(Cash preCashDetails) {
        def listPaymentsToCash = []
        // Buscamos todos los pagos realizados posteriores al último cierre de caja
        if(preCashDetails) {
            listPaymentsToCash = Payment.findAllByDateCreatedBetween(preCashDetails.dateCreated, new Date())
        } else {
            listPaymentsToCash = Payment.findAll()
        }

        def paymentsToCashTotal = 0
        for (def payment : listPaymentsToCash) {
            paymentsToCashTotal = paymentsToCashTotal + payment.amountPaid
        }
        return paymentsToCashTotal
    }
    def create() {
        def preCashDetails = Cash.last(sort:'id')
        def paymentsToCashTotal = findLastCash(preCashDetails)

        if(preCashDetails) {
            respond new Cash(params), [model: [preDate: preCashDetails.dateCreated, preInitialAmount: preCashDetails.initalAmount, preCosts: preCashDetails.costs, preIncome: preCashDetails.income, preWithdraw: preCashDetails.withdraw, preTotal: preCashDetails.total, preComments: preCashDetails.comment, initialAmountNew: preCashDetails.total.abs(), income: paymentsToCashTotal, total: preCashDetails.total + paymentsToCashTotal]]
            return
        }
        // Monto del cierre de caja actual
        respond new Cash(params), [model: [initialAmountNew:0, income: paymentsToCashTotal, total: paymentsToCashTotal]]
        return
        }

    @Transactional
    def save(Cash cash) {
        if (cash == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        def messageError = ""
        if (!cash.income) {
            messageError = "No se han generado nuevos cobros de cuotas para poder generar el cierre de caja."
        }
        if (cash.total < 0) {
            messageError = "El total en el cierre de caja no puede ser negativo."
        }

        if (messageError != "") {
            flash.error = messageError
            def preCashDetails = Cash.last(sort:'id')
            def paymentsToCashTotal = findLastCash(preCashDetails)

            def initialAmount = Double.parseDouble(params.initalAmount)
            def costs = Double.parseDouble(params.costs).abs()
            def withdraw = Double.parseDouble(params.withdraw).abs()
            def total = paymentsToCashTotal

            if(preCashDetails) {
                render(view: "create", model: [cash: cash, preDate: preCashDetails.dateCreated, preInitialAmount: preCashDetails.initalAmount, preCosts: preCashDetails.costs, preIncome: preCashDetails.income, preWithdraw: preCashDetails.withdraw, preTotal: preCashDetails.total, preComments: preCashDetails.comment, initialAmountNew: preCashDetails.total, income: paymentsToCashTotal, total: initialAmount - costs - withdraw + total])
                return
            }
            // Monto del cierre de caja actual
            render(view: "create", model: [cash: cash, initialAmountNew: 0, income: paymentsToCashTotal, total: initialAmount - costs - withdraw + total])
            return
        }

        cash.user = springSecurityService.currentUser

        if (cash.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond cash.errors, view:'create'
            return
        }

        cash.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'cash.label', default: 'Cash'), cash.id])
                redirect cash
            }
            '*' { respond cash, [status: CREATED] }
        }
    }

    def edit(Cash cash) {
        respond cash
    }

    @Transactional
    def update(Cash cash) {
        if (cash == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (cash.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond cash.errors, view:'edit'
            return
        }

        cash.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'cash.label', default: 'Cash'), cash.id])
                redirect cash
            }
            '*'{ respond cash, [status: OK] }
        }
    }

    @Transactional
    def delete(Cash cash) {

        if (cash == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        cash.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'cash.label', default: 'Cash'), cash.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'cash.label', default: 'Cash'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
