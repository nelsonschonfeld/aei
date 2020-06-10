<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'cash.label', default: 'Cash')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
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
        <a href="#edit-cash" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
                <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="edit-cash" class="content scaffold-edit" role="main">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${this.cash}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.cash}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form resource="${this.cash}" method="PUT">
                <g:hiddenField name="version" value="${this.cash?.version}" />
                <fieldset class="form">

                    <f:all bean="cash" except="initalAmount,withdraw,costs,eCollections,income,comment"/>

                    <f:field bean="cash" property="initalAmount">
                        <g:field type="number" min="0" name="${property}" value="${value ? value : 0}" required="" id="initalAmount" onchange="getAmountReturned()"/>
                    </f:field>

                    <f:field bean="cash" property="costs">
                        <g:field type="number" min="0" name="${property}" value="${value ? value : 0}" required="" id="costs" onchange="getAmountReturned()"/>
                    </f:field>

                    <f:field bean="cash" property="withdraw">
                        <g:field type="number" min="0" name="${property}" value="${value ? value : 0}" required="" id="withdraw" onchange="getAmountReturned()"/>
                    </f:field>

                    <f:field bean="cash" property="income">
                        <g:field type="number" min="0" name="${property}" value="${value ? value : 0}" required="" id="income" onchange="getAmountReturned()"/>
                    </f:field>

                    <f:field bean="cash" property="eCollections">
                        <g:field type="number" min="0" name="${property}" value="${value ? value : 0}" required="" id="eCollections" onchange="getAmountReturned()"/>
                    </f:field>

                    <f:field bean="cash" property="comment">
                        <g:textArea name="comment" rows="3" cols="60"/>
                    </f:field>
                </fieldset>
                <fieldset class="buttons">
                    <input class="save" type="submit" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>