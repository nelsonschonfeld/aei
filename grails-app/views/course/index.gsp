<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'course.label', default: 'Course')}"/>
    <r:require module="export"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
    <asset:javascript src="jquery-1.9.1.js"/>
</head>

<body>
<a href="#list-course" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                             default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                              args="[entityName]"/></g:link></li>
    </ul>
</div>

<export:formats formats="['csv', 'excel', 'pdf']" />

<div id="list-course" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <fieldset class="form">
        <g:form action="index" method="GET">
            <div class="fieldcontain">
                <label for="query">Buscar:</label>
                <g:textField name="query" value="${params.query}" placeholder="Nombre,Tipo,Año,Prof."/>
            </div>
        </g:form>
    </fieldset>
    <f:table collection="${courseList}" properties="['id','name','type','year','teacher','amount','schedule','monday','thursday','wednesday','tuesday','friday','saturday','status']"/>

        <div class="pagination">
            <g:paginate total="${courseCount ?: 0}"/>
        </div>
</div>
    <script type="text/javascript">
        $('.menuButton a').click(function() {
            var url = this.href + '&query=' + $('#query').val();
            window.location.href = url;
            return false;
        });
    </script>
</body>
</html>