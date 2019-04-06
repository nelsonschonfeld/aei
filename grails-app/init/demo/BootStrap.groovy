package demo

import enums.PersonTypeEnum

class BootStrap {

    def init = { servletContext ->

        final boolean flush = true
        final boolean failOnError = true
        def role = new Role(authority: 'ROLE_ADMIN')
        role.save(flush: flush, failOnError: true)
        def user = new User(username: 'admin', password: 'admin', fullName: 'Administrador')
        user.save(flush: flush)
        def userRole = new UserRole(role: role, user: user)
        userRole.save(flush: flush, failOnError: failOnError)

        def roleSec = new Role(authority: 'ROLE_SECRETARIA')
        roleSec.save(flush: flush, failOnError: true)
        def userSec = new User(username: 'secret', password: 'secret', fullName: 'Secretaria')
        userSec.save(flush: flush)
        def userRoleSec = new UserRole(role: roleSec, user: userSec)
        userRoleSec.save(flush: flush, failOnError: failOnError)

        def person = new Person(name:'Brian', surname:'Ramseyer',dni:'34680226',email:'rbriann@gmail.com',
                                address:'Venezuela 1345',cellphone:'155044759',birthDate:new Date(),type:PersonTypeEnum.Estudiante)
        person.save(flush: flush, failOnError: failOnError)
        def person2 = new Person(name:'Nelson', surname:'Schonfeld',dni:'34867234',email:'schonfeld@gmail.com',
                                address:'BS AS 1345',cellphone:'155567890',birthDate:new Date(),type:PersonTypeEnum.Estudiante)
        person2.save(flush: flush, failOnError: failOnError)
    }

    def destroy = {
    }
}
