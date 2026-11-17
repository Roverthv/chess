package client;

import dataaccess.*;
import dataaccess.sql.SQLAuthDataAccess;
import dataaccess.sql.SQLGameDataAccess;
import dataaccess.sql.SQLUserDataAccess;
import org.junit.jupiter.api.*;
import server.Server;
import service.AdminService;
import service.BadRequestException;
import service.UserService;

public class ServerFacadeTests {

    private static Server server;
    ClientBackground facade = new ClientBackground();
    private static UserDataAccess userDatabase;
    private static AuthDataAccess authDatabase;
    private static GameDataAccess gameDatabase;
    private static AdminService admin;
    private static UserService service;

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
            admin = new AdminService(userDatabase, authDatabase, gameDatabase);
            service = new UserService(userDatabase, authDatabase, gameDatabase);
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

    private void createUser() {
        try {
            admin.createUser("test user", "test password", "test email");
        } catch (BadRequestException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private String createAuth() {
        try {
            return admin.createAuth("test user");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void createGame() {
        try {
            createUser();
            service.createGame("test user", "test game");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void registerTest() {
        facade.sendRegisterRequest("test user", "test password", "test email");
    }

    @Test
    public void loginTest() {
        createUser();
        facade.sendLoginRequest("test user", "test password");
    }

    @Test
    public void logoutTest() {
        String testToken = createAuth();
        facade.setAuthToken(testToken);
        facade.sendLogoutRequest();
    }

    @Test
    public void createGameTest() {
        createUser();
        String testToken = createAuth();
        facade.setAuthToken(testToken);
        facade.sendCreateGameRequest("test name");
    }

    @Test
    public void listGamesTest() {
        createGame();
        String testToken = createAuth();
        facade.setAuthToken(testToken);
        facade.sendListGameRequest();
    }

}
