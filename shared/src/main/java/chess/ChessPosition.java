package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;// figure out row and column
    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left column
     */
    public int getColumn() {
        return col;
    }

    public ChessPosition addOffset(int rowOffset, int colOffset ){
        int adjustedRow = this.row + rowOffset;
        int adjustedCol = this.col + colOffset;
        return new ChessPosition(adjustedRow, adjustedCol);
    }

    @Override
    public boolean equals(Object p){
        if (this == p) {return true;}
        if (p == null || getClass() != p.getClass()) {return false;}
        ChessPosition pos = (ChessPosition) p;
        return (row == pos.getRow() && col == pos.getColumn());
    }

    @Override
    public int hashCode(){
        return (67 * row) + (83*col);
    }

    @Override
    public String toString(){
        return String.format("%d,%d", row, col);
    }
}
