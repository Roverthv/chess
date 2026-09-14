package chess.MovementRules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;


public interface PiecePatterns {

    int[][][] movementPatterns = null;


    default Collection<ChessMove> findMoves(ChessBoard board, ChessPosition myPosition) {throw new RuntimeException("Not implemented");}
}
