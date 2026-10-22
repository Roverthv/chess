package server;

import com.google.gson.Gson;
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
        javalin.get("/game", this::getGame);
    }

    private void getGame(@NotNull Context ctx) {
    }

    private void joinGame(@NotNull Context ctx) {
    }

    private void newGame(@NotNull Context ctx) {
    }

    private void login(@NotNull Context ctx) {
        var json = new Gson();
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
            ctx.result(json.toJson(e));
        } catch (UnauthorizedException e) {
            ctx.status(401);
            ctx.result(json.toJson(e));
        } catch (RuntimeException e) {
            ctx.status(500);
            ctx.result(json.toJson(e));
        }
    }

    private void logout(@NotNull Context ctx) {
        var json = new Gson();
        var authToken = ctx.header("Authorization");
        try {
            Admin.authenticate(authToken);
            Service.logout(authToken);
            ctx.removeHeader("Authorization");
            ctx.status(200);
            ctx.result();
        } catch (UnauthorizedException e) {
            ctx.status(401);
            ctx.result(json.toJson(e));
        } catch (RuntimeException e) {
            ctx.status(500);
            ctx.result(json.toJson(e));
        }
    }

    private void register(@NotNull Context ctx) {
        var json = new Gson();
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
            ctx.result(json.toJson(e));
        } catch (RuntimeException e) {
            ctx.status(500);
            ctx.result(json.toJson(e));
        }
    }

    private void clear(Context ctx) {
        var json = new Gson();
        try {
            Admin.clear();
            ctx.removeHeader("Authorization");
            ctx.status(200);
            ctx.result();
        } catch (RuntimeException e) {
            ctx.status(500);
            ctx.result(json.toJson(e));
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
