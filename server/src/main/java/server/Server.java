package server;

import DataStructures.GameData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import io.javalin.*;
import io.javalin.http.Context;
import io.javalin.json.JavalinGson;
import org.jetbrains.annotations.NotNull;
import service.AdminService;
import service.UnauthorizedException;
import service.UserService;

import java.util.List;
import java.util.Map;

public class Server {

    private final DataAccess database;
    private final AdminService Admin;
    private final UserService Service;

    private final Javalin javalin;

    public Server() {
        this.database = new MemoryDataAccess();
        this.Admin = new AdminService(database);
        this.Service = new UserService(database);
        javalin = Javalin.create(config -> {
            config.staticFiles.add("web");
            config.jsonMapper(new JavalinGson());
        });

        // Register your endpoints and exception handlers here.
        javalin.delete("/db", this::clear);
        javalin.delete("/session", this::logout);
        javalin.post("/session", this::login);
        javalin.post("/user/", this::register);
        javalin.post("/game", this::newGame);
        javalin.put("/game", this::joinGame);
        javalin.get("/game", this::getGames);
    }

    private void getGames(@NotNull Context ctx) {
        var json = new GsonBuilder()
                .serializeNulls()
                .create();
        Map<String, String> error;
        var authToken = ctx.header("Authorization");
        try {
            Admin.authenticate(authToken);
            List<GameData> games = Admin.getAllGames();
            ctx.status(200);
            Map<String, List<GameData>> result = Map.of("games", games);
            ctx.result(json.toJson(result));
        } catch (UnauthorizedException e) {
            ctx.status(401);
            error = Map.of("Error", e.getMessage());
            ctx.result(json.toJson(error));
        } catch (RuntimeException e) {
            ctx.status(500);
            error = Map.of("Error", e.getMessage());
            ctx.result(json.toJson(error));
        }
    }

    private void joinGame(@NotNull Context ctx) {

    }

    private void newGame(@NotNull Context ctx) {
        var json = new Gson();
        Map<String, String> error;
        var authToken = ctx.header("Authorization");
        Map<String, String> body = ctx.bodyAsClass(Map.class);
        var gamename = body.get("gameName");
        try {
            String username = Admin.authenticate(authToken);
            var gameID = Service.createGame(username, gamename);
            ctx.status(200);
            Map<String, Integer> result = Map.of("gameID", gameID);
            ctx.result(json.toJson(result));
        } catch (UnauthorizedException e) {
            ctx.status(401);
            error = Map.of("Error", e.getMessage());
            ctx.result(json.toJson(error));
        } catch (RuntimeException e) {
            ctx.status(500);
            error = Map.of("Error", e.getMessage());
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
            Service.login(username, password);
            var authToken = Admin.createAuth(username);
            var authdata = database.getAuthData(authToken);
            ctx.header("Authorization", authToken);
            ctx.status(200);
            ctx.result(json.toJson(authdata));
        } catch (DataAccessException e) {
            ctx.status(400);
            error = Map.of("Error", e.getMessage());
            ctx.result(json.toJson(error));
        } catch (UnauthorizedException e) {
            ctx.status(401);
            error = Map.of("Error", e.getMessage());
            ctx.result(json.toJson(error));
        } catch (RuntimeException e) {
            ctx.status(500);
            error = Map.of("Error", e.getMessage());
            ctx.result(json.toJson(error));
        }
    }

    private void logout(@NotNull Context ctx) {
        var json = new Gson();
        Map<String, String> error;
        var authToken = ctx.header("Authorization");
        try {
            Admin.authenticate(authToken);
            Service.logout(authToken);
            ctx.removeHeader("Authorization");
            ctx.status(200);
            ctx.result();
        } catch (UnauthorizedException e) {
            ctx.status(401);
            error = Map.of("Error", e.getMessage());
            ctx.result(json.toJson(error));
        } catch (RuntimeException e) {
            ctx.status(500);
            error = Map.of("Error", e.getMessage());
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
            var authToken = Admin.createUser(username, password, email);
            var authdata = database.getAuthData(authToken);
            ctx.header("Authorization", authToken);
            ctx.status(200);
            ctx.result(json.toJson(authdata));
        } catch (DataAccessException e) {
            ctx.status(403);
            error = Map.of("Error", e.getMessage());
            ctx.result(json.toJson(error));
        } catch (RuntimeException e) {
            ctx.status(500);
            error = Map.of("Error", e.getMessage());
            ctx.result(json.toJson(error));
        }
    }

    private void clear(Context ctx) {
        var json = new Gson();
        Map<String, String> error;
        try {
            Admin.clear();
            ctx.removeHeader("Authorization");
            ctx.status(200);
            ctx.result();
        } catch (RuntimeException e) {
            ctx.status(500);
            error = Map.of("Error", e.getMessage());
            ctx.result(json.toJson(error));
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
