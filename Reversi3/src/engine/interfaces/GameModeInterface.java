package engine.interfaces;

import engine.gamelogic.Board;

import java.io.Serializable;

public class GameModeInterface implements Serializable {
    public interface GameModeLogic  {
         boolean checkIfTurnIsOk(int rowNewMove, int colNewMove, Board gameBoard) throws Exception;
    }
}
