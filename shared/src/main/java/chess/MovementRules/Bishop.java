package chess.MovementRules;

import chess.*;

import java.util.ArrayList;
import java.util.List;

public class Bishop implements PiecePatterns {

    private static final int[][][] movementPatterns = {{{1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}, {7, 7}}, {{-1, 1},
            {-2, 2}, {-3, 3}, {-4, 4}, {-5, 5}, {-6, 6}, {-7, 7}}, {{-1, -1}, {-2, -2}, {-3, -3}, {-4, -4}, {-5, -5},
            {-6, -6}, {-7, -7}}, {{1, -1}, {2, -2}, {3, -3}, {4, -4}, {5, -5}, {6, -6}, {7, -7}}};
    public Bishop() {}

    public static int[][][] getMoveData(){
        return movementPatterns;
    }

    public static List<ChessMove> findMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        List<ChessMove> moves = new ArrayList<>();
        for (int[][] pattern : movementPatterns) {
            for (int[] offset : pattern) {
                ChessPosition target = myPosition.addOffset(offset[0], offset[1]);
                var valid = board.isValidMove(target, pieceColor);
                if (valid[0] && !valid[1]) {
                    moves.add(new ChessMove(myPosition, target, null));
                }
                else if(valid[0] && valid[1]){
                    moves.add(new ChessMove(myPosition, target, null));
                    break;
                }
                else{
                    break;
                }
            }
        }
        return moves;
    }
}

