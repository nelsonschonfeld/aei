<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'inscription.label', default: 'Inscription')}" />
        <title><g:message code="default.create.label.a" args="[entityName]" /></title>
        <script type="text/javascript">
            function findPersons() {

                var query = $('#query').val();

                if (query != '') {
                    $.ajax({
                        type: 'GET',
                        url: '/inscription/findPersons',
                        data: {query: query},
                        success: function (resp) {
                            $("#students").html(resp);
                        }
                    });
                }
            }
        </script>
    </head>
    <body>
        <a href="#create-inscription" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="create-inscription" class="content scaffold-create" role="main">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="alert">${flash.error}</div>
            </g:if>
            <g:hasErrors bean="${this.inscription}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.inscription}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>

            <g:form action="save">

                <fieldset class="form">

                <div class="fieldcontain required">
                    <label>Buscar
                        <span class="required-indicator">*</span>
                    </label>
                    <g:textField name="query"
                                 id="query"
                                 placeholder="Nombre,Apellido,DNI"
                    ></g:textField>
                    <input type="button" id = "search" onclick="findPersons()" value="${message(code: 'default.button.search.label')}"/>
                </div>

                <div class="fieldcontain required">
                    <label>Alumno
                        <span class="required-indicator">*</span>
                    </label>
                    <span id="students"></span>
                </div>

                <div class="fieldcontain required">
                    <label>Cursos Inscriptos
                        <span class="required-indicator">*</span>
                    </label>
                    <g:select
                            id="course"
                            name="course"
                            from="${aei.Course.list()}"
                            optionKey="id"
                            optionValue="id"
                            />
                </div>

                <f:field bean="inscription" property="discountAmount"/>

                </fieldset>

                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </fieldset>

            </g:form>
        </div>
    </body>
</html>
