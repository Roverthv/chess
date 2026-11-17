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

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static UserDataAccess userDatabase;
    private static AuthDataAccess authDatabase;
    private static GameDataAccess gameDatabase;
    private static AdminService admin;
    private static UserService service;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
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

    private double createGame() {
        try {
            createUser();
            return service.createGame("test user", "test game");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void registerPassesTest() {
        assertDoesNotThrow(() -> facade.sendRegisterRequest("test user", "test password", "test email"));
    }

    @Test
    public void registerTakenUsernameTest() {
        createUser();
        assertThrows(ClientEndException.class, () -> facade.sendRegisterRequest("test user", "test password", "test email"));
    }

    @Test
    public void registerMissingUsernameTest() {
        createUser();
        assertThrows(ClientEndException.class, () -> facade.sendRegisterRequest("", "test password", "test email"));
    }

    @Test
    public void registerMissingPasswordTest() {
        createUser();
        assertThrows(ClientEndException.class, () -> facade.sendRegisterRequest("test user", "", "test email"));
    }

    @Test
    public void registerMissingEmailTest() {
        createUser();
        assertThrows(ClientEndException.class, () -> facade.sendRegisterRequest("test user", "test password", ""));
    }

    @Test
    public void loginPassesTest() {
        createUser();
        assertDoesNotThrow(() -> facade.sendLoginRequest("test user", "test password"));
    }

    @Test
    public void loginIncorrectPasswordTest() {
        createUser();
        assertThrows(ClientEndException.class, () -> facade.sendLoginRequest("test user", "password"));
    }

    @Test
    public void loginMissingPasswordTest() {
        createUser();
        assertThrows(ClientEndException.class, () -> facade.sendLoginRequest("test user", ""));
    }

    @Test
    public void loginMissingUsernameTest() {
        createUser();
        assertThrows(ClientEndException.class, () -> facade.sendLoginRequest("", "test password"));
    }

    @Test
    public void logoutPassesTest() {
        String testToken = createAuth();
        facade.setAuthToken(testToken);
        assertDoesNotThrow(() -> facade.sendLogoutRequest());
    }

    @Test
    public void logoutFailsTest() {
        createAuth();
        facade.setAuthToken("testToken");
        assertThrows(ClientEndException.class, () -> facade.sendLogoutRequest());
    }

    @Test
    public void createGamePassesTest() {
        createUser();
        String testToken = createAuth();
        facade.setAuthToken(testToken);
        assertDoesNotThrow(() -> facade.sendCreateGameRequest("test name"));
    }

    @Test
    public void createGameInvalidAuthTokenTest() {
        createUser();
        createAuth();
        facade.setAuthToken("testToken");
        assertThrows(ClientEndException.class, () -> facade.sendCreateGameRequest("test name"));
    }

    @Test
    public void createGameInvalidNameTest() {
        createUser();
        String testToken = createAuth();
        facade.setAuthToken(testToken);
        assertThrows(ClientEndException.class, () -> facade.sendCreateGameRequest(""));
    }

    @Test
    public void listGamesPassesTest() {
        createGame();
        String testToken = createAuth();
        facade.setAuthToken(testToken);
        assertDoesNotThrow(() -> facade.sendListGameRequest());
    }

    @Test
    public void listGamesNoGamesTest() {
        String testToken = createAuth();
        facade.setAuthToken(testToken);
        assertDoesNotThrow(() -> facade.sendListGameRequest());
    }

    @Test
    public void joinGamePassesWhiteTest() {
        double gameID = createGame();
        String testToken = createAuth();
        facade.setAuthToken(testToken);
        assertDoesNotThrow(() -> facade.sendJoinGameRequest(gameID, "WHITE"));
    }

    @Test
    public void joinGamePassesBlackTest() {
        double gameID = createGame();
        String testToken = createAuth();
        facade.setAuthToken(testToken);
        assertDoesNotThrow(() -> facade.sendJoinGameRequest(gameID, "BLACK"));
    }

    @Test
    public void joinGameInvalidIDTest() {
        createGame();
        String testToken = createAuth();
        facade.setAuthToken(testToken);
        assertThrows(ClientEndException.class, () -> facade.sendJoinGameRequest(1321323, "WHITE"));
    }

    @Test
    public void joinGameInvalidColorTest() {
        double gameID = createGame();
        String testToken = createAuth();
        facade.setAuthToken(testToken);
        assertThrows(ClientEndException.class, () -> facade.sendJoinGameRequest(gameID, "uiewfh"));
    }

    @Test
    public void observeGamePassesTest() {
        double gameID = createGame();
        String testToken = createAuth();
        facade.setAuthToken(testToken);
        assertDoesNotThrow(() -> facade.sendObserveGameRequest(gameID));
    }

    @Test
    public void observeGameInvalidIDTest() {
        createGame();
        String testToken = createAuth();
        facade.setAuthToken(testToken);
        assertThrows(ClientEndException.class, () -> facade.sendObserveGameRequest(23958257));
    }

}
