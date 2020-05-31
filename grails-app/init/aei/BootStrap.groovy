package aei

import enums.CourseStatusEnum
import enums.FeeStatusEnum
import enums.Months
import enums.PersonTypeEnum
import grails.util.Environment

class BootStrap {

    def init = { servletContext ->

        if (Environment.current == Environment.DEVELOPMENT) {
            final boolean flush = true
            final boolean failOnError = true
            def role = new Role(authority: 'ROLE_ADMIN')
            role.save(flush: flush, failOnError: true)
            def user = new User(username: 'juanpablo', password: 'admjpablo', fullName: 'Administrador')
            user.save(flush: flush)
            def userRole = new UserRole(role: role, user: user)
            userRole.save(flush: flush, failOnError: failOnError)

            def roleSec = new Role(authority: 'ROLE_SECRETARIA')
            roleSec.save(flush: flush, failOnError: true)
            def userSec = new User(username: 'secretaria', password: 'aeisecret', fullName: 'Secretaria')
            userSec.save(flush: flush)
            def userRoleSec = new UserRole(role: roleSec, user: userSec)
            userRoleSec.save(flush: flush, failOnError: failOnError)

            def person = new Person(name:'Brian', surname:'Ramseyer',dni:'34680226',email:'rbriann@gmail.com',
                    address:'Venezuela 1345',cellphone:'155044759',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person.save(flush: flush, failOnError: failOnError)
            def person2 = new Person(name:'Nelson', surname:'Schonfeld',dni:'34867234',email:'schonfeld@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person2.save(flush: flush, failOnError: failOnError)
            def person3 = new Person(name:'Ayelen', surname:'Cian',dni:'33444555',email:'ayelen@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person3.save(flush: flush, failOnError: failOnError)

            def course = new Course(name: 'Segment 5', type: 'Junior', year: 2020, teacher: 'Mercedez', amount: 1000, firstDueCost: 1100, secondDueCost: 1100, inscriptionCost: 300, testCost: 500, printCost: 50, monday: true, wednesday: true, schedule: "19:30", status: CourseStatusEnum.Abierto, dateCreated: new Date(), lastUpdated: new Date(), updatedByUser: "nelson")
            course.id="Segment 5 Junior 2020"
            course.save(flush: flush, failOnError: true)
            def course2 = new Course(name: 'Segment 5', type: 'Adult', year: 2018, teacher: 'Mercedez', amount: 1200, firstDueCost: 1300, secondDueCost: 1300, inscriptionCost: 500, testCost: 800, printCost: 25, wednesday: true, saturday: true, schedule: "21:30", status: CourseStatusEnum.Abierto, dateCreated: new Date(), lastUpdated: new Date(), updatedByUser: "nelson")
            course2.id="Segment 5 Adult 2018"
            course2.save(flush: flush, failOnError: true)
            def course3 = new Course(name: 'Segment 5', type: 'Adult', year: 2021, teacher: 'Mercedez', amount: 1200, firstDueCost: 1300, secondDueCost: 1300, inscriptionCost: 500, testCost: 800, printCost: 25, monday: true, wednesday: true, schedule: "20:30", status: CourseStatusEnum.Abierto, dateCreated: new Date(), lastUpdated: new Date(), updatedByUser: "nelson")
            course3.id="Segment 5 Adult 2021"
            course3.save(flush: flush, failOnError: true)

            def inscription = new Inscription(student: person, course: course, discountAmount: 10 ,dateCreated: new Date(), lastUpdated: new Date(), updatedByUser: "nelson")
            inscription.id = "123"
            inscription.save(flush: flush, failOnError: true)
            def inscription2 = new Inscription(student: person, course: course2, discountAmount: 10 ,dateCreated: new Date(), lastUpdated: new Date(), updatedByUser: "nelson")
            inscription2.id = "1234"
            inscription2.save(flush: flush, failOnError: true)
            def inscription3 = new Inscription(student: person, course: course3, discountAmount: 10 ,dateCreated: new Date(), lastUpdated: new Date(), updatedByUser: "nelson")
            inscription3.id = "1235"
            inscription3.save(flush: flush, failOnError: true)
            /*def inscription2 = new Inscription(student: person2, course: course, discountAmount: 10, status: 'activo', active: true,dateCreated: new Date(), lastUpdated: new Date())
            inscription2.save(flush: flush, failOnError: true)
            def inscription3 = new Inscription(student: person2, course: course2, discountAmount: 10, status: 'activo', active: true,dateCreated: new Date(), lastUpdated: new Date())
            inscription3.save(flush: flush, failOnError: true)*/
        }
    }

    def destroy = {
    }

}
