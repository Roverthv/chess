package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import datastructures.AuthData;
import datastructures.GameData;
import datastructures.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdminServiceTest {
    DataAccess database;
    AdminService admin;

    void createUser() {
        var userdata = new UserData("test user", "test password", "test email", new ArrayList<>());
        database.register(userdata);
    }

    void createAuthData() {
        var authdata = new AuthData("test user", "testToken");
        database.addAuthData(authdata);
    }

    void createGame(int gameID) {
        database.newGame(gameID, "testGame");
    }

    @BeforeEach
    void setUp() {
        database = new MemoryDataAccess();
        admin = new AdminService(database);
    }

    @AfterEach
    void tearDown() {
        database.clear();
    }

    @Test
    void authenticatePass() {
        createAuthData();
        assertDoesNotThrow(() -> admin.authenticate("testToken"));
    }

    @Test
    void authenticateFail() {
        createAuthData();
        assertThrows(UnauthorizedException.class, () -> admin.authenticate("BadToken"));
    }

    @Test
    void createAuth() {
        String authToken = admin.createAuth("test user");
        assertEquals(new AuthData("test user", authToken), database.getAuthData(authToken));
    }

    @Test
    void createUserPass() {
        assertDoesNotThrow(() -> admin.createUser("test user", "test password", "test email"));
    }

    @Test
    void createUserNameTaken() {
        createUser();
        assertThrows(DataAccessException.class, () -> admin.createUser("test user", "test password", "test email"));
    }

    @Test
    void getAllGames() {
        createGame(1);
        createGame(2);
        createGame(3);
        List<GameData> actual = admin.getAllGames();
        List<GameData> expected = List.of(new GameData(1, "testGame"),
                new GameData(2, "testGame"), new GameData(3, "testGame"));
        assertEquals(expected, actual);
    }

    @Test
    void clear() {
        createUser();
        createAuthData();
        createGame(1);
        admin.clear();
        assertTrue(database.getGames().isEmpty());
        assertNull(database.getUser("test user"));
        assertNull(database.getAuthData("testToken"));
        assertNull(database.getGameData(1));
    }
}