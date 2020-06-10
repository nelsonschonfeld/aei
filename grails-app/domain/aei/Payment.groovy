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
        student nullable:true
        course nullable:true
        inscription nullable:true
        fee nullable:true
        feeCode nullable:true
        amountDelivered nullable:false
        amountPaid nullable:false
        amountReturned nullable:false
        status nullable:true
        dateCreated nullable:true
        lastUpdated (display:false, nullable:true)
        updatedByUser(display:false, nullable:true)
    }
}
