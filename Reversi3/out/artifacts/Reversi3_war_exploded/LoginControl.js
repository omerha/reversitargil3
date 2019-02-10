$(function () { // onload...do
    checkIfUserLoggedIn();
    $("#loginForm").submit(function () {

        var userName = $('.UserNameInput').val();
        var computerFlag = $('.ComputerCheckBox').is(':checked');

        $.ajax({
            data: {action:"login",userName:userName,computerFlag:computerFlag},

            url: "LoginServlet",
            timeout: 2000,
            error: function () {
                console.error("Failed to submit");
                console.log("fail");
            },
            success: function (response) {
              console.log("Success");
               $("#errorLogin").html(response)
                if(!response.length){
                    window.location="Lobby.html";
                }
            }

        });


        return false;
    })
});

function checkIfUserLoggedIn() {
    $.ajax
    ({
        url: 'LoginServlet',
        data: {
            action: "checkIfLogged"
        },
        error: function () {
            console.error("Failed to submit");
            console.log("fail");
        },
        success: function(json){
            if(json.connected){
            window.location="Lobby.html";
            }
        }
    });
}

