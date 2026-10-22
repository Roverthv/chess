package service;

import DataStructures.AuthData;
import DataStructures.UserData;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;

import java.util.UUID;
import java.util.ArrayList;

public class AdminService {
    DataAccess database;

    public AdminService(DataAccess database) {
        this.database = database;
    }

    public String authenticate(String authtoken) throws UnauthorizedException {
        var token = database.getAuthData(authtoken);
        if (token == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return token.getUsername();
    }

    public String createAuth(String username) {
        String authToken = generateToken();
        var authdata = new AuthData(username, authToken);
        database.addAuthData(authdata);
        return authToken;
    }

    public void deleteAuth(String authtoken) {
        database.removeAuthData(authtoken);
    }

    public String createUser(String username, String password, String email) throws DataAccessException {
        var exists = database.getUser(username);
        if (exists != null) {
            throw new DataAccessException("Error: already taken");
        }
        UserData newUser = new UserData(username, password, email, new ArrayList<String>());
        database.register(newUser);
        return createAuth(username);

    }

    public void deleteUser(String authtoken, String username) {
        database.removeUser(username);
    }

    public void clear() {
        database.clear();
    }

    private String generateToken() {
        String token;
        do {
            token = UUID.randomUUID().toString();
        } while (database.getAuthData(token) != null);
        return token;
    }
}
