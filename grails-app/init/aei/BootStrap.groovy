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
            def person4 = new Person(name:'Ayelenn', surname:'Ciann',dni:'33444556',email:'ayelen@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person4.save(flush: flush, failOnError: failOnError)
            def person5 = new Person(name:'Ayelenq', surname:'Cianq',dni:'33444557',email:'ayelen@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person5.save(flush: flush, failOnError: failOnError)
            def person6 = new Person(name:'Ayelenw', surname:'Cianw',dni:'33444558',email:'ayelen@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person6.save(flush: flush, failOnError: failOnError)
            def person7 = new Person(name:'Ayelene', surname:'Ciane',dni:'33444559',email:'ayelen@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person7.save(flush: flush, failOnError: failOnError)
            def person8 = new Person(name:'Ayelenr', surname:'Cianr',dni:'33444550',email:'ayelen@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person8.save(flush: flush, failOnError: failOnError)
            def person9 = new Person(name:'Ayelent', surname:'Ciant',dni:'33444551',email:'ayelen@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person9.save(flush: flush, failOnError: failOnError)
            def person10 = new Person(name:'Ayeleny', surname:'Ciany',dni:'33444552',email:'ayelen@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person10.save(flush: flush, failOnError: failOnError)
            def person11 = new Person(name:'Ayelenu', surname:'Cianu',dni:'33444553',email:'ayelen@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person11.save(flush: flush, failOnError: failOnError)
            def person12 = new Person(name:'Nelsonq', surname:'Schonfeldq',dni:'34167234',email:'schonfeld@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person12.save(flush: flush, failOnError: failOnError)
            def person13 = new Person(name:'Ayelena', surname:'Ciana',dni:'33144555',email:'ayelen@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person13.save(flush: flush, failOnError: failOnError)
            def person14 = new Person(name:'Ayelenna', surname:'Cianna',dni:'33244556',email:'ayelen@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person14.save(flush: flush, failOnError: failOnError)
            def person15 = new Person(name:'Ayelenqa', surname:'Cianqa',dni:'33344557',email:'ayelen@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person15.save(flush: flush, failOnError: failOnError)
            def person16 = new Person(name:'Ayelenwa', surname:'Cianwa',dni:'33544558',email:'ayelen@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person16.save(flush: flush, failOnError: failOnError)
            def person17 = new Person(name:'Ayelenea', surname:'Cianea',dni:'33644559',email:'ayelen@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person17.save(flush: flush, failOnError: failOnError)
            def person18 = new Person(name:'Ayelenra', surname:'Cianra',dni:'33744550',email:'ayelen@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person18.save(flush: flush, failOnError: failOnError)
            def person19 = new Person(name:'Ayelenta', surname:'Cianta',dni:'33844551',email:'ayelen@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person19.save(flush: flush, failOnError: failOnError)
            def person20 = new Person(name:'Ayelenya', surname:'Cianya',dni:'33944552',email:'ayelen@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person20.save(flush: flush, failOnError: failOnError)
            def person21 = new Person(name:'Ayelenua', surname:'Cianua',dni:'33444953',email:'ayelen@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person21.save(flush: flush, failOnError: failOnError)
            def person22 = new Person(name:'Ayelenyae', surname:'Cianyae',dni:'33914552',email:'ayelen@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person22.save(flush: flush, failOnError: failOnError)
            def person23 = new Person(name:'Ayelenuae', surname:'Cianuae',dni:'33444963',email:'ayelen@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person23.save(flush: flush, failOnError: failOnError)
            def person24 = new Person(name:'Ayelenyar', surname:'Cianyar',dni:'33944652',email:'ayelen@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person24.save(flush: flush, failOnError: failOnError)
            def person25 = new Person(name:'Ayelenuat', surname:'Cianuat',dni:'33444053',email:'ayelen@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person25.save(flush: flush, failOnError: failOnError)
            def person26 = new Person(name:'Ayelenyaey', surname:'Cianyaey',dni:'33954552',email:'ayelen@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person26.save(flush: flush, failOnError: failOnError)
            def person27 = new Person(name:'Ayelenuaey', surname:'Cianuaey',dni:'33444933',email:'ayelen@gmail.com',
                    address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante, updatedByUser: "nelson")
            person27.save(flush: flush, failOnError: failOnError)

            def course = new Course(name: 'Segment 5', type: 'Junior', year: 2020, teacher: 'Mercedez', amount: 1000, firstDueCost: 1100, inscriptionCost: 300, testCost: 500, printCost: 50, monday: true, wednesday: true, schedule: "19:30", status: CourseStatusEnum.Abierto, dateCreated: new Date(), lastUpdated: new Date(), updatedByUser: "nelson")
            course.id="Segment 5 Junior 2020"
            course.save(flush: flush, failOnError: true)
            def course2 = new Course(name: 'Segment 5', type: 'Adult', year: 2018, teacher: 'Mercedez', amount: 1200, firstDueCost: 1300, inscriptionCost: 500, testCost: 800, printCost: 25, wednesday: true, saturday: true, schedule: "21:30", status: CourseStatusEnum.Abierto, dateCreated: new Date(), lastUpdated: new Date(), updatedByUser: "nelson")
            course2.id="Segment 5 Adult 2018"
            course2.save(flush: flush, failOnError: true)
            def course3 = new Course(name: 'Segment 5', type: 'Adult', year: 2021, teacher: 'Mercedez', amount: 1200, firstDueCost: 1300, inscriptionCost: 500, testCost: 800, printCost: 25, monday: true, wednesday: true, schedule: "20:30", status: CourseStatusEnum.Abierto, dateCreated: new Date(), lastUpdated: new Date(), updatedByUser: "nelson")
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
