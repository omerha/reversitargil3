package engine.gamelogic;

import engine.User;
import engine.filesutils.XmlUtils;
import engine.gamesettings.GameDescriptor;
import engine.interfaces.GameModeInterface;
import engine.interfaces.PlayTurnInterface;
import javafx.util.Pair;
import engine.gamelogic.Player;
import javafx.scene.paint.Color;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameManager implements Serializable {
    public static final String REGULAR_MODE = "REGULAR";
    public static final String ISLANDS_MODE = "ISLANDS";
    private int numOfPlayers;
    private Board gameBoard;
    private String gameName;
    private int gameID;
    private String nameOfPlayerWhoCreatedTheGame;
    private int numOfSignedPlayers;
    private Player[] players;
    private GameModeInterface.GameModeLogic gameModeLogic;
    private GameDescriptor gameSettings;
    private int totalNumOfTurns;
    private int totalNumOfTurnsToReplay;
    private boolean isActiveGame;
    private XmlUtils xmlUtils;
    private boolean loadedGame = false;
    List<Color> colors = new ArrayList<>();
    List<String> colorsName = new ArrayList<>();
    private boolean isReplayMode;
    private int totalNumOfTurnsDisplay;

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getNameOfPlayerWhoCreatedTheGame() {
        return nameOfPlayerWhoCreatedTheGame;
    }

    public void setNameOfPlayerWhoCreatedTheGame(String nameOfPlayerWhoCreatedTheGame) {
        this.nameOfPlayerWhoCreatedTheGame = nameOfPlayerWhoCreatedTheGame;
    }

    public int getTotalNumOfTurnsDisplay() {
        return totalNumOfTurnsDisplay;
    }



    public void setTotalNumOfTurnsDisplay(int totalNumOfTurnsDisplay) {
        this.totalNumOfTurnsDisplay = totalNumOfTurnsDisplay;
    }

    public int getNumOfSignedPlayers() {
        return numOfSignedPlayers;

    }

    public void setNumOfSignedPlayers(int numOfSignedPlayers) {
        this.numOfSignedPlayers = numOfSignedPlayers;
        if (numOfPlayers == numOfSignedPlayers) {
            isActiveGame = true;
        }

    }
    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public boolean isReplayMode() {
        return isReplayMode;
    }

    public void setReplayMode(boolean replayMode) {
        isReplayMode = replayMode;
    }

    public boolean isLoadedGame() {
        return loadedGame;
    }

    public void setGameSettings(GameDescriptor gameSettings) {
        this.gameSettings = gameSettings;
    }

    public void setLoadedGame(boolean loadedGame) {
        this.loadedGame = loadedGame;
    }

    public boolean isActiveGame() {
        return isActiveGame;
    }

    public void setActiveGame(boolean activeGame) {
        isActiveGame = activeGame;
    }

    PlayTurnInterface.playComputerTurn computerTurn;

    public int getNumOfPlayers() {
        return numOfPlayers;
    }


    public Board getGameBoard() {
        return gameBoard;
    }

    private void setColors() {
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.BLACK);
        colors.add(Color.PINK);
    }

    private void setColorsName() {
        colorsName.add("Blue");
        colorsName.add("Red");
        colorsName.add("Black");
        colorsName.add("Pink");
    }

    public void saveGame(String filePath) throws IOException {
        File file = new File(filePath);
        FileOutputStream fileOut = null;
        try {
            file.createNewFile();
            fileOut = new FileOutputStream(file, false);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }

    public static GameManager loadGame(String path) throws IOException {

        GameManager outPut = null;
        FileInputStream fileIn = null;
        ObjectInputStream in = null;
        try {
            fileIn = new FileInputStream(path);
            in = new ObjectInputStream(fileIn);
            outPut = (GameManager) in.readObject();
            fileIn.close();
            in.close();
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        outPut.setActiveGame(false);
        outPut.setLoadedGame(true);
        return outPut;
    }

    public StringBuilder createHistoryOfMoves() {
        StringBuilder res = new StringBuilder();
        int secondPlayerSize = players[1].getPlayedMovesArrayList().size();
        for (int index = 0; index < players[0].getPlayedMovesArrayList().size(); index++) {
            res.append("Turn number: " + (index + 1) + System.lineSeparator());
            res.append("Player 1 move: row - " + players[0].getPlayedMovesArrayList().get(index).getKey() + " col - " + players[0].getPlayedMovesArrayList().get(index).getValue() + System.lineSeparator());
            if (index < secondPlayerSize) {
                res.append("Player 2 move: row - " + players[1].getPlayedMovesArrayList().get(index).getKey() + " col - " + players[1].getPlayedMovesArrayList().get(index).getValue() + System.lineSeparator());
            }
        }
        if (res.length() == 0) {
            res.append("No moves were performed");
        }
        return res;
    }

    public StringBuilder displayGameBoardAndStats(GameManager gameManager) {

        StringBuilder outPut = new StringBuilder();
        for (int i = 0; i < players.length; i++) {
            //   outPut.append("Player number " + (i + 1) + " sign: " + gameManager.getPlayers()[i].getPlayerSign() + System.lineSeparator());
        }
        outPut.append("Game mode: " + gameManager.getGameSettings().getGame().getVariant() + System.lineSeparator());
        outPut.append("Is game active: " + (gameManager.isActiveGame() ? "Yes" : "No") + System.lineSeparator());
        if (gameManager.isActiveGame()) {
            int currentPlayerTurnIndex = gameManager.getTotalNumOfTurns() % players.length;
            outPut.append("Current turn: Player number " + (currentPlayerTurnIndex + 1) + System.lineSeparator());
            for (int i = 0; i < players.length; i++) {
                outPut.append("Player number " + (i + 1) + " stats:" + System.lineSeparator());
                outPut.append("Turns played: " + gameManager.getPlayers()[i].getNumOfMoves() + System.lineSeparator());
                outPut.append("Average points per turn: " + gameManager.getPlayers()[i].getAvgPointsPerTurn() + System.lineSeparator());
                outPut.append("Points: " + gameManager.getPlayers()[i].getPoints() + System.lineSeparator());
            }
        }
        return outPut;
    }

    public void loadGameSettingsFromXML(String xml) throws Exception {
        // computerTurn = i_ComputerFunc;
        isActiveGame = false;
        xmlUtils = new XmlUtils();
        gameSettings = xmlUtils.getGameSettingsObjectFromFile(xml);
        numOfPlayers = gameSettings.getDynamicPlayers().getTotalPlayers();

        setGameLogic();
        setPlayers();
        gameName = gameSettings.getDynamicPlayers().getGameTitle();
        gameBoard = new Board();
        gameBoard.initBoardHelper(gameSettings, players, gameModeLogic);
    }

    public int numOfHumanPlayers() {
        int result = 0;
        for (Player player : players) {
            if (!player.isComputer()) {
                result += 1;
            }
        }
        return result;
    }

    public void retirePlayerFromGame() {
        int currentPlayersTurnIndex = totalNumOfTurns % players.length;
        players[currentPlayersTurnIndex].setRetiredFromGame(true);
        try {
            this.setTotalNumOfTurns(totalNumOfTurns + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        numOfPlayers -= 1;
    }

    public void setPlayers() throws Exception {
        setColors();
        setColorsName();
        players = new Player[numOfPlayers];
        List<BigInteger> idList = new ArrayList<>();
        String isComputer;
        Random rand = new Random();


        for (int i = 0; i < numOfPlayers; i++) {
            players[i] = new Player();
            players[i].setPlayerColor(colors.get(i));
            players[i].setPlayerColorName(colorsName.get(i));
        }
    }

    public boolean putUserInPlayer(User user) {
        boolean res = false;
        for (int i = 0; i < players.length && !res; i++) {
            if (players[i].getPlayerName() == null) {
                players[i].setPlayerName(user.getName());
                user.setInGameNumber(gameID);
                user.setPlayerID(i);

                setNumOfSignedPlayers(numOfSignedPlayers+1);
             res=true;
            }
        }
        return res;
    }

    private void setGameLogic() {
        if (gameSettings.getGame().getVariant().toUpperCase().equals(REGULAR_MODE)) {
            gameModeLogic = (Serializable & GameModeInterface.GameModeLogic) (rowNewMove, colNewMove, gameBoard) -> {
                boolean res = false;
                if (gameBoard.isEmpty()) {
                    res = true;
                }
                int minRow = rowNewMove > 1 ? rowNewMove - 1 : rowNewMove;
                int maxRow = rowNewMove < gameBoard.getRows() ? rowNewMove + 1 : rowNewMove;
                int minCol = colNewMove > 1 ? colNewMove - 1 : colNewMove;
                int maxCol = colNewMove < gameBoard.getCols() ? colNewMove + 1 : colNewMove;
                if (gameBoard.getGameBoard()[rowNewMove][colNewMove] == Board.EMPTY_SIGN) {
                    for (int iRow = minRow; iRow <= maxRow && !res; iRow++) {
                        for (int iCol = minCol; iCol <= maxCol && !res; iCol++) {
                            if ((gameBoard.getGameBoard()[iRow][iCol] != Board.EMPTY_SIGN)) {
                                res = true;
                            }
                        }
                    }
                } else {
                    throw new Exception("Move is illegal, position is not empty");
                }
                if (!res) {
                    throw new Exception("Move is illegal, new move must be near existing pieces");
                }
                return res;
            };
        } else {

            gameModeLogic = (Serializable & GameModeInterface.GameModeLogic) (row, col, board) -> {
                boolean res = false;
                if (board.getGameBoard()[row][col] == Board.EMPTY_SIGN) {
                    res = true;
                }
                if (!res) {
                    throw new Exception("Move is illegal, position is not empty");
                }
                return res;
            };
        }
    }

    public int getTotalNumOfTurns() {
        return totalNumOfTurns;
    }

    public void setTotalNumOfTurns(int totalNumOfTurns) throws Exception {
        this.totalNumOfTurns = totalNumOfTurns;
        boolean retiredFromGame = true;
        while (retiredFromGame) {
            if (players[this.totalNumOfTurns % players.length].isRetiredFromGame()) {
                this.totalNumOfTurns += 1;
            } else {
                retiredFromGame = false;
            }
        }
        if (players[totalNumOfTurns % players.length].isComputer() && !isReplayMode) {

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            try {
                                computerTurn.computerTurn();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    1000
            );
        }
    }

    private int displayPossibleMovesScoreHelper(int rowMove, int colMove, int playerIndex) {
        int possiblePoints = 0;
        try {
            if (gameModeLogic.checkIfTurnIsOk(rowMove, colMove, getGameBoard())) {
                possiblePoints = gameBoard.checkForSequence(rowMove, colMove, playerIndex, new Move(), false);
            } else {
                possiblePoints = -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            possiblePoints = -1;
        }
        return possiblePoints;
    }

    public void displayPossibleMovesScore(int playerIndex) {
        int possiblePoints = 0;
        for (int currRow = 1; currRow < getGameBoard().getRows(); currRow++) {
            for (int currCol = 0; currCol < getGameBoard().getCols(); currCol++) {
                possiblePoints = displayPossibleMovesScoreHelper(currRow, currCol, playerIndex);
                System.out.println("Row: " + currRow + "; Col: " + currCol + "; possiblePoints: " + possiblePoints + ";");
            }
        }
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }


    public GameDescriptor getGameSettings() {
        return gameSettings;
    }

    public Move playComputerTurn() {
        int playerIndex = totalNumOfTurns % numOfPlayers;
        int colToPutNewMove = 0, rowToPutNewMove = 0;
        Random rand = new Random();
        Move move;
        int pointsToAdd = 0;
        boolean turnIsValid = false;
        while (!turnIsValid) {
            colToPutNewMove = rand.nextInt(gameBoard.getCols()) + 1;
            rowToPutNewMove = rand.nextInt(gameBoard.getRows()) + 1;
            try {
                turnIsValid = checkIfTurnIsValidAndPerform(rowToPutNewMove, colToPutNewMove, playerIndex, true);
            } catch (Exception e) {

            }
        }
        move = new Move(rowToPutNewMove, colToPutNewMove);
        return move;
    }

    public void makeATurnForReplay() {
        int playerIndex = totalNumOfTurns % players.length;
        players[playerIndex].setNumOfCurrentMoveForReplay(players[playerIndex].getNumOfCurrentMoveForReplay() + 1);
        Move moveToDo = players[playerIndex].getPlayerMovesHistoryList().get(players[playerIndex].getNumOfCurrentMoveForReplay());
        try {
            checkIfTurnIsValidAndPerform(moveToDo.getMoveLocation().getKey(), moveToDo.getMoveLocation().getValue(), playerIndex, false);
        } catch (Exception e) {

        }
        totalNumOfTurns += 1;
    }

    public void setNumOfCurrentMoveForPlayers() {
        totalNumOfTurnsToReplay = totalNumOfTurns;
        for (int i = 0; i < players.length; i++) {
            players[i].setNumOfCurrentMoveForReplay(players[i].getPlayerMovesHistoryList().size() - 1);
        }
    }

    public boolean checkIfTurnIsValidAndPerform(int rowNewMove, int colNewMove, int playerIndex, boolean shouldAddMove) throws Exception {
        boolean res = false;
        Move currMove;
        if (gameModeLogic.checkIfTurnIsOk(rowNewMove, colNewMove, getGameBoard())) {
            res = true;
            currMove = new Move(rowNewMove, colNewMove);
            gameBoard.getGameBoard()[rowNewMove][colNewMove] = players[playerIndex].getPlayerNum();
            int pointsTurn = gameBoard.checkForSequence(rowNewMove, colNewMove, players[playerIndex].getPlayerNum(), currMove, true);
            players[playerIndex].setPointsAndCalculateAverage(pointsTurn);
            decreaseRivalsPoints(currMove);
            players[playerIndex].setNumOfMoves(players[playerIndex].getNumOfMoves() + 1);
            if (shouldAddMove) {
                players[playerIndex].getPlayerMovesHistoryList().add(currMove);
                players[playerIndex].getPlayedMovesArrayList().add(new Pair(rowNewMove, colNewMove));
            }
        }
        return res;
    }

    public void undoTurn(boolean shouldRemoveMove) throws Exception {
        if (totalNumOfTurns > 0) {
            setTotalNumOfTurns(totalNumOfTurns - 1);
            totalNumOfTurnsDisplay -= 1;

            int lastPlayedPlayerIndex = totalNumOfTurns % numOfPlayers;
            Move lastMove = null;
            if (shouldRemoveMove) {
                lastMove = players[lastPlayedPlayerIndex].getPlayerMovesHistoryList().get(players[lastPlayedPlayerIndex].getPlayerMovesHistoryList().size() - 1);
                totalNumOfTurnsToReplay -= 1;
            } else {
                lastMove = players[lastPlayedPlayerIndex].getPlayerMovesHistoryList().get(players[lastPlayedPlayerIndex].getNumOfCurrentMoveForReplay());
            }
            gameBoard.undoPositionsAndAddPointsBack(lastMove, players);
            players[lastPlayedPlayerIndex].undoTurn(shouldRemoveMove);
        } else {
            throw new Exception("No turns to undo");
        }

    }

    public int getWinnerIndex() {
        int maxPoints = players[0].getPoints();
        int currIndex = 0;
        for (int i = 1; i < players.length; i++) {
            if (players[i].getPoints() > maxPoints) {
                maxPoints = players[i].getPoints();
                currIndex = i;
            }
        }
        return currIndex;
    }

    public void decreaseRivalsPoints(Move currMove) {
        for (Pair<Integer, Pair<Integer, Integer>> curr : currMove.getFlippedPiecesLocationsList()) {
            players[curr.getKey() - 1].decreasePoints(1);
        }
    }

    public int getTotalNumOfTurnsToReplay() {
        return totalNumOfTurnsToReplay;
    }

    public void setTotalNumOfTurnsToReplay(int totalNumOfTurnsToReplay) {
        this.totalNumOfTurnsToReplay = totalNumOfTurnsToReplay;
    }
}
