package engine;

import java.util.*;

public class UsersManager {
    private static UsersManager usersManagerInstance = null;
    private static int numOfUsers;
    private final HashMap<String,User> usersSet;

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
        usersSet.put(username,new User(username,isComputer));
    }

    public synchronized void removeUser(String username) {
        usersSet.remove(username);
    }

    public synchronized List<String> getUsers() {
        List<String> res = new ArrayList<>(usersSet.keySet());
        return  res;
    }

    public boolean isUserExists(String userName) {
        Iterator it = usersSet.entrySet().iterator();
        boolean res = false;
        while (it.hasNext() && !res) {
            Map.Entry pair = (Map.Entry) it.next();
            if(pair.getKey().toString().toLowerCase().equals(userName.toLowerCase())){
                res = true;
            }
        //    it.remove(); // avoids a ConcurrentModificationException
        }
        return res;
    }
    public User getUserByName(String userName){
        Iterator it = usersSet.entrySet().iterator();
        User res = null;
        while (it.hasNext() && res == null) {
            Map.Entry pair = (Map.Entry) it.next();
            if(pair.getKey().toString().toLowerCase().equals(userName.toLowerCase())){
                res = (User) pair.getValue();
            }
            //    it.remove(); // avoids a ConcurrentModificationException
        }
        return res;
    }
}
