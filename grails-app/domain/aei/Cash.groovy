package aei

class Cash {

  Date dateCreated = new Date()
    Double initalAmount
    Double costs
    Double total
    String comment
    String user
    Double withdraw
    Double income

    static constraints = {
        dateCreated(nullable:false, unique:true)
        initalAmount nullable:false
        costs nullable:false
        withdraw nullable:false
        income nullable:false
        total nullable:true
        comment nullable:true
        user(display:false, nullable:true)
    }
}
