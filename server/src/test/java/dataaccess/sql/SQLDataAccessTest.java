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
    void setUpSQL() {
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
    void clearSQL() {
        utils.clear();
    }

    @Test
    void registerSQL() {
        utils.register();
    }

    @Test
    void getUserSQL() {
        utils.getUser();
    }

    @Test
    void removeUserSQL() {
        utils.removeUser();
    }

    @Test
    void addAuthDataSQL() {
        utils.addAuthData();
    }

    @Test
    void getAuthDataSQL() {
        utils.getAuthData();
    }

    @Test
    void removeAuthDataSQL() {
        utils.removeAuthData();
    }

    @Test
    void addGameToUserSQL() {
        utils.addGameToUser();
    }

    @Test
    void removeGameFromUserSQL() {
        utils.removeGameFromUser();
    }

    @Test
    void newGameSQL() {
        utils.newGame();
    }

    @Test
    void removeGameSQL() {
        utils.removeGame();
    }

    @Test
    void getGameDataSQL() {
        utils.getGameData();
    }

    @Test
    void getGameSQL() {
        utils.getGame();
    }

    @Test
    void getGamesSQL() {
        utils.getGames();
    }

    @AfterEach
    void tearDownSQL() {
        try {
            userDatabase.clear();
            authDatabase.clear();
            gameDatabase.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


}