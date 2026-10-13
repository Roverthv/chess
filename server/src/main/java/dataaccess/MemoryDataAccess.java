package dataaccess;

import DataStructures.AuthData;
import DataStructures.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryDataAccess implements DataAccess {
    private final Map<String,UserData> users = new HashMap<>();
    private final Map<String, AuthData> authTokens = new HashMap<>();

    @Override
    public void clear() {
        users.clear();
        authTokens.clear();
    }

    @Override
    public void register(UserData userData){
       users.put(userData.getUsername(), userData);
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public void removeUser(String username) {
        users.remove(username);
    }

    @Override
    public void addAuthData(AuthData authData) {
        authTokens.put(authData.getAuthToken(), authData);
    }

    @Override
    public AuthData getAuthData(String authToken) {
        return authTokens.get(authToken);
    }

    @Override
    public void removeAuthData(String authToken) {
        authTokens.remove(authToken);
    }
}
