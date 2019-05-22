package aei

import enums.FeeStatusEnum
import enums.Months
import groovy.transform.AutoClone

@AutoClone
class Fee {
    String id
    Inscription inscription
    Person student
    Double amount
    Double inscriptionCost
    Double testCost
    Long printCost
    Double extraCost
    FeeStatusEnum status = FeeStatusEnum.Iniciado
    Months month
    String year
    Date firstExpiredDate
    Date secondExpiredDate
    Date dateCreated
    Date lastUpdated

    static constraints = {
        amount(nullable: true)
        inscription(nullable: true)
        student(nullable: true)
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
