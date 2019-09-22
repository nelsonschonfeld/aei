JsBarcode("#barcode", "1234", {
    format: "CODE39",
    lineColor: "#000",
    width: 4,
    height: 40,
    displayValue: true
});

var doc = new jsPDF()
var jpegUrl = ($("#barcode")[0]).toDataURL("image/jpeg");

doc.addImage(jpegUrl, 'JPEG', 20, 0);
doc.setFontSize(12);
doc.text(20, 30, $("#student").text());
doc.text(20, 40, $("#descBill").text());
doc.text(20, 50, $("#hours").text());
doc.text(20, 60, $("#firstExpirationDate").text());
doc.text(20, 70, $("#secondExpirationDate").text());

doc.save('a4.pdf')

