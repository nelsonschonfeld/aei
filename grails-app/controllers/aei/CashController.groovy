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

    def create() {
        def preCashDetails = Cash.last(sort:'id')
        def listPaymentsToCash = []

        // Buscamos todos los pagos realizados posteriores al último cierre de caja
        if(preCashDetails) {
            listPaymentsToCash = Payment.findAllByDateCreatedBetween(preCashDetails.dateCreated, new Date())
        } else {
            listPaymentsToCash = Payment.findAll()
        }

        def paymentsToCashTotal = 0
        for (def payment : listPaymentsToCash) {
            println(payment.amountPaid)
            paymentsToCashTotal = paymentsToCashTotal + payment.amountPaid
        }

        if(preCashDetails) {
            respond new Cash(params), [model: [preDate: preCashDetails.dateCreated, preInitialAmount: preCashDetails.initalAmount, preCosts: preCashDetails.costs, preIncome: preCashDetails.income, preWithdraw: preCashDetails.withdraw, preTotal: preCashDetails.total, preComments: preCashDetails.comment, initialAmountNew: preCashDetails.total, income: paymentsToCashTotal]]
        }
        // Monto del cierre de caja actual
        respond new Cash(params), [model: [initialAmount:0 , income: paymentsToCashTotal]]
        }

    @Transactional
    def save(Cash cash) {
        if (cash == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        cash.user = springSecurityService.currentUser.username

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
