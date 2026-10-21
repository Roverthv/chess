package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;

import java.util.Collection;

public class UserService {
    DataAccess database;

    public UserService(DataAccess database) {
        this.database = database;
    }


    public void login(String username, String password) throws DataAccessException, UnauthorizedException {
        var user = database.getUser(username);
        if (user == null) {
            throw new DataAccessException("Error: bad request");
        }
        if (!user.checkPassword(password)) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        //return Adminservice.create;
    }

    public void logout(String authtoken) {
    }

    public void createGame(String authtoken) {
    }

    public void joinGame(String authtoken, String id) {
    }

    public /*Collection<String>*/ void listGames(String authtoken) {
    }
}
