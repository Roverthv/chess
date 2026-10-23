package dataaccess;

import DataStructures.AuthData;
import DataStructures.GameData;
import DataStructures.UserData;
import chess.ChessGame;

import java.util.Map;

public interface DataAccess {

    void clear();

    void register(UserData userdata);

    UserData getUser(String username);

    void removeUser(String username);

    void addAuthData(AuthData authData);

    AuthData getAuthData(String authToken);

    void removeAuthData(String authToken);

    void addGametoUser(String username, int gameID);

    void removeGamefromUser(String username, int gameID);

    void newGame(int gameID, String gameName);

    void removeGame(int gameID);

    GameData getGameData(int gameID);

    ChessGame getGame(int gameID);

    Map<Integer, GameData> getGames();
}
