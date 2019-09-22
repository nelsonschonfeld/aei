<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'payment.label', default: 'Payment')}"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>
    <script type="text/javascript">
        function findFeeByIdentificationCode() {

            var query = $('#query').val();

            if (query != '') {
                $.ajax({
                    type: 'GET',
                    url: '/payment/findFeeByIdentificationCode',
                    data: {query: query},
                    success: function (resp) {
                        $("#identificationCode").html(resp);
                        //inicio del select
                        if ($('#identificationCode select').length > 0) {
                            findFeeDetails()
                        }
                        //funciion change del select
                        $('#identificationCode select').on('change', function () {
                            findFeeDetails()
                            $('#amountPaid').val(null)
                            $('#amountReturned').val(null)
                            $('#valueAmountReturned').val(null)
                        });
                    }
                });
            }
        }

        function findFeeDetails() {
            $.ajax({
                type: 'GET',
                url: '/payment/findFeeDetails',
                data: {idFee: $('#identificationCode select').val()},
                success: function (resp) {
                    $("#course").text(resp.course);
                    $("#student").text(resp.student);
                    $("#year").text(resp.year);
                    $("#month").text(resp.month);
                    $("#feeAmount").text(resp.feeAmount);
                    $("#feeAmountPaid").text(resp.feeAmountPaid);
                    $("#feeAmountFull").text(resp.feeAmountFull - resp.feeAmountPaid);
                    $("#discountAmount").text(resp.discountAmount);
                    $("#inscriptionCost").text(resp.inscriptionCost);
                    $("#testCost").text(resp.testCost);
                    $("#printCost").text(resp.printCost);
                    $("#extraCost").text(resp.extraCost);
                    $("#firstExpiredDate").text(resp.firstExpiredDate);
                    $("#amountFirstExpiredDate").text(resp.amountFirstExpiredDate);
                    $("#secondExpiredDate").text(resp.secondExpiredDate);
                    $("#amountSecondExpiredDate").text(resp.amountSecondExpiredDate);
                    $("#status").text(resp.status);
                }
            });
        }

        function getAmountReturned() {
            if (parseFloat($('#amountPaid').val()) > parseFloat($('#feeAmountFull').text())) {
                $('#amountReturned').val(parseFloat($('#amountPaid').val()) - parseFloat($('#feeAmountFull').text()))
                $('#valueAmountReturned').val($('#amountReturned').val())
            } else {
                $('#amountReturned').val(null)
                $('#valueAmountReturned').val(null)
            }
        }
    </script>
</head>

<body>
<a href="#create-payment" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                                default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="create-payment" class="content scaffold-create" role="main">
    <h1><g:message code="default.create.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:if test="${flash.error}">
        <div class="errors" role="alert">${flash.error}</div>
    </g:if>
    <g:hasErrors bean="${this.payment}">
        <ul class="errors" role="alert">
            <g:eachError bean="${this.payment}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                        error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>
    <g:form action="save">
        <fieldset class="form">

            <fieldset class="embedded " style="float: right; width: 30%;">
                <legend>Información de La Cuota</legend>

                <div>
                    <label>- Curso:</label>
                    <span id="course"></span>
                </div>

                <div class="">
                    <label>- Alumno:</label>
                    <span id="student"></span>
                </div>

                <div>
                    <label>- Año:</label>
                    <span id="year"></span>
                </div>

                <div>
                    <label>- Mes:</label>
                    <span id="month"></span>
                </div>

                <div>
                    <label>- Estado:</label>
                    <span id="status"></span>
                </div>

                <div>
                    <label><u>- Total a pagar ($):</u></label>
                    <span id="feeAmountFull"></span>
                </div>

                <div>
                    <label>- Total de la cuota ($):</label>
                    <span id="feeAmount"></span>
                </div>

                <div class="">
                    <label>- Total Pagado ($):</label>
                    <span id="feeAmountPaid"></span>
                </div>

                <div>
                    <label>- Descuento (%):</label>
                    <span id="discountAmount"></span>
                </div>

                <div>
                    <label>- Inscripción ($):</label>
                    <span id="inscriptionCost"></span>
                </div>

                <div>
                    <label>- Examen ($):</label>
                    <span id="testCost"></span>
                </div>

                <div>
                    <label>- Impresión ($):</label>
                    <span id="printCost"></span>
                </div>

                <div>
                    <label>- Monto Extra ($):</label>
                    <span id="extraCost"></span>
                </div>

                <div>
                    <label>- Primer Vencimiento:</label>
                    <span id="firstExpiredDate" var="first"></span>
                </div>

                <div>
                    <label>- Monto Primer Vencimiento ($):</label>
                    <span id="amountFirstExpiredDate"></span>
                </div>

                <div>
                    <label>- Segundo Vencimiento:</label>
                    <span id="secondExpiredDate"></span>
                </div>

                <div>
                    <label>- Monto Segundo Vencimiento ($):</label>
                    <span id="amountSecondExpiredDate"></span>
                </div>

            </fieldset>

            <div class="fieldcontain required">
                <label>Buscar
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="query"
                             id="query"
                             placeholder="Código de barra"></g:textField>
                <input type="button" id="search" onclick="findFeeByIdentificationCode()"
                       value="${message(code: 'default.button.search.label', default: 'Buscar')}"/>
            </div>

            <div class="fieldcontain required">
                <label>Cuota
                    <span class="required-indicator">*</span>
                </label>
                <span id="identificationCode"></span>
            </div>

            <f:field bean="payment" property="amountPaid">
                <g:field type="number" name="amountPaid" id="amountPaid" onchange="getAmountReturned()"/>
            </f:field>
            <div class="fieldcontain required">
                <label>Vuelto</label>
                <g:field type="number" name="amountReturned" id="amountReturned" disabled=""/>
                <g:hiddenField name="valueAmountReturned" id="valueAmountReturned"/>
            </div>

        </fieldset>
        <fieldset class="buttons">
            <g:submitButton name="create" class="save"
                            value="${message(code: 'default.button.create.label', default: 'Create')}"/>
        </fieldset>
    </g:form>
</div>
</body>
</html>
