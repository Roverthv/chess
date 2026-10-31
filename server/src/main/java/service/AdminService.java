package service;

import dataaccess.*;
import datastructures.AuthData;
import datastructures.GameData;
import datastructures.UserData;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

public class AdminService {
    final UserDataAccess userDatabase;
    final AuthDataAccess authDatabase;
    final GameDataAccess gameDatabase;

    public AdminService(UserDataAccess userDatabase, AuthDataAccess authDatabase, GameDataAccess gameDatabase) {
        this.userDatabase = userDatabase;
        this.authDatabase = authDatabase;
        this.gameDatabase = gameDatabase;
    }

    public String authenticate(String authToken) throws UnauthorizedException {
        var token = authDatabase.getAuthData(authToken);
        if (token == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return token.getUsername();
    }

    public String createAuth(String username) {
        String authToken = generateToken();
        var authdata = new AuthData(username, authToken);
        authDatabase.addAuthData(authdata);
        return authToken;
    }

    public String createUser(String username, String password, String email) throws DataAccessException {
        var exists = userDatabase.getUser(username);
        if (exists != null) {
            throw new DataAccessException("Error: already taken");
        }
        UserData newUser = new UserData(username, password, email, new ArrayList<>());
        userDatabase.register(newUser);
        return createAuth(username);

    }

    public List<GameData> getAllGames() throws DataAccessException {
        return new ArrayList<>(gameDatabase.getGames().values());
    }

    public void clear() throws DataAccessException {
        userDatabase.clear();
        authDatabase.clear();
        gameDatabase.clear();
    }

    private String generateToken() {
        String token;
        do {
            token = UUID.randomUUID().toString();
        } while (authDatabase.getAuthData(token) != null);
        return token;
    }
}
