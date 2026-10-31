package dataaccess.memory;

import dataaccess.*;
import datastructures.AuthData;
import datastructures.GameData;
import datastructures.UserData;
import chess.ChessGame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MemoryDataAccessTest {

    private static UserDataAccess userDatabase;
    private static AuthDataAccess authDatabase;
    private static GameDataAccess gameDatabase;

    @BeforeEach
    void setUp() {
        userDatabase = new UserMemoryDataAccess();
        authDatabase = new AuthMemoryDataAccess();
        gameDatabase = new GameMemoryDataAccess();
    }

    void createUser() throws DataAccessException {
        var userdata = new UserData("test user", "test password", "test email", new ArrayList<>());
        userDatabase.register(userdata);
    }

    void createAuth() throws DataAccessException {
        var authdata = new AuthData("test user", "testToken");
        authDatabase.addAuthData(authdata);
    }

    void createGame() throws DataAccessException {
        gameDatabase.newGame(1, "testGame");
    }

    @Test
    void clear() {
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

    @Test
    void register() {
        try {
            createUser();
            assertNotNull(userDatabase.getUser("test user"));
        } catch (DataAccessException e) {
            throw new RuntimeException("SQL test register failed");
        }
    }

    @Test
    void getUser() {
        try {
            createUser();
            assertEquals(new UserData("test user", "test password", "test email", new ArrayList<>()), userDatabase.getUser("test user"));
        } catch (DataAccessException e) {
            throw new RuntimeException("SQL test getUser failed");
        }
    }

    @Test
    void removeUser() {
        try {
            createUser();
            assertNotNull(userDatabase.getUser("test user"));
            userDatabase.removeUser("test user");
            assertNull(userDatabase.getUser("test user"));
        } catch (DataAccessException e) {
            throw new RuntimeException("SQL test removeUser failed");
        }
    }

    @Test
    void addAuthData() {
        try {
            createAuth();
            assertNotNull(authDatabase.getAuthData("testToken"));
        } catch (DataAccessException e) {
            throw new RuntimeException("SQL test addAuthData failed");
        }
    }

    @Test
    void getAuthData() {
        try {
            createAuth();
            assertEquals(new AuthData("test user", "testToken"), authDatabase.getAuthData("testToken"));
        } catch (DataAccessException e) {
            throw new RuntimeException("SQL test getAuthData failed");
        }
    }

    @Test
    void removeAuthData() {
        try {
            createAuth();
            assertNotNull(authDatabase.getAuthData("testToken"));
            authDatabase.removeAuthData("testToken");
            assertNull(authDatabase.getAuthData("testToken"));
        } catch (DataAccessException e) {
            throw new RuntimeException("SQL test getAuthData failed");
        }
    }

    @Test
    void addGameToUser() {
        try {
            createUser();
            assertFalse(userDatabase.getUser("test user").getGameList().contains(1));
            userDatabase.addGameToUser("test user", 1);
            assertTrue(userDatabase.getUser("test user").getGameList().contains(1));
        } catch (DataAccessException e) {
            throw new RuntimeException("SQL test addGameToUser failed");
        }
    }

    @Test
    void removeGameFromUser() {
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

    @Test
    void newGame() {
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

    @Test
    void removeGame() {
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

    @Test
    void getGameData() {
        GameData testData = new GameData(1, "testGame");
        try {
            createGame();
            assertEquals(testData, gameDatabase.getGameData(1));
        } catch (DataAccessException e) {
            throw new RuntimeException("GetGameData SQl test failed");
        }

    }

    @Test
    void getGame() {
        ChessGame testGame = new ChessGame();
        try {
            createGame();
            assertEquals(testGame, gameDatabase.getGame(1));
        } catch (DataAccessException e) {
            throw new RuntimeException("GetGame SQL test failed");
        }
    }

    @Test
    void getGames() {
        Map<Integer, GameData> testGameList = Map.of(1, new GameData(1, "testGame"));
        try {
            createGame();
            assertEquals(testGameList, gameDatabase.getGames());
        } catch (DataAccessException e) {
            throw new RuntimeException("getGames SQL test failed");
        }
    }

    @AfterEach
    void tearDown() {
        try {
            userDatabase.clear();
            authDatabase.clear();
            gameDatabase.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


}