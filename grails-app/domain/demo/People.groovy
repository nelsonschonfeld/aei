package demo

class People {

    String name
    String surname
    String dni
    String address
    String email
    String telephone
    String cellphone
    Date birthDate
    Date birthPlace
    String profession
    String professionPlace
    Date dateCreated
    Date lastUpdated
    String type
    Boolean active

    static constraints = {
        telephone(nullable:true)
        cellphone(nullable:true)
        birthPlace(nullable:true)
        profession(nullable:true)
        professionPlace(nullable:true)
        email(email:true)
    }
}
