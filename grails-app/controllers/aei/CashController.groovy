package aei

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(['ROLE_ADMIN', 'ROLE_SECRETARIA'])
class CashController {

    def springSecurityService
    def exportService
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

        if (params.dateFrom_day && params.dateFrom_month && params.dateFrom_year) {
            def dateFrom = new GregorianCalendar(params.dateFrom_year.toInteger(), params.dateFrom_month.toInteger() - 1, params.dateFrom_day.toInteger(),0,0,0).time
            cashList = cashList.findAll { it.dateCreated >= dateFrom }
        }
        
        if (params.dateTo_day && params.dateTo_month && params.dateTo_year) {
            def dateTo = new GregorianCalendar(params.dateTo_year.toInteger(), params.dateTo_month.toInteger() - 1, params.dateTo_day.toInteger(),23,59,59).time
            cashList = cashList.findAll { it.dateCreated <= dateTo }
        }

        if(params?.f && params.f != "html"){
            List fields = ["dateCreated", "initalAmount", "costs", "withdraw", "income", "total", "comment", "user"]
			Map labels = ["dateCreated": g.message(code: 'cash.dateCreated.label'), 
                            "initalAmount": g.message(code: 'cash.initalAmount.label'),
                            "costs": g.message(code: 'cash.costs.label'),
                            "withdraw": g.message(code: 'cash.withdraw.label'),
                            "income": g.message(code: 'cash.income.label'),
                            "total": g.message(code: 'cash.total.label'),
                            "comment": g.message(code: 'cash.comment.label'),
                            "user": g.message(code: 'cash.user.label')]

            response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=cash.${params.extension}")

            exportService.export(params.f, response.outputStream, cashList, fields, labels, [:], [:])
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
        respond new Cash(params), [model: [initialAmountNew:0 , income: paymentsToCashTotal]]
        }

    @Transactional
    def save(Cash cash) {
        if (cash == null) {
            transactionStatus.setRollbackOnly()
            notFound()
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
