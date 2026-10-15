package DataStructures;

import chess.ChessGame;
import dataaccess.DataAccessException;

import java.util.Objects;

public class GameData {
    ChessGame game;
    String BlackPlayer;
    String WhitePlayer;
    GameData(ChessGame game){
        this.game = game;
    }

    void setPlayer(String username, ChessGame.TeamColor team) throws DataAccessException{
        if (team == ChessGame.TeamColor.BLACK){
            if (BlackPlayer == null){BlackPlayer = username;}
            else{throw new DataAccessException("Team Already taken");}
        }
        if (team == ChessGame.TeamColor.WHITE){
            if (WhitePlayer == null){WhitePlayer = username;}
            else{throw new DataAccessException("Team Already taken");}
        }
    }

    String getPlayer(ChessGame.TeamColor team){
        if (team == ChessGame.TeamColor.BLACK){return BlackPlayer;}
        if (team == ChessGame.TeamColor.WHITE){return WhitePlayer;}
        else {throw new RuntimeException("Invalid Team");}
    }

    ChessGame getGame(){return game;}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameData gameData = (GameData) o;
        return Objects.equals(game, gameData.game) && Objects.equals(BlackPlayer, gameData.BlackPlayer) && Objects.equals(WhitePlayer, gameData.WhitePlayer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(game, BlackPlayer, WhitePlayer);
    }
}
