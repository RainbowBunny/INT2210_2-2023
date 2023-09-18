public class Board {
    private int boardSize;
    private int[][] boardTiles;

    private int hammingDistance;
    private int manhattanDistance;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        boardSize = boardTiles.length;
        hammingDistance = 0;
        manhattanDistance = 0;

        boardTiles = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                boardTiles[i][j] = tiles[i][j];
                int currentX = getRow(boardTiles[i][j]);
                int currentY = getColumn(boardTiles[i][j]);
                if (Math.abs(i - currentX) + Math.abs(j - currentY) == 0) {
                    hammingDistance++;
                }
                manhattanDistance += Math.abs(i - currentX) + Math.abs(j - currentY);
            }
        }
    }

    private int getRow(int position) {
        if (position <= 0 || position > boardSize * boardSize) {
            throw new IllegalArgumentException();
        }

        return (position - 1) / boardSize;
    }

    private int getColumn(int position) {
        if (position < 0 || position >= boardSize * boardSize) {
            throw new IllegalArgumentException();
        }
        return (position - 1) % boardSize;
    }
                                           
    // string representation of this board
    public String toString() {
        String rep = "";
        rep += boardSize + "\n";
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                rep += boardTiles[i][j] + " ";
            }
            rep += "\n";
        }
        return rep;
    }

    // board dimension n
    public int dimension() {
        return boardSize;
    }

    // number of tiles out of place
    public int hamming() {
        return hammingDistance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hammingDistance == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y.getClass() != this.getClass()) {
            return false;
        }

        return toString().equals(y.toString());
    }

    private class boardIterator implements Iterable<Board> {
        
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
           
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {

    }

    // unit testing (not graded)
    public static void main(String[] args) {

    }
}