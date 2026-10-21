package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import io.javalin.*;
import io.javalin.http.Context;
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
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.delete("/db", this::clear);
        javalin.delete("/session", this::logout);
        javalin.post("/session/{username}/{password}", this::login);
        javalin.post("/user/{username}/{password}", this::register);
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
        var username = ctx.pathParam("username");
        var password = ctx.pathParam("password");
        try {
            Service.login(username, password);
        } catch (DataAccessException e) {
            ctx.status(400);
            ctx.result(json.toJson(e));
        } catch (UnauthorizedException e) {
            ctx.status(401);
            ctx.result(json.toJson(e));
        }
        var authToken = Admin.createAuth(username);
        var authdata = database.getAuthData(authToken);
        ctx.header(json.toJson(authToken));
        ctx.status(200);
        ctx.result(json.toJson(authdata));

    }

    private void logout(@NotNull Context ctx) {
    }

    private void register(@NotNull Context ctx) {
    }

    private void clear(Context ctx) {
        var r = Map.of();
        var json = new Gson();
        ctx.json(json.toJson(r));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
