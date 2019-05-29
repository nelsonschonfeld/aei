package aei

class Inscription {

    String id
    Course course
    Person student
    Integer discountAmount = 0
    Date dateCreated
    Date lastUpdated

    static constraints = {
        course nullable:true
        student nullable:true
        discountAmount min:0, max:100
        dateCreated display:false
        lastUpdated display:false
    }

    static mapping = {
        id generator: 'assigned'
    }
}
