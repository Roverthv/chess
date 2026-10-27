package dataaccess;

import chess.ChessGame;
import datastructures.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class SQLGameDataAccess implements GameDataAccess {
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

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  gameData (
              `id` int NOT NULL AUTO_INCREMENT,
              `name` varchar(256) NOT NULL,
              `whitePlayer` varchar(256) NOT NULL,
              `blackPlayer` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(whitePlayer),
              INDEX(blackPlayer),
              INDEX(name)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS  gameState (
              `id` int NOT NULL AUTO_INCREMENT,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
