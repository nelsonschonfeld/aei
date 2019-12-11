<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <title><g:message code="system.export.title" /></title>
    </head>
    <body>
        <g:form action="save">
            <fieldset class="form">
            </fieldset>
            <fieldset class="buttons">
                <g:submitButton name="create" class="save" value="${message(code: 'system.export.title', default: 'Export')}" />
            </fieldset>
        </g:form>
    </body>
</html>