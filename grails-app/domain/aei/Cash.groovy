package aei

class Cash {

  Date dateCreated = new Date()
    Double initalAmount
    Double costs
    Double total
    String comment
    User user

    static constraints = {
        dateCreated(nullable:false, editable: false,  unique:true)
        initalAmount nullable:false
        costs nullable:false
        total (nullable:true, editable: false)
        comment nullable:true
        user(display:false, nullable:true)
    }
}
