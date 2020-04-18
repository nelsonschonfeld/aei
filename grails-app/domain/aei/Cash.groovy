package aei

class Cash {
  
    Date dateCreated = new Date()
    Double initalAmount
    Double costs
    String comment
    String user

    static constraints = {
        dateCreated(nullable:false, unique:true)
    }
}
