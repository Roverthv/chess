package service;

import dataaccess.*;

import java.util.Random;

public class UserService {
    final UserDataAccess userDatabase;
    final AuthDataAccess authDatabase;
    final GameDataAccess gameDatabase;

    public UserService(UserDataAccess userDatabase, AuthDataAccess authDatabase, GameDataAccess gameDatabase) {
        this.userDatabase = userDatabase;
        this.authDatabase = authDatabase;
        this.gameDatabase = gameDatabase;
    }


    public void login(String username, String password) throws UnauthorizedException, DataAccessException {
        var user = userDatabase.getUser(username);
        if (user == null || !user.checkPassword(password)) {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    public void logout(String authToken) throws DataAccessException {
        authDatabase.removeAuthData(authToken);
    }

    public int createGame(String username, String gameName) throws DataAccessException {
        var gameID = makeGameID();
        gameDatabase.newGame(gameID, gameName);
        userDatabase.addGameToUser(username, gameID);
        return gameID;
    }

    public void joinGame(String username, int id, String team) throws DataAccessException, BadRequestException {
        gameDatabase.joinGame(username, id, team);
    }

    private int makeGameID() throws DataAccessException {

        int id;
        Random random = new Random();
        do {
            id = random.nextInt(10000);
        } while (gameDatabase.getGame(id) != null);
        return id;
    }
}
