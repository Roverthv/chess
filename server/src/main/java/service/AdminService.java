package service;

import DataStructures.AuthData;
import DataStructures.UserData;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;

import java.util.ArrayList;

public class AdminService {
    DataAccess database;

    public AdminService(DataAccess database) {
        this.database = database;
    }

    public void authenticate(int authtoken) throws UnauthorizedException {
        var token = database.getAuthData(authtoken);
        if (token == null) {
            throw new UnauthorizedException("Invalid AuthToken");
        }
    }

    public int createAuth(String username) {
        int authToken = username.hashCode();
        var authdata = new AuthData(authToken, username);
        database.addAuthData(authdata);
        return authToken;
    }

    public void deleteAuth(int authtoken) {
        database.removeAuthData(authtoken);
    }

    public int createUser(String username, String password) throws DataAccessException {
        var exists = database.getUser(username);
        if (exists == null) {
            throw new DataAccessException("Username is Taken");
        }
        UserData newUser = new UserData(username, password, new ArrayList<String>());
        database.register(newUser);
        return createAuth(username);

    }

    public void deleteUser(int authtoken, String username) {
        database.removeUser(username);
    }

    public void clear() {
        database.clear();
    }
}
