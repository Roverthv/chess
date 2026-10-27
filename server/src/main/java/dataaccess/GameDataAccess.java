package dataaccess;

import chess.ChessGame;
import datastructures.GameData;

import java.util.Map;

public interface GameDataAccess {

    void clear();

    void newGame(int gameID, String gameName);

    void removeGame(int gameID);

    GameData getGameData(int gameID);

    ChessGame getGame(int gameID);

    Map<Integer, GameData> getGames();
}
