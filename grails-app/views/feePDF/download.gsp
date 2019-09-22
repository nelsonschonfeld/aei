<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
 <%! import grails.converters.JSON %>
<html>
<head>
<asset:javascript src="barcode.js"/>
<asset:javascript src="jspdf.min.js"/>
<asset:javascript src="jquery-3.4.1.min.js"/>
</head>
<body>

<g:javascript>
    function parseModelToJS(jsonString) {
        jsonString=jsonString.replace(/\"/g,'"');
        var jsonObject=$.parseJSON(jsonString);
        return jsonObject
    }
$(document).ready(function(){
    var fillList = parseModelToJS('${pdf as JSON}');
    var doc = new jsPDF('landscape');
    var bill = 1;
    var y = 0;
    var x = 0;

    fillList.forEach(function(element) {
        if (bill !== 1 && bill%2 !== 0) { // si no es la boleta uno y es impar agrego una hoja
            doc.addPage();
        }
        JsBarcode("#barcode"+element.id, element.id, {
        format: "CODE39",
        lineColor: "#000",
        width: 4,
        height: 40,
        displayValue: true
        });

        var jpegUrl = ($("#barcode"+element.id)[0]).toDataURL("image/jpeg");

        for (var i=1; i<=2; i++) { // va duplicado
            if (i == 2) { // si es el duplicado incremento eje y
                y = 90;
            } else {
                y = 0;
            }
            doc.addImage(jpegUrl, 'JPEG', 30+x, 0+y);
            doc.setFontSize(12);
            doc.text(30+x, 30+y, $("#student"+element.id).text());
            doc.text(30+x, 40+y, $("#descBill"+element.id).text());
            doc.text(30+x, 50+y, $("#hours"+element.id).text());
            doc.text(30+x, 60+y, $("#firstExpirationDate"+element.id).text());
            doc.text(30+x, 70+y, $("#secondExpirationDate"+element.id).text());
        }

        // seción vertical eje y igual
        doc.text(10+x, 60, $("#student"+element.id).text(), null, 90);

        if (bill%2 === 0) { // si es par reinicio el eje x
            x = 0;
        } else {
            x = 150;
        }
        bill = bill + 1;
    });

    doc.save('a4.pdf')
});
</g:javascript>

<g:each var="bill" in="${this.pdf}">
    <canvas id="barcode${bill.id}"></canvas>
    <p id="student${bill.id}">${bill.student}</p>
    <p id="descBill${bill.id}">${bill.descBill}</p>
    <p id="hours${bill.id}">${bill.hours}</p>
    <p id="firstExpirationDate${bill.id}">${bill.firstExpirationDate}</p>
    <p id="secondExpirationDate${bill.id}">${bill.secondExpirationDate}</p>
</g:each>

</body>
</html>