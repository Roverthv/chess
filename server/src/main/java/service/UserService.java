package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;

import java.util.Random;

public class UserService {
    final DataAccess database;

    public UserService(DataAccess database) {
        this.database = database;
    }


    public void login(String username, String password) throws UnauthorizedException {
        var user = database.getUser(username);
        if (user == null || !user.checkPassword(password)) {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    public void logout(String authToken) {
        database.removeAuthData(authToken);
    }

    public int createGame(String username, String gameName) {
        var gameID = makeGameID();
        database.newGame(gameID, gameName);
        database.addGameToUser(username, gameID);
        return gameID;
    }

    public void joinGame(String username, int id, String team) throws DataAccessException, BadRequestException {
        database.getGameData(id).setPlayer(username, team);
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
