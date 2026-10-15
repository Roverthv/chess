package dataaccess;

import DataStructures.AuthData;
import DataStructures.UserData;
import chess.ChessGame;

public interface DataAccess {

    void clear();
    void register(UserData userdata);
    UserData getUser(String username);
    void removeUser(String username);
    void addAuthData(AuthData authData);
    AuthData getAuthData(String authToken);
    void removeAuthData(String authToken);
    void addGame();
    void removeGame();
    ChessGame getGame();
}
