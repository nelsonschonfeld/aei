package aei

class Cash {

  Date dateCreated = new Date()
    Double initalAmount
    Double costs
    Double total
    String comment
    String user

    static constraints = {
        dateCreated(unique:true)
        user (display:false,nullable:true)
        total(editable: false,nullable:true)
        comment(nullable:true)
    }
}
