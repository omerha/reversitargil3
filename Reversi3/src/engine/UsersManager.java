package engine;

import java.util.*;

public class UsersManager {
    private static UsersManager usersManagerInstance = null;
    private static int numOfUsers;
    private final HashMap<String,Boolean> usersSet;

    private UsersManager() {

        usersSet = new HashMap<>();
    }

    public static UsersManager getInstance(){
        if(usersManagerInstance == null){
            usersManagerInstance = new UsersManager();
        }
        return usersManagerInstance;
    }

    public synchronized void addUser(String username,Boolean isComputer) {
        usersSet.put(username,isComputer);
    }

    public synchronized void removeUser(String username) {
        usersSet.remove(username);
    }

    public synchronized List<String> getUsers() {
        List<String> res = new ArrayList<>(usersSet.keySet());
        return  res;
    }

    public boolean isUserExists(String username) {
        return usersSet.containsKey(username);
    }
}
