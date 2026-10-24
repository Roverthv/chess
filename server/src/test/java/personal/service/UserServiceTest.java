package personal.service;

import datastructures.AuthData;
import datastructures.GameData;
import datastructures.UserData;
import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.BadRequestException;
import service.UnauthorizedException;
import service.UserService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    DataAccess database;
    UserService Service;

    @BeforeEach
    void setUp() {
        database = new MemoryDataAccess();
        Service = new UserService(database);
    }

    @AfterEach
    void tearDown() {
        database.clear();
    }

    @Test
    void testLoginPass() {
        createUser();
        assertDoesNotThrow(() -> Service.login("test user", "test password"));
    }

    @Test
    void testLoginBadUsername() {
        createUser();
        assertThrows(UnauthorizedException.class, () -> Service.login("", "test password"));
    }

    @Test
    void testLoginBadPassword() {
        createUser();
        assertThrows(UnauthorizedException.class, () -> Service.login("test user", ""));
    }

    @Test
    void testLogoutPass() {
        createAuth();
        assertDoesNotThrow(() -> Service.logout("testToken"));
    }

    @Test
    void testCreateGame() {
        createUser();
        int gameID = Service.createGame("test user", "test game");
        assertEquals(new GameData(gameID, "test game"), database.getGameData(gameID));
        assertEquals(new ChessGame(), database.getGame(gameID));
    }

    @Test
    void testJoinGameWhite() {
        createUser();
        int gameID = Service.createGame("test user", "test game");
        assertDoesNotThrow(() -> Service.joinGame("test user", gameID, "WHITE"));
    }

    @Test
    void testJoinGameBlack() {
        createUser();
        int gameID = Service.createGame("test user", "test game");
        assertDoesNotThrow(() -> Service.joinGame("test user", gameID, "BLACK"));
    }

    @Test
    void testJoinGameEither() {
        createUser();
        int gameID = Service.createGame("test user", "test game");
        assertDoesNotThrow(() -> Service.joinGame("test user", gameID, "WHITE/BLACK"));
    }

    @Test
    void testJoinGameWhiteTaken() {
        createUser();
        int gameID = Service.createGame("test user", "test game");
        try {
            Service.joinGame("test user", gameID, "WHITE");
        } catch (DataAccessException | BadRequestException e) {
            throw new RuntimeException(e);
        }
        assertThrows(DataAccessException.class, () -> Service.joinGame("test user", gameID, "WHITE"));
    }

    @Test
    void testJoinGameBlackTaken() {
        createUser();
        int gameID = Service.createGame("test user", "test game");
        try {
            Service.joinGame("test user", gameID, "BLACK");
        } catch (DataAccessException | BadRequestException e) {
            throw new RuntimeException(e);
        }
        assertThrows(DataAccessException.class, () -> Service.joinGame("test user", gameID, "BLACK"));
    }

    @Test
    void testJoinGameFull() {
        createUser();
        int gameID = Service.createGame("test user", "test game");
        try {
            Service.joinGame("test user", gameID, "WHITE");
            Service.joinGame("test user", gameID, "BLACK");
        } catch (DataAccessException | BadRequestException e) {
            throw new RuntimeException(e);
        }
        assertThrows(DataAccessException.class, () -> Service.joinGame("test user", gameID, "WHITE/BLACK"));
    }

    @Test
    void testJoinGameInvalid() {
        createUser();
        int gameID = Service.createGame("test user", "test game");
        assertThrows(BadRequestException.class, () -> Service.joinGame("test user", gameID, "Green"));
    }

    void createUser() {
        var userdata = new UserData("test user", "test password", "test email", new ArrayList<>());
        database.register(userdata);
    }

    void createAuth() {
        var authdata = new AuthData("test user", "testToken");
        database.addAuthData(authdata);
    }
}