$(function () { // onload...do
    $("#loginForm").submit(function () {

        var parameters = $(this).serialize();


        $.ajax({
            data: parameters,
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
