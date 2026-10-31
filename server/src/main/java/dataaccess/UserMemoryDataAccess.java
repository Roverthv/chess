package dataaccess;

import datastructures.UserData;

import java.util.HashMap;
import java.util.Map;

public class UserMemoryDataAccess implements UserDataAccess {
    private final Map<String, UserData> users = new HashMap<>();

    @Override
    public void clear() {
        users.clear();
    }

    @Override
    public void register(UserData userData) {
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
    public void addGameToUser(String username, int gameID) {
        users.get(username).addGame(gameID);
    }

    @Override
    public void removeGamefromUser(String username, int gameID) {
        users.get(username).removeGame(gameID);
    }
}
