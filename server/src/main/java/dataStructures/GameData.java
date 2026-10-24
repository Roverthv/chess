package dataStructures;

import dataaccess.DataAccessException;
import service.BadRequestException;

import java.util.Objects;

public class GameData {
    private final int gameID;
    private String blackUsername;
    private String whiteUsername;
    private final String gameName;

    public GameData(int gameID, String gameName) {
        this.gameName = gameName;
        this.gameID = gameID;
    }

//    public int getGameID() {
//        return gameID;
//    }
//
//    public void setGameName(String name) {
//        this.gameName = name;
//    }
//
//    public String getGameName() {
//        return gameName;
//    }

    public void setPlayer(String username, String team) throws DataAccessException, BadRequestException {
        switch (team) {
            case "WHITE/BLACK" -> {
                if (whiteUsername == null) {
                    whiteUsername = username;
                } else if (blackUsername == null) {
                    blackUsername = username;
                } else {
                    throw new DataAccessException("Error: already taken");
                }
            }
            case "BLACK" -> {
                if (blackUsername == null) {
                    blackUsername = username;
                } else {
                    throw new DataAccessException("Error: already taken");
                }
            }
            case "WHITE" -> {
                if (whiteUsername == null) {
                    whiteUsername = username;
                } else {
                    throw new DataAccessException("Error: already taken");
                }
            }
            case null, default -> throw new BadRequestException("Error: bad request");
        }
    }

//    public String getPlayer(String team) throws BadRequestException {
//        if (Objects.equals(team, "BLACK")) {
//            return blackUsername;
//        }
//        if (Objects.equals(team, "WHITE")) {
//            return whiteUsername;
//        } else {
//            throw new BadRequestException("Error: bad request");
//        }
//    }

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
