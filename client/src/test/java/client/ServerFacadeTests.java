package client;

import dataaccess.*;
import dataaccess.sql.SQLAuthDataAccess;
import dataaccess.sql.SQLGameDataAccess;
import dataaccess.sql.SQLUserDataAccess;
import org.junit.jupiter.api.*;
import server.Server;

public class ServerFacadeTests {

    private static Server server;
    ClientBackground facade = new ClientBackground();
    private static UserDataAccess userDatabase;
    private static AuthDataAccess authDatabase;
    private static GameDataAccess gameDatabase;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    void setUpSQL() {
        try {
            userDatabase = new SQLUserDataAccess();
            authDatabase = new SQLAuthDataAccess();
            gameDatabase = new SQLGameDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException("Creating new SQLGameDataAccess structure failed");
        }
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

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerTest() {
        facade.sendRegisterRequest("testuser", "testpass", "testemail");
    }

}
