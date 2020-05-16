<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <asset:javascript src="jquery-1.9.1.js"/>
        <asset:stylesheet src="select2.min.css"/>
        <asset:javascript src="select2.min.js"/>
        <g:set var="entityName" value="${message(code: 'inscription.label', default: 'Inscription')}" />
        <r:require module="export"/>
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>

    <body>
        <a href="#list-inscription" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="create" action="create"><g:message code="default.new.label.a" args="[entityName]" /></g:link></li>
            </ul>
        </div>

        <export:formats formats="['excel', 'pdf']" />

        <div id="list-inscription" class="content scaffold-list" role="main">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>

            <fieldset class="form">
            <g:form action="index" method="GET">
                <div class="fieldcontain">
                    <label>Filtros</label>
                    <g:select
                            class="searchSelect"
                            id="course"
                            name="course"
                            from="${aei.Course.list()}"
                            optionKey="id"
                            optionValue="id"
                            noSelection="['':'Selecciona un curso']"
                            value="${params.course}"
                    />
                    <g:select class="searchSelect"
                        id="student"
                        name="student"
                        from="${aei.Person.list()}"
                        optionKey="id"
                        optionValue="${name}"
                        noSelection="['':'Selecciona un estudiante']"
                        value="${params.student}"
                    />
                   <g:submitButton name="search" class="save" style="border-radius: 4px;" value="${message(code: 'default.button.search.label', default: 'Buscar')}" />
                </div>
            </g:form>
            </fieldset>

            <f:table collection="${inscriptionList}" properties="['id','course','student','discountAmount']"/>

            %{--<g:if test="${inscriptionCount > 20}">--}%
                <div class="pagination">
                    <g:paginate total="${inscriptionCount ?: 0}" />
                </div>
            %{--</g:if>--}%

            </div>

            <script type="text/javascript">
                $('.menuButton a').click(function() {
                    var url = this.href + '&course=' + $('#course').val() + '&student=' + $('#student').val();
                    window.location.href = url;
                    return false;
                });
            </script>
        </body>
    </html>
