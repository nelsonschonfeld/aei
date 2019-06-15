package aei

import enums.FeeStatusEnum
import enums.Months
import groovy.transform.AutoClone

@AutoClone
class Fee {
    String id
    Inscription inscription
    Course course
    Person student
    Double amount
    Double discountAmount = 0
    Double amountPaid = 0
    Double amountFull
    Double inscriptionCost = 0
    Double testCost = 0
    Double printCost = 0
    Double extraCost = 0
    FeeStatusEnum status = FeeStatusEnum.Iniciado
    Months month
    String year
    Date firstExpiredDate
    Date secondExpiredDate
    Date dateCreated
    Date lastUpdated

    static constraints = {
        amount(nullable: true)
        discountAmount(nullable: true)
        amountPaid(nullable: true)
        amountFull(nullable: true)
        inscription(nullable: true)
        student(nullable: true)
        course(nullable: true)
        extraCost(nullable: true)
        inscriptionCost(nullable: true)
        testCost(nullable: true)
        printCost(nullable: true)
        month(nullable: false)
        firstExpiredDate(nullable: false)
        secondExpiredDate(nullable: false)
        status(nullable: false)
        year(nullable: true)
    }

    static mapping = {
        id generator: 'assigned'
    }
}
