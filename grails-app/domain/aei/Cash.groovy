package aei

class Cash {

  Date dateCreated = new Date()
    Double initalAmount
    Double costs
    Double total
    String comment
    String user

    static constraints = {
        dateCreated(nullable:false, unique:true)
        initalAmount nullable:false
        costs nullable:false
        total nullable:true
        comment nullable:true
        user(display:false, nullable:true)
    }
}
