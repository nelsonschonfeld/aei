<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'userRole.label', default: 'UserRole')}" />
        <title><g:message code="userRole.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#list-userRole" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="create" action="create"><g:message code="userRole.create.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="list-userRole" class="content scaffold-list" role="main">
            <h1><g:message code="userRole.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <f:table collection="${userRoleList}" properties="['id', 'user', 'role']" />

            <g:if test="${userRoleCount > 20}">
                <div class="pagination">
                    <g:paginate total="${userRoleCount ?: 0}" />
                </div>
            </g:if>
        </div>
    </body>
</html>