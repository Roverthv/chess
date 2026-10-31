package dataaccess.sql;

import dataaccess.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SQLDataAccessTest {

    private static UserDataAccess userDatabase;
    private static AuthDataAccess authDatabase;
    private static GameDataAccess gameDatabase;
    private DataAccessTestUtils utils;

    @BeforeEach
    void setUp() {
        try {
            userDatabase = new SQLUserDataAccess();
            authDatabase = new SQLAuthDataAccess();
            gameDatabase = new SQLGameDataAccess();
            utils = new DataAccessTestUtils(userDatabase, authDatabase, gameDatabase);
        } catch (DataAccessException e) {
            throw new RuntimeException("Creating new SQLGameDataAccess structure failed");
        }

    }

    @Test
    void clear() {
        utils.clear();
    }

    @Test
    void register() {
        utils.register();
    }

    @Test
    void getUser() {
        utils.getUser();
    }

    @Test
    void removeUser() {
        utils.removeUser();
    }

    @Test
    void addAuthData() {
        utils.addAuthData();
    }

    @Test
    void getAuthData() {
        utils.getAuthData();
    }

    @Test
    void removeAuthData() {
        utils.removeAuthData();
    }

    @Test
    void addGameToUser() {
        utils.addGameToUser();
    }

    @Test
    void removeGameFromUser() {
        utils.removeGameFromUser();
    }

    @Test
    void newGame() {
        utils.newGame();
    }

    @Test
    void removeGame() {
        utils.removeGame();
    }

    @Test
    void getGameData() {
        utils.getGameData();
    }

    @Test
    void getGame() {
        utils.getGame();
    }

    @Test
    void getGames() {
        utils.getGames();
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