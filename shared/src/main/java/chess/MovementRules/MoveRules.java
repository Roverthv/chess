package chess.MovementRules;

import chess.*;

import java.util.List;

public class MoveRules {

    public static List<ChessMove> determineMoves(ChessBoard board, ChessPosition myPosition, ChessPiece.PieceType type, ChessGame.TeamColor pieceColor, Boolean hasMoved){
        List<ChessMove> moves;
        switch(type) {
            case ChessPiece.PieceType.KING -> moves = King.findMoves(board, myPosition, pieceColor);
            case ChessPiece.PieceType.QUEEN -> moves = Queen.findMoves(board, myPosition, pieceColor);
            case ChessPiece.PieceType.BISHOP -> moves = Bishop.findMoves(board, myPosition, pieceColor);
            case ChessPiece.PieceType.KNIGHT -> moves = Knight.findMoves(board, myPosition, pieceColor);
            case ChessPiece.PieceType.ROOK -> moves = Rook.findMoves(board, myPosition, pieceColor);
            case ChessPiece.PieceType.PAWN -> moves = Pawn.findMoves(board,myPosition,pieceColor, hasMoved);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
        return moves;
    }
}
