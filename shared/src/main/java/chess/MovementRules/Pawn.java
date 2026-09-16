package chess.MovementRules;

import chess.*;

import java.util.ArrayList;
import java.util.List;

public class Pawn implements PiecePatterns {

    private static final int[][][] movementPatternsWhite = {{{1,0}, {2,0}},{{1,1},{1,-1}}};
    private static final int[][][] movementPatternsBlack = {{{-1,0}, {-2,0}},{{-1,-1},{-1,1}}};
    public Pawn() {}

    private static List<ChessMove> evaluateMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor, Boolean hasMoved, int[][][] movementPatterns){
        List<ChessMove> moves = new ArrayList<>();
        int startRow = 7;
        int promoteRow = 1;
        if (pieceColor == ChessGame.TeamColor.WHITE){
            startRow = 2;
            promoteRow = 8;
        }
//        if (!hasMoved && myPosition.getRow() == startRow){
//            offset = movementPatterns[0][0];
//            ChessPosition target = myPosition.addOffset(offset[0], offset[1]);
//            var valid = board.isValidMove(target, pieceColor);
//            if (valid[0] && !valid[1]) {
//                moves.add(new ChessMove(myPosition, target, null));
//            }
//        }
//        offset = movementPatterns[1][0];
//        ChessPosition target = myPosition.addOffset(offset[0], offset[1]);
//        var valid = board.isValidMove(target, pieceColor);
//        if (valid[0] && !valid[1]) {
//            if (target.getRow() == promoteRow){
//                // figure out how to have them select new piece type
//                moves.add(new ChessMove(myPosition, target, null));
//            }
//            moves.add(new ChessMove(myPosition, target, null));
//        }
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

    public static List<ChessMove> findMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor, Boolean hasMoved) {
        if (pieceColor == ChessGame.TeamColor.WHITE){
            return evaluateMoves(board, myPosition, pieceColor, hasMoved, movementPatternsWhite);
        }
        else if (pieceColor == ChessGame.TeamColor.BLACK) {
            return evaluateMoves(board, myPosition, pieceColor, hasMoved, movementPatternsBlack);
        }
        else {
            throw new RuntimeException("Not implemented");
        }
    }

}
