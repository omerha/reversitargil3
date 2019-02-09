package engine.gamelogic;

import engine.gamesettings.GameDescriptor;
import engine.gamesettings.Position;
import engine.interfaces.GameModeInterface;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board implements Serializable {
    public static final char FIRST_PLAYER_SIGN = 'X';
    public static final char SECOND_PLAYER_SIGN = 'O';
    public static final int EMPTY_SIGN = 0;
    public static final int MIN_ROW_VALUE = 1;
    public static final int MIN_COL_VALUE = 1;
    private int[][] gameBoard;
    private int rows, cols;

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int[][] getGameBoard() {
        return gameBoard;
    }

    public void undoPositionsAndAddPointsBack(Move lastMove, Player[] players) {
        gameBoard[lastMove.getMoveLocation().getKey()][lastMove.getMoveLocation().getValue()] = EMPTY_SIGN;
        for (Pair<Integer, Pair<Integer, Integer>> currPosition : lastMove.getFlippedPiecesLocationsList()) {
            gameBoard[currPosition.getValue().getKey()][currPosition.getValue().getValue()] = currPosition.getKey();
            players[currPosition.getKey()-1].addPoints(1);
        }
    }

    public void initBoardHelper(GameDescriptor gameSettings, Player[] players, GameModeInterface.GameModeLogic gameModeLogic) throws Exception {
        boolean enoughEmptyPositions = false;
        int totalStartPositions = 0;
        initBoard(gameSettings, players, gameModeLogic);
        for (int i = 0; i < players.length; i++) {
            totalStartPositions += players[i].getPoints();
        }
        enoughEmptyPositions = ((cols * rows) - totalStartPositions) % players.length == 0;
        if (!enoughEmptyPositions) {
            throw new Exception("The initialized board does not have equal number of empty spots for each player, please try again");
        }
    }

    private void initBoard(GameDescriptor gameSettings, Player[] players, GameModeInterface.GameModeLogic gameModeLogic) throws Exception {

        rows = gameSettings.getGame().getBoard().getRows();
        cols = gameSettings.getGame().getBoard().getColumns();
        gameBoard = new int[rows + 1][cols + 1];
        Position currPosition = null;
        setEmptyPieces(gameBoard);
        for (int numOfPlayer = 0; numOfPlayer < gameSettings.getGame().getInitialPositions().getParticipant().size(); numOfPlayer++) {
            for (int numOfMove = 0; numOfMove < gameSettings.getGame().getInitialPositions().getParticipant().get(numOfPlayer).getPosition().size(); numOfMove++) {
                players[numOfPlayer].addPoints(1);
                currPosition = gameSettings.getGame().getInitialPositions().getParticipant().get(numOfPlayer).getPosition().get(numOfMove);
                validateStartPositions(currPosition, numOfPlayer, gameModeLogic);
                gameBoard[currPosition.getRow()][currPosition.getColumn()] = numOfPlayer + 1;
            }
        }
    }

    private void validateStartPositions(Position position, int playerIndex, GameModeInterface.GameModeLogic gameModeLogic) throws Exception {
        if (position.getColumn() > cols || position.getColumn() < MIN_COL_VALUE || position.getRow() < MIN_ROW_VALUE || position.getRow() > rows) {
            throw new Exception("The start position for player number " + (playerIndex + 1) + " is out of the board limits");
        }
        try {
            gameModeLogic.checkIfTurnIsOk(position.getRow(), position.getColumn(), this);
        } catch (Exception e) {
            throw new Exception("The start position for player number " + (playerIndex + 1) + " is not possible\n" + e.getMessage());
        }

    }

    public void setEmptyPieces(int[][] board) {
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                board[i][j] = EMPTY_SIGN;
            }
        }
    }

    public boolean isEmpty() {
        boolean res = true;
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols && res; j++) {
                if (gameBoard[i][j] != EMPTY_SIGN) {
                    res = false;
                }
            }
        }
        return res;
    }

    public boolean isFullBoard() {
        boolean isFull = true;
        for (int row = 1; row <= rows && isFull; row++) {
            for (int col = 1; col <= cols && isFull; col++) {
                if (gameBoard[row][col] == EMPTY_SIGN) {
                    isFull = false;
                }
            }
        }
        return isFull;
    }

    public int checkForSequence(int row, int col, int sign, Move currMove) {
        return checkRowForSequence(row, col, sign, currMove) + checkColForSequence(row, col, sign, currMove) + checkDiagonalForSequence(row, col, sign, currMove);
    }

    public int checkRowForSequence(int row, int col, int sign, Move currMove) {
        int numOfFlippedPiecesRight = 0, numOfFLippedPiecesLeft = 0;
        boolean finishedCounting = false;
        List<Pair<Integer, Pair<Integer, Integer>>> tempFlippedPiecesList = new ArrayList<>();
        int[] tempRow = Arrays.copyOf(gameBoard[row], gameBoard[row].length);
        for (int currCol = col + 1; currCol <= cols && !finishedCounting; currCol++) {
            // System.out.println("Checking cell row: " + row + " Col: " + currCol);
            if (tempRow[currCol] == sign) {
                finishedCounting = true;
            } else if (tempRow[currCol] != sign && tempRow[currCol] != EMPTY_SIGN) {
                tempFlippedPiecesList.add(new Pair<>(tempRow[currCol], new Pair<>(row, currCol)));
                tempRow[currCol] = sign;
                numOfFlippedPiecesRight++;
            } else {
                finishedCounting = true;
                numOfFlippedPiecesRight = 0;
            }
        }
        if (!finishedCounting) {
            numOfFlippedPiecesRight = 0;
        }
        if (numOfFlippedPiecesRight == 0) {
            tempRow = Arrays.copyOf(gameBoard[row], gameBoard[row].length);
        } else {
            gameBoard[row] = Arrays.copyOf(tempRow, gameBoard[row].length);
            currMove.getFlippedPiecesLocationsList().addAll(tempFlippedPiecesList);
        }
        tempFlippedPiecesList.clear();
        finishedCounting = false;
        for (int currCol = col - 1; currCol >= 1 && !finishedCounting; currCol--) {
            //  System.out.println("Checking cell row: " + row + " Col: " + currCol);
            if (tempRow[currCol] == sign) {
                finishedCounting = true;
            } else if (tempRow[currCol] != sign && tempRow[currCol] != EMPTY_SIGN) {
                tempFlippedPiecesList.add(new Pair<>(tempRow[currCol], new Pair<>(row, currCol)));
                tempRow[currCol] = sign;
                numOfFLippedPiecesLeft++;
            } else {
                finishedCounting = true;
                numOfFLippedPiecesLeft = 0;
            }
        }
        if (!finishedCounting) {
            numOfFLippedPiecesLeft = 0;
        }
        if (numOfFLippedPiecesLeft != 0) {
            gameBoard[row] = tempRow;
            currMove.getFlippedPiecesLocationsList().addAll(tempFlippedPiecesList);
        }
        return numOfFlippedPiecesRight + numOfFLippedPiecesLeft;
    }

    public int[][] cloneArray(int[][] src) {
        int[][] output = new int[rows + 1][cols + 1];
        for (int i = 1; i < rows + 1; i++) {
            for (int j = 1; j < cols + 1; j++) {
                output[i][j] = src[i][j];
            }
        }
        return output;
    }

    public int checkColForSequence(int row, int col, int sign, Move currMove) {
        int numOfFlippedPiecesUp = 0, numOfFlippedPiecesDown = 0;
        List<Pair<Integer, Pair<Integer, Integer>>> tempFlippedPiecesList = new ArrayList<>();
        boolean finishedCounting = false;
        int[][] tempBoard = cloneArray(gameBoard);
        for (int currRow = row + 1; currRow <= rows && !finishedCounting; currRow++) {
            //  System.out.println("Checking cell row: " + currRow + " Col: " + col);
            if (tempBoard[currRow][col] == sign) {
                finishedCounting = true;
            } else if (tempBoard[currRow][col] != sign && tempBoard[currRow][col] != EMPTY_SIGN) {
                tempFlippedPiecesList.add(new Pair<>(tempBoard[currRow][col], new Pair<>(currRow, col)));
                tempBoard[currRow][col] = sign;
                numOfFlippedPiecesDown++;
            } else {
                finishedCounting = true;
                numOfFlippedPiecesDown = 0;
            }
        }
        if (!finishedCounting) {
            numOfFlippedPiecesDown = 0;
        }
        if (numOfFlippedPiecesDown == 0) {
            tempBoard = cloneArray(gameBoard);
        } else {
            gameBoard = cloneArray(tempBoard);
            currMove.getFlippedPiecesLocationsList().addAll(tempFlippedPiecesList);
        }
        tempFlippedPiecesList.clear();
        finishedCounting = false;
        for (int currRow = row - 1; currRow >= 1 && !finishedCounting; currRow--) {
            //   System.out.println("Checking cell row: " + currRow + " Col: " + col);
            if (tempBoard[currRow][col] == sign) {
                finishedCounting = true;
            } else if (tempBoard[currRow][col] != sign && tempBoard[currRow][col] != EMPTY_SIGN) {
                tempFlippedPiecesList.add(new Pair<>(tempBoard[currRow][col], new Pair<>(currRow, col)));
                tempBoard[currRow][col] = sign;
                numOfFlippedPiecesUp++;
            } else {
                finishedCounting = true;
                numOfFlippedPiecesUp = 0;
            }
        }
        if (!finishedCounting) {
            numOfFlippedPiecesUp = 0;
        }
        if (numOfFlippedPiecesUp != 0) {
            gameBoard = cloneArray(tempBoard);
            currMove.getFlippedPiecesLocationsList().addAll(tempFlippedPiecesList);
        }
        return numOfFlippedPiecesDown + numOfFlippedPiecesUp;
    }

    public int checkDiagonalForSequence(int row, int col, int sign, Move currMove) {
        int numOfFlippedPiecesUp = 0, numOfFlippedPiecesDown = 0;
        List<Pair<Integer, Pair<Integer, Integer>>> tempFlippedPiecesList = new ArrayList<>();
        boolean finishedCounting = false;
        int currRow, currCol;
        int[][] tempBoard = cloneArray(gameBoard);
        for (currRow = row == rows ? row : row + 1, currCol = col == cols ? col : col + 1; currCol <= cols && currRow <= rows && !finishedCounting; currRow++, currCol++) {
            //     System.out.println("Checking cell row: " + currRow + " Col: " + currCol);
            if (tempBoard[currRow][currCol] == sign) {
                finishedCounting = true;
            } else if (tempBoard[currRow][currCol] != sign && tempBoard[currRow][currCol] != EMPTY_SIGN) {
                tempFlippedPiecesList.add(new Pair<>(tempBoard[currRow][currCol], new Pair<>(currRow, currCol)));
                tempBoard[currRow][currCol] = sign;
                numOfFlippedPiecesDown++;
            } else {
                finishedCounting = true;
                numOfFlippedPiecesDown = 0;
            }
        }
        if (!finishedCounting) {
            numOfFlippedPiecesDown = 0;
        }
        if (numOfFlippedPiecesDown == 0) {
            tempBoard = cloneArray(gameBoard);
        } else {
            gameBoard = cloneArray(tempBoard);
            currMove.getFlippedPiecesLocationsList().addAll(tempFlippedPiecesList);
        }
        tempFlippedPiecesList.clear();
        finishedCounting = false;
        for (currRow = row == 1 ? row : row - 1, currCol = col == 1 ? col : col - 1; currRow >= 1 && currCol >= 1 && !finishedCounting; currRow--, currCol--) {
            //   System.out.println("Checking cell row: " + currRow + " Col: " + currCol);
            if (tempBoard[currRow][currCol] == sign) {
                finishedCounting = true;
            } else if (tempBoard[currRow][currCol] != sign && tempBoard[currRow][currCol] != EMPTY_SIGN) {
                tempFlippedPiecesList.add(new Pair<>(tempBoard[currRow][currCol], new Pair<>(currRow, currCol)));
                tempBoard[currRow][currCol] = sign;
                numOfFlippedPiecesUp++;
            } else {
                finishedCounting = true;
                numOfFlippedPiecesUp = 0;
            }
        }
        if (!finishedCounting) {
            numOfFlippedPiecesUp = 0;
        }
        if (numOfFlippedPiecesUp != 0) {
            gameBoard = cloneArray(tempBoard);
            currMove.getFlippedPiecesLocationsList().addAll(tempFlippedPiecesList);
        }
        return numOfFlippedPiecesDown + numOfFlippedPiecesUp + checkDiagonalsForSequence(row, col, sign, currMove);
    }

    private int checkDiagonalsForSequence(int row, int col, int sign, Move currMove) {
        int numOfFlippedPiecesUp = 0, numOfFlippedPiecesDown = 0;
        List<Pair<Integer, Pair<Integer, Integer>>> tempFlippedPiecesList = new ArrayList<>();
        boolean finishedCounting = false;
        int currRow, currCol;
        int[][] tempBoard = cloneArray(gameBoard);
        for (currRow = row == rows ? row : row + 1, currCol = col == 1 ? col : col - 1; currCol >= 1 && currRow <= rows && !finishedCounting; currRow++, currCol--) {
            //     System.out.println("Checking cell row: " + currRow + " Col: " + currCol);
            if (tempBoard[currRow][currCol] == sign) {
                finishedCounting = true;
            } else if (tempBoard[currRow][currCol] != sign && tempBoard[currRow][currCol] != EMPTY_SIGN) {
                tempFlippedPiecesList.add(new Pair<>(tempBoard[currRow][currCol], new Pair<>(currRow, currCol)));
                tempBoard[currRow][currCol] = sign;
                numOfFlippedPiecesDown++;
            } else {
                finishedCounting = true;
                numOfFlippedPiecesDown = 0;
            }
        }
        if (!finishedCounting) {
            numOfFlippedPiecesDown = 0;
        }
        if (numOfFlippedPiecesDown == 0) {
            tempBoard = cloneArray(gameBoard);
        } else {
            gameBoard = cloneArray(tempBoard);
            currMove.getFlippedPiecesLocationsList().addAll(tempFlippedPiecesList);
        }
        tempFlippedPiecesList.clear();
        finishedCounting = false;
        for (currRow = row == 1 ? row : row - 1, currCol = col == cols ? col : col + 1; currRow >= 1 && currCol <= cols && !finishedCounting; currRow--, currCol++) {
            //   System.out.println("Checking cell row: " + currRow + " Col: " + currCol);
            if (tempBoard[currRow][currCol] == sign) {
                finishedCounting = true;
            } else if (tempBoard[currRow][currCol] != sign && tempBoard[currRow][currCol] != EMPTY_SIGN) {
                tempFlippedPiecesList.add(new Pair<>(tempBoard[currRow][currCol], new Pair<>(currRow, currCol)));
                tempBoard[currRow][currCol] = sign;
                numOfFlippedPiecesUp++;
            } else {
                finishedCounting = true;
                numOfFlippedPiecesUp = 0;
            }
        }
        if (!finishedCounting) {
            numOfFlippedPiecesUp = 0;
        }
        if (numOfFlippedPiecesUp != 0) {
            gameBoard = cloneArray(tempBoard);
            currMove.getFlippedPiecesLocationsList().addAll(tempFlippedPiecesList);
        }
        return numOfFlippedPiecesDown + numOfFlippedPiecesUp;
    }
}


