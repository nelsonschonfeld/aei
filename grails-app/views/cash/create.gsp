<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'cash.label', default: 'Cash')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
        <script type="text/javascript">
            function getAmountReturned() {
                var initalAmount = parseFloat($('#initalAmount').val())
                var costs = parseFloat($('#costs').val())
                var withdraw = parseFloat($('#withdraw').val())
                var income = parseFloat($('#income').val())
                var eCollections = parseFloat($('#eCollections').val())
                $('#total').val(initalAmount - costs - withdraw + income - eCollections);
                $('#valueTotal').val($('#total').val());
            }
        </script>
    </head>
    <body>
        <a href="#create-cash" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="create-cash" class="content scaffold-create" role="main">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="alert">${flash.error}</div>
            </g:if>
            <g:hasErrors bean="${this.cash}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.cash}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form action="save">
                <fieldset class="form">
                    <fieldset class="embedded " style="float: right; width: 30%;">
                        <legend>Cierre de caja anterior</legend>

                        <div>
                            <span><label>- Fecha:</label> ${preDate}</span>
                        </div>

                        <div>
                            <span><label>- Monto inicial ($):</label> ${preInitialAmount}</span>
                        </div>

                        <div>
                            <span><label>- Gastos ($):</label> ${preCosts}</span>
                        </div>

                        <div>
                            <span><label>- Retiros ($):</label> ${preWithdraw}</span>
                        </div>

                        <div>
                            <span><label>- Ingresos por pago de cuotas ($):</label> ${preIncome}</span>
                        </div>

                        <div>
                            <span><label>- Ingresos por pagos electrónicos ($):</label> ${preECollections}</span>
                        </div>

                        <div>
                            <span><label><u>- Total Cierre de Caja ($):</u></label> ${preTotal}</span>
                        </div>

                        <div>
                            <span><label>- Comentarios: </label> ${preComments}</span>
                        </div>

                    </fieldset>

                    <f:all bean="cash" except="comment,initalAmount,total,income,withdraw,costs,eCollections"/>

                    <f:field bean="cash" property="initalAmount" required="false">
                        <g:field type="number" min="0" name="initalAmountDisable" value="${initialAmountNew}" required="" id="initialAmountNew" disabled="true"/>
                        <g:hiddenField name="${property}" value="${initialAmountNew}" />
                    </f:field>

                    <f:field bean="cash" property="costs">
                        <g:field type="number" min="0" name="${property}" value="${value ? value : 0}" required="" id="costs" onchange="getAmountReturned()"/>
                    </f:field>

                    <f:field bean="cash" property="withdraw">
                        <g:field type="number" min="0" name="${property}" value="${value ? value : 0}" required="" id="withdraw" onchange="getAmountReturned()"/>
                    </f:field>

                    <f:field bean="cash" property="income" required="false">
                        <g:field type="number" min="0" name="incomeDisable" value="${income}" id="income" required="" disabled="true"/>
                        <g:hiddenField name="${property}" value="${income}" />
                    </f:field>

                    <f:field bean="cash" property="eCollections">
                        <g:field type="number" min="0" name="${property}" value="${value ? value : 0}" required="false" id="eCollections" onchange="getAmountReturned()"/>
                    </f:field>

                    <f:field bean="cash" property="total">
                        <g:field type="number" min="0" name="totalDisable" value="${total}" id="total" required="" disabled="true"/>
                        <g:hiddenField name="${property}" id="valueTotal" value="${total}" />
                    </f:field>

                    <f:field bean="cash" property="comment">
                        <g:textArea name="comment" rows="3" cols="60"/>
                    </f:field>

                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" onclick="return confirm('${message(code: 'default.button.close.confirm.message', default: 'Are you sure?')}');" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
