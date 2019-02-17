package engine.gamelogic;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Pair;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
public class Player implements Serializable {

    private int playerNum;
    private Color playerColor;
    private String playerName = null;
    private int playerID;
    //private int numOfMoves;
   // private int points;
    private IntegerProperty points = new SimpleIntegerProperty(0);
    private IntegerProperty numOfMoves = new SimpleIntegerProperty(0);
    private IntegerProperty avgFlippedPointsToDisplay = new SimpleIntegerProperty(0);
private int numOfCurrentMoveForReplay;
    private int avgPointsPerTurn;
    private int flippedPieces;
    private List<Pair<Integer, Integer>> playedMovesArrayList = new ArrayList<>();
    private List<Move> playerMovesHistoryList = new ArrayList<>();
    private boolean isComputer;
    private String playerColorName;
    private boolean isRetiredFromGame;

    public boolean isRetiredFromGame() {
        return isRetiredFromGame;
    }

    public void setRetiredFromGame(boolean retiredFromGame) {
        isRetiredFromGame = retiredFromGame;
    }

    public String getPlayerColorName() {
        return playerColorName;
    }

    public void setPlayerColorName(String playerColorName) {
        this.playerColorName = playerColorName;
    }

    public IntegerProperty pointsToDisplayProperty() {
        return points;
    }

    public int getAvgFlippedPointsToDisplay() {
        return avgFlippedPointsToDisplay.get();
    }

    public IntegerProperty avgFlippedPointsToDisplayProperty() {
        return avgFlippedPointsToDisplay;
    }

    public void setAvgFlippedPointsToDisplay(int avgFlippedPointsToDisplay) {
        this.avgFlippedPointsToDisplay.set(avgFlippedPointsToDisplay);
    }

    public int getNumOfMoves() {
        return numOfMoves.get();
    }

    public IntegerProperty numOfMovesProperty() {
        return numOfMoves;
    }

    public void setNumOfMoves(int numOfMoves) {
        this.numOfMoves.set(numOfMoves);
    }

    public int getPointsToDisplay() {
        return points.get();
    }



    public void setPointsToDisplay(int pointsToDisplay) {
        this.points.set(pointsToDisplay);
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {

        this.playerID = playerID;

    }

    public List<Move> getPlayerMovesHistoryList() {
        return playerMovesHistoryList;
    }

    public void setPlayerMovesHistoryList(List<Move> playerMovesHistoryList) {
        this.playerMovesHistoryList = playerMovesHistoryList;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isComputer() {
        return isComputer;
    }
public String getIsComputer(){
        return String.valueOf(!isComputer);
}
    public void setComputer(boolean computer) {
        isComputer = computer;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }


    public List<Pair<Integer, Integer>> getPlayedMovesArrayList() {
        return playedMovesArrayList;
    }

    public void setPlayedMovesArrayList(List<Pair<Integer, Integer>> playedMovesArrayList) {
        this.playedMovesArrayList = playedMovesArrayList;
    }

    public int getPoints() {
        return points.get();
    }

    public void addPoints(int points) {
        this.points.set(points+this.points.get());
       // this.points += points;

    }

    public void undoTurn(boolean shouldRemoveMove) {
        int pointsToDecrease = 0;
        if(shouldRemoveMove) {
            pointsToDecrease = playerMovesHistoryList.get(playerMovesHistoryList.size() - 1).getFlippedPiecesLocationsList().size();
        }else{
            pointsToDecrease = playerMovesHistoryList.get(numOfCurrentMoveForReplay).getFlippedPiecesLocationsList().size();
        }
         setNumOfMoves(numOfMoves.get()-1);
        this.points.set(this.points.getValue() - (pointsToDecrease + 1));
        this.flippedPieces -= pointsToDecrease;
        if (numOfMoves.get() != 0) {
            setAvgPointsPerTurn(flippedPieces / numOfMoves.get());

        }
        if(shouldRemoveMove) {
            playerMovesHistoryList.remove(playerMovesHistoryList.size() - 1);
        }
        else{
            numOfCurrentMoveForReplay -=1;
        }
    }

    public void decreasePoints(int points) {
        this.points.set(this.getPoints()- points);
    }

    public void setPointsAndCalculateAverage(int points) {
        this.points.set(this.points.get()+ points + 1);
        flippedPieces += points;
        if (numOfMoves.get() != 0) {
            setAvgPointsPerTurn(flippedPieces / numOfMoves.get());
        }

    }

    public int getAvgPointsPerTurn() {
        return avgPointsPerTurn;
    }

    public void setAvgPointsPerTurn(int avgPointsPerTurn) {

        this.avgPointsPerTurn = avgPointsPerTurn;
        avgFlippedPointsToDisplay.setValue(this.avgPointsPerTurn);
    }


    public void play() {
    }

    public int getNumOfCurrentMoveForReplay() {
        return numOfCurrentMoveForReplay;
    }

    public void setNumOfCurrentMoveForReplay(int numOfCurrentMoveForReplay) {
        this.numOfCurrentMoveForReplay = numOfCurrentMoveForReplay;
    }

    ;
}
