package aei

import enums.PaymentStatusEnum

class Payment {
    Person student
    Course course
    Inscription inscription
    String feeCode
    Fee fee
    Double amountDelivered = 0
    Double amountPaid = 0
    Double amountReturned = 0
    PaymentStatusEnum status
    Date dateCreated
    Date lastUpdated
    String updatedByUser

    static constraints = {
        feeCode nullable:true
        fee nullable:true
        amountPaid nullable:false
        student nullable:true
        course nullable:true
        inscription nullable:true
        status nullable:true
        updatedByUser(display:false, nullable:true)
    }
}
