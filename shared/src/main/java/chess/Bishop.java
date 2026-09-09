package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Bishop implements ChessPiece {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> moves = new ArrayList<>();
        int[][] offsets = { {1, 1}, {-1, -1}, {2, 2}, {-2, -2}, {3, 3}, {-3, -3}, {4, 4}, {-4, -4}, {5, 5}, {-5, -5}
                , {6, 6}, {-6, -6}, {7, 7}, {-7, -7}, {8, 8}, {-8, -8}};

        for (int[] offset : offsets) {
            ChessPosition target = myPosition.addOffset(offset[0], offset[1]);
            if (board.isValidMove(target, this.getTeamColor())) {
                moves.add(new ChessMove(myPosition, target, null));
            }
        }
        return moves;
    }
}
}
