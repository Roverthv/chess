package service;

import DataStructures.GameData;
import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;

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
        database.removeAuthData(authtoken);
    }

    public void createGame(String username) {
        var gameID = makeGameID();
        //add game to userlist and database
        database.newGame(gameID);
        database.getUser(username).addGame(gameID);
    }

    public void joinGame(String authtoken, String id) {
    }

    public /*Collection<String>*/ void listGames(String authtoken) {
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
