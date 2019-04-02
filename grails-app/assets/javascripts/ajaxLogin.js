var onLogin;

$.ajaxSetup({
    beforeSend: function(jqXHR, event) {
        if (event.url != $("#ajaxLoginForm").attr("action")) {
            // save the 'success' function for later use if
            // it wasn't triggered by an explicit login click
            onLogin = event.success;
        }
    },
    statusCode: {
        // Set up a global Ajax error handler to handle 401
        // unauthorized responses. If a 401 status code is
        // returned the user is no longer logged in (e.g. when
        // the session times out), so re-display the login form.
        401: function() {
            showLogin();
        }
    }
});

function showLogin() {
    var ajaxLogin = $("#ajaxLogin");
    ajaxLogin.css("text-align", "center");
    ajaxLogin.jqmShow();
}

function logout(event) {
    event.preventDefault();
    $.ajax({
        url: $("#_logout").attr("href"),
        method: "POST",
        success: function(data, textStatus, jqXHR) {
            window.location = "/";
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log("Logout error, textStatus: " + textStatus +
                ", errorThrown: " + errorThrown);
        }
    });
}

function authAjax() {
    $("#loginMessage").html("Sending request ...").show();

    var form = $("#ajaxLoginForm");
    $.ajax({
        url:       form.attr("action"),
        method:   "POST",
        data:      form.serialize(),
        dataType: "JSON",
        success: function(json, textStatus, jqXHR) {
            if (json.success) {
                form[0].reset();
                $("#loginMessage").empty();
                $("#ajaxLogin").jqmHide();
                $("#loginLink").html(
                    'Logged in as ' + json.username +
                    ' (<a href="' + $("#_logout").attr("href") +
                    '" id="logout">Logout</a>)');
                $("#logout").click(logout);
                if (onLogin) {
                    // execute the saved event.success function
                    onLogin(json, textStatus, jqXHR);
                }
            }
            else if (json.error) {
                $("#loginMessage").html('<span class="errorMessage">' +
                    json.error + "</error>");
            }
            else {
                $("#loginMessage").html(jqXHR.responseText);
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            if (jqXHR.status == 401 && jqXHR.getResponseHeader("Location")) {
                // the login request itself wasn't allowed, possibly because the
                // post url is incorrect and access was denied to it
                $("#loginMessage").html('<span class="errorMessage">' +
                    'Sorry, there was a problem with the login request</error>');
            }
            else {
                var responseText = jqXHR.responseText;
                if (responseText) {
                    var json = $.parseJSON(responseText);
                    if (json.error) {
                        $("#loginMessage").html('<span class="errorMessage">' +
                            json.error + "</error>");
                        return;
                    }
                }
                else {
                    responseText = "Sorry, an error occurred (status: " +
                        textStatus + ", error: " + errorThrown + ")";
                }
                $("#loginMessage").html('<span class="errorMessage">' +
                    responseText + "</error>");
            }
        }
    });
}

$(function() {
    $("#ajaxLogin").jqm({ closeOnEsc: true });
    $("#ajaxLogin").jqmAddClose("#cancelLogin");
    $("#ajaxLoginForm").submit(function(event) {
        event.preventDefault();
        authAjax();
    });
    $("#authAjax").click(authAjax);
    $("#logout").click(logout);
});