package chess.MovementRules;

import chess.*;

import java.util.Collection;

public class CheckmateRules {

    public static boolean checkCheck(ChessBoard board, ChessPosition position, ChessGame.TeamColor team){
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                ChessPosition enemyPosition = new ChessPosition(i,j);
                ChessPiece enemyPiece = board.getPiece(enemyPosition);
                if(enemyPiece == null || enemyPiece.getTeamColor()== team){
                    continue;
                }
                Collection<ChessMove> enemyMoves = enemyPiece.pieceMoves(board,enemyPosition);
                for(ChessMove move: enemyMoves){
                    if(move.getEndPosition() == position){
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
