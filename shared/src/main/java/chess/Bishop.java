package chess;

public class Bishop extends ChessPiece {

    private static final int[][] movementPattern = {{1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}, {7, 7}, {-1, 1},
            {-2, 2}, {-3, 3}, {-4, 4}, {-5, 5}, {-6, 6}, {-7, 7}, {-1, -1}, {-2, -2}, {-3, -3}, {-4, -4}, {-5, -5},
            {-6, -6}, {-7, -7}, {1, -1}, {2, -2}, {3, -3}, {4, -4}, {5, -5}, {6, -6}, {7, -7}};
    public Bishop(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        super(pieceColor, type);
    }

    public static int[][] getMoveData(){
        return movementPattern;
    }

}

