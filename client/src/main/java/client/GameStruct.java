package client;

import java.util.Objects;

public record GameStruct(int gameID, String blackUsername, String whiteUsername, String gameName) {
    public int getGameID() {
        return gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameStruct that = (GameStruct) o;
        return gameID == that.gameID && Objects.equals(gameName, that.gameName) && Objects.equals(blackUsername, that.blackUsername) && Objects.equals(whiteUsername, that.whiteUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, blackUsername, whiteUsername, gameName);
    }
}
