package chess.MovementRules;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;


public interface PiecePatterns {

    int[][][] movementPatterns = null;

    default int[][][] getMoveData(){
        return movementPatterns;
    }

    static Collection<ChessMove> findMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {throw new RuntimeException("Not implemented");}
}
