$(function () {
setInterval(displayLoggedInUsers,2000);

});
function displayLoggedInUsers(){
    $.ajax(
        {
            type: 'GET',
            url: 'LoginServlet',
            data:{ action:"getUsers"},
            error: function () {
                console.error("Failed to submit");
                console.log("fail");
            },
            success: function (usersMap) {

                var usersTable = $('.usersTable tbody');
                usersTable.empty();
                usersMap.forEach(function(user){
                    var tr = $(document.createElement('tr'));

                    var td = $(document.createElement('td')).text(user);

                    td.appendTo(tr);

                    tr.appendTo(usersTable);
                });
                console.log("Success");
            }
        }
    );
}
function displayAllGames(){
    var gamesTable = $(".gamesTable tbody");
    gamesTable.empty();
    $.ajax({
        url:"GameServlet",
        data:{action:"getAllGames"},
        error:function(error){
            console.log("Error:" +error);
        },
        success:function(Games){
            for (var key in Games) {
                if (Games.hasOwnProperty(key)) {
                    displayGameStats(Games[key]);
                }
            }

        }
    })
}
function displayBoardInPopup(){
    var popupWindow = $(".dialogDiv")[0];
    popupWindow.display = "block";

}
function displayGameStats(gameManager){
    var gamesTable = $(".gamesTable tbody");
    var tr = $(document.createElement('tr'));
    var gamesName = $(document.createElement('td')).text(gameManager.gameName);
    var uploadedName = $(document.createElement('td')).text("TEST");
    var gameType = $(document.createElement('td')).text(gameManager.gameSettings.game.variant);
    var gameStatus = $(document.createElement('td')).text("TEST");
    var numberOfSignedPlayers = $(document.createElement('td')).text(0+"/"+gameManager.numOfPlayers);

    tr.append(gamesName);

    tr.append(uploadedName);
    tr.append(gameType);
    tr.append(gameStatus);
    tr.append(numberOfSignedPlayers);

    tr.click(displayBoardInPopup);
gamesTable.append(tr);

}
function loadGameClicked(event) {
    var file = event.target.files[0];
    var reader = new FileReader();
    //   var creatorName = getUserName();
    reader.readAsText(file);
    reader.onload = function () {
        console.log("Reader on load");
        var content = reader.result;

        $.ajax(
            {
                type: 'POST',
                url: 'GameServlet',
                data:{ file:content},
                error: function () {
                    console.error("Failed to submit");
                    console.log("fail");
                },
                success: function(json){
                    if(json.error){
                        alert(json.text);
                    }
                    else{
                        displayAllGames();
                    }
                }
            }
        );
    };
}
