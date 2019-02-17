package engine;
public class User {
    private final int EMPTY = -1;
    private String name;
    private boolean isComputer;
    private int inGameNumber;
    private int playerID;

    public User(String name, boolean isComputer) {
        this.name = name;
        this.isComputer = isComputer;
        this.inGameNumber = -1;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getName() {
        return this.name;
    }

    public boolean isComputer() {
        return this.isComputer;
    }

    public int getInGameNumber() {
        return this.inGameNumber;
    }

    public void setInGameNumber(int inGameNumber) {
        this.inGameNumber = inGameNumber;
    }
}
