package service;

import DataStructures.AuthData;
import dataaccess.DataAccess;

public class AdminService {
    DataAccess database;

    public AdminService(DataAccess database) {
        this.database = database;
    }

    public void authenticate(int authtoken) {
        var token = database.getAuthData(authtoken);
        if (token == null) {
            throw new UnauthorizedException("Invalid AuthToken");
        }
    }

    public void createAuth(String username) {
        int authToken = username.hashCode();
        var authdata = new AuthData(authToken, username);
        database.addAuthData(authdata);
    }

    public void deleteAuth(int authtoken) {
    }

    public void createUser(String username, String password) {
    }

    public void deleteUser(int authtoken, String username) {
    }

    public void clear() {
        database.clear();
    }
}
