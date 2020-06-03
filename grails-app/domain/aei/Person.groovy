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
    String birthPlace
    String profession
    String professionPlace
    PersonTypeEnum type
    String comment
    String updatedByUser
    Date dateCreated
    Date lastUpdated


    static constraints = {
        dni(nullable:false, unique:true, matches:'\\d{7,10}')
        name(nullable:false)
        surname(nullable:false)
        address(nullable:false)
        cellphone(nullable:false, matches:'\\d{9,15}')
        email(nullable:true, email:true)
        telephone(nullable:true, matches:'\\d{7,15}')
        birthDate(nullable:false)
        birthPlace(nullable:true)
        profession(nullable:true)
        professionPlace(nullable:true)
        type(nullable:false)
        comment(nullable:true)
        updatedByUser(display:false, nullable:true)
        dateCreated(display:true)
        lastUpdated(display:false)
    }

    String toString(){
        return surname + ' ' + name + ' ' + dni
    }
}
