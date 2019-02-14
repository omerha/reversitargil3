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
            System.out.println("New users manager");
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
        Iterator it = usersSet.entrySet().iterator();
        boolean res = false;
        while (it.hasNext() && !res) {
            Map.Entry pair = (Map.Entry) it.next();
            if(pair.getKey().toString().toLowerCase().equals(username.toLowerCase())){
                res = true;
            }
        //    it.remove(); // avoids a ConcurrentModificationException
        }
        return res;
    }
}
