<span id="logoutLink" style="display: none;">
    <g:link elementId='_logout' controller='logout'>Logout</g:link>
</span>

<span id="loginLink" style="position: relative; margin-right: 30px; float: right">
    <sec:ifLoggedIn>
        Usuario Logueado: <u><b><sec:username/></b></u>
        <br>
        <g:link elementId='logout' controller='logout'> Salir</g:link>
    </sec:ifLoggedIn>
</span>

<div id="ajaxLogin" class="jqmWindow" style="z-index: 3000;">
    <div class="inner">
        <div class="fheader">Por favor identíficate</div>
        <form action="${request.contextPath}/login/authenticate" method="POST"
              id="ajaxLoginForm" name="ajaxLoginForm" class="cssform" autocomplete="off">
            <p>
                <label for="username">Nombre de usuario:</label>
                <input type="text" class="text_" name="username" id="username" />
            </p>
            <p>
                <label for="password">Contraseña:</label>
                <input type="password" class="text_" name="password" id="password" />
            </p>
            <p>
                <label for="remember_me">Recuérdame</label>
                <input type="checkbox" class="chk" id="remember_me" name="remember-me"/>
            </p>
            <p>
                <input type="submit" id="authAjax" name="authAjax"
                       value="Identifícate" class="ajaxLoginButton" />
                <input type="button" id="cancelLogin" value="Cancelar"
                       class="ajaxLoginButton" />
            </p>
        </form>
        <div style="display: none; text-align: left;" id="loginMessage"></div>
    </div>
</div>