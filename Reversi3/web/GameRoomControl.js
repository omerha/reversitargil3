var playerIndex = -1;
var isMyTurn = false;
var timeInterval = 2000;
var turnsInterval;
var isComputer;
$(function () {

    var initBoardInterval = setInterval(function () {
        initBoardHelper(initBoardInterval);
    }, timeInterval);

});

function initBoardHelper(intervalId) {
    $.ajax({
        url: "GameServlet",
        data: {action: "getGameManager"},
        error: function (error) {
            console.log("Error:" + error);
        },
        success: function (gameManager) {
            var parsedGameManager = JSON.parse(gameManager);
            if (parsedGameManager.isActiveGame) {
                initGame(parsedGameManager);
                clearInterval(intervalId);
                turnsInterval = setInterval(function () {
                    getGameManager(checkWhosTurn);
                }, timeInterval);
            }
            displayNumOfSignedPlayers(parsedGameManager);
        }
    })
}

function checkWhosTurn(gameManager) {
    var label = $(".playerCurrentTurn");
    var currentPlayerIndex = gameManager.totalNumOfTurns % gameManager.numOfPlayers;
    var playerColor = gameManager.players[currentPlayerIndex].playerColorName;
    isMyTurn = currentPlayerIndex == playerIndex;
    if (isMyTurn && label[0].style.backgroundColor == playerColor.toLowerCase()) {

    }
    else {
        label.css("background-color", playerColor);
        label.text("Its " + gameManager.players[currentPlayerIndex].playerName + " turn!");
        if (isMyTurn) {
                shouldBoardBeClickAble(true);
        }
        else {
            shouldBoardBeClickAble(false);
        }
        initGame(gameManager);
    }
}

function setPlayerIndex(gameManager) {

    $.ajax({
        url: "GameServlet",
        data: {action: "getPlayerIndex", gameIndex: gameManager.gameID},
        error: function (error) {
            console.log("Error:" + error);
        },
        success: function (res) {
            playerIndex = parseInt(res);
        }
    })
}

function displayNumOfSignedPlayers(gameManager) {
    var numOfSignedPlayers = gameManager.numOfSignedPlayers;
    var totalNumOfPlayers = gameManager.numOfPlayers;
    var playerUserName;
    $.ajax
    ({
        url: 'LoginServlet',
        data: {
            action: "checkIfLogged"
        },
        error: function () {
            console.error("Failed to get user details");
            console.log("fail");
        },
        success: function (json) {
            playerUserName = json.userName;
            isComputer = json.isComputer;
            $(".userNameSpan").text("Hi " + playerUserName);
            $(".gameStatus").text("There " + numOfSignedPlayers + "\/" + totalNumOfPlayers + " connected players");
        }
    });
}

function initGame(gameManager) {
    printBoard(gameManager);
    fillPlayersTable(gameManager);
    if (playerIndex == -1) {
        setPlayerIndex(gameManager)
    }
}

function shouldBoardBeClickAble(isClickAble) {
    $(".boardBody").css("pointer-events", isClickAble ? "auto" : "none"); //-- makes board unclick able

}

function getGameManager(functionCallBack) {
    $.ajax({
        url: "GameServlet",
        data: {action: "getGameManager"},
        error: function (error) {
            console.log("Error:" + error);
        },
        success: function (gameManager) {
            console.log("ASDFASDF");
            functionCallBack(JSON.parse(gameManager));
        }
    })
}

function fillPlayersTable(gameManager) {
    $(".playersTable tbody").empty();
    var players = gameManager.players;
    players.forEach(function (player) {
        if (player.playerName) {
            addPlayerToBoard(player);
        }
    });
}

function addPlayerToBoard(player) {
    var playersTable = $(".playersTable tbody");
    var tr = $(document.createElement("tr"));
    var playerName = $(document.createElement("td")).text(player.playerName);
    var playerColor = $(document.createElement("td")).text(player.playerColor);
    var playerComputer = $(document.createElement("td")).text(player.isComputer ? "Yes" : "No");
    var playerNumOfMoves = $(document.createElement("td")).text(player.numOfMoves.value);
    var playerPoints = $(document.createElement("td")).text(player.points.value);
    var playerAvgFlippedPoints = $(document.createElement("td")).text(player.avgFlippedPointsToDisplay.value);
    tr.append(playerName);
    tr.append(playerColor);
    tr.append(playerComputer);
    tr.append(playerNumOfMoves);
    tr.append(playerPoints);
    tr.append(playerAvgFlippedPoints);
    tr.find("*").addClass("tdPlayerTable");
    playersTable.after(tr);

}

function printBoard(gameManager) {
    var numOfRows = gameManager.gameBoard.rows;
    var numOfCols = gameManager.gameBoard.cols;
    var gameBoard = gameManager.gameBoard.gameBoard;
    var board = $(".boardBody");
    board.empty();
    for (var i = 0; i < numOfRows; i++) {
        var rowDiv = $(document.createElement("div"));
        rowDiv.addClass("rowDiv");
        for (var j = 0; j < numOfCols; j++) {
            var squareDiv = $(document.createElement("div"));
            squareDiv.addClass("square");
            squareDiv.attr("row", i + 1);
            squareDiv.attr("col", j + 1);
            squareDiv.click(clickedOnGameBoard);
            switch (gameBoard[i + 1][j + 1]) {
                case 1:
                    squareDiv.css({"background": "red"});
                    break;
                case 2:
                    squareDiv.css({"background": "blue"});
                    break;
                case 3:
                    squareDiv.css({"background": "yellow"});
                    break;
                case 4:
                    squareDiv.css({"background": "black"});
                    break;
                default:
            }
            rowDiv.append(squareDiv);
        }
        board.append(rowDiv);
    }
}

function clickedOnGameBoard(event) {
    console.log("clickedongameboard");
    shouldBoardBeClickAble(false);
    var row = event.target.getAttribute("row");
    var col = event.target.getAttribute("col");
    $.ajax({
       url:"GameServlet",
       data:{action:"runHumanTurn",playerIndex:playerIndex,row:row,col:col},
       error:function(err){
           console.log(err);
    },
        success:function(res){
           var parsedRes = JSON.parse(res);
           if(parsedRes.error !=""){
               alert(parsedRes.error);
               shouldBoardBeClickAble(true);

           }
           else if(parsedRes.winner!=-1){

           }
           else {
               getGameManager(initGame);
           }
        }
    });
}