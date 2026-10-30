package dataaccess;

import chess.ChessGame;
import datastructures.GameData;
import service.BadRequestException;

import java.util.HashMap;
import java.util.Map;

public class GameMemoryDataAccess implements GameDataAccess {
    private final Map<Integer, GameData> gameData = new HashMap<>();
    private final Map<Integer, ChessGame> gameState = new HashMap<>();

    @Override
    public void clear() {
        gameData.clear();
        gameState.clear();
    }

    @Override
    public void newGame(int gameID, String gameName) {
        gameData.put(gameID, new GameData(gameID, gameName));
        gameState.put(gameID, new ChessGame());
    }

    @Override
    public void removeGame(int gameID) {
        gameData.remove(gameID);
        gameState.remove(gameID);
    }

    @Override
    public void joinGame(String username, int id, String team) throws BadRequestException, DataAccessException {
        getGameData(id).setPlayer(username, team);
    }

    @Override
    public GameData getGameData(int gameID) {
        return gameData.get(gameID);
    }

    @Override
    public ChessGame getGame(int gameID) {
        return gameState.get(gameID);
    }

    @Override
    public Map<Integer, GameData> getGames() {
        return gameData;
    }
}
