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

    public HashMap<Integer, GameManager> getGamesMap() {
        return gamesMap;
    }

    public boolean isGameNameExists(String name) {
        boolean res = false;
        for (GameManager value : gamesMap.values()) {
            if (value.getGameName().equals(name)) {
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
          newGame.setNameOfPlayerWhoCreatedTheGame(gameCreatorName);
      }
      else{
          throw new Exception("There is a game named like this already!");
      }
    }
    public GameManager getGameByNumber(int i){
        return gamesMap.get(i);
    }
}
