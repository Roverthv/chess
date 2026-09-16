package chess.MovementRules;

import chess.*;

import java.util.ArrayList;
import java.util.List;

public class MoveRules {

    private static final int[][][] KingPatterns = {{{1, 0}}, {{-1, 0}},
            {{0, 1}}, {{0, -1}}, {{1, 1}}, {{1, -1}}, {{-1, 1}}, {{-1, -1}}};

    private static final int[][][] QueenPatterns = {{{1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}, {7, 7}},
            {{-1, 1}, {-2, 2}, {-3, 3}, {-4, 4}, {-5, 5}, {-6, 6}, {-7, 7}}, {{-1, -1}, {-2, -2}, {-3, -3}, {-4, -4},
            {-5, -5}, {-6, -6}, {-7, -7}}, {{1, -1}, {2, -2}, {3, -3}, {4, -4}, {5, -5}, {6, -6}, {7, -7}}, {{1, 0},
            {2, 0}, {3, 0}, {4, 0}, {5, 0}, {6, 0}, {7, 0}}, {{0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}, {0, 7}},
            {{-1, 0}, {-2, 0}, {-3, 0}, {-4, 0}, {-5, 0}, {-6, 0}, {-7, 0}}, {{0, -1}, {0, -2}, {0, -3}, {0, -4},
            {0, -5}, {0, -6}, {0, -7}}};

    private static final int[][][] BishopPatterns = {{{1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}, {7, 7}},
            {{-1, 1}, {-2, 2}, {-3, 3}, {-4, 4}, {-5, 5}, {-6, 6}, {-7, 7}}, {{-1, -1}, {-2, -2}, {-3, -3}, {-4, -4},
            {-5, -5}, {-6, -6}, {-7, -7}}, {{1, -1}, {2, -2}, {3, -3}, {4, -4}, {5, -5}, {6, -6}, {7, -7}}};

    private static final int[][][] RookPatterns = {{{1, 0}, {2, 0}, {3, 0}, {4, 0}, {5, 0}, {6, 0}, {7, 0}},
            {{0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}, {0, 7}}, {{-1, 0}, {-2, 0}, {-3, 0}, {-4, 0}, {-5, 0},
            {-6, 0}, {-7, 0}}, {{0, -1}, {0, -2}, {0, -3}, {0, -4}, {0, -5}, {0, -6}, {0, -7}}};

    private static final int[][][] KnightPatterns = {{{2, 1}}, {{2, -1}},
            {{-1, 2}}, {{-1, -2}}, {{-2, -1}}, {{-2, 1}}, {{1, -2}}, {{1, 2}}};

    private static final int[][][] PawnPatternsWhite = {{{1,0}, {2,0}},{{1,1},{1,-1}}};
    private static final int[][][] PawnPatternsBlack = {{{-1,0}, {-2,0}},{{-1,-1},{-1,1}}};

    public static List<ChessMove> findMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor, int[][][] movementPatterns) {
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

    private static List<ChessMove> evaluatePawnMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor, Boolean hasMoved, int[][][] movementPatterns){
        List<ChessMove> moves = new ArrayList<>();
        int startRow = 7;
        int promoteRow = 1;
        if (pieceColor == ChessGame.TeamColor.WHITE){
            startRow = 2;
            promoteRow = 8;
        }
        for (int i = 0; i < 2; i++){
            int[] offset = movementPatterns[0][i];
            if (i == 1 && (hasMoved || myPosition.getRow() != startRow)){
                break;
            }
            ChessPosition target = myPosition.addOffset(offset[0], offset[1]);
            var valid = board.isValidMove(target, pieceColor);
            if (valid[0] && !valid[1]) {
                if (target.getRow() == promoteRow){
                    moves.add(new ChessMove(myPosition, target, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, target, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, target, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, target, ChessPiece.PieceType.KNIGHT));
                }
                else {
                    moves.add(new ChessMove(myPosition, target, null));
                }
            }
            else {
                break;
            }
        }
        for (int[] offsets : movementPatterns[1]) {
            ChessPosition target = myPosition.addOffset(offsets[0], offsets[1]);
            var valid = board.isValidMove(target, pieceColor);
            if(valid[0] && valid[1]){
                if (target.getRow() == promoteRow){
                    moves.add(new ChessMove(myPosition, target, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, target, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, target, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, target, ChessPiece.PieceType.KNIGHT));
                }
                else {
                    moves.add(new ChessMove(myPosition, target, null));
                }
            }
        }
        return moves;
    }

    public static List<ChessMove> findPawnMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor, Boolean hasMoved) {
        if (pieceColor == ChessGame.TeamColor.WHITE){
            return evaluatePawnMoves(board, myPosition, pieceColor, hasMoved, PawnPatternsWhite);
        }
        else if (pieceColor == ChessGame.TeamColor.BLACK) {
            return evaluatePawnMoves(board, myPosition, pieceColor, hasMoved, PawnPatternsBlack);
        }
        else {
            throw new RuntimeException("Not implemented");
        }
    }

    public static List<ChessMove> determineMoves(ChessBoard board, ChessPosition myPosition, ChessPiece.PieceType type, ChessGame.TeamColor pieceColor, Boolean hasMoved){
        List<ChessMove> moves;
        switch(type) {
            case ChessPiece.PieceType.KING -> moves = findMoves(board, myPosition, pieceColor, KingPatterns);
            case ChessPiece.PieceType.QUEEN -> moves = findMoves(board, myPosition, pieceColor, QueenPatterns);
            case ChessPiece.PieceType.BISHOP -> moves = findMoves(board, myPosition, pieceColor, BishopPatterns);
            case ChessPiece.PieceType.KNIGHT -> moves = findMoves(board, myPosition, pieceColor, KnightPatterns);
            case ChessPiece.PieceType.ROOK -> moves = findMoves(board, myPosition, pieceColor, RookPatterns);
            case ChessPiece.PieceType.PAWN -> moves = findPawnMoves(board, myPosition, pieceColor, hasMoved);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
        return moves;
    }
}
