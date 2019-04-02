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
        roleSec = new Role(authority: 'ROLE_SECRETARIA1')
        roleSec.save(flush: flush, failOnError: true)
        roleSec = new Role(authority: 'ROLE_SECRETARIA2')
        roleSec.save(flush: flush, failOnError: true)
        roleSec = new Role(authority: 'ROLE_SECRETARIA3')
        roleSec.save(flush: flush, failOnError: true)
        roleSec = new Role(authority: 'ROLE_SECRETARIA4')
        roleSec.save(flush: flush, failOnError: true)
        roleSec = new Role(authority: 'ROLE_SECRETARIA5')
        roleSec.save(flush: flush, failOnError: true)
        roleSec = new Role(authority: 'ROLE_SECRETARIA6')
        roleSec.save(flush: flush, failOnError: true)
        roleSec = new Role(authority: 'ROLE_SECRETARIA7')
        roleSec.save(flush: flush, failOnError: true)
        roleSec = new Role(authority: 'ROLE_SECRETARIA8')
        roleSec.save(flush: flush, failOnError: true)
        roleSec = new Role(authority: 'ROLE_SECRETARIA9')
        roleSec.save(flush: flush, failOnError: true)
        roleSec = new Role(authority: 'ROLE_SECRETARIA11')
        roleSec.save(flush: flush, failOnError: true)
        roleSec = new Role(authority: 'ROLE_SECRETARIA12')
        roleSec.save(flush: flush, failOnError: true)
        roleSec = new Role(authority: 'ROLE_SECRETARIA13')
        roleSec.save(flush: flush, failOnError: true)
        roleSec = new Role(authority: 'ROLE_SECRETARIA14')
        roleSec.save(flush: flush, failOnError: true)
        roleSec = new Role(authority: 'ROLE_SECRETARIA15')
        roleSec.save(flush: flush, failOnError: true)
        roleSec = new Role(authority: 'ROLE_SECRETARIA16')
        roleSec.save(flush: flush, failOnError: true)
        def userSec = new User(username: 'secret', password: 'secret')
        userSec.save(flush: flush)
        def userRoleSec = new UserRole(role: roleSec, user: userSec)
        userRoleSec.save(flush: flush, failOnError: failOnError)
    }

    def destroy = {
    }
}
