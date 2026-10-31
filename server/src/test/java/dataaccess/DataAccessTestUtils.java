package dataaccess;

import chess.ChessGame;
import datastructures.AuthData;
import datastructures.GameData;
import datastructures.UserData;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataAccessTestUtils {

    private final UserDataAccess userDatabase;
    private final AuthDataAccess authDatabase;
    private final GameDataAccess gameDatabase;

    public DataAccessTestUtils(UserDataAccess userDatabase, AuthDataAccess authDatabase, GameDataAccess gameDatabase) {
        this.userDatabase = userDatabase;
        this.authDatabase = authDatabase;
        this.gameDatabase = gameDatabase;
    }

    private void createUser() throws DataAccessException {
        var userdata = new UserData("test user", "test password", "test email", new ArrayList<>());
        userDatabase.register(userdata);
    }

    private void createAuth() throws DataAccessException {
        var authdata = new AuthData("test user", "testToken");
        authDatabase.addAuthData(authdata);
    }

    private void createGame() throws DataAccessException {
        gameDatabase.newGame(1, "testGame");
    }

    public void clear() {
        try {
            createUser();
            createAuth();
            createGame();
            assertNotNull(userDatabase.getUser("test user"));
            assertNotNull(authDatabase.getAuthData("testToken"));
            assertNotNull(gameDatabase.getGameData(1));
            assertNotNull(gameDatabase.getGame(1));
            userDatabase.clear();
            authDatabase.clear();
            gameDatabase.clear();
            assertNull(userDatabase.getUser("test user"));
            assertNull(authDatabase.getAuthData("testToken"));
            assertNull(gameDatabase.getGameData(1));
            assertNull(gameDatabase.getGame(1));
        } catch (DataAccessException e) {
            throw new RuntimeException("error in test clear");
        }

    }

    public void register() {
        try {
            createUser();
            assertNotNull(userDatabase.getUser("test user"));
        } catch (DataAccessException e) {
            throw new RuntimeException("SQL test register failed");
        }
    }

    public void getUser() {
        try {
            createUser();
            assertEquals(new UserData("test user", "test password", "test email", new ArrayList<>()), userDatabase.getUser("test user"));
        } catch (DataAccessException e) {
            throw new RuntimeException("SQL test getUser failed");
        }
    }

    public void removeUser() {
        try {
            createUser();
            assertNotNull(userDatabase.getUser("test user"));
            userDatabase.removeUser("test user");
            assertNull(userDatabase.getUser("test user"));
        } catch (DataAccessException e) {
            throw new RuntimeException("SQL test removeUser failed");
        }
    }

    public void addAuthData() {
        try {
            createAuth();
            assertNotNull(authDatabase.getAuthData("testToken"));
        } catch (DataAccessException e) {
            throw new RuntimeException("SQL test addAuthData failed");
        }
    }

    public void getAuthData() {
        try {
            createAuth();
            assertEquals(new AuthData("test user", "testToken"), authDatabase.getAuthData("testToken"));
        } catch (DataAccessException e) {
            throw new RuntimeException("SQL test getAuthData failed");
        }
    }

    public void removeAuthData() {
        try {
            createAuth();
            assertNotNull(authDatabase.getAuthData("testToken"));
            authDatabase.removeAuthData("testToken");
            assertNull(authDatabase.getAuthData("testToken"));
        } catch (DataAccessException e) {
            throw new RuntimeException("SQL test getAuthData failed");
        }
    }

    public void addGameToUser() {
        try {
            createUser();
            assertFalse(userDatabase.getUser("test user").getGameList().contains(1));
            userDatabase.addGameToUser("test user", 1);
            assertTrue(userDatabase.getUser("test user").getGameList().contains(1));
        } catch (DataAccessException e) {
            throw new RuntimeException("SQL test addGameToUser failed");
        }
    }

    public void removeGameFromUser() {
        try {
            createUser();
            userDatabase.addGameToUser("test user", 1);
            assertTrue(userDatabase.getUser("test user").getGameList().contains(1));
            userDatabase.removeGamefromUser("test user", 1);
            assertFalse(userDatabase.getUser("test user").getGameList().contains(1));
        } catch (DataAccessException e) {
            throw new RuntimeException("SQL test removeGameFromUser failed");
        }
    }

    public void newGame() {
        try {
            assertNull(gameDatabase.getGameData(1));
            assertNull(gameDatabase.getGame(1));
            createGame();
            assertEquals(new GameData(1, "testGame"), gameDatabase.getGameData(1));
            assertEquals(new ChessGame(), gameDatabase.getGame(1));
        } catch (DataAccessException e) {
            throw new RuntimeException("New game SQL test failed");
        }
    }

    public void removeGame() {
        try {
            createGame();
            assertEquals(new GameData(1, "testGame"), gameDatabase.getGameData(1));
            assertEquals(new ChessGame(), gameDatabase.getGame(1));
            gameDatabase.removeGame(1);
            assertNull(gameDatabase.getGameData(1));
            assertNull(gameDatabase.getGame(1));
        } catch (DataAccessException e) {
            throw new RuntimeException("Remove game SQL test failed");
        }

    }

    public void getGameData() {
        GameData testData = new GameData(1, "testGame");
        try {
            createGame();
            assertEquals(testData, gameDatabase.getGameData(1));
        } catch (DataAccessException e) {
            throw new RuntimeException("GetGameData SQl test failed");
        }

    }

    public void getGame() {
        ChessGame testGame = new ChessGame();
        try {
            createGame();
            assertEquals(testGame, gameDatabase.getGame(1));
        } catch (DataAccessException e) {
            throw new RuntimeException("GetGame SQL test failed");
        }
    }

    public void getGames() {
        Map<Integer, GameData> testGameList = Map.of(1, new GameData(1, "testGame"));
        try {
            createGame();
            assertEquals(testGameList, gameDatabase.getGames());
        } catch (DataAccessException e) {
            throw new RuntimeException("getGames SQL test failed");
        }
    }

}
