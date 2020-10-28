<%@ page import="enums.FeeStatusEnum" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'fee.label', default: 'Fee')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#show-fee" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
                <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="show-fee" class="content scaffold-show" role="main">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <f:display bean="fee" />
            <g:form resource="${this.fee}" method="DELETE">
                <fieldset class="buttons">
                    <g:if test="${this.fee.status == enums.FeeStatusEnum.Iniciado || this.fee.status == enums.FeeStatusEnum.Parcial}">
                        <g:link class="edit" action="edit" resource="${this.fee}"><g:message code="default.button.reprint.label" default="Edit" /></g:link>
                    </g:if>
                    <g:if test="${this.fee.status == enums.FeeStatusEnum.Iniciado || this.fee.status == enums.FeeStatusEnum.Trasladado}">
                        <input class="delete" type="submit" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                    </g:if>
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
