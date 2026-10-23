package Personal.Service;

import DataStructures.AuthData;
import DataStructures.GameData;
import DataStructures.UserData;
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
    void TestloginPass() {
        createUser();
        assertDoesNotThrow(() -> Service.login("test user", "test password"));
    }

    @Test
    void TestloginBadUsername() {
        createUser();
        assertThrows(UnauthorizedException.class, () -> Service.login("", "test password"));
    }

    @Test
    void TestloginBadPassword() {
        createUser();
        assertThrows(UnauthorizedException.class, () -> Service.login("test user", ""));
    }

    @Test
    void TestlogoutPass() {
        createAuth();
        assertDoesNotThrow(() -> Service.logout("testToken"));
    }

    @Test
    void TestcreateGame() {
        createUser();
        int gameID = Service.createGame("test user", "test game");
        assertEquals(new GameData(gameID, "test game"), database.getGameData(gameID));
        assertEquals(new ChessGame(), database.getGame(gameID));
    }

    @Test
    void TestjoinGameWhite() {
        createUser();
        int gameID = Service.createGame("test user", "test game");
        assertDoesNotThrow(() -> Service.joinGame("test user", gameID, "WHITE"));
    }

    @Test
    void TestjoinGameBlack() {
        createUser();
        int gameID = Service.createGame("test user", "test game");
        assertDoesNotThrow(() -> Service.joinGame("test user", gameID, "BLACK"));
    }

    @Test
    void TestjoinGameEither() {
        createUser();
        int gameID = Service.createGame("test user", "test game");
        assertDoesNotThrow(() -> Service.joinGame("test user", gameID, "WHITE/BLACK"));
    }

    @Test
    void TestjoinGameWhiteTaken() {
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
    void TestjoinGameBlackTaken() {
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
    void TestjoinGameFull() {
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
    void TestjoinGameInvalid() {
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