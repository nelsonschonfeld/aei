package aei

import enums.CourseStatusEnum

class Course {

    String id
    String name
    String type
    String year
    String teacher
    String schedule
    Double amount
    Double firstDueCost
    Double inscriptionCost
    Double testCost
    Long printCost
    Boolean monday
    Boolean thursday
    Boolean wednesday
    Boolean tuesday
    Boolean friday
    Boolean saturday
    String updatedByUser
    Date dateCreated
    Date lastUpdated
    CourseStatusEnum status

    static constraints = {
        name(nullable: false)
        type(nullable: false)
        year(nullable: false, matches: "^[0-9][0-9][0-9][0-9]\$")
        teacher(nullable: false)
        amount(nullable: false)
        firstDueCost(nullable: false)
        inscriptionCost(nullable: false)
        testCost(nullable: false)
        printCost(nullable: false)
        schedule(nullable: true, matches: "^([01]?[0-9]|2[0-3]):[0-5][0-9]\$")
        monday(nullable: true)
        thursday(nullable: true)
        wednesday(nullable: true)
        tuesday(nullable: true)
        friday(nullable: true)
        saturday(nullable: true)
        status(nullable: false)
        updatedByUser(display:false, nullable:true)
        dateCreated(display: false)
        lastUpdated(display: false)
    }

    static mapping = {
        id generator: 'assigned'
    }

    String toString(){
        return name + ' ' + type + ' ' + year
    }
}
