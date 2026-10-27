package dataaccess;

import chess.ChessGame;
import datastructures.GameData;

import java.util.HashMap;
import java.util.Map;

public class GameMemoryDataAccess implements GameDataAccess {
    private final Map<Integer, GameData> gameData = new HashMap<>();
    private final Map<Integer, ChessGame> games = new HashMap<>();

    @Override
    public void clear() {
        gameData.clear();
        games.clear();
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
