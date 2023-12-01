/**
 * Represents a location with row and column coordinates.
 * @author Liam Warren
 * @version 11.30.23
 */
public class Location {
    
    /** The row coordinate. */
    int r;

    /** The column coordinate. */
    int c;

    /**
     * Constructs a Location with specified row and column coordinates.
     *
     * @param r The row coordinate.
     * @param c The column coordinate.
     */
    public Location(int r, int c) {
        this.r = r;
        this.c = c;
    }

    /**
     * Gets the row coordinate of the location.
     *
     * @return The row coordinate.
     */
    public int getRow() {
        return r;
    }

    /**
     * Gets the column coordinate of the location.
     *
     * @return The column coordinate.
     */
    public int getColumn() {
        return c;
    }

    /**
     * Returns the next location based on the current location.
     * If the current location is at the last column and row, returns null.
     * If the current location is at the last column, moves to the next row and resets the column to 0.
     * Otherwise, moves to the next column.
     *
     * @return The next location.
     */
    public Location next() {
        if (r >= 8 && c >= 8) {
            return null;
        } else if (c >= 8) {
            return new Location(r + 1, 0);
        } else {
            return new Location(r, c + 1);
        }
    }

    /**
     * Returns a string representation of the location in the format "row, column".
     *
     * @return The string representation of the location.
     */
    public String toString() {
        return r + ", " + c;
    }

}
