package dataaccess;

import chess.ChessGame;
import datastructures.GameData;
import service.BadRequestException;

import java.util.Map;

public interface GameDataAccess {

    void clear() throws DataAccessException;

    void newGame(int gameID, String gameName) throws DataAccessException;

    void removeGame(int gameID) throws DataAccessException;

    void joinGame(String username, int id, String team) throws BadRequestException, DataAccessException;

    GameData getGameData(int gameID) throws DataAccessException;

    ChessGame getGame(int gameID) throws DataAccessException;

    Map<Integer, GameData> getGames() throws DataAccessException;
}
