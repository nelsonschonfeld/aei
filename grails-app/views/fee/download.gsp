<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%! import grails.converters.JSON %>
<html>
<head>
    <meta name="layout" content="main" />
    <asset:javascript src="barcode.js"/>
    <asset:javascript src="jspdf.min.js"/>
    <asset:javascript src="jquery-3.4.1.min.js"/>
</head>
<body>
<g:if test="${flash.message}">
    <div class="message" role="status">${flash.message}</div>
</g:if>

<g:javascript>
    function parseModelToJS(jsonString) {
        jsonString=jsonString.replace(/\"/g,'"');
        var jsonObject=$.parseJSON(jsonString);
        return jsonObject
    }
$(document).ready(function(){
    var fillList = parseModelToJS('${pdf as JSON}');
    var doc = new jsPDF({orientation: 'landscape', format: 'legal'});
    var bill = 1;
    var y = 0;
    var x = 0;

    fillList.forEach(function(element) {
        if (bill !== 1 && bill%2 !== 0) { // si no es la boleta uno y es impar agrego una hoja
            doc.addPage();
        }
        JsBarcode("#barcode"+element.identificationCode, element.identificationCode, {
            format: "CODE128",
            lineColor: "#000",
            width: 1,
            height: 25,
            displayValue: true
        });

        var jpegUrl = ($("#barcode"+element.identificationCode)[0]).toDataURL("image/jpeg");
        
        // to print
        let student = $("#student"+element.identificationCode).text() + ' - ' + $("#course"+element.identificationCode).text();
        let bill1 = '1er venc. ' + $("#firstExpirationDate"+element.identificationCode).text() + ': $' + $("#totalBill"+element.identificationCode).text();
        let bill2 = '2do venc. ' + $("#secondExpirationDate"+element.identificationCode).text() + ': $' + $("#amountFirstExpiredDate"+element.identificationCode).text();
        let detail = 'Cuota ' + $("#month"+element.identificationCode).text() + ' ' + $("#year"+element.identificationCode).text();
        let detail2 = 'Cuota pura: $' + $("#amount"+element.identificationCode).text();
        let balance = 'Saldo: $' + $("#balance"+element.identificationCode).text();
        let discount = "Descuento(%): "+$("#descBill"+element.identificationCode).text();
        let printAmount = "Costo impresión: $"+$("#printCost"+element.identificationCode).text();

        //1er cuota (instituto)
        doc.addImage(jpegUrl, 'JPEG', 75+x, 10);
        doc.setFontSize(12);
        doc.setFont('arial', 'bold');
        //alumno - curso
        doc.text(50+x, 35, student);
        doc.setFontSize(11);
        doc.setFont('arial', '');
        //detalle
        doc.text(50+x, 45, detail);
        doc.text(100+x, 45, detail2);
        //horario cobro
        doc.text(50+x, 50, 'Horario de cobro: Lunes a viernes de 14:30 a 19:00');
        //comments
        doc.text(30+x, 55, 'Observaciones:');
        doc.text(30+x, 60, $("#comment"+element.identificationCode).text());
        //a cuenta y saldo labels
        //doc.text(30+x, 70, 'A cuenta:');
        doc.text(30+x, 75, balance);
        //descuento %
        doc.text(120+x, 70, discount);
        //costo impresion
        doc.text(120+x, 75, printAmount);
        //1er vencimiento
        doc.setFont('arial', 'bold');
        doc.text(120+x, 80, bill1);
        //2do vencimiento
        doc.text(120+x, 88, bill2);

        //2da cuota (alumno)
        doc.setFontSize(12);
        //alumno - curso
        doc.text(50+x, 145, student);
        doc.setFontSize(11);
        doc.setFont('arial', '');
        //detalle
        doc.text(50+x, 155, detail);
        doc.text(100+x, 155, detail2);
        //comments
        doc.text(30+x, 165, 'Observaciones:');
        doc.text(30+x, 170, $("#comment"+element.identificationCode).text());
        //a cuenta y saldo labels
        //doc.text(30+x, 180, 'A cuenta:');
        doc.text(30+x, 185, balance);
        //descuento %
        doc.text(120+x, 180, discount);
        //costo impresion
        doc.text(120+x, 185, printAmount);
        //1er vencimiento
        doc.setFont('arial', 'bold');
        doc.text(120+x, 190, bill1);
        //2do vencimiento
        doc.text(120+x, 198, bill2);


        // sección vertical eje y igual
        //alumno - segment
        doc.setFont('arial', '');
        doc.text(12+x, 195, student, null, 90);
        //1er vencimiento
        doc.text(12+x, 120, bill1, null, 90);
        //2do vencimiento
        doc.text(12+x, 60, bill2, null, 90);

        if (bill%2 === 0) { // si es par reinicio el eje x
            x = 0;
        } else {
            x = 170;
        }
        bill = bill + 1;
    });

    doc.output('dataurlnewwindow');
});
</g:javascript>

<g:each var="bill" in="${this.pdf}">
    <canvas id="barcode${bill.identificationCode}" style="display: none"></canvas>
    <p id="student${bill.identificationCode}" style="display: none">${bill.student.surname} ${bill.student.name}</p>
    <p id="course${bill.identificationCode}" style="display: none">${bill.course}</p>
    <p id="month${bill.identificationCode}" style="display: none">${bill.month}</p>
    <p id="year${bill.identificationCode}" style="display: none">${bill.year}</p>
    <p id="comment${bill.identificationCode}" style="display: none">${bill.comment}</p>
    <p id="descBill${bill.identificationCode}" style="display: none"><g:formatNumber number="${bill.discountAmount}" type="number" maxFractionDigits="0"/></p>
    <p id="printCost${bill.identificationCode}" style="display: none"><g:formatNumber number="${bill.printCost}" type="number" maxFractionDigits="0"/></p>
    <p id="amount${bill.identificationCode}" style="display: none"><g:formatNumber number="${bill.amount}" type="number" maxFractionDigits="0"/></p>
    <p id="totalBill${bill.identificationCode}" style="display: none"><g:formatNumber number="${bill.amountFull}" type="number" maxFractionDigits="0"/></p>
    <p id="balance${bill.identificationCode}" style="display: none"><g:formatNumber number="${bill.amountFull - bill.amount - bill.printCost}" type="number" maxFractionDigits="0"/></p>
    <p id="amountFirstExpiredDate${bill.identificationCode}" style="display: none"><g:formatNumber number="${bill.amountFirstExpiredDate}" type="number" maxFractionDigits="0"/></p>
    <p id="firstExpirationDate${bill.identificationCode}" style="display: none"><g:formatDate date="${bill.firstExpiredDate}" type="date"/></p>
    <p id="secondExpirationDate${bill.identificationCode}" style="display: none"><g:formatDate date="${bill.secondExpiredDate}" type="date"/></p>
</g:each>

</body>
</html>