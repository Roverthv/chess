package chess;

import chess.MovementRules.CheckmateRules;

import java.util.Collection;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor turn;
    ChessBoard gameboard;
    ChessPosition WhiteKing;
    ChessPosition BlackKing;
    public ChessGame() {
        gameboard = new ChessBoard();
        turn = TeamColor.WHITE;
        WhiteKing = new ChessPosition(1,5);
        BlackKing = new ChessPosition(8,5);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece toMove = getBoard().getPiece(startPosition);
        if (toMove.getTeamColor() != getTeamTurn()){
            return null;
        }
        return toMove.pieceMoves(getBoard(), startPosition);
    // right now just base move rules.
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        getBoard().addPiece(move.getEndPosition(), getBoard().getPiece(move.getStartPosition()));
        if(move.getPromotionPiece() != null){
            getBoard().getPiece(move.getEndPosition()).promote(move.getPromotionPiece());
        }
        getBoard().removePiece(move.getEndPosition());
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition KingSpot;
        if(teamColor == TeamColor.WHITE){
            KingSpot = WhiteKing;
        }
        else if(teamColor == TeamColor.BLACK){
            KingSpot = BlackKing;
        }
        else{
            throw new IllegalStateException("Unexpected value: " + teamColor);
        }
        return CheckmateRules.checkCheck(getBoard(), KingSpot, teamColor);
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameboard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameboard;
    }
}
