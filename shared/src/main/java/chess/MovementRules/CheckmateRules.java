package chess.MovementRules;

import chess.*;

import java.util.Objects;

public class CheckmateRules {

    public static boolean checkCheck(ChessBoard board, ChessPosition position, ChessGame.TeamColor team){
        return checkLines(board, position, team, "Straight") ||
                checkLines(board, position, team, "Diagonal") ||
                checkKnightsAndKing(board, position, team) ||
                checkPawns(board, position, team);
    }

    private static boolean checkLines(ChessBoard board, ChessPosition position, ChessGame.TeamColor team, String direction){
        ChessPiece.PieceType threat;
        int[][][] movementPatterns;
        if(Objects.equals(direction, "Straight")){
            threat = ChessPiece.PieceType.ROOK;
            movementPatterns = MoveRules.getPatterns(ChessPiece.PieceType.ROOK);
        }
        else if (Objects.equals(direction, "Diagonal")){
            threat = ChessPiece.PieceType.BISHOP;
            movementPatterns = MoveRules.getPatterns(ChessPiece.PieceType.BISHOP);
        }
        else{throw new IllegalStateException("Unexpected value: " + direction);}
        ChessPiece.PieceType[] threats = {ChessPiece.PieceType.QUEEN, threat};

        return checkPattern(board, position, team, movementPatterns, threats);
    }


    private static boolean checkKnightsAndKing(ChessBoard board, ChessPosition position, ChessGame.TeamColor team){
        boolean inCheck = false;
        int[][][] movementPatterns = MoveRules.getPatterns(ChessPiece.PieceType.KING);
        ChessPiece.PieceType[] threats ={ChessPiece.PieceType.KING};
        inCheck = checkPattern(board, position,team, movementPatterns, threats);
        if(inCheck){
            return inCheck;
        }
        else{
            movementPatterns = MoveRules.getPatterns(ChessPiece.PieceType.KNIGHT);
            threats[0] = ChessPiece.PieceType.KNIGHT;
            return checkPattern(board, position,team, movementPatterns, threats);
        }
    }

    private static boolean checkPawns(ChessBoard board, ChessPosition position, ChessGame.TeamColor team){
        int[][][] movementPatterns = {{}};
        movementPatterns[0][0] = MoveRules.getPawnPatterns(team)[0][1];
        ChessPiece.PieceType[] threats = {ChessPiece.PieceType.PAWN};
        return checkPattern(board, position, team, movementPatterns, threats);
    }

    private static boolean checkPattern(ChessBoard board, ChessPosition position, ChessGame.TeamColor team, int[][][] movementPatterns, ChessPiece.PieceType[] threats){
        for(int[][] pattern : movementPatterns){
            line:
            for(int[] offset : pattern){
                ChessPosition attack = position.addOffset(offset[0], offset[1]);
                ChessPiece attacker = board.getPiece(attack);
                if(attacker != null){
                    if (attacker.getTeamColor() == team){
                        break;
                    }
                    for (ChessPiece.PieceType threat : threats){
                        if (attacker.getPieceType() == threat){
                            return true;
                        }
                        else{
                            break line;
                        }
                    }

                }
            }
        }
        return false;
    }


}
