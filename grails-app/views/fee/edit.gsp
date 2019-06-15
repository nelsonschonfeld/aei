<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'fee.label', default: 'Fee')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#edit-fee" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
                <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="edit-fee" class="content scaffold-edit" role="main">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="alert">${flash.error}</div>
            </g:if>
            <g:hasErrors bean="${this.fee}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.fee}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form resource="${this.fee}" method="PUT">
                <g:hiddenField name="version" value="${this.fee?.version}" />
                <fieldset class="form">
                    <b> CURSO:</b>
                    <f:display bean="fee" property="course"/>
                    <b> ALUMNO:</b>
                    <f:display bean="fee" property="student"/>
                    <b> MES:</b>
                    <f:display bean="fee" property="month"/>
                    <b> MONTO TOTAL A PAGAR: </b>$
                    <f:display bean="fee" property="amountFull"/>
                    <b> ESTADO:</b>
                    <f:display bean="fee" property="status"/>
                    <f:all bean="fee" except="inscription, course,student,amount, amountFull, discountAmount,inscriptionCost,testCost,printCost,year, month, firstExpiredDate, secondExpiredDate, status, extraCost"/>
                </fieldset>
                <fieldset class="buttons">
                    <input class="save" type="submit" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
