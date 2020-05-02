<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'cash.label', default: 'Cash')}" />
        <r:require module="export"/>
        <title><g:message code="default.list.label" args="[entityName]" /></title>
        <asset:javascript src="jquery-1.9.1.js"/>
    </head>
    <body>
        <a href="#list-cash" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>

        <export:formats formats="['csv', 'excel', 'pdf']" />

        <div id="list-cash" class="content scaffold-list" role="main">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>

            <fieldset class="form">
                <g:form action="index" method="GET">
                    <div class="fieldcontain">
                        <label>Desde</label>
                        <g:datePicker id="dateFrom"
                                      name="dateFrom"
                                      value="${params.dateFrom}"
                                      precision="day"
                                      />
                        <label style="display: unset;" >Hasta</label>
                        <g:datePicker id="dateTo"
                                      name="dateTo"
                                      value="${params.dateTo}"
                                      precision="day"
                        />
                        <g:select class="searchSelect"
                                  id="user"
                                  name="user"
                                  from="${aei.User.list()}"
                                  optionKey="id"
                                  optionValue="${username}"
                                  noSelection="['':'Selecciona un usuario']"
                                  value="${params.user}"
                        />
                        <g:submitButton name="search" class="save" style="border-radius: 4px;" value="${message(code: 'default.button.search.label', default: 'Buscar')}" />
                    </div>
                </g:form>
            </fieldset>


            <f:table collection="${cashList}" />

            <div class="pagination">
                <g:paginate total="${cashCount ?: 0}" />
            </div>
        </div>

        <script type="text/javascript">
            $('.menuButton a').click(function() {
                var url = this.href + '&dateFrom_day=' + $('#dateFrom_day').val() +
                            '&dateFrom_month=' + $('#dateFrom_month').val() +
                            '&dateFrom_year=' + $('#dateFrom_year').val() +
                            '&dateTo_day=' + $('#dateTo_day').val() +
                            '&dateTo_month=' + $('#dateTo_month').val() +
                            '&dateTo_year=' + $('#dateTo_year').val() +
                            '&user=' + $('#user').val();
                window.location.href = url;
                return false;
            });
        </script>
    </body>
</html>