package demo

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
    String birthPlace
    String profession
    String professionPlace
    PersonTypeEnum type
    String comment
    Date dateCreated
    Date lastUpdated
    Boolean active = true

    static constraints = {
        name(nullable:false)
        surname(nullable:false)
        dni(nullable:false)
        email(nullable:false)
        address(nullable:false)
        cellphone(nullable:false)
        telephone(nullable:true)
        birthDate(nullable:false)
        birthPlace(nullable:true)
        profession(nullable:true)
        professionPlace(nullable:true)
        type(nullable:false)
        comment(nullable:true)
        dateCreated display:false
        lastUpdated display:false
        active display:false
    }
}
