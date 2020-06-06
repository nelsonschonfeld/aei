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
           
        //1er cuota (instituto)
        doc.addImage(jpegUrl, 'JPEG', 75+x, 10);
        doc.setFontSize(12);
        //alumno - curso
        doc.text(50+x, 35, $("#student"+element.identificationCode).text() + ' - ' + $("#course"+element.identificationCode).text());
        doc.setFontSize(11);
        //detalle
        doc.text(50+x, 45, 'Cuota ' + $("#month"+element.identificationCode).text() + ' ' + $("#year"+element.identificationCode).text());
        doc.text(100+x, 45, 'Cuota pura: ' + $("#amount"+element.identificationCode).text());
        //horario cobro
        doc.text(50+x, 50, 'Horario de cobro: Lunes a viernes de 14:30 a 19:00');
        //comments
        doc.text(30+x, 55, 'Observaciones:');
        doc.text(30+x, 60, $("#comment"+element.identificationCode).text());
        //a cuenta y saldo labels
        doc.text(30+x, 70, 'A cuenta:');
        doc.text(30+x, 75, 'Saldo:');
        //descuento %
        doc.text(120+x, 70, "Descuento(%): "+$("#descBill"+element.identificationCode).text());
        //costo impresion
        doc.text(120+x, 75, "Costo impresión: "+$("#printCost"+element.identificationCode).text());
        //1er vencimiento
        doc.text(120+x, 80, '1er venc. ' + $("#firstExpirationDate"+element.identificationCode).text() + ' ' + $("#totalBill"+element.identificationCode).text());
        //2do vencimiento
        doc.text(120+x, 88, '2do venc. ' + $("#secondExpirationDate"+element.identificationCode).text() + ' ' + $("#amountFirstExpiredDate"+element.identificationCode).text());

        //2da cuota (alumno)
        doc.setFontSize(12);
        //alumno - curso
        doc.text(50+x, 145, $("#student"+element.identificationCode).text() + ' - ' + $("#course"+element.identificationCode).text());
        doc.setFontSize(11);
        //detalle
        doc.text(50+x, 155, 'Cuota ' + $("#month"+element.identificationCode).text() + ' ' + $("#year"+element.identificationCode).text());
        doc.text(100+x, 155, 'Cuota pura: ' + $("#amount"+element.identificationCode).text());
        //comments
        doc.text(30+x, 165, 'Observaciones:');
        doc.text(30+x, 170, $("#comment"+element.identificationCode).text());
        //a cuenta y saldo labels
        doc.text(30+x, 180, 'A cuenta:');
        doc.text(30+x, 185, 'Saldo:');
        //descuento %
        doc.text(120+x, 180, "Descuento(%): "+$("#descBill"+element.identificationCode).text());
        //costo impresion
        doc.text(120+x, 185, "Costo impresión: "+$("#printCost"+element.identificationCode).text());
        //1er vencimiento
        doc.text(120+x, 190, '1er venc. ' + $("#firstExpirationDate"+element.identificationCode).text() + ' ' + $("#totalBill"+element.identificationCode).text());
        //2do vencimiento
        doc.text(120+x, 198, '2do venc. ' + $("#secondExpirationDate"+element.identificationCode).text() + ' ' + $("#amountFirstExpiredDate"+element.identificationCode).text());


        // sección vertical eje y igual
        //alumno - segment
        doc.text(12+x, 195, $("#student"+element.identificationCode).text() + ' - ' + $("#course"+element.identificationCode).text(), null, 90);
        //1er vencimiento
        doc.text(12+x, 120, '1er venc. ' + $("#firstExpirationDate"+element.identificationCode).text() + ' ' + $("#totalBill"+element.identificationCode).text(), null, 90);
        //2do vencimiento
        doc.text(12+x, 60, '2do venc. ' + $("#secondExpirationDate"+element.identificationCode).text() + ' ' + $("#amountFirstExpiredDate"+element.identificationCode).text(), null, 90);

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
    <p id="amountFirstExpiredDate${bill.identificationCode}" style="display: none"><g:formatNumber number="${bill.amountFirstExpiredDate}" type="number" maxFractionDigits="0"/></p>
    <p id="firstExpirationDate${bill.identificationCode}" style="display: none"><g:formatDate date="${bill.firstExpiredDate}" type="date"/></p>
    <p id="secondExpirationDate${bill.identificationCode}" style="display: none"><g:formatDate date="${bill.secondExpiredDate}" type="date"/></p>
</g:each>

</body>
</html>