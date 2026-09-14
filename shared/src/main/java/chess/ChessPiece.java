package chess;

import chess.MovementRules.Bishop;
import chess.MovementRules.Queen;
import chess.MovementRules.Rook;

import java.util.Collection;
import java.util.List;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //throw new RuntimeException("Not implemented");
        // create library of movements for each type and create checks for validity
        List<ChessMove> moves;
        switch(type) {
            case PieceType.KING -> throw new RuntimeException("Not implemented");
            case PieceType.QUEEN -> moves = Queen.findMoves(board, myPosition, pieceColor);
            case PieceType.BISHOP -> moves = Bishop.findMoves(board, myPosition, pieceColor);
            case PieceType.KNIGHT -> throw new RuntimeException("Not implemented");
            case PieceType.ROOK -> moves = Rook.findMoves(board, myPosition, pieceColor);
            case PieceType.PAWN -> throw new RuntimeException("Not implemented");
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }

        return moves;
    }

}
