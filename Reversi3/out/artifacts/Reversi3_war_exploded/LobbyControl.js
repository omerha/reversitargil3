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
function displayBoardInPopup(gameManager){
    var popupWindow = $(".dialogDiv")[0];
    popupWindow.style.display = "table";

    printBoard(gameManager.gameBoard.rows,gameManager.gameBoard.cols,gameManager.gameBoard.gameBoard);

}
function removeGameDialog(window){
    $(".dialogDiv")[0].style.display = "none";
}
function printBoard(numOfRows,numOfCols,gameBoard){

    var board  = $(".board");
board.empty();
    for(var i = 0 ; i<numOfRows ; i++){
        var rowDiv = $(document.createElement("div"));
        rowDiv.addClass("rowDiv");
        for (var j =0;j<numOfCols;j++ ){
            var squareDiv = $(document.createElement("div"));
            squareDiv.addClass("square");

            switch(gameBoard[i+1][j+1]){
                case 1:
                    squareDiv.css({"background":"red"});
                    break;
                case 2:
                    squareDiv.css({"background":"blue"});
                    break;
                case 3:
                    squareDiv.css({"background":"yellow"});
                    break;
                case 4:
                    squareDiv.css({"background":"black"});
                    break;
                default:
            }
            rowDiv.append(squareDiv);
        }
        board.append(rowDiv);
    }

}
function displayGameStats(gameManager){
    var gamesTable = $(".gamesTable tbody");
    var tr = $(document.createElement('tr'));
    var gamesName = $(document.createElement('td')).text(gameManager.gameName);
    var uploaderName = $(document.createElement('td')).text(gameManager.nameOfPlayerWhoCreatedTheGame);
    var gameType = $(document.createElement('td')).text(gameManager.gameSettings.game.variant);
    var gameStatus = $(document.createElement('td')).text(gameManager.isActiveGame?"Active":"Inactive");
    var numberOfSignedPlayers = $(document.createElement('td')).text(gameManager.numOfSignedPlayers+"/"+gameManager.numOfPlayers);

    tr.append(gamesName);

    tr.append(uploaderName);
    tr.append(gameType);
    tr.append(gameStatus);
    tr.append(numberOfSignedPlayers);

    tr.click(function(){displayBoardInPopup(gameManager)});
gamesTable.append(tr);

}
function onLogoutClick(){

    $.ajax({
        data:{action:"logout"},
        url:"LoginServlet",
        error:function(){
            console.log("Something went wrong while attempting to log out");},
        success:function(){
            window.location="/";}

    });
    return false;
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
                data:{ file:content,action:"uploadGame"},
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
