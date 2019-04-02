package demo

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
    }

    def destroy = {
    }
}
