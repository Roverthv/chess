package Personal.DataAccess;

import DataStructures.UserData;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MemoryDataAccessTest {

    private static DataAccess database;

    @BeforeEach
    void setUp() {
        database = new MemoryDataAccess();
    }

//    @AfterEach
//    void tearDown() {
//    }

    @Test
    void clear() {
    }

    @Test
    void register() {
        var userdata = new UserData("testuser", "test password", new ArrayList<>());
        database.register(userdata);
        assertNotNull(database.getUser(userdata.getUsername()));
    }

    @Test
    void getUser() {
    }

    @Test
    void removeUser() {
    }

    @Test
    void addAuthData() {
    }

    @Test
    void getAuthData() {
    }

    @Test
    void removeAuthData() {
    }
}