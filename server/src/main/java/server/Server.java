package server;

import dataaccess.*;
import dataaccess.sql.SQLAuthDataAccess;
import dataaccess.sql.SQLGameDataAccess;
import dataaccess.sql.SQLUserDataAccess;
import datastructures.GameData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.javalin.*;
import io.javalin.http.Context;
import io.javalin.json.JavalinGson;
import org.jetbrains.annotations.NotNull;
import service.AdminService;
import service.BadRequestException;
import service.UnauthorizedException;
import service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Server {

    final UserDataAccess userDatabase;
    final AuthDataAccess authDatabase;
    final GameDataAccess gameDatabase;
    private final AdminService admin;
    private final UserService service;

    private final Javalin javalin;

    public Server() {
        try {
            this.userDatabase = new SQLUserDataAccess();
            this.authDatabase = new SQLAuthDataAccess();
            this.gameDatabase = new SQLGameDataAccess();
            this.admin = new AdminService(userDatabase, authDatabase, gameDatabase);
            this.service = new UserService(userDatabase, authDatabase, gameDatabase);
            javalin = Javalin.create(config -> {
                config.staticFiles.add("web");
                config.jsonMapper(new JavalinGson());
            });

            // Register your endpoints and exception handlers here.
            javalin.delete("/db", this::clear);
            javalin.delete("/session", this::logout);
            javalin.post("/session", this::login);
            javalin.post("/user", this::register);
            javalin.post("/game", this::newGame);
            javalin.put("/game", this::joinGame);
            javalin.get("/game", this::getGames);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void getGames(@NotNull Context ctx) {
        var json = new GsonBuilder()
                .serializeNulls()
                .create();
        Map<String, String> error;
        var authToken = ctx.header("authorization");
        try {
            admin.authenticate(authToken);
            List<GameData> games = admin.getAllGames();
            ctx.status(200);
            Map<String, List<GameData>> result = Map.of("games", games);
            ctx.result(json.toJson(result));
        } catch (UnauthorizedException e) {
            ctx.status(401);
            error = Map.of("message", e.getMessage());
            ctx.result(json.toJson(error));
        } catch (RuntimeException | DataAccessException e) {
            ctx.status(500);
            error = Map.of("message", e.getMessage());
            ctx.result(json.toJson(error));
        }
    }

    private void joinGame(@NotNull Context ctx) {
        var json = new Gson();
        Map<String, String> error;
        var authToken = ctx.header("authorization");
        Map<String, Object> body = ctx.bodyAsClass(Map.class);
        try {
            checksForBadRequest(body, 2);
            Double gameID = (Double) body.get("gameID");
            String team = body.get("playerColor").toString();
            String username = admin.authenticate(authToken);
            service.joinGame(username, gameID.intValue(), team);
            ctx.status(200);
            ctx.result();
        } catch (UnauthorizedException e) {
            ctx.status(401);
            error = Map.of("message", e.getMessage());
            ctx.result(json.toJson(error));
        } catch (RuntimeException | DataAccessException e) {
            ctx.status(500);
            error = Map.of("message", e.getMessage());
            ctx.result(json.toJson(error));
        } catch (BadRequestException e) {
            alreadyTakenCheck(ctx, e.getMessage());
            error = Map.of("message", e.getMessage());
            ctx.result(json.toJson(error));
        }

    }

    private void newGame(@NotNull Context ctx) {
        var json = new Gson();
        Map<String, String> error;
        var authToken = ctx.header("authorization");
        Map<String, String> body = ctx.bodyAsClass(Map.class);
        var gameName = body.get("gameName");
        try {
            checkForBadRequest(body, 1);
            String username = admin.authenticate(authToken);
            var gameID = service.createGame(username, gameName);
            ctx.status(200);
            Map<String, Integer> result = Map.of("gameID", gameID);
            ctx.result(json.toJson(result));
        } catch (UnauthorizedException e) {
            ctx.status(401);
            error = Map.of("message", e.getMessage());
            ctx.result(json.toJson(error));
        } catch (RuntimeException | DataAccessException e) {
            ctx.status(500);
            error = Map.of("message", e.getMessage());
            ctx.result(json.toJson(error));
        } catch (BadRequestException e) {
            ctx.status(400);
            error = Map.of("message", e.getMessage());
            ctx.result(json.toJson(error));
        }
    }

    private void login(@NotNull Context ctx) {
        var json = new Gson();
        Map<String, String> error;
        Map<String, String> body = ctx.bodyAsClass(Map.class);
        var username = body.get("username");
        var password = body.get("password");
        try {
            checkForBadRequest(body, 2);
            service.login(username, password);
            var authToken = admin.createAuth(username);
            var authdata = authDatabase.getAuthData(authToken);
            ctx.header("authorization", authToken);
            ctx.status(200);
            ctx.result(json.toJson(authdata));
        } catch (BadRequestException e) {
            ctx.status(400);
            error = Map.of("message", e.getMessage());
            ctx.result(json.toJson(error));
        } catch (UnauthorizedException e) {
            ctx.status(401);
            error = Map.of("message", e.getMessage());
            ctx.result(json.toJson(error));
        } catch (RuntimeException | DataAccessException e) {
            ctx.status(500);
            error = Map.of("message", e.getMessage());
            ctx.result(json.toJson(error));
        }
    }

    private void logout(@NotNull Context ctx) {
        var json = new Gson();
        Map<String, String> error;
        var authToken = ctx.header("authorization");
        try {
            admin.authenticate(authToken);
            service.logout(authToken);
            ctx.removeHeader("authorization");
            ctx.status(200);
            ctx.result();
        } catch (UnauthorizedException e) {
            ctx.status(401);
            error = Map.of("message", e.getMessage());
            ctx.result(json.toJson(error));
        } catch (RuntimeException | DataAccessException e) {
            ctx.status(500);
            error = Map.of("message", e.getMessage());
            ctx.result(json.toJson(error));
        }
    }

    private void register(@NotNull Context ctx) {
        var json = new Gson();
        Map<String, String> error;
        Map<String, String> body = ctx.bodyAsClass(Map.class);
        var username = body.get("username");
        var password = body.get("password");
        var email = body.get("email");
        try {
            checkForBadRequest(body, 3);
            var authToken = admin.createUser(username, password, email);
            var authdata = authDatabase.getAuthData(authToken);
            ctx.header("authorization", authToken);
            ctx.status(200);
            ctx.result(json.toJson(authdata));
        } catch (RuntimeException | DataAccessException e) {
            ctx.status(500);
            error = Map.of("message", e.getMessage());
            ctx.result(json.toJson(error));
        } catch (BadRequestException e) {
            alreadyTakenCheck(ctx, e.getMessage());
            error = Map.of("message", e.getMessage());
            ctx.result(json.toJson(error));
        }
    }

    private void clear(Context ctx) {
        var json = new Gson();
        Map<String, String> error;
        try {
            admin.clear();
            ctx.removeHeader("authorization");
            ctx.status(200);
            ctx.result();
        } catch (RuntimeException | DataAccessException e) {
            ctx.status(500);
            error = Map.of("message", e.getMessage());
            ctx.result(json.toJson(error));
        }
    }

    private void checksForBadRequest(Map<String, Object> body, int numOfExParam) throws BadRequestException {
        if (body.size() != numOfExParam) {
            throw new BadRequestException("Error: bad request");
        }
        for (Object i : body.values()) {
            if (i == null || i.toString().isEmpty()) {
                throw new BadRequestException("Error: bad request");
            }
        }
    }

    private void checkForBadRequest(Map<String, String> body, int numOfExParam) throws BadRequestException {
        if (body.size() != numOfExParam) {
            throw new BadRequestException("Error: bad request");
        }
        for (String i : body.values()) {
            if (i == null || i.isEmpty()) {
                throw new BadRequestException("Error: bad request");
            }
        }
    }

    private void alreadyTakenCheck(Context ctx, String e) {
        if (Objects.equals(e, "Error: already taken")) {
            ctx.status(403);
        } else {
            ctx.status(400);
        }
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
