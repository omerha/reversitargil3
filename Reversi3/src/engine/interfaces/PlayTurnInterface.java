package engine.interfaces;

import java.io.Serializable;

public class PlayTurnInterface implements Serializable {
    public interface playComputerTurn  {
        void computerTurn() throws Exception;
    }
}
