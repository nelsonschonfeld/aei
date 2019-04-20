package aei

import enums.PersonTypeEnum

class Person {

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
    PersonTypeEnum type
    String comment
    Date dateCreated
    Date lastUpdated
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
