package aei

import enums.FeeStatusEnum
import enums.Months

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
    Date firstExpiredDate
    Date secondExpiredDate
    Date dateCreated
    Date lastUpdated

    static constraints = {
        amount(nullable: false)
        extraCost(nullable: true)
        inscriptionCost(nullable: true)
        testCost(nullable: true)
        printCost(nullable: true)
        month(nullable: false)
        firstExpiredDate(nullable: false)
        secondExpiredDate(nullable: false)
        status(nullable: false)

        inscription(validator: {
            return true
        })

        student(validator: {
            return true
        })
    }

    static mapping = {
        id generator: 'assigned'
    }
}
