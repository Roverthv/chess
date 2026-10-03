package chess;

import chess.MovementRules.CheckmateRules;

import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor turn;
    ChessBoard gameboard;
    public ChessGame() {
        gameboard = new ChessBoard();
        gameboard.resetBoard();
        turn = TeamColor.WHITE;
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
        if (toMove == null){ // || toMove.getTeamColor() != getTeamTurn()){
            return null;
        }
        Collection<ChessMove> moves = toMove.pieceMoves(getBoard(), startPosition);
        moves.removeIf(move -> moveInvalid(move, toMove.getTeamColor()));
        return moves;
    }

    private boolean moveInvalid(ChessMove move, TeamColor team){
        ChessBoard testBoard = testMove(move);
        ChessPosition KingSpot = testBoard.findKing(team);
        return CheckmateRules.checkCheck(testBoard, KingSpot, team);
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece pieceToMove = getBoard().getPiece(move.getStartPosition());
        if(pieceToMove == null || pieceToMove.getTeamColor() != getTeamTurn() || !validMoves(move.getStartPosition()).contains(move)){
            throw new InvalidMoveException("Illegal Move Detected");
        }
        getBoard().addPiece(move.getEndPosition(), pieceToMove);
        if(move.getPromotionPiece() != null){
            getBoard().getPiece(move.getEndPosition()).promote(move.getPromotionPiece());
        }
        getBoard().removePiece(move.getStartPosition());

        if(getTeamTurn() == TeamColor.WHITE){
            setTeamTurn(TeamColor.BLACK);
        } else if (getTeamTurn() == TeamColor.BLACK){
            setTeamTurn(TeamColor.WHITE);
        }
        else{
            throw new IllegalStateException("Unexpected value: " + getTeamTurn());
        }
    }

    private ChessBoard testMove(ChessMove move){
        ChessBoard testBoard = new ChessBoard(getBoard());
        testBoard.addPiece(move.getEndPosition(), testBoard.getPiece(move.getStartPosition()));
        if(move.getPromotionPiece() != null){
            testBoard.getPiece(move.getEndPosition()).promote(move.getPromotionPiece());
        }
        testBoard.removePiece(move.getStartPosition());
        return testBoard;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition KingSpot = getBoard().findKing(teamColor);
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
        return isInCheck(teamColor) && !hasValidMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && !hasValidMoves(teamColor);
    }

    private boolean hasValidMoves(TeamColor teamColor){
        for(ChessPosition position: getBoard().findTeamPositions(teamColor)){
            if(!validMoves(position).isEmpty()){
                return true;
            }
        }
        return false;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return turn == chessGame.turn && Objects.equals(gameboard, chessGame.gameboard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, gameboard);
    }

}
