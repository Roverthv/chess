package dataaccess;

import DataStructures.AuthData;
import DataStructures.GameData;
import DataStructures.UserData;

public interface DataAccess {

    void clear();

    void register(UserData userdata);

    UserData getUser(String username);

    void removeUser(String username);

    void addAuthData(AuthData authData);

    AuthData getAuthData(String authToken);

    void removeAuthData(String authToken);

    void addGametoUser(String username, String gameID);

    void removeGamefromUser(String username, String gameID);

    void newGame(String gameID);

    void removeGame(String gameID);

    GameData getGame(String gameID);
}
