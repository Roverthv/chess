package Personal.DataAccess;

import DataStructures.AuthData;
import DataStructures.GameData;
import DataStructures.UserData;
import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MemoryDataAccessTest {

    private static DataAccess database;

    @BeforeEach
    void setUp() {
        database = new MemoryDataAccess();
    }

    void createUser() {
        var userdata = new UserData("test user", "test password", "test email", new ArrayList<>());
        database.register(userdata);
    }

    void createAuth() {
        var authdata = new AuthData("test user", "testToken");
        database.addAuthData(authdata);
    }

    @Test
    void clear() {
        createUser();
        createAuth();
        assertNotNull(database.getUser("test user"));
        assertNotNull(database.getAuthData("testToken"));
        database.clear();
        assertNull(database.getUser("test user"));
        assertNull(database.getAuthData("testToken"));

    }

    @Test
    void register() {
        createUser();
        assertNotNull(database.getUser("test user"));
    }

    @Test
    void getUser() {
        createUser();
        assertEquals(new UserData("test user", "test password", "test email", new ArrayList<>()), database.getUser("test user"));
    }

    @Test
    void removeUser() {
        createUser();
        assertNotNull(database.getUser("test user"));
        database.removeUser("test user");
        assertNull(database.getUser("test user"));
    }

    @Test
    void addAuthData() {
        createAuth();
        assertNotNull(database.getAuthData("testToken"));
    }

    @Test
    void getAuthData() {
        createAuth();
        assertEquals(new AuthData("test user", "testToken"), database.getAuthData("testToken"));
    }

    @Test
    void removeAuthData() {
        createAuth();
        assertNotNull(database.getAuthData("testToken"));
        database.removeAuthData("testToken");
        assertNull(database.getAuthData("testToken"));
    }

    @Test
    void addGametoUser() {
        createUser();
        assertFalse(database.getUser("test user").getGameList().contains(1));
        database.getUser("test user").addGame(1);
        assertTrue(database.getUser("test user").getGameList().contains(1));
    }

    @Test
    void removeGamefromUser() {
        createUser();
        database.getUser("test user").addGame(1);
        assertTrue(database.getUser("test user").getGameList().contains(1));
        database.getUser("test user").removeGame(1);
        assertFalse(database.getUser("test user").getGameList().contains(1));

    }

    @Test
    void newGame() {
        assertNull(database.getGameData(1));
        assertNull(database.getGame(1));
        database.newGame(1, "testGame");
        assertEquals(new GameData(1, "testGame"), database.getGameData(1));
        assertEquals(new ChessGame(), database.getGame(1));
    }

    @Test
    void removeGame() {
        database.newGame(1, "testGame");
        assertEquals(new GameData(1, "testGame"), database.getGameData(1));
        assertEquals(new ChessGame(), database.getGame(1));
        database.removeGame(1);
        assertNull(database.getGameData(1));
        assertNull(database.getGame(1));
    }

    @Test
    void getGameData() {
        GameData testData = new GameData(1, "testGame");
        database.newGame(1, "testGame");
        assertEquals(testData, database.getGameData(1));
    }

    @Test
    void getGame() {
        ChessGame testGame = new ChessGame();
        database.newGame(1, "testGame");
        assertEquals(testGame, database.getGame(1));
    }

    @Test
    void getGames() {
        Map<Integer, GameData> testGameList = Map.of(1, new GameData(1, "testGame"));
        database.newGame(1, "testGame");
        assertEquals(testGameList, database.getGames());
    }

    @AfterEach
    void tearDown() {
        database.clear();
    }


}