package aei

class Inscription {

    String id
    Course course
    Person student
    int discountAmount
    Date dateCreated
    Date lastUpdated
    String updatedByUser

    static constraints = {
        course nullable:true
        student nullable:true
        discountAmount min:0, max:100
        dateCreated display:false
        lastUpdated display:false
        updatedByUser(display:false, nullable:true)
    }

    static mapping = {
        id generator: 'assigned'
        discountAmount defaultValue: 0
    }

    String toString(){
        return id
    }
}
