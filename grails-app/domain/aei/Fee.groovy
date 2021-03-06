package aei

import enums.FeeStatusEnum
import enums.Months
import groovy.transform.AutoClone

@AutoClone
class Fee {
    Integer id
    String identificationCode
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
    Double amountFirstExpiredDate
    Date secondExpiredDate
    Double amountSecondExpiredDate
    String comment
    Date dateCreated
    Date lastUpdated
    String updatedByUser

    static constraints = {
        identificationCode(nullable: true)
        identificationCode(display: false)
        inscription(nullable: true)
        course(nullable: true)
        student(nullable: true)
        month(nullable: false)
        year(nullable: true)
        discountAmount(nullable: true)
        amount(nullable: true)
        amountPaid(nullable: true)
        extraCost(nullable: true)
        inscriptionCost(nullable: true)
        testCost(nullable: true)
        printCost(nullable: true)
        amountFull(nullable: true)
        amountFirstExpiredDate(nullable: true)
        firstExpiredDate(nullable: false)
        amountSecondExpiredDate(nullable: true)
        comment(nullable: true)
        secondExpiredDate(nullable: false)
        status(nullable: false)
        updatedByUser(display: false, nullable: true)
    }

    static mapping = {
        id(generator: 'org.hibernate.id.enhanced.SequenceStyleGenerator', params: [sequence_name: 'start_seq', initial_value: 10000])
    }

    String toString() {
        return id
    }
}
