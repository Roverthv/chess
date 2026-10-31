package dataaccess.memory;

import dataaccess.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class MemoryDataAccessTest {

    private static UserDataAccess userDatabase;
    private static AuthDataAccess authDatabase;
    private static GameDataAccess gameDatabase;
    private DataAccessTestUtils utils;

    @BeforeEach
    void setUpMemory() {
        userDatabase = new UserMemoryDataAccess();
        authDatabase = new AuthMemoryDataAccess();
        gameDatabase = new GameMemoryDataAccess();
        utils = new DataAccessTestUtils(userDatabase, authDatabase, gameDatabase);
    }

    @Test
    void clearMemory() {
        utils.clear();
    }

    @Test
    void registerMemory() {
        utils.register();
    }

    @Test
    void getUserMemory() {
        utils.getUser();
    }

    @Test
    void removeUserMemory() {
        utils.removeUser();
    }

    @Test
    void addAuthDataMemory() {
        utils.addAuthData();
    }

    @Test
    void getAuthDataMemory() {
        utils.getAuthData();
    }

    @Test
    void removeAuthDataMemory() {
        utils.removeAuthData();
    }

    @Test
    void addGameToUserMemory() {
        utils.addGameToUser();
    }

    @Test
    void removeGameFromUserMemory() {
        utils.removeGameFromUser();
    }

    @Test
    void newGameMemory() {
        utils.newGame();
    }

    @Test
    void removeGameMemory() {
        utils.removeGame();
    }

    @Test
    void getGameDataMemory() {
        utils.getGameData();
    }

    @Test
    void getGameMemory() {
        utils.getGame();
    }

    @Test
    void getGamesMemory() {
        utils.getGames();
    }

    @AfterEach
    void tearDownMemory() {
        try {
            userDatabase.clear();
            authDatabase.clear();
            gameDatabase.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


}