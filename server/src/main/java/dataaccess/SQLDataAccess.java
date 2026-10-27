package dataaccess;

import chess.ChessGame;
import datastructures.AuthData;
import datastructures.GameData;
import datastructures.UserData;

import java.util.Map;

public class SQLDataAccess implements DataAccess {
    @Override
    public void clear() {

    }

    @Override
    public void register(UserData userdata) {

    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void removeUser(String username) {

    }

    @Override
    public void addAuthData(AuthData authData) {

    }

    @Override
    public AuthData getAuthData(String authToken) {
        return null;
    }

    @Override
    public void removeAuthData(String authToken) {

    }

    @Override
    public void addGameToUser(String username, int gameID) {

    }

    @Override
    public void newGame(int gameID, String gameName) {

    }

    @Override
    public void removeGame(int gameID) {

    }

    @Override
    public GameData getGameData(int gameID) {
        return null;
    }

    @Override
    public ChessGame getGame(int gameID) {
        return null;
    }

    @Override
    public Map<Integer, GameData> getGames() {
        return Map.of();
    }
}
