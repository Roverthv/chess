package chess.MovementRules;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.List;

public class King implements PiecePatterns {

    private static final int[][] movementPatterns = {{1, 0}, {-1, 0},
            {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    public King() {}

    public static int[][] getMoveData(){
        return movementPatterns;
    }

    public static List<ChessMove> findMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        List<ChessMove> moves = new ArrayList<>();
        for (int[] offset : movementPatterns) {
            ChessPosition target = myPosition.addOffset(offset[0], offset[1]);
            var valid = board.isValidMove(target, pieceColor);
            if (valid[0] && !valid[1]) {
                moves.add(new ChessMove(myPosition, target, null));
            }
            else if(valid[0] && valid[1]){
                moves.add(new ChessMove(myPosition, target, null));
            }
        }
        return moves;
    }
}
