package engine.gamelogic;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Move {

    private Pair<Integer, Integer> moveLocation;
    private List<Pair<Integer, Pair<Integer, Integer>>> flippedPiecesLocationsList = new ArrayList<>();//<which player the piece was,<rowLocation,colLocation>>

    public Pair<Integer, Integer> getMoveLocation() {
        return moveLocation;
    }

    public Move() {
    }

    public Move(int row, int col) {
        moveLocation = new Pair<>(row, col);
    }

    public void setMoveLocation(Pair<Integer, Integer> moveLocation) {
        this.moveLocation = moveLocation;
    }

    public List<Pair<Integer, Pair<Integer, Integer>>> getFlippedPiecesLocationsList() {
        return flippedPiecesLocationsList;
    }

    public void setFlippedPiecesLocationsList(List<Pair<Integer, Pair<Integer, Integer>>> flippedPiecesLocationsList) {
        this.flippedPiecesLocationsList = flippedPiecesLocationsList;
    }
}
