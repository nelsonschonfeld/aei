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
        }
    }

    def destroy = {
    }

}
