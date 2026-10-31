package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import datastructures.GameData;
import service.BadRequestException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SQLGameDataAccess implements GameDataAccess {

    public SQLGameDataAccess() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE gameData";
        DatabaseManager.executeUpdate(statement);
        var statement2 = "TRUNCATE gameState";
        DatabaseManager.executeUpdate(statement2);
    }

    @Override
    public void newGame(int gameID, String gameName) throws DataAccessException {
        Gson json = new Gson();
        var statement = "INSERT INTO gameData (id, name, json) VALUES (?, ?, ?)";
        String json1 = json.toJson(new GameData(gameID, gameName));
        DatabaseManager.executeUpdate(statement, gameID, gameName, json1);
        var statement2 = "INSERT INTO gameState (id, json) VALUES (?, ?)";
        String json2 = json.toJson(new ChessGame());
        DatabaseManager.executeUpdate(statement2, gameID, json2);
    }

    @Override
    public void removeGame(int gameID) throws DataAccessException {
        var statement = "DELETE FROM gameData WHERE id=?";
        DatabaseManager.executeUpdate(statement, gameID);
        var statement2 = "DELETE FROM gameState WHERE id=?";
        DatabaseManager.executeUpdate(statement2, gameID);

    }

    @Override
    public void joinGame(String username, int id, String team) throws BadRequestException, DataAccessException {
        GameData updatedGame = getGameData(id);
        updatedGame.setPlayer(username, team);
        Gson json = new Gson();
        var statement = "UPDATE gameData SET whiteUsername = ?, blackUsername = ?, json = ? WHERE id=?";
        String json1 = json.toJson(updatedGame);
        DatabaseManager.executeUpdate(statement, updatedGame.getWhiteUsername(), updatedGame.getBlackUsername(), json1, id);
    }

    @Override
    public GameData getGameData(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT json FROM gameData WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGameData(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public ChessGame getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT json FROM gameState WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGameState(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public Map<Integer, GameData> getGames() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, json FROM gameData";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGames(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    private GameData readGameData(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        return new Gson().fromJson(json, GameData.class);
    }

    private ChessGame readGameState(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        return new Gson().fromJson(json, ChessGame.class);
    }

    private Map<Integer, GameData> readGames(ResultSet rs) throws SQLException {
        Map<Integer, GameData> games = new HashMap<>();
        while (!rs.isAfterLast()) {
            var id = rs.getInt("id");
            var json = rs.getString("json");
            GameData gameData = new Gson().fromJson(json, GameData.class);
            games.put(id, gameData);
            rs.next();
        }
        return games;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  gameData (
              `id` int NOT NULL AUTO_INCREMENT,
              `name` varchar(256) NOT NULL,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(whiteUsername),
              INDEX(blackUsername),
              INDEX(name)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS  gameState (
              `id` int NOT NULL AUTO_INCREMENT,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
