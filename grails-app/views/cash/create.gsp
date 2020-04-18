<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'cash.label', default: 'Cash')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
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
                            <span><label>- Fecha:</label> ${date}</span>
                        </div>

                        <div>
                            <span><label><u>- Cierre de Caja anterior ($):</u></label> ${preCash}</span>
                        </div>

                        <div>
                            <span><label>- Monto inicial anterior ($):</label> ${preInitialAmount}</span>
                        </div>

                        <div>
                            <span><label>- Gastos anteriores ($):</label> ${preCosts}</span>
                        </div>

                        <div>
                            <span><label>- Comentarios: </label> ${comments}</span>
                        </div>

                    </fieldset>

                    <f:all bean="cash" except="comment" order="initalAmount,costs,total"/>

                    <f:field bean="cash" property="comment">
                        <g:textArea name="comment" rows="3" cols="60"/>
                    </f:field>

                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
