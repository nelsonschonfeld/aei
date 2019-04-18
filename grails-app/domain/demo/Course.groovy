package demo

import enums.CourseStatusEnum

class Course implements Serializable {

    String id
    String name
    String type
    String year
    String teacher
    String schedule
    Long amount
    Long firstDueCost
    Long secondDueCost
    Long inscriptionCost
    Long testCost
    Long printCost
    Boolean monday
    Boolean thursday
    Boolean wednesday
    Boolean tuesday
    Boolean friday
    Boolean saturday
    Date dateCreated
    Date lastUpdated
    CourseStatusEnum status

    static constraints = {
        name(nullable: false)
        type(nullable: false)
        year(nullable: false)
        teacher(nullable: false)
        amount(nullable: false)
        firstDueCost(nullable: false)
        secondDueCost(nullable: false)
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
        dateCreated(display: false)
        lastUpdated(display: false)
    }

    static mapping = {
        id generator: 'assigned'
    }
}
