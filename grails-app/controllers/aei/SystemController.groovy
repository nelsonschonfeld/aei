package aei

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.util.Environment

@Transactional(readOnly = true)
@Secured(['ROLE_ADMIN'])

class SystemController {
    
    def export() {

    }
    
    def save() {
        def host = 'localhost'
        def user = 'root'
        def password = 'jp2019sistema'
        def database = 'prodDB'

        if (Environment.current == Environment.DEVELOPMENT) {
            password = 'root'
            database = 'devDB'
        }

        def filename = "BKP-${database}-${new Date().format('yyyyMMdd')}"
        def sqlfilename = "${filename}.sql"

        def command = ""
        if (Environment.current == Environment.DEVELOPMENT) {
            // Export develop
            command = "mysqldump --opt --user=${user} --password=${password} ${database}"
        } else {
            // Export en prod
            command = "mysqldump -u ${user} -p${password} ${database} > ${sqlfilename}"
        }

        def dump = command.execute()
        dump.waitFor()
        def file = new File(sqlfilename)
        file.write(dump.text)

        if (file.exists()) {
            response.setContentType("application/octet-stream")
            response.setHeader("Content-disposition", "filename=${file.name}")
            response.outputStream << file.bytes
        }
        return
    }
}

