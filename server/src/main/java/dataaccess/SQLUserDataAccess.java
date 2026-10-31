package dataaccess;

import com.google.gson.Gson;
import datastructures.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLUserDataAccess implements UserDataAccess {

    public SQLUserDataAccess() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE userData";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public void register(UserData userdata) throws DataAccessException {
        Gson json = new Gson();
        var statement = "INSERT INTO userData (username, password, email, gameList, json) VALUES (?, ?, ?, ?, ?)";
        String json1 = json.toJson(userdata);
        String list = json.toJson(userdata.getGameList());
        DatabaseManager.executeUpdate(statement, userdata.getUsername(), userdata.password(), userdata.getEmail(), list, json1);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT json FROM userData WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUserData(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void removeUser(String username) throws DataAccessException {
        var statement = "DELETE FROM userData WHERE username=?";
        DatabaseManager.executeUpdate(statement, username);
    }

    @Override
    public void addGameToUser(String username, int gameID) throws DataAccessException {
        ArrayList<Integer> gameList = getGameList(username);
        gameList.add(gameID);
        var statement = "UPDATE userData SET gameList=? WHERE username=?";
        String list = new Gson().toJson(gameList);
        DatabaseManager.executeUpdate(statement, list, username);
    }

    private ArrayList<Integer> getGameList(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT list FROM userData WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGameList(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    private UserData readUserData(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        return new Gson().fromJson(json, UserData.class);
    }

    private ArrayList<Integer> readGameList(ResultSet rs) throws SQLException {
        var list = rs.getString("list");
        return new Gson().fromJson(list, ArrayList.class);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  userData (
              `username` varchar(256) NOT NULL,
              `password` varchar(72) NOT NULL,
              `email` varchar(256) NOT NULL,
              `gameList` TEXT NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`username`),
              INDEX(email)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
