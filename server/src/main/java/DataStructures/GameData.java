package DataStructures;

import dataaccess.DataAccessException;
import service.BadRequestException;

import java.util.Objects;

public class GameData {
    private int gameID;
    private String blackUsername;
    private String whiteUsername;
    private String gameName;

    public GameData(int gameID, String gamename) {
        this.gameName = gamename;
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameName(String name) {
        this.gameName = name;
    }

    public String getGameName() {
        return gameName;
    }

    public void setPlayer(String username, String team) throws DataAccessException, BadRequestException {
        if (Objects.equals(team, "WHITE/BLACK")) {
            if (whiteUsername == null) {
                whiteUsername = username;
            } else if (blackUsername == null) {
                blackUsername = username;
            } else {
                throw new DataAccessException("Error: already taken");
            }
        }
        if (Objects.equals(team, "BLACK")) {
            if (blackUsername == null) {
                blackUsername = username;
            } else {
                throw new DataAccessException("Error: already taken");
            }
        }
        if (Objects.equals(team, "WHITE")) {
            if (whiteUsername == null) {
                whiteUsername = username;
            } else {
                throw new DataAccessException("Error: already taken");
            }
        } else {
            throw new BadRequestException("Error: bad request");
        }
    }

    public String getPlayer(String team) throws BadRequestException {
        if (Objects.equals(team, "BLACK")) {
            return blackUsername;
        }
        if (Objects.equals(team, "WHITE")) {
            return whiteUsername;
        } else {
            throw new BadRequestException("Error: bad request");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameData gameData = (GameData) o;
        return gameID == gameData.gameID && Objects.equals(blackUsername, gameData.blackUsername) && Objects.equals(whiteUsername, gameData.whiteUsername) && Objects.equals(gameName, gameData.gameName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, blackUsername, whiteUsername, gameName);
    }
}
