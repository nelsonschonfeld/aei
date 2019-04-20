package aei

import enums.CourseStatusEnum

class BootStrap {

    def init = { servletContext ->

        final boolean flush = true
        final boolean failOnError = true
        def role = new Role(authority: 'ROLE_ADMIN')
        role.save(flush: flush, failOnError: true)
        def user = new User(username: 'admin', password: 'admin')
        user.save(flush: flush)
        def userRole = new UserRole(role: role, user: user)
        userRole.save(flush: flush, failOnError: failOnError)

        def roleSec = new Role(authority: 'ROLE_SECRETARIA')
        roleSec.save(flush: flush, failOnError: true)
        def userSec = new User(username: 'secret', password: 'secret')
        userSec.save(flush: flush)
        def userRoleSec = new UserRole(role: roleSec, user: userSec)
        userRoleSec.save(flush: flush, failOnError: failOnError)

        def course = new Course(name: 'Segment 5', type: 'Junior', year: 2019, teacher: 'Mercedez', amount: 1200, firstDueCost: 1300, secondDueCost: 1400, inscriptionCost: 500, testCost: 800, printCost: 25, status: CourseStatusEnum.Abierto, dateCreated: new Date(), lastUpdated: new Date())
        course.id="Segment 5 Junior 2019"
        course.save(flush: flush, failOnError: true)
        def course2 = new Course(name: 'Segment 5', type: 'Adult', year: 2018, teacher: 'Mercedez', amount: 1200, firstDueCost: 1300, secondDueCost: 1400, inscriptionCost: 500, testCost: 800, printCost: 25, saturday: true,status: CourseStatusEnum.Abierto, dateCreated: new Date(), lastUpdated: new Date())
        course2.id="Segment 5 Adult 2018"
        course2.save(flush: flush, failOnError: true)
        def course3 = new Course(name: 'Segment 5', type: 'Adult', year: 2019, teacher: 'Mercedez', amount: 1200, firstDueCost: 1300, secondDueCost: 1400, inscriptionCost: 500, testCost: 800, printCost: 25, status: CourseStatusEnum.Abierto, dateCreated: new Date(), lastUpdated: new Date())
        course3.id="Segment 5 Adult 2019"
        course3.save(flush: flush, failOnError: true)
    }

    def destroy = {
    }
}