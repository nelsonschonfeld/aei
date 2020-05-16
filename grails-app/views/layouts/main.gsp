<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
        <g:layoutTitle default="Grails"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <asset:stylesheet src="application.css"/>

    <g:layoutHead/>
</head>

<body>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header ">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                    data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar">Cursos</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/">
                <asset:image src="aei.png" alt="American English Institue" class="float-left"/>
            </a>
        </div>
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <div class="btn-group btn-group-sm" role="group" aria-label="...">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                           aria-expanded="false">Usuarios <span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="/user/index">Usuarios</a></li>
                            <li><a href="/userRole/index">Perfiles Asignados</a></li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                           aria-expanded="false">Personas <span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="/person/index">Personas</a></li>
                            <li><a href="/person/create">Crear Persona</a></li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                           aria-expanded="false">Cursos <span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="/course/index">Cursos</a></li>
                            <li><a href="/course/create">Crear Curso</a></li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                           aria-expanded="false">Inscripciones <span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="/inscription/index">Inscripciones</a></li>
                            <li><a href="/inscription/create">Crear Inscripción</a></li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                           aria-expanded="false">Cuotas <span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="/fee/index">Cuotas</a></li>
                            <li><a href="/fee/create">Generar Cuotas</a></li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                           aria-expanded="false">Pagos <span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="/payment/index">Pagos</a></li>
                            <li><a href="/payment/create">Generar Pago</a></li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                           aria-expanded="false">Cierre de caja<span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="/cash/index">Cierres de caja</a></li>
                            <li><a href="/cash/create">Generar Cerrar caja</a></li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                           aria-expanded="false">Sistema <span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="/system/export">Generar backup</a></li>
                        </ul>
                    </li>
                </div>
            </ul>
            <g:render template='/includes/ajaxLogin'/>
        </div><!-- /.navbar-collapse -->

    </div><!-- /.container-fluid -->
</nav>

<g:layoutBody/>

<div class="page-header">
    <small>American English Institue - Sistema de gestión</small>
</div>

<div id="spinner" class="spinner" style="display:none;">
    <g:message code="spinner.alt" default="Loading&hellip;"/>
</div>

<asset:javascript src="application.js"/>

</body>
</html>
