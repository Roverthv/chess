package dataaccess.sql;

import com.google.gson.Gson;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import datastructures.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLAuthDataAccess implements AuthDataAccess {

    public SQLAuthDataAccess() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE authData";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public void addAuthData(AuthData authData) throws DataAccessException {
        Gson json = new Gson();
        var statement = "INSERT INTO authData (authToken, username, json) VALUES (?, ?, ?)";
        String json1 = json.toJson(authData);
        DatabaseManager.executeUpdate(statement, authData.getAuthToken(), authData.getUsername(), json1);
    }

    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT json FROM authData WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuthData(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void removeAuthData(String authToken) throws DataAccessException {
        var statement = "DELETE FROM authData WHERE authToken=?";
        DatabaseManager.executeUpdate(statement, authToken);
    }

    private AuthData readAuthData(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        return new Gson().fromJson(json, AuthData.class);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  authData (
              `authToken` varchar(36) NOT NULL,
              `username` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
