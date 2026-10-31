package service;

import dataaccess.*;
import datastructures.AuthData;
import datastructures.GameData;
import datastructures.UserData;
import chess.ChessGame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserDataAccess userDatabase;
    AuthDataAccess authDatabase;
    GameDataAccess gameDatabase;

    UserService service;

    @BeforeEach
    void setUp() {
        userDatabase = new UserMemoryDataAccess();
        authDatabase = new AuthMemoryDataAccess();
        gameDatabase = new GameMemoryDataAccess();

        service = new UserService(userDatabase, authDatabase, gameDatabase);
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
    void testLoginPass() {
        createUser();
        assertDoesNotThrow(() -> service.login("test user", "test password"));
    }

    @Test
    void testLoginBadUsername() {
        createUser();
        assertThrows(UnauthorizedException.class, () -> service.login("", "test password"));
    }

    @Test
    void testLoginBadPassword() {
        createUser();
        assertThrows(UnauthorizedException.class, () -> service.login("test user", ""));
    }

    @Test
    void testLogoutPass() {
        try {
            createAuth();
            assertDoesNotThrow(() -> service.logout("testToken"));
        } catch (DataAccessException e) {
            throw new RuntimeException("UserService test logoutpass failed");
        }
    }

    @Test
    void testCreateGame() {
        createUser();
        try {
            int gameID = service.createGame("test user", "test game");
            assertEquals(new GameData(gameID, "test game"), gameDatabase.getGameData(gameID));
            assertEquals(new ChessGame(), gameDatabase.getGame(gameID));
        } catch (DataAccessException e) {
            throw new RuntimeException("UserService test create game failed");
        }

    }

    @Test
    void testJoinGameWhite() {
        try {
            createUser();
            int gameID = service.createGame("test user", "test game");
            assertDoesNotThrow(() -> service.joinGame("test user", gameID, "WHITE"));
        } catch (DataAccessException e) {
            throw new RuntimeException("UserService test join game white failed");
        }
    }

    @Test
    void testJoinGameBlack() {
        try {
            createUser();
            int gameID = service.createGame("test user", "test game");
            assertDoesNotThrow(() -> service.joinGame("test user", gameID, "BLACK"));
        } catch (DataAccessException e) {
            throw new RuntimeException("UserService test join game black failed");
        }
    }

    @Test
    void testJoinGameEither() {
        try {
            createUser();
            int gameID = service.createGame("test user", "test game");
            assertDoesNotThrow(() -> service.joinGame("test user", gameID, "WHITE/BLACK"));
        } catch (DataAccessException e) {
            throw new RuntimeException("UserService test join game either failed");
        }
    }

    @Test
    void testJoinGameWhiteTaken() {
        try {
            createUser();
            int gameID = service.createGame("test user", "test game");
            try {
                service.joinGame("test user", gameID, "WHITE");
            } catch (DataAccessException | BadRequestException e) {
                throw new RuntimeException(e);
            }
            assertThrows(DataAccessException.class, () -> service.joinGame("test user", gameID, "WHITE"));
        } catch (DataAccessException e) {
            throw new RuntimeException("UserService test join game white taken failed");
        }
    }

    @Test
    void testJoinGameBlackTaken() {
        try {
            createUser();
            int gameID = service.createGame("test user", "test game");
            try {
                service.joinGame("test user", gameID, "BLACK");
            } catch (DataAccessException | BadRequestException e) {
                throw new RuntimeException(e);
            }
            assertThrows(DataAccessException.class, () -> service.joinGame("test user", gameID, "BLACK"));
        } catch (DataAccessException e) {
            throw new RuntimeException("UserService test join game black taken failed");
        }
    }

    @Test
    void testJoinGameFull() {
        try {
            createUser();
            int gameID = service.createGame("test user", "test game");
            try {
                service.joinGame("test user", gameID, "WHITE");
                service.joinGame("test user", gameID, "BLACK");
            } catch (DataAccessException | BadRequestException e) {
                throw new RuntimeException(e);
            }
            assertThrows(DataAccessException.class, () -> service.joinGame("test user", gameID, "WHITE/BLACK"));
        } catch (DataAccessException e) {
            throw new RuntimeException("UserService test join game full failed");
        }
    }

    @Test
    void testJoinGameInvalid() {
        try {
            createUser();
            int gameID = service.createGame("test user", "test game");
            assertThrows(BadRequestException.class, () -> service.joinGame("test user", gameID, "Green"));
        } catch (DataAccessException e) {
            throw new RuntimeException("UserService test join game invalid failed");
        }
    }

    void createUser() {
        try {
            var userdata = new UserData("test user", "test password", "test email", new ArrayList<>());
            userDatabase.register(userdata);
        } catch (DataAccessException e) {
            throw new RuntimeException("UserServiceTest Assist function createUser failed");
        }
    }

    void createAuth() throws DataAccessException {
        var authdata = new AuthData("test user", "testToken");
        authDatabase.addAuthData(authdata);
    }
}