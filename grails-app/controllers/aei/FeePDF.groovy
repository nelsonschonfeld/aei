package aei

import static org.springframework.http.HttpStatus.*
import grails.plugin.springsecurity.annotation.Secured
import grails.converters.JSON

@Secured(['ROLE_ADMIN', 'ROLE_SECRETARIA'])
class FeePDFController {

    def download() {
        def pdf = []
        def student = [:]
        student.id = 0001
        student.student = "Nelson Schonfeld"
        student.descBill = "Cuota Nov + 1/2 Dic 2018 - Segment VIII A"
        student.hours = "HORARIO DE COBRO: 14:00 a 20:30 - Lunes a Viernes"
        student.firstExpirationDate = "1er venc 12/11/2018: \$1500"
        student.secondExpirationDate = "1er venc 12/11/2018: \$1500"

        pdf.add(student)

        student = [:]
        student.id = 0002
        student.student = "Brian Ramseyer"
        student.descBill = "Cuota Nov + 1/2 Dic 2018 - Segment VIII A"
        student.hours = "HORARIO DE COBRO: 14:00 a 20:30 - Lunes a Viernes"
        student.firstExpirationDate = "1er venc 12/11/2018: \$1500"
        student.secondExpirationDate = "1er venc 12/11/2018: \$1500"

        pdf.add(student)

        student = [:]
        student.id = 0003
        student.student = "Ayelen Cian"
        student.descBill = "Cuota Nov + 1/2 Dic 2018 - Segment VIII A"
        student.hours = "HORARIO DE COBRO: 14:00 a 20:30 - Lunes a Viernes"
        student.firstExpirationDate = "1er venc 12/11/2018: \$1500"
        student.secondExpirationDate = "1er venc 12/11/2018: \$1500"

        pdf.add(student)
        
        respond ("pdf":pdf)
    }
}