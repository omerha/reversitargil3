package engine;

import engine.gamelogic.GameManager;

import java.util.HashMap;
import java.util.Iterator;

public class GamesManager {
    private static GamesManager gamesManagerInstance = null;
    private static int numOfgames;
    private HashMap<Integer, GameManager> gamesMap;

    private GamesManager() {
        gamesMap = new HashMap<>();
    }

    public static GamesManager getInstance() {
        if (gamesManagerInstance == null) {
            gamesManagerInstance = new GamesManager();

        }
        return gamesManagerInstance;
    }

    public boolean isGameNameExists(String name) {
        boolean res = false;
        for (GameManager value : gamesMap.values()) {
            if (value.getGameName() == name) {
                res = true;
                break;
            }
        }
        return res;
    }
    public void addGame(String xml, String gameCreatorName) throws Exception {
        GameManager newGame = new GameManager();
        newGame.loadGameSettingsFromXML(xml);
      if(isGameNameExists(newGame.getGameName())==false){
          gamesMap.put(numOfgames++,newGame);
      }
      else{
          throw new Exception("There is a game named like this already!");
      }
    }
}
