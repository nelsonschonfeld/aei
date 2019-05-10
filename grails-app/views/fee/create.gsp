<%@ page import="aei.Person; aei.Inscription" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'fee.label', default: 'Fee')}"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>
    <script type="text/javascript">
        function getStudentForCourse(idInscription) {
            $.ajax({
                type: 'POST',
                url: '/fee/findStudentForCourse',
                data: {idInscription: idInscription},
                success: function (resp) {
                    $("#studentsInscriptos").html(resp);
                }
            });
        }
        function getCourseDetails(idInscrip) {
            $.ajax({
                type: 'POST',
                url: '/fee/findCourseDetail',
                data: {idInscrip: idInscrip},
                success: function (resp) {
                    $("#courseAmount").val(resp.courseAmount);
                    //examen
                    $("#courseTestCost").val(resp.courseTestCost);
                    $("#spanCourseTestCost").text(" $ " + resp.coursePrintCost);
                    //costo de impresión
                    $("#coursePrintCost").val(resp.coursePrintCost);
                    $("#spanCoursePrintCost").text(" $ " + resp.coursePrintCost);
                    //Horario
                    if (resp.courseSchedule !== undefined && resp.courseSchedule !== null) {
                        $("#spanCourseSchedule").text(resp.courseSchedule);
                    }
                    $("#spanCourseDays").text(resp.courseDays);
                    $("#spanCourseStatus").text(resp.courseStatus);
                }
            });
        }
    </script>
</head>

<body>
<a href="#create-fee" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                            default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="create-fee" class="content scaffold-create" role="main">
    <h1><g:message code="default.create.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${this.fee}">
        <ul class="errors" role="alert">
            <g:eachError bean="${this.fee}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                        error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>

    <g:form action="save">
        <fieldset class="form">
            <div class="fieldcontain required">
                <label>Cursos Inscriptos
                    <span class="required-indicator">*</span>
                </label>
                <g:select
                        id="inscriptionsSelect"
                        name="inscriptionsSelect"
                        from="${aei.Inscription.list().unique{courseIns -> courseIns.course}}"
                        optionKey="id"
                        optionValue="courseId"
                        noSelection="['': 'Cursos Inscriptos']"
                        onchange="getStudentForCourse(this.value);getCourseDetails(this.value)"/>
            </div>

            <div class="fieldcontain required">
                <label>Alumnos del Curso
                    <span class="required-indicator">*</span>
                </label>
                <span id="studentsInscriptos"></span>
            </div>
            <div class="fieldcontain required">
                <label>Cuota
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="courseAmount" id="courseAmount"></g:textField>
            </div>
            <div class="fieldcontain required">
                <label>Monto de Examen</label>
                <g:checkBox name="checkCourseTestCost"/>
                <span id="spanCourseTestCost"></span>
                <g:hiddenField name="courseTestCost" id="courseTestCost"/>
            </div>
            <div class="fieldcontain required">
                <label>Costo de Reimpresión</label>
                <g:checkBox name="checkCoursePrintCost"/>
                <span id="spanCoursePrintCost"></span>
                <g:hiddenField name="coursePrintCost" id="coursePrintCost"/>
            </div>
            <div class="fieldcontain required">
                <label>Horario</label>
                <span id="spanCourseSchedule"></span>
            </div>
            <div class="fieldcontain required">
                <label>Días de Cursado</label>
                <span id="spanCourseDays"></span>
            </div>
            <div class="fieldcontain required">
                <label>Estado del Curso</label>
                <b><u><span id="spanCourseStatus"></span></u></b>
            </div>
            <f:all bean="fee" except="inscription,student,amount,inscriptionCost,testCost,printCost,status"/>
        </fieldset>
        <fieldset class="buttons">
            <g:submitButton name="create" class="save"
                            value="${message(code: 'default.button.create.label', default: 'Create')}"/>
        </fieldset>
    </g:form>
</div>
</body>
</html>
