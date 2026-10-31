package dataaccess;

import datastructures.UserData;

public interface UserDataAccess {

    void clear() throws DataAccessException;

    void register(UserData userdata) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void removeUser(String username) throws DataAccessException;

    void addGameToUser(String username, int gameID) throws DataAccessException;

    void removeGamefromUser(String username, int gameID) throws DataAccessException;
}
