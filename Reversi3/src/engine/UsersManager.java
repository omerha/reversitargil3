package engine;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UsersManager {
    private static UsersManager usersManagerInstance = null;
    private static int numOfUsers;
    private final HashSet<String> usersSet;

    private UsersManager() {

        usersSet = new HashSet<>();
    }
    
    public static UsersManager getInstance(){
        if(usersManagerInstance == null){
            usersManagerInstance = new UsersManager();
        }
        return usersManagerInstance;
    }

    public synchronized void addUser(String username) {
        usersSet.add(username);
    }

    public synchronized void removeUser(String username) {
        usersSet.remove(username);
    }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public boolean isUserExists(String username) {
        return usersSet.contains(username);
    }
}
