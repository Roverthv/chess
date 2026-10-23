package dataaccess;

import DataStructures.AuthData;
import DataStructures.GameData;
import DataStructures.UserData;
import chess.ChessGame;

import java.util.HashMap;
import java.util.Map;

public class MemoryDataAccess implements DataAccess {
    private final Map<String, UserData> users = new HashMap<>();
    private final Map<String, AuthData> authTokens = new HashMap<>();
    private final Map<Integer, GameData> gameData = new HashMap<>();
    private final Map<Integer, ChessGame> games = new HashMap<>();

    @Override
    public void clear() {
        users.clear();
        authTokens.clear();
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

    @Override
    public void addGametoUser(String username, int gameID) {
        users.get(username).addGame(gameID);
    }

    @Override
    public void removeGamefromUser(String username, int gameID) {
        users.get(username).removeGame(gameID);
    }

    @Override
    public void newGame(int gameID, String gameName) {
        gameData.put(gameID, new GameData(gameID, gameName));
        games.put(gameID, new ChessGame());
    }

    @Override
    public void removeGame(int gameID) {
        gameData.remove(gameID);
        games.remove(gameID);
    }

    @Override
    public GameData getGameData(int gameID) {
        return gameData.get(gameID);
    }

    @Override
    public ChessGame getGame(int gameID) {
        return games.get(gameID);
    }

    @Override
    public Map<Integer, GameData> getGames() {
        return gameData;
    }
}
