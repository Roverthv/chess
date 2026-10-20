package Personal.DataAccess;

import DataStructures.AuthData;
import DataStructures.UserData;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
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

    void createUser() {
        var userdata = new UserData("testuser", "test password", new ArrayList<>());
        database.register(userdata);
    }

    void createAuth() {
        var authdata = new AuthData(1, "testuser");
        database.addAuthData(authdata);
    }

    @Test
    void clear() {
        createUser();
        createAuth();
        assertNotNull(database.getUser("testuser"));
        assertNotNull(database.getAuthData(1));
        database.clear();
        assertNull(database.getUser("testuser"));
        assertNull(database.getAuthData(1));

    }

    @Test
    void register() {
        createUser();
        assertNotNull(database.getUser("testuser"));
    }

    @Test
    void getUser() {
        createUser();
        assertEquals(new UserData("testuser", "test password", new ArrayList<>()), database.getUser("testuser"));
    }

    @Test
    void removeUser() {
        createUser();
        assertNotNull(database.getUser("testuser"));
        database.removeUser("testuser");
        assertNull(database.getUser("testuser"));
    }

    @Test
    void addAuthData() {
        createAuth();
        assertNotNull(database.getAuthData(1));
    }

    @Test
    void getAuthData() {
        createAuth();
        assertEquals(new AuthData(1, "testuser"), database.getAuthData(1));
    }

    @Test
    void removeAuthData() {
        createAuth();
        assertNotNull(database.getAuthData(1));
        database.removeAuthData(1);
        assertNull(database.getAuthData(1));
    }
}