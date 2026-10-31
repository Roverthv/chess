package service;

import dataaccess.*;
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
    UserDataAccess userDatabase;
    AuthDataAccess authDatabase;
    GameDataAccess gameDatabase;
    AdminService admin;

    void createUser() {
        try {
            var userdata = new UserData("test user", "test password", "test email", new ArrayList<>());
            userDatabase.register(userdata);
        } catch (DataAccessException e) {
            throw new RuntimeException("AdminServiceTest Assist function createUser failed");
        }
    }

    void createAuthData() throws DataAccessException {
        var authdata = new AuthData("test user", "testToken");
        authDatabase.addAuthData(authdata);
    }

    void createGame(int gameID) throws DataAccessException {
        gameDatabase.newGame(gameID, "testGame");
    }

    @BeforeEach
    void setUp() {
        userDatabase = new UserMemoryDataAccess();
        authDatabase = new AuthMemoryDataAccess();
        gameDatabase = new GameMemoryDataAccess();
        admin = new AdminService(userDatabase, authDatabase, gameDatabase);
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

    @Test
    void authenticatePass() {
        try {
            createAuthData();
            assertDoesNotThrow(() -> admin.authenticate("testToken"));
        } catch (DataAccessException e) {
            throw new RuntimeException("AdminService authenticatePass test failed");
        }
    }

    @Test
    void authenticateFail() {
        try {
            createAuthData();
            assertThrows(UnauthorizedException.class, () -> admin.authenticate("BadToken"));
        } catch (DataAccessException e) {
            throw new RuntimeException("AdminService authenticateFail test failed");
        }
    }

    @Test
    void createAuth() {
        try {
            String authToken = admin.createAuth("test user");
            assertEquals(new AuthData("test user", authToken), authDatabase.getAuthData(authToken));
        } catch (DataAccessException e) {
            throw new RuntimeException("AdminService createAuth test failed");
        }
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
        try {
            createGame(1);
            createGame(2);
            createGame(3);
            List<GameData> actual = admin.getAllGames();
            List<GameData> expected = List.of(new GameData(1, "testGame"),
                    new GameData(2, "testGame"), new GameData(3, "testGame"));
            assertEquals(expected, actual);
        } catch (DataAccessException e) {
            throw new RuntimeException("AdminService getAllGames test failed");
        }
    }

    @Test
    void clear() {
        try {
            createUser();
            createAuthData();
            createGame(1);
            admin.clear();
            assertTrue(gameDatabase.getGames().isEmpty());
            assertNull(userDatabase.getUser("test user"));
            assertNull(authDatabase.getAuthData("testToken"));
            assertNull(gameDatabase.getGameData(1));
        } catch (DataAccessException e) {
            throw new RuntimeException("AdminService clear test failed");
        }
    }
}