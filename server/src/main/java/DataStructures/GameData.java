package DataStructures;

import chess.ChessGame;
import dataaccess.DataAccessException;

import java.util.Objects;

public class GameData {
    private int gameID;
    private String BlackPlayer;
    private String WhitePlayer;
    private String GameName;

    public GameData(int gameID, String gamename) {
        this.GameName = gamename;
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameName(String name) {
        this.GameName = name;
    }

    public String getGameName() {
        return GameName;
    }

    public void setPlayer(String username, ChessGame.TeamColor team) throws DataAccessException {
        if (team == ChessGame.TeamColor.BLACK) {
            if (BlackPlayer == null) {
                BlackPlayer = username;
            } else {
                throw new DataAccessException("Team Already taken");
            }
        }
        if (team == ChessGame.TeamColor.WHITE) {
            if (WhitePlayer == null) {
                WhitePlayer = username;
            } else {
                throw new DataAccessException("Team Already taken");
            }
        }
    }

    public String getPlayer(ChessGame.TeamColor team) {
        if (team == ChessGame.TeamColor.BLACK) {
            return BlackPlayer;
        }
        if (team == ChessGame.TeamColor.WHITE) {
            return WhitePlayer;
        } else {
            throw new RuntimeException("Invalid Team");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameData gameData = (GameData) o;
        return gameID == gameData.gameID && Objects.equals(BlackPlayer, gameData.BlackPlayer) && Objects.equals(WhitePlayer, gameData.WhitePlayer) && Objects.equals(GameName, gameData.GameName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, BlackPlayer, WhitePlayer, GameName);
    }
}
