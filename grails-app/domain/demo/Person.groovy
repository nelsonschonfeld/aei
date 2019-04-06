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

    static constraints = {
        name(nullable:false)
        surname(nullable:false)
        dni(nullable:false, matches:'\\d{7,10}')
        email(nullable:false, email:true)
        address(nullable:false)
        cellphone(nullable:false, matches:'\\d{9,15}')
        telephone(nullable:true, matches:'\\d{7,15}')
        birthDate(nullable:false)
        birthPlace(nullable:true)
        profession(nullable:true)
        professionPlace(nullable:true)
        type(nullable:false)
        comment(nullable:true)
        dateCreated display:false
        lastUpdated display:false
    }
}
