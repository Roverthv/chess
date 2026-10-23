package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;

import java.util.Random;

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
    }

    public void logout(String authtoken) {
        database.removeAuthData(authtoken);
    }

    public int createGame(String username, String gamename) {
        var gameID = makeGameID();
        database.newGame(gameID, gamename);
        database.getUser(username).addGame(gameID);
        return gameID;
    }

    public void joinGame(String authtoken, String id) {
    }

    private String getUsername(String authToken) {
        return database.getAuthData(authToken).getUsername();
    }

    private int makeGameID() {

        int id;
        Random random = new Random();
        do {
            id = random.nextInt(10000);
        } while (database.getGame(id) != null);
        return id;
    }
}
