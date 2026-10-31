package service;

import chess.ChessGame;
import dataaccess.*;
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

    //    private static UserDataAccess userDatabase;
    private static AuthDataAccess authDatabase;
    private static GameDataAccess gameDatabase;

    @BeforeEach
    void setUp() {
        try {
            authDatabase = new SQLAuthDataAccess();
            gameDatabase = new SQLGameDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException("Creating new SQLGameDataAccess structure failed");
        }

    }

    //    void createUser() {
//        var userdata = new UserData("test user", "test password", "test email", new ArrayList<>());
//        gameDatabase.register(userdata);
//    }

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
            //createUser();
            //createAuth();
            //assertNotNull(gameDatabase.getUser("test user"));
            //assertNotNull(gameDatabase.getAuthData("testToken"));
            authDatabase.clear();
            //gameDatabase.clear();
            //assertNull(userDatabase.getUser("test user"));
            //assertNull(authDatabase.getAuthData("testToken"));
        } catch (DataAccessException e) {
            throw new RuntimeException("error in test clear");
        }

    }

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
            //userDatabase.clear();
            authDatabase.clear();
            gameDatabase.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


}