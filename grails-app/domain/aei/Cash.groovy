package aei

class Cash {

    Date dateCreated = new Date()
    Double initalAmount
    Double eCollections
    Double costs
    Double total
    String comment
    User user
    Double withdraw
    Double income
    String updatedByUser
    Date lastUpdated

    static constraints = {
        dateCreated(nullable:false, editable: false,  unique:true)
        initalAmount nullable:false
        costs nullable:false
        withdraw nullable:false
        income nullable:false
        eCollections nullable:true
        total (nullable:true, editable: false)
        comment nullable:true
        user(display:false, nullable:true)
        lastUpdated(display:false, nullable:true)
        updatedByUser(display:false, nullable:true)
    }
}
