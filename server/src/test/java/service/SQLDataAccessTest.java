package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.MemoryDataAccess;
import dataaccess.SQLGameDataAccess;
import datastructures.AuthData;
import datastructures.GameData;
import datastructures.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SQLDataAccessTest {

    private static GameDataAccess gameDatabase;

    @BeforeEach
    void setUp() {
        try {
            gameDatabase = new SQLGameDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException("Creating new SQLGameDataAccess structure failed");
        }

    }

    //    void createUser() {
//        var userdata = new UserData("test user", "test password", "test email", new ArrayList<>());
//        gameDatabase.register(userdata);
//    }
//
//    void createAuth() {
//        var authdata = new AuthData("test user", "testToken");
//        gameDatabase.addAuthData(authdata);
//    }
//
    void createGame() throws DataAccessException {
        gameDatabase.newGame(1, "testGame");
    }
//
//    @Test
//    void clear() {
//        createUser();
//        createAuth();
//        assertNotNull(gameDatabase.getUser("test user"));
//        assertNotNull(gameDatabase.getAuthData("testToken"));
//        gameDatabase.clear();
//        assertNull(gameDatabase.getUser("test user"));
//        assertNull(gameDatabase.getAuthData("testToken"));
//
//    }
//
//    @Test
//    void register() {
//        createUser();
//        assertNotNull(gameDatabase.getUser("test user"));
//    }
//
//    @Test
//    void getUser() {
//        createUser();
//        assertEquals(new UserData("test user", "test password", "test email", new ArrayList<>()), gameDatabase.getUser("test user"));
//    }
//
//    @Test
//    void removeUser() {
//        createUser();
//        assertNotNull(gameDatabase.getUser("test user"));
//        gameDatabase.removeUser("test user");
//        assertNull(gameDatabase.getUser("test user"));
//    }
//
//    @Test
//    void addAuthData() {
//        createAuth();
//        assertNotNull(gameDatabase.getAuthData("testToken"));
//    }
//
//    @Test
//    void getAuthData() {
//        createAuth();
//        assertEquals(new AuthData("test user", "testToken"), gameDatabase.getAuthData("testToken"));
//    }
//
//    @Test
//    void removeAuthData() {
//        createAuth();
//        assertNotNull(gameDatabase.getAuthData("testToken"));
//        gameDatabase.removeAuthData("testToken");
//        assertNull(gameDatabase.getAuthData("testToken"));
//    }
//
//    @Test
//    void addGameToUser() {
//        createUser();
//        assertFalse(gameDatabase.getUser("test user").getGameList().contains(1));
//        gameDatabase.getUser("test user").addGame(1);
//        assertTrue(gameDatabase.getUser("test user").getGameList().contains(1));
//    }
//
//    @Test
//    void removeGameFromUser() {
//        createUser();
//        gameDatabase.getUser("test user").addGame(1);
//        assertTrue(gameDatabase.getUser("test user").getGameList().contains(1));
//        gameDatabase.getUser("test user").removeGame(1);
//        assertFalse(gameDatabase.getUser("test user").getGameList().contains(1));
//
//    }

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
            gameDatabase.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


}