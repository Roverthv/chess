package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Bishop extends ChessPiece {

    private static final int[][] movementPattern = {{1, 1}, {-1, -1}, {2, 2}, {-2, -2}, {3, 3}, {-3, -3}, {4, 4}, {-4, -4},
            {5, 5}, {-5, -5}, {6, 6}, {-6, -6}, {7, 7}, {-7, -7}, {8, 8}, {-8, -8}, {-1, 1}, {1, -1}, {-2, 2}, {2, -2},
            {-3, 3}, {3, -3}, {-4, 4}, {4, -4}, {-5, 5}, {5, -5}, {-6, 6}, {6, -6}, {-7, 7}, {7, -7}, {-8, 8}, {8, -8}};
    public Bishop(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        super(pieceColor, type);
    }

    public static int[][] getMoveData(){
        return movementPattern;
    }

}

