<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.js"></script>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
        <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>
        <g:set var="entityName" value="${message(code: 'payment.label', default: 'Payment')}" />
        <r:require module="export"/>
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#list-payment" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>

    <fieldset class="form">
        <g:form action="index" method="GET">
            <div class="fieldcontain">
                <label>Filtros</label>
        <g:select
                class="searchSelect"
                id="course"
                name="course"
                from="${aei.Course.findAll()}"
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

        <export:formats formats="['excel', 'pdf']" />

        <div id="list-payment" class="content scaffold-list" role="main">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <f:table collection="${paymentList}" properties="['id','course','student','inscription','fee','feeCode','amountPaid','status','dateCreated']"/>

            <div class="pagination">
                <g:paginate total="${paymentCount ?: 0}" />
            </div>
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