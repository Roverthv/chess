package dataaccess;

import datastructures.UserData;

public interface UserDataAccess {

    void clear();

    void register(UserData userdata);

    UserData getUser(String username);

    void removeUser(String username);

    void addGameToUser(String username, int gameID);
}
