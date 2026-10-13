package dataaccess;

import java.util.HashMap;

public class MemoryDataAccess implements DataAccess {
    private Map<String, UserData> users = new HashMap<>();

    @Override
    public void register(UserData userData, AuthData authdata){
       users.put(userData.getUsername(), userData);
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }
}
