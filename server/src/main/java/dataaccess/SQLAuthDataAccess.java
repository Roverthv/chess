package dataaccess;

import chess.ChessGame;
import datastructures.GameData;

import java.util.Map;

public class SQLAuthDataAccess implements GameDataAccess {
    @Override
    public void clear() {

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
